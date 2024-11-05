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

package ch.dvbern.stip.api.notification.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.notification.entity.Notification;
import ch.dvbern.stip.generated.dto.NotificationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class)
public interface NotificationMapper {
    Notification toEntity(NotificationDto notificationDto);

    @Mapping(source = "gesuch.id", target = "gesuchId")
    NotificationDto toDto(Notification notification);

    Notification partialUpdate(NotificationDto notificationDto, @MappingTarget Notification notification);
}
