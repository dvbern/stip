package ch.dvbern.stip.api.gesuch.util;

import java.time.LocalDate;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class GesuchTrancheCopyUtilTest {
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
        final var gesuchsperiode = new Gesuchsperiode()
            .setGesuchsperiodeStart(gesuchsperiodeStart)
            .setGesuchsperiodeStopp(gesuchsperiodeStopp);
        final var range = new DateRange(start, end);

        final var clampedRange = GesuchTrancheCopyUtil.clampStartStop(gesuchsperiode, range);

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
