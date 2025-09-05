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

package ch.dvbern.stip.berechnung.service.bern.v1;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Stream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CalculatorUtilV1 {
    public <T> Function<T, Integer> mapAndReturn(
        final BiConsumer<T, Integer> setter,
        final Integer value
    ) {
        return (result) -> {
            setter.accept(result, value);
            return value;
        };
    }

    public <T> int applyAndSum(
        final Stream<Function<T, Integer>> toApply,
        final T result
    ) {
        return toApply.map(applier -> applier.apply(result))
            .mapToInt(Integer::intValue)
            .sum();
    }
}
