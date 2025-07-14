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

package ch.dvbern.stip.api.benutzer.service;

import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.benutzer.entity.Sachbearbeiter;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.generated.dto.SachbearbeiterDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterUpdateDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MappingConfig.class)
public interface SachbearbeiterMapper {
    Sachbearbeiter toEntity(SachbearbeiterUpdateDto sachbearbeiterUpdateDto);

    @Mapping(source = "rollen", target = "sachbearbeiterRollen")
    SachbearbeiterDto toDto(Sachbearbeiter sachbearbeiter);

    default String map(Rolle role) {
        return role.getKeycloakIdentifier();
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Sachbearbeiter partialUpdate(
        SachbearbeiterUpdateDto sachbearbeiterUpdateDto,
        @MappingTarget Sachbearbeiter sachbearbeiter
    );

}
