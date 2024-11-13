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

package ch.dvbern.stip.api.plz.repo;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.plz.entity.Plz;
import ch.dvbern.stip.api.plz.entity.QPlz;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class PlzRepository implements BaseRepository<Plz> {
    private final EntityManager em;

    public boolean isPlzInKanton(String postleitzahl, String kantonsKuerzel) {
        final var plz = QPlz.plz1;
        final var flushmode = em.getFlushMode();
        em.setFlushMode(FlushModeType.COMMIT);
        var ret = new JPAQueryFactory(em)
            .selectFrom(plz)
            .where(
                plz.kantonskuerzel.equalsIgnoreCase(kantonsKuerzel)
                    .and(plz.plz.eq(postleitzahl))
            )
            .stream()
            .findAny()
            .isPresent();
        em.setFlushMode(flushmode);
        return ret;
    }
}
