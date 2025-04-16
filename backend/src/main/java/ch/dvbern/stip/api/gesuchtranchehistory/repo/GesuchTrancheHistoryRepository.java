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

import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchhistory.service.GesuchHistoryService;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;

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
            .addOrder(AuditEntity.revisionNumber().asc())
            .setMaxResults(1)
            .getSingleResult();
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

    @Transactional
    public Optional<GesuchTranche> getGesuchTrancheAtRevision(final UUID gesuchTrancheId, final Integer revision) {
        @SuppressWarnings("unchecked")
        final Optional<GesuchTranche> gesuchTrancheOpt = AuditReaderFactory.get(em)
            .createQuery()
            .forEntitiesAtRevision(GesuchTranche.class, revision)
            .add(AuditEntity.id().eq(gesuchTrancheId))
            .setMaxResults(1)
            .getResultList()
            .stream()
            .findFirst();
        return gesuchTrancheOpt;
    }

    @Transactional
    public Optional<GesuchTranche> getLatestWhereGesuchStatusChangedToVerfuegt(final UUID gesuchId) {
        return findCurrentGesuchTrancheOfGesuchInStatus(gesuchId, Gesuchstatus.VERFUEGT);
    }

    @Transactional
    public Optional<GesuchTranche> getLatestWhereGesuchStatusChangedToEingereicht(final UUID gesuchId) {
        return findCurrentGesuchTrancheOfGesuchInStatus(gesuchId, Gesuchstatus.EINGEREICHT);
    }

    @Transactional
    public Optional<GesuchTranche> findCurrentGesuchTrancheOfGesuchInStatus(
        final UUID gesuchId,
        final Gesuchstatus gesuchStatus
    ) {
        return gesuchHistoryService.getLatestWhereStatusChangedTo(gesuchId, gesuchStatus)
            .flatMap(Gesuch::getNewestGesuchTranche);
    }

    @Transactional
    public Optional<GesuchTranche> findOldestHistoricTrancheOfGesuchWhereStatusChangedTo(
        final UUID gesuchId,
        final Gesuchstatus gesuchStatus
    ) {
        // The GesuchTranchen attached to this here are the revision that they were at the historic moment in time
        final var historicGesuch = gesuchHistoryService.getLatestWhereStatusChangedTo(gesuchId, gesuchStatus);

        // Get the one that was created the furthest in the past, i.e. the first/ initial Tranche
        return historicGesuch.flatMap(
            gesuch -> gesuch.getGesuchTranchen()
                .stream()
                .min(Comparator.comparing(AbstractEntity::getTimestampErstellt))
        );
    }
}
