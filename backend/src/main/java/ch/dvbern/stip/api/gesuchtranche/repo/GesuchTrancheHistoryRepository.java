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

package ch.dvbern.stip.api.gesuchtranche.repo;

import java.util.UUID;

import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchTrancheHistoryRepository {
    private final EntityManager em;

    public GesuchTranche getInitialRevision(final UUID gesuchTrancheId) {
        final var reader = AuditReaderFactory.get(em);

        return (GesuchTranche) reader.createQuery()
            .forRevisionsOfEntity(GesuchTranche.class, true, true)
            .add(AuditEntity.id().eq(gesuchTrancheId))
            .addOrder(AuditEntity.revisionNumber().asc())
            .setMaxResults(1)
            .getSingleResult();
    }

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

    @SuppressWarnings("unchecked")
    // Reason: forRevisionsOfEntity with Gesuch.class and selectEntitiesOnly will always return a List<Gesuch>
    public GesuchTranche getLatestWhereGesuchStatusChangedToVerfuegt(final UUID gesuchId) {
        return findNewestGesuchTrancheOfGesuchInStatus(gesuchId, Gesuchstatus.VERFUEGT);
    }

    @SuppressWarnings("unchecked")
    // Reason: forRevisionsOfEntity with Gesuch.class and selectEntitiesOnly will always return a List<Gesuch>
    public GesuchTranche getLatestWhereGesuchStatusChangedToEingereicht(final UUID gesuchId) {
        return findNewestGesuchTrancheOfGesuchInStatus(gesuchId, Gesuchstatus.EINGEREICHT);
    }

    private GesuchTranche findNewestGesuchTrancheOfGesuchInStatus(final UUID gesuchId, Gesuchstatus gesuchStatus) {
        try {
            final var reader = AuditReaderFactory.get(em);
            return (GesuchTranche) reader
                .createQuery()
                .forRevisionsOfEntity(GesuchTranche.class, true, true)
                .add(AuditEntity.property("gesuch_id").eq(gesuchId))
                .traverseRelation("gesuch", JoinType.INNER, "g")
                .up()
                .add(
                    AuditEntity.and(
                        AuditEntity.property("g", "gesuchStatus").eq(gesuchStatus),
                        AuditEntity.property("g", "gesuchStatus").hasChanged()
                    )
                )
                .addOrder(AuditEntity.revisionNumber().asc()) // ältester eintrag in history nehmen
                .setMaxResults(1)
                .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }

    }

}
