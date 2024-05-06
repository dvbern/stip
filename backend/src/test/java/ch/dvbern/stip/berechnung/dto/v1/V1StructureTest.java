package ch.dvbern.stip.berechnung.dto.v1;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import com.savoirtech.json.JsonComparatorBuilder;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertTrue;

class V1StructureTest {
    private static final String EXPECTED = """
        {
            "templateJson": {
                "Stammdaten_V1": {
                    "maxSaeule3a":7000,
                    "einkommensfreibetrag":6000,
                    "anzahlWochenLehre":42,
                    "anzahlWochenSchule":37,
                    "preisProMahlzeit":7
                }
            }
        }
        """;

    @Test
    void test() throws JsonProcessingException {
        final var request = BerechnungRequestV1.createRequest(prepareGesuch());
        final var actual = new ObjectMapper().writeValueAsString(request);
        final var comparator = new JsonComparatorBuilder().build();

        final var result = comparator.compare(EXPECTED, actual);
        assertTrue(result.isMatch(), result.getErrorMessage());
    }

    private Gesuch prepareGesuch() {
        return new Gesuch().setGesuchsperiode(
            new Gesuchsperiode()
                .setMaxSaeule3a(7000)
                .setEinkommensfreibetrag(6000)
                .setAnzahlWochenLehre(42)
                .setAnzahlWochenSchule(37)
                .setPreisProMahlzeit(7)
        );
    }
}
