package ch.dvbern.stip.api.scheduledtask.repo;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.scheduledtask.entity.QScheduledtask;
import ch.dvbern.stip.api.scheduledtask.entity.Scheduledtask;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.stream.Stream;

@ApplicationScoped
@RequiredArgsConstructor
public class ScheduledtaskRepository implements BaseRepository<Scheduledtask> {
    private final EntityManager entityManager;

//    public Stream<Scheduledtask> findAllWithType(final String type) {
//        final var scheduledtask = QScheduledtask.scheduledtask;
//        return new JPAQueryFactory(entityManager)
//            .selectFrom(scheduledtask)
//            .where(scheduledtask.type.eq(type))
//            .orderBy(scheduledtask.timestamp.desc())
//            .stream();
//    }

    public Optional<Scheduledtask> findLastWithType(final String type) {
        final var scheduledtask = QScheduledtask.scheduledtask;
//        Persistence.getPersistenceUtil();
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory(unitname);
//        EntityManager em = emf.createEntityManager();

        return new JPAQueryFactory(entityManager)
            .selectFrom(scheduledtask)
            .where(scheduledtask.type.eq(type))
            .orderBy(scheduledtask.timestamp.desc())
            .stream()
            .findFirst();
    }
}
