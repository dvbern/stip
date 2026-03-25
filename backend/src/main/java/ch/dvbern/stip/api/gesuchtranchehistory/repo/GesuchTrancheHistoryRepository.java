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

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.common.util.AuditEntityUtil;
import ch.dvbern.stip.api.gesuchhistory.service.GesuchHistoryService;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import jakarta.annotation.Nullable;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchTrancheHistoryRepository {
    private final EntityManager em;
    private final GesuchHistoryService gesuchHistoryService;

    @Transactional
    public GesuchTranche getInitialRevision(final UUID gesuchTrancheId) {
        final var reader = AuditReaderFactory.get(em);

        return (GesuchTranche) reader.createQuery()
            .forRevisionsOfEntity(GesuchTranche.class, true, true)
            .add(AuditEntity.id().eq(gesuchTrancheId))
            .addOrder(AuditEntityUtil.revisionTimestamp().asc())
            .setMaxResults(1)
            .getSingleResult();
    }

    private AuditQuery getRevisionQuery(final UUID gesuchTrancheId) {
        final var reader = AuditReaderFactory.get(em);
        return reader.createQuery()
            .forRevisionsOfEntity(GesuchTranche.class, true, false)
            .add(AuditEntity.id().eq(gesuchTrancheId))
            .add(AuditEntity.property("status").hasChanged())
            .addOrder(AuditEntityUtil.revisionTimestamp().desc())
            .setMaxResults(1);
    }

    @Transactional
    public GesuchTranche getLatestWhereStatusChangedToUeberpruefen(final UUID gesuchTrancheId) {
        return (GesuchTranche) getRevisionQuery(gesuchTrancheId)
            .add(AuditEntity.property("status").eq(GesuchTrancheStatus.UEBERPRUEFEN))
            .getSingleResult();
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public Long getLatestRevisionTimestampWhereStatusWasInBearbeitungGs(
        final UUID gesuchTrancheId
    ) {
        final var reader = AuditReaderFactory.get(em);
        return (Long) reader.createQuery()
            .forRevisionsOfEntity(GesuchTranche.class, false, false)
            .addProjection(AuditEntityUtil.revisionTimestamp())
            .add(AuditEntity.id().eq(gesuchTrancheId))
            .add(AuditEntity.property("status").eq(GesuchTrancheStatus.IN_BEARBEITUNG_GS))
            .addOrder(AuditEntityUtil.revisionTimestamp().desc())
            .setMaxResults(1)
            .getSingleResult();
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public GesuchTranche getEarliestTrancheAfterTimestampWhereStatusWasUeberprufen(
        final UUID gesuchTrancheId,
        final Long shouldBeAfterTimestamp
    ) {
        final var reader = AuditReaderFactory.get(em);
        return (GesuchTranche) reader.createQuery()
            .forRevisionsOfEntity(GesuchTranche.class, true, false)
            .add(AuditEntity.id().eq(gesuchTrancheId))
            .add(AuditEntity.property("status").eq(GesuchTrancheStatus.UEBERPRUEFEN))
            .add(AuditEntity.revisionType().eq(RevisionType.MOD))
            .add(AuditEntityUtil.revisionTimestamp().gt(shouldBeAfterTimestamp))
            .addOrder(AuditEntityUtil.revisionTimestamp().asc())
            .setMaxResults(1)
            .getSingleResult();
    }

    @Transactional
    public GesuchTranche getByRevisionId(
        final UUID gesuchTrancheId,
        final @Nullable Integer revisionNumber
    ) {
        @SuppressWarnings("unchecked")
        final GesuchTranche revision = (GesuchTranche) AuditReaderFactory.get(em)
            .createQuery()
            .forEntitiesAtRevision(GesuchTranche.class, revisionNumber)
            .add(AuditEntity.id().eq(gesuchTrancheId))
            .getSingleResult();
        return revision;
    }

    @Transactional
    public GesuchTranche getByRevisionTimestamp(
        final UUID gesuchTrancheId,
        final Long revisionTimestamp
    ) {
        final var reader = AuditReaderFactory.get(em);
        final var revisionNumber = reader.getRevisionNumberForDate(Instant.ofEpochMilli(revisionTimestamp));
        return getByRevisionId(gesuchTrancheId, revisionNumber.intValue());
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
            .addOrder(AuditEntityUtil.revisionTimestamp().desc())
            .setMaxResults(1)
            .getResultList()
            .stream()
            .findFirst();
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public Optional<Integer> getLatestRevisionWhereStatusChangedToOneOf(
        final UUID gesuchTrancheId,
        final List<GesuchTrancheStatus> gesuchTrancheStatusToList
    ) {
        final var reader = AuditReaderFactory.get(em);
        return reader.createQuery()
            .forRevisionsOfEntity(GesuchTranche.class, false, false)
            .addProjection(AuditEntity.revisionNumber())
            .add(AuditEntity.id().eq(gesuchTrancheId))
            .add(AuditEntity.property("status").in(gesuchTrancheStatusToList))
            .add(AuditEntity.property("status").hasChanged())
            .addOrder(AuditEntityUtil.revisionTimestamp().desc())
            .setMaxResults(1)
            .getResultList()
            .stream()
            .findFirst();
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public Optional<Long> getLatestRevisionTimestampWhereStatusChangedToOneOf(
        final UUID gesuchTrancheId,
        final List<GesuchTrancheStatus> gesuchTrancheStatusToList
    ) {
        final var reader = AuditReaderFactory.get(em);
        return reader.createQuery()
            .forRevisionsOfEntity(GesuchTranche.class, false, false)
            .addProjection(AuditEntityUtil.revisionTimestamp())
            .add(AuditEntity.id().eq(gesuchTrancheId))
            .add(AuditEntity.property("status").in(gesuchTrancheStatusToList))
            .add(AuditEntity.property("status").hasChanged())
            .addOrder(AuditEntityUtil.revisionTimestamp().desc())
            .setMaxResults(1)
            .getResultList()
            .stream()
            .findFirst();
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
            .add(AuditEntity.property("gesuch").isNotNull()) // Envers may have erroneous entries with null elements
                                                             // this filters those out
            .addOrder(AuditEntityUtil.revisionTimestamp().desc())
            .setMaxResults(1)
            .getResultList()
            .stream()
            .findFirst();
        return gesuchTrancheOpt;
    }

    @Transactional
    public List<Pair<GesuchTranche, DefaultRevisionEntity>> getAllAbgelehnteAenderungTrancheHistory(
        final UUID gesuchId
    ) {
        final var reader = AuditReaderFactory.get(em);
        @SuppressWarnings("unchecked")
        final List<Pair<GesuchTranche, DefaultRevisionEntity>> abgelehntAenderungHistory = reader
            .createQuery()
            .forRevisionsOfEntity(GesuchTranche.class, false, true)
            .add(AuditEntity.property("gesuch_id").eq(gesuchId))
            .add(AuditEntity.revisionType().ne(RevisionType.DEL))
            .add(AuditEntity.revisionType().ne(RevisionType.ADD))
            .add(AuditEntity.property("typ").eq(GesuchTrancheTyp.AENDERUNG))
            .add(AuditEntity.property("status").eq(GesuchTrancheStatus.IN_BEARBEITUNG_GS))
            .add(AuditEntity.property("status").hasChanged())
            .getResultList()
            .stream()
            .map(result -> {
                final var list = (Object[]) result;
                return Pair.of((GesuchTranche) list[0], (DefaultRevisionEntity) list[1]);
            })
            .toList();
        return abgelehntAenderungHistory;
    }
}
