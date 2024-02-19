package ch.dvbern.stip.api.benutzer.entity;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.BUCHSTABEN_RANGE_VALIDATION_PATTERN;

public class BuchstabenRangeConstraintValidator implements ConstraintValidator<BuchstabenRangeConstraint, String> {

    @Override
    public boolean isValid(String buchstabenRange, ConstraintValidatorContext constraintValidatorContext) {

        if (buchstabenRange == null) {
            return true;
        }
        if (!Pattern.compile(BUCHSTABEN_RANGE_VALIDATION_PATTERN).matcher(buchstabenRange).matches()) {
            return false;
        }
        String[] parts = buchstabenRange.split(",");

        for (String part : parts) {
            if (part.contains("-")) {
                char startLetter = part.charAt(0);
                char endLetter = part.charAt(2);
                if (startLetter >= endLetter) {
                    return false;
                }
            }
        }
        return true;
    }
}
