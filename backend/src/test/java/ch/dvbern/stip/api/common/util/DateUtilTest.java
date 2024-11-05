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

package ch.dvbern.stip.api.common.util;

import java.time.LocalDate;

import ch.dvbern.stip.api.common.util.providers.GetMonthsBetweenTestArgumentsProvider;
import ch.dvbern.stip.api.common.util.providers.RoundToEndTestArgumentsProvider;
import ch.dvbern.stip.api.common.util.providers.RoundToStartTestArgumentsProvider;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class DateUtilTest {
    @ParameterizedTest
    @ArgumentsSource(RoundToStartTestArgumentsProvider.class)
    void roundToStartTest(final LocalDate toRound, final LocalDate expected, final int midpoint) {
        final var roundedStart = DateUtil.roundToStartOrEnd(toRound, midpoint, false, true);
        assertThat(roundedStart, is(expected));
    }

    @ParameterizedTest
    @ArgumentsSource(RoundToEndTestArgumentsProvider.class)
    void roundToStartEndTest(final LocalDate toRound, final LocalDate expected, final int midpoint) {
        final var roundedEnd = DateUtil.roundToStartOrEnd(toRound, midpoint, true, false);
        assertThat(roundedEnd, is(expected));
    }

    @ParameterizedTest
    @ArgumentsSource(GetMonthsBetweenTestArgumentsProvider.class)
    void getMonthsBetweenTest(final LocalDate start, final LocalDate end, final int expected) {
        final var monthsBetween = DateUtil.getMonthsBetween(start, end);
        assertThat(monthsBetween, is(expected));
    }
}
