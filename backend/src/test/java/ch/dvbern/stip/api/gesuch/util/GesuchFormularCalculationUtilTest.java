package ch.dvbern.stip.api.gesuch.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class GesuchFormularCalculationUtilTest {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @ParameterizedTest
    @CsvSource({
        "01.01.2024,01.02.2025,1",
        "01.01.2024,01.01.2025,1",
        "01.01.2008,01.01.2024,16",
        "01.03.2024,01.01.2025,0",
        "03.01.2024,01.01.2025,0",
        "01.01.2024,03.01.2025,1",
        "01.01.2024,01.01.2008,-16"
    })
    void calculateNumberOfYearsBetweenTest(final String date1, final String date2, final int expected) {
        assertThat(
            GesuchFormularCalculationUtil.calculateNumberOfYearsBetween(
                LocalDate.parse(date1, formatter),
                LocalDate.parse(date2, formatter)
            )
        ).isEqualTo(expected);
    }
}
