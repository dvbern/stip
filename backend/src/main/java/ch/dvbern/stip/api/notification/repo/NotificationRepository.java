package ch.dvbern.stip.api.notification.repo;

import java.util.UUID;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.notification.entity.Notification;
import ch.dvbern.stip.api.notification.entity.QNotification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class NotificationRepository implements BaseRepository<Notification> {
    private final EntityManager entityManager;

    public void deleteAllForGesuch(final UUID gesuchId) {
        final var notification = QNotification.notification;

        new JPAQueryFactory(entityManager)
            .delete(notification)
            .where(notification.gesuch.id.eq(gesuchId))
            .execute();
    }
}
