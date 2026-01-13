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

import java.util.List;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.demo.entity.DemoData;
import ch.dvbern.stip.api.demo.entity.DemoDataImport;
import ch.dvbern.stip.api.dokument.service.DokumentMapper;
import ch.dvbern.stip.generated.dto.DemoDataListDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MappingConfig.class, uses = DokumentMapper.class)
public interface DemoDataMapper {
    @Mapping(source = "demoDataImport.userErstellt", target = "importiertVon")
    @Mapping(source = "demoDataImport.timestampErstellt", target = "letzteAktivitaet")
    @Mapping(source = "demoDataImport.dokument.filename", target = "filename")
    @Mapping(source = "demoDataImport.dokument.filepath", target = "filepath")
    @Mapping(source = "demoDataImport.dokument.filesize", target = "filesize")
    @Mapping(source = "demoDataImport.dokument.objectId", target = "objectId")
    @Mapping(source = "demoDataList", target = "demoDatas")
    DemoDataListDto toDto(final DemoDataImport demoDataImport, final List<DemoData> demoDataList);
}
