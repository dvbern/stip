package ch.dvbern.stip.api.common.util;

import java.time.LocalDate;

import ch.dvbern.stip.api.common.exception.AppErrorException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateUtil {
    /**
     * @return the date which is before the other. If any date is null, returns the other
     * @throws IllegalArgumentException if both are null
     * */
    public LocalDate before(final LocalDate left, final LocalDate right) {
        if (left == null && right == null) {
            throw new IllegalArgumentException();
        }

        if (left == null) {
            return right;
        } else if (right == null) {
            return left;
        }

        return left.isBefore(right) ? left : right;
    }

    /**
     * @return the date which is after the other. If any date is null, returns the other
     * @throws IllegalArgumentException if both are null
     * */
    public LocalDate after(final LocalDate left, final LocalDate right) {
        if (left == null && right == null) {
            throw new IllegalArgumentException();
        }

        if (left == null) {
            return right;
        } else if (right == null) {
            return left;
        }

        return left.isAfter(right) ? left : right;
    }

    public LocalDate clamp(final LocalDate date, final LocalDate min, final LocalDate max) {
        if (min.isAfter(max) || max.isBefore(min)) {
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
}
