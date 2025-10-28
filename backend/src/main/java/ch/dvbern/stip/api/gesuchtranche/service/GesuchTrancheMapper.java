/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.gesuchtranche.service;

import java.util.List;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.eltern.service.ElternMapper;
import ch.dvbern.stip.api.gesuchformular.service.GesuchFormularMapper;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.generated.dto.GesuchTrancheDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheListDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheUpdateDto;
import jakarta.inject.Inject;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.envers.DefaultRevisionEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    config = MappingConfig.class,
    uses = GesuchFormularMapper.class
)
public abstract class GesuchTrancheMapper {
    @Mapping(source = "gueltigkeit.gueltigAb", target = "gueltigAb")
    @Mapping(source = "gueltigkeit.gueltigBis", target = "gueltigBis")
    @interface ToDtoDefaultMapping {
    }

    @Inject
    ElternMapper elternMapper;

    @ToDtoDefaultMapping
    public abstract GesuchTrancheDto toDtoWithVersteckteEltern(GesuchTranche gesuch, @Context GesuchTranche context);

    public GesuchTrancheDto toDtoWithVersteckteEltern(GesuchTranche gesuch) {
        return toDtoWithVersteckteEltern(gesuch, gesuch);
    }

    @ToDtoDefaultMapping
    @BeanMapping(qualifiedByName = "afterMappingWithoutVersteckteEltern")
    public abstract GesuchTrancheDto toDtoWithoutVersteckteEltern(GesuchTranche gesuch, @Context GesuchTranche context);

    public GesuchTrancheDto toDtoWithoutVersteckteEltern(GesuchTranche gesuch) {
        return toDtoWithoutVersteckteEltern(gesuch, gesuch);
    }

    @Named("toSlimDto")
    @ToDtoDefaultMapping
    public abstract GesuchTrancheSlimDto toSlimDto(GesuchTranche gesuchTranche);

    @Named("toSlimDtoWithRevision")
    public List<GesuchTrancheSlimDto> toSlimDtoWithRevision(List<Pair<GesuchTranche, DefaultRevisionEntity>> value) {
        return value.stream()
            .map(pair -> toSlimDto(pair.getLeft()).revision(pair.getRight().getId()))
            .toList();
    }

    @Mapping(target = "tranchen", qualifiedByName = "toSlimDto")
    @Mapping(target = "initialTranchen", qualifiedByName = "toSlimDto")
    @Mapping(target = "aenderungen", qualifiedByName = "toSlimDto")
    @Mapping(target = "abgelehnteAenderungen", qualifiedByName = "toSlimDtoWithRevision")
    public abstract GesuchTrancheListDto toListDto(
        List<GesuchTranche> tranchen,
        List<GesuchTranche> initialTranchen,
        List<GesuchTranche> aenderungen,
        List<Pair<GesuchTranche, DefaultRevisionEntity>> abgelehnteAenderungen
    );

    @BeanMapping(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        qualifiedByName = "beforeMappingOverrideIncomingVersteckteEltern"
    )
    public abstract GesuchTranche partialUpdateOverrideIncomingVersteckteEltern(
        GesuchTrancheUpdateDto gesuchUpdateDto,
        @MappingTarget GesuchTranche gesuch
    );

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract GesuchTranche partialUpdateAcceptIncomingVersteckteEltern(
        GesuchTrancheUpdateDto gesuchUpdateDto,
        @MappingTarget GesuchTranche gesuch
    );

    public GesuchTranche partialUpdate(
        final GesuchTrancheUpdateDto gesuchUpdateDto,
        final GesuchTranche gesuch,
        final boolean overrideIncomingVersteckteEltern
    ) {
        if (overrideIncomingVersteckteEltern) {
            return partialUpdateOverrideIncomingVersteckteEltern(gesuchUpdateDto, gesuch);
        } else {
            return partialUpdateAcceptIncomingVersteckteEltern(gesuchUpdateDto, gesuch);
        }
    }

    // TODO KSTIP-2784: Move this into GesuchFormularMapper?
    @Named("afterMappingWithoutVersteckteEltern")
    @AfterMapping
    protected void afterMappingWithoutVersteckteEltern(
        @MappingTarget GesuchTrancheDto gesuchTrancheDto,
        @Context GesuchTranche context
    ) {
        final var eltern = gesuchTrancheDto.getGesuchFormular().getElterns();
        if (eltern == null) {
            return;
        }

        final var versteckteEltern = context.getGesuchFormular().getVersteckteEltern();
        eltern.removeIf(elternteil -> versteckteEltern.contains(elternteil.getElternTyp()));
    }

    @Named("beforeMappingOverrideIncomingVersteckteEltern")
    @BeforeMapping
    protected void beforeMappingOverrideIncomingVersteckteEltern(
        final GesuchTrancheUpdateDto newTranche,
        final @MappingTarget GesuchTranche gesuchTranche
    ) {
        if (newTranche.getGesuchFormular().getElterns() == null) {
            return;
        }

        final var versteckteEltern = gesuchTranche.getGesuchFormular().getVersteckteEltern();
        final var replacements = gesuchTranche.getGesuchFormular()
            .getElterns()
            .stream()
            .filter(elternteil -> versteckteEltern.contains(elternteil.getElternTyp()))
            .map(elternMapper::toUpdateDto)
            .toList();

        for (final var replacement : replacements) {
            final var newFormular = newTranche.getGesuchFormular();
            newFormular.getElterns()
                .removeIf(eltern -> eltern.getElternTyp() == replacement.getElternTyp());

            newFormular.getElterns().add(replacement);
        }

        newTranche.getGesuchFormular().getElterns().addAll(replacements);
    }
}
