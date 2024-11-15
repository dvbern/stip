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

import ch.dvbern.stip.api.common.entity.FamilieEntityWohnsitzValidator;
import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FamiliensituationPersonInAusbildungWohnsitzConstraintValidator
    implements ConstraintValidator<FamiliensituationPersonInAusbildungWohnsitzConstraint, GesuchFormular> {
    private String property = "";
    @Inject
    FamilieEntityWohnsitzValidator validator;

    @Override
    public void initialize(FamiliensituationPersonInAusbildungWohnsitzConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    @Override
    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext constraintValidatorContext) {
        final var isValid =
            validator.isValid(gesuchFormular.getPersonInAusbildung(), gesuchFormular.getFamiliensituation());

        if (isValid) {
            return true;
        }

        return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
    }

}
