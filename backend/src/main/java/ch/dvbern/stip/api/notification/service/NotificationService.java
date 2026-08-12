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
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.api.delegieren.entity.PersoenlicheAngaben;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.notification.entity.Notification;
import ch.dvbern.stip.api.notification.repo.NotificationRepository;
import ch.dvbern.stip.api.notification.type.NotificationType;
import ch.dvbern.stip.generated.dto.NotificationDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final MailService mailService;
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

    void createNotificationAndSendStdMail(
        final NotificationType notificationType,
        final Gesuch gesuch,
        final String msg
    ) {
        createNotificationAndSendStdMail(notificationType, gesuch, msg, Optional.empty(), Optional.empty());
    }

    void createNotificationAndSendStdMail(
        final NotificationType notificationType,
        final Gesuch gesuch,
        final String msg,
        final Optional<String> absender,
        final Optional<UUID> contextId
    ) {
        final Notification notification = new Notification()
            .setNotificationType(notificationType)
            .setFall(gesuch.getAusbildung().getFall())
            .setNotificationText(msg);

        if (contextId.isPresent()) {
            notification.setContextId(contextId.get());
        }

        if (absender.isPresent()) {
            NotificationUtil.setAbsender(absender.get(), notification);
        } else {
            NotificationUtil.setAbsender(gesuch, notification);
        }

        notificationRepository.persistAndFlush(notification);
        mailService.sendStandardNotificationEmailForGesuch(gesuch);
    }

    void createNotificationAndSendStdMail(
        final NotificationType notificationType,
        final Fall fall,
        final String absender,
        final PersoenlicheAngaben persoenlicheAngaben,
        final String msg
    ) {
        final Notification notification = new Notification()
            .setNotificationType(notificationType)
            .setFall(fall)
            .setNotificationText(msg);
        NotificationUtil.setAbsender(absender, notification);

        notificationRepository.persistAndFlush(notification);
        mailService.sendStandardNotificationEmailForFall(persoenlicheAngaben, fall);
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
