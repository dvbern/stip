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

package ch.dvbern.stip.api.gesuchhistory.repo;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.util.AuditEntityUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchHistoryRepository {
    private final EntityManager entityManager;

    public List<Gesuch> getStatusHistory(final UUID gesuchId) {
        final var reader = AuditReaderFactory.get(entityManager);
        @SuppressWarnings("unchecked")
        // Reason: forRevisionsOfEntity with Gesuch.class and selectEntitiesOnly will always return a List<Gesuch>
        final List<Gesuch> revisions = reader
            .createQuery()
            .forRevisionsOfEntity(Gesuch.class, true, true)
            .add(AuditEntity.property("id").eq(gesuchId))
            .add(AuditEntity.property("gesuchStatus").hasChanged())
            .addOrder(AuditEntity.property("timestampMutiert").desc())
            .getResultList()
            .stream()
            .map(Gesuch.class::cast)
            .toList();

        return revisions;
    }

    // Reason: forRevisionsOfEntity with Gesuch.class and selectEntitiesOnly will always return a List<Gesuch>
    @SuppressWarnings("unchecked")
    public Stream<Gesuch> getWhereStatusChangeHappenedBefore(
        final List<UUID> ids,
        final Gesuchstatus gesuchstatus,
        final LocalDateTime dueDate
    ) {
        final var reader = AuditReaderFactory.get(entityManager);
        return reader
            .createQuery()
            .forRevisionsOfEntity(Gesuch.class, true, true)
            .add(AuditEntity.property("id").in(ids))
            .add(AuditEntity.property("gesuchStatus").eq(gesuchstatus))
            .add(AuditEntity.property("gesuchStatus").hasChanged())
            .add(AuditEntity.property("gesuchStatusAenderungDatum").lt(dueDate))
            .setMaxResults(1)
            .getResultList()
            .stream();
    }

    // Reason: forRevisionsOfEntity with Gesuch.class and selectEntitiesOnly will always return a List<Gesuch>
    @SuppressWarnings("unchecked")
    public Optional<Gesuch> getLatestWhereStatusChangedTo(
        final UUID gesuchId,
        final Gesuchstatus gesuchStatus
    ) {
        final var reader = AuditReaderFactory.get(entityManager);
        return reader
            .createQuery()
            .forRevisionsOfEntity(Gesuch.class, true, true)
            .add(AuditEntity.property("id").eq(gesuchId))
            .add(AuditEntity.property("gesuchStatus").eq(gesuchStatus))
            .add(AuditEntity.property("gesuchStatus").hasChanged())
            .addOrder(AuditEntityUtil.revisionTimestamp().desc())
            .setMaxResults(1)
            .getResultList()
            .stream()
            .findFirst();
    }

    // Reason: forRevisionsOfEntity with Gesuch.class and selectEntitiesOnly will always return a List<Gesuch>
    @SuppressWarnings("unchecked")
    public Optional<Gesuch> getFirstWhereStatusChangedTo(
        final UUID gesuchId,
        final Gesuchstatus gesuchStatus
    ) {
        final var reader = AuditReaderFactory.get(entityManager);
        return reader
            .createQuery()
            .forRevisionsOfEntity(Gesuch.class, true, true)
            .add(AuditEntity.property("id").eq(gesuchId))
            .add(AuditEntity.property("gesuchStatus").eq(gesuchStatus))
            .add(AuditEntity.property("gesuchStatus").hasChanged())
            .addOrder(AuditEntityUtil.revisionTimestamp().asc())
            .setMaxResults(1)
            .getResultList()
            .stream()
            .findFirst();
    }

    // Reason: forRevisionsOfEntity with Gesuch.class and selectEntitiesOnly will always return a List<Gesuch>
    @SuppressWarnings("unchecked")
    public Optional<Gesuch> getLatestWhereStatusChangedToOneOf(
        final UUID gesuchId,
        final Collection<Gesuchstatus> gesuchStatus
    ) {
        final var reader = AuditReaderFactory.get(entityManager);
        return reader
            .createQuery()
            .forRevisionsOfEntity(Gesuch.class, true, true)
            .add(AuditEntity.property("id").eq(gesuchId))
            .add(AuditEntity.property("gesuchStatus").in(gesuchStatus))
            .add(AuditEntity.property("gesuchStatus").hasChanged())
            .addOrder(AuditEntityUtil.revisionTimestamp().desc())
            .setMaxResults(1)
            .getResultList()
            .stream()
            .findFirst();
    }

    @SuppressWarnings("unchecked")
    public Optional<Integer> getRevisionWhereStatusChangedTo(final UUID gesuchId, final Gesuchstatus gesuchStatus) {
        final var reader = AuditReaderFactory.get(entityManager);
        return reader
            .createQuery()
            .forRevisionsOfEntity(Gesuch.class, false, true)
            .addProjection(AuditEntity.revisionNumber())
            .add(AuditEntity.property("id").eq(gesuchId))
            .add(AuditEntity.property("gesuchStatus").eq(gesuchStatus))
            .add(AuditEntity.property("gesuchStatus").hasChanged())
            .addOrder(AuditEntityUtil.revisionTimestamp().desc())
            .setMaxResults(1)
            .getResultList()
            .stream()
            .findFirst();
    }

    @SuppressWarnings("unchecked")
    public Optional<Integer> getRevisionWhereStatusChangedToOneOf(
        final UUID gesuchId,
        final Collection<Gesuchstatus> gesuchStatus
    ) {
        final var reader = AuditReaderFactory.get(entityManager);
        return reader
            .createQuery()
            .forRevisionsOfEntity(Gesuch.class, false, true)
            .addProjection(AuditEntity.revisionNumber())
            .add(AuditEntity.property("id").eq(gesuchId))
            .add(AuditEntity.property("gesuchStatus").in(gesuchStatus))
            .add(AuditEntity.property("gesuchStatus").hasChanged())
            .addOrder(AuditEntityUtil.revisionTimestamp().desc())
            .setMaxResults(1)
            .getResultList()
            .stream()
            .findFirst();
    }

    @SuppressWarnings("unchecked")
    public Optional<Integer> getEarliestRevisionWhereStatusChangedAfterRevision(
        final UUID gesuchId,
        final Integer revisionNumber
    ) {
        final var reader = AuditReaderFactory.get(entityManager);
        return reader
            .createQuery()
            .forRevisionsOfEntity(Gesuch.class, false, true)
            .addProjection(AuditEntity.revisionNumber())
            .add(AuditEntity.property("id").eq(gesuchId))
            .add(AuditEntity.revisionNumber().gt(revisionNumber))
            .add(AuditEntity.property("gesuchStatus").hasChanged())
            .addOrder(AuditEntityUtil.revisionTimestamp().asc())
            .setMaxResults(1)
            .getResultList()
            .stream()
            .findFirst();
    }

    public Optional<Gesuch> getGesuchAtRevision(final UUID gesuchId, final Integer revisionNumber) {
        @SuppressWarnings("unchecked")
        final Optional<Gesuch> revision = AuditReaderFactory.get(entityManager)
            .createQuery()
            .forEntitiesAtRevision(Gesuch.class, revisionNumber)
            .add(AuditEntity.id().eq(gesuchId))
            .setMaxResults(1)
            .getResultList()
            .stream()
            .findFirst();
        return revision;
    }

}
