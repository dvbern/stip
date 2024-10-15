package ch.dvbern.stip.api.common.validation;

import java.util.List;
import java.util.Set;

import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import org.apache.commons.lang3.tuple.Pair;

public interface RequiredDocumentProducer {
    Pair<String, Set<DokumentTyp>> getRequiredDocuments(GesuchFormular formular);
}
