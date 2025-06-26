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

import java.util.Optional;
import java.util.stream.Stream;

import ch.dvbern.stip.api.benutzer.entity.QRolle;
import ch.dvbern.stip.api.benutzer.entity.QSachbearbeiter;
import ch.dvbern.stip.api.benutzer.entity.Sachbearbeiter;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class SachbearbeiterRepository implements BaseRepository<Sachbearbeiter> {
    private final EntityManager entityManager;

    public Stream<Sachbearbeiter> findByRolle(String stringRolle) {
        final var sachbearbeiter = QSachbearbeiter.sachbearbeiter;
        final var rolle = QRolle.rolle;

        return new JPAQueryFactory(entityManager)
            .selectFrom(sachbearbeiter)
            .join(sachbearbeiter.rollen, rolle)
            .where(rolle.keycloakIdentifier.eq(stringRolle))
            .stream();
    }

    public Optional<Sachbearbeiter> findByKeycloakId(String keycloakId) {
        final var sachbearbeiter = QSachbearbeiter.sachbearbeiter;

        return new JPAQueryFactory(entityManager)
            .selectFrom(sachbearbeiter)
            .where(sachbearbeiter.keycloakId.eq(keycloakId))
            .stream()
            .findFirst();
    }
}
