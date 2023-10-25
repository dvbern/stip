package ch.dvbern.stip.api.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.validator.routines.IBANValidator;

public class IbanConstraintValidator implements ConstraintValidator<IbanConstraint, String> {
    private final IBANValidator validator;

    public IbanConstraintValidator() {
        var ch = IBANValidator.DEFAULT_IBAN_VALIDATOR.getValidator("CH");
        validator = new IBANValidator(new IBANValidator.Validator[]{ ch });
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return validator.isValid(s.replace(" ", ""));
    }
}
