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

import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OnlyOneTrancheInBearbeitungConstraintValidator
    implements ConstraintValidator<OnlyOneTrancheInBearbeitungConstraint, Gesuch> {
    @Override
    public boolean isValid(Gesuch gesuch, ConstraintValidatorContext context) {
        if (gesuch.getGesuchTranchen().size() <= 1) {
            return true;
        }

        // Only one Tranche with status IN_BEARBEITUNG_GS is allowed
        return gesuch.getGesuchTranchen()
            .stream()
            .filter(tranche -> tranche.getStatus() == GesuchTrancheStatus.IN_BEARBEITUNG_GS)
            .count() <= 1;
    }
}
