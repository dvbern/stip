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

package ch.dvbern.stip.api.massendruck.repo;

import java.time.LocalDate;

import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.api.massendruck.entity.MassendruckJob;
import ch.dvbern.stip.api.massendruck.entity.QMassendruckJob;
import ch.dvbern.stip.api.massendruck.type.MassendruckJobSortColumn;
import ch.dvbern.stip.api.massendruck.type.MassendruckJobStatus;
import ch.dvbern.stip.api.massendruck.type.MassendruckJobTyp;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class MassendruckJobQueryBuilder {
    private static final QMassendruckJob massendruckJob = QMassendruckJob.massendruckJob;

    private final EntityManager entityManager;

    public JPAQuery<MassendruckJob> getAllQuery() {
        return new JPAQueryFactory(entityManager).selectFrom(massendruckJob);
    }

    public JPAQuery<MassendruckJob> getAlleAktivQuery() {
        return addStatusFilter(getAllQuery(), MassendruckJobStatus.IN_PROGRESS);
    }

    public JPAQuery<MassendruckJob> getAlleArchiviertQuery() {
        return addStatusFilter(getAllQuery(), MassendruckJobStatus.ARCHIVED);
    }

    public JPAQuery<MassendruckJob> getAlleFehlerhaftGenerierung() {
        return addStatusFilter(getAllQuery(), MassendruckJobStatus.FAILED);
    }

    public JPAQuery<MassendruckJob> addStatusFilter(
        final JPAQuery<MassendruckJob> query,
        final MassendruckJobStatus status
    ) {
        final var massendruckJob = QMassendruckJob.massendruckJob;
        return query.where(massendruckJob.status.eq(status));
    }

    public void massendruckJobNumber(final JPAQuery<MassendruckJob> query, final int massendruckJobNumber) {
        query.where(massendruckJob.massendruckJobNumber.eq(massendruckJobNumber));
    }

    public void userErstellt(final JPAQuery<MassendruckJob> query, final String userErstellt) {
        query.where(massendruckJob.userErstellt.containsIgnoreCase(userErstellt));
    }

    public void timestampErstellt(final JPAQuery<MassendruckJob> query, final LocalDate timestampErstellt) {
        // There is no equality for LocalDateTime and LocalDate, so we check that dayOfYear and the year are equal
        query.where(
            massendruckJob.timestampErstellt.dayOfYear()
                .eq(timestampErstellt.getDayOfYear())
                .and(massendruckJob.timestampErstellt.year().eq(timestampErstellt.getYear()))
        );
    }

    public void massendruckJobStatus(final JPAQuery<MassendruckJob> query, final MassendruckJobStatus status) {
        query.where(massendruckJob.status.eq(status));
    }

    public void massendruckJobType(final JPAQuery<MassendruckJob> query, final MassendruckJobTyp massendruckJobTyp) {
        switch (massendruckJobTyp) {
            case DATENSCHUTZBRIEF -> query.where(massendruckJob.datenschutzbriefMassendrucks.isNotEmpty());
            case VERFUEGUNG -> query.where(massendruckJob.verfuegungMassendrucks.isNotEmpty());
        }
    }

    public void orderBy(
        final JPAQuery<MassendruckJob> query,
        final MassendruckJobSortColumn column,
        final SortOrder sortOrder
    ) {
        final var fieldSpecified = switch (column) {
            case JOB_NUMBER -> massendruckJob.massendruckJobNumber;
            case USER_ERSTELLT -> massendruckJob.userErstellt;
            case TIMESTAMP_ERSTELLT -> massendruckJob.timestampErstellt;
            case STATUS -> massendruckJob.status;
            case TYP -> massendruckJob.status; // TODO KSTIP-2294 actually implement this
        };

        final var orderSpecifier = switch (sortOrder) {
            case ASCENDING -> fieldSpecified.asc();
            case DESCENDING -> fieldSpecified.desc();
        };

        query.orderBy(orderSpecifier);
    }

    public JPAQuery<Long> getCountQuery(final JPAQuery<MassendruckJob> query) {
        return query.clone().select(massendruckJob.count());
    }

    public void defaultOrder(final JPAQuery<MassendruckJob> query) {
        query.orderBy(massendruckJob.timestampErstellt.desc());
    }

    public void paginate(final JPAQuery<MassendruckJob> query, final int page, final int pageSize) {
        query.offset((long) pageSize * page).limit(pageSize);
    }
}
