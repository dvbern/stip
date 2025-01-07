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

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.dokument.entity.GesuchDokumentKommentar;
import ch.dvbern.stip.api.dokument.entity.QGesuchDokumentKommentar;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchDokumentKommentarRepository implements BaseRepository<GesuchDokumentKommentar> {
    private final EntityManager entityManager;
    private static QGesuchDokumentKommentar gesuchDokumentKommentar = QGesuchDokumentKommentar.gesuchDokumentKommentar;

    @Transactional
    public void deleteAllForGesuchTranche(final UUID gesuchTrancheId) {
        new JPAQueryFactory(entityManager)
            .delete(gesuchDokumentKommentar)
            .where(gesuchDokumentKommentar.gesuchTranche.id.eq(gesuchTrancheId))
            .execute();
    }

    public List<GesuchDokumentKommentar> getByTypAndGesuchTrancheId(
        final DokumentTyp dokumentTyp,
        final UUID gesuchTrancheId
    ) {
        return new JPAQueryFactory(entityManager)
            .selectFrom(gesuchDokumentKommentar)
            .where(
                gesuchDokumentKommentar.gesuchTranche.id.eq(gesuchTrancheId)
                    .and(gesuchDokumentKommentar.dokumentTyp.eq(dokumentTyp))
            )
            .orderBy(gesuchDokumentKommentar.timestampErstellt.desc())
            .fetch();
    }

    public List<GesuchDokumentKommentar> getByGesuchTrancheId(
        final UUID gesuchTrancheId
    ) {
        return new JPAQueryFactory(entityManager)
            .selectFrom(gesuchDokumentKommentar)
            .where(
                gesuchDokumentKommentar.gesuchTranche.id.eq(gesuchTrancheId)
            )
            .orderBy(gesuchDokumentKommentar.timestampErstellt.desc())
            .fetch();
    }
}
