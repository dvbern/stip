package ch.dvbern.stip.api.dokument.repo;

import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.dokument.entity.GesuchDokumentKommentar;
import ch.dvbern.stip.api.dokument.entity.QGesuchDokumentKommentar;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchDokumentKommentarRepository implements BaseRepository<GesuchDokumentKommentar> {
    private final EntityManager entityManager;

    public Optional<GesuchDokumentKommentar> getByTypAndGesuchTrancheId(
        final DokumentTyp dokumentTyp,
        final UUID gesuchTrancheId
    ) {
        final var gesuchDokumentKommentar = QGesuchDokumentKommentar.gesuchDokumentKommentar;
        return Optional.ofNullable(
            new JPAQueryFactory(entityManager)
                .selectFrom(gesuchDokumentKommentar)
                .where(
                    gesuchDokumentKommentar.gesuchTranche.id.eq(gesuchTrancheId)
                        .and(gesuchDokumentKommentar.dokumentTyp.eq(dokumentTyp))
                )
                .fetchOne()
        );
    }
}
