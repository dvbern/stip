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

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.entity.QAusbildungsgang;
import ch.dvbern.stip.api.ausbildung.type.AusbildungsgangSortColumn;
import ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class AusbildungsgangQueryBuilder {
    private final EntityManager entityManager;

    private static final QAusbildungsgang Q_AUSBILDUNGSGANG = QAusbildungsgang.ausbildungsgang;

    public JPAQuery<Ausbildungsgang> baseQuery() {
        return new JPAQueryFactory(entityManager).selectFrom(Q_AUSBILDUNGSGANG);
    }

    public void abschlussBezeichnungDeFilter(
        final JPAQuery<Ausbildungsgang> query,
        final String abschlussBezeichnungDe
    ) {
        query.where(Q_AUSBILDUNGSGANG.abschluss.bezeichnungDe.containsIgnoreCase(abschlussBezeichnungDe));
    }

    public void abschlussBezeichnungFrFilter(
        final JPAQuery<Ausbildungsgang> query,
        final String abschlussBezeichnungFr
    ) {
        query.where(Q_AUSBILDUNGSGANG.abschluss.bezeichnungFr.containsIgnoreCase(abschlussBezeichnungFr));
    }

    public void ausbildungskategorieFilter(
        final JPAQuery<Ausbildungsgang> query,
        final Ausbildungskategorie ausbildungskategorie
    ) {
        query.where(Q_AUSBILDUNGSGANG.abschluss.ausbildungskategorie.eq(ausbildungskategorie));
    }

    public void ausbildungsstaetteNameDeFilter(
        final JPAQuery<Ausbildungsgang> query,
        final String ausbildungsstaetteNameDe
    ) {
        query.where(Q_AUSBILDUNGSGANG.ausbildungsstaette.nameDe.containsIgnoreCase(ausbildungsstaetteNameDe));
    }

    public void ausbildungsstaetteNameFrFilter(
        final JPAQuery<Ausbildungsgang> query,
        final String ausbildungsstaetteNameFr
    ) {
        query.where(Q_AUSBILDUNGSGANG.ausbildungsstaette.nameFr.containsIgnoreCase(ausbildungsstaetteNameFr));
    }

    public void aktivFilter(final JPAQuery<Ausbildungsgang> query, final Boolean aktiv) {
        query.where(Q_AUSBILDUNGSGANG.aktiv.eq(aktiv));
    }

    public void orderBy(
        final JPAQuery<Ausbildungsgang> query,
        final AusbildungsgangSortColumn column,
        final SortOrder sortOrder
    ) {
        final var fieldSpecified = switch (column) {
            case ABSCHLUSS_BEZEICHNUNG_DE -> Q_AUSBILDUNGSGANG.abschluss.bezeichnungDe;
            case ABSCHLUSS_BEZEICHNUNG_FR -> Q_AUSBILDUNGSGANG.abschluss.bezeichnungFr;
            case AUSBILDUNGSSTAETTE_NAME_DE -> Q_AUSBILDUNGSGANG.ausbildungsstaette.nameDe;
            case AUSBILDUNGSSTAETTE_NAME_FR -> Q_AUSBILDUNGSGANG.ausbildungsstaette.nameFr;
            case AKTIV -> Q_AUSBILDUNGSGANG.aktiv;
        };

        final var orderSpecifier = switch (sortOrder) {
            case ASCENDING -> fieldSpecified.asc();
            case DESCENDING -> fieldSpecified.desc();
        };

        query.orderBy(orderSpecifier);
    }

    public void defaultOrder(final JPAQuery<Ausbildungsgang> query) {
        query.orderBy(Q_AUSBILDUNGSGANG.abschluss.bezeichnungDe.desc());
    }

    public JPAQuery<Long> getCountQuery(final JPAQuery<Ausbildungsgang> query) {
        return query.clone().select(Q_AUSBILDUNGSGANG.count());
    }

    public void paginate(final JPAQuery<Ausbildungsgang> query, final int page, final int pageSize) {
        query.offset((long) pageSize * page).limit(pageSize);
    }
}
