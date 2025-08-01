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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.entity.QGesuchDokument;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.GesuchDokumentStatus;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchDokumentRepository implements BaseRepository<GesuchDokument> {

    private final EntityManager entityManager;

    public List<GesuchDokument> findAllByCustomDokumentTypId(UUID customDokumentTypId) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var gesuchDokument = QGesuchDokument.gesuchDokument;
        var query = queryFactory
            .selectFrom(gesuchDokument)
            .where(
                gesuchDokument.customDokumentTyp.id.eq(customDokumentTypId)
            );
        return query.stream().toList();
    }

    public Optional<GesuchDokument> findByGesuchTrancheAndDokumentTyp(UUID gesuchTrancheId, DokumentTyp dokumentTyp) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var gesuchDokument = QGesuchDokument.gesuchDokument;
        var query = queryFactory
            .selectFrom(gesuchDokument)
            .where(
                gesuchDokument.gesuchTranche.id.eq(gesuchTrancheId)
                    .and(gesuchDokument.dokumentTyp.eq(dokumentTyp))
            );
        return query.stream().findFirst();
    }

    public Stream<GesuchDokument> findAllForGesuchTranche(UUID gesuchTrancheId) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var gesuchDokument = QGesuchDokument.gesuchDokument;

        return queryFactory
            .selectFrom(gesuchDokument)
            .where(gesuchDokument.gesuchTranche.id.eq(gesuchTrancheId))
            .stream();
    }

    public void dropGesuchDokumentIfNoDokumente(final UUID gesuchDokumentId) {
        final var gesuchDokument = requireById(gesuchDokumentId);
        final var hasNoDokuments = gesuchDokument.getDokumente().isEmpty();
        final var isCustomGesuchDokument = Objects.nonNull(gesuchDokument.getCustomDokumentTyp());

        if (hasNoDokuments && !isCustomGesuchDokument) {
            gesuchDokument.getGesuchTranche().getGesuchDokuments().remove(gesuchDokument);
            deleteById(gesuchDokumentId);
        }
    }

    public Stream<GesuchDokument> getAllForGesuchInStatus(
        final Gesuch gesuch,
        final GesuchDokumentStatus gesuchDokumentStatus
    ) {
        final var gesuchDokument = QGesuchDokument.gesuchDokument;

        return new JPAQueryFactory(entityManager)
            .selectFrom(gesuchDokument)
            .where(
                gesuchDokument.gesuchTranche.id.in(
                    gesuch.getGesuchTranchen().stream().map(AbstractEntity::getId).toList()
                ).and(gesuchDokument.status.eq(gesuchDokumentStatus))
            )
            .stream();
    }

    public Optional<GesuchDokument> findByCustomDokumentTyp(
        UUID customDokumentTypId
    ) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var gesuchDokument = QGesuchDokument.gesuchDokument;
        var query = queryFactory
            .selectFrom(gesuchDokument)
            .where(
                gesuchDokument.customDokumentTyp.id.eq(customDokumentTypId)
            );
        return query.stream().findFirst();
    }

    public List<GesuchDokument> findAllOfTypeCustomByGesuchTrancheId(UUID gesuchTrancheId) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var gesuchDokument = QGesuchDokument.gesuchDokument;
        var query = queryFactory
            .selectFrom(gesuchDokument)
            .where(
                gesuchDokument.gesuchTranche.id.eq(gesuchTrancheId)
                    .and(gesuchDokument.customDokumentTyp.id.isNotNull())
            );
        return query.stream().toList();
    }

    public boolean customDokumentHasGesuchDokuments(UUID customDokumentTypId) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var gesuchDokument = QGesuchDokument.gesuchDokument;
        var query = queryFactory
            .selectFrom(gesuchDokument)
            .where(
                (gesuchDokument.customDokumentTyp.id.eq(customDokumentTypId))
                    .and(gesuchDokument.dokumente.isNotEmpty())
            );
        return query.stream().findAny().isPresent();
    }

    public Stream<GesuchDokument> findByDokumentTyps(final UUID trancheId, final List<DokumentTyp> dokumentTyps) {
        final var gesuchDokument = QGesuchDokument.gesuchDokument;

        return new JPAQueryFactory(entityManager)
            .selectFrom(gesuchDokument)
            .where(gesuchDokument.dokumentTyp.in(dokumentTyps).and(gesuchDokument.gesuchTranche.id.eq(trancheId)))
            .stream();
    }
}
