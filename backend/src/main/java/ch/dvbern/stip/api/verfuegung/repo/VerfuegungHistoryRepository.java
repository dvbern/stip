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

package ch.dvbern.stip.api.verfuegung.repo;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.common.util.AuditEntityUtil;
import ch.dvbern.stip.api.verfuegung.entity.Verfuegung;
import ch.dvbern.stip.api.verfuegung.type.VerfuegungStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;

@ApplicationScoped
@RequiredArgsConstructor
public class VerfuegungHistoryRepository {
    private final EntityManager em;

    @Transactional
    public List<Pair<Verfuegung, DefaultRevisionEntity>> getVerfuegungsHistoryByGesuchId(final UUID gesuchId) {
        final var reader = AuditReaderFactory.get(em);
        @SuppressWarnings("unchecked")
        final List<Pair<Verfuegung, DefaultRevisionEntity>> verfuegungsHistory = reader
            .createQuery()
            .forRevisionsOfEntity(Verfuegung.class, false, true)
            .add(AuditEntity.property("gesuch_id").eq(gesuchId))
            .add(AuditEntity.revisionType().ne(RevisionType.DEL))
            .add(AuditEntity.revisionType().ne(RevisionType.ADD))
            .add(AuditEntity.property("verfuegungStatus").in(VerfuegungStatus.FINAL_STATUS))
            .add(AuditEntity.property("verfuegungStatus").hasChanged())
            .addOrder(AuditEntityUtil.revisionTimestamp().desc())
            .getResultList()
            .stream()
            .map(result -> {
                final var list = (Object[]) result;
                return Pair.of((Verfuegung) list[0], (DefaultRevisionEntity) list[1]);
            })
            .toList();
        return verfuegungsHistory;
    }
}
