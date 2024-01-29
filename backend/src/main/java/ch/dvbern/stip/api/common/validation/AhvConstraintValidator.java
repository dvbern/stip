package ch.dvbern.stip.api.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AhvConstraintValidator implements ConstraintValidator<AhvConstraint, String> {
	boolean optional = false;

    AhvValidator validator;

    public AhvConstraintValidator() {
        validator = new AhvValidator();
    }

	@Override
	public void initialize(AhvConstraint constraintAnnotation) {
		optional = constraintAnnotation.optional();
	}

	@Override
	public boolean isValid(String ahvNummer, ConstraintValidatorContext constraintValidatorContext) {

		if (ahvNummer == null && !optional) {
            return false;
        }
		if (ahvNummer == null) {
            return true;
        }

        return validator.isValid(ahvNummer);
	}
}
