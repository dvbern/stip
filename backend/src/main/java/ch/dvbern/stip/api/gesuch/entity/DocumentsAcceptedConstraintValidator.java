package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DocumentsAcceptedConstraintValidator
    implements ConstraintValidator<DocumentsAcceptedConstraint, GesuchFormular> {
    @Override
    public boolean isValid(GesuchFormular formular, ConstraintValidatorContext context) {
        return formular.getTranche()
            .getGesuchDokuments()
            .stream()
            .allMatch(gesuchDokument -> gesuchDokument.getStatus() == Dokumentstatus.AKZEPTIERT);
    }
}
