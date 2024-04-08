package ch.dvbern.stip.api.geschwister.entity;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import ch.dvbern.stip.api.common.validation.RequiredDocumentProducer;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@ApplicationScoped
public class GeschwisterRequiredDocumentsProducer implements RequiredDocumentProducer {
    @Override
    public Pair<String, List<DokumentTyp>> getRequiredDocuments(GesuchFormular formular) {
        final var geschwister = formular.getGeschwisters();
        if (geschwister == null) {
            return ImmutablePair.of("", List.of());
        }

        final var requiredDocs = new ArrayList<DokumentTyp>();

        if (formular.getGeschwisters()
            .stream()
            .anyMatch(x -> x.getAusbildungssituation() == Ausbildungssituation.IN_AUSBILDUNG)
        ) {
            requiredDocs.add(DokumentTyp.GESCHWISTER_BESTAETIGUNG_AUSBILDUNGSSTAETTE);
        }

        return ImmutablePair.of("geschwister", requiredDocs);
    }
}
