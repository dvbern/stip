package ch.dvbern.stip.api.common.util;

import java.util.HashSet;
import java.util.List;

import ch.dvbern.stip.api.common.exception.ValidationsException;
import jakarta.validation.Validator;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidatorUtil {
    public <T> void validate(final Validator validator, final T toValidate, final Class<?> validationGroup) {
        validate(validator, toValidate, List.of(validationGroup));
    }

    public <T> void validate(final Validator validator, final T toValidate) {
        validate(validator, toValidate, List.of());
    }

    public <T> void validate(final Validator validator, final T toValidate, final List<Class<?>> validationGroups) {
        final var concatenatedViolations = new HashSet<>(validator.validate(toValidate));

        if (!validationGroups.isEmpty()) {
            concatenatedViolations.addAll(validator.validate(toValidate, validationGroups.toArray(new Class<?>[0])));
        }

        if (!concatenatedViolations.isEmpty()) {
            throw new ValidationsException(
                String.format(
                    "Validation of class %s with validation groups %s failed",
                    toValidate.getClass(),
                    validationGroups
                ),
                concatenatedViolations
            );
        }
    }
}
