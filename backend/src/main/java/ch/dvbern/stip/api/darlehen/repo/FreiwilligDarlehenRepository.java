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
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.darlehen.entity.FreiwilligDarlehen;
import ch.dvbern.stip.api.darlehen.entity.QFreiwilligDarlehen;
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
public class FreiwilligDarlehenRepository implements BaseRepository<FreiwilligDarlehen> {
    private static final QFreiwilligDarlehen freiwilligDarlehen = QFreiwilligDarlehen.freiwilligDarlehen;
    private static final QZuordnung zuordnung = QZuordnung.zuordnung;

    private final EntityManager entityManager;

    public List<FreiwilligDarlehen> findByFallId(final UUID fallId) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var query = queryFactory
            .selectFrom(freiwilligDarlehen)
            .where(
                freiwilligDarlehen.fall.id.eq(fallId)
            );
        return query.stream().toList();
    }

    public List<FreiwilligDarlehen> findByGesuchId(final UUID gesuchId) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var query = queryFactory
            .selectFrom(freiwilligDarlehen)
            .where(
                freiwilligDarlehen.relatedGesuch.id.eq(gesuchId)
            );
        return query.stream().toList();
    }

    public JPAQuery<FreiwilligDarlehen> getAlleQuery() {
        return new JPAQueryFactory(entityManager)
            .selectFrom(freiwilligDarlehen);
    }

    public JPAQuery<FreiwilligDarlehen> getMeineQuery(final UUID benutzerId) {
        return getAlleQuery()
            .join(zuordnung)
            .on(freiwilligDarlehen.fall.sachbearbeiterZuordnung.id.eq(zuordnung.id))
            .where(zuordnung.sachbearbeiter.id.eq(benutzerId));
    }

    public JPAQuery<FreiwilligDarlehen> getAlleBearbeitbarQuery() {
        return getAlleQuery().where(freiwilligDarlehen.status.eq(DarlehenStatus.EINGEGEBEN));
    }

    public JPAQuery<FreiwilligDarlehen> getMeineBearbeitbarQuery(final UUID benutzerId) {
        return getMeineQuery(benutzerId).where(freiwilligDarlehen.status.eq(DarlehenStatus.EINGEGEBEN));
    }

    public FreiwilligDarlehen requireByDokumentId(final UUID dokumentId) {
        return getAlleQuery()
            .where(
                freiwilligDarlehen.dokumente.any().dokumente.any().id.eq(dokumentId)
            )
            .stream()
            .findFirst()
            .orElseThrow(NotFoundException::new);
    }

    public Optional<FreiwilligDarlehen> getByDokumentId(final UUID dokumentId) {
        return getAlleQuery()
            .where(
                freiwilligDarlehen.dokumente.any().dokumente.any().id.eq(dokumentId)
            )
            .stream()
            .findFirst();
    }

}
