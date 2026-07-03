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

package ch.dvbern.stip.api.verfuegung.service;

import java.util.Comparator;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.verfuegung.entity.Verfuegung;
import ch.dvbern.stip.api.verfuegung.entity.VerfuegungDokument;
import ch.dvbern.stip.generated.dto.VerfuegungDokumentDto;
import ch.dvbern.stip.generated.dto.VerfuegungDokumentTypDto;
import ch.dvbern.stip.generated.dto.VerfuegungDto;
import ch.dvbern.stip.generated.dto.VerfuegungFallDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static ch.dvbern.stip.api.verfuegung.type.VerfuegungDokumentTyp.VERFUEGUNG_DOKUMENT_TYPS_WITHOUT_BERECHNUNG;

@Mapper(config = MappingConfig.class, uses = { VerfuegungDokumentMapper.class })
public abstract class VerfuegungMapper {
    public abstract VerfuegungDto toDto(final Verfuegung verfuegung);

    @Mapping(source = ".", target = "yearRange", qualifiedByName = "mapYearRangeOfAttachedGesuchsperiode")
    @Mapping(source = ".", target = "totalbetragStipendium", qualifiedByName = "mapTotalBetragStipendium")
    @Mapping(source = ".", target = "dokument", qualifiedByName = "mapVerfuegungsDokumentWithoutBerechnung")
    public abstract VerfuegungFallDto toFallDto(final Verfuegung verfuegung);

    @Named("mapYearRangeOfAttachedGesuchsperiode")
    String mapYearRangeOfAttachedGesuchsperiode(final Verfuegung verfuegung) {
        final var gesuchsperiode = verfuegung.getGesuch().getGesuchsperiode();
        return DateUtil.getGesuchsPeriodeYearRange(gesuchsperiode);
    }

    @Named("mapTotalBetragStipendium")
    Integer mapTotalBetragStipendium(final Verfuegung verfuegung) {
        if (verfuegung.getBerechnungJsonData() == null) {
            return 0;
        }
        final var berechnung = verfuegung.parseBerechnungData();
        return berechnung.getBerechnungStipendium() == null ? 0 : berechnung.getBerechnungStipendium();
    }

    @Named("mapVerfuegungsDokumentWithoutBerechnung")
    VerfuegungDokumentDto mapVerfuegungsDokumentWithoutBerechnung(final Verfuegung verfuegung) {
        final var relevantDokumentOpt = verfuegung.getDokumente()
            .stream()
            .filter(
                dokument -> VERFUEGUNG_DOKUMENT_TYPS_WITHOUT_BERECHNUNG.contains(dokument.getTyp())
            )
            .max(Comparator.comparing(VerfuegungDokument::getTimestampErstellt));

        if (relevantDokumentOpt.isEmpty()) {
            return null;
        }

        final var relevantDokument = relevantDokumentOpt.get();

        final var verfuegungDokumentDto = new VerfuegungDokumentDto();
        verfuegungDokumentDto.setId(relevantDokument.getId());
        verfuegungDokumentDto.setFilename(relevantDokument.getFilename());
        verfuegungDokumentDto.setTyp(VerfuegungDokumentTypDto.valueOf(relevantDokument.getTyp().name()));
        return verfuegungDokumentDto;
    }
}
