package ch.dvbern.stip.api.gesuch.entity;

import java.util.ArrayList;
import java.util.Arrays;

import ch.dvbern.stip.api.common.util.ReflectionUtil;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.DokumentTypMap;
import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_DOCUMENTS_REQUIRED_MESSAGE;

public class DocumentsRequiredConstraintValidator
    implements ConstraintValidator<DocumentsRequiredConstraint, GesuchFormular> {
    @Inject
    GesuchDokumentRepository gesuchDokumentRepository;

    @Override
    public boolean isValid(GesuchFormular value, ConstraintValidatorContext context) {
        // Get all properties on child pages from GesuchFormular that have @RequiresDocumentIf
        final var requiredDokumente = ReflectionUtil.getRequiresDocumentIfFields(value)
            .stream()
            .filter(x -> !x.getRight().isEmpty())
            .toList();

        // Iterate over all properties and construct query including only document types that are required
        final var requireAnyAmountOfType = new ArrayList<Pair<String, DokumentTyp>>();
        for (final var requiredDokument : requiredDokumente) {
            for (final var fieldValueAnnotation : requiredDokument.getRight()) {
                if (fieldValueAnnotation.getLeft() == null) {
                    continue;
                }

                final var currentValue = fieldValueAnnotation.getLeft().toString();
                final var annotation = fieldValueAnnotation.getRight();

                if (Arrays.asList(annotation.valueIs()).contains(currentValue)) {
                    final var requiredDokType = DokumentTypMap.getDokumentTypFromValue(currentValue);
                    if (requiredDokType != null) {
                        requireAnyAmountOfType.add(new ImmutablePair<>(requiredDokument.getLeft(), requiredDokType));
                    }
                }
            }
        }

        // Execute query and map back to missing properties
        final var dokumenteOfType = gesuchDokumentRepository.findAllForGesuchFormularWithType(
            value.getId(),
            requireAnyAmountOfType.stream().map(Pair::getRight).toList()
        ).map(Enum::name).toList();

        final var filtered = requireAnyAmountOfType.stream().filter(x -> !dokumenteOfType.contains(x.getRight().toString())).toList();

        if (!filtered.isEmpty()) {
            final var properties = filtered.stream().map(Pair::getLeft).toList();
            return GesuchValidatorUtil.addProperty(
                context,
                VALIDATION_DOCUMENTS_REQUIRED_MESSAGE,
                properties
            );
        }

        return true;
    }
}
