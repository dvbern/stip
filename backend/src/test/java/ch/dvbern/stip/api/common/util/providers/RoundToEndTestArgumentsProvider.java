package ch.dvbern.stip.api.common.util.providers;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.time.LocalDate;
import java.util.stream.Stream;

public class RoundToEndTestArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(
                // First of month stays first of month
                Arguments.of(
                    LocalDate.of(2024, 1, 1),
                    LocalDate.of(2023, 12, 31),
                    14
                ),
                // Below midpoint is rounded down
                Arguments.of(
                    LocalDate.of(2024, 1, 2),
                    LocalDate.of(2023, 12, 31),
                    14
                ),
                // Equals to midpoint is rounded down
                Arguments.of(
                    LocalDate.of(2024, 1, 14),
                    LocalDate.of(2023, 12, 31),
                    14
                ),
                // Above midpoint is rounded up
                Arguments.of(
                    LocalDate.of(2024, 1, 15),
                    LocalDate.of(2024, 1, 31),
                    14
                ),
                // End of month stays end of month
                Arguments.of(
                    LocalDate.of(2024, 1, 31),
                    LocalDate.of(2024, 1, 31),
                    14
                ),
                // Custom midpoint rounds down
                Arguments.of(
                    LocalDate.of(2024, 1, 2),
                    LocalDate.of(2023, 12, 31),
                    3
                ),
                // Custom midpoint rounds up
                Arguments.of(
                    LocalDate.of(2024, 1, 4),
                    LocalDate.of(2024, 1, 31),
                    3
                )
            );
        }
    }
