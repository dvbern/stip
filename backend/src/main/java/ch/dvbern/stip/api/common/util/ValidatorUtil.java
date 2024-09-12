package ch.dvbern.stip.api.common.util;

import java.util.HashSet;

import ch.dvbern.stip.api.common.exception.ValidationsException;
import jakarta.validation.Validator;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidatorUtil {
    public <T> void validate(final Validator validator, final T toValidate, final Class<?> validationGroup) {
        final var concatenatedViolations = new HashSet<>(validator.validate(toValidate));

        if (validationGroup != null) {
            concatenatedViolations.addAll(validator.validate(toValidate, validationGroup));
        }

        if (!concatenatedViolations.isEmpty()) {
            throw new ValidationsException(
                String.format(
                    "Validation of class %s with validation group %s failed",
                    toValidate.getClass(),
                    validationGroup
                ),
                concatenatedViolations
            );
        }
    }

    public <T> void validate(final Validator validator, final T toValidate) {
        validate(validator, toValidate, null);
    }
}
