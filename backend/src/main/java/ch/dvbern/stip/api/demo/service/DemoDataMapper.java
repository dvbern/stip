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

package ch.dvbern.stip.api.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.demo.entity.DemoData;
import ch.dvbern.stip.api.demo.entity.DemoDataImport;
import ch.dvbern.stip.api.dokument.service.DokumentMapper;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.generated.dto.ApplyDemoDataResponseDto;
import ch.dvbern.stip.generated.dto.DemoDataListDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class, uses = DokumentMapper.class)
public abstract class DemoDataMapper {
    @Mapping(source = "demoDataImport.userErstellt", target = "importiertVon")
    @Mapping(source = "demoDataImport.timestampErstellt", target = "letzteAktivitaet")
    @Mapping(source = "demoDataImport.dokument.filename", target = "filename")
    @Mapping(source = "demoDataImport.dokument.filepath", target = "filepath")
    @Mapping(source = "demoDataImport.dokument.filesize", target = "filesize")
    @Mapping(source = "demoDataImport.dokument.id", target = "documentId")
    @Mapping(source = "demoDataList", target = "demoDatas")
    public abstract DemoDataListDto toDto(final DemoDataImport demoDataImport, final List<DemoData> demoDataList);

    @Mapping(source = "id", target = "gesuchId")
    @Mapping(source = ".", target = "gesuchTrancheId", qualifiedByName = "getCurrentGesuchTrancheId")
    @Mapping(source = ".", target = "gueltigAb", qualifiedByName = "getStartDate")
    @Mapping(source = ".", target = "gueltigBis", qualifiedByName = "getEndDate")
    public abstract ApplyDemoDataResponseDto toDto(final Gesuch gesuch);

    @Named("getCurrentGesuchTrancheId")
    UUID getCurrentGesuchTrancheId(Gesuch gesuch) {
        return gesuch.getLatestGesuchTranche().getId();
    }

    @Named("getStartDate")
    static LocalDate getStartDate(Gesuch gesuch) {
        return DateUtil.getGesuchDateRange(gesuch).getGueltigAb();
    }

    @Named("getEndDate")
    static LocalDate getEndDate(Gesuch gesuch) {
        return DateUtil.getGesuchDateRange(gesuch).getGueltigBis();
    }
}
