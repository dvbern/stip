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

import java.util.Arrays;
import java.util.Objects;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AusbildungBesuchtBMSValidationConstraintValidator
    implements ConstraintValidator<AusbildungBesuchtBMSValidationConstraint, Ausbildung> {

    private static final Integer[] VALID_BFS_VALUES_FOR_BMS_FLAG = { 4, 5 };

    @Override
    public boolean isValid(Ausbildung ausbildung, ConstraintValidatorContext context) {
        if (
            Objects.isNull(ausbildung.getAusbildungsgang()) ||
            Objects.isNull(ausbildung.getAusbildungsgang().getBildungskategorie())
        ) {
            return true;
        }
        final var bildungsKategorie = ausbildung.getAusbildungsgang().getBildungskategorie().getBfs();

        // the BMS-Flag may only be set to true if bildungskategorie is 4 or 5
        final var isBMSFlagUpdateableToTrue = Arrays.stream(VALID_BFS_VALUES_FOR_BMS_FLAG)
            .toList()
            .contains(bildungsKategorie);

        if (isBMSFlagUpdateableToTrue) {
            // both values (true/false) are valid for BMS flag
            return true;
        }
        // only false is a valid for BMS flag
        return !ausbildung.isBesuchtBMS();
    }
}
