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

package ch.dvbern.stip.berechnung.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum;
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
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
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
class BerechnungTest {
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
    @CsvSource(
        {
            "1, -6427",
            "2, -14192",
            "3, 6441",
            "4, -17986",
            "5, -39751", // muss noch angepasst werden, wenn fachliche Abklärungen gemacht wurden
            "6, -27179",
            "7, -6669",
            "8, -266", // muss noch angepasst werden, wenn fachliche Abklärungen gemacht wurden
            "9, -23527"
        }
    )
    void testBerechnungFaelle(final int fall, final int expectedStipendien) {
        // Load Fall resources/berechnung/fall_{fall}.json, deserialize to a BerechnungRequestV1
        // and calculate Stipendien for it
        final var result = berechnungService.calculateStipendien(BerechnungUtil.getRequest(fall));
        assertThat(result.getStipendien(), is(expectedStipendien));
    }

    @Test
    @TestAsGesuchsteller
    void testMinimalGesuchBerechnung() {
        // Arrange
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());

        gesuch.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                        .setBildungskategorie(
                            new Bildungskategorie()
                                .setBfs(10)
                        )
                )
        );

        final var gesuchTranche = gesuch.getNewestGesuchTranche().get();
        final var gesuchFormular = gesuchTranche.getGesuchFormular();

        gesuchTranche.setTyp(GesuchTrancheTyp.TRANCHE);

        gesuchFormular.getPersonInAusbildung()
            .setZivilstand(Zivilstand.LEDIG)
            .setWohnsitz(Wohnsitz.EIGENER_HAUSHALT)
            .setGeburtsdatum(LocalDate.now().minusYears(18).minusDays(1));

        gesuchFormular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setNettoerwerbseinkommen(0)
                .setErgaenzungsleistungen(0)
                .setWohnkosten(10000)
                .setAusbildungskostenSekundarstufeZwei(2000)
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

        // Act
        List<TranchenBerechnungsresultatDto> tranchenBerechnungsresultatDtos = null;
        for (int i = 0; i < 1; i++) { // for profiling
            tranchenBerechnungsresultatDtos = berechnungService.getBerechnungsresultatFromGesuchTranche(
                gesuch.getNewestGesuchTranche().orElseThrow(NotFoundException::new),
                1,
                0
            );
        }

        // Assert
        for (final var berechnungsresultatDto : tranchenBerechnungsresultatDtos) {
            assertThat(berechnungsresultatDto.getBerechnung(), is(not(nullValue())));
        }
    }

    @Test
    @TestAsGesuchsteller
    void testFall7GesuchBerechnung() {
        // Arrange
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());

        gesuch.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                        .setBildungskategorie(
                            new Bildungskategorie()
                                .setBfs(5)
                        )
                )
        );

        final var gesuchFormular = gesuch.getNewestGesuchTranche().get().getGesuchFormular();

        gesuch.setGesuchTranchen(
            List.of(
                gesuch.getNewestGesuchTranche()
                    .get()
                    .setGueltigkeit(
                        new DateRange(
                            LocalDate.of(2023, 8, 1),
                            LocalDate.of(2024, 7, 31)
                        )
                    )
                    .setTyp(GesuchTrancheTyp.TRANCHE)
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
                    .setSteuernBund(0)
                    .setSteuernKantonGemeinde(0)
                    .setTotalEinkuenfte(1026)
                    .setIsArbeitsverhaeltnisSelbstaendig(false)
            )
        );

        // Act
        final var berechnungsresultatDto = berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0);

        // Assert
        assertThat(berechnungsresultatDto.getTranchenBerechnungsresultate().size(), is(1));
        assertThat(berechnungsresultatDto.getBerechnung(), is(equalTo(6669)));
    }

    @Test
    @TestAsGesuchsteller
    void testFall8GesuchBerechnung() {
        // Arrange
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());

        gesuch.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                        .setBildungskategorie(
                            new Bildungskategorie()
                                .setBfs(5)
                        )
                )
        );

        final var gesuchFormular = gesuch.getNewestGesuchTranche().get().getGesuchFormular();

        gesuch.setGesuchTranchen(
            List.of(
                gesuch.getNewestGesuchTranche()
                    .get()
                    .setGueltigkeit(
                        new DateRange(
                            LocalDate.of(2023, 8, 1),
                            LocalDate.of(2024, 7, 31)
                        )
                    )
                    .setTyp(GesuchTrancheTyp.TRANCHE)
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
                    .setSteuernBund(0)
                    .setSteuernKantonGemeinde(1857)
                    .setEigenmietwert(10500)
                    .setTotalEinkuenfte(87516)
                    .setSaeule2(1500)
                    .setVermoegen(100000)
                    .setIsArbeitsverhaeltnisSelbstaendig(true)
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

        // Act
        final var berechnungsresultatDto = berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0);

        // Assert
        assertThat(berechnungsresultatDto.getTranchenBerechnungsresultate().size(), is(1));
        assertThat(berechnungsresultatDto.getBerechnung(), is(equalTo(0)));
    }

    @Test
    @TestAsGesuchsteller
    void testFall11GesuchBerechnung() {
        // Arrange
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());

        gesuch.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                        .setBildungskategorie(
                            new Bildungskategorie()
                                .setBfs(6)
                        )
                )
        );

        final var gesuchFormular = gesuch.getNewestGesuchTranche().get().getGesuchFormular();
        gesuch.setGesuchTranchen(
            List.of(
                gesuch.getNewestGesuchTranche()
                    .get()
                    .setGueltigkeit(
                        new DateRange(
                            LocalDate.of(2023, 8, 1),
                            LocalDate.of(2024, 7, 31)
                        )
                    )
                    .setTyp(GesuchTrancheTyp.TRANCHE)
            )
        );

        gesuch.getGesuchsperiode()
            .setAnzahlWochenLehre(47)
            .setAnzahlWochenSchule(38);

        gesuchFormular.getPersonInAusbildung()
            .setZivilstand(Zivilstand.LEDIG)
            .setSozialhilfebeitraege(false)
            .setWohnsitz(Wohnsitz.MUTTER_VATER)
            .setWohnsitzAnteilMutter(BigDecimal.valueOf(0))
            .setWohnsitzAnteilVater(BigDecimal.valueOf(100))
            .setGeburtsdatum(
                // Was 2000-01-01, used LocalDate.now to ensure complicity in the future
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
                    .setWohnkosten(9_000)
                    .setErgaenzungsleistungen(0)
                    .setSozialhilfebeitraege(false)
                    .setGeburtsdatum(LocalDate.of(1960, 1, 1)),
                (Eltern) new Eltern()
                    .setElternTyp(ElternTyp.MUTTER)
                    .setWohnkosten(12_720)
                    .setErgaenzungsleistungen(0)
                    .setSozialhilfebeitraege(false)
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
                    .setVon(LocalDate.of(2016, 8, 1))
                    .setBis(LocalDate.of(2023, 8, 1))
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
                .setAusbildungskostenSekundarstufeZwei(0)
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
                    .setFahrkostenPartner(0)
                    .setVerpflegungPartner(0)
                    .setEigenmietwert(0),
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
                    .setFahrkostenPartner(0)
                    .setVerpflegungPartner(0)
            )
        );

        // Act
        final var berechnungsresultatDto = berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0);

        LOG.info(berechnungsresultatDto.toString());

        // Assert
        assertThat(berechnungsresultatDto.getTranchenBerechnungsresultate().size(), is(2));
        assertThat(berechnungsresultatDto.getBerechnung(), is(equalTo(7044)));
    }

    @Test
    @TestAsGesuchsteller
    void testFall14GesuchBerechnung() {
        // Arrange
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());

        gesuch.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                        .setBildungskategorie(
                            new Bildungskategorie()
                                .setBfs(9)
                        )
                )
        );

        final var gesuchFormular = gesuch.getNewestGesuchTranche().get().getGesuchFormular();
        gesuch.setGesuchTranchen(
            List.of(
                gesuch.getNewestGesuchTranche()
                    .get()
                    .setGueltigkeit(
                        new DateRange(
                            LocalDate.of(2023, 8, 1),
                            LocalDate.of(2024, 7, 31)
                        )
                    )
                    .setTyp(GesuchTrancheTyp.TRANCHE)
            )
        );

        gesuch.getGesuchsperiode()
            .setAnzahlWochenLehre(47)
            .setAnzahlWochenSchule(38);

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
                    .setWohnkosten(19_992)
                    .setGeburtsdatum(LocalDate.of(1960, 1, 1)),
                (Eltern) new Eltern()
                    .setElternTyp(ElternTyp.MUTTER)
                    .setWohnkosten(18_000)
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
                    .setVon(LocalDate.of(2016, 8, 1))
                    .setBis(LocalDate.of(2023, 8, 1))
                    .setTaetigkeitsart(Taetigkeitsart.ANDERE_TAETIGKEIT)
            )
        );

        gesuchFormular.setKinds(Set.of());

        gesuchFormular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setNettoerwerbseinkommen(9_100 + gesuch.getGesuchsperiode().getEinkommensfreibetrag())
                .setVermoegen(0)
                .setFahrkosten(2_760)
                .setRenten(0)
                .setAusbildungskostenSekundarstufeZwei(1_500)
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
                    .setFahrkostenPartner(0)
                    .setVerpflegungPartner(0)
                    .setEigenmietwert(0),
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
                    .setFahrkostenPartner(0)
                    .setVerpflegungPartner(0)
            )
        );

        // Act
        final var berechnungsresultatDto = berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0);

        // Assert
        assertThat(berechnungsresultatDto.getTranchenBerechnungsresultate().size(), is(2));
        assertThat(berechnungsresultatDto.getBerechnung(), is(equalTo(2126)));
    }

    @Test
    @TestAsGesuchsteller
    void testFall5GesuchBerechnungKinder() {
        // Arrange
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());

        gesuch.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                        .setBildungskategorie(
                            new Bildungskategorie()
                                .setBfs(5)
                        )
                )
        );

        final var gesuchTranche = gesuch.getNewestGesuchTranche().get();
        final var gesuchFormular = gesuchTranche.getGesuchFormular();
        gesuchTranche.setTyp(GesuchTrancheTyp.TRANCHE);

        gesuch.getGesuchsperiode()
            .setAnzahlWochenLehre(47)
            .setAnzahlWochenSchule(38);

        gesuchFormular.getPersonInAusbildung()
            .setSozialhilfebeitraege(true)
            .setZivilstand(Zivilstand.VERHEIRATET)
            .setNiederlassungsstatus(Niederlassungsstatus.FLUECHTLING)
            .setNationalitaet(Land.IR)
            .setWohnsitz(Wohnsitz.EIGENER_HAUSHALT)
            .setGeburtsdatum(LocalDate.of(1996, 7, 1));

        gesuchFormular.setPartner(
            (Partner) new Partner()
                .setJahreseinkommen(25000)
                .setVerpflegungskosten(1000)
                .setFahrkosten(1600)
                .setGeburtsdatum(LocalDate.of(1990, 12, 1))
        );

        gesuchFormular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setNettoerwerbseinkommen(0)
                .setVermoegen(0)
                .setFahrkosten(0)
                .setRenten(0)
                .setZulagen(5520)
                .setAusbildungskostenSekundarstufeZwei(0)
                .setAusbildungskostenSekundarstufeZwei(500)
                .setBetreuungskostenKinder(3333)
                .setFahrkosten(790)
                .setWohnkosten(18000)
                .setWgWohnend(false)
                .setVermoegen(12)
                .setSteuerjahr(2023)
                .setVeranlagungsCode(0)
                .setVerdienstRealisiert(false)
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

        gesuchFormular.setLebenslaufItems(
            Set.of(
                new LebenslaufItem()
                    .setTaetigkeitsart(Taetigkeitsart.ANDERE_TAETIGKEIT)
                    .setVon(LocalDate.of(2011, 8, 1))
                    .setBis(LocalDate.of(2021, 11, 30))
                    .setTaetigkeitsBeschreibung("Hausfrau"),
                new LebenslaufItem()
                    .setTaetigkeitsart(Taetigkeitsart.ANDERE_TAETIGKEIT)
                    .setVon(LocalDate.of(2021, 12, 1))
                    .setBis(LocalDate.of(2022, 7, 30))
                    .setTaetigkeitsBeschreibung("Flucht"),
                new LebenslaufItem().setTaetigkeitsart(Taetigkeitsart.ERWERBSTAETIGKEIT)
                    .setVon(LocalDate.of(2022, 8, 1))
                    .setBis(LocalDate.of(2024, 7, 30))
                    .setTaetigkeitsBeschreibung("Diverse Jobs"),
                new LebenslaufItem()
                    .setTaetigkeitsart(Taetigkeitsart.ERWERBSTAETIGKEIT)
                    .setVon(LocalDate.of(2024, 8, 1))
                    .setBis(LocalDate.of(2026, 7, 30))
                    .setTaetigkeitsBeschreibung("BFF Bern: EBA Lehre")
            )
        );

        gesuchFormular.setElterns(
            Set.of()
        );

        gesuchFormular.setSteuerdaten(
            Set.of()
        );

        gesuchFormular.setGeschwisters(
            Set.of()
        );

        Kind kind1 = (Kind) new Kind()
            .setNachname("Testfall5")
            .setVorname("Kind1")
            .setGeburtsdatum(LocalDate.of(2013, 9, 1));
        kind1.setWohnsitzAnteilPia(100);
        kind1.setAusbildungssituation(Ausbildungssituation.VORSCHULPFLICHTIG);

        Kind kind2 = (Kind) new Kind()
            .setNachname("Testfall5")
            .setVorname("Kind2")
            .setGeburtsdatum(LocalDate.of(2019, 6, 1));
        kind2.setWohnsitzAnteilPia(100);
        kind2.setAusbildungssituation(Ausbildungssituation.VORSCHULPFLICHTIG);

        gesuchFormular.setKinds(
            Set.of(
                kind1,
                kind2
            )
        );

        // Act
        final var berechnungsresultatDtos = berechnungService.getBerechnungsresultatFromGesuchTranche(
            gesuch.getNewestGesuchTranche().orElseThrow(NotFoundException::new),
            1,
            0
        );

        // Assert
        assertThat(berechnungsresultatDtos.size(), is(equalTo(1)));
        // TODO KSTIP-1503: Um 1 Franken daneben
        assertThat(berechnungsresultatDtos.get(0).getBerechnung(), is(equalTo(-9938)));
    }

    @Test
    @TestAsGesuchsteller
    void testFall6BerechnungEinKind() {
        // Arrange
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());

        gesuch.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                        .setBildungskategorie(
                            new Bildungskategorie()
                                .setBfs(8)
                        )
                )
                .setPensum(AusbildungsPensum.TEILZEIT)
        );

        final var gesuchTranche = gesuch.getNewestGesuchTranche().get();
        final var gesuchFormular = gesuchTranche.getGesuchFormular();
        gesuchTranche.setTyp(GesuchTrancheTyp.TRANCHE);

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
            .setGeburtsdatum(LocalDate.of(1988, 4, 1));

        gesuchFormular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setNettoerwerbseinkommen(0)
                .setVermoegen(0)
                .setFahrkosten(800)
                .setRenten(0)
                .setZulagen(0)
                .setAusbildungskostenSekundarstufeZwei(2000)
                .setBetreuungskostenKinder(0)
                .setFahrkosten(800)
                .setWohnkosten(11460)
                .setWgWohnend(true)
                .setVermoegen(0)
                .setSteuerjahr(2023)
                .setVeranlagungsCode(0)
                .setVerdienstRealisiert(false)
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

        gesuchFormular.setLebenslaufItems(
            Set.of(
                new LebenslaufItem()
                    .setTaetigkeitsart(Taetigkeitsart.ANDERE_TAETIGKEIT)
                    .setVon(LocalDate.of(2004, 8, 1))
                    .setBis(LocalDate.of(2008, 7, 30))
                    .setTaetigkeitsBeschreibung("Gymnasiale Maturität"),
                new LebenslaufItem()
                    .setTaetigkeitsart(Taetigkeitsart.ERWERBSTAETIGKEIT)
                    .setVon(LocalDate.of(2008, 8, 1))
                    .setBis(LocalDate.of(2013, 6, 30))
                    .setTaetigkeitsBeschreibung("Diverse Jobs"),
                new LebenslaufItem()
                    .setTaetigkeitsart(Taetigkeitsart.ERWERBSTAETIGKEIT)
                    .setVon(LocalDate.of(2013, 7, 1))
                    .setBis(LocalDate.of(2019, 8, 31))
                    .setTaetigkeitsBeschreibung("Diverse Jobs"),
                new LebenslaufItem()
                    .setTaetigkeitsart(Taetigkeitsart.ANDERE_TAETIGKEIT)
                    .setVon(LocalDate.of(2019, 8, 1))
                    .setBis(LocalDate.of(2023, 8, 31))
                    .setTaetigkeitsBeschreibung("Bachelor (Hochschule/UNI)"),
                new LebenslaufItem()
                    .setTaetigkeitsart(Taetigkeitsart.ANDERE_TAETIGKEIT)
                    .setVon(LocalDate.of(2023, 9, 1))
                    .setBis(LocalDate.of(2026, 8, 31))
                    .setTaetigkeitsBeschreibung("Fachhochschule Nordwestschweiz: Bachelor")
            )
        );

        gesuchFormular.setElterns(
            Set.of(
                ((Eltern) new Eltern()
                    .setWohnkosten(0)
                    .setGeburtsdatum(LocalDate.of(1963, 8, 1)))
                        .setElternTyp(ElternTyp.VATER),
                ((Eltern) new Eltern()
                    .setWohnkosten(0)
                    .setGeburtsdatum(LocalDate.of(1963, 6, 1)))
                        .setElternTyp(ElternTyp.MUTTER)
            )
        );

        gesuchFormular.setSteuerdaten(
            Set.of(
                new Steuerdaten().setSteuerdatenTyp(SteuerdatenTyp.MUTTER)
                    .setTotalEinkuenfte(15201)
                    .setIsArbeitsverhaeltnisSelbstaendig(false)
                    .setVerpflegung(0)
                    .setVerpflegungPartner(0)
                    .setFahrkosten(0)
                    .setFahrkostenPartner(0)
                    .setSteuernBund(0)
                    .setSteuernKantonGemeinde(0)
                    .setEigenmietwert(0),
                new Steuerdaten().setSteuerdatenTyp(SteuerdatenTyp.VATER)
                    .setIsArbeitsverhaeltnisSelbstaendig(false)
                    .setVerpflegung(0)
                    .setVerpflegungPartner(0)
                    .setFahrkosten(0)
                    .setFahrkostenPartner(0)
                    .setSteuernBund(0)
                    .setSteuernKantonGemeinde(0)
                    .setEigenmietwert(0)
            )
        );

        gesuchFormular.setGeschwisters(
            Set.of()
        );

        Kind kind1 = (Kind) new Kind()
            .setNachname("Testfall6")
            .setVorname("Kind1")
            .setGeburtsdatum(LocalDate.of(2008, 8, 1));
        kind1.setWohnsitzAnteilPia(0);
        kind1.setAusbildungssituation(Ausbildungssituation.VORSCHULPFLICHTIG);

        gesuchFormular.setKinds(
            Set.of(
                kind1
            )
        );

        // Act
        final var berechnungsresultatDto = berechnungService.getBerechnungsresultatFromGesuchTranche(
            gesuch.getNewestGesuchTranche().orElseThrow(NotFoundException::new),
            1,
            0
        );

        // Assert
        assertThat(berechnungsresultatDto.size(), is(1));
        assertThat(berechnungsresultatDto.get(0).getBerechnung(), is(equalTo(-27179)));
    }
}
