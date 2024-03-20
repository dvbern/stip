package ch.dvbern.stip.api.common.util;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.experimental.UtilityClass;

@UtilityClass
public class DebugUtil {
    public static String prettyPrintMap(Map<String, Serializable> args) {
        return args.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .map(e -> "%s=%s".formatted(e.getKey(), e.getValue()))
            .collect(Collectors.joining(", "));
    }
}
