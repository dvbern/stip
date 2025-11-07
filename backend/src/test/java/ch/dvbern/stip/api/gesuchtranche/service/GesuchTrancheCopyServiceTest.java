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

package ch.dvbern.stip.api.gesuchtranche.service;

import java.time.LocalDate;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.util.DateRange;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Execution(ExecutionMode.CONCURRENT)
class GesuchTrancheCopyServiceTest {
    @ParameterizedTest
    @ArgumentsSource(ClampStartEndTestArgumentsProvider.class)
    void clampStartEndTest(
        final LocalDate gesuchsperiodeStart,
        final LocalDate gesuchsperiodeStopp,
        final LocalDate start,
        final LocalDate end,
        final LocalDate expectedStart,
        final LocalDate expectedEnd
    ) {
        final var range = new DateRange(start, end);

        final var clampedRange =
            GesuchTrancheCopyService.clampStartStop(gesuchsperiodeStart, gesuchsperiodeStopp, range);

        assertThat(clampedRange.getGueltigAb(), is(expectedStart));
        assertThat(clampedRange.getGueltigBis(), is(expectedEnd));
    }

    private static class ClampStartEndTestArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                // Aenderung fully inside Gesuchsperiode
                Arguments.of(
                    LocalDate.of(2024, 1, 1),
                    LocalDate.of(2024, 10, 1),
                    LocalDate.of(2024, 2, 1),
                    LocalDate.of(2024, 9, 1),
                    LocalDate.of(2024, 2, 1),
                    LocalDate.of(2024, 8, 31)
                ),
                // Aenderung starts before Gesuchsperiode does
                Arguments.of(
                    LocalDate.of(2024, 2, 1),
                    LocalDate.of(2024, 10, 1),
                    LocalDate.of(2024, 1, 1),
                    LocalDate.of(2024, 9, 1),
                    LocalDate.of(2024, 2, 1),
                    LocalDate.of(2024, 8, 31)
                ),
                // Aenderung ends after Gesuchsperiode does
                Arguments.of(
                    LocalDate.of(2024, 1, 1),
                    LocalDate.of(2024, 10, 1),
                    LocalDate.of(2024, 2, 1),
                    LocalDate.of(2024, 11, 1),
                    LocalDate.of(2024, 2, 1),
                    LocalDate.of(2024, 9, 30)
                ),
                // Aenderung starts before and ends after Gesuchsperiode does
                Arguments.of(
                    LocalDate.of(2024, 2, 1),
                    LocalDate.of(2024, 10, 1),
                    LocalDate.of(2024, 1, 1),
                    LocalDate.of(2024, 11, 1),
                    LocalDate.of(2024, 2, 1),
                    LocalDate.of(2024, 9, 30)
                ),
                // Aenderung has no end date
                Arguments.of(
                    LocalDate.of(2024, 1, 1),
                    LocalDate.of(2024, 10, 1),
                    LocalDate.of(2024, 2, 1),
                    null,
                    LocalDate.of(2024, 2, 1),
                    LocalDate.of(2024, 9, 30)
                ),
                // Aenderung clamped to beginning of start month
                Arguments.of(
                    LocalDate.of(2024, 2, 1),
                    LocalDate.of(2024, 10, 1),
                    LocalDate.of(2024, 2, 15),
                    LocalDate.of(2024, 10, 14),
                    LocalDate.of(2024, 2, 1),
                    LocalDate.of(2024, 9, 30)
                ),

                // Aenderung clamped to end of end month
                Arguments.of(
                    LocalDate.of(2024, 2, 1),
                    LocalDate.of(2024, 10, 31),
                    LocalDate.of(2024, 2, 16),
                    LocalDate.of(2024, 10, 16),
                    LocalDate.of(2024, 3, 1),
                    LocalDate.of(2024, 10, 31)
                )
            );
        }
    }
}
