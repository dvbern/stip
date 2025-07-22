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

package ch.dvbern.stip.api.buchhaltung.repo;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.buchhaltung.entity.Buchhaltung;
import ch.dvbern.stip.api.buchhaltung.entity.QBuchhaltung;
import ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType;
import ch.dvbern.stip.api.buchhaltung.type.SapStatus;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.sap.entity.QSapDelivery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class BuchhaltungRepository implements BaseRepository<Buchhaltung> {
    private final EntityManager entityManager;
    static final QBuchhaltung Q_BUCHHALTUNG = QBuchhaltung.buchhaltung;

    public Stream<Buchhaltung> findAllForFallId(final UUID fallId) {
        final var queryFactory = new JPAQueryFactory(entityManager);

        final var query = queryFactory
            .selectFrom(Q_BUCHHALTUNG)
            .where(Q_BUCHHALTUNG.fall.id.eq(fallId))
            .orderBy(Q_BUCHHALTUNG.timestampErstellt.asc());
        return query.stream();
    }

    public Stream<Buchhaltung> findStipendiumsEntrysForGesuch(final UUID gesuchId) {
        return findEntrysOfTypeForGesuch(gesuchId, BuchhaltungType.STIPENDIUM);
    }

    public Optional<Buchhaltung> findPendingBuchhaltungEntryOfGesuch(
        final UUID fallId,
        final BuchhaltungType buchhaltungType
    ) {
        final var queryFactory = new JPAQueryFactory(entityManager);
        final var sapDelivery = QSapDelivery.sapDelivery;

        final var query = queryFactory
            .selectFrom(Q_BUCHHALTUNG)
            .where(Q_BUCHHALTUNG.fall.id.eq(fallId))
            .where(Q_BUCHHALTUNG.buchhaltungType.eq(buchhaltungType))
            .join(sapDelivery)
            .on(Q_BUCHHALTUNG.sapDeliverys.any().id.eq(sapDelivery.id))
            .where(sapDelivery.sapStatus.eq(SapStatus.IN_PROGRESS))
            .orderBy(Q_BUCHHALTUNG.timestampErstellt.desc());
        return query.stream().findFirst();
    }

    public Stream<Buchhaltung> findEntrysOfTypeForGesuch(final UUID gesuchId, final BuchhaltungType buchhaltungType) {
        final var queryFactory = new JPAQueryFactory(entityManager);

        final var query = queryFactory
            .selectFrom(Q_BUCHHALTUNG)
            .where(Q_BUCHHALTUNG.gesuch.id.eq(gesuchId))
            .where(Q_BUCHHALTUNG.buchhaltungType.eq(buchhaltungType))
            .orderBy(Q_BUCHHALTUNG.timestampErstellt.asc());
        return query.stream();
    }

    public Stream<Buchhaltung> findBuchhaltungWithPendingSapDelivery() {
        return new JPAQueryFactory(entityManager)
            .selectFrom(Q_BUCHHALTUNG)
            .where(Q_BUCHHALTUNG.sapDeliverys.any().sapStatus.eq(SapStatus.IN_PROGRESS))
            // .where(Q_BUCHHALTUNG.sapDelivery.sapStatus.eq(SapStatus.IN_PROGRESS))
            .stream();
    }
}
