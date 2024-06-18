package ch.dvbern.stip.api.plz.repo;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.plz.entity.Plz;
import ch.dvbern.stip.api.plz.entity.QPlz;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class PlzRepository implements BaseRepository<Plz> {
    private final EntityManager em;

    public boolean isPlzInKanton(String postleitzahl, String kantonsKuerzel) {
        final var plz = QPlz.plz1;
        final var flushmode = em.getFlushMode();
        em.setFlushMode(FlushModeType.COMMIT);
        var ret = new JPAQueryFactory(em)
            .selectFrom(plz)
            .where(
                plz.kantonskuerzel.equalsIgnoreCase(kantonsKuerzel)
                    .and(plz.plz.eq(postleitzahl))
            )
            .stream().findAny().isPresent();
        em.setFlushMode(flushmode);
        return ret;
    }
}
