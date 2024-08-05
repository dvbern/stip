package ch.dvbern.stip.berechnung.service;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.bildungsart.entity.Bildungsart;
import ch.dvbern.stip.api.bildungsart.type.Bildungsstufe;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.berechnung.util.BerechnungUtil;
import ch.dvbern.stip.generated.dto.BerechnungsresultatDto;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
@RequiredArgsConstructor
@Slf4j
class BerechnungServiceTest {
    private final BerechnungService berechnungService;

    @Test
    void getV1Test() {
        final var gesuch = TestUtil.getGesuchForBerechnung(UUID.randomUUID());

        final var request = berechnungService.getBerechnungRequest(
            1,
            0,
            gesuch,
            gesuch.getNewestGesuchTranche().orElseThrow(),
            ElternTyp.VATER
        );
        assertThat(request, is(not(nullValue())));
    }

    @Test
    void getNonExistentTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            berechnungService.getBerechnungRequest(
                -1,
                0,
                null,
                null,
                ElternTyp.MUTTER
            );
        });
    }

    @TestAsGesuchsteller
    @ParameterizedTest
    @CsvSource({
        "1, 6427",
        "2, 14192",
        "3, 0",
        "4, 17986",
        "5, 39751", // muss noch angepasst werden, wenn fachliche Abklärungen gemacht wurden
        "6, 27179",
        "7, 6669",
        "8, 266",   // muss noch angepasst werden, wenn fachliche Abklärungen gemacht wurden
        "9, 23527"
    })
    void testBerechnungFaelle(final int fall, final int expectedStipendien) {
        // Load Fall resources/berechnung/fall_{fall}.json, deserialize to a BerechnungRequestV1
        // and calculate Stipendien for it
        final var result = berechnungService.calculateStipendien(BerechnungUtil.getRequest(fall));
        assertThat(result.getStipendien(), is(expectedStipendien));
    }

    @Test
    @TestAsGesuchsteller
    void testMinimalGesuchBerechnung() {
        //Arrange
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());
        final var gesuchFormular = gesuch.getNewestGesuchTranche().get().getGesuchFormular();

        gesuchFormular.getPersonInAusbildung()
            .setWohnsitz(Wohnsitz.EIGENER_HAUSHALT)
            .setGeburtsdatum(LocalDate.now().minusYears(18).minusDays(1));

        gesuchFormular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setNettoerwerbseinkommen(0)
                .setErgaenzungsleistungen(0)
                .setWohnkosten(10000)
                .setAusbildungskostenTertiaerstufe(2000)
                .setFahrkosten(0)
        );

        gesuchFormular.setFamiliensituation(
            new Familiensituation()
                .setElternVerheiratetZusammen(false)
                .setGerichtlicheAlimentenregelung(false)
                .setElternteilUnbekanntVerstorben(true)
                .setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.VERSTORBEN)
                .setVaterUnbekanntVerstorben(ElternAbwesenheitsGrund.VERSTORBEN)
        );

        gesuchFormular.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                        .setBildungsart(
                            new Bildungsart()
                                .setBildungsstufe(Bildungsstufe.TERTIAER)
                        )
                )
        );

        //Act
        BerechnungsresultatDto berechnungsresultatDto = null;
        for (int i = 0; i< 1; i++) { // for profiling
            berechnungsresultatDto = berechnungService.getBerechnungsresultatFromGesuchTranche(
                gesuch.getNewestGesuchTranche().orElseThrow(NotFoundException::new), 1, 0
            );
        }

        //Assert
        assertThat(berechnungsresultatDto.getBerechnung(), is(not(nullValue())));
    }

    @Test
    @TestAsGesuchsteller
    void testFall7GesuchBerechnung() {
        //Arrange
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());
        final var gesuchFormular = gesuch.getNewestGesuchTranche().get().getGesuchFormular();

        gesuch.getGesuchsperiode()
            .setAnzahlWochenLehre(47)
            .setAnzahlWochenSchule(38);

        gesuchFormular.getPersonInAusbildung()
            .setWohnsitz(Wohnsitz.EIGENER_HAUSHALT)
            .setGeburtsdatum(LocalDate.now().minusDays(1).minusYears(23));

        gesuchFormular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setNettoerwerbseinkommen(16701)
                .setVermoegen(0)
                .setFahrkosten(0)
                .setRenten(0)
                .setErgaenzungsleistungen(1800)
                .setAusbildungskostenSekundarstufeZwei(2000)
                .setWohnkosten(9000 - 2754) // TODO: reduced as WG reduction of grundbedarf is not yet implemented
                .setFahrkosten(600)
                .setAuswaertigeMittagessenProWoche(0)
        );

        gesuchFormular.setFamiliensituation(
            new Familiensituation()
                .setElternVerheiratetZusammen(false)
                .setGerichtlicheAlimentenregelung(false)
                .setWerZahltAlimente(Elternschaftsteilung.GEMEINSAM)
                .setElternteilUnbekanntVerstorben(true)
                .setVaterUnbekanntVerstorben(ElternAbwesenheitsGrund.VERSTORBEN)
                .setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.WEDER_NOCH)
                .setMutterWiederverheiratet(false)
                .setVaterWiederverheiratet(false)
        );

        gesuchFormular.setElterns(
            Set.of(
                (Eltern) new Eltern()
                    .setElternTyp(ElternTyp.MUTTER)
                    .setWohnkosten(14000)
                    .setGeburtsdatum(LocalDate.now().minusYears(45))
            )
        );

        gesuchFormular.setSteuerdaten(
            Set.of(
                new Steuerdaten()
                    .setSteuerdatenTyp(SteuerdatenTyp.MUTTER)
                    .setVerpflegung(3200)
                    .setVerpflegungPartner(0)
                    .setFahrkosten(3696)
                    .setFahrkostenPartner(0)
                    // .setIntegrationszulage(7200) TODO: Missing
                    .setSteuernBund(0)
                    .setSteuernStaat(0)
                    .setTotalEinkuenfte(1026)
                    .setIsArbeitsverhaeltnisSelbstaendig(false)
                    .setErgaenzungsleistungen(21000 - 2400)  // TODO: adaption for integrationszulage
            )
        );

        gesuchFormular.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                        .setBildungsart(
                            new Bildungsart()
                                .setBildungsstufe(Bildungsstufe.SEKUNDAR_2)
                        )
                )
        );

        //Act
        final BerechnungsresultatDto berechnungsresultatDto = berechnungService.getBerechnungsresultatFromGesuchTranche(
            gesuch.getNewestGesuchTranche().orElseThrow(NotFoundException::new), 1, 0)
            ;;

        //Assert
        assertThat(berechnungsresultatDto.getBerechnung(), is(equalTo(6669)));
    }

    @Test
    @TestAsGesuchsteller
    void testFall8GesuchBerechnung() {
        //Arrange
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());
        final var gesuchFormular = gesuch.getNewestGesuchTranche().get().getGesuchFormular();

        gesuch.getGesuchsperiode()
            .setAnzahlWochenLehre(47)
            .setAnzahlWochenSchule(38);

        gesuchFormular.getPersonInAusbildung()
            .setWohnsitz(Wohnsitz.FAMILIE)
            .setGeburtsdatum(LocalDate.now().minusDays(1).minusYears(29));

        gesuchFormular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setNettoerwerbseinkommen(0)
                .setVermoegen(0)
                .setFahrkosten(0)
                .setRenten(0)
                .setAusbildungskostenSekundarstufeZwei(750)
                .setFahrkosten(0)
                .setAuswaertigeMittagessenProWoche(0)
        );

        gesuchFormular.setFamiliensituation(
            new Familiensituation()
                .setElternVerheiratetZusammen(false)
                .setGerichtlicheAlimentenregelung(false)
                .setWerZahltAlimente(Elternschaftsteilung.GEMEINSAM)
                .setElternteilUnbekanntVerstorben(true)
                .setVaterUnbekanntVerstorben(ElternAbwesenheitsGrund.VERSTORBEN)
                .setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.WEDER_NOCH)
                .setMutterWiederverheiratet(false)
                .setVaterWiederverheiratet(false)
        );

        gesuchFormular.setElterns(
            Set.of(
                (Eltern) new Eltern()
                    .setElternTyp(ElternTyp.MUTTER)
                    .setWohnkosten(20000)
                    .setGeburtsdatum(LocalDate.now().minusYears(45))
            )
        );

        gesuchFormular.setSteuerdaten(
            Set.of(
                new Steuerdaten()
                    .setSteuerdatenTyp(SteuerdatenTyp.MUTTER)
                    .setVerpflegung(3200)
                    .setVerpflegungPartner(0)
                    .setFahrkosten(3696)
                    .setFahrkostenPartner(0)
                    // .setIntegrationszulage(7200) TODO: Missing
                    .setSteuernBund(0)
                    .setSteuernStaat(1857)
                    .setEigenmietwert(10500)
                    .setTotalEinkuenfte(87516 - 7200) // TODO: adaption for integrationszulage
                    .setSaeule2(1500)
                    .setVermoegen(100000)
                    .setIsArbeitsverhaeltnisSelbstaendig(true)
                    .setErgaenzungsleistungen(0)
            )
        );

        gesuchFormular.setGeschwisters(
            Set.of(
                (Geschwister) new Geschwister()
                    .setWohnsitz(Wohnsitz.FAMILIE)
                    .setGeburtsdatum(LocalDate.now().minusDays(1).minusYears(17)),
                (Geschwister) new Geschwister()
                    .setWohnsitz(Wohnsitz.FAMILIE)
                    .setGeburtsdatum(LocalDate.now().minusDays(1).minusYears(27))
            )
        );

        gesuchFormular.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                        .setBildungsart(
                            new Bildungsart()
                                .setBildungsstufe(Bildungsstufe.SEKUNDAR_2)
                        )
                )
        );

        //Act
        final BerechnungsresultatDto berechnungsresultatDto = berechnungService.getBerechnungsresultatFromGesuchTranche(
            gesuch.getNewestGesuchTranche().orElseThrow(NotFoundException::new), 1, 0
        );

        //Assert
        assertThat(berechnungsresultatDto.getBerechnung(), is(equalTo(266)));
    }
}
