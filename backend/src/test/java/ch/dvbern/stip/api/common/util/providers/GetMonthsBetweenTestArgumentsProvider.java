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

public class GetMonthsBetweenTestArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
            // Same day of months is 1
            Arguments.of(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 2, 1),
                1
            ),
            // Same day is 0
            Arguments.of(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 1),
                0
            ),
            // Days of same month don't count
            Arguments.of(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 3),
                0
            ),
            // Days don't count
            Arguments.of(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 2, 3),
                1
            ),
            // Reverse
            Arguments.of(
                LocalDate.of(2024, 3, 1),
                LocalDate.of(2024, 1, 1),
                -1
            ),
            // Days of same month reverse
            Arguments.of(
                LocalDate.of(2024, 1, 4),
                LocalDate.of(2024, 1, 1),
                0
            ),
            // Full month counts as one month
            Arguments.of(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 31),
                1
            ),
            // Multiple months same day
            Arguments.of(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 1),
                2
            ),
            // Multiple months later end day
            Arguments.of(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 4),
                2
            ),
            Arguments.of(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 4, 4),
                3
            ),
            // Multiple months non-first days
            Arguments.of(
                LocalDate.of(2024, 1, 17),
                LocalDate.of(2024, 4, 15),
                2
            ),
            Arguments.of(
                LocalDate.of(2024, 1, 17),
                LocalDate.of(2024, 4, 17),
                3
            )
        );
    }
}
