package ch.dvbern.stip.api.eltern.entity;

import java.util.Set;

import ch.dvbern.stip.api.common.validation.RequiredDocumentProducer;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@ApplicationScoped
@RequiredArgsConstructor
public class VaterRequiredDocumentsProducer implements RequiredDocumentProducer {
    private final ElternRequiredDocumentsProducer producer;

    @Override
    public Pair<String, Set<DokumentTyp>> getRequiredDocuments(GesuchFormular formular) {
        final var eltern = formular.getElterns();
        if (eltern.isEmpty()) {
            return ImmutablePair.of("", Set.of());
        }

        final var vater = eltern.stream()
            .filter(x -> x.getElternTyp() == ElternTyp.VATER)
            .findFirst().orElse(null);
        return ImmutablePair.of("elterns", producer.getForElternteil(vater, formular.getFamiliensituation()));
    }
}

