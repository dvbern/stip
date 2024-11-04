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
