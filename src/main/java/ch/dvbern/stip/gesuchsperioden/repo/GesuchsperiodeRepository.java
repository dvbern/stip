package ch.dvbern.stip.gesuchsperioden.repo;

import ch.dvbern.stip.common.repostitory.BaseRepository;
import ch.dvbern.stip.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.gesuchsperioden.entity.QGesuchsperiode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.stream.Stream;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchsperiodeRepository implements BaseRepository<Gesuchsperiode> {

    private final EntityManager entityManager;

    public Stream<Gesuchsperiode> findAllActiveForDate(LocalDate date) {
        var queryFactory = new JPAQueryFactory(entityManager);
        var gesuchsperiode = new QGesuchsperiode("gesuchsperiode");

        var query = queryFactory
                .select(gesuchsperiode)
                .from(gesuchsperiode)
                .where(gesuchsperiode.aufschaltdatum.before(date)
                        .and(gesuchsperiode.gueltigkeit.gueltigBis.after(date)
                                .or(gesuchsperiode.gueltigkeit.gueltigBis.eq(date))));
        return query.stream();
    }

}