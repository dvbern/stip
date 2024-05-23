package ch.dvbern.stip.api.benutzer.repo;

import java.util.HashSet;
import java.util.Set;

import ch.dvbern.stip.api.benutzer.entity.QRolle;
import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class RolleRepository implements BaseRepository<Rolle> {
    private final EntityManager entityManager;

    public Set<Rolle> findByKeycloakIdentifier(Set<String> keycloakIdentifiers) {
        final var qRolle = QRolle.rolle;

        final var fromDb = new JPAQueryFactory(entityManager)
            .selectFrom(qRolle)
            .where(qRolle.keycloakIdentifier.in(keycloakIdentifiers))
            .stream()
            .toList();

        return new HashSet<>(fromDb);
    }
}
