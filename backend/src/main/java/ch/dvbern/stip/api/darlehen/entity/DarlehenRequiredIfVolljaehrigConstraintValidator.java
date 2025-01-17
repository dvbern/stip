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

package ch.dvbern.stip.api.darlehen.entity;

import java.util.Objects;

import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchformular.util.GesuchFormularCalculationUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_DARLEHEN_REQUIRED_VOLLJAEHRIG_MESSAGE;

public class DarlehenRequiredIfVolljaehrigConstraintValidator
    implements ConstraintValidator<DarlehenRequiredIfVolljaehrigConstraint, GesuchFormular> {
    private String property;

    @Override
    public void initialize(DarlehenRequiredIfVolljaehrigConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    @Override
    public boolean isValid(GesuchFormular value, ConstraintValidatorContext context) {
        final var isNotVolljaehrig = !GesuchFormularCalculationUtil.isPersonInAusbildungVolljaehrig(value);
        // not volljaehrig -> darlehen has to be null, else: darlehen must NOT be null
        final var isValid =
            isNotVolljaehrig ? (Objects.isNull(value.getDarlehen())) : Objects.nonNull(value.getDarlehen());
        if (!isValid) {
            // validation fails
            GesuchValidatorUtil.addProperty(
                context,
                VALIDATION_DARLEHEN_REQUIRED_VOLLJAEHRIG_MESSAGE,
                property
            );
        }
        return isValid;
    }
}
