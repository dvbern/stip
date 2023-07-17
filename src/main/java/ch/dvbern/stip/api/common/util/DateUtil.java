package ch.dvbern.stip.api.common.util;

import ch.dvbern.stip.api.common.service.*;
import org.mapstruct.Mapper;

import java.time.LocalDate;

@MonthYearMapper
@Mapper(config = MappingConfig.class)
public class DateUtil {

    @DateToMonthYear
    public String DateToMonthYear(LocalDate date) {
        return date.getMonthValue() + "." + date.getYear();
    }

    @MonthYearToBeginOfMonth
    public LocalDate MonthYearToBeginOfMonth(String monthYear){
       String[] date = monthYear.split("\\.");
      return LocalDate.parse(date[1] + "-" + date[0] + "-01");
    }

    @MonthYearToEndOfMonth
    public LocalDate MonthYearToEndOfMonth(String monthYear){
        LocalDate date= MonthYearToBeginOfMonth(monthYear);
        return date.plusMonths(1).minusDays(1);
    }
}
