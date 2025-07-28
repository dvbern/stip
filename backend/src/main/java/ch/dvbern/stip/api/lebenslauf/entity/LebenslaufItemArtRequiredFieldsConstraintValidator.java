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

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_LEBENSLAUFITEM_ART_FIELD_REQUIRED_NULL_MESSAGE;

public class LebenslaufItemArtRequiredFieldsConstraintValidator
    implements ConstraintValidator<LebenslaufItemArtRequiredFieldsConstraint, LebenslaufItem> {

    @Override
    public boolean isValid(
        LebenslaufItem lebenslaufItem,
        ConstraintValidatorContext constraintValidatorContext
    ) {
        if (lebenslaufItem.getAbschluss() == null && lebenslaufItem.getTaetigkeitsart() == null) {
            return false;
        }
        if (lebenslaufItem.getAbschluss() != null && lebenslaufItem.getTaetigkeitsart() != null) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(
                VALIDATION_LEBENSLAUFITEM_ART_FIELD_REQUIRED_NULL_MESSAGE
            )
                .addConstraintViolation();
            return false;
        }
        return true;
    }
}
