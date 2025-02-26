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

package ch.dvbern.stip.api.sozialdienst.repo;

import java.util.stream.Stream;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.sozialdienst.entity.QSozialdienst;
import ch.dvbern.stip.api.sozialdienst.entity.Sozialdienst;
import ch.dvbern.stip.api.sozialdienstbenutzer.entity.SozialdienstBenutzer;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class SozialdienstRepository implements BaseRepository<Sozialdienst> {
    private final EntityManager entityManager;

    public Sozialdienst getSozialdienstBySozialdienstAdmin(final SozialdienstBenutzer sozialdienstAdmin) {
        final var sozialdienst = QSozialdienst.sozialdienst;

        return new JPAQueryFactory(entityManager)
            .selectFrom(sozialdienst)
            .where(sozialdienst.sozialdienstAdmin.eq(sozialdienstAdmin))
            .fetchOne();
    }

    public Sozialdienst getSozialdienstByBenutzer(final SozialdienstBenutzer benutzer) {
        final var sozialdienst = QSozialdienst.sozialdienst;

        return new JPAQueryFactory(entityManager)
            .selectFrom(sozialdienst)
            .where(sozialdienst.sozialdienstBenutzers.contains(benutzer))
            .stream()
            .findFirst()
            .orElseThrow(NotFoundException::new);
    }

    public Stream<Sozialdienst> getSozialdiensteWithMitarbeiter() {
        final var sozialdienst = QSozialdienst.sozialdienst;

        return new JPAQueryFactory(entityManager)
            .selectFrom(sozialdienst)
            .where(sozialdienst.sozialdienstBenutzers.isNotEmpty())
            .stream();
    }
}
