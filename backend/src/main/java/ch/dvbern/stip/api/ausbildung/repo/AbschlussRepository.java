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

package ch.dvbern.stip.api.ausbildung.repo;

import java.util.stream.Stream;

import ch.dvbern.stip.api.ausbildung.entity.Abschluss;
import ch.dvbern.stip.api.ausbildung.entity.QAbschluss;
import ch.dvbern.stip.api.ausbildung.type.AbschlussSortColumn;
import ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie;
import ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class AbschlussRepository implements BaseRepository<Abschluss> {
    private final EntityManager entityManager;

    private static final QAbschluss Q_ABSCHLUSS = QAbschluss.abschluss;

    public JPAQuery<Abschluss> baseQuery() {
        return new JPAQueryFactory(entityManager).selectFrom(Q_ABSCHLUSS);
    }

    public Stream<Abschluss> findAllAktiv() {
        return new JPAQueryFactory(entityManager)
            .selectFrom(Q_ABSCHLUSS)
            .where(Q_ABSCHLUSS.aktiv.isTrue())
            .stream();
    }

    public void ausbildungskategorieFilter(
        final JPAQuery<Abschluss> query,
        final Ausbildungskategorie ausbildungskategorie
    ) {
        query.where(Q_ABSCHLUSS.ausbildungskategorie.eq(ausbildungskategorie));
    }

    public void bildungsrichtungFilter(final JPAQuery<Abschluss> query, final Bildungsrichtung bildungsrichtung) {
        query.where(Q_ABSCHLUSS.bildungsrichtung.eq(bildungsrichtung));
    }

    public void bezeichnungDeFilter(final JPAQuery<Abschluss> query, final String bezeichnungDe) {
        query.where(Q_ABSCHLUSS.bezeichnungDe.containsIgnoreCase(bezeichnungDe));
    }

    public void bezeichnungFrFilter(final JPAQuery<Abschluss> query, final String bezeichnungFr) {
        query.where(Q_ABSCHLUSS.bezeichnungFr.containsIgnoreCase(bezeichnungFr));
    }

    public void aktivFilter(final JPAQuery<Abschluss> query, final Boolean aktiv) {
        query.where(Q_ABSCHLUSS.aktiv.eq(aktiv));
    }

    public void orderBy(
        final JPAQuery<Abschluss> query,
        final AbschlussSortColumn column,
        final SortOrder sortOrder
    ) {
        final var fieldSpecified = switch (column) {
            case AUSBILDUNGSKATEGORIE -> Q_ABSCHLUSS.ausbildungskategorie;
            case BILDUNGSKATEGORIE -> Q_ABSCHLUSS.bildungskategorie;
            case BILDUNGSRICHTUNG -> Q_ABSCHLUSS.bildungsrichtung;
            case BFS_KATEGORIE -> Q_ABSCHLUSS.bfsKategorie;
            case BERUFSBEFAEHIGENDER_ABSCHLUSS -> Q_ABSCHLUSS.berufsbefaehigenderAbschluss;
            case FERIEN -> Q_ABSCHLUSS.ferien;
            case BEZEICHNUNG_DE -> Q_ABSCHLUSS.bezeichnungDe;
            case BEZEICHNUNG_FR -> Q_ABSCHLUSS.bezeichnungFr;
            case AKTIV -> Q_ABSCHLUSS.aktiv;
        };

        final var orderSpecifier = switch (sortOrder) {
            case ASCENDING -> fieldSpecified.asc();
            case DESCENDING -> fieldSpecified.desc();
        };

        query.orderBy(orderSpecifier);
    }

    public void defaultOrder(final JPAQuery<Abschluss> query) {
        query.orderBy(Q_ABSCHLUSS.bfsKategorie.desc());
    }

    public JPAQuery<Long> getCountQuery(final JPAQuery<Abschluss> query) {
        return query.clone().select(Q_ABSCHLUSS.count());
    }

    public void paginate(final JPAQuery<Abschluss> query, final int page, final int pageSize) {
        query.offset((long) pageSize * page).limit(pageSize);
    }
}
