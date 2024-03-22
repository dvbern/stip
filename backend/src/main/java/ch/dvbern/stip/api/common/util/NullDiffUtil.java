package ch.dvbern.stip.api.common.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class NullDiffUtil {
    public boolean hasNullChanged(Object original, Object updated) {
        return (original == null && updated != null) ||
            (original != null && updated == null);
    }
}
