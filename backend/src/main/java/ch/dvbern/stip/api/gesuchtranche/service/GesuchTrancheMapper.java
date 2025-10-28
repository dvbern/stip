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

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.gesuchformular.service.GesuchFormularMapper;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.generated.dto.GesuchTrancheDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheListDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheUpdateDto;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.envers.DefaultRevisionEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.Qualifier;

@Mapper(
    config = MappingConfig.class,
    uses = GesuchFormularMapper.class
)
public interface GesuchTrancheMapper {

    @Mapping(source = "gueltigAb", target = "gueltigkeit.gueltigAb")
    @Mapping(source = "gueltigBis", target = "gueltigkeit.gueltigBis")
    GesuchTranche toEntity(GesuchTrancheDto gesuchTrancheDto);

    @ToDtoDefaultMapping
    @BeanMapping(qualifiedBy = WithVersteckteEltern.class)
    GesuchTrancheDto toDtoWithVersteckteEltern(GesuchTranche gesuch, @Context GesuchTranche context);

    default GesuchTrancheDto toDtoWithVersteckteEltern(GesuchTranche gesuch) {
        return toDtoWithVersteckteEltern(gesuch, gesuch);
    }

    @ToDtoDefaultMapping
    @BeanMapping(qualifiedBy = WithoutVersteckteEltern.class)
    GesuchTrancheDto toDtoWithoutVersteckteEltern(GesuchTranche gesuch, @Context GesuchTranche context);

    default GesuchTrancheDto toDtoWithoutVersteckteEltern(GesuchTranche gesuch) {
        return toDtoWithoutVersteckteEltern(gesuch, gesuch);
    }

    @Named("toSlimDto")
    @Mapping(source = "gueltigkeit.gueltigAb", target = "gueltigAb")
    @Mapping(source = "gueltigkeit.gueltigBis", target = "gueltigBis")
    GesuchTrancheSlimDto toSlimDto(GesuchTranche gesuchTranche);

    @Named("toSlimDtoWithRevision")
    default List<GesuchTrancheSlimDto> toSlimDtoWithRevision(List<Pair<GesuchTranche, DefaultRevisionEntity>> value) {
        return value.stream()
            .map(pair -> toSlimDto(pair.getLeft()).revision(pair.getRight().getId()))
            .toList();
    }

    @Mapping(target = "tranchen", qualifiedByName = "toSlimDto")
    @Mapping(target = "initialTranchen", qualifiedByName = "toSlimDto")
    @Mapping(target = "aenderungen", qualifiedByName = "toSlimDto")
    @Mapping(target = "abgelehnteAenderungen", qualifiedByName = "toSlimDtoWithRevision")
    GesuchTrancheListDto toListDto(
        List<GesuchTranche> tranchen,
        List<GesuchTranche> initialTranchen,
        List<GesuchTranche> aenderungen,
        List<Pair<GesuchTranche, DefaultRevisionEntity>> abgelehnteAenderungen
    );

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    GesuchTranche partialUpdate(GesuchTrancheUpdateDto gesuchUpdateDto, @MappingTarget GesuchTranche gesuch);

    @Mapping(source = "gueltigkeit.gueltigAb", target = "gueltigAb")
    @Mapping(source = "gueltigkeit.gueltigBis", target = "gueltigBis")
    @interface ToDtoDefaultMapping {
    }

    @Qualifier
    @Retention(RetentionPolicy.CLASS)
    @interface WithVersteckteEltern {
    }

    @Qualifier
    @Retention(RetentionPolicy.CLASS)
    @interface WithoutVersteckteEltern {
    }

    @WithVersteckteEltern
    @AfterMapping
    default void afterMappingWithVersteckteEltern(
        @MappingTarget GesuchTrancheDto gesuchTrancheDto,
        @Context GesuchTranche context
    ) {
        // Do nothing, this is just for the sake of symmetry
        ;
    }

    @WithoutVersteckteEltern
    @AfterMapping
    default void afterMappingWithoutVersteckteEltern(
        @MappingTarget GesuchTrancheDto gesuchTrancheDto,
        @Context GesuchTranche context
    ) {
        final var versteckteEltern = context.getGesuchFormular().getVersteckteEltern();
        final var eltern = gesuchTrancheDto.getGesuchFormular().getElterns();
        eltern.removeIf(elternteil -> versteckteEltern.contains(elternteil.getElternTyp()));
    }
}
