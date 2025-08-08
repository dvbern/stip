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

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.entity.QAusbildungsgang;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class AusbildungsgangRepository implements BaseRepository<Ausbildungsgang> {
    private final EntityManager em;

    public Optional<Ausbildungsgang> findByAusbildungsstaetteAndAbschluss(
        final UUID ausbildungsstaetteId,
        final UUID abschlussId
    ) {
        final var ausbildungsgang = QAusbildungsgang.ausbildungsgang;
        return new JPAQueryFactory(em)
            .selectFrom(ausbildungsgang)
            .where(
                ausbildungsgang.ausbildungsstaette.id.eq(ausbildungsstaetteId)
                    .and(ausbildungsgang.abschluss.id.eq(abschlussId))
                    .and(ausbildungsgang.aktiv)
            )
            .stream()
            .findFirst();
    }
}
