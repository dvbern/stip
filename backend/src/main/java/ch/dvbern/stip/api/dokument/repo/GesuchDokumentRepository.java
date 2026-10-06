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
import ch.dvbern.stip.generated.dto.GesuchDokumentRefDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchDokumentRepository implements BaseRepository<GesuchDokument> {

    private final EntityManager entityManager;
    private final QGesuchDokument Q_GESUCH_DOKUMENT = QGesuchDokument.gesuchDokument;

    public List<GesuchDokument> findAllByCustomDokumentTypId(UUID customDokumentTypId) {
        final var queryFactory = new JPAQueryFactory(entityManager);

        final var query = queryFactory
            .selectFrom(Q_GESUCH_DOKUMENT)
            .where(Q_GESUCH_DOKUMENT.customDokumentTyp.id.eq(customDokumentTypId));

        return query.stream().toList();
    }

    public Optional<GesuchDokument> findByGesuchTrancheAndDokumentTypAndEntryIdIfSet(
        final UUID gesuchTrancheId,
        final DokumentTyp dokumentTyp,
        final UUID entryId
    ) {
        final var queryFactory = new JPAQueryFactory(entityManager);

        final var query = queryFactory
            .selectFrom(Q_GESUCH_DOKUMENT)
            .where(
                Q_GESUCH_DOKUMENT.gesuchTranche.id.eq(gesuchTrancheId)
                    .and(Q_GESUCH_DOKUMENT.dokumentTyp.eq(dokumentTyp))
                    .and(
                        Objects.isNull(entryId) ? Q_GESUCH_DOKUMENT.entryId.isNull()
                            : Q_GESUCH_DOKUMENT.entryId.eq(entryId)
                    )
            );

        return query.stream().findFirst();
    }

    public Stream<GesuchDokument> findAllForGesuchTranche(final UUID gesuchTrancheId) {
        final var queryFactory = new JPAQueryFactory(entityManager);

        final var query = queryFactory
            .selectFrom(Q_GESUCH_DOKUMENT)
            .where(Q_GESUCH_DOKUMENT.gesuchTranche.id.eq(gesuchTrancheId));

        return query.stream();
    }

    public List<String> getAllObjectIdsForGesuchDokuments(final List<GesuchDokument> gesuchDokuments) {
        final var queryFactory = new JPAQueryFactory(entityManager);

        final var query = queryFactory
            .select(Q_GESUCH_DOKUMENT.dokumente.any().objectId)
            .from(Q_GESUCH_DOKUMENT)
            .where(Q_GESUCH_DOKUMENT.id.in(gesuchDokuments.stream().map(AbstractEntity::getId).toList()));

        return query.stream().toList();
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
        final var queryFactory = new JPAQueryFactory(entityManager);

        final var query = queryFactory
            .selectFrom(Q_GESUCH_DOKUMENT)
            .where(
                Q_GESUCH_DOKUMENT.gesuchTranche.id.in(
                    gesuch.getTranchenTranchen().map(AbstractEntity::getId).toList()
                ).and(Q_GESUCH_DOKUMENT.status.eq(gesuchDokumentStatus))
            );

        return query.stream();
    }

    public Stream<GesuchDokument> findAllForGesuchTrancheInStatus(
        final UUID gesuchTrancheId,
        final GesuchDokumentStatus gesuchDokumentStatus
    ) {
        final var queryFactory = new JPAQueryFactory(entityManager);

        final var query = queryFactory
            .selectFrom(Q_GESUCH_DOKUMENT)
            .where(
                Q_GESUCH_DOKUMENT.gesuchTranche.id.eq(gesuchTrancheId)
                    .and(Q_GESUCH_DOKUMENT.status.eq(gesuchDokumentStatus))
            );

        return query.stream();

    }

    public Optional<GesuchDokument> findByCustomDokumentTyp(
        UUID customDokumentTypId
    ) {
        final var queryFactory = new JPAQueryFactory(entityManager);

        final var query = queryFactory
            .selectFrom(Q_GESUCH_DOKUMENT)
            .where(
                Q_GESUCH_DOKUMENT.customDokumentTyp.id.eq(customDokumentTypId)
            );

        return query.stream().findFirst();
    }

    public List<GesuchDokument> findAllOfTypeCustomByGesuchTrancheId(UUID gesuchTrancheId) {
        final var queryFactory = new JPAQueryFactory(entityManager);

        final var query = queryFactory
            .selectFrom(Q_GESUCH_DOKUMENT)
            .where(
                Q_GESUCH_DOKUMENT.gesuchTranche.id.eq(gesuchTrancheId)
                    .and(Q_GESUCH_DOKUMENT.customDokumentTyp.id.isNotNull())
            );

        return query.stream().toList();
    }

    public List<GesuchDokument> getAllOfTypeForGesuch(final UUID gesuchId, final DokumentTyp dokumentTyp) {
        final var queryFactory = new JPAQueryFactory(entityManager);

        final var query = queryFactory
            .selectFrom(Q_GESUCH_DOKUMENT)
            .where(Q_GESUCH_DOKUMENT.dokumentTyp.eq(dokumentTyp))
            .where(Q_GESUCH_DOKUMENT.gesuchTranche.gesuch.id.eq(gesuchId));

        return query.stream().toList();

    }

    public boolean customDokumentHasGesuchDokuments(UUID customDokumentTypId) {
        final var queryFactory = new JPAQueryFactory(entityManager);

        final var query = queryFactory
            .selectFrom(Q_GESUCH_DOKUMENT)
            .where(
                (Q_GESUCH_DOKUMENT.customDokumentTyp.id.eq(customDokumentTypId))
                    .and(Q_GESUCH_DOKUMENT.dokumente.isNotEmpty())
            );

        return query.stream().findAny().isPresent();
    }

    public Stream<GesuchDokument> findByDokumentRefs(
        final UUID trancheId,
        final List<GesuchDokumentRefDto> dokumentRefs
    ) {
        final var queryFactory = new JPAQueryFactory(entityManager);

        final var dokumentTyps = dokumentRefs.stream()
            .filter(ref -> Objects.isNull(ref.getEntryId()))
            .map(GesuchDokumentRefDto::getDokumentTyp)
            .toList();
        final var dokumentRefsWithEntryId =
            dokumentRefs.stream().filter(ref -> Objects.nonNull(ref.getEntryId())).toList();

        final var query = queryFactory
            .selectFrom(Q_GESUCH_DOKUMENT)
            .where(
                Q_GESUCH_DOKUMENT.gesuchTranche.id.eq(trancheId),
                // Match normal Dokument refs
                Q_GESUCH_DOKUMENT.dokumentTyp.in(dokumentTyps)
                    .and(Q_GESUCH_DOKUMENT.entryId.isNull())
                    .or(
                        // Or Dokument Refs for lists with entryIds
                        Q_GESUCH_DOKUMENT.dokumentTyp
                            .in(dokumentRefsWithEntryId.stream().map(GesuchDokumentRefDto::getDokumentTyp).toList())
                            .and(
                                Q_GESUCH_DOKUMENT.entryId
                                    .in(dokumentRefsWithEntryId.stream().map(GesuchDokumentRefDto::getEntryId).toList())
                            )
                    )
            );

        return query.stream();
    }
}
