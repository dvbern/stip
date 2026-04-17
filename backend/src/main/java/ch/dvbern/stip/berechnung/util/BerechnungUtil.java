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

    public int calculateGesetzlichesDarlehen(int total) {
        return BigDecimal.valueOf(total)
            .divide(BigDecimal.valueOf(300), 0, RoundingMode.UP)
            .multiply(BigDecimal.valueOf(100))
            .intValue();
    }

    public int substractGesezlichesDarlehen(int total, Integer darlehen) {
        if (Objects.isNull(darlehen)) {
            return total;
        }

        return BigDecimal.valueOf(total)
            .multiply(BigDecimal.valueOf(2))
            .divide(BigDecimal.valueOf(3), RoundingMode.HALF_UP)
            .intValue();
    }

    public boolean nullableCompare(Integer value1, Integer value2, int defaultValue) {
        if ((Objects.isNull(value1) || value1 == defaultValue) && (Objects.isNull(value2) || value2 == defaultValue)) {
            return true;
        }

        return Objects.equals(value1, value2);
    }
}
