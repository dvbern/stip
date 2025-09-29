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

package ch.dvbern.stip.api.datenschutzbrief.repo;

import java.util.UUID;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.datenschutzbrief.entity.Datenschutzbrief;
import ch.dvbern.stip.api.datenschutzbrief.entity.QDatenschutzbrief;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class DatenschutzbriefRepository implements BaseRepository<Datenschutzbrief> {
    private final EntityManager entityManager;

    @Transactional
    public void deleteAllByGesuchId(final UUID gesuchId) {
        final var datenschuzbrief = QDatenschutzbrief.datenschutzbrief;

        new JPAQueryFactory(entityManager)
            .delete(datenschuzbrief)
            .where(datenschuzbrief.gesuch.id.eq(gesuchId))
            .execute();
    }
}
