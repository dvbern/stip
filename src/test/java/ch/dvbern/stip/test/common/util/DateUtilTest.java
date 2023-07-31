package ch.dvbern.stip.test.common.util;

import ch.dvbern.stip.api.common.util.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;


class DateUtilTest {

    private static final String MONTH_YEAR = "10.2023";
    DateUtil dateUtil = new DateUtil();

    @Test
    void testDateToMonthYear() {
        LocalDate dateToTest = LocalDate.of(2023,10,1);
        String monthYear = dateUtil.dateToMonthYear(dateToTest);
        Assertions.assertEquals( MONTH_YEAR, monthYear);
    }

    @Test
    void testMonthYearToBeginOfMonth() {
        LocalDate dateToTest = dateUtil.monthYearToBeginOfMonth(MONTH_YEAR);
        Assertions.assertEquals(LocalDate.of(2023,10,1), dateToTest);
    }

    @Test
    void testShotMonthYearToBeginOfMonth() {
        LocalDate dateToTest = dateUtil.monthYearToBeginOfMonth("1.2023");
        Assertions.assertEquals(LocalDate.of(2023,1,1), dateToTest);
    }

    @Test
    void testMonthYearToEndOfMonth() {
        LocalDate dateToTest = dateUtil.monthYearToEndOfMonth(MONTH_YEAR);
        Assertions.assertEquals(LocalDate.of(2023,10,31), dateToTest);
    }
}
