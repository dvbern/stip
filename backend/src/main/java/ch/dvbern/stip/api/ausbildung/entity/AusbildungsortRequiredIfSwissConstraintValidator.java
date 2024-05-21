package ch.dvbern.stip.api.ausbildung.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AusbildungsortRequiredIfSwissConstraintValidator
    implements ConstraintValidator<AusbildungsortRequiredIfSwissConstraint, Ausbildung> {
    @Override
    public boolean isValid(Ausbildung ausbildung, ConstraintValidatorContext context) {
        if (Boolean.TRUE.equals(ausbildung.getIsAusbildungAusland())) {
            return ausbildung.getAusbildungsort() == null;
        } else {
            return ausbildung.getAusbildungsort() != null;
        }
    }
}
