package ch.dvbern.stip.api.gesuchsperioden.repo;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.common.type.GueltigkeitStatus;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchsperioden.entity.QGesuchsperiode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchsperiodeRepository implements BaseRepository<Gesuchsperiode> {
    private final EntityManager entityManager;

    static final QGesuchsperiode gesuchsperiode = QGesuchsperiode.gesuchsperiode;

    public Stream<Gesuchsperiode> findAllActiveForDate(LocalDate date) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var query = queryFactory
            .selectFrom(gesuchsperiode)
            .where(gesuchsperiode.aufschaltterminStart.before(date)
                .and(gesuchsperiode.aufschaltterminStopp.after(date)
                    .or(gesuchsperiode.aufschaltterminStopp.eq(date))));
        return query.stream();
    }

    public Stream<Gesuchsperiode> findAllStartBefore(LocalDate date) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var query = queryFactory
            .selectFrom(gesuchsperiode)
            .where(gesuchsperiode.gueltigkeitStatus.eq(GueltigkeitStatus.PUBLIZIERT))
            .where(gesuchsperiode.gesuchsperiodeStart.before(date)
                .or(gesuchsperiode.aufschaltterminStart.eq(date))
            ).orderBy(gesuchsperiode.aufschaltterminStart.desc());
        return query.stream();
    }

    public Optional<Gesuchsperiode> getLatest() {
        return new JPAQueryFactory(entityManager)
            .selectFrom(gesuchsperiode)
            .orderBy(gesuchsperiode.timestampErstellt.desc())
            .stream()
            .findFirst();
    }
}
