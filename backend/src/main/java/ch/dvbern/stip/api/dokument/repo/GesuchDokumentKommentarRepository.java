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
    public void deleteAllForGesuch(final UUID gesuchId) {
        final var kommentar = QGesuchDokumentKommentar.gesuchDokumentKommentar;
        new JPAQueryFactory(entityManager)
            .delete(kommentar)
            .where(kommentar.gesuch.id.eq(gesuchId))
            .execute();
    }

    public List<GesuchDokumentKommentar> getByTypAndGesuchId(
        final DokumentTyp dokumentTyp,
        final UUID gesuchId
    ) {
        return
            new JPAQueryFactory(entityManager)
                .selectFrom(gesuchDokumentKommentar)
                .where(
                    gesuchDokumentKommentar.gesuch.id.eq(gesuchId)
                        .and(gesuchDokumentKommentar.dokumentTyp.eq(dokumentTyp))
                )
                .orderBy(gesuchDokumentKommentar.timestampErstellt.desc())
                .fetch();
    }
}
