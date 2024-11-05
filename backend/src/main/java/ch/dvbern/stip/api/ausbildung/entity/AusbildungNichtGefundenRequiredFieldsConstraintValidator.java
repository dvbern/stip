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

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_AUSBILDUNG_FIELD_REQUIRED_MESSAGE;

public class AusbildungNichtGefundenRequiredFieldsConstraintValidator
    implements ConstraintValidator<AusbildungNichtGefundenRequiredFieldsConstraint, Ausbildung> {

    @Override
    public boolean isValid(
        Ausbildung ausbildung,
        ConstraintValidatorContext constraintValidatorContext
    ) {
        if (ausbildung.isAusbildungNichtGefunden()) {
            return StringUtils.isNotEmpty(ausbildung.getAlternativeAusbildungsgang())
            && StringUtils.isNotEmpty(ausbildung.getAlternativeAusbildungsstaette());
        }

        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(VALIDATION_AUSBILDUNG_FIELD_REQUIRED_MESSAGE)
            .addConstraintViolation();
        return ausbildung.getAusbildungsgang() != null;
    }
}
