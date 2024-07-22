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
        return gesuchService.getGesuchDokumenteForGesuch(gesuch.getId()).stream().allMatch(gesuchDokumentDto -> gesuchDokumentDto.getStatus() != Dokumentstatus.AUSSTEHEND);
    }
}
