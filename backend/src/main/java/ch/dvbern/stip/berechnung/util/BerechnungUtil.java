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

package ch.dvbern.stip.berechnung.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BerechnungUtil {
    public int monthLimitAusbildungTertiaerstufe = 36;
    public int darlehenLimit = 50000;

    public int calculateGesetzlichesDarlehen(final int total) {
        // divide by 300 then round and multiply by 100 to get a rounded (to the nearest 100) third of the
        // stipendium
        return BigDecimal.valueOf(total)
            .divide(BigDecimal.valueOf(300), 0, RoundingMode.UP)
            .multiply(BigDecimal.valueOf(100))
            .intValue();
    }

    public int roundGesetzlichesDarlehen(final int total) {
        return BigDecimal.valueOf(total)
            .divide(BigDecimal.valueOf(100), 0, RoundingMode.UP)
            .multiply(BigDecimal.valueOf(100))
            .intValue();
    }

    public int subtractGesezlichesDarlehen(final int total, final int monateMitDarlehen) {
        if (monateMitDarlehen == 0) {
            return total;
        }

        final var monateOhneDarlehen = 12 - monateMitDarlehen;

        final var stipendiumOfMonateOhneDarlehen = total * monateOhneDarlehen / 12;
        final var stipendiumOfMonateMitDarlehen = BigDecimal.valueOf(total)
            .multiply(BigDecimal.valueOf(monateMitDarlehen))
            .divide(BigDecimal.valueOf(12), RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(2))
            .divide(BigDecimal.valueOf(3), RoundingMode.HALF_UP)
            .intValue();

        return stipendiumOfMonateOhneDarlehen + stipendiumOfMonateMitDarlehen;
    }

    public boolean nullableCompare(final Integer value1, final Integer value2, final int defaultValue) {
        if ((Objects.isNull(value1) || value1 == defaultValue) && (Objects.isNull(value2) || value2 == defaultValue)) {
            return true;
        }

        return Objects.equals(value1, value2);
    }
}
