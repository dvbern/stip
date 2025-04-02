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

import ch.dvbern.stip.api.dokument.entity.GesuchDokumentKommentar;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchDokumentKommentarHistoryRepository {
    private final EntityManager em;

    public List<GesuchDokumentKommentar> getGesuchDokumentKommentarOfGesuchDokumentAtRevision(
        final UUID gesuchDokumentId,
        final Integer revision
    ) {
        @SuppressWarnings("unchecked")
        final List<GesuchDokumentKommentar> revisions = AuditReaderFactory.get(em)
            .createQuery()
            .forEntitiesAtRevision(GesuchDokumentKommentar.class, revision)
            .add(AuditEntity.revisionType().ne(RevisionType.DEL))
            .add(AuditEntity.relatedId("gesuchDokument").eq(gesuchDokumentId))
            .getResultList()
            .stream()
            .map(GesuchDokumentKommentar.class::cast)
            .toList();
        return revisions;
    }
}
