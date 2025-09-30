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
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.service.SbDashboardQueryBuilder;
import ch.dvbern.stip.api.gesuch.type.GetGesucheSBQueryType;
import ch.dvbern.stip.api.gesuch.type.SortOrder;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.massendruck.entity.DatenschutzbriefMassendruck;
import ch.dvbern.stip.api.massendruck.entity.MassendruckJob;
import ch.dvbern.stip.api.massendruck.entity.VerfuegungMassendruck;
import ch.dvbern.stip.api.massendruck.repo.MassendruckJobQueryBuilder;
import ch.dvbern.stip.api.massendruck.repo.MassendruckJobRepository;
import ch.dvbern.stip.api.massendruck.repo.MassendruckJobSeqRepository;
import ch.dvbern.stip.api.massendruck.type.GetMassendruckJobQueryType;
import ch.dvbern.stip.api.massendruck.type.MassendruckJobSortColumn;
import ch.dvbern.stip.api.massendruck.type.MassendruckJobStatus;
import ch.dvbern.stip.api.massendruck.type.MassendruckJobTyp;
import ch.dvbern.stip.api.tenancy.service.TenantService;
import ch.dvbern.stip.generated.dto.MassendruckJobDto;
import ch.dvbern.stip.generated.dto.PaginatedMassendruckJobDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class MassendruckJobService {
    private final MassendruckJobQueryBuilder massendruckJobQueryBuilder;
    private final MassendruckJobMapper massendruckJobMapper;
    private final MassendruckJobDocumentWorker massendruckJobDocumentWorker;
    private final SbDashboardQueryBuilder sbDashboardQueryBuilder;
    private final MassendruckJobSeqRepository massendruckJobSeqRepository;
    private final MassendruckJobRepository massendruckJobRepository;
    private final GesuchStatusService gesuchStatusService;
    private final TenantService tenantService;
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

    @Transactional
    public MassendruckJobDto createMassendruckJobForQueryType(final GetGesucheSBQueryType getGesucheSBQueryType) {
        final var gesuche = sbDashboardQueryBuilder.baseQuery(getGesucheSBQueryType, GesuchTrancheTyp.TRANCHE)
            .stream()
            .toList();

        final var tenantIdentifier = tenantService.getCurrentTenantIdentifier();
        final var massendruckJob = new MassendruckJob()
            .setMassendruckJobNumber(massendruckJobSeqRepository.getNextValue(tenantIdentifier))
            .setStatus(MassendruckJobStatus.IN_PROGRESS);

        switch (getGesucheSBQueryType) {
            case ALLE_DRUCKBAR_VERFUEGUNGEN, MEINE_DRUCKBAR_VERFUEGUNGEN -> createAndSetVerfuegungMassendruck(
                massendruckJob,
                gesuche
            );
            case ALLE_DRUCKBAR_DATENSCHUTZBRIEFE, MEINE_DRUCKBAR_DATENSCHUTZBRIEFE -> createAndSetDatenschutzbriefMassendruck(
                massendruckJob,
                gesuche
            );
            default -> throw new BadRequestException();
        }

        massendruckJobRepository.persistAndFlush(massendruckJob);
        return massendruckJobMapper.toDto(massendruckJob);
    }

    public void combineDocument(final UUID massendruckJobId) {
        massendruckJobDocumentWorker.combineDocuments(massendruckJobId, tenantService.getCurrentTenantIdentifier());
    }

    private void createAndSetDatenschutzbriefMassendruck(
        final MassendruckJob massendruckJob,
        final List<Gesuch> gesuche
    ) {
        final var toPersist = gesuche.stream()
            .flatMap(gesuch -> gesuch.getDatenschutzbriefs().stream())
            .map(
                datenschutzbrief -> new DatenschutzbriefMassendruck()
                    .setDatenschutzbrief(datenschutzbrief)
                    .setMassendruckJob(massendruckJob)
            )
            .toList();

        gesuchStatusService
            .bulkTriggerStateMachineEvent(gesuche, GesuchStatusChangeEvent.DATENSCHUTZBRIEF_AM_GENERIEREN);

        massendruckJob.setDatenschutzbriefMassendrucks(toPersist);
    }

    private void createAndSetVerfuegungMassendruck(
        final MassendruckJob massendruckJob,
        final List<Gesuch> gesuche
    ) {
        final var toPersist = gesuche.stream()
            .flatMap(
                gesuch -> gesuch.getVerfuegungs()
                    .stream()
                    .filter(verfuegung -> !verfuegung.isVersendet())
            )
            .map(
                verfuegung -> new VerfuegungMassendruck()
                    .setVerfuegung(verfuegung)
                    .setVorname("Foo")
                    .setNachname("Bar")
                    .setMassendruckJob(massendruckJob)
            )
            .toList();

        gesuchStatusService.bulkTriggerStateMachineEvent(gesuche, GesuchStatusChangeEvent.VERFUEGUNG_AM_GENERIEREN);

        massendruckJob.setVerfuegungMassendrucks(toPersist);
    }
}
