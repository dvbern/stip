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

package ch.dvbern.stip.api.dokument.service;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp;
import ch.dvbern.stip.generated.dto.DokumenteToUploadDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public abstract class DokumenteToUploadMapper {
    public abstract DokumenteToUploadDto toDto(
        final List<DokumentTyp> required,
        final List<UnterschriftenblattDokumentTyp> unterschriftenblaetter
    );

    @AfterMapping
    protected void setNullToEmptyList(
        @MappingTarget final DokumenteToUploadDto dokumenteToUploadDto
    ) {
        if (dokumenteToUploadDto.getRequired() == null) {
            dokumenteToUploadDto.setRequired(new ArrayList<>());
        }

        if (dokumenteToUploadDto.getUnterschriftenblaetter() == null) {
            dokumenteToUploadDto.setUnterschriftenblaetter(new ArrayList<>());
        }
    }
}
