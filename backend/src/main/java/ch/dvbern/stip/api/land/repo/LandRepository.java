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

package ch.dvbern.stip.api.land.repo;

import java.util.Optional;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.land.entity.Land;
import ch.dvbern.stip.api.land.entity.QLand;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class LandRepository implements BaseRepository<Land> {
    private final EntityManager entityManager;

    public Optional<Land> getByBfsCode(final String bfsCode) {
        final var land = QLand.land;
        return new JPAQueryFactory(entityManager)
            .selectFrom(land)
            .where(land.laendercodeBfs.eq(bfsCode))
            .stream()
            .findFirst();
    }

    public Optional<Land> getByIso3code(final String iso3code) {
        final var land = QLand.land;
        return new JPAQueryFactory(entityManager)
            .selectFrom(land)
            .where(land.iso3code.eq(iso3code))
            .stream()
            .findFirst();
    }
}
