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

import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EinnahmenKostenBetreuungskostenRequiredConstraintValidator
    implements ConstraintValidator<EinnahmenKostenBetreuungskostenRequiredConstraint, GesuchFormular> {
    private String property = "";

    @Override
    public void initialize(EinnahmenKostenBetreuungskostenRequiredConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    @Override
    public boolean isValid(GesuchFormular value, ConstraintValidatorContext context) {
        if (value.getKinds().isEmpty()) {
            return true;
        }

        final var ek = value.getEinnahmenKosten();
        if (ek == null) {
            return true;
        }

        if (ek.getBetreuungskostenKinder() == null) {
            return GesuchValidatorUtil.addProperty(
                context,
                property
            );
        }

        return true;
    }
}
