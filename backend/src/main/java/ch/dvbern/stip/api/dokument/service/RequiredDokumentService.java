package ch.dvbern.stip.api.dokument.service;

import java.util.HashSet;
import java.util.List;

import ch.dvbern.stip.api.common.validation.RequiredDocumentProducer;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class RequiredDokumentService {
    private final Instance<RequiredDocumentProducer> requiredDocumentProducers;

    public List<DokumentTyp> getRequiredDokumenteForGesuch(final GesuchFormular formular) {
        final var existingDokumentTypes = new HashSet<>(
            formular
                .getTranche()
                .getGesuch()
                .getGesuchDokuments()
                .stream()
                .map(GesuchDokument::getDokumentTyp)
                .toList()
        );

        return requiredDocumentProducers.stream()
            .map(x -> x.getRequiredDocuments(formular))
            .flatMap(x -> x.getRight().stream().filter(y -> !existingDokumentTypes.contains(y)))
            .toList();
    }
}
