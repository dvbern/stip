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

package ch.dvbern.stip.api.gesuchformular.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class GesuchFormularCalculationUtilTest {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @ParameterizedTest
    @CsvSource(
        {
            "01.01.2024,01.02.2025,1",
            "01.01.2024,01.01.2025,1",
            "01.01.2008,01.01.2024,16",
            "01.03.2024,01.01.2025,0",
            "03.01.2024,01.01.2025,0",
            "01.01.2024,03.01.2025,1",
            "01.01.2024,01.01.2008,-16"
        }
    )
    void calculateNumberOfYearsBetweenTest(final String date1, final String date2, final int expected) {
        assertThat(
            GesuchFormularCalculationUtil.calculateNumberOfYearsBetween(
                LocalDate.parse(date1, formatter),
                LocalDate.parse(date2, formatter)
            )
        ).isEqualTo(expected);
    }
}
