package ch.dvbern.stip.api.dokument.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.entity.QDokument;
import ch.dvbern.stip.api.dokument.entity.QGesuchDokument;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.QGesuch;
import ch.dvbern.stip.api.gesuch.entity.QGesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.QGesuchTranche;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchDokumentRepository implements BaseRepository<GesuchDokument> {

    private final EntityManager entityManager;

    public Optional<GesuchDokument> findByGesuchAndDokumentType(UUID gesuchId, DokumentTyp dokumentTyp) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var gesuchDokument = QGesuchDokument.gesuchDokument;
        var query = queryFactory
            .select(gesuchDokument)
            .from(gesuchDokument)
            .where(gesuchDokument.gesuch.id.eq(gesuchId)
                .and(gesuchDokument.dokumentTyp.eq(dokumentTyp)));
        return query.stream().findFirst();
    }

    public Stream<GesuchDokument> findAllForGesuch(UUID gesuchId) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var gesuchDokument = QGesuchDokument.gesuchDokument;

        var query = queryFactory
            .select(gesuchDokument)
            .from(gesuchDokument)
            .where(gesuchDokument.gesuch.id.eq(gesuchId));
        return query.stream();
    }

    /**
     * Returns a list of distinct {@link DokumentTyp} that are present for a {@link GesuchFormular} that are in the
	 * given types
     */
    public Stream<DokumentTyp> findAllForGesuchFormularWithType(final UUID gesuchFormularId, final List<DokumentTyp> types) {
        final var gesuchDokument = QGesuchDokument.gesuchDokument;
        final var gesuch = QGesuch.gesuch;
        final var gesuchTranche = QGesuchTranche.gesuchTranche;
        final var gesuchFormular = QGesuchFormular.gesuchFormular;

        final var query = new JPAQueryFactory(entityManager)
            .select(gesuchDokument.dokumentTyp)
            .from(gesuchDokument)
            .join(gesuch).on(gesuch.id.eq(gesuchDokument.gesuch.id))
            .join(gesuchTranche).on(gesuchTranche.gesuch.id.eq(gesuch.id))
            .join(gesuchFormular).on(gesuchFormular.id.eq(gesuchTranche.gesuchFormular.id))
            .where(
                gesuchFormular.id.eq(gesuchFormularId)
                    .and(gesuchDokument.dokumentTyp.in(types))
            ).distinct();

        return query.stream();
    }

    public void dropGesuchDokumentIfNoDokumente(final UUID gesuchDokumentId) {
        final var dokument = QDokument.dokument;

        final var dokuments = new JPAQueryFactory(entityManager)
            .selectFrom(dokument)
            .where(dokument.gesuchDokument.id.eq(gesuchDokumentId))
            .stream().count();

        if (dokuments == 0) {
            deleteById(gesuchDokumentId);
        }
    }
}
