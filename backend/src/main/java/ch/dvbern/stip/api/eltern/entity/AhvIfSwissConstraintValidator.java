package ch.dvbern.stip.api.eltern.entity;

import ch.dvbern.stip.api.common.validation.AhvValidator;
import ch.dvbern.stip.api.stammdaten.type.Land;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AhvIfSwissConstraintValidator implements ConstraintValidator<AhvIfSwissConstraint, Eltern> {
    private AhvValidator validator;

    public AhvIfSwissConstraintValidator() {
        validator = new AhvValidator();
    }

    @Override
    public void initialize(AhvIfSwissConstraint constraintAnnotation) {
        validator = new AhvValidator();
    }

    @Override
    public boolean isValid(Eltern eltern, ConstraintValidatorContext constraintValidatorContext) {
        final var adresse = eltern.getAdresse();
        if (adresse == null) {
            return true;
        }

        if (adresse.getLand() != Land.CH) {
            return true;
        }

        return validator.isValid(eltern.getSozialversicherungsnummer());
    }
}
