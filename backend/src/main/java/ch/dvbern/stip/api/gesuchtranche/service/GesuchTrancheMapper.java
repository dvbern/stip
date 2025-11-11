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

import ch.dvbern.stip.api.common.jahreswert.JahreswertUtil;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.eltern.service.ElternMapper;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.service.FamiliensituationMapper;
import ch.dvbern.stip.api.gesuchformular.service.GesuchFormularMapper;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.steuererklaerung.service.SteuererklaerungMapper;
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

    @Inject
    SteuererklaerungMapper steuererklaerungMapper;

    @Inject
    FamiliensituationMapper familiensituationMapper;

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
        qualifiedByName = "centralMappingWithOverrideIncomingElternteile"
    )
    public abstract GesuchTranche partialUpdateOverrideIncomingVersteckteEltern(
        GesuchTrancheUpdateDto gesuchUpdateDto,
        @MappingTarget GesuchTranche gesuch
    );

    @BeanMapping(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        qualifiedByName = "centralMappingWithKeepIncomingElternteile"
    )
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

    @Named("afterMappingWithoutVersteckteEltern")
    @AfterMapping
    protected void afterMappingWithoutVersteckteEltern(
        @MappingTarget GesuchTrancheDto gesuchTrancheDto,
        @Context GesuchTranche context
    ) {
        final var eltern = gesuchTrancheDto.getGesuchFormular().getElterns();
        final var versteckteEltern = context.getGesuchFormular().getVersteckteEltern();
        if (eltern != null) {
            eltern.removeIf(elternteil -> versteckteEltern.contains(elternteil.getElternTyp()));
        }

        final var steuererklaerungen = gesuchTrancheDto.getGesuchFormular().getSteuererklaerung();
        if (steuererklaerungen != null) {
            steuererklaerungen.removeIf(steuererklaerung -> switch (steuererklaerung.getSteuerdatenTyp()) {
                case MUTTER -> versteckteEltern.contains(ElternTyp.MUTTER);
                case VATER -> versteckteEltern.contains(ElternTyp.VATER);
                case FAMILIE -> !versteckteEltern.isEmpty();
            }
            );
        }
    }

    @Named("centralMappingWithOverrideIncomingElternteile")
    @BeforeMapping
    protected void centralBeforeMappingWithOverrideIncomingElternteile(
        final GesuchTrancheUpdateDto newTranche,
        final @MappingTarget GesuchTranche gesuchTranche
    ) {
        beforeMappingOverrideIncomingVersteckteEltern(newTranche, gesuchTranche);
    }

    @Named("centralMappingWithOverrideIncomingElternteile")
    @AfterMapping
    protected void centralAfterMappingWithOverrideIncomingElternteile(
        final GesuchTrancheUpdateDto newTranche,
        final @MappingTarget GesuchTranche gesuchTranche
    ) {
        synchroniseJahresfelder(newTranche, gesuchTranche);
    }

    @Named("centralMappingWithKeepIncomingElternteile")
    @AfterMapping
    protected void centralAfterMappingWithKeepIncomingElternteile(
        final GesuchTrancheUpdateDto newTranche,
        final @MappingTarget GesuchTranche gesuchTranche
    ) {
        synchroniseJahresfelder(newTranche, gesuchTranche);
    }

    protected void beforeMappingOverrideIncomingVersteckteEltern(
        final GesuchTrancheUpdateDto newTranche,
        final @MappingTarget GesuchTranche gesuchTranche
    ) {
        final var versteckteEltern = gesuchTranche.getGesuchFormular().getVersteckteEltern();
        if (versteckteEltern.isEmpty()) {
            return;
        }

        if (gesuchTranche.getTyp() == GesuchTrancheTyp.AENDERUNG) {
            // Override incoming Familiensituation for Aenderungen with versteckte Elternteile
            final var replacementFamiliensituation =
                familiensituationMapper.toUpdateDto(gesuchTranche.getGesuchFormular().getFamiliensituation());
            newTranche.getGesuchFormular().setFamiliensituation(replacementFamiliensituation);
        }

        if (gesuchTranche.getGesuchFormular().getElterns() == null) {
            return;
        }

        // Load and find Eltern to replace the incoming one(s) (i.e. ignoring incoming changes)
        final var replacementEltern = gesuchTranche.getGesuchFormular()
            .getElterns()
            .stream()
            .filter(elternteil -> versteckteEltern.contains(elternteil.getElternTyp()))
            .map(elternMapper::toUpdateDto)
            .toList();

        for (final var replacement : replacementEltern) {
            final var newFormular = newTranche.getGesuchFormular();
            newFormular.getElterns()
                .removeIf(eltern -> eltern.getElternTyp() == replacement.getElternTyp());

            newFormular.getElterns().add(replacement);
        }

        // Load and find Steuererklaerungen to replace the incoming one(s)
        final var replacementSteuererklaerungen = gesuchTranche.getGesuchFormular()
            .getSteuererklaerung()
            .stream()
            .filter(steuererklaerung -> switch (steuererklaerung.getSteuerdatenTyp()) {
                case null -> false;
                case MUTTER -> versteckteEltern.contains(ElternTyp.MUTTER);
                case VATER -> versteckteEltern.contains(ElternTyp.VATER);
                case FAMILIE -> versteckteEltern.isEmpty();
            }
            )
            .map(steuererklaerungMapper::toUpdateDto)
            .toList();

        for (final var replacementSteuererklaerung : replacementSteuererklaerungen) {
            final var newFormular = newTranche.getGesuchFormular();
            newFormular.getSteuererklaerung()
                .removeIf(
                    steuererklaerung -> steuererklaerung.getSteuerdatenTyp() == replacementSteuererklaerung
                        .getSteuerdatenTyp()
                );

            newFormular.getSteuererklaerung().add(replacementSteuererklaerung);
        }
    }

    protected void synchroniseJahresfelder(
        final GesuchTrancheUpdateDto newTranche,
        final @MappingTarget GesuchTranche gesuchTranche
    ) {
        JahreswertUtil.synchroniseJahreswerte(gesuchTranche);
    }
}
