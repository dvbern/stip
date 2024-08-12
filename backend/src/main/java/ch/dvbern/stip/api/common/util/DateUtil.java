package ch.dvbern.stip.api.common.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
     * @see DateUtil#roundToStartOrEnd(LocalDate, int, boolean)
     */
    public LocalDate roundToStartOrEnd(final LocalDate date, final boolean roundUpIfEnd) {
        return roundToStartOrEnd(date, 14, roundUpIfEnd);
    }

    /**
     * Rounds the given date to the start or end of month.
     * All dates up to and including the midpoint of the month are rounded down to the start,
     * all after are clamped up the end of the month.
     * If the param {@code roundUpIfEnd} is {@code true}, then rounds up the date to the start of next month
     */
    public LocalDate roundToStartOrEnd(final LocalDate date, final int midpoint, final boolean roundUpIfEnd) {
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }

        if (date.getDayOfMonth() <= midpoint) {
            return date.with(firstDayOfMonth());
        } else {
            if (roundUpIfEnd) {
                return date.plusMonths(1).with(firstDayOfMonth());
            }

            return date.with(lastDayOfMonth());
        }
    }

    /**
     * Gets the number of months between two dates.
     * <p>
     * WARNING: If the start is the 15th and the end the 16th of at least the next month, it will count as 1.
     * This means it's not mathematically accurate,
     * but logically there is 1 month between e.g. 01.03.2024 and 31.03.2024
     */
    public int getMonthsBetween(final LocalDate start, final LocalDate end) {
        return (int) ChronoUnit.MONTHS.between(start, end.plusDays(1));
    }

    public boolean beforeOrEqual(final LocalDate left, final LocalDate right) {
        return left.isBefore(right) || left.isEqual(right);
    }

    public boolean afterOrEqual(final LocalDate left, final LocalDate right) {
        return left.isAfter(right) || left.isEqual(right);
    }
}
