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

package ch.dvbern.stip.api.common.validation;

import ch.dvbern.stip.api.notiz.entity.GesuchNotiz;
import ch.dvbern.stip.api.notiz.type.GesuchNotizTyp;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

@ApplicationScoped
public class GesuchNotizAntwortValidator implements ConstraintValidator<GesuchNotizAntwortConstraint, GesuchNotiz> {

    @Override
    public boolean isValid(GesuchNotiz gesuchNotiz, ConstraintValidatorContext context) {
        if(Objects.nonNull(gesuchNotiz.getAntwort())) {
            // only a GesuchNotiz of GesuchNotizTyp may have an answer
            return gesuchNotiz.getNotizTyp().equals(GesuchNotizTyp.JURISTISCHE_NOTIZ);
        }
        return true;
    }
}
