package ch.dvbern.stip.api.dokument.repo;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.entity.QGesuchDokument;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchDokumentRepository implements BaseRepository<GesuchDokument> {

    private final EntityManager entityManager;

    public Optional<GesuchDokument> findByGesuchTrancheAndDokumentType(UUID gesuchTrancheId, DokumentTyp dokumentTyp) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var gesuchDokument = QGesuchDokument.gesuchDokument;
        var query = queryFactory
            .select(gesuchDokument)
            .from(gesuchDokument)
            .where(gesuchDokument.gesuchTranche.id.eq(gesuchTrancheId)
                .and(gesuchDokument.dokumentTyp.eq(dokumentTyp)));
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
        final var hasDokuments = gesuchDokument.getDokumente().isEmpty();

        if (!hasDokuments) {
            gesuchDokument.getGesuchTranche().getGesuchDokuments().remove(gesuchDokument);
            deleteById(gesuchDokumentId);
        }
    }

    public Stream<GesuchDokument> getAllForGesuchInStatus(final Gesuch gesuch, final Dokumentstatus dokumentstatus) {
        final var gesuchDokument = QGesuchDokument.gesuchDokument;

        return new JPAQueryFactory(entityManager)
            .selectFrom(gesuchDokument)
            .where(gesuchDokument.gesuchTranche.id.in(
                gesuch.getGesuchTranchen().stream().map(AbstractEntity::getId).toList()
            ).and(gesuchDokument.status.eq(dokumentstatus)))
            .stream();
    }
}
