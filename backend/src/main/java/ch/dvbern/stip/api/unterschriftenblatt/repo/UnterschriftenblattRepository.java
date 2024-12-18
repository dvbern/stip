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

package ch.dvbern.stip.api.unterschriftenblatt.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.dokument.entity.QDokument;
import ch.dvbern.stip.api.unterschriftenblatt.entity.QUnterschriftenblatt;
import ch.dvbern.stip.api.unterschriftenblatt.entity.Unterschriftenblatt;
import ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class UnterschriftenblattRepository implements BaseRepository<Unterschriftenblatt> {
    private final EntityManager entityManager;

    public Optional<Unterschriftenblatt> findByGesuchAndDokumentTyp(
        final UUID gesuchId,
        final UnterschriftenblattDokumentTyp dokumentTyp
    ) {
        final var queryFactory = new JPAQueryFactory(entityManager);
        final var unterschriftenblatt = QUnterschriftenblatt.unterschriftenblatt;
        return queryFactory
            .selectFrom(unterschriftenblatt)
            .where(unterschriftenblatt.gesuch.id.eq(gesuchId).and(unterschriftenblatt.dokumentTyp.eq(dokumentTyp)))
            .stream()
            .findFirst();
    }

    public Stream<Unterschriftenblatt> findByGesuchAndDokumentTyps(
        final UUID gesuchId,
        final List<UnterschriftenblattDokumentTyp> dokumentTyps
    ) {
        final var queryFactory = new JPAQueryFactory(entityManager);
        final var unterschriftenblatt = QUnterschriftenblatt.unterschriftenblatt;
        return queryFactory
            .selectFrom(unterschriftenblatt)
            .where(unterschriftenblatt.gesuch.id.eq(gesuchId).and(unterschriftenblatt.dokumentTyp.in(dokumentTyps)))
            .stream();
    }

    public Stream<Unterschriftenblatt> requireForGesuch(
        final UUID gesuchId
    ) {
        final var unterschriftenblatt = QUnterschriftenblatt.unterschriftenblatt;
        return new JPAQueryFactory(entityManager)
            .selectFrom(unterschriftenblatt)
            .where(unterschriftenblatt.gesuch.id.eq(gesuchId))
            .stream();
    }

    public Unterschriftenblatt requireByDokumentId(final UUID dokumentId) {
        final var unterschriftenblatt = QUnterschriftenblatt.unterschriftenblatt;
        final var dokument = QDokument.dokument;

        return new JPAQueryFactory(entityManager)
            .selectFrom(unterschriftenblatt)
            .join(dokument)
            .on(unterschriftenblatt.dokumente.contains(dokument))
            .where(dokument.id.eq(dokumentId))
            .stream()
            .findFirst()
            .orElseThrow(NotFoundException::new);
    }
}
