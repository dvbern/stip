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

package ch.dvbern.stip.api.gesuchsperioden.service;

import ch.dvbern.stip.api.common.service.EntityIdReference;
import ch.dvbern.stip.api.common.service.EntityReferenceMapper;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.generated.dto.GesuchsperiodeCreateDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeWithDatenDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MappingConfig.class)
public interface GesuchsperiodeMapper {
    @Mapping(source = "gesuchsjahrId", target = "gesuchsjahr.id")
    Gesuchsperiode toEntity(GesuchsperiodeCreateDto gesuchsperiodeDto);

    GesuchsperiodeDto toDto(Gesuchsperiode gesuchsperiode);

    @Mapping(source = "gesuchsjahr.id", target = "gesuchsjahrId")
    GesuchsperiodeWithDatenDto toDatenDto(Gesuchsperiode gesuchsperiode);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(
        source = "gesuchsjahrId",
        target = "gesuchsjahr",
        qualifiedBy = { EntityReferenceMapper.class, EntityIdReference.class }
    )
    Gesuchsperiode partialUpdate(
        GesuchsperiodeUpdateDto gesuchsperiodeDto,
        @MappingTarget Gesuchsperiode gesuchsperiode
    );
}
