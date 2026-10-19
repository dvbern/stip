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

package ch.dvbern.stip.api.ausbildung.repo;

import java.util.Optional;
import java.util.stream.Stream;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsstaette;
import ch.dvbern.stip.api.ausbildung.entity.QAusbildungsstaette;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class AusbildungsstaetteRepository implements BaseRepository<Ausbildungsstaette> {
    private static final QAusbildungsstaette Q_AUSBILDUNGSSTAETTE = QAusbildungsstaette.ausbildungsstaette;

    /// Query all Ausbildungsstaette joining ausbildungsgaenge and abschluss
    ///
    /// Using `left join `**`fetch`** to make hibernate use a single query to fetch everything
    /// @return a stream of all ausbildungsstaette with their related data
    public Stream<Ausbildungsstaette> findAllJoiningRelated() {
        return getEntityManager().createQuery(
            """
                select ausbildungsstaette
                from Ausbildungsstaette ausbildungsstaette
                left join fetch ausbildungsstaette.ausbildungsgaenge ausbildungsgang
                left join fetch ausbildungsgang.abschluss
            """,
            Ausbildungsstaette.class
        ).getResultStream();
    }

    public Optional<Ausbildungsstaette> findByNameDe(final String nameDe) {
        return new JPAQueryFactory(getEntityManager())
            .selectFrom(Q_AUSBILDUNGSSTAETTE)
            .where(Q_AUSBILDUNGSSTAETTE.nameDe.eq(nameDe))
            .where(Q_AUSBILDUNGSSTAETTE.aktiv)
            .stream()
            .findAny();
    }

    public Optional<Ausbildungsstaette> findByNameFr(final String nameFr) {
        return new JPAQueryFactory(getEntityManager())
            .selectFrom(Q_AUSBILDUNGSSTAETTE)
            .where(Q_AUSBILDUNGSSTAETTE.nameFr.eq(nameFr))
            .where(Q_AUSBILDUNGSSTAETTE.aktiv)
            .stream()
            .findAny();
    }
}
