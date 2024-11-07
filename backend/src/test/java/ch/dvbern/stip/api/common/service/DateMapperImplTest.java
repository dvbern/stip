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

package ch.dvbern.stip.api.common.service;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DateMapperImplTest {

    private static final String MONTH_YEAR = "10.2023";
    DateMapperImpl dateMapperImpl = new DateMapperImpl();

    @Test
    void testDateToMonthYear() {
        LocalDate dateToTest = LocalDate.of(2023, 10, 1);
        String monthYear = dateMapperImpl.dateToMonthYear(dateToTest);
        Assertions.assertEquals(MONTH_YEAR, monthYear);
    }

    @Test
    void testMonthYearToBeginOfMonth() {
        LocalDate dateToTest = dateMapperImpl.monthYearToBeginOfMonth(MONTH_YEAR);
        Assertions.assertEquals(LocalDate.of(2023, 10, 1), dateToTest);
    }

    @Test
    void testShotMonthYearToBeginOfMonth() {
        LocalDate dateToTest = dateMapperImpl.monthYearToBeginOfMonth("1.2023");
        Assertions.assertEquals(LocalDate.of(2023, 1, 1), dateToTest);
    }

    @Test
    void testMonthYearToEndOfMonth() {
        LocalDate dateToTest = dateMapperImpl.monthYearToEndOfMonth(MONTH_YEAR);
        Assertions.assertEquals(LocalDate.of(2023, 10, 31), dateToTest);
    }
}
