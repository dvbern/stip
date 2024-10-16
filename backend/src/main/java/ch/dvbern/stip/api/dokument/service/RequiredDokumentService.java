package ch.dvbern.stip.api.dokument.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    private static List<GesuchDokument> getExistingDokumentsForGesuch(final GesuchFormular formular) {
        return formular
            .getTranche()
            .getGesuchDokuments();
    }

    private static List<DokumentTyp> getExistingDokumentTypesForGesuch(final GesuchFormular formular) {
        return getExistingDokumentsForGesuch(formular)
            .stream()
            .filter(dokument -> !dokument.getDokumente().isEmpty())
            .map(GesuchDokument::getDokumentTyp)
            .toList();
    }

    private Set<DokumentTyp> getRequiredDokumentTypesForGesuch(final GesuchFormular formular) {
        return requiredDocumentProducers
            .stream()
            .map(requiredDocumentProducer -> requiredDocumentProducer.getRequiredDocuments(formular))
            .flatMap(
                dokumentTypPair -> dokumentTypPair.getRight().stream()
            ).collect(Collectors.toSet());
    }

    public List<DokumentTyp> getRequiredDokumentsForGesuchFormular(final GesuchFormular formular) {
        final var existingDokumentTypesHashSet = new HashSet<>(
            getExistingDokumentTypesForGesuch(formular)
        );

        final var requiredDokumentTypes = getRequiredDokumentTypesForGesuch(formular);

        return requiredDokumentTypes
            .stream()
            .filter(
                requiredDokumentType -> !existingDokumentTypesHashSet.contains(requiredDokumentType)
            ).toList();
    }

    public List<GesuchDokument> getSuperfluousDokumentsForGesuch(final GesuchFormular formular) {
        final var existingDokumentTypes = getExistingDokumentTypesForGesuch(formular);

        final var requiredDokumentTypesHashSet = new HashSet<>(
            getRequiredDokumentTypesForGesuch(formular)
        );

        final var superfluousDokumentTypes = existingDokumentTypes
            .stream()
            .filter(
                existingDokumentType -> !requiredDokumentTypesHashSet.contains(existingDokumentType)
            ).toList();
        final var superfluousDokumentTypesHashSet = new HashSet<>(superfluousDokumentTypes);

        return getExistingDokumentsForGesuch(formular)
            .stream()
            .filter(
                existingDokument -> superfluousDokumentTypesHashSet.contains(existingDokument.getDokumentTyp())
            ).toList();
    }
}
