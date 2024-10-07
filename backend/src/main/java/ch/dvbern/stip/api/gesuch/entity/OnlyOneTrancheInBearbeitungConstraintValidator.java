package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OnlyOneTrancheInBearbeitungConstraintValidator
    implements ConstraintValidator<OnlyOneTrancheInBearbeitungConstraint, Gesuch> {
    @Override
    public boolean isValid(Gesuch gesuch, ConstraintValidatorContext context) {
        if (gesuch.getGesuchTranchen().size() <= 1) {
            return true;
        }

        // Only one Tranche with status IN_BEARBEITUNG_GS is allowed
        return gesuch.getGesuchTranchen()
            .stream()
            .filter(tranche -> tranche.getStatus() == GesuchTrancheStatus.IN_BEARBEITUNG_GS)
            .count() <= 1;
    }
}
