/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.benutzer.entity;

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
