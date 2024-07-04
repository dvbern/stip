package ch.dvbern.stip.api.notification.service;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.notification.entity.Notification;
import ch.dvbern.stip.api.notification.repo.NotificationRepository;
import ch.dvbern.stip.api.notification.type.NotificationType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Transactional
    public void createNotification(NotificationType notificationType, Gesuch gesuch) {
        Notification notification = new Notification()
            .setNotificationType(notificationType)
            .setGesuch(gesuch);

        notificationRepository.persistAndFlush(notification);
    }
}
