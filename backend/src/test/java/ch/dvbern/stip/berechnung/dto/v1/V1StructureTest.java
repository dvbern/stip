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
import jakarta.ws.rs.NotFoundException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class V1StructureTest {
    private static final String EXPECTED = """
        {
          "templateJson": {
            "Stammdaten_V1": {
              "maxSaeule3a": 7000,
              "einkommensfreibetrag": 6000,
              "abzugslimite": 13200,
              "freibetragErwerbseinkommen": 6000,
              "freibetragVermoegen": 30000,
              "vermoegensanteilInProzent": 15,
              "anzahlWochenLehre": 47,
              "anzahlWochenSchule": 38,
              "preisProMahlzeit": 10,
              "stipLimiteMinimalstipendium": 500,
              "limiteAlterAntragsstellerHalbierungElternbeitrag": 25,
              "anzahlMonate": 12
            },
            "InputFamilienbudget_1_V1": {
              "elternteil": {
                "verpflegungskosten": 0,
                "verpflegungskostenPartner": 0,
                "grundbedarf": 21816,
                "fahrkosten": 0,
                "fahrkostenPartner": 0,
                "integrationszulage": 2400,
                "integrationszulageAnzahl": 1,
                "integrationszulageTotal": 2400,
                "steuernKantonGemeinde": 0,
                "steuernBund": 0,
                "medizinischeGrundversorgung": 8200,
                "effektiveWohnkosten": 0,
                "totalEinkuenfte": 0,
                "ergaenzungsleistungen": 0,
                "eigenmietwert": 0,
                "unterhaltsbeitraege": 0,
                "einzahlungSaeule3a": 0,
                "einzahlungSaeule2": 0,
                "vermoegen": 0,
                "selbststaendigErwerbend": false,
                "anzahlPersonenImHaushalt": 3,
                "anzahlGeschwisterInAusbildung": 0,
                "einnahmenBGSA": 0,
                "andereEinnahmen": 0,
                "renten": 0,
                "steuerdatenTyp": "VATER"
              }
            },
            "InputFamilienbudget_2_V1": {
              "elternteil": {
                "verpflegungskosten": 0,
                "verpflegungskostenPartner": 0,
                "grundbedarf": 21816,
                "fahrkosten": 0,
                "fahrkostenPartner": 0,
                "integrationszulage": 2400,
                "integrationszulageAnzahl": 1,
                "integrationszulageTotal": 2400,
                "steuernKantonGemeinde": 0,
                "steuernBund": 0,
                "medizinischeGrundversorgung": 12200,
                "effektiveWohnkosten": 0,
                "totalEinkuenfte": 0,
                "ergaenzungsleistungen": 0,
                "eigenmietwert": 0,
                "unterhaltsbeitraege": 0,
                "einzahlungSaeule3a": 0,
                "einzahlungSaeule2": 0,
                "vermoegen": 0,
                "selbststaendigErwerbend": false,
                "anzahlPersonenImHaushalt": 3,
                "anzahlGeschwisterInAusbildung": 0,
                "einnahmenBGSA": 0,
                "andereEinnahmen": 0,
                "renten": 0,
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
                "renten": 0,
                "rentenPartner": 0,
                "rentenKinder": [],
                "kinderAusbildungszulagen": 0,
                "kinderAusbildungszulagenPartner": 0,
                "kinderAusbildungszulagenKinder": [],
                "unterhaltsbeitraege": 0,
                "unterhaltsbeitraegePartner": 0,
                "unterhaltsbeitraegeKinder": [],
                "ergaenzungsleistungen": 1200,
                "ergaenzungsleistungenPartner": 1200,
                "ergaenzungsleistungenKinder": [],
                "eoLeistungen": 0,
                "eoLeistungenPartner": 0,
                "gemeindeInstitutionen": 0,
                "alter": 18,
                "grundbedarf": 17940,
                "wohnkosten": 13536,
                "medizinischeGrundversorgung": 2800,
                "medizinischeGrundversorgungPartner": 1400,
                "medizinischeGrundversorgungKinder": [],
                "ausbildungskosten": 450,
                "steuern": 0,
                "steuernPartner": 0,
                "fahrkosten": 523,
                "fahrkostenPartner": 523,
                "auswaertigeMittagessenProWoche": 0,
                "verpflegungskostenPartner": 5,
                "fremdbetreuung": 0,
                "anteilFamilienbudget": 0,
                "lehre": false,
                "eigenerHaushalt": true,
                "halbierungElternbeitrag": false,
                "anzahlPersonenImHaushalt": 2,
                "verheiratetKonkubinat": true,
                "taggeld": 0,
                "taggeldPartner": 0,
                "andereEinnahmen": 0,
                "andereEinnahmenPartner": 0,
                "andereEinnahmenKinder": [],
                "einnahmenBGSA": 0,
                "einnahmenBGSAPartner": 0
              }
            }
          }
        }
    """;

    private static final UUID trancheUuid = UUID.randomUUID();

    @SneakyThrows
    @Test
    void test() throws JsonProcessingException {
        final var gesuch = TestUtil.getGesuchForBerechnung(trancheUuid);

        final var request = BerechnungRequestV1.createRequest(
            gesuch,
            gesuch.getNewestGesuchTranche().orElseThrow(NotFoundException::new),
            ElternTyp.VATER,
            BerechnungUtil.getPersonenImHaushaltService()
        );
        final var mapper = new ObjectMapper();
        final var actual = mapper.writeValueAsString(request);
        // final var comparator = new JsonComparatorBuilder().build();

        assertEquals(mapper.readTree(EXPECTED).get("templateJson"), mapper.readTree(actual));
    }
}
