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

import java.time.Instant;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.common.util.AuditEntityUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.QGesuch;
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
    private static final QGesuch Q_GESUCH = QGesuch.gesuch;

    @SuppressWarnings("unchecked")
    public Optional<UUID> getUserMutiertOfLatestToInFreigabeChange(final UUID gesuchId) {
        final var reader = AuditReaderFactory.get(entityManager);

        return (Optional<UUID>) reader
            .createQuery()
            .forRevisionsOfEntity(Gesuch.class, true, false)
            .addProjection(AuditEntity.property(Q_GESUCH.userMutiertId.getMetadata().getName()))
            .add(AuditEntity.property(Q_GESUCH.id.getMetadata().getName()).eq(gesuchId))
            .add(AuditEntity.property(Q_GESUCH.gesuchStatus.getMetadata().getName()).eq(Gesuchstatus.IN_FREIGABE))
            .add(AuditEntity.property(Q_GESUCH.gesuchStatus.getMetadata().getName()).hasChanged())
            .addOrder(AuditEntityUtil.revisionTimestamp().desc())
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

    public Optional<Gesuch> getGesuchAtRevision(final UUID gesuchId, final Integer revisionNumber) {
        @SuppressWarnings("unchecked")
        final Optional<Gesuch> revision = AuditReaderFactory.get(entityManager)
            .createQuery()
            .forEntitiesAtRevision(Gesuch.class, revisionNumber)
            .add(AuditEntity.id().eq(gesuchId))
            .addOrder(AuditEntityUtil.revisionTimestamp().desc())
            .setMaxResults(1)
            .getResultList()
            .stream()
            .findFirst();
        return revision;
    }

    public Optional<Gesuch> getGesuchAtRevisionTimestamp(final UUID gesuchId, final Long revisionTimestamp) {
        final var reader = AuditReaderFactory.get(entityManager);
        final var revision = reader.getRevisionNumberForDate(Instant.ofEpochMilli(revisionTimestamp));

        return getGesuchAtRevision(gesuchId, revision.intValue());
    }

    @SuppressWarnings("unchecked")
    public Optional<Gesuch> getLastEingereichtGesuchVersion(final UUID gesuchId, final boolean before) {
        final var reader = AuditReaderFactory.get(entityManager);

        final Optional<Long> revisionTimestampOpt = reader.createQuery()
            .forRevisionsOfEntity(Gesuch.class, false, true)
            .addProjection(AuditEntityUtil.revisionTimestamp())
            .add(AuditEntity.property("id").eq(gesuchId))
            .add(AuditEntity.property("eingereichtCount").hasChanged())
            .add(AuditEntity.property("eingereichtCount").gt(0))
            .addOrder(AuditEntityUtil.revisionTimestamp().desc())
            .setMaxResults(1)
            .getResultList()
            .stream()
            .findFirst();

        if (revisionTimestampOpt.isEmpty()) {
            return Optional.empty();
        }

        return getGesuchAtRevisionTimestamp(gesuchId, revisionTimestampOpt.get() - (before ? 1 : 0));
    }

    @SuppressWarnings("unchecked")
    public Optional<Gesuch> getFirstEingereichtGesuchVersion(final UUID gesuchId) {
        final var reader = AuditReaderFactory.get(entityManager);

        final Optional<Long> revisionTimestampOpt = reader.createQuery()
            .forRevisionsOfEntity(Gesuch.class, false, true)
            .addProjection(AuditEntityUtil.revisionTimestamp())
            .add(AuditEntity.property("id").eq(gesuchId))
            .add(AuditEntity.property("eingereichtCount").hasChanged())
            .add(AuditEntity.property("eingereichtCount").gt(0))
            .addOrder(AuditEntityUtil.revisionTimestamp().asc())
            .setMaxResults(1)
            .getResultList()
            .stream()
            .findFirst();

        if (revisionTimestampOpt.isEmpty()) {
            return Optional.empty();
        }

        return getGesuchAtRevisionTimestamp(gesuchId, revisionTimestampOpt.get());
    }

    @SuppressWarnings("unchecked")
    public Optional<Gesuch> getFirstVerfuegtGesuchVersion(final UUID gesuchId) {
        final var reader = AuditReaderFactory.get(entityManager);

        final Optional<Long> revisionTimestampOpt = reader.createQuery()
            .forRevisionsOfEntity(Gesuch.class, false, true)
            .addProjection(AuditEntityUtil.revisionTimestamp())
            .add(AuditEntity.property("id").eq(gesuchId))
            .add(AuditEntity.property("verfuegtCount").hasChanged())
            .add(AuditEntity.property("verfuegtCount").gt(0))
            .addOrder(AuditEntityUtil.revisionTimestamp().asc())
            .setMaxResults(1)
            .getResultList()
            .stream()
            .findFirst();

        if (revisionTimestampOpt.isEmpty()) {
            return Optional.empty();
        }

        return getGesuchAtRevisionTimestamp(gesuchId, revisionTimestampOpt.get());
    }

    @SuppressWarnings("unchecked")
    public Optional<Gesuch> getLastVerfuegtGesuchVersion(final UUID gesuchId) {
        final var reader = AuditReaderFactory.get(entityManager);

        final Optional<Long> revisionTimestampOpt = reader.createQuery()
            .forRevisionsOfEntity(Gesuch.class, false, true)
            .addProjection(AuditEntityUtil.revisionTimestamp())
            .add(AuditEntity.property("id").eq(gesuchId))
            .add(AuditEntity.property("verfuegtCount").hasChanged())
            .add(AuditEntity.property("verfuegtCount").gt(0))
            .addOrder(AuditEntityUtil.revisionTimestamp().desc())
            .setMaxResults(1)
            .getResultList()
            .stream()
            .findFirst();

        if (revisionTimestampOpt.isEmpty()) {
            return Optional.empty();
        }

        return getGesuchAtRevisionTimestamp(gesuchId, revisionTimestampOpt.get());
    }

    @SuppressWarnings("unchecked")
    public Optional<Integer> getLastEingereichtGesuchRevision(final UUID gesuchId) {
        final var reader = AuditReaderFactory.get(entityManager);

        final Optional<Long> revisionTimestampOpt = reader.createQuery()
            .forRevisionsOfEntity(Gesuch.class, false, true)
            .addProjection(AuditEntityUtil.revisionTimestamp())
            .add(AuditEntity.property("id").eq(gesuchId))
            .add(AuditEntity.property("eingereichtCount").hasChanged())
            .add(AuditEntity.property("eingereichtCount").gt(0))
            .addOrder(AuditEntityUtil.revisionTimestamp().desc())
            .setMaxResults(1)
            .getResultList()
            .stream()
            .findFirst();

        if (revisionTimestampOpt.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(
            reader.getRevisionNumberForDate(Instant.ofEpochSecond(revisionTimestampOpt.get()))
                .intValue()
        );
    }
}
