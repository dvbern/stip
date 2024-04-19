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

        if (!BUCHSTABEN_RANGE_VALIDATION_PATTERN.matcher(buchstabenRange).matches()) {
            return false;
        }

        for (final var part : buchstabenRange.split(",")) {
            if (part.contains("-")) {
                final var ranges = part.split("-");
                if (ranges.length != 2) {
                    return false;
                }

                // lexicographically compares both sides of the range, if the left side is larger returns false
                // lexicographically speaking "a > b" and "aa > a", so "SAA > SOA"
                if (ranges[0].compareTo(ranges[1]) > 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
