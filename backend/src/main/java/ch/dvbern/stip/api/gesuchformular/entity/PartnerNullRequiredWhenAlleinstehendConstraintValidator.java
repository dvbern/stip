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

package ch.dvbern.stip.api.gesuchformular.entity;

import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_PARTNER_NOT_NULL_REQUIRED_MESSAGE;

public class PartnerNullRequiredWhenAlleinstehendConstraintValidator
    implements ConstraintValidator<PartnerNullRequiredWhenAlleinstehendConstraint, GesuchFormular> {
    private String property = "";

    @Override
    public void initialize(PartnerNullRequiredWhenAlleinstehendConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
        if (gesuchFormular.getPersonInAusbildung() == null) {
            return true;
        }
        if (
            gesuchFormular.getPersonInAusbildung().getZivilstand().hasPartnerschaft()
            && gesuchFormular.getPartner() == null
        ) {
            return GesuchValidatorUtil.addProperty(
                constraintValidatorContext,
                VALIDATION_PARTNER_NOT_NULL_REQUIRED_MESSAGE,
                property
            );
        }

        if (
            gesuchFormular.getPersonInAusbildung().getZivilstand().hasPartnerschaft()
            || gesuchFormular.getPartner() == null
        ) {
            return true;
        } else {
            return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
        }
    }
}
