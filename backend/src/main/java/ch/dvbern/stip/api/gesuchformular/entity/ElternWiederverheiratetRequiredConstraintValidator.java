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

import java.util.Objects;

import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ElternWiederverheiratetRequiredConstraintValidator
    implements ConstraintValidator<ElternWiederverheiratetRequiredConstraint, GesuchFormular> {
    private String property = "";

    @Override
    public void initialize(ElternWiederverheiratetRequiredConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    public boolean isValid(GesuchFormular gesuchFormular, ConstraintValidatorContext context) {
        final var familiensituation = gesuchFormular.getFamiliensituation();
        if (Objects.isNull(familiensituation) || familiensituation.getElternVerheiratetZusammen()) {
            return true;
        }

        boolean valid = true;

        for (var eltern : gesuchFormular.getElterns()) {
            if (Objects.isNull(eltern.getWiederverheiratet())) {
                valid = GesuchValidatorUtil
                    .addProperty(context, String.format("%s.%s", property, eltern.getElternTyp().name()));
            }
        }

        return valid;
    }
}
