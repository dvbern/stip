package ch.dvbern.stip.api.ausbildung.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AusbildungEndDateMustBeAfterStartConstraintValidator
    implements ConstraintValidator<AusbildungEndDateMustBeAfterStartConstraint, Ausbildung> {

    @Override
    public boolean isValid(Ausbildung value, ConstraintValidatorContext context) {
        return value.getAusbildungEnd().isAfter(value.getAusbildungBegin());
    }
}
