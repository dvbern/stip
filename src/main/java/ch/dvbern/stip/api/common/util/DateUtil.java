package ch.dvbern.stip.api.common.util;

import java.time.LocalDate;

public final class DateUtil {

    public static String DateToMonthYear(LocalDate date) {
        return date.getMonthValue() + "." + date.getYear();
    }

    public static LocalDate MonthYearToBeginOfMonth(String monthYear){
       String[] date = monthYear.split("\\.");
      return LocalDate.parse(date[1] + "-" + date[0] + "-01");
    }

    public static LocalDate MonthYearToEndOfMonth(String monthYear){
        LocalDate date= MonthYearToBeginOfMonth(monthYear);
        return date.plusMonths(1).minusDays(1);
    }
}
