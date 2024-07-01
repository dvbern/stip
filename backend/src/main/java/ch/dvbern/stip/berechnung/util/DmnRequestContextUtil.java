package ch.dvbern.stip.berechnung.util;

import java.util.Map;

import ch.dvbern.stip.berechnung.dto.DmnRequest;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DmnRequestContextUtil {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public Map<String, Object> toContext(final DmnRequest berechnungRequest) {
        // Use Jackson to serialize the BerechnungRequest to a Map<String, Object> via JSON
        // Once we have a separate DMN Server we'll send the JSON as the input directly
        return OBJECT_MAPPER.convertValue(berechnungRequest, new TypeReference<>() {});
    }
}
