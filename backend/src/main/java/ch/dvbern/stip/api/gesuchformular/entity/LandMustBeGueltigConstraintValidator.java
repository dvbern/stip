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
import ch.dvbern.stip.api.gesuchformular.type.LandGueltigFor;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LandMustBeGueltigConstraintValidator
    implements ConstraintValidator<LandMustBeGueltigConstraint, GesuchFormular> {
    private LandGueltigFor landGueltigFor;

    @Override
    public void initialize(LandMustBeGueltigConstraint constraintAnnotation) {
        landGueltigFor = constraintAnnotation.landGueltigFor();
    }

    @Override
    public boolean isValid(GesuchFormular formular, ConstraintValidatorContext context) {
        final var land = landGueltigFor.getProducer().apply(formular);
        if (land == null) {
            return true;
        }

        if (land.isGueltig()) {
            return true;
        }

        return GesuchValidatorUtil.addProperty(context, landGueltigFor.getProperty());
    }
}
