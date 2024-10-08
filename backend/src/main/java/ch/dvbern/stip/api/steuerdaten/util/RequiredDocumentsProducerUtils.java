package ch.dvbern.stip.api.steuerdaten.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class RequiredDocumentsProducerUtils {
    public boolean greaterThanZero(final Integer base) {
        return base != null && base > 0;
    }
}
