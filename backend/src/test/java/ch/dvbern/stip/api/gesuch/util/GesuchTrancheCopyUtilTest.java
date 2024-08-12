package ch.dvbern.stip.api.gesuch.util;

import java.time.LocalDate;
import java.util.stream.Stream;

import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.generated.dto.AenderungsantragCreateDto;
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

        final var createDto = new AenderungsantragCreateDto();
        createDto.setStart(start);
        createDto.setEnd(end);

        final var clampedRange = GesuchTrancheCopyUtil.clampStartStop(gesuchsperiode, createDto);

        assertThat(clampedRange.getGueltigAb(), is(expectedStart));
        assertThat(clampedRange.getGueltigBis(), is(expectedEnd));
    }

    private static class ClampStartEndTestArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(
                // Aenderung fully inside Gesuchsperiode
                Arguments.of(
                    LocalDate.of(2024, 1, 1),
                    LocalDate.of(2024, 10, 1),
                    LocalDate.of(2024, 2, 1),
                    LocalDate.of(2024, 9, 1),
                    LocalDate.of(2024, 2, 1),
                    LocalDate.of(2024, 9, 1)
                ),
                // Aenderung starts before Gesuchsperiode does
                Arguments.of(
                    LocalDate.of(2024, 2, 1),
                    LocalDate.of(2024, 10, 1),
                    LocalDate.of(2024, 1, 1),
                    LocalDate.of(2024, 9, 1),
                    LocalDate.of(2024, 2, 1),
                    LocalDate.of(2024, 9, 1)
                ),
                // Aenderung ends after Gesuchsperiode does
                Arguments.of(
                    LocalDate.of(2024, 1, 1),
                    LocalDate.of(2024, 10, 1),
                    LocalDate.of(2024, 2, 1),
                    LocalDate.of(2024, 11, 1),
                    LocalDate.of(2024, 2, 1),
                    LocalDate.of(2024, 10, 1)
                ),
                // Aenderung starts before and ends after Gesuchsperiode does
                Arguments.of(
                    LocalDate.of(2024, 2, 1),
                    LocalDate.of(2024, 10, 1),
                    LocalDate.of(2024, 1, 1),
                    LocalDate.of(2024, 11, 1),
                    LocalDate.of(2024, 2, 1),
                    LocalDate.of(2024, 10, 1)
                ),
                // Aenderung has no end date
                Arguments.of(
                    LocalDate.of(2024, 1, 1),
                    LocalDate.of(2024, 10, 1),
                    LocalDate.of(2024, 2, 1),
                    null,
                    LocalDate.of(2024, 2, 1),
                    LocalDate.of(2024, 10, 1)
                )
            );
        }
    }
}
