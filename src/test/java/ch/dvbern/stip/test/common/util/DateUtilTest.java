package ch.dvbern.stip.test.common.util;

import ch.dvbern.stip.api.common.service.DateMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;


class DateUtilTest {

    private static final String MONTH_YEAR = "10.2023";
    DateMapperImpl dateMapperImpl = new DateMapperImpl();

    @Test
    public void testDateToMonthYear() {
        LocalDate dateToTest = LocalDate.of(2023,10,1);
        String monthYear = dateMapperImpl.dateToMonthYear(dateToTest);
        Assertions.assertEquals( MONTH_YEAR, monthYear);
    }

    @Test
    public void testMonthYearToBeginOfMonth() {
        LocalDate dateToTest = dateMapperImpl.monthYearToBeginOfMonth(MONTH_YEAR);
        Assertions.assertEquals(LocalDate.of(2023,10,1), dateToTest);
    }

    @Test
    public void testShotMonthYearToBeginOfMonth() {
        LocalDate dateToTest = dateMapperImpl.monthYearToBeginOfMonth("1.2023");
        Assertions.assertEquals(LocalDate.of(2023,1,1), dateToTest);
    }

    @Test
    public void testMonthYearToEndOfMonth() {
        LocalDate dateToTest = dateMapperImpl.monthYearToEndOfMonth(MONTH_YEAR);
        Assertions.assertEquals(LocalDate.of(2023,10,31), dateToTest);
    }
}
