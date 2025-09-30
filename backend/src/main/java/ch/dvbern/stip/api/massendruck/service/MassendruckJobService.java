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

package ch.dvbern.stip.api.massendruck.service;

import java.time.LocalDate;

import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.api.massendruck.repo.MassendruckJobQueryBuilder;
import ch.dvbern.stip.api.massendruck.type.GetMassendruckJobQueryType;
import ch.dvbern.stip.api.massendruck.type.MassendruckJobSortColumn;
import ch.dvbern.stip.api.massendruck.type.MassendruckJobStatus;
import ch.dvbern.stip.api.massendruck.type.MassendruckJobTyp;
import ch.dvbern.stip.generated.dto.PaginatedMassendruckJobDto;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class MassendruckJobService {
    private final MassendruckJobQueryBuilder massendruckJobQueryBuilder;
    private final MassendruckJobMapper massendruckJobMapper;
    private final ConfigService configService;

    public PaginatedMassendruckJobDto getAllMassendruckJobs(
        final GetMassendruckJobQueryType queryTyp,
        final int page,
        final int pageSize,
        final Integer massendruckJobNumber,
        final String userErstellt,
        final LocalDate timestampErstellt,
        final MassendruckJobStatus massendruckJobStatus,
        final MassendruckJobTyp massendruckJobTyp,
        final MassendruckJobSortColumn sortColumn,
        final SortOrder sortOrder
    ) {
        if (pageSize > configService.getMaxAllowedPageSize()) {
            throw new IllegalArgumentException("Page size exceeded max allowed page size");
        }

        final var baseQuery = switch (queryTyp) {
            case ALLE -> massendruckJobQueryBuilder.getAllQuery();
            case ALLE_AKTIV -> massendruckJobQueryBuilder.getAlleAktivQuery();
            case ALLE_ARCHIVIERT -> massendruckJobQueryBuilder.getAlleArchiviertQuery();
            case ALLE_FEHLERHAFTE_GENERIERUNG -> massendruckJobQueryBuilder.getAlleFehlerhaftGenerierung();
        };

        if (massendruckJobNumber != null) {
            massendruckJobQueryBuilder.massendruckJobNumber(baseQuery, massendruckJobNumber);
        }

        if (userErstellt != null) {
            massendruckJobQueryBuilder.userErstellt(baseQuery, userErstellt);
        }

        if (timestampErstellt != null) {
            massendruckJobQueryBuilder.timestampErstellt(baseQuery, timestampErstellt);
        }

        if (massendruckJobStatus != null) {
            massendruckJobQueryBuilder.massendruckJobStatus(baseQuery, massendruckJobStatus);
        }

        if (massendruckJobTyp != null) {
            massendruckJobQueryBuilder.massendruckJobType(baseQuery, massendruckJobTyp);
        }

        // Creating the count query must happen before ordering,
        // otherwise the ordered column must appear in a GROUP BY clause or be used in an aggregate function
        final var countQuery = massendruckJobQueryBuilder.getCountQuery(baseQuery);

        if (sortColumn != null && sortOrder != null) {
            massendruckJobQueryBuilder.orderBy(baseQuery, sortColumn, sortOrder);
        } else {
            massendruckJobQueryBuilder.defaultOrder(baseQuery);
        }

        massendruckJobQueryBuilder.paginate(baseQuery, page, pageSize);

        // This needs to be 2 statements, as streaming over the result and mapping directly throws a JDBC Exception
        // Likely because 'getMassendruckTyp' can reach back out to the DB
        final var entities = baseQuery.stream().toList();
        final var results = entities.stream().map(massendruckJobMapper::toDto).toList();

        return new PaginatedMassendruckJobDto(
            page,
            results.size(),
            Math.toIntExact(countQuery.fetchOne()),
            results
        );
    }
}
