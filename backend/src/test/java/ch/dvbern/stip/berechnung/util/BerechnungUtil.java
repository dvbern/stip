package ch.dvbern.stip.berechnung.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import ch.dvbern.stip.berechnung.dto.BerechnungRequest;
import ch.dvbern.stip.berechnung.dto.v1.BerechnungRequestV1;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BerechnungUtil {
    public BerechnungRequest getRequest(final int fall) {
        try {
            final var resource = BerechnungUtil.class.getClassLoader().getResource(String.format("berechnung/fall_%d.json", fall));
            assert resource != null;
            final var inputs = Files.readString(Paths.get(resource.toURI()));
            final var mapper = new ObjectMapper();
            return mapper.readValue(inputs, BerechnungRequestV1.class);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
