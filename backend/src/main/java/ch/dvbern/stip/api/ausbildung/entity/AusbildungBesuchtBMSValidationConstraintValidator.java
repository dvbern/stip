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

import java.util.Objects;

import ch.dvbern.stip.api.ausbildung.type.AbschlussZusatzfrage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AusbildungBesuchtBMSValidationConstraintValidator
    implements ConstraintValidator<AusbildungBesuchtBMSValidationConstraint, Ausbildung> {

    @Override
    public boolean isValid(Ausbildung ausbildung, ConstraintValidatorContext context) {
        if (
            Objects.isNull(ausbildung.getAusbildungsgang()) ||
            Objects.isNull(ausbildung.getAusbildungsgang().getAbschluss())
        ) {
            return true;
        }
        final var abschluss = ausbildung.getAusbildungsgang().getAbschluss();

        if (abschluss.getZusatzfrage() == AbschlussZusatzfrage.BERUFSBEZEICHNUNG_BERUFSMATURITAET) {
            // both values (true/false) are valid for BMS flag
            return true;
        }
        // only false is a valid for BMS flag
        return !ausbildung.isBesuchtBMS();
    }
}
