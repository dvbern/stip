package ch.dvbern.stip.api.gesuch.repo;

import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.QGesuch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchRepository implements BaseRepository<Gesuch> {

	private final EntityManager entityManager;

	public Stream<Gesuch> findAllForBenutzer(UUID benutzerId) {
		var queryFactory = new JPAQueryFactory(entityManager);
		var gesuch = new QGesuch("gesuch");

		var query = queryFactory
				.select(gesuch)
				.from(gesuch)
				.where(gesuch.fall.gesuchsteller.id.eq(benutzerId).or(gesuch.fall.sachbearbeiter.id.eq(benutzerId)));
		return query.stream();
	}
}
