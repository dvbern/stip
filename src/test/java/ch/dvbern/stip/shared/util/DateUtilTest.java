package ch.dvbern.stip.shared.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;


public class DateUtilTest {

    private static final String MONTH_YEAR = "10.2023";

    @Test
    public void testDateToMonthYear() {
        LocalDate dateToTest = LocalDate.of(2023,10,1);
        String monthYear = DateUtil.DateToMonthYear(dateToTest);
        Assertions.assertEquals( MONTH_YEAR, monthYear);
    }

    @Test
    public void testMonthYearToBeginOfMonth() {
        LocalDate dateToTest = DateUtil.MonthYearToBeginOfMonth(MONTH_YEAR);
        Assertions.assertEquals(LocalDate.of(2023,10,1), dateToTest);
    }

    @Test
    public void testMonthYearToEndOfMonth() {
        LocalDate dateToTest = DateUtil.MonthYearToEndOfMonth(MONTH_YEAR);
        Assertions.assertEquals(LocalDate.of(2023,10,31), dateToTest);
    }

}
