package ch.dvbern.stip.api.gesuch.entity;

import java.util.stream.Collectors;

import ch.dvbern.stip.api.common.validation.RequiredDocumentProducer;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.tuple.Pair;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_DOCUMENTS_REQUIRED_MESSAGE;

public class DocumentsRequiredConstraintValidator
    implements ConstraintValidator<DocumentsRequiredConstraint, GesuchFormular> {
    //    @Inject
    //    GesuchDokumentRepository gesuchDokumentRepository;
    //
    @Inject
    Instance<RequiredDocumentProducer> producers;

    @Override
    public boolean isValid(GesuchFormular value, ConstraintValidatorContext context) {
        final var requiredDocs = producers.stream().map(x -> x.getRequiredDocuments(value)).toList();
        final var dokumenteOfType = value.getTranche().getGesuch().getGesuchDokuments().stream().map(GesuchDokument::getDokumentTyp).collect(Collectors.toSet());
        //        final var dokumenteOfType = gesuchDokumentRepository.findAllForGesuchFormularWithType(
        //            value.getId(),
        //            requiredDocs.stream().flatMap(x -> x.getRight().stream()).toList()
        //        ).collect(Collectors.toSet());

        final var filtered = requiredDocs.stream()
            .filter(x -> x.getRight().stream().anyMatch(y -> !dokumenteOfType.contains(y)))
            .map(Pair::getLeft)
            .toList();

        if (!filtered.isEmpty()) {
            return GesuchValidatorUtil.addProperty(
                context,
                VALIDATION_DOCUMENTS_REQUIRED_MESSAGE,
                filtered
            );
        }

        return true;
    }
}
