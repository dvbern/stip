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
                "elternhaushalt": 1,
                "vorname": "a",
                "nachname": "a",
                "vornamePartner": null,
                "nachnamePartner": null,
                "sozialversicherungsnummer": "756.1111.1114.10",
                "sozialversicherungsnummerPartner": null,
                "geburtsdatum": {
                  "year": 1990,
                  "month": "JANUARY",
                  "monthValue": 1,
                  "dayOfMonth": 1,
                  "leapYear": false,
                  "dayOfWeek": "MONDAY",
                  "dayOfYear": 1,
                  "era": "CE",
                  "chronology": {
                    "id": "ISO",
                    "calendarType": "iso8601",
                    "isoBased": true
                  }
                },
                "geburtsdatumPartner": null,
                "steuerdatenTyp": "VATER",
                "selbststaendigErwerbend": false,
                "anzahlPersonenImHaushalt": 3,
                "anzahlGeschwisterInAusbildung": 0,
                "steuerjahr": 2000,
                "veranlagungscode": "AutomatischProvisorischVeranlagt",
                "totalEinkuenfte": 0,
                "einnahmenBGSA": null,
                "ergaenzungsleistungen": null,
                "andereEinnahmen": null,
                "eigenmietwert": 0,
                "unterhaltsbeitraege": null,
                "einzahlungSaeule3a": null,
                "einzahlungSaeule2": null,
                "renten": null,
                "vermoegen": 0,
                "grundbedarf": 21816,
                "effektiveWohnkosten": 0,
                "medizinischeGrundversorgung": 8200,
                "integrationszulage": 2400,
                "integrationszulageAnzahl": 1,
                "integrationszulageTotal": 2400,
                "steuernKantonGemeinde": 0,
                "steuernBund": 0,
                "fahrkostens": [{ "vorname": "a", "value": 0 }],
                "verpflegungskostens": [{ "vorname": "a", "value": 0 }],
                "initialized": true
              }
            },
            "InputFamilienbudget_2_V1": {
              "elternteil": {
                "elternhaushalt": 2,
                "vorname": "a",
                "nachname": "a",
                "vornamePartner": null,
                "nachnamePartner": null,
                "sozialversicherungsnummer": "756.1111.1111.13",
                "sozialversicherungsnummerPartner": null,
                "geburtsdatum": {
                  "year": 1990,
                  "month": "JANUARY",
                  "monthValue": 1,
                  "dayOfMonth": 1,
                  "leapYear": false,
                  "dayOfWeek": "MONDAY",
                  "dayOfYear": 1,
                  "era": "CE",
                  "chronology": {
                    "id": "ISO",
                    "calendarType": "iso8601",
                    "isoBased": true
                  }
                },
                "geburtsdatumPartner": null,
                "steuerdatenTyp": "MUTTER",
                "selbststaendigErwerbend": false,
                "anzahlPersonenImHaushalt": 3,
                "anzahlGeschwisterInAusbildung": 0,
                "steuerjahr": 2000,
                "veranlagungscode": "AutomatischProvisorischVeranlagt",
                "totalEinkuenfte": 0,
                "einnahmenBGSA": null,
                "ergaenzungsleistungen": null,
                "andereEinnahmen": null,
                "eigenmietwert": 0,
                "unterhaltsbeitraege": null,
                "einzahlungSaeule3a": null,
                "einzahlungSaeule2": null,
                "renten": null,
                "vermoegen": 0,
                "grundbedarf": 21816,
                "effektiveWohnkosten": 0,
                "medizinischeGrundversorgung": 12200,
                "integrationszulage": 2400,
                "integrationszulageAnzahl": 1,
                "integrationszulageTotal": 2400,
                "steuernKantonGemeinde": 0,
                "steuernBund": 0,
                "fahrkostens": [{ "vorname": "a", "value": 0 }],
                "verpflegungskostens": [{ "vorname": "a", "value": 0 }],
                "initialized": true
              }
            },
            "InputPersoenlichesbudget_V1": {
              "antragssteller": {
                "vorname": "a",
                "nachname": "a",
                "vornamePartner": "a",
                "nachnamePartner": "a",
                "sozialversicherungsnummer": "756.1111.1113.11",
                "geburtsdatum": {
                  "year": 2008,
                  "month": "JANUARY",
                  "monthValue": 1,
                  "dayOfMonth": 1,
                  "leapYear": true,
                  "dayOfWeek": "TUESDAY",
                  "dayOfYear": 1,
                  "era": "CE",
                  "chronology": {
                    "id": "ISO",
                    "calendarType": "iso8601",
                    "isoBased": true
                  }
                },
                "alter": 18,
                "piaWohntInElternHaushalt": 0,
                "anzahlPersonenImHaushalt": 2,
                "anteilFamilienbudget": 0,
                "verheiratetKonkubinat": true,
                "tertiaerstufe": true,
                "lehre": false,
                "eigenerHaushalt": true,
                "halbierungElternbeitrag": false,
                "einkommens": [
                  { "vorname": "a", "value": 6916 },
                  { "vorname": "a", "value": 12916 }
                ],
                "einnahmenBGSAs": [
                  { "vorname": "a", "value": 0 },
                  { "vorname": "a", "value": 0 }
                ],
                "kinderAusbildungszulagens": [
                  { "vorname": "a", "value": 0 },
                  { "vorname": "a", "value": 0 }
                ],
                "unterhaltsbeitraeges": [
                  { "vorname": "a", "value": 0 },
                  { "vorname": "a", "value": 0 }
                ],
                "eoLeistungens": [
                  { "vorname": "a", "value": 0 },
                  { "vorname": "a", "value": 0 }
                ],
                "taggelds": [
                  { "vorname": "a", "value": 0 },
                  { "vorname": "a", "value": 0 }
                ],
                "rentens": [
                  { "vorname": "a", "value": 0 },
                  { "vorname": "a", "value": 0 }
                ],
                "ergaenzungsleistungens": [
                  { "vorname": "a", "value": 1200 },
                  { "vorname": "a", "value": 1200 }
                ],
                "gemeindeInstitutionen": null,
                "andereEinnahmens": [
                  { "vorname": "a", "value": 0 },
                  { "vorname": "a", "value": 0 }
                ],
                "vermoegen": null,
                "ausbildungskosten": 450,
                "fahrkosten": 523,
                "auswaertigeMittagessenProWoche": 0,
                "grundbedarf": 17940,
                "wohnkosten": 13536,
                "medizinischeGrundversorgungs": [
                  { "vorname": "a", "value": 1400 },
                  { "vorname": "a", "value": 1400 }
                ],
                "fahrkostenPartner": 523,
                "verpflegungskostenPartner": 5,
                "fremdbetreuung": 0,
                "steuern": 0,
                "steuernPartner": 0
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
        final var formular = gesuch.getGesuchTranchen().getFirst().getGesuchFormular();
        formular.getPersonInAusbildung()
            .setGeburtsdatum(
                formular.getPersonInAusbildung().getGeburtsdatum().withYear(2008).withMonth(1).withDayOfMonth(1)
            );
        formular.getElterns()
            .stream()
            .toList()
            .get(0)
            .setGeburtsdatum(
                formular.getElterns()
                    .stream()
                    .toList()
                    .get(0)
                    .getGeburtsdatum()
                    .withYear(1990)
                    .withMonth(1)
                    .withDayOfMonth(1)
            );
        formular.getElterns()
            .stream()
            .toList()
            .get(1)
            .setGeburtsdatum(
                formular.getElterns()
                    .stream()
                    .toList()
                    .get(1)
                    .getGeburtsdatum()
                    .withYear(1990)
                    .withMonth(1)
                    .withDayOfMonth(1)
            );

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
