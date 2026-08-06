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
import ch.dvbern.stip.api.dokument.type.GesuchDokumentStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchDokumentKommentarRepository implements BaseRepository<GesuchDokumentKommentar> {
    private static QGesuchDokumentKommentar gesuchDokumentKommentar = QGesuchDokumentKommentar.gesuchDokumentKommentar;

    public record GesuchDokumentKommentarSlim(DokumentTyp typ, String customDokumentTyp, String kommentar) {
    }

    @Transactional
    public void deleteAllByGesuchDokumentId(UUID gesuchDokumentId) {
        new JPAQueryFactory(getEntityManager())
            .delete(gesuchDokumentKommentar)
            .where(gesuchDokumentKommentar.gesuchDokument.id.eq(gesuchDokumentId))
            .execute();
    }

    @Transactional
    public List<GesuchDokumentKommentarSlim> getAllNewestAbgelehntKommentarOfGesuch(final UUID gesuchId) {
        return getEntityManager().createQuery("""
            select gesuchDokument.dokumentTyp, customDokumentTyp.type, latestKommentar.kommentar
            from GesuchDokument gesuchDokument
            join lateral (
                select kommentar.kommentar as kommentar
                from GesuchDokumentKommentar kommentar
                where gesuchDokument = kommentar.gesuchDokument
                order by kommentar.timestampErstellt DESC
                limit 1
            ) latestKommentar
            left join gesuchDokument.customDokumentTyp customDokumentTyp
            where gesuchDokument.status = :status
                and gesuchDokument.gesuchTranche.gesuch.id = :gesuchId
                and gesuchDokument.gesuchTranche.typ = :typ
        """, GesuchDokumentKommentarSlim.class)
            .setParameter("status", GesuchDokumentStatus.ABGELEHNT)
            .setParameter("typ", GesuchTrancheTyp.TRANCHE)
            .setParameter("gesuchId", gesuchId)
            .getResultList();
    }

    @Transactional
    public List<GesuchDokumentKommentarSlim> getAllNewestAbgelehntKommentarOfAenderung(final UUID aenderungId) {
        return getEntityManager().createQuery("""
            select gesuchDokument.dokumentTyp, customDokumentTyp.type, latestKommentar.kommentar
            from GesuchDokument gesuchDokument
            join lateral (
                select kommentar.kommentar as kommentar
                from GesuchDokumentKommentar kommentar
                where gesuchDokument = kommentar.gesuchDokument
                order by kommentar.timestampErstellt DESC
                limit 1
            ) latestKommentar
            left join gesuchDokument.customDokumentTyp customDokumentTyp
            where gesuchDokument.status = :status
                and gesuchDokument.gesuchTranche.id = :aenderungId
                and gesuchDokument.gesuchTranche.typ = :typ
        """, GesuchDokumentKommentarSlim.class)
            .setParameter("status", GesuchDokumentStatus.ABGELEHNT)
            .setParameter("typ", GesuchTrancheTyp.AENDERUNG)
            .setParameter("aenderungId", aenderungId)
            .getResultList();
    }

    public List<GesuchDokumentKommentar> getByGesuchDokumentId(
        final UUID gesuchDokumentId
    ) {
        return new JPAQueryFactory(getEntityManager())
            .selectFrom(gesuchDokumentKommentar)
            .where(gesuchDokumentKommentar.gesuchDokument.id.eq(gesuchDokumentId))
            .orderBy(gesuchDokumentKommentar.timestampErstellt.desc())
            .fetch();
    }
}
