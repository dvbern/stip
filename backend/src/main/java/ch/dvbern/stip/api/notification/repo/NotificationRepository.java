package ch.dvbern.stip.api.notification.repo;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.notification.entity.Notification;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class NotificationRepository implements BaseRepository<Notification> {
}
