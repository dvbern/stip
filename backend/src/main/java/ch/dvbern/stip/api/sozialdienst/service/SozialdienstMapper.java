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

package ch.dvbern.stip.api.sozialdienst.service;

import ch.dvbern.stip.api.auszahlung.service.ZahlungsverbindungMapper;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.sozialdienst.entity.Sozialdienst;
import ch.dvbern.stip.api.sozialdienstbenutzer.service.SozialdienstAdminMapper;
import ch.dvbern.stip.generated.dto.SozialdienstCreateDto;
import ch.dvbern.stip.generated.dto.SozialdienstDto;
import ch.dvbern.stip.generated.dto.SozialdienstSlimDto;
import ch.dvbern.stip.generated.dto.SozialdienstUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class, uses = { SozialdienstAdminMapper.class, ZahlungsverbindungMapper.class })
public interface SozialdienstMapper {
    Sozialdienst toEntity(SozialdienstCreateDto dto);

    SozialdienstDto toDto(Sozialdienst entity);

    SozialdienstSlimDto toSlimDto(Sozialdienst sozialdienst);

    Sozialdienst partialUpdate(SozialdienstUpdateDto dto, @MappingTarget Sozialdienst entity);
}
