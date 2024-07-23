package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DocumentsRequiredFehlendeDokumenteConstraintValidator
    implements ConstraintValidator<DocumentsRequiredFehlendeDokumenteConstraint, Gesuch> {

    @Inject
    GesuchService gesuchService;

    @Override
    public boolean isValid(Gesuch gesuch, ConstraintValidatorContext context) {
        var anyAusstehend = false;
        var anyAbgelehnt = false;

        final var gesuchDokumentDtos = gesuchService.getGesuchDokumenteForGesuch(gesuch.getId());
        if (gesuchDokumentDtos.isEmpty()) {
            return true;
        }

        for (final var gesuchDokumentDto : gesuchDokumentDtos) {
            if (gesuchDokumentDto.getStatus() == Dokumentstatus.AUSSTEHEND) {
                anyAusstehend = true;
                break;
            }

            if (gesuchDokumentDto.getStatus() == Dokumentstatus.ABGELEHNT) {
                anyAbgelehnt = true;
            }
        }

        // Only return true if none are ausstehen and at least 1 is abgelehnt
        return !anyAusstehend && anyAbgelehnt;
    }
}
