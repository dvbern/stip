package ch.dvbern.stip.api.auszahlung.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.auszahlung.type.Kontoinhaber;
import ch.dvbern.stip.api.common.validation.RequiredDocumentProducer;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@ApplicationScoped
public class AuszahlungRequiredDocumentsProducer implements RequiredDocumentProducer {
    @Override
    public Pair<String, Set<DokumentTyp>> getRequiredDocuments(GesuchFormular formular) {
        final var auszahlung = formular.getAuszahlung();
        if (auszahlung == null) {
            return ImmutablePair.of("", Set.of());
        }

        final var requiredDocs = new HashSet<DokumentTyp>();
        if (auszahlung.getKontoinhaber() == Kontoinhaber.SOZIALDIENST_INSTITUTION ||
            auszahlung.getKontoinhaber() == Kontoinhaber.ANDERE) {
            requiredDocs.add(DokumentTyp.AUSZAHLUNG_ABTRETUNGSERKLAERUNG);
        }

        return ImmutablePair.of("auszahlung", requiredDocs);
    }
}
