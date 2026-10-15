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
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.entity.QAusbildungsgang;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class AusbildungsgangRepository implements BaseRepository<Ausbildungsgang> {
    private static final QAusbildungsgang Q_AUSBILDUNGSGANG = QAusbildungsgang.ausbildungsgang;

    public Optional<Ausbildungsgang> findByAusbildungsstaetteAndAbschluss(
        final UUID ausbildungsstaetteId,
        final UUID abschlussId
    ) {
        return new JPAQueryFactory(getEntityManager())
            .selectFrom(Q_AUSBILDUNGSGANG)
            .where(
                Q_AUSBILDUNGSGANG.ausbildungsstaette.id.eq(ausbildungsstaetteId)
                    .and(Q_AUSBILDUNGSGANG.abschluss.id.eq(abschlussId))
                    .and(Q_AUSBILDUNGSGANG.aktiv)
            )
            .stream()
            .findFirst();
    }

    /// Query all aktiv Ausbildungsgang joining ausbildungsstaettes and abschluss
    ///
    /// Using `left join `**`fetch`** to make hibernate use a single query to fetch everything
    /// @return a stream of all aktiv Ausbildungsgang with their related data
    public Stream<Ausbildungsgang> findAllAktivJoiningRelated() {
        return getEntityManager().createQuery(
            """
                select ausbildungsgang
                from Ausbildungsgang ausbildungsgang
                left join fetch ausbildungsgang.ausbildungsstaette
                left join fetch ausbildungsgang.abschluss
                where ausbildungsgang.aktiv = true
            """,
            Ausbildungsgang.class
        ).getResultStream();
    }

    public void setAllAusbildungsgaengeOfAusbildungsstaetteToInaktiv(final UUID ausbildungsstaetteId) {
        new JPAQueryFactory(getEntityManager())
            .update(Q_AUSBILDUNGSGANG)
            .where(Q_AUSBILDUNGSGANG.ausbildungsstaette.id.eq(ausbildungsstaetteId))
            .set(Q_AUSBILDUNGSGANG.aktiv, false)
            .execute();
    }

    public void setAllAusbildungsgaengeOfAbschlussToInaktiv(final UUID abschlussId) {
        new JPAQueryFactory(getEntityManager())
            .update(Q_AUSBILDUNGSGANG)
            .where(Q_AUSBILDUNGSGANG.abschluss.id.eq(abschlussId))
            .set(Q_AUSBILDUNGSGANG.aktiv, false)
            .execute();
    }
}
