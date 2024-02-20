package ch.dvbern.stip.api.benutzer.repo;

import java.util.Optional;
import java.util.stream.Stream;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.QBenutzer;
import ch.dvbern.stip.api.benutzer.type.BenutzerTyp;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class BenutzerRepository implements BaseRepository<Benutzer> {

    private final EntityManager entityManager;

    public Optional<Benutzer> findByKeycloakId(String keycloakId) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var benutzer = QBenutzer.benutzer;
        var query = queryFactory
            .select(benutzer)
            .from(benutzer)
            .where(benutzer.keycloakId.eq(keycloakId));
        return query.stream().findFirst();
    }

    public Stream<Benutzer> findByBenutzerTyp(BenutzerTyp benutzerTyp) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var benutzer = QBenutzer.benutzer;

        var query = queryFactory
            .select(benutzer)
            .from(benutzer)
            .where(benutzer.benutzerTyp.eq(benutzerTyp));
        return query.stream();
    }
}
