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

import java.util.UUID;

import ch.dvbern.stip.api.common.service.EntityIdReference;
import ch.dvbern.stip.api.common.service.EntityReferenceMapper;
import ch.dvbern.stip.api.common.service.IdOfReferenceEntity;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.dokument.entity.SachbearbeiterGesuchDokument;
import ch.dvbern.stip.generated.dto.SachbearbeiterGesuchDokumentCreateDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterGesuchDokumentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    config = MappingConfig.class,
    uses = DokumentMapper.class
)
public interface SachbearbeiterGesuchDokumentMapper {
    @Mapping(
        source = "gesuchId",
        target = "gesuch",
        qualifiedBy = { EntityReferenceMapper.class, EntityIdReference.class }
    )
    SachbearbeiterGesuchDokument toEntity(
        final UUID gesuchId,
        final SachbearbeiterGesuchDokumentCreateDto sachbearbeiterGesuchDokumentCreateDto
    );

    @Mapping(
        source = "gesuch", target = "gesuchId", qualifiedBy = { EntityReferenceMapper.class, IdOfReferenceEntity.class }
    )
    SachbearbeiterGesuchDokumentDto toDto(final SachbearbeiterGesuchDokument sachbearbeiterGesuchDokument);
}
