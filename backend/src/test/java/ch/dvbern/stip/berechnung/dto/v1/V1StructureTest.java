package ch.dvbern.stip.berechnung.dto.v1;

import java.util.UUID;

import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.berechnung.service.PersonenImHaushaltService;
import com.savoirtech.json.JsonComparatorBuilder;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@Slf4j
class V1StructureTest {
    @Inject
    PersonenImHaushaltService personenImHaushaltService;

    private static final String EXPECTED = """
        {
            "templateJson": {
                "Stammdaten_V1": {
                    "maxSaeule3a": 7000,
                    "einkommensfreibetrag": 6000,
                    "anzahlWochenLehre": 42,
                    "anzahlWochenSchule": 37,
                    "preisProMahlzeit": 7
                },
                "InputFamilienbudget_1_V1": {
                    "elternteil": {
                        "essenskostenPerson1": 0,
                        "essenskostenPerson2": 0,
                        "grundbedarf": 21816,
                        "fahrkostenPerson1": 0,
                        "fahrkostenPerson2": 0,
                        "integrationszulage": 0,
                        "steuernBund": 0,
                        "steuernStaat": 0,
                        "medizinischeGrundversorgung": 8200,
                        "effektiveWohnkosten": 0,
                        "totalEinkuenfte": 0,
                        "ergaenzungsleistungen": 0,
                        "eigenmietwert": 0,
                        "alimente": 0,
                        "einzahlungSaeule3a": 0,
                        "einzahlungSaeule2": 0,
                        "steuerbaresVermoegen": 0,
                        "selbststaendigErwerbend": false,
                        "anzahlPersonenImHaushalt": 3,
                        "anzahlGeschwisterInAusbildung": 0
                    }
                },
                "InputFamilienbudget_2_V1": {
                    "elternteil": {
                        "essenskostenPerson1": 0,
                        "essenskostenPerson2": 0,
                        "grundbedarf": 21816,
                        "fahrkostenPerson1": 0,
                        "fahrkostenPerson2": 0,
                        "integrationszulage": 0,
                        "steuernBund": 0,
                        "steuernStaat": 0,
                        "medizinischeGrundversorgung": 12200,
                        "effektiveWohnkosten": 0,
                        "totalEinkuenfte": 0,
                        "ergaenzungsleistungen": 0,
                        "eigenmietwert": 0,
                        "alimente": 0,
                        "einzahlungSaeule3a": 0,
                        "einzahlungSaeule2": 0,
                        "steuerbaresVermoegen": 0,
                        "selbststaendigErwerbend": false,
                        "anzahlPersonenImHaushalt": 3,
                        "anzahlGeschwisterInAusbildung": 0
                    }
                },
                "InputPersoenlichesbudget_V1": {
                    "antragssteller": {
                        "tertiaerstufe": true,
                        "einkommen": 12916,
                        "einkommenPartner": 0,
                        "vermoegen": 0,
                        "alimente": 0,
                        "rente": 0,
                        "kinderAusbildungszulagen": 0,
                        "ergaenzungsleistungen": 1200,
                        "leistungenEO": 0,
                        "gemeindeInstitutionen": 0,
                        "alter": 18,
                        "grundbedarf": 17940,
                        "wohnkosten": 6000,
                        "medizinischeGrundversorgung": 2800,
                        "ausbildungskosten": 450,
                        "steuern": 0,
                        "steuernKonkubinatspartner": 0,
                        "fahrkosten": 523,
                        "fahrkostenPartner": 0,
                        "verpflegung": 0,
                        "verpflegungPartner": 0,
                        "fremdbetreuung": 0,
                        "anteilFamilienbudget": 0,
                        "lehre": false,
                        "eigenerHaushalt": true,
                        "abgeschlosseneErstausbildung": false,
                        "anzahlPersonenImHaushalt": 2
                    }
                }
            }
        }
    """;

    private static final UUID trancheUuid = UUID.randomUUID();

    @Test
    void test() throws JsonProcessingException {
        final var gesuch = TestUtil.getGesuchForBerechnung(trancheUuid);

        final var request = BerechnungRequestV1.createRequest(
            gesuch,
            gesuch.getNewestGesuchTranche().orElseThrow(NotFoundException::new),
            ElternTyp.VATER,
            personenImHaushaltService
        );
        final var actual = new ObjectMapper().writeValueAsString(request);
        final var comparator = new JsonComparatorBuilder().build();

        final var result = comparator.compare(EXPECTED, actual);
        LOG.info("Actual: " + actual.toString());
        assertTrue(result.isMatch(), result.getErrorMessage());
    }
}
