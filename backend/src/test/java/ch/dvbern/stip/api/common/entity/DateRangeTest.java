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

package ch.dvbern.stip.api.common.entity;

import java.time.LocalDate;

import ch.dvbern.stip.api.common.util.DateRange;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.CONCURRENT)
class DateRangeTest {
    private final LocalDate von = LocalDate.of(2022, 12, 1);
    private final LocalDate bis = LocalDate.of(2023, 8, 31);

    private final DateRange dateRange = new DateRange();
    private final DateRange otherDateRange = new DateRange(von, bis);
    private final DateRange sameOtherDateRange = new DateRange(von, bis);

    @Test
    void testDateRangeEquality() {
        Assertions.assertEquals(false, dateRange.equals(otherDateRange));
        Assertions.assertEquals(true, otherDateRange.equals(sameOtherDateRange));
    }

    @Test
    void testDateRangeHashCode() {
        Assertions.assertNotEquals(dateRange.hashCode(), otherDateRange.hashCode());
        Assertions.assertEquals(otherDateRange.hashCode(), sameOtherDateRange.hashCode());
    }

    @Test
    void testDateRangeEqualitySameHashCodeEquality() {
        Assertions.assertEquals(dateRange.equals(otherDateRange), dateRange.hashCode() == otherDateRange.hashCode());
        Assertions.assertEquals(
            otherDateRange.equals(sameOtherDateRange),
            otherDateRange.hashCode() == sameOtherDateRange.hashCode()
        );
    }

    @Test
    void containsOneDayBevorRange() {
        LocalDate dateToTest = bis.plusDays(1);
        DateRange range = new DateRange(von, bis);
        Assertions.assertFalse(range.contains(dateToTest));
    }

    @Test
    void containsOneDayAfterRange() {
        LocalDate dateToTest = von.minusDays(1);
        DateRange range = new DateRange(von, bis);
        Assertions.assertFalse(range.contains(dateToTest));
    }

    @Test
    void containsFirstDayOfRange() {
        DateRange range = new DateRange(von, bis);
        Assertions.assertTrue(range.contains(von));
    }

    @Test
    void containsLastDayOfRange() {
        DateRange range = new DateRange(von, bis);
        Assertions.assertTrue(range.contains(bis));
    }
}
