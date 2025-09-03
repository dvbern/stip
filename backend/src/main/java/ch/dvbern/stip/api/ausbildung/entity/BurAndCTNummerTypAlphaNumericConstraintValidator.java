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

package ch.dvbern.stip.api.ausbildung.entity;

import ch.dvbern.stip.api.ausbildung.type.AusbildungsstaetteNummerTyp;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BurAndCTNummerTypAlphaNumericConstraintValidator
    implements ConstraintValidator<BurAndCTNummerTypAlphaNumericConstraint, Ausbildungsstaette> {
    private static final String ALPHANUMERIC_REGEX_PATTERN = "^[A-Za-z0-9]+$";

    @Override
    public boolean isValid(Ausbildungsstaette ausbildungsstaette, ConstraintValidatorContext context) {
        if (ausbildungsstaette.getNummerTyp() == AusbildungsstaetteNummerTyp.CH_SHIS) {
            return true;
        }
        return ausbildungsstaette.getNummer().matches(ALPHANUMERIC_REGEX_PATTERN);
    }
}
