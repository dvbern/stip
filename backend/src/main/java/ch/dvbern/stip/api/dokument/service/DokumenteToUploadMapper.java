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
import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp;
import ch.dvbern.stip.generated.dto.DokumenteToUploadDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentRefDto;
import org.apache.commons.lang3.tuple.Pair;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class, uses = { CustomDocumentTypMapper.class })
public abstract class DokumenteToUploadMapper {
    public abstract DokumenteToUploadDto toDto(
        final List<DokumentTyp> required,
        final List<Pair<DokumentTyp, UUID>> requiredRefs,
        final List<UnterschriftenblattDokumentTyp> unterschriftenblaetter,
        final List<CustomDokumentTyp> customDokumentTyps
    );

    public GesuchDokumentRefDto toDto(final Pair<DokumentTyp, UUID> pair) {
        return new GesuchDokumentRefDto()
            .dokumentTyp(pair.getLeft())
            .entryId(pair.getRight());
    }

    @AfterMapping
    protected void setNullToEmptyList(
        @MappingTarget final DokumenteToUploadDto dokumenteToUploadDto
    ) {
        if (Objects.isNull(dokumenteToUploadDto.getRequired())) {
            dokumenteToUploadDto.setRequired(new ArrayList<>());
        }

        if (Objects.isNull(dokumenteToUploadDto.getRequiredRefs())) {
            dokumenteToUploadDto.setRequiredRefs(new ArrayList<>());
        }

        if (Objects.isNull(dokumenteToUploadDto.getUnterschriftenblaetter())) {
            dokumenteToUploadDto.setUnterschriftenblaetter(new ArrayList<>());
        }

        if (Objects.isNull(dokumenteToUploadDto.getCustomDokumentTyps())) {
            dokumenteToUploadDto.setCustomDokumentTyps(new ArrayList<>());
        }
    }
}
