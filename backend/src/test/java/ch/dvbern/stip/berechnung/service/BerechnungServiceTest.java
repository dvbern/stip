package ch.dvbern.stip.berechnung.service;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.bildungskategorie.entity.Bildungskategorie;
import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.type.Taetigkeitsart;
import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.api.stammdaten.type.Land;
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
        "8, 0",   // muss noch angepasst werden, wenn fachliche Abklärungen gemacht wurden
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
            .setZivilstand(Zivilstand.LEDIG)
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
                        .setBildungskategorie(
                            new Bildungskategorie()
                                .setBfs(10)
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
            .setZivilstand(Zivilstand.LEDIG)
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
                    .setGeburtsdatum(LocalDate.now().minusYears(45))
            )
        );

        gesuchFormular.setSteuerdaten(
            Set.of(
                new Steuerdaten()
                    .setWohnkosten(14000)
                    .setSteuerdatenTyp(SteuerdatenTyp.MUTTER)
                    .setVerpflegung(3200)
                    .setVerpflegungPartner(0)
                    .setFahrkosten(3696)
                    .setFahrkostenPartner(0)
                    .setSteuernBund(0)
                    .setSteuernKantonGemeinde(0)
                    .setTotalEinkuenfte(1026)
                    .setIsArbeitsverhaeltnisSelbstaendig(false)
                    .setErgaenzungsleistungen(21000)
            )
        );

        gesuchFormular.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                        .setBildungskategorie(
                            new Bildungskategorie()
                                .setBfs(5)
                        )
                )
        );

        //Act
        final BerechnungsresultatDto berechnungsresultatDto = berechnungService.getBerechnungsresultatFromGesuchTranche(
            gesuch.getNewestGesuchTranche().orElseThrow(NotFoundException::new), 1, 0)
            ;

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
            .setZivilstand(Zivilstand.LEDIG)
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
                    .setGeburtsdatum(LocalDate.now().minusYears(45))
            )
        );

        gesuchFormular.setSteuerdaten(
            Set.of(
                new Steuerdaten()
                    .setWohnkosten(20000)
                    .setSteuerdatenTyp(SteuerdatenTyp.MUTTER)
                    .setVerpflegung(3200)
                    .setVerpflegungPartner(0)
                    .setFahrkosten(3696)
                    .setFahrkostenPartner(0)
                    .setSteuernBund(0)
                    .setSteuernKantonGemeinde(1857)
                    .setEigenmietwert(10500)
                    .setTotalEinkuenfte(87516)
                    .setSaeule2(1500)
                    .setVermoegen(100000)
                    .setIsArbeitsverhaeltnisSelbstaendig(true)
                    .setErgaenzungsleistungen(0)
            )
        );

        gesuchFormular.setGeschwisters(
            Set.of(
                (Geschwister) new Geschwister()
                    .setAusbildungssituation(Ausbildungssituation.IN_AUSBILDUNG)
                    .setWohnsitz(Wohnsitz.FAMILIE)
                    .setGeburtsdatum(LocalDate.now().minusDays(1).minusYears(17)),
                (Geschwister) new Geschwister()
                    .setAusbildungssituation(Ausbildungssituation.IN_AUSBILDUNG)
                    .setWohnsitz(Wohnsitz.FAMILIE)
                    .setGeburtsdatum(LocalDate.now().minusDays(1).minusYears(27))
            )
        );

        gesuchFormular.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                        .setBildungskategorie(
                            new Bildungskategorie()
                                .setBfs(5)
                        )
                )
        );

        //Act
        final BerechnungsresultatDto berechnungsresultatDto = berechnungService.getBerechnungsresultatFromGesuchTranche(
            gesuch.getNewestGesuchTranche().orElseThrow(NotFoundException::new), 1, 0
        );

        //Assert
        assertThat(berechnungsresultatDto.getBerechnung(), is(equalTo(0)));
    }


    @Test
    @TestAsGesuchsteller
    void testFall5GesuchBerechnungKinder() {
        //Arrange
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());
        final var gesuchFormular = gesuch.getNewestGesuchTranche().get().getGesuchFormular();

        gesuch.getGesuchsperiode()
            .setAnzahlWochenLehre(47)
            .setAnzahlWochenSchule(38);

        gesuchFormular.getPersonInAusbildung()
            .setSozialhilfebeitraege(true)
            .setZivilstand(Zivilstand.VERHEIRATET)
            .setNiederlassungsstatus(Niederlassungsstatus.FLUECHTLING)
            .setNationalitaet(Land.IR)
            .setWohnsitz(Wohnsitz.EIGENER_HAUSHALT)
            .setGeburtsdatum(LocalDate.of(1996,07,01));

        gesuchFormular.setPartner((Partner) new Partner()
            .setJahreseinkommen(25000)
            .setVerpflegungskosten(1000)
            .setFahrkosten(1600)
            .setGeburtsdatum(LocalDate.of(1990,12,1)));

        gesuchFormular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setNettoerwerbseinkommen(0)
                .setVermoegen(0)
                .setFahrkosten(0)
                .setRenten(0)
                .setZulagen(5520)
                .setAusbildungskostenTertiaerstufe(0)
                .setAusbildungskostenSekundarstufeZwei(500)
                .setBetreuungskostenKinder(3333)
                .setFahrkosten(790)
                .setWohnkosten(18000)
                .setWgWohnend(false)
                .setVermoegen(12)
                .setSteuerjahr(2023)
                .setVeranlagungsCode(0)
                .setVerdienstRealisiert(false)
                .setWillDarlehen(true)
                .setAuswaertigeMittagessenProWoche(0)
        );

        gesuchFormular.setFamiliensituation(
            new Familiensituation()
                .setElternVerheiratetZusammen(false)
                .setGerichtlicheAlimentenregelung(false)
                .setElternteilUnbekanntVerstorben(true)
                .setVaterUnbekanntVerstorben(ElternAbwesenheitsGrund.UNBEKANNT)
                .setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.UNBEKANNT)
                .setMutterWiederverheiratet(false)
                .setVaterWiederverheiratet(false)
        );

        gesuchFormular.setLebenslaufItems(Set.of(
            new LebenslaufItem()
                .setTaetigkeitsart(Taetigkeitsart.ANDERE_TAETIGKEIT)
                .setVon(LocalDate.of(2011,8,1))
                .setBis(LocalDate.of(2021,11,30))
                .setTaetigkeitsBeschreibung("Hausfrau"),
            new LebenslaufItem()
                .setTaetigkeitsart(Taetigkeitsart.ANDERE_TAETIGKEIT)
                .setVon(LocalDate.of(2021,12,1))
                .setBis(LocalDate.of(2022,7,30))
                .setTaetigkeitsBeschreibung("Flucht"),
            new LebenslaufItem().
                setTaetigkeitsart(Taetigkeitsart.ERWERBSTAETIGKEIT)
                .setVon(LocalDate.of(2022,8,1))
                .setBis(LocalDate.of(2024,7,30))
                .setTaetigkeitsBeschreibung("Diverse Jobs"),
            new LebenslaufItem()
                .setTaetigkeitsart(Taetigkeitsart.ERWERBSTAETIGKEIT)
                .setVon(LocalDate.of(2024,8,1))
                .setBis(LocalDate.of(2026,7,30))
                .setTaetigkeitsBeschreibung("BFF Bern: EBA Lehre")
            ));

        gesuchFormular.setElterns(
            Set.of(
            )
        );

        gesuchFormular.setSteuerdaten(
            Set.of(
            )
        );

        gesuchFormular.setGeschwisters(
            Set.of(
            )
        );

        Kind kind1 = (Kind) new Kind()
            .setNachname("Testfall5")
            .setVorname("Kind1")
            .setGeburtsdatum(LocalDate.of(2013,9,01));
        kind1.setWohnsitzAnteilPia(100);
        kind1.setAusbildungssituation(Ausbildungssituation.VORSCHULPFLICHTIG);

        Kind kind2 = (Kind) new Kind()
            .setNachname("Testfall5")
            .setVorname("Kind2")
            .setGeburtsdatum(LocalDate.of(2019,6,01));
        kind2.setWohnsitzAnteilPia(100);
        kind2.setAusbildungssituation(Ausbildungssituation.VORSCHULPFLICHTIG);

        gesuchFormular.setKinds(
            Set.of(
                kind1,kind2
            )
        );

        gesuchFormular.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                        .setBildungskategorie(
                            new Bildungskategorie()
                                .setBfs(5)
                        )
                )
        );

        //Act
        final BerechnungsresultatDto berechnungsresultatDto = berechnungService.getBerechnungsresultatFromGesuchTranche(
            gesuch.getNewestGesuchTranche().orElseThrow(NotFoundException::new), 1, 0
        );

        //Assert
        assertThat(berechnungsresultatDto.getBerechnung(), is(equalTo(9938)));
    }

    @Test
    @TestAsGesuchsteller
    void testFall6BerechnungEinKind() {
        //Arrange
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());
        final var gesuchFormular = gesuch.getNewestGesuchTranche().get().getGesuchFormular();

        gesuch.getGesuchsperiode()
            .setAnzahlWochenLehre(47)
            .setAnzahlWochenSchule(38)
            .setReduzierungDesGrundbedarfs(2754);

        gesuchFormular.getPersonInAusbildung()
            .setSozialhilfebeitraege(true)
            .setZivilstand(Zivilstand.LEDIG)
            .setNiederlassungsstatus(Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_C)
            .setNationalitaet(Land.DE)
            .setWohnsitz(Wohnsitz.EIGENER_HAUSHALT)
            .setGeburtsdatum(LocalDate.of(1988,04,01));


        gesuchFormular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setNettoerwerbseinkommen(0)
                .setVermoegen(0)
                .setFahrkosten(800)
                .setRenten(0)
                .setZulagen(0)
                .setAusbildungskostenTertiaerstufe(2000)
                .setAusbildungskostenSekundarstufeZwei(0)
                .setBetreuungskostenKinder(0)
                .setFahrkosten(800)
                .setWohnkosten(11460)
                .setWgWohnend(true)
                .setVermoegen(0)
                .setSteuerjahr(2023)
                .setVeranlagungsCode(0)
                .setVerdienstRealisiert(false)
                .setWillDarlehen(true)
                .setAuswaertigeMittagessenProWoche(0)
        );

        gesuchFormular.setFamiliensituation(
            new Familiensituation()
                .setElternVerheiratetZusammen(false)
                .setGerichtlicheAlimentenregelung(false)
                .setElternteilUnbekanntVerstorben(false)
                .setWerZahltAlimente(Elternschaftsteilung.GEMEINSAM)
                .setMutterWiederverheiratet(false)
                .setVaterWiederverheiratet(false)
        );

        gesuchFormular.setLebenslaufItems(Set.of(
            new LebenslaufItem()
                .setTaetigkeitsart(Taetigkeitsart.ANDERE_TAETIGKEIT)
                .setVon(LocalDate.of(2004,8,1))
                .setBis(LocalDate.of(2008,7,30))
                .setTaetigkeitsBeschreibung("Gymnasiale Maturität"),
            new LebenslaufItem()
                .setTaetigkeitsart(Taetigkeitsart.ERWERBSTAETIGKEIT)
                .setVon(LocalDate.of(2008,8,1))
                .setBis(LocalDate.of(2013,6,30))
                .setTaetigkeitsBeschreibung("Diverse Jobs"),
            new LebenslaufItem()
                .setTaetigkeitsart(Taetigkeitsart.ERWERBSTAETIGKEIT)
                .setVon(LocalDate.of(2013,7,1))
                .setBis(LocalDate.of(2019,8,31))
                .setTaetigkeitsBeschreibung("Diverse Jobs"),
            new LebenslaufItem()
                .setTaetigkeitsart(Taetigkeitsart.ANDERE_TAETIGKEIT)
                .setVon(LocalDate.of(2019,8,1))
                .setBis(LocalDate.of(2023,8,31))
                .setTaetigkeitsBeschreibung("Bachelor (Hochschule/UNI)"),
            new LebenslaufItem()
                .setTaetigkeitsart(Taetigkeitsart.ANDERE_TAETIGKEIT)
                .setVon(LocalDate.of(2023,9,1))
                .setBis(LocalDate.of(2026,8,31))
                .setTaetigkeitsBeschreibung("Fachhochschule Nordwestschweiz: Bachelor")
        ));

        gesuchFormular.setElterns(
            Set.of( ((Eltern)
                new Eltern().setGeburtsdatum(LocalDate.of(1963,8,1)))
                    .setElternTyp(ElternTyp.VATER)
                    ,
                ((Eltern)
                    new Eltern().setGeburtsdatum(LocalDate.of(1963,6,1)))
                    .setElternTyp(ElternTyp.MUTTER)
            )
        );

        gesuchFormular.setSteuerdaten(
            Set.of( new Steuerdaten().setSteuerdatenTyp(SteuerdatenTyp.MUTTER)
                .setTotalEinkuenfte(15201)
                .setIsArbeitsverhaeltnisSelbstaendig(false)
                    .setVerpflegung(0)
                    .setVerpflegungPartner(0)
                    .setFahrkosten(0)
                    .setFahrkostenPartner(0)
                    .setSteuernBund(0)
                    .setSteuernKantonGemeinde(0)
                    .setWohnkosten(0)
                    .setEigenmietwert(0),
                new Steuerdaten().setSteuerdatenTyp(SteuerdatenTyp.VATER)
                    .setIsArbeitsverhaeltnisSelbstaendig(false)
                    .setVerpflegung(0)
                    .setVerpflegungPartner(0)
                    .setFahrkosten(0)
                    .setFahrkostenPartner(0)
                    .setSteuernBund(0)
                    .setSteuernKantonGemeinde(0)
                    .setWohnkosten(0)
                    .setEigenmietwert(0)
            )
        );

        gesuchFormular.setGeschwisters(
            Set.of(
            )
        );

        Kind kind1 = (Kind) new Kind()
            .setNachname("Testfall6")
            .setVorname("Kind1")
            .setGeburtsdatum(LocalDate.of(2008,8,01));
        kind1.setWohnsitzAnteilPia(0);
        kind1.setAusbildungssituation(Ausbildungssituation.VORSCHULPFLICHTIG);

        gesuchFormular.setKinds(
            Set.of(
                kind1
            )
        );

        gesuchFormular.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                        .setBildungskategorie(
                            new Bildungskategorie()
                                .setBfs(8)
                        )
                ).setPensum(AusbildungsPensum.TEILZEIT)
        );

        //Act
        final BerechnungsresultatDto berechnungsresultatDto = berechnungService.getBerechnungsresultatFromGesuchTranche(
            gesuch.getNewestGesuchTranche().orElseThrow(NotFoundException::new), 1, 0
        );

        LOG.info(berechnungsresultatDto.toString());

        //Assert
        assertThat(berechnungsresultatDto.getBerechnung(), is(equalTo(27179)));
    }
}
