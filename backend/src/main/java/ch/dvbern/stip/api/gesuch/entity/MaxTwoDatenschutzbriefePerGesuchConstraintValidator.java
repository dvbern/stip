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

package ch.dvbern.stip.api.gesuch.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MaxTwoDatenschutzbriefePerGesuchConstraintValidator
    implements ConstraintValidator<MaxTwoDatenschutzbriefePerGesuchConstraint, Gesuch> {
    private static final int MAX_NUMBER_OF_DATENSCHUTZBREFS = 2;

    @Override
    public boolean isValid(Gesuch gesuch, ConstraintValidatorContext context) {
        if (gesuch.getDatenschutzbriefs().isEmpty()) {
            return true;
        }
        return gesuch.getDatenschutzbriefs().stream().count() <= MAX_NUMBER_OF_DATENSCHUTZBREFS;
    }
}
