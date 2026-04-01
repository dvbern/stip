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
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.dokument.entity.QDokument;
import ch.dvbern.stip.api.dokument.entity.QSachbearbeiterGesuchDokument;
import ch.dvbern.stip.api.dokument.entity.SachbearbeiterGesuchDokument;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class SachbearbeiterGesuchDokumentRepository implements BaseRepository<SachbearbeiterGesuchDokument> {
    final QSachbearbeiterGesuchDokument sachbearbeiterGesuchDokument =
        QSachbearbeiterGesuchDokument.sachbearbeiterGesuchDokument;
    final QDokument dokument = QDokument.dokument;

    public SachbearbeiterGesuchDokument requireByDokumentId(final UUID dokumentId) {
        return new JPAQueryFactory(getEntityManager())
            .selectFrom(sachbearbeiterGesuchDokument)
            .join(dokument)
            .on(sachbearbeiterGesuchDokument.dokumente.contains(dokument))
            .where(dokument.id.eq(dokumentId))
            .stream()
            .findFirst()
            .orElseThrow(NotFoundException::new);
    }

    public Stream<SachbearbeiterGesuchDokument> getAllByGesuchId(final UUID gesuchId) {
        return new JPAQueryFactory(getEntityManager())
            .selectFrom(sachbearbeiterGesuchDokument)
            .where(sachbearbeiterGesuchDokument.gesuch.id.eq(gesuchId))
            .stream();
    }
}
