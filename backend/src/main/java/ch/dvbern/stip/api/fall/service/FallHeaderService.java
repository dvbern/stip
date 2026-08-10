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

package ch.dvbern.stip.api.fall.service;

import java.util.UUID;

import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.notification.service.NotificationService;
import ch.dvbern.stip.generated.dto.FallHeaderDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class FallHeaderService {
    private final FallRepository fallRepository;
    private final NotificationService notificationService;

    @Transactional
    public FallHeaderDto getFallHeader(final UUID fallId) {
        fallRepository.requireById(fallId);
        final var unreadNotificationsCount = notificationService.getUnreadNotificationCountForFall(fallId);

        return new FallHeaderDto()
            .fallId(fallId)
            .unreadNotificationsCount(Math.toIntExact(unreadNotificationsCount));
    }
}
