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

import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsstaette;
import ch.dvbern.stip.api.ausbildung.entity.QAusbildungsstaette;
import ch.dvbern.stip.api.ausbildung.type.AusbildungsstaetteSortColumn;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class AusbildungsstaetteQueryBuilder {
    private final EntityManager entityManager;

    private static final QAusbildungsstaette Q_AUSBILDUNGSSTAETTE = QAusbildungsstaette.ausbildungsstaette;

    public JPAQuery<Ausbildungsstaette> baseQuery() {
        return new JPAQueryFactory(entityManager).selectFrom(Q_AUSBILDUNGSSTAETTE);
    }

    public void nameDeFilter(final JPAQuery<Ausbildungsstaette> query, final String nameDe) {
        query.where(Q_AUSBILDUNGSSTAETTE.nameDe.containsIgnoreCase(nameDe));
    }

    public void nameFrFilter(final JPAQuery<Ausbildungsstaette> query, final String nameFr) {
        query.where(Q_AUSBILDUNGSSTAETTE.nameFr.containsIgnoreCase(nameFr));
    }

    public void chShisFilter(final JPAQuery<Ausbildungsstaette> query, final String chShis) {
        query.where(Q_AUSBILDUNGSSTAETTE.nummer.containsIgnoreCase(chShis));
    }

    public void burNoFilter(final JPAQuery<Ausbildungsstaette> query, final String burNo) {
        query.where(Q_AUSBILDUNGSSTAETTE.nummer.containsIgnoreCase(burNo));
    }

    public void ctNoFilter(final JPAQuery<Ausbildungsstaette> query, final String ctNo) {
        query.where(Q_AUSBILDUNGSSTAETTE.nummer.containsIgnoreCase(ctNo));
    }

    public void aktivFilter(final JPAQuery<Ausbildungsstaette> query, final Boolean aktiv) {
        query.where(Q_AUSBILDUNGSSTAETTE.aktiv.eq(aktiv));
    }

    public void orderBy(
        final JPAQuery<Ausbildungsstaette> query,
        final AusbildungsstaetteSortColumn column,
        final SortOrder sortOrder
    ) {
        final var fieldSpecified = switch (column) {
            case NAME_DE -> Q_AUSBILDUNGSSTAETTE.nameDe;
            case NAME_FR -> Q_AUSBILDUNGSSTAETTE.nameFr;
            // Adapted from https://stackoverflow.com/a/8502570/9363973, with Postgres 16 maybe replace
            // todo: complete
            // case CH_SHIS -> Expressions.stringTemplate(
            // "CAST(NULLIF(regexp_replace({0}, '\\D', '', 'g'), '') AS integer)",
            // Q_AUSBILDUNGSSTAETTE.chShis
            // );
            // case BUR_NO -> Q_AUSBILDUNGSSTAETTE.burNo;
            // case CT_NO -> Q_AUSBILDUNGSSTAETTE.ctNo;
            case CH_SHIS, BUR_NO, CT_NO -> Q_AUSBILDUNGSSTAETTE.nummer;
            case AKTIV -> Q_AUSBILDUNGSSTAETTE.aktiv;
        };

        final var orderSpecifier = switch (sortOrder) {
            case ASCENDING -> fieldSpecified.asc().nullsLast();
            case DESCENDING -> fieldSpecified.desc().nullsFirst();
        };

        query.orderBy(orderSpecifier);
    }

    public void defaultOrder(final JPAQuery<Ausbildungsstaette> query) {
        query.orderBy(Q_AUSBILDUNGSSTAETTE.nameDe.desc());
    }

    public JPAQuery<Long> getCountQuery(final JPAQuery<Ausbildungsstaette> query) {
        return query.clone().select(Q_AUSBILDUNGSSTAETTE.count());
    }

    public void paginate(final JPAQuery<Ausbildungsstaette> query, final int page, final int pageSize) {
        query.offset((long) pageSize * page).limit(pageSize);
    }
}
