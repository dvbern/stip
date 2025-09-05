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

package ch.dvbern.stip.api.gesuch.service;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.delegieren.service.DelegierungMapper;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.generated.dto.FallDashboardItemDto;
import jakarta.inject.Inject;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class, uses = { AusbildungDashboardItemMapper.class, DelegierungMapper.class })
public abstract class FallDashboardItemMapper {
    @Inject
    BenutzerService benutzerService;

    @Mapping(source = "ausbildungs", target = "ausbildungDashboardItems")
    @Mapping(source = ".", target = "fall")
    public abstract FallDashboardItemDto toDto(final Fall fall);

    @AfterMapping
    protected void setNotifications(
        final Fall entity,
        @MappingTarget final FallDashboardItemDto dto
    ) {
        dto.setNotifications(benutzerService.getNotificationsForUser(entity.getGesuchsteller().getId()));
    }

}
