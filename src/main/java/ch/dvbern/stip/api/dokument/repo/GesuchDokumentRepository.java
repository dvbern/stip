package ch.dvbern.stip.api.dokument.repo;

import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.common.repo.BaseRepository;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.entity.QGesuchDokument;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchDokumentRepository implements BaseRepository<GesuchDokument> {

	private final EntityManager entityManager;

	public Optional<GesuchDokument> findByGesuchAndDokumentType(UUID gesuchId, DokumentTyp dokumentTyp) {
		var queryFactory = new JPAQueryFactory(entityManager);
		var gesuchDokument = new QGesuchDokument("gesuchDokument");

		var query = queryFactory
				.select(gesuchDokument)
				.from(gesuchDokument)
				.where(gesuchDokument.gesuch.id.eq(gesuchId).and(gesuchDokument.dokumentTyp.eq(dokumentTyp)));
		return query.stream().findFirst();
	}
}
