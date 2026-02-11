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

package ch.dvbern.stip.api.demo.util;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public record FindEnum<T extends Enum<?>>(T enumValue, List<String> values) {
    private static <T extends Enum<?>> FindEnum<T> of(T enumValue, List<String> values) {
        return new FindEnum<>(enumValue, values);
    }

    /**
     * Find a matching enum value by giving an exhaustive switch expression for the given enum class.
     * It should help to keep the Enum matching synchronized to future Enum changes.
     *
     * <pre>{@code
     *     return FindEnum.findEnumValue(
     *             cell.getStringCellValue(),
     *             Wohnsitz.class,
     *             (wohnsitz) -> switch (wohnsitz) {
     *                 case FAMILIE -> List.of("Familie");
     *                 case MUTTER_VATER -> List.of("Vater und/oder Mutter");
     *                 case EIGENER_HAUSHALT -> List.of("eigener Haushalt");
     *             }
     *         ).orElseThrow(() -> invalidValue(cell))
     * }</pre>
     *
     * @param value The searched value
     * @param enumClass The enum class
     * @param switchResult The switch expression that returns the possible matches
     * @return The corresponding enum value if found as optional
     */
    static <T extends Enum<?>> Optional<T> findEnumValue(
        String value,
        Class<T> enumClass,
        Function<T, List<String>> switchResult
    ) {
        final var allValues = enumClass.getEnumConstants();
        return Stream.of(allValues)
            .map(t -> FindEnum.of(t, switchResult.apply(t)))
            // Find the first matching Enum/List<String> pair that match the searched value or return empty
            .filter(available -> available.values.stream().anyMatch(v -> v.equalsIgnoreCase(value)))
            .map(FindEnum::enumValue)
            .findFirst();
    }
}
