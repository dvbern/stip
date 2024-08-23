package ch.dvbern.stip.api.gesuch.repo;

import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.entity.QGesuchTranche;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchTrancheRepository implements BaseRepository<GesuchTranche> {
    private final EntityManager em;

    public Stream<GesuchTranche> findForGesuch(final UUID gesuchId) {
        final var gesuchTranche = QGesuchTranche.gesuchTranche;

        return new JPAQueryFactory(em)
            .selectFrom(gesuchTranche)
            .where(gesuchTranche.gesuch.id.eq(gesuchId))
            .orderBy(gesuchTranche.gueltigkeit.gueltigAb.asc())
            .stream();
    }
}
