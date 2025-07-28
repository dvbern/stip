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

package ch.dvbern.stip.api.lebenslauf.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_LEBENSLAUFITEM_AUSBILDUNG_FACHRICHTUNG_NULL_MESSAGE;

public class LebenslaufItemAusbildungFachrichtungConstraintValidator
    implements ConstraintValidator<LebenslaufItemAusbildungFachrichtungConstraint, LebenslaufItem> {
    @Override
    public boolean isValid(LebenslaufItem lebenslaufItem, ConstraintValidatorContext constraintValidatorContext) {
        if (lebenslaufItem.getAbschluss().getZusatzfrage() == AbschlussZusatzfrage.FACHRICHTUNG) {
            return lebenslaufItem.getFachrichtung() != null;
        }

        if (lebenslaufItem.getFachrichtung() == null) {
            return true;
        }

        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(
            VALIDATION_LEBENSLAUFITEM_AUSBILDUNG_FACHRICHTUNG_NULL_MESSAGE
        )
            .addConstraintViolation();
        return false;
    }
}
