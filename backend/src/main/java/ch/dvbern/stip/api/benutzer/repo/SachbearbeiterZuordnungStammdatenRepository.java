package ch.dvbern.stip.api.benutzer.repo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.benutzer.entity.QSachbearbeiterZuordnungStammdaten;
import ch.dvbern.stip.api.benutzer.entity.SachbearbeiterZuordnungStammdaten;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class SachbearbeiterZuordnungStammdatenRepository implements BaseRepository<SachbearbeiterZuordnungStammdaten> {
    private final EntityManager entityManager;

    public Optional<SachbearbeiterZuordnungStammdaten> findByBenutzerId(UUID benutzerId) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var sachbearbeiterZuordnungStammdaten = QSachbearbeiterZuordnungStammdaten.sachbearbeiterZuordnungStammdaten;
        var query = queryFactory
            .select(sachbearbeiterZuordnungStammdaten)
            .from(sachbearbeiterZuordnungStammdaten)
            .where(sachbearbeiterZuordnungStammdaten.benutzer.id.eq(benutzerId));
        return query.stream().findFirst();
    }

    public Stream<SachbearbeiterZuordnungStammdaten> findForBenutzers(List<UUID> benutzerIds) {
        final var szs = QSachbearbeiterZuordnungStammdaten.sachbearbeiterZuordnungStammdaten;
        return new JPAQueryFactory(entityManager)
            .selectFrom(szs)
            .where(szs.benutzer.id.in(benutzerIds))
            .stream();
    }
}
