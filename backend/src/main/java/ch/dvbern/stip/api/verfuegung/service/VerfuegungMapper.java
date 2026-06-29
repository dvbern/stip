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
import ch.dvbern.stip.api.verfuegung.type.VerfuegungDokumentTyp;
import ch.dvbern.stip.generated.dto.VerfuegungDokumentDto;
import ch.dvbern.stip.generated.dto.VerfuegungDokumentTypDto;
import ch.dvbern.stip.generated.dto.VerfuegungDto;
import ch.dvbern.stip.generated.dto.VerfuegungFallDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class, uses = { VerfuegungDokumentMapper.class })
public interface VerfuegungMapper {
    VerfuegungDokumentTyp RELEVANT_DOKUMENT_TYPE = VerfuegungDokumentTyp.VERFUEGUNGSBRIEF;

    VerfuegungDto toDto(final Verfuegung verfuegung);

    @Mapping(target = "yearRange", expression = "java(toYearRange(verfuegung))")
    @Mapping(target = "totalbetragStipendium", expression = "java(toTotalBetragStipendium(verfuegung))")
    @Mapping(target = "dokument", expression = "java(toRelevantDokument(verfuegung))")
    VerfuegungFallDto toFallDto(final Verfuegung verfuegung);

    default String toYearRange(final Verfuegung verfuegung) {
        if (
            verfuegung == null || verfuegung.getGesuch() == null || verfuegung.getGesuch().getGesuchsperiode() == null
        ) {
            return null;
        }

        final var gesuchsperiode = verfuegung.getGesuch().getGesuchsperiode();
        return DateUtil.getGesuchsPeriodeYearRange(gesuchsperiode);
    }

    default Integer toTotalBetragStipendium(final Verfuegung verfuegung) {
        if (verfuegung.getBerechnungJsonData() == null) {
            return 0;
        }
        final var berechnung = verfuegung.parseBerechnungData();
        return berechnung.getBerechnungStipendium() == null ? 0 : berechnung.getBerechnungStipendium();
    }

    default VerfuegungDokumentDto toRelevantDokument(final Verfuegung verfuegung) {
        final var relevantDokument = verfuegung.getDokumente()
            .stream()
            .filter(dokument -> dokument.getTyp() == RELEVANT_DOKUMENT_TYPE)
            .max(Comparator.comparing(VerfuegungDokument::getTimestampErstellt))
            .or(
                () -> verfuegung.getDokumente()
                    .stream()
                    .max(Comparator.comparing(VerfuegungDokument::getTimestampErstellt))
            )
            .orElse(null);

        if (relevantDokument == null) {
            return null;
        }

        final var verfuegungDokumentDto = new VerfuegungDokumentDto();
        verfuegungDokumentDto.setId(relevantDokument.getId());
        verfuegungDokumentDto.setFilename(relevantDokument.getFilename());
        verfuegungDokumentDto.setTyp(VerfuegungDokumentTypDto.valueOf(relevantDokument.getTyp().name()));
        return verfuegungDokumentDto;
    }
}
