package ch.dvbern.stip.api.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtil {
    public boolean isNullOrEmpty(final String toCheck) {
        return toCheck == null || toCheck.isEmpty();
    }
}
