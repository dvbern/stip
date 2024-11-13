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

package ch.dvbern.stip.api.stammdaten.repo;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.stammdaten.entity.LandEuEfta;
import ch.dvbern.stip.api.stammdaten.entity.QLandEuEfta;
import ch.dvbern.stip.api.stammdaten.type.Land;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class LandEuEftaRepository implements BaseRepository<LandEuEfta> {
    private final EntityManager entityManager;

    public void setLandEuEfta(Land land, Boolean isEuEfta) {
        if (Boolean.TRUE.equals(isEuEfta)) {
            setLandEuEfta(land);
        } else {
            unsetLandEuEfta(land);
        }
    }

    public void setLandEuEfta(Land land) {
        final var landEuEfta = new JPAQueryFactory(entityManager)
            .selectFrom(QLandEuEfta.landEuEfta)
            .where(QLandEuEfta.landEuEfta.land.eq(land))
            .fetchOne();

        if (landEuEfta == null) {
            entityManager.persist(new LandEuEfta().setLand(land));
        }
    }

    public void unsetLandEuEfta(Land land) {
        final var landEuEfta = new JPAQueryFactory(entityManager)
            .selectFrom(QLandEuEfta.landEuEfta)
            .where(QLandEuEfta.landEuEfta.land.eq(land))
            .fetchOne();

        if (landEuEfta != null) {
            this.delete(landEuEfta);
        }
    }
}
