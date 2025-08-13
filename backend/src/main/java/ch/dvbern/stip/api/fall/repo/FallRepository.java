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

package ch.dvbern.stip.api.fall.repo;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.fall.entity.QFall;
import ch.dvbern.stip.api.zuordnung.entity.QZuordnung;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class FallRepository implements BaseRepository<Fall> {
    private final EntityManager entityManager;

    private static final QFall Q_FALL = QFall.fall;

    public Stream<Fall> findFaelleForSb(UUID sachbearbeiterId) {
        final var queryFactory = new JPAQueryFactory(entityManager);

        final var zuordnung = QZuordnung.zuordnung;

        final var query = queryFactory
            .selectFrom(Q_FALL)
            .where(
                JPAExpressions
                    .select(zuordnung.sachbearbeiter.id)
                    .from(zuordnung)
                    .where(zuordnung.sachbearbeiter.id.eq(sachbearbeiterId))
                    .contains(sachbearbeiterId)
            );

        return query.stream();
    }

    public Optional<Fall> findFallForGsOptional(final UUID gesuchstellerId) {
        return find("gesuchsteller.id", gesuchstellerId).firstResultOptional();
    }

    public Stream<Fall> findAllFallsWithFailedAuszahlungBuchhaltung(final Integer page, final Integer pageSize) {
        return new JPAQueryFactory(entityManager)
            .selectFrom(Q_FALL)
            .where(Q_FALL.failedBuchhaltungAuszahlungType.isNotNull())
            .offset((long) page * pageSize)
            .limit(pageSize)
            .stream();
    }
}
