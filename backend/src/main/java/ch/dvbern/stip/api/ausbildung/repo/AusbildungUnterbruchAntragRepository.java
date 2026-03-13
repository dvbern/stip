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

package ch.dvbern.stip.api.ausbildung.repo;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.AusbildungUnterbruchAntrag;
import ch.dvbern.stip.api.ausbildung.entity.QAusbildungUnterbruchAntrag;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class AusbildungUnterbruchAntragRepository implements BaseRepository<AusbildungUnterbruchAntrag> {
    private final EntityManager entityManager;
    public static final QAusbildungUnterbruchAntrag Q_AUSBILDUNG_UNTERBRUCH_ANTRAG =
        QAusbildungUnterbruchAntrag.ausbildungUnterbruchAntrag;

    public AusbildungUnterbruchAntrag requireByDokumentId(final UUID dokumentId) {
        return new JPAQueryFactory(entityManager)
            .selectFrom(Q_AUSBILDUNG_UNTERBRUCH_ANTRAG)
            .where(Q_AUSBILDUNG_UNTERBRUCH_ANTRAG.dokuments.any().id.eq(dokumentId))
            .fetchFirst();
    }

    public List<AusbildungUnterbruchAntrag> getAusbildungUnterbruchAntragsByGesuchId(final UUID gesuchId) {
        return new JPAQueryFactory(entityManager)
            .selectFrom(Q_AUSBILDUNG_UNTERBRUCH_ANTRAG)
            .where(Q_AUSBILDUNG_UNTERBRUCH_ANTRAG.gesuch.id.eq(gesuchId))
            .stream()
            .toList();
    }
}
