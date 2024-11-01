package ch.dvbern.stip.api.benutzer.repo;

import ch.dvbern.stip.api.benutzer.entity.QSozialdienstAdmin;
import ch.dvbern.stip.api.benutzer.entity.SozialdienstAdmin;
import ch.dvbern.stip.api.common.repo.BaseRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@ApplicationScoped
@RequiredArgsConstructor
public class SozialdienstAdminRepository implements BaseRepository<SozialdienstAdmin> {
    private final EntityManager entityManager;

    public Optional<SozialdienstAdmin> findByKeycloakId(String keycloakId) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var sozialdienstAdmin = QSozialdienstAdmin.sozialdienstAdmin;
        var query = queryFactory
            .select(sozialdienstAdmin)
            .from(sozialdienstAdmin)
            .where(sozialdienstAdmin.keycloakId.eq(keycloakId));
        return query.stream().findFirst();
    }
}
