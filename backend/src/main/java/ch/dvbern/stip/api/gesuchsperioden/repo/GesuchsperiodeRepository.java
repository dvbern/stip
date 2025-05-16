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
import java.util.List;
import java.util.Optional;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.common.type.GueltigkeitStatus;
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

    static final QGesuchsperiode gesuchsperiode = QGesuchsperiode.gesuchsperiode;

    public List<Gesuchsperiode> findAllPubliziertStoppBefore(LocalDate date) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var query = queryFactory
            .selectFrom(gesuchsperiode)
            .where(gesuchsperiode.gueltigkeitStatus.eq(GueltigkeitStatus.PUBLIZIERT))
            .where(
                gesuchsperiode.gesuchsperiodeStopp.before(date)
            )
            .orderBy(gesuchsperiode.aufschaltterminStart.desc());
        return query.stream().toList();
    }

    public Gesuchsperiode findPubliziertStartBeforeOrAt(LocalDate date) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var query = queryFactory
            .selectFrom(gesuchsperiode)
            .where(gesuchsperiode.gueltigkeitStatus.eq(GueltigkeitStatus.PUBLIZIERT))
            .where(
                gesuchsperiode.gesuchsperiodeStart.before(date)
                    .or(gesuchsperiode.gesuchsperiodeStart.eq(date))
            )
            .orderBy(gesuchsperiode.aufschaltterminStart.desc());
        return query.fetchFirst();
    }

    public Gesuchsperiode findStartBeforeOrAt(LocalDate date) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var query = queryFactory
            .selectFrom(gesuchsperiode)
            .where(
                gesuchsperiode.gesuchsperiodeStart.before(date)
                    .or(gesuchsperiode.gesuchsperiodeStart.eq(date))
            )
            .orderBy(gesuchsperiode.aufschaltterminStart.desc());
        return query.fetchFirst();
    }

    public Optional<Gesuchsperiode> getLatest() {
        return new JPAQueryFactory(entityManager)
            .selectFrom(gesuchsperiode)
            .orderBy(gesuchsperiode.timestampErstellt.desc())
            .stream()
            .findFirst();
    }
}
