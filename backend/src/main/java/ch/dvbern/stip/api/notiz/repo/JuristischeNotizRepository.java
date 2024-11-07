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

package ch.dvbern.stip.api.notiz.repo;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.notiz.entity.JuristischeAbklaerungNotiz;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class JuristischeNotizRepository implements BaseRepository<JuristischeAbklaerungNotiz> {
    private final EntityManager entityManager;

    public List<JuristischeAbklaerungNotiz> findAllByGesuchId(UUID gesuchId) {
        final var list =
            entityManager.createQuery(
                "SELECT j from JuristischeAbklaerungNotiz j " +
                "WHERE j.gesuch.id = :gesuchId",
                JuristischeAbklaerungNotiz.class
            ).setParameter("gesuchId", gesuchId).getResultList();

        return list;
    }
}
