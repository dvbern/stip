package ch.dvbern.stip.api.zuordnung.repo;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.zuordnung.entity.QZuordnung;
import ch.dvbern.stip.api.zuordnung.entity.Zuordnung;
import ch.dvbern.stip.api.zuordnung.type.ZuordnungType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@ApplicationScoped
public class ZuordnungRepository implements BaseRepository<Zuordnung> {
    private final EntityManager entityManager;

    public Stream<Zuordnung> findAllWithType(final ZuordnungType type) {
        final var zuordnung = QZuordnung.zuordnung;
        return new JPAQueryFactory(entityManager)
            .selectFrom(zuordnung)
            .where(zuordnung.zuordnungType.eq(type))
            .stream();
    }

    public void deleteByFallIds(Set<UUID> fallIds) {
        final var zuordnung = QZuordnung.zuordnung;

        new JPAQueryFactory(entityManager)
            .delete(zuordnung)
            .where(zuordnung.fall.id.in(fallIds))
            .execute();
    }
}
