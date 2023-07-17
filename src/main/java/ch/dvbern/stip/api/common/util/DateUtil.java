package ch.dvbern.stip.api.common.util;

import ch.dvbern.stip.api.common.service.*;
import org.mapstruct.Mapper;

import java.time.LocalDate;

@MonthYearMapper
@Mapper(config = MappingConfig.class)
public class DateUtil {

	@DateToMonthYear
	public String dateToMonthYear(LocalDate date) {
		return date.getMonthValue() + "." + date.getYear();
	}

	@MonthYearToBeginOfMonth
	public LocalDate monthYearToBeginOfMonth(String monthYear) {
		String[] date = monthYear.split("\\.");
		String month = date[0].length() == 1 ? "0" + date[0] : date[0];
		return LocalDate.parse(date[1] + "-" + month + "-01");
	}

	@MonthYearToEndOfMonth
	public LocalDate monthYearToEndOfMonth(String monthYear) {
		LocalDate date = monthYearToBeginOfMonth(monthYear);
		return date.plusMonths(1).minusDays(1);
	}
}
