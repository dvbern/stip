package ch.dvbern.stip.api.gesuchsperioden.repo;

import java.time.LocalDate;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.repo.BaseRepository;
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

    public Stream<Gesuchsperiode> findAllActiveForDate(LocalDate date) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var gesuchsperiode = QGesuchsperiode.gesuchsperiode;

        var query = queryFactory
            .select(gesuchsperiode)
            .from(gesuchsperiode)
            .where(gesuchsperiode.aufschaltterminStart.before(date)
                .and(gesuchsperiode.aufschaltterminStopp.after(date)
                    .or(gesuchsperiode.aufschaltterminStopp.eq(date))));
        return query.stream();
    }

}
