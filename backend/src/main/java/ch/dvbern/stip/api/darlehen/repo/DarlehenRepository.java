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

package ch.dvbern.stip.api.darlehen.repo;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.darlehen.entity.Darlehen;
import ch.dvbern.stip.api.darlehen.entity.QDarlehen;
import ch.dvbern.stip.api.darlehen.type.DarlehenStatus;
import ch.dvbern.stip.api.zuordnung.entity.QZuordnung;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class DarlehenRepository implements BaseRepository<Darlehen> {
    private static final QDarlehen darlehen = QDarlehen.darlehen;
    private static final QZuordnung zuordnung = QZuordnung.zuordnung;

    private final EntityManager entityManager;

    public List<Darlehen> findByFallId(final UUID fallId) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var darlehen = QDarlehen.darlehen;
        var query = queryFactory
            .selectFrom(darlehen)
            .where(
                darlehen.fall.id.eq(fallId)
            );
        return query.stream().toList();
    }

    public List<Darlehen> findByGesuchId(final UUID gesuchId) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var darlehen = QDarlehen.darlehen;
        var query = queryFactory
            .selectFrom(darlehen)
            .where(
                darlehen.relatedGesuch.id.eq(gesuchId)
            );
        return query.stream().toList();
    }

    public JPAQuery<Darlehen> getAlleQuery() {
        return new JPAQueryFactory(entityManager)
            .selectFrom(darlehen);
    }

    public JPAQuery<Darlehen> getMeineQuery(final UUID benutzerId) {
        return getAlleQuery()
            .join(zuordnung)
            .on(darlehen.fall.sachbearbeiterZuordnung.id.eq(zuordnung.id))
            .where(zuordnung.sachbearbeiter.id.eq(benutzerId));
    }

    public JPAQuery<Darlehen> getAlleBearbeitbarQuery() {
        return getAlleQuery().where(darlehen.status.eq(DarlehenStatus.EINGEGEBEN));
    }

    public JPAQuery<Darlehen> getMeineBearbeitbarQuery(final UUID benutzerId) {
        return getMeineQuery(benutzerId).where(darlehen.status.eq(DarlehenStatus.EINGEGEBEN));
    }

    public Darlehen requireByDokumentId(final UUID dokumentId) {
        return getAlleQuery()
            .where(darlehen.dokumente.any().dokumente.any().id.eq(dokumentId))
            .stream()
            .findFirst()
            .orElseThrow(NotFoundException::new);
    }
}
