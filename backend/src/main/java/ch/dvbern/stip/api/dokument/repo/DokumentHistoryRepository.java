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

package ch.dvbern.stip.api.dokument.repo;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.util.AuditEntityUtil;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.JoinType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;

@ApplicationScoped
@RequiredArgsConstructor
public class DokumentHistoryRepository {
    private final EntityManager em;

    @Transactional
    public Dokument findInHistoryById(UUID dokumentId) {
        final var reader = AuditReaderFactory.get(em);

        return (Dokument) reader.createQuery()
            .forRevisionsOfEntity(Dokument.class, true, true)
            .add(AuditEntity.id().eq(dokumentId))
            .addOrder(AuditEntityUtil.revisionTimestamp().asc())
            .setMaxResults(1)
            .getSingleResult();
    }

    public long countDistinctTranchenThatReferenceDokumentByObjectId(final String objectId) {
        final var reader = AuditReaderFactory.get(em);

        // We can't check due to type erasure
        // noinspection unchecked
        final var list = (List<GesuchTranche>) reader.createQuery()
            .forRevisionsOfEntity(GesuchTranche.class, true, true)
            .traverseRelation("gesuchDokuments", JoinType.INNER)
            .traverseRelation("dokumente", JoinType.INNER)
            .add(AuditEntity.property("objectId").eq(objectId))
            .getResultList();

        return list.stream()
            .map(AbstractEntity::getId)
            .distinct()
            .count();
    }
}
