/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.berechnung.dto.v1;

import java.util.UUID;

import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.berechnung.util.BerechnungUtil;
import com.savoirtech.json.JsonComparatorBuilder;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class V1StructureTest {
    private static final String EXPECTED = """
        {
            "templateJson": {
                "Stammdaten_V1": {
                    "maxSaeule3a": 7000,
                    "abzugslimite": 13200,
                    "einkommensfreibetrag": 6000,
                    "freibetragErwerbseinkommen": 6000,
                    "freibetragVermoegen": 30000,
                    "vermoegensanteilInProzent": 15,
                    "anzahlWochenLehre": 42,
                    "anzahlWochenSchule": 37,
                    "preisProMahlzeit": 7,
                    "stipLimiteMinimalstipendium": 0,
                    "limiteAlterAntragsstellerHalbierungElternbeitrag": 25
                },
                "InputFamilienbudget_1_V1": {
                    "elternteil": {
                        "essenskostenPerson1": 0,
                        "essenskostenPerson2": 0,
                        "grundbedarf": 21816,
                        "fahrkostenPerson1": 0,
                        "fahrkostenPerson2": 0,
                        "integrationszulage": 2400,
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
                        "anzahlGeschwisterInAusbildung": 0,
                        "steuerdatenTyp": "VATER"
                    }
                },
                "InputFamilienbudget_2_V1": {
                    "elternteil": {
                        "essenskostenPerson1": 0,
                        "essenskostenPerson2": 0,
                        "grundbedarf": 21816,
                        "fahrkostenPerson1": 0,
                        "fahrkostenPerson2": 0,
                        "integrationszulage": 2400,
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
                        "anzahlGeschwisterInAusbildung": 0,
                        "steuerdatenTyp": "MUTTER"
                    }
                },
                "InputPersoenlichesbudget_V1": {
                    "antragssteller": {
               "piaWohntInElternHaushalt": 0,
                        "tertiaerstufe": true,
                        "einkommen": 12916,
                        "einkommenPartner": 12916,
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
                        "steuernPartner": 0,
                        "fahrkosten": 523,
                        "fahrkostenPartner": 523,
                        "verpflegung": 0,
                        "verpflegungPartner": 5,
                        "fremdbetreuung": 0,
                        "anteilFamilienbudget": 0,
                        "lehre": false,
                        "eigenerHaushalt": true,
                        "halbierungElternbeitrag": false,
                        "anzahlPersonenImHaushalt": 2,
                        "verheiratetKonkubinat": true
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
            BerechnungUtil.getPersonenImHaushaltService()
        );
        final var actual = new ObjectMapper().writeValueAsString(request);
        final var comparator = new JsonComparatorBuilder().build();

        final var result = comparator.compare(EXPECTED, actual);
        if (!result.isMatch()) {
            LOG.error("Mismatched results. Actual: " + actual.toString());
        }
        assertTrue(result.isMatch(), result.getErrorMessage());
    }
}
