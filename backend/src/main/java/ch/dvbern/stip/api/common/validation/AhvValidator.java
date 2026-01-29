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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotNull;

@ApplicationScoped
public class AhvValidator {
    public static final String START_DIGITS = "756";

    public boolean isValid(@NotNull String ahvNummer) {
        String cleanedAhv = ahvNummer.replace(".", "");
        char[] digitsArray = cleanedAhv.toCharArray();

        if (digitsArray.length != 13) {
            return false;
        }

        int calculatedCheckDigit = getCheckDigit(digitsArray);
        int checkDigit = Character.getNumericValue(digitsArray[12]);

        String startDigits = ahvNummer.substring(0, 3);

        return checkDigit == calculatedCheckDigit && startDigits.equals(START_DIGITS);
    }

    public static int getCheckDigit(char[] digitsArray) {
        int[] relevantDigits = new int[12];
        for (int i = 0; i < 12; i++) {
            relevantDigits[i] = Character.getNumericValue(digitsArray[11 - i]);
        }
        int digitsSum = 0;
        for (int i = 0; i < relevantDigits.length; i++) {
            int multiplier = i % 2 == 0 ? 3 : 1;
            digitsSum += relevantDigits[i] * multiplier;
        }
        int relevantDigitsRounded = (int) Math.ceil(digitsSum / 10.0) * 10;
        return relevantDigitsRounded - digitsSum;
    }
}
