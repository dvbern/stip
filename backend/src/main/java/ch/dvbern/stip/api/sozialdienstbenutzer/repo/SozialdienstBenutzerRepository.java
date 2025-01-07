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

package ch.dvbern.stip.api.sozialdienstbenutzer.repo;

import java.util.Optional;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.sozialdienstbenutzer.entity.QSozialdienstBenutzer;
import ch.dvbern.stip.api.sozialdienstbenutzer.entity.SozialdienstBenutzer;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class SozialdienstBenutzerRepository implements BaseRepository<SozialdienstBenutzer> {
    private final EntityManager entityManager;

    public Optional<SozialdienstBenutzer> findByKeycloakId(String keycloakId) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var sozialdienstAdmin = QSozialdienstBenutzer.sozialdienstBenutzer;
        var query = queryFactory
            .select(sozialdienstAdmin)
            .from(sozialdienstAdmin)
            .where(sozialdienstAdmin.keycloakId.eq(keycloakId));
        return query.stream().findFirst();
    }
}
