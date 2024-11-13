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

package ch.dvbern.stip.api.notification.repo;

import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.ausbildung.entity.QAusbildung;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.fall.entity.QFall;
import ch.dvbern.stip.api.gesuch.entity.QGesuch;
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

    public Stream<Notification> getAllForUser(final UUID userId) {
        final var notification = QNotification.notification;
        final var gesuch = QGesuch.gesuch;
        final var ausbildung = QAusbildung.ausbildung;
        final var fall = QFall.fall;

        final var queryFactory = new JPAQueryFactory(entityManager);

        return queryFactory
            .selectFrom(notification)
            .join(gesuch)
            .on(notification.gesuch.id.eq(gesuch.id))
            .join(ausbildung)
            .on(gesuch.ausbildung.id.eq(ausbildung.id))
            .join(fall)
            .on(ausbildung.fall.id.eq(fall.id))
            .where(fall.gesuchsteller.id.eq(userId))
            .orderBy(notification.timestampErstellt.desc())
            .stream();
    }
}
