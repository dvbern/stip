package ch.dvbern.stip.api.gesuch.service;

import java.time.LocalDate;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.service.GesuchTrancheService.OverlapType;
import ch.dvbern.stip.api.gesuch.service.GesuchTrancheService.TrancheRange;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class GesuchTrancheServiceTrancheRangeTest {
    @ParameterizedTest
    @ArgumentsSource(TestOverlapArgumentsProvider.class)
    void testOverlap(
        final LocalDate start1,
        final LocalDate end1,
        final LocalDate start2,
        final LocalDate end2,
        final OverlapType expectedOverlapType
    ) {
        // Arrange
        final var range1 = createRange(start1, end1);
        final var range2 = createRange(start2, end2);

        // Act
        final var actual = range1.overlaps(range2);

        // Assert
        assertThat(actual, is(expectedOverlapType));
    }

    private TrancheRange createRange(final LocalDate von, final LocalDate bis)  {
        return TrancheRange.from(new GesuchTranche().setGueltigkeit(new DateRange(von, bis)));
    }
    private static class TestOverlapArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(
                // Exact
                Arguments.of(
                    LocalDate.of(2024, 4, 1),
                    LocalDate.of(2024, 4, 30),
                    LocalDate.of(2024, 4, 1),
                    LocalDate.of(2024, 4, 30),
                    OverlapType.EXACT
                ),
                // Full
                Arguments.of(
                    LocalDate.of(2024, 4, 1),
                    LocalDate.of(2024, 4, 30),
                    LocalDate.of(2024, 3, 1),
                    LocalDate.of(2024, 5, 31),
                    OverlapType.FULL
                ),
                // Left
                Arguments.of(
                    LocalDate.of(2024, 4, 1),
                    LocalDate.of(2024, 5, 31),
                    LocalDate.of(2024, 3, 1),
                    LocalDate.of(2024, 4, 30),
                    OverlapType.LEFT
                ),
                // Left full
                Arguments.of(
                    LocalDate.of(2024, 4, 1),
                    LocalDate.of(2024, 4, 30),
                    LocalDate.of(2024, 2, 1),
                    LocalDate.of(2024, 4, 30),
                    OverlapType.LEFT_FULL
                ),
                // Right
                Arguments.of(
                    LocalDate.of(2024, 4, 1),
                    LocalDate.of(2024, 5, 31),
                    LocalDate.of(2024, 5, 1),
                    LocalDate.of(2024, 6, 30),
                    OverlapType.RIGHT
                ),
                // Right full
                Arguments.of(
                    LocalDate.of(2024, 4, 1),
                    LocalDate.of(2024, 5, 31),
                    LocalDate.of(2024, 4, 1),
                    LocalDate.of(2024, 6, 30),
                    OverlapType.RIGHT_FULL
                )
            );
        }
    }
}
