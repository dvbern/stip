package ch.dvbern.stip.api.dokument.entity;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.common.validation.RequiredDocumentProducer;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@ApplicationScoped
public class DokumentsRequiredDocumentProducer implements RequiredDocumentProducer {

    @Override
    public Pair<String, List<DokumentTyp>> getRequiredDocuments(GesuchFormular formular) {
        if (formular == null) {
            return ImmutablePair.of("", List.of());
        }

        final var requiredDocs = new ArrayList<DokumentTyp>();

        final var pia = formular.getPersonInAusbildung();
        final var kinds = formular.getKinds();
        if (!kinds.isEmpty() && pia != null && pia.getZivilstand() == Zivilstand.LEDIG) {
            requiredDocs.add(DokumentTyp.KINDER_UNTERHALTSVERTRAG_TRENNUNGSKONVENTION);
        }

        return ImmutablePair.of("dokuments", requiredDocs);
    }
}
