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

package ch.dvbern.stip.api.beschwerdeverlauf.repo;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.beschwerdeverlauf.entity.BeschwerdeVerlaufEntry;
import ch.dvbern.stip.api.beschwerdeverlauf.entity.QBeschwerdeVerlaufEntry;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor
public class BeschwerdeVerlaufRepository implements BaseRepository<BeschwerdeVerlaufEntry> {
    private final EntityManager entityManager;
    private static final QBeschwerdeVerlaufEntry beschwerdeVerlaufEntry =
        QBeschwerdeVerlaufEntry.beschwerdeVerlaufEntry;

    public List<BeschwerdeVerlaufEntry> findByGesuchId(final UUID gesuchId) {
        return new JPAQueryFactory(entityManager)
            .selectFrom(beschwerdeVerlaufEntry)
            .where(beschwerdeVerlaufEntry.gesuch.id.eq(gesuchId))
            .orderBy(beschwerdeVerlaufEntry.timestampErstellt.asc())
            .fetch();
    }
}
