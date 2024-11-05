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

package ch.dvbern.stip.api.ausbildung.service;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.bildungskategorie.service.BildungskategorieMapper;
import ch.dvbern.stip.api.common.service.EntityIdReference;
import ch.dvbern.stip.api.common.service.EntityReferenceMapper;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.AusbildungsgangCreateDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangDto;
import ch.dvbern.stip.generated.dto.AusbildungsgangUpdateDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MappingConfig.class, uses = BildungskategorieMapper.class)
public interface AusbildungsgangMapper {
    @Mapping(
        target = "bildungskategorie",
        source = "bildungskategorieId",
        qualifiedBy = { EntityReferenceMapper.class, EntityIdReference.class }
    )
    Ausbildungsgang toEntity(AusbildungsgangCreateDto ausbildungsgangDto);

    @Mapping(
        target = "ausbildungsstaetteId",
        source = "ausbildungsstaette.id"
    )
    AusbildungsgangDto toDto(Ausbildungsgang ausbildungsgang);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(
        target = "bildungskategorie",
        source = "bildungskategorieId",
        qualifiedBy = { EntityReferenceMapper.class, EntityIdReference.class }
    )
    Ausbildungsgang partialUpdate(
        AusbildungsgangUpdateDto ausbildungsgangDto,
        @MappingTarget Ausbildungsgang ausbildungsgang
    );
}
