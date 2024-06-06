package ch.dvbern.stip.api.gesuch.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;

public class EinnahmenKostenSteuerjahrInPastOrCurrentConstraintValidator
implements ConstraintValidator<EinnahmenKostenSteuerjahrInPastOrCurrentConstraint, Integer> {
    private static final Integer CURRENT_YEAR = Year.now().getValue();

    @Override
    public boolean isValid(Integer steuerjahr, ConstraintValidatorContext constraintValidatorContext) {
        return steuerjahr <= CURRENT_YEAR;
    }
}
