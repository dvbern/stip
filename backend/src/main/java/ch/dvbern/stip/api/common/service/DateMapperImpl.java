package ch.dvbern.stip.api.common.service;

import java.time.LocalDate;

import ch.dvbern.stip.api.common.exception.AppValidationMessage;
import org.mapstruct.Mapper;

@DateMapper
@Mapper(config = MappingQualifierConfig.class)
public class DateMapperImpl {

    @DateToMonthYear
    public String dateToMonthYear(LocalDate date) {
        return date.getMonthValue() + "." + date.getYear();
    }

    @MonthYearToBeginOfMonth
    public LocalDate monthYearToBeginOfMonth(String monthYear) {
        String[] date = monthYear.split("\\.");

        if (date.length != 2) {
            throw AppValidationMessage.invalidDate(monthYear).create();
        }

        String month = date[0].length() == 1 ? '0' + date[0] : date[0];
        return LocalDate.parse(date[1] + '-' + month + "-01");
    }

    @MonthYearToEndOfMonth
    public LocalDate monthYearToEndOfMonth(String monthYear) {
        LocalDate date = monthYearToBeginOfMonth(monthYear);
        return date.plusMonths(1).minusDays(1);
    }
}
