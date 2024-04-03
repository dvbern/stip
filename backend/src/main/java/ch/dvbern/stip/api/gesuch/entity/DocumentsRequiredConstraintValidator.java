package ch.dvbern.stip.api.gesuch.entity;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.common.validation.RequiredDocumentProducer;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_DOCUMENTS_REQUIRED_MESSAGE;

@Slf4j
public class DocumentsRequiredConstraintValidator
    implements ConstraintValidator<DocumentsRequiredConstraint, GesuchFormular> {
    @Inject
    Instance<RequiredDocumentProducer> producers;

    @Override
    public boolean isValid(GesuchFormular formular, ConstraintValidatorContext context) {
        final var requiredDocs = producers.stream().map(x -> x.getRequiredDocuments(formular)).toList();
        final var dokumenteOfType = getRequiredDokumentTypes(formular);

        final var filtered = requiredDocs.stream()
            .filter(x -> x.getRight().stream().anyMatch(y -> !dokumenteOfType.contains(y)))
//            .map(Pair::getLeft)
            .toList();

        if (!filtered.isEmpty()) {
            return GesuchValidatorUtil.addProperty(
                context,
                VALIDATION_DOCUMENTS_REQUIRED_MESSAGE,
                filtered.toString()
            );
        }

        return true;
    }

    private Set<DokumentTyp> getRequiredDokumentTypes(GesuchFormular formular) {
        final Function<String, Set<DokumentTyp>> logAndReturn = path -> {
            LOG.error("If this happens in testing it's OK: {} on GesuchFormular with id '{}' is null", path, formular.getId());
            return Set.of();
        };

        final var tranche = formular.getTranche();
        if (tranche == null) {
            return logAndReturn.apply("GesuchTranche");
        }

        final var gesuch = tranche.getGesuch();
        if (gesuch == null) {
            return logAndReturn.apply("GesuchTranche->Gesuch");
        }

        final var gesuchDokumente = gesuch.getGesuchDokuments();
        if (gesuchDokumente == null) {
            return logAndReturn.apply("GesuchTranche->Gesuch->GesuchDokumente");
        }

        return gesuchDokumente.stream().map(GesuchDokument::getDokumentTyp).collect(Collectors.toSet());
    }
}
