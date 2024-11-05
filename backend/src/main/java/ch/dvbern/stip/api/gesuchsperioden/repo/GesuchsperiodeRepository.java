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

package ch.dvbern.stip.api.gesuchsperioden.repo;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchsperioden.entity.QGesuchsperiode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchsperiodeRepository implements BaseRepository<Gesuchsperiode> {
    private final EntityManager entityManager;

    public Stream<Gesuchsperiode> findAllActiveForDate(LocalDate date) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var gesuchsperiode = QGesuchsperiode.gesuchsperiode;

        var query = queryFactory
            .select(gesuchsperiode)
            .from(gesuchsperiode)
            .where(
                gesuchsperiode.aufschaltterminStart.before(date)
                    .and(
                        gesuchsperiode.aufschaltterminStopp.after(date)
                            .or(gesuchsperiode.aufschaltterminStopp.eq(date))
                    )
            );
        return query.stream();
    }

    public Optional<Gesuchsperiode> getLatest() {
        final var gesuchsperiode = QGesuchsperiode.gesuchsperiode;
        return new JPAQueryFactory(entityManager)
            .selectFrom(gesuchsperiode)
            .orderBy(gesuchsperiode.timestampErstellt.desc())
            .stream()
            .findFirst();
    }
}
