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

import ch.dvbern.stip.api.common.exception.AppValidationMessage;
import org.mapstruct.Mapper;

@DateMapper
@Mapper(config = MappingQualifierConfig.class)
public class DateMapperImpl {

    @DateToMonthYear
    public static String dateToMonthYear(LocalDate date) {
        return date.getMonthValue() + "." + date.getYear();
    }

    @MonthYearToBeginOfMonth
    public static LocalDate monthYearToBeginOfMonth(String monthYear) {
        String[] date = monthYear.split("\\.");

        if (date.length != 2) {
            throw AppValidationMessage.invalidDate(monthYear).create();
        }

        String month = date[0].length() == 1 ? '0' + date[0] : date[0];
        return LocalDate.parse(date[1] + '-' + month + "-01");
    }

    @MonthYearToEndOfMonth
    public static LocalDate monthYearToEndOfMonth(String monthYear) {
        LocalDate date = monthYearToBeginOfMonth(monthYear);
        return date.plusMonths(1).minusDays(1);
    }
}
