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

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.notification.entity.Notification;
import ch.dvbern.stip.api.notification.repo.NotificationRepository;
import ch.dvbern.stip.generated.dto.NotificationDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public Notification requireById(final UUID notificationId) {
        return notificationRepository.requireById(notificationId);
    }

    public List<NotificationDto> getNotificationsForFall(final UUID fallId) {
        return notificationRepository.getAllForFall(fallId).map(notificationMapper::toDto).toList();
    }

    public long getUnreadNotificationCountForFall(final UUID fallId) {
        return notificationRepository.getUnreadNotificationsCountForFall(fallId);
    }

    @Transactional
    public void markNotificationAsRead(final UUID notificationId) {
        notificationRepository.markNotificationAsRead(notificationId);
    }

    @Transactional
    public void deleteNotificationsForFall(final UUID fallId) {
        notificationRepository.deleteAllForFall(fallId);
    }
}
