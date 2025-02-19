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

import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

public class BetweenTestArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
            Arguments.of(
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 1).with(lastDayOfMonth()),
                LocalDate.of(2025, 1, 10),
                true,
                true
            ),
            Arguments.of(
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2025, 1, 10),
                true,
                true
            ),
            Arguments.of(
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 10, 1).with(lastDayOfMonth()),
                LocalDate.of(2025, 3, 10),
                true,
                true
            ),
            Arguments.of(
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 1).with(lastDayOfMonth()),
                LocalDate.of(2025, 2, 1),
                false,
                false
            ),
            Arguments.of(
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 1).with(lastDayOfMonth()),
                LocalDate.of(2026, 1, 10),
                true,
                false
            ),
            Arguments.of(
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 1).with(lastDayOfMonth()),
                LocalDate.of(2024, 1, 10),
                true,
                false
            ),
            Arguments.of(
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 1).with(lastDayOfMonth()),
                LocalDate.of(2025, 1, 1),
                true,
                true
            ),
            Arguments.of(
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 1).with(lastDayOfMonth()),
                LocalDate.of(2025, 1, 1),
                false,
                false
            ),
            Arguments.of(
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 1).with(lastDayOfMonth()),
                LocalDate.of(2025, 1, 1).with(lastDayOfMonth()),
                true,
                true
            ),
            Arguments.of(
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 1, 1).with(lastDayOfMonth()),
                LocalDate.of(2025, 1, 1).with(lastDayOfMonth()),
                false,
                false
            )
        );
    }
}
