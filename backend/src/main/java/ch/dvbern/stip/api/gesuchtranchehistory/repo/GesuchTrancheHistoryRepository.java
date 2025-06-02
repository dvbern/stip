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

package ch.dvbern.stip.api.gesuchtranchehistory.repo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.dokument.service.GesuchDokumentMapper;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchhistory.service.GesuchHistoryService;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.generated.dto.GesuchDokumentDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchTrancheHistoryRepository {
    private final EntityManager em;
    private final GesuchHistoryService gesuchHistoryService;
    private final GesuchDokumentMapper gesuchDokumentMapper;

    @Transactional
    public GesuchTranche getInitialRevision(final UUID gesuchTrancheId) {
        final var reader = AuditReaderFactory.get(em);

        return (GesuchTranche) reader.createQuery()
            .forRevisionsOfEntity(GesuchTranche.class, true, true)
            .add(AuditEntity.id().eq(gesuchTrancheId))
            .addOrder(AuditEntity.revisionNumber().asc())
            .setMaxResults(1)
            .getSingleResult();
    }

    public GesuchTranche getLatestVersion(final UUID gesuchTrancheId) {
        try {
            final var reader = AuditReaderFactory.get(em);

            return (GesuchTranche) reader.createQuery()
                .forRevisionsOfEntity(GesuchTranche.class, true, true)
                .add(AuditEntity.id().eq(gesuchTrancheId))
                .add(AuditEntity.property("gesuch").isNotNull())
                .addOrder(AuditEntity.revisionNumber().desc())
                .setMaxResults(1)
                .getSingleResult();
        } catch (final NoResultException e) {
            throw new NotFoundException();
        }
    }

    public List<GesuchDokumentDto> getGesuchDokumenteForGesuchTrancheOfLatestRevision(final UUID gesuchTrancheId) {
        return getLatestVersion(gesuchTrancheId).getGesuchDokuments()
            .stream()
            .map(gesuchDokumentMapper::toDto)
            .toList();
    }

    @Transactional
    public GesuchTranche getLatestWhereStatusChangedToUeberpruefen(final UUID gesuchTrancheId) {
        final var reader = AuditReaderFactory.get(em);
        return (GesuchTranche) reader.createQuery()
            .forRevisionsOfEntity(GesuchTranche.class, true, false)
            .add(AuditEntity.id().eq(gesuchTrancheId))
            .add(AuditEntity.property("status").eq(GesuchTrancheStatus.UEBERPRUEFEN))
            .add(AuditEntity.property("status").hasChanged())
            .addOrder(AuditEntity.revisionNumber().desc())
            .setMaxResults(1)
            .getSingleResult();
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public Optional<Integer> getLatestRevisionWhereStatusChangedTo(
        final UUID gesuchTrancheId,
        final GesuchTrancheStatus gesuchTrancheStatus
    ) {
        final var reader = AuditReaderFactory.get(em);
        return reader.createQuery()
            .forRevisionsOfEntity(GesuchTranche.class, false, false)
            .addProjection(AuditEntity.revisionNumber())
            .add(AuditEntity.id().eq(gesuchTrancheId))
            .add(AuditEntity.property("status").eq(gesuchTrancheStatus))
            .add(AuditEntity.property("status").hasChanged())
            .addOrder(AuditEntity.revisionNumber().desc())
            .setMaxResults(1)
            .getResultList()
            .stream()
            .findFirst();
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public Optional<Integer> getEarliestRevisionWhereStatusChangedTo(
        final UUID gesuchTrancheId,
        final GesuchTrancheStatus gesuchTrancheStatus
    ) {
        final var reader = AuditReaderFactory.get(em);
        return reader.createQuery()
            .forRevisionsOfEntity(GesuchTranche.class, false, false)
            .addProjection(AuditEntity.revisionNumber())
            .add(AuditEntity.id().eq(gesuchTrancheId))
            .add(AuditEntity.property("status").eq(gesuchTrancheStatus))
            .add(AuditEntity.property("status").hasChanged())
            .addOrder(AuditEntity.revisionNumber().asc())
            .setMaxResults(1)
            .getResultList()
            .stream()
            .findFirst();
    }

    public List<GesuchTranche> getAllTranchenWhereGesuchStatusFirstChangedToVerfuegt(final UUID gesuchId) {
        return gesuchHistoryService.getFirstWhereStatusChangedTo(gesuchId, Gesuchstatus.VERFUEGT)
            .map(Gesuch::getTranchenTranchen)
            .map(Stream::toList)
            .orElse(new ArrayList<>());
    }

    @Transactional
    public Optional<GesuchTranche> getLatestWhereGesuchStatusChangedToVerfuegt(
        final UUID gesuchId,
        final LocalDate gueltigAb
    ) {
        return gesuchHistoryService.getLatestWhereStatusChangedTo(gesuchId, Gesuchstatus.VERFUEGT)
            .flatMap(gesuch -> gesuch.getEingereichteGesuchTrancheValidOnDate(gueltigAb))
            .stream()
            .findFirst();
    }

    @Transactional
    public Optional<GesuchTranche> getLatestWhereGesuchStatusChangedToEingereicht(
        final UUID gesuchId,
        final LocalDate gueltigAb
    ) {
        return gesuchHistoryService.getLatestWhereStatusChangedTo(
            gesuchId,
            Gesuchstatus.EINGEREICHT
        )
            .flatMap(gesuch -> gesuch.getEingereichteGesuchTrancheValidOnDate(gueltigAb))
            .stream()
            .findFirst();
    }

    @Transactional
    public Optional<GesuchTranche> getLatestExistingVersionOfTranche(final UUID gesuchTrancheId) {
        @SuppressWarnings("unchecked")
        final Optional<GesuchTranche> gesuchTrancheOpt = AuditReaderFactory.get(em)
            .createQuery()
            .forRevisionsOfEntity(GesuchTranche.class, true, true)
            .add(AuditEntity.revisionType().ne(RevisionType.DEL))
            .add(AuditEntity.id().eq(gesuchTrancheId))
            .addOrder(AuditEntity.revisionNumber().desc())
            .setMaxResults(1)
            .getResultList()
            .stream()
            .findFirst();
        return gesuchTrancheOpt;
    }
}
