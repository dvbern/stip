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

package ch.dvbern.stip.api.eltern.entity;

import ch.dvbern.stip.api.common.validation.AhvValidator;
import ch.dvbern.stip.api.land.type.WellKnownLand;
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

        if (!adresse.getLand().is(WellKnownLand.CH)) {
            return true;
        }

        return validator.isValid(eltern.getSozialversicherungsnummer());
    }
}
