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

package ch.dvbern.stip.api.demo.repo;

import ch.dvbern.stip.api.ausbildung.entity.Abschluss;
import ch.dvbern.stip.api.ausbildung.entity.QAbschluss;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class DemoDataAbschlussRepository implements BaseRepository<Abschluss> {
    private final EntityManager em;

    private static final QAbschluss Q_ABSCHLUSS = QAbschluss.abschluss;

    public final Abschluss requireByAbschlussName(String abschluss) {
        return new JPAQueryFactory(em)
            .selectFrom(Q_ABSCHLUSS)
            .where(Q_ABSCHLUSS.bezeichnungDe.eq(abschluss).and(Q_ABSCHLUSS.aktiv))
            .stream()
            .findFirst()
            .orElseThrow();
    }
}
