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

package ch.dvbern.stip.api.benutzer.repo;

import java.util.HashSet;
import java.util.Set;

import ch.dvbern.stip.api.benutzer.entity.QRolle;
import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class RolleRepository implements BaseRepository<Rolle> {
    private final EntityManager entityManager;

    public Set<Rolle> findByKeycloakIdentifier(Set<String> keycloakIdentifiers) {
        final var rolle = QRolle.rolle;

        final var fromDb = new JPAQueryFactory(entityManager)
            .selectFrom(rolle)
            .where(rolle.keycloakIdentifier.in(keycloakIdentifiers))
            .stream()
            .toList();

        return new HashSet<>(fromDb);
    }
}
