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

import java.util.UUID;

import ch.dvbern.stip.api.dokument.entity.Dokument;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;

@ApplicationScoped
@RequiredArgsConstructor
public class DokumentHistoryRepository {
    private final EntityManager em;

    public Dokument findInHistoryById(UUID dokumentId) {
        final var reader = AuditReaderFactory.get(em);

        return (Dokument) reader.createQuery()
            .forRevisionsOfEntity(Dokument.class, true, true)
            .add(AuditEntity.id().eq(dokumentId))
            .addOrder(AuditEntity.revisionNumber().asc())
            .setMaxResults(1)
            .getSingleResult();
    }

    public Dokument findFirstInHistoryById(UUID dokumentId) {
        final var reader = AuditReaderFactory.get(em);

        return (Dokument) reader.createQuery()
            .forRevisionsOfEntity(Dokument.class, true, true)
            .add(AuditEntity.id().eq(dokumentId))
            .addOrder(AuditEntity.revisionNumber().desc())
            .setMaxResults(1)
            .getSingleResult();
    }
}
