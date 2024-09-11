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
        final var roundedStart = DateUtil.roundToPeriodStart(toRound, midpoint, false);
        assertThat(roundedStart, is(expected));
    }

    @ParameterizedTest
    @ArgumentsSource(RoundToEndTestArgumentsProvider.class)
    void roundToStartEndTest(final LocalDate toRound, final LocalDate expected, final int midpoint) {
        final var roundedEnd = DateUtil.roundToPeriodEnd(toRound, midpoint, false);
        assertThat(roundedEnd, is(expected));
    }

    @ParameterizedTest
    @ArgumentsSource(GetMonthsBetweenTestArgumentsProvider.class)
    void getMonthsBetweenTest(final LocalDate start, final LocalDate end, final int expected) {
        final var monthsBetween = DateUtil.getMonthsBetween(start, end);
        assertThat(monthsBetween, is(expected));
    }
}
