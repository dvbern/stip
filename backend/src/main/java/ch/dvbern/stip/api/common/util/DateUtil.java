package ch.dvbern.stip.api.common.util;

import java.time.LocalDate;

import ch.dvbern.stip.api.common.exception.AppErrorException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DateUtil {
    /**
     * Clamps the given {@param date} to be no less than {@param min} and no more that {@param max},
     * if the given value is already between the two dates it returns it.
     * @throws IllegalArgumentException if {@param min} is after {@param max}
     * */
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
}
