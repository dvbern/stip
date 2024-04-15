package ch.dvbern.stip.api.kind.entity;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.common.validation.RequiredDocumentProducer;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@ApplicationScoped
@RequiredArgsConstructor
public class KindRequiredDocumentsProducer implements RequiredDocumentProducer {
	@Override
	public Pair<String, List<DokumentTyp>> getRequiredDocuments(GesuchFormular formular) {
		final var kinds = formular.getKinds();
		if (kinds == null) {
			return ImmutablePair.of("", List.of());
		}

		final var requiredDocs = new ArrayList<DokumentTyp>();
		kinds.forEach( kind ->
				{
					if (kind.getErhalteneAlimentebeitraege() != null) {
						requiredDocs.add(DokumentTyp.KINDER_ALIMENTENVERORDUNG);
					}
				}
		);
		return ImmutablePair.of("kind", requiredDocs);
	}
}
