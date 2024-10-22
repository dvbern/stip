package ch.dvbern.stip.api.notiz.repo;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.notiz.entity.GesuchNotiz;
import ch.dvbern.stip.api.notiz.entity.QGesuchNotiz;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@ApplicationScoped
public class GesuchNotizRepository implements BaseRepository<GesuchNotiz> {

    private final EntityManager entityManager;

    public List<GesuchNotiz> findAllByGesuchId(UUID gesuchId) {
        final var gesuchNotiz = QGesuchNotiz.gesuchNotiz;

        return new JPAQueryFactory(entityManager)
            .selectFrom(gesuchNotiz)
            .where(gesuchNotiz.gesuchId.eq(gesuchId))
            .stream()
            .toList();
    }
}
