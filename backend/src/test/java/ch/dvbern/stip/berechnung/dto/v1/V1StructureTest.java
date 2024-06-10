package ch.dvbern.stip.berechnung.dto.v1;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
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
                    "maxSaeule3a": 7000,
                    "einkommensfreibetrag": 6000,
                    "anzahlWochenLehre": 42,
                    "anzahlWochenSchule": 37,
                    "preisProMahlzeit": 7
                },
                "InputFamilienBudget_1_V1": {
                    "elternteil": {
                        "essenskostenPerson1": 0,
                        "essenskostenPerson2": 0,
                        "grundbedarf": 0,
                        "fahrkostenPerson1": 0,
                        "fahrkostenPerson2": 0,
                        "integrationszulage": 0,
                        "steuernBund": 0,
                        "steuernStaat": 0,
                        "medizinischeGrundversorgung": 0,
                        "effektiveWohnkosten": 0,
                        "totalEinkuenfte": 0,
                        "ergaenzungsleistungen": 0,
                        "eigenmietwert": 0,
                        "alimente": 0,
                        "einzahlungSaeule3a": 0,
                        "einzahlungSaeule2": 0,
                        "steuerbaresVermoegen": 0,
                        "selbststaendigErwerbend": false,
                        "anzahlPersonenImHaushalt": 0,
                        "anzahlGeschwisterInAusbildung": 0
                    }
                },
                "InputFamilienBudget_2_V1": {
                    "elternteil": {
                        "essenskostenPerson1": 0,
                        "essenskostenPerson2": 0,
                        "grundbedarf": 0,
                        "fahrkostenPerson1": 0,
                        "fahrkostenPerson2": 0,
                        "integrationszulage": 0,
                        "steuernBund": 0,
                        "steuernStaat": 0,
                        "medizinischeGrundversorgung": 0,
                        "effektiveWohnkosten": 0,
                        "totalEinkuenfte": 0,
                        "ergaenzungsleistungen": 0,
                        "eigenmietwert": 0,
                        "alimente": 0,
                        "einzahlungSaeule3a": 0,
                        "einzahlungSaeule2": 0,
                        "steuerbaresVermoegen": 0,
                        "selbststaendigErwerbend": false,
                        "anzahlPersonenImHaushalt": 0,
                        "anzahlGeschwisterInAusbildung": 0
                    }
                },
                "InputPersoenlichesbudget_V1": {
                    "antragssteller": {
                        "tertiaerstufe": false,
                        "einkommen": 12916,
                        "einkommenPartner": 0,
                        "vermoegen": 0,
                        "alimente": 0,
                        "rente": 0,
                        "kinderAusbildungszulagen": 0,
                        "ergaenzungsleistungen": 1200,
                        "leistungenEO": 0,
                        "gemeindeInstitutionen": 0,
                        "alter": -18,
                        "grundbedarf": 0,
                        "wohnkosten": 6000,
                        "medizinischeGrundversorgung": 0,
                        "ausbildungskosten": 450,
                        "steuern": 0,
                        "steuernKonkubinatspartner": 0,
                        "fahrkosten": 523,
                        "fahrkostenPartner": 0,
                        "verpflegung": 0,
                        "verpflegungPartner": 0,
                        "fremdbetreuung": 0,
                        "anteilFamilienbudget": 0,
                        "lehre": true,
                        "eigenerHaushalt": true,
                        "abgeschlosseneErstausbildung": true
                    }
                }
            }
        }
    """;

    private static final UUID trancheUuid = UUID.randomUUID();

    @Test
    void test() throws JsonProcessingException {
        final var request = BerechnungRequestV1.createRequest(prepareGesuch(), trancheUuid);
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
        ).setGesuchTranchen(
            List.of(
                (GesuchTranche) new GesuchTranche()
                    .setGesuchFormular(
                    new GesuchFormular()
                        .setPersonInAusbildung(
                            (PersonInAusbildung) new PersonInAusbildung()
                                .setVermoegenVorjahr(0)
                                .setWohnsitz(Wohnsitz.EIGENER_HAUSHALT)
                                .setGeburtsdatum(LocalDate.now().minusYears(18).minusDays(1))
                        )
                        .setPartner(
                            new Partner()
                                .setJahreseinkommen(0)
                                .setFahrkosten(0)
                                .setVerpflegungskosten(0)
                        )
                        .setEinnahmenKosten(
                            new EinnahmenKosten()
                                .setNettoerwerbseinkommen(12916)
                                .setErgaenzungsleistungen(1200)
                                .setWohnkosten(6000)
                                .setAusbildungskostenTertiaerstufe(450)
                                .setFahrkosten(523)
                        )
                ).setId(trancheUuid)
            )
        );
    }
}
