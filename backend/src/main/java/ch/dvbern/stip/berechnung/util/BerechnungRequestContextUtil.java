package ch.dvbern.stip.berechnung.util;

import java.util.Map;

import ch.dvbern.stip.berechnung.dto.BerechnungRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BerechnungRequestContextUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public Map<String, Object> toContext(final BerechnungRequest berechnungRequest) {
        // Use Jackson to serialize the BerechnungRequest to a Map<String, Object> via JSON
        // Once we have a separate DMN Server we'll send the JSON as the input directly
        return OBJECT_MAPPER.convertValue(berechnungRequest, new TypeReference<>() {});
    }
}
