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

package ch.dvbern.stip.api.massendruck.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.massendruck.entity.DatenschutzbriefMassendruck;
import ch.dvbern.stip.api.massendruck.entity.MassendruckJob;
import ch.dvbern.stip.api.massendruck.entity.VerfuegungMassendruck;
import ch.dvbern.stip.generated.dto.MassendruckDatenschutzbriefDto;
import ch.dvbern.stip.generated.dto.MassendruckJobDetailDto;
import ch.dvbern.stip.generated.dto.MassendruckJobDto;
import ch.dvbern.stip.generated.dto.MassendruckVerfuegungDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class)
public interface MassendruckJobMapper {
    @Mapping(source = "status", target = "massendruckJobStatus")
    @Mapping(source = "massendruckTyp", target = "massendruckJobTyp")
    MassendruckJobDto toDto(MassendruckJob massendruckJob);

    @Mapping(source = "status", target = "massendruckJobStatus")
    @Mapping(source = "massendruckTyp", target = "massendruckJobTyp")
    @Mapping(
        source = "datenschutzbriefMassendrucks",
        target = "datenschutzbriefMassendrucks",
        qualifiedByName = "toMassendruckDatenschutzbriefDto"
    )
    @Mapping(
        source = "verfuegungMassendrucks",
        target = "verfuegungMassendrucks",
        qualifiedByName = "toMassendruckVerfuegungDto"
    )
    MassendruckJobDetailDto toDetailDto(MassendruckJob massendruckJob);

    @Named("toMassendruckDatenschutzbriefDto")
    default MassendruckDatenschutzbriefDto toMassendruckDatenschutzbriefDto(
        final DatenschutzbriefMassendruck datenschutzbriefMassendruck
    ) {
        final var datenschutzbrief = datenschutzbriefMassendruck.getDatenschutzbrief();
        final var gesuch = datenschutzbrief.getGesuch();

        return new MassendruckDatenschutzbriefDto()
            .id(datenschutzbriefMassendruck.getId())
            .vorname(datenschutzbrief.getVorname())
            .nachname(datenschutzbrief.getNachname())
            .elternTyp(datenschutzbrief.getDatenschutzbriefEmpfaenger())
            .isVersendet(datenschutzbrief.isVersendet())
            .gesuchNummer(gesuch.getGesuchNummer())
            .gesuchId(gesuch.getId());
    }

    @Named("toMassendruckVerfuegungDto")
    default MassendruckVerfuegungDto toMassendruckVerfuegungDto(final VerfuegungMassendruck verfuegungMassendruck) {
        final var verfuegung = verfuegungMassendruck.getVerfuegung();
        final var gesuch = verfuegung.getGesuch();

        return new MassendruckVerfuegungDto()
            .id(verfuegungMassendruck.getId())
            .vorname(verfuegungMassendruck.getVorname())
            .nachname(verfuegungMassendruck.getNachname())
            .isVersendet(verfuegung.isVersendet())
            .gesuchNummer(gesuch.getGesuchNummer())
            .gesuchId(gesuch.getId());
    }
}
