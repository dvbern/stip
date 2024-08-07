package ch.dvbern.stip.api.common.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;

import ch.dvbern.stip.api.common.exception.AppErrorException;
import lombok.experimental.UtilityClass;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

@UtilityClass
public class DateUtil {
    /**
     * Clamps the given {@param date} to be no less than {@param min} and no more that {@param max},
     * if the given value is already between the two dates it returns it.
     *
     * @throws IllegalArgumentException if {@param min} is after {@param max}
     */
    public LocalDate clamp(final LocalDate date, final LocalDate min, final LocalDate max) {
        if (min.isAfter(max)) {
            throw new IllegalArgumentException("Min must be after max and max must be after min");
        }

        if (date.isAfter(min) && date.isBefore(max)) {
            return date;
        }

        if (date.isBefore(min) || date.equals(min)) {
            return min;
        }

        if (date.isAfter(max) || date.equals(max)) {
            return max;
        }

        throw new AppErrorException("Unreachable code was reached!");
    }

    /**
     * Defaults the midpoint to 14
     *
     * @see DateUtil#roundToStartOrEnd(LocalDate, int)
     */
    public LocalDate roundToStartOrEnd(final LocalDate date) {
        return roundToStartOrEnd(date, 14);
    }

    /**
     * Clamps to either the first or last day of month.
     * All dates up to and including the midpoint of the month are clamped down to the start,
     * all after are clamped up the start of next month.
     */
    public LocalDate roundToStartOrEnd(final LocalDate date, final int midpoint) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        if (date.getDayOfMonth() <= midpoint) {
            return date.with(firstDayOfMonth());
        } else {
            return date.with(lastDayOfMonth());
        }
    }

    /**
     * Gets the number of months between two dates.
     * <p>
     * WARNING: If start = first day of month and end = last day of same month and year 1 will be returned
     */
    public int getMonthsBetween(final LocalDate start, final LocalDate end) {
        final var lastDayOfEndMonth = YearMonth.from(end).atEndOfMonth();
        // If same year and month but first and last day of month return 1
        if (start.getYear() == end.getYear() &&
            start.getMonth().equals(end.getMonth()) &&
            start.getDayOfMonth() == 1 && lastDayOfEndMonth.getDayOfMonth() == end.getDayOfMonth()) {
            return 1;
        }

        return Period.between(start, end).getMonths();
    }

    public boolean beforeOrEqual(final LocalDate left, final LocalDate right) {
        return left.isBefore(right) || left.isEqual(right);
    }

    public boolean afterOrEqual(final LocalDate left, final LocalDate right) {
        return left.isAfter(right) || left.isEqual(right);
    }
}
