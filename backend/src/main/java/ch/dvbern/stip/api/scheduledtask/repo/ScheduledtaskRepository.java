package ch.dvbern.stip.api.scheduledtask.repo;

import java.util.Optional;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.scheduledtask.entity.QScheduledtask;
import ch.dvbern.stip.api.scheduledtask.entity.Scheduledtask;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class ScheduledtaskRepository implements BaseRepository<Scheduledtask> {
    private final EntityManager entityManager;

    public Optional<Scheduledtask> findLatestWithType(final String type) {
        final var scheduledtask = QScheduledtask.scheduledtask;

        return new JPAQueryFactory(entityManager)
            .selectFrom(scheduledtask)
            .where(scheduledtask.type.eq(type))
            .orderBy(scheduledtask.timestamp.desc())
            .stream()
            .findFirst();
    }
}
