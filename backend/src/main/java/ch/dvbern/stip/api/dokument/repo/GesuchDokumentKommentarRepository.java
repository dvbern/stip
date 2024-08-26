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
        return
            new JPAQueryFactory(entityManager)
                .selectFrom(gesuchDokumentKommentar)
                .where(
                    gesuchDokumentKommentar.gesuchTranche.id.eq(gesuchTrancheId)
                        .and(gesuchDokumentKommentar.dokumentTyp.eq(dokumentTyp))
                )
                .orderBy(gesuchDokumentKommentar.timestampErstellt.desc())
                .fetch();
    }
}
