package ch.dvbern.stip.api.common.util;

import java.time.LocalDate;

import ch.dvbern.stip.api.common.util.providers.GetMonthsBetweenTestArgumentsProvider;
import ch.dvbern.stip.api.common.util.providers.RoundToStartOrEndTestArgumentsProvider;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class DateUtilTest {
    @ParameterizedTest
    @ArgumentsSource(RoundToStartOrEndTestArgumentsProvider.class)
    void roundToStartOrEndTest(final LocalDate toRound, final LocalDate expected, final int midpoint) {
        final var rounded = DateUtil.roundToStartOrEnd(toRound, midpoint);
        assertThat(rounded, is(expected));
    }

    @ParameterizedTest
    @ArgumentsSource(GetMonthsBetweenTestArgumentsProvider.class)
    void getMonthsBetweenTest(final LocalDate start, final LocalDate end, final int expected) {
        final var monthsBetween = DateUtil.getMonthsBetween(start, end);
        assertThat(monthsBetween, is(expected));
    }
}
