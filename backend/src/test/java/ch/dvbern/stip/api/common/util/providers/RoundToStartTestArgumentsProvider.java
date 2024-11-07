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

package ch.dvbern.stip.api.common.util.providers;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class RoundToStartTestArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
            // First of month stays first of month
            Arguments.of(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 1),
                15
            ),
            // Below midpoint is rounded down
            Arguments.of(
                LocalDate.of(2024, 1, 2),
                LocalDate.of(2024, 1, 1),
                15
            ),
            // Equals to midpoint is rounded down
            Arguments.of(
                LocalDate.of(2024, 1, 14),
                LocalDate.of(2024, 1, 1),
                15
            ),
            // Above midpoint is rounded up
            Arguments.of(
                LocalDate.of(2024, 1, 16),
                LocalDate.of(2024, 2, 1),
                15
            ),
            // End of month is rounded up
            Arguments.of(
                LocalDate.of(2024, 1, 31),
                LocalDate.of(2024, 2, 1),
                15
            ),
            // Custom midpoint rounds down
            Arguments.of(
                LocalDate.of(2024, 1, 2),
                LocalDate.of(2024, 1, 1),
                3
            ),
            // Custom midpoint rounds up
            Arguments.of(
                LocalDate.of(2024, 1, 16),
                LocalDate.of(2024, 2, 1),
                15
            )
        );
    }
}
