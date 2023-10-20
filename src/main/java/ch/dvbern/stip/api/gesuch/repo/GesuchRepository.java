package ch.dvbern.stip.api.gesuch.repo;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.QGesuch;
import ch.dvbern.stip.api.gesuch.entity.QGesuchFormular;
import ch.dvbern.stip.api.personinausbildung.entity.QPersonInAusbildung;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.stream.Stream;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchRepository implements BaseRepository<Gesuch> {

	private final EntityManager entityManager;

	public Stream<Gesuch> findAllForBenutzer(UUID benutzerId) {
		var queryFactory = new JPAQueryFactory(entityManager);
		var gesuch = QGesuch.gesuch;

		var query = queryFactory
				.select(gesuch)
				.from(gesuch)
				.where(gesuch.fall.gesuchsteller.id.eq(benutzerId).or(gesuch.fall.sachbearbeiter.id.eq(benutzerId)));
		return query.stream();
	}

	public Stream<Gesuch> findAllForFall(UUID fallId) {
		var queryFactory = new JPAQueryFactory(entityManager);
		var gesuch = QGesuch.gesuch;

		var query = queryFactory
				.select(gesuch)
				.from(gesuch)
				.where(gesuch.fall.id.eq(fallId));
		return query.stream();
	}

	public Stream<Gesuch> findAllWithFormularToWorkWith() {
		var queryFactory = new JPAQueryFactory(entityManager);
		var gesuch = QGesuch.gesuch;

		return queryFactory
				.select(gesuch)
				.from(gesuch)
				.where(gesuch.gesuchFormularToWorkWith.isNotNull())
				.stream();
	}

	public Stream<Gesuch> findGesucheBySvNummer(String svNummer) {
		var queryFactory = new JPAQueryFactory(entityManager);
		var gesuch = QGesuch.gesuch;
		var gesuchFormular = QGesuchFormular.gesuchFormular;
		var personInAubsilung = QPersonInAusbildung.personInAusbildung;

		return queryFactory.selectFrom(gesuch)
				.join(gesuch.gesuchFormularToWorkWith, gesuchFormular)
				.join(gesuchFormular.personInAusbildung, personInAubsilung)
				.where(personInAubsilung.sozialversicherungsnummer.eq(svNummer))
				.stream();
	}
}
