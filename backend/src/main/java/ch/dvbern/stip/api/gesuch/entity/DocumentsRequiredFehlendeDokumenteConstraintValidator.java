package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DocumentsRequiredFehlendeDokumenteConstraintValidator
    implements ConstraintValidator<DocumentsRequiredFehlendeDokumenteConstraint, Gesuch> {
    @Override
    public boolean isValid(Gesuch gesuch, ConstraintValidatorContext context) {
        return gesuch.getGesuchDokuments().stream().allMatch(dok -> dok.getStatus() != Dokumentstatus.AUSSTEHEND);
    }
}
