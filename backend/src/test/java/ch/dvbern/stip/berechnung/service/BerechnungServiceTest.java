package ch.dvbern.stip.berechnung.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.bildungskategorie.entity.Bildungskategorie;
import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.type.Taetigkeitsart;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.berechnung.util.BerechnungUtil;
import ch.dvbern.stip.generated.dto.TranchenBerechnungsresultatDto;
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
        "1, -6427",
        "2, -14192",
        "3, 6441",
        "4, -17986",
        "5, -39751", // muss noch angepasst werden, wenn fachliche Abklärungen gemacht wurden
        "6, -27179",
        "7, -6669",
        "8, -266",   // muss noch angepasst werden, wenn fachliche Abklärungen gemacht wurden
        "9, -23527"
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

        gesuch.setGesuchTranchen(
            List.of(
                gesuch.getNewestGesuchTranche().get()
                    .setGueltigkeit(
                        new DateRange(
                            LocalDate.of(2023, 8, 1),
                            LocalDate.of(2024, 7, 31)
                        )
                    )
            )
        );

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
        List<TranchenBerechnungsresultatDto> tranchenBerechnungsresultatDtos = null;
        for (int i = 0; i< 1; i++) { // for profiling
            tranchenBerechnungsresultatDtos = berechnungService.getBerechnungsresultatFromGesuchTranche(
                gesuch.getNewestGesuchTranche().orElseThrow(NotFoundException::new), 1, 0
            );
        }

        //Assert
        for (final var berechnungsresultatDto : tranchenBerechnungsresultatDtos) {
            assertThat(berechnungsresultatDto.getBerechnung(), is(not(nullValue())));
        }
    }

    @Test
    @TestAsGesuchsteller
    void testFall7GesuchBerechnung() {
        //Arrange
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());
        final var gesuchFormular = gesuch.getNewestGesuchTranche().get().getGesuchFormular();

        gesuch.setGesuchTranchen(
            List.of(
                gesuch.getNewestGesuchTranche().get()
                    .setGueltigkeit(
                        new DateRange(
                            LocalDate.of(2023, 8, 1),
                            LocalDate.of(2024, 7, 31)
                        )
                    )
            )
        );

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
        final var berechnungsresultatDto = berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0);

        //Assert
        assertThat(berechnungsresultatDto.getTranchenBerechnungsresultate().size(), is(1));
        assertThat(berechnungsresultatDto.getBerechnung(), is(equalTo(6669)));
    }

    @Test
    @TestAsGesuchsteller
    void testFall8GesuchBerechnung() {
        //Arrange
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());
        final var gesuchFormular = gesuch.getNewestGesuchTranche().get().getGesuchFormular();

        gesuch.setGesuchTranchen(
            List.of(
                gesuch.getNewestGesuchTranche().get()
                    .setGueltigkeit(
                        new DateRange(
                            LocalDate.of(2023, 8, 1),
                            LocalDate.of(2024, 7, 31)
                        )
                    )
            )
        );

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
        final var berechnungsresultatDto = berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0);

        //Assert
        assertThat(berechnungsresultatDto.getTranchenBerechnungsresultate().size(), is(1));
        assertThat(berechnungsresultatDto.getBerechnung(), is(equalTo(266)));
    }

    @Test
    @TestAsGesuchsteller
    void testFall11GesuchBerechnung() {
        //Arrange
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());
        final var gesuchFormular = gesuch.getNewestGesuchTranche().get().getGesuchFormular();
        gesuch.setGesuchTranchen(
            List.of(
                gesuch.getNewestGesuchTranche().get()
                    .setGueltigkeit(
                        new DateRange(
                            LocalDate.of(2023, 8, 1),
                            LocalDate.of(2024, 7, 31)
                        )
                    )
            )
        );

        gesuch.getGesuchsperiode()
            .setAnzahlWochenLehre(47)
            .setAnzahlWochenSchule(38);

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

        gesuchFormular.getPersonInAusbildung()
            .setZivilstand(Zivilstand.LEDIG)
            .setSozialhilfebeitraege(false)
            .setWohnsitz(Wohnsitz.MUTTER_VATER)
            .setWohnsitzAnteilMutter(BigDecimal.valueOf(0))
            .setWohnsitzAnteilVater(BigDecimal.valueOf(100))
            .setGeburtsdatum( // Was 2000-01-01, used LocalDate.now to ensure complicity in the future
                LocalDate.now().minusYears(24).minusMonths(6)
            );

        gesuchFormular.setFamiliensituation(
            new Familiensituation()
                .setElternVerheiratetZusammen(false)
                .setGerichtlicheAlimentenregelung(false)
                .setElternteilUnbekanntVerstorben(false)
                .setMutterWiederverheiratet(false)
                .setVaterWiederverheiratet(false)
        );

        gesuchFormular.setElterns(
            Set.of(
                (Eltern) new Eltern()
                    .setElternTyp(ElternTyp.VATER)
                    .setGeburtsdatum(LocalDate.of(1960, 1, 1)),
                (Eltern) new Eltern()
                    .setElternTyp(ElternTyp.MUTTER)
                    .setGeburtsdatum(LocalDate.of(1961, 1, 1))
            )
        );

        gesuchFormular.setGeschwisters(
            Set.of(
                (Geschwister) new Geschwister()
                    .setAusbildungssituation(Ausbildungssituation.IN_AUSBILDUNG)
                    .setWohnsitz(Wohnsitz.MUTTER_VATER)
                    .setWohnsitzAnteilVater(BigDecimal.valueOf(100))
                    .setWohnsitzAnteilMutter(BigDecimal.valueOf(0))
                    .setGeburtsdatum(LocalDate.now().minusYears(19)),
                (Geschwister) new Geschwister()
                    .setAusbildungssituation(Ausbildungssituation.IN_AUSBILDUNG)
                    .setWohnsitz(Wohnsitz.MUTTER_VATER)
                    .setWohnsitzAnteilVater(BigDecimal.valueOf(0))
                    .setWohnsitzAnteilMutter(BigDecimal.valueOf(100))
                    .setGeburtsdatum(LocalDate.now().minusYears(16)),
                (Geschwister) new Geschwister()
                    .setAusbildungssituation(Ausbildungssituation.KEINE)
                    .setWohnsitz(Wohnsitz.MUTTER_VATER)
                    .setWohnsitzAnteilVater(BigDecimal.valueOf(50))
                    .setWohnsitzAnteilMutter(BigDecimal.valueOf(50))
                    .setGeburtsdatum(LocalDate.now().minusYears(16)),
                (Geschwister) new Geschwister()
                    .setAusbildungssituation(Ausbildungssituation.KEINE)
                    .setWohnsitz(Wohnsitz.MUTTER_VATER)
                    .setWohnsitzAnteilVater(BigDecimal.valueOf(50))
                    .setWohnsitzAnteilMutter(BigDecimal.valueOf(50))
                    .setGeburtsdatum(LocalDate.now().minusYears(16))
            )
        );

        gesuchFormular.setLebenslaufItems(
            Set.of(
                new LebenslaufItem()
                    .setVon(LocalDate.of(2016, 8 ,1))
                    .setBis(LocalDate.of(2023, 8 ,1))
                    .setTaetigkeitsart(Taetigkeitsart.ANDERE_TAETIGKEIT)
            )
        );

        gesuchFormular.setKinds(Set.of());

        gesuchFormular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setNettoerwerbseinkommen(0)
                .setVermoegen(0)
                .setFahrkosten(925)
                .setRenten(0)
                .setAusbildungskostenTertiaerstufe(0)
                .setAusbildungskostenSekundarstufeZwei(950)
                .setAuswaertigeMittagessenProWoche(5)
        );


        gesuchFormular.setSteuerdaten(
            Set.of(
                new Steuerdaten()
                    .setSteuerdatenTyp(SteuerdatenTyp.VATER)
                    .setSteuernKantonGemeinde(0)
                    .setSteuernBund(0)
                    .setFahrkosten(0)
                    .setVerpflegung(0)
                    .setTotalEinkuenfte(38_820)
                    .setIsArbeitsverhaeltnisSelbstaendig(false)
                    .setKinderalimente(0)
                    .setVermoegen(2717)
                    .setWohnkosten(9_000)
                    .setFahrkostenPartner(0)
                    .setVerpflegungPartner(0)
                    .setEigenmietwert(0)
                    .setErgaenzungsleistungen(0)
                    .setSozialhilfebeitraege(0),
                new Steuerdaten()
                    .setSteuerdatenTyp(SteuerdatenTyp.MUTTER)
                    .setSteuernKantonGemeinde(1_192)
                    .setSteuernBund(0)
                    .setFahrkosten(0)
                    .setVerpflegung(0)
                    .setTotalEinkuenfte(63_484)
                    .setIsArbeitsverhaeltnisSelbstaendig(false)
                    .setKinderalimente(0)
                    .setVermoegen(918)
                    .setWohnkosten(12_720)
                    .setFahrkostenPartner(0)
                    .setVerpflegungPartner(0)
                    .setErgaenzungsleistungen(0)
                    .setSozialhilfebeitraege(0)
            )
        );

        //Act
        final var berechnungsresultatDto = berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0);

        LOG.info(berechnungsresultatDto.toString());

        //Assert
        assertThat(berechnungsresultatDto.getTranchenBerechnungsresultate().size(), is(2));
        assertThat(berechnungsresultatDto.getBerechnung(), is(equalTo(7044)));
    }

    @Test
    @TestAsGesuchsteller
    void testFall14GesuchBerechnung() {
        //Arrange
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());
        final var gesuchFormular = gesuch.getNewestGesuchTranche().get().getGesuchFormular();
        gesuch.setGesuchTranchen(
            List.of(
                gesuch.getNewestGesuchTranche().get()
                    .setGueltigkeit(
                        new DateRange(
                            LocalDate.of(2023, 8, 1),
                            LocalDate.of(2024, 7, 31)
                        )
                    )
            )
        );

        gesuch.getGesuchsperiode()
            .setAnzahlWochenLehre(47)
            .setAnzahlWochenSchule(38);

        gesuchFormular.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                        .setBildungskategorie(
                            new Bildungskategorie()
                                .setBfs(9)
                        )
                )
        );

        gesuchFormular.getPersonInAusbildung()
            .setZivilstand(Zivilstand.LEDIG)
            .setSozialhilfebeitraege(false)
            .setWohnsitz(Wohnsitz.MUTTER_VATER)
            .setWohnsitzAnteilMutter(BigDecimal.valueOf(50))
            .setWohnsitzAnteilVater(BigDecimal.valueOf(50))
            .setGeburtsdatum(
                LocalDate.now().minusYears(23)
            );

        gesuchFormular.setFamiliensituation(
            new Familiensituation()
                .setElternVerheiratetZusammen(false)
                .setGerichtlicheAlimentenregelung(false)
                .setElternteilUnbekanntVerstorben(false)
                .setMutterWiederverheiratet(false)
                .setVaterWiederverheiratet(false)
        );

        gesuchFormular.setElterns(
            Set.of(
                (Eltern) new Eltern()
                    .setElternTyp(ElternTyp.VATER)
                    .setGeburtsdatum(LocalDate.of(1960, 1, 1)),
                (Eltern) new Eltern()
                    .setElternTyp(ElternTyp.MUTTER)
                    .setGeburtsdatum(LocalDate.of(1961, 1, 1))
            )
        );

        gesuchFormular.setGeschwisters(
            Set.of(
                (Geschwister) new Geschwister()
                    .setAusbildungssituation(Ausbildungssituation.KEINE)
                    .setWohnsitz(Wohnsitz.MUTTER_VATER)
                    .setWohnsitzAnteilVater(BigDecimal.valueOf(50))
                    .setWohnsitzAnteilMutter(BigDecimal.valueOf(50))
                    .setGeburtsdatum(LocalDate.now().minusYears(16)),
                (Geschwister) new Geschwister()
                    .setAusbildungssituation(Ausbildungssituation.KEINE)
                    .setWohnsitz(Wohnsitz.MUTTER_VATER)
                    .setWohnsitzAnteilVater(BigDecimal.valueOf(50))
                    .setWohnsitzAnteilMutter(BigDecimal.valueOf(50))
                    .setGeburtsdatum(LocalDate.now().minusYears(16))
            )
        );

        gesuchFormular.setLebenslaufItems(
            Set.of(
                new LebenslaufItem()
                    .setVon(LocalDate.of(2016, 8 ,1))
                    .setBis(LocalDate.of(2023, 8 ,1))
                    .setTaetigkeitsart(Taetigkeitsart.ANDERE_TAETIGKEIT)
            )
        );

        gesuchFormular.setKinds(Set.of());

        gesuchFormular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setNettoerwerbseinkommen(9_100+gesuch.getGesuchsperiode().getEinkommensfreibetrag())
                .setVermoegen(0)
                .setFahrkosten(2_760)
                .setRenten(0)
                .setAusbildungskostenTertiaerstufe(1_500)
                .setAusbildungskostenSekundarstufeZwei(0)
                .setAuswaertigeMittagessenProWoche(0)
        );


        gesuchFormular.setSteuerdaten(
            Set.of(
                new Steuerdaten()
                    .setSteuerdatenTyp(SteuerdatenTyp.VATER)
                    .setTotalEinkuenfte(45_000)
                    .setSteuernKantonGemeinde(0)
                    .setSteuernBund(0)
                    .setFahrkosten(0)
                    .setVerpflegung(0)
                    .setIsArbeitsverhaeltnisSelbstaendig(false)
                    .setKinderalimente(0)
                    .setVermoegen(0)
                    .setWohnkosten(19_992)
                    .setFahrkostenPartner(0)
                    .setVerpflegungPartner(0)
                    .setEigenmietwert(0)
                    .setErgaenzungsleistungen(0)
                    .setSozialhilfebeitraege(0),
                new Steuerdaten()
                    .setSteuerdatenTyp(SteuerdatenTyp.MUTTER)
                    .setTotalEinkuenfte(5_667)
                    .setSteuernKantonGemeinde(0)
                    .setSteuernBund(0)
                    .setFahrkosten(0)
                    .setVerpflegung(0)
                    .setIsArbeitsverhaeltnisSelbstaendig(false)
                    .setKinderalimente(0)
                    .setVermoegen(0)
                    .setWohnkosten(18_000)
                    .setFahrkostenPartner(0)
                    .setVerpflegungPartner(0)
                    .setErgaenzungsleistungen(0)
                    .setSozialhilfebeitraege(0)
            )
        );

        //Act
        final var berechnungsresultatDto = berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0);

        LOG.info(berechnungsresultatDto.toString());

        //Assert
        assertThat(berechnungsresultatDto.getTranchenBerechnungsresultate().size(), is(2));
        assertThat(berechnungsresultatDto.getBerechnung(), is(equalTo(2126)));
    }
}
