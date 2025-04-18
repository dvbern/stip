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

package ch.dvbern.stip.api.zuordnung.repo;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.zuordnung.entity.QZuordnung;
import ch.dvbern.stip.api.zuordnung.entity.Zuordnung;
import ch.dvbern.stip.api.zuordnung.type.ZuordnungType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class ZuordnungRepository implements BaseRepository<Zuordnung> {
    private final EntityManager entityManager;

    public Stream<Zuordnung> findAllWithType(final ZuordnungType type) {
        final var zuordnung = QZuordnung.zuordnung;
        return new JPAQueryFactory(entityManager)
            .selectFrom(zuordnung)
            .where(zuordnung.zuordnungType.eq(type))
            .stream();
    }

    public void deleteByFallIds(Set<UUID> fallIds) {
        final var zuordnung = QZuordnung.zuordnung;

        new JPAQueryFactory(entityManager)
            .delete(zuordnung)
            .where(zuordnung.fall.id.in(fallIds))
            .execute();
    }

    public void deleteByIds(final List<UUID> ids) {
        final var zuordnung = QZuordnung.zuordnung;

        new JPAQueryFactory(entityManager)
            .delete(zuordnung)
            .where(zuordnung.id.in(ids))
            .execute();
    }

    public Stream<Zuordnung> findByBenutzerId(final UUID benutzerId) {
        final var zuordnung = QZuordnung.zuordnung;

        return new JPAQueryFactory(entityManager)
            .selectFrom(zuordnung)
            .where(zuordnung.sachbearbeiter.id.eq(benutzerId))
            .stream();
    }
}
