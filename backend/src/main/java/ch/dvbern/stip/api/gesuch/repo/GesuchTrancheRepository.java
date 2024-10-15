package ch.dvbern.stip.api.gesuch.repo;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.entity.QGesuchTranche;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.NotFoundException;
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

    public GesuchTranche requireAenderungById(final UUID aenderungId) {
        final var gesuchTranche = QGesuchTranche.gesuchTranche;

        final var found = new JPAQueryFactory(em)
            .selectFrom(gesuchTranche)
            .where(gesuchTranche.id.eq(aenderungId))
            .fetchFirst();

        if (found != null) {
            return found;
        }

        throw new NotFoundException();
    }

    public Optional<GesuchTranche> findMostRecentCreatedTranche(final Gesuch gesuch) {
        final var gesuchTranche = QGesuchTranche.gesuchTranche;

        return new JPAQueryFactory(em)
            .selectFrom(gesuchTranche)
            .where(gesuchTranche.gesuch.id.eq(gesuch.getId()))
            .orderBy(gesuchTranche.timestampErstellt.desc())
            .stream()
            .findFirst();
    }
}
