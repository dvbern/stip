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

package ch.dvbern.stip.api.personinausbildung.service;

import java.util.UUID;

import ch.dvbern.stip.api.adresse.service.AdresseMapper;
import ch.dvbern.stip.api.common.service.EntityUpdateMapper;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.land.entity.Land;
import ch.dvbern.stip.api.land.service.LandService;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.generated.dto.PersonInAusbildungDto;
import ch.dvbern.stip.generated.dto.PersonInAusbildungUpdateDto;
import jakarta.inject.Inject;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class, uses = AdresseMapper.class)
public abstract class PersonInAusbildungMapper
extends EntityUpdateMapper<PersonInAusbildungUpdateDto, PersonInAusbildung> {
    @Inject
    LandService landService;

    @Mapping(source = "nationalitaetId", target = "nationalitaet", qualifiedByName = "mapNationalitaet")
    public abstract PersonInAusbildung toEntity(PersonInAusbildungDto personInAusbildungDto);

    @Mapping(source = "nationalitaet.id", target = "nationalitaetId")
    public abstract PersonInAusbildungDto toDto(PersonInAusbildung personInAusbildung);

    @Mapping(source = "nationalitaetId", target = "nationalitaet", qualifiedByName = "mapNationalitaet")
    public abstract PersonInAusbildung partialUpdate(
        PersonInAusbildungUpdateDto personInAusbildungUpdateDto,
        @MappingTarget PersonInAusbildung personInAusbildung
    );

    @Mapping(source = "nationalitaet.id", target = "nationalitaetId")
    public abstract PersonInAusbildungUpdateDto toUpdateDto(PersonInAusbildung personInAusbildung);

    @Override
    @BeforeMapping
    protected void resetDependentDataBeforeUpdate(
        final PersonInAusbildungUpdateDto newFormular,
        final @MappingTarget PersonInAusbildung targetFormular
    ) {
        resetFieldIf(
            () -> Boolean.TRUE.equals(newFormular.getIdentischerZivilrechtlicherWohnsitz()),
            "Reset IzW PLZ and Ort because IzW is true",
            () -> {
                newFormular.setIdentischerZivilrechtlicherWohnsitzOrt(null);
                newFormular.setIdentischerZivilrechtlicherWohnsitzPLZ(null);
            }
        );
    }

    @Named("mapNationalitaet")
    public Land mapNationalitaet(UUID landId) {
        return landService.requireLandById(landId);
    }
}
