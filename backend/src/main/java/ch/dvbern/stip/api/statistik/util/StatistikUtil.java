package ch.dvbern.stip.api.statistik.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StatistikUtil {
    public static int booleanToBfsCode(boolean value) {
        return value ? 2 : 1;
    }
}
