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

import java.util.Set;

import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.familiensituation.util.FamiliensituationUtil;
import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FamiliensituationElternEntityRequiredConstraintValidator
implements ConstraintValidator<FamiliensituationElternEntityRequiredConstraint, GesuchFormular> {
    private String property = "";

    @Override
    public void initialize(FamiliensituationElternEntityRequiredConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    @Override
    public boolean isValid(
        GesuchFormular gesuchFormular,
        ConstraintValidatorContext constraintValidatorContext
    ) {
        if (gesuchFormular.getFamiliensituation() == null) {
            return true;
        }

        final var mutterValid =
            isElternTeilRequiredAndVorhanden(ElternTyp.MUTTER, gesuchFormular, constraintValidatorContext);
        final var vaterValid =
            isElternTeilRequiredAndVorhanden(ElternTyp.VATER, gesuchFormular, constraintValidatorContext);
        final var familiensituationAbwesenheitValid =
            isFamiliensituationAbwesenheitValid(gesuchFormular.getFamiliensituation());

        return mutterValid && vaterValid && familiensituationAbwesenheitValid;
    }

    private boolean isFamiliensituationAbwesenheitValid(final Familiensituation familiensituation) {
        return !(familiensituation.getVaterUnbekanntVerstorben() == ElternAbwesenheitsGrund.WEDER_NOCH
        && familiensituation.getMutterUnbekanntVerstorben() == ElternAbwesenheitsGrund.WEDER_NOCH);
    }

    private boolean isElternTeilRequiredAndVorhanden(
        final ElternTyp elternTyp,
        final GesuchFormular gesuchFormular,
        final ConstraintValidatorContext constraintValidatorContext
    ) {
        var isValid = true;
        if (FamiliensituationUtil.isElternteilOfTypRequired(gesuchFormular.getFamiliensituation(), elternTyp)) {
            if (!isElternTeilVorhanden(elternTyp, gesuchFormular.getElterns())) {
                isValid = GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
            }
        } else {
            if (isElternTeilVorhanden(elternTyp, gesuchFormular.getElterns())) {
                isValid = GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
            }
        }

        return isValid;
    }

    private boolean isElternTeilVorhanden(final ElternTyp elternTyp, final Set<Eltern> eltern) {
        return eltern.stream().anyMatch(elternTeil -> elternTeil.getElternTyp() == elternTyp);
    }
}
