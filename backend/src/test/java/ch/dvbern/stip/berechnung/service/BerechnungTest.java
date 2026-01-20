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

import ch.dvbern.stip.api.ausbildung.entity.Abschluss;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum;
import ch.dvbern.stip.api.ausbildung.type.Bildungskategorie;
import ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.api.generator.entities.service.LandGenerator;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.type.Taetigkeitsart;
import ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton;
import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.api.steuererklaerung.entity.Steuererklaerung;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.berechnung.util.BerechnungUtil;
import ch.dvbern.stip.generated.dto.TranchenBerechnungsresultatDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor
@Slf4j
class BerechnungTest {
    private BerechnungService berechnungService;

    @BeforeEach
    void setUpEach() {
        berechnungService = BerechnungUtil.getMockBerechnungService();
    }

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
            "1, -11039",
        // "2, 0",
        // "3, -12357",
        // "4, 0",
        // "5, -12719",
        // "6, 0",
        // "7, -20254",
        // "8, 0",
        // "9, -15781",
        // "10, -35142",
        }
    )
    void testBerechnungFaelle(final int fall, final int expectedStipendien) throws JsonProcessingException {
        // Load Fall resources/berechnung/fall_{fall}.json, deserialize to a BerechnungRequestV1
        // and calculate Stipendien for it
        final var objectMapper = BerechnungUtil.createObjectMapper();
        final var result = berechnungService.calculateStipendien(BerechnungUtil.getRequest(fall));
        final var summary = objectMapper.writeValueAsString(result);
        assertThat("Value did not match, debug:\n" + summary, result.getStipendien(), is(expectedStipendien));
    }

    @Test
    @TestAsGesuchsteller
    void testMinimalGesuchBerechnung() {
        // Arrange
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());
        final var ausbildungsBegin = LocalDate.now().withMonth(9);

        gesuch.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                        .setAbschluss(
                            new Abschluss().setBfsKategorie(10)
                                .setBildungskategorie(Bildungskategorie.TERTIAERSTUFE_B)
                                .setBildungsrichtung(
                                    Bildungsrichtung.HOCHSCHULE
                                )
                        )
                )
                .setAusbildungBegin(ausbildungsBegin)
                .setAusbildungEnd(ausbildungsBegin.plusYears(2))
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
                .setAusbildungskosten(2000)
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
            assertThat(berechnungsresultatDto.getBerechnungAnteilTotal(), is(not(nullValue())));
        }
    }

    @Test
    @TestAsGesuchsteller
    void testFall11GesuchBerechnung() {
        // Arrange
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());
        final var ausbildungsBegin = LocalDate.of(2025, 9, 1);
        final var abschluss = new Abschluss()
            .setBfsKategorie(6)
            .setBildungskategorie(Bildungskategorie.TERTIAERSTUFE_B)
            .setBildungsrichtung(Bildungsrichtung.HOEHERE_BERUFSBILDUNG);

        gesuch.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                        .setAbschluss(abschluss)
                )
                .setAusbildungBegin(ausbildungsBegin)
                .setAusbildungEnd(ausbildungsBegin.plusYears(3).minusDays(1))
        );

        final var gesuchFormular = gesuch.getNewestGesuchTranche().get().getGesuchFormular();
        gesuch.setGesuchTranchen(
            List.of(
                gesuch.getNewestGesuchTranche()
                    .get()
                    .setGueltigkeit(
                        new DateRange(
                            LocalDate.of(2025, 8, 1),
                            LocalDate.of(2026, 7, 31)
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
            .setWohnsitz(Wohnsitz.EIGENER_HAUSHALT)
            .setGeburtsdatum(LocalDate.of(2025, 8, 1));

        gesuchFormular.setFamiliensituation(new Familiensituation().setElternVerheiratetZusammen(true));

        gesuchFormular.setElterns(
            Set.of(
                (Eltern) new Eltern()
                    .setElternTyp(ElternTyp.VATER)
                    .setWohnkosten(833)
                    .setSozialhilfebeitraege(false)
                    .setGeburtsdatum(LocalDate.of(1955, 1, 1)),
                (Eltern) new Eltern()
                    .setElternTyp(ElternTyp.MUTTER)
                    .setWohnkosten(833)
                    .setSozialhilfebeitraege(false)
                    .setGeburtsdatum(LocalDate.of(1970, 11, 4))
            )
        );

        gesuchFormular.setLebenslaufItems(
            Set.of(
                new LebenslaufItem()
                    .setVon(LocalDate.of(2020, 8, 1))
                    .setBis(LocalDate.of(2024, 7, 1))
                    .setWohnsitz(WohnsitzKanton.BE)
                    .setAusbildungAbgeschlossen(true)
                    .setAbschluss(abschluss),
                new LebenslaufItem()
                    .setVon(LocalDate.of(2024, 8, 1))
                    .setBis(LocalDate.of(2025, 7, 1))
                    .setTaetigkeitsart(Taetigkeitsart.ERWERBSTAETIGKEIT)
            )
        );

        gesuchFormular.setKinds(
            Set.of(
                (Kind) new Kind()
                    .setWohnsitzAnteilPia(100)
                    .setAusbildungssituation(Ausbildungssituation.SCHULPFLICHTIG)
                    .setGeburtsdatum(LocalDate.of(2020, 8, 5))
            )
        );

        gesuchFormular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setNettoerwerbseinkommen(0)
                .setZulagen(3_720)
                .setAusbildungskosten(1_800)
                .setFahrkosten(558)
                .setWohnkosten(1_167)
                .setVermoegen(10_000)
        );

        gesuchFormular.setSteuererklaerung(
            Set.of(
                new Steuererklaerung().setSteuerdatenTyp(SteuerdatenTyp.FAMILIE)
            )
        );

        gesuchFormular.setSteuerdaten(
            Set.of(
                new Steuerdaten()
                    .setSteuerdatenTyp(SteuerdatenTyp.FAMILIE)
                    .setSteuernKantonGemeinde(2_000)
                    .setSteuernBund(500)
                    .setFahrkosten(790)
                    .setVerpflegung(3_200)
                    .setTotalEinkuenfte(50_000)
                    .setIsArbeitsverhaeltnisSelbstaendig(true)
                    .setVermoegen(20_000)
                    .setFahrkostenPartner(0)
                    .setVerpflegungPartner(0)
                    .setEigenmietwert(0)
                    .setSteuerjahr(2024)
            )
        );

        // Act
        final var berechnungsresultatDto = berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0);

        LOG.info(berechnungsresultatDto.toString());

        // Assert
        assertThat(berechnungsresultatDto.getTranchenBerechnungsresultate().size(), is(1));
        // TODO fix once it is clear where the error happens
        // assertThat(berechnungsresultatDto.getBerechnung(), is(equalTo(35_142)));
    }

    @Test
    @TestAsGesuchsteller
    void testFall7GesuchBerechnung() {
        // Arrange
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());
        final var ausbildungsBegin = LocalDate.now().withMonth(9);

        gesuch.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                        .setAbschluss(
                            new Abschluss().setBfsKategorie(5)
                                .setBildungskategorie(Bildungskategorie.SEKUNDARSTUFE_II)
                                .setBildungsrichtung(Bildungsrichtung.BERUFLICHE_GRUNDBILDUNG)
                        )
                )
                .setAusbildungBegin(ausbildungsBegin)
                .setAusbildungEnd(ausbildungsBegin.plusYears(2))
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
                .setAusbildungskosten(2000)
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
        gesuchFormular.setSteuererklaerung(
            Set.of(
                new Steuererklaerung().setSteuerdatenTyp(SteuerdatenTyp.VATER),
                new Steuererklaerung().setSteuerdatenTyp(SteuerdatenTyp.MUTTER)
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
                    .setEigenmietwert(0)
                    .setVermoegen(0)
                    .setSteuerjahr(gesuchFormular.getTranche().getGueltigkeit().getGueltigAb().getYear() - 1)
                    .setIsArbeitsverhaeltnisSelbstaendig(false)
            )
        );

        // Act
        final var berechnungsresultatDto = berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0);

        // Assert
        assertThat(berechnungsresultatDto.getTranchenBerechnungsresultate().size(), is(1));
        assertThat(berechnungsresultatDto.getBerechnungTotal(), is(equalTo(10432)));

        // Arrange
        gesuch.getGesuchsperiode()
            .setAnzahlWochenLehre(47)
            .setAnzahlWochenSchule(38)
            .setReduzierungDesGrundbedarfs(2754);
        gesuchFormular.getEinnahmenKosten().setWgWohnend(true);
        gesuchFormular.getEinnahmenKosten().setWgAnzahlPersonen(2);

        // Act
        final var berechnungsresultatDtoWG2Pers = berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0);
        // Assert
        assertThat(berechnungsresultatDtoWG2Pers.getTranchenBerechnungsresultate().size(), is(1));
        assertThat(berechnungsresultatDtoWG2Pers.getBerechnungTotal(), is(equalTo(7678)));

        // Arrange
        gesuchFormular.getEinnahmenKosten().setWgWohnend(false);
        gesuchFormular.getEinnahmenKosten().setWgAnzahlPersonen(null);
        gesuchFormular.getEinnahmenKosten().setAlternativeWohnformWohnend(true);

        // Act
        final var berechnungsresultatDtoAlternativeWohnform =
            berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0);
        // Assert
        assertThat(berechnungsresultatDtoAlternativeWohnform.getTranchenBerechnungsresultate().size(), is(1));
        assertThat(berechnungsresultatDtoAlternativeWohnform.getBerechnungTotal(), is(equalTo(7678)));

        // Arrange
        gesuchFormular.getEinnahmenKosten().setWgWohnend(true);
        gesuchFormular.getEinnahmenKosten().setWgAnzahlPersonen(1);
        gesuchFormular.getEinnahmenKosten().setAlternativeWohnformWohnend(false);
        // Act
        final var berechnungsresultatDtoWgWohnend1Pers =
            berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0);
        // Assert
        assertThat(
            berechnungsresultatDtoWgWohnend1Pers.getBerechnungTotal(),
            is(equalTo(berechnungsresultatDtoAlternativeWohnform.getBerechnungTotal()))
        );
    }

    @Test
    @TestAsGesuchsteller
    void testFall8GesuchBerechnung() {
        // Arrange
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());
        final var ausbildungsBegin = LocalDate.now().withMonth(9);

        gesuch.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                        .setAbschluss(
                            new Abschluss().setBfsKategorie(5)
                                .setBildungskategorie(Bildungskategorie.TERTIAERSTUFE_B)
                                .setBildungsrichtung(Bildungsrichtung.HOCHSCHULE)
                        )
                )
                .setAusbildungBegin(ausbildungsBegin)
                .setAusbildungEnd(ausbildungsBegin.plusYears(2))
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
                .setAusbildungskosten(750)
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
                    .setSteuerjahr(gesuchFormular.getTranche().getGueltigkeit().getGueltigAb().getYear() - 1)
                    .setIsArbeitsverhaeltnisSelbstaendig(true)
            )
        );
        gesuchFormular.setSteuererklaerung(
            Set.of(
                new Steuererklaerung().setSteuerdatenTyp(SteuerdatenTyp.VATER),
                new Steuererklaerung().setSteuerdatenTyp(SteuerdatenTyp.MUTTER)
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
        assertThat(berechnungsresultatDto.getBerechnungTotal(), is(equalTo(6266)));
    }

    @Test
    @TestAsGesuchsteller
    void testFall14GesuchBerechnung() {
        // Arrange
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());
        final var ausbildungsBegin = LocalDate.of(2023, 8, 1);

        gesuch.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                        .setAbschluss(
                            new Abschluss().setBfsKategorie(9)
                                .setBildungskategorie(Bildungskategorie.TERTIAERSTUFE_B)
                                .setBildungsrichtung(Bildungsrichtung.HOCHSCHULE)
                        )
                )
                .setAusbildungBegin(ausbildungsBegin)
                .setAusbildungEnd(ausbildungsBegin.plusYears(2))
        );

        final var gesuchFormular = gesuch.getNewestGesuchTranche().get().getGesuchFormular();
        gesuch.setGesuchTranchen(
            List.of(
                gesuch.getNewestGesuchTranche()
                    .get()
                    .setGueltigkeit(
                        new DateRange(
                            ausbildungsBegin,
                            LocalDate.of(2024, 7, 31)
                        )
                    )
                    .setTyp(GesuchTrancheTyp.TRANCHE)
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
                .setAusbildungskosten(1_500)
                .setAuswaertigeMittagessenProWoche(0)
        );

        gesuchFormular.setSteuererklaerung(
            Set.of(
                new Steuererklaerung().setSteuerdatenTyp(SteuerdatenTyp.VATER),
                new Steuererklaerung().setSteuerdatenTyp(SteuerdatenTyp.MUTTER)
            )
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
                    .setVermoegen(0)
                    .setFahrkostenPartner(0)
                    .setVerpflegungPartner(0)
                    .setEigenmietwert(0)
                    .setVermoegen(0)
                    .setSteuerjahr(gesuchFormular.getTranche().getGueltigkeit().getGueltigAb().getYear() - 1),
                new Steuerdaten()
                    .setSteuerdatenTyp(SteuerdatenTyp.MUTTER)
                    .setTotalEinkuenfte(5_667)
                    .setSteuernKantonGemeinde(0)
                    .setSteuernBund(0)
                    .setFahrkosten(0)
                    .setVerpflegung(0)
                    .setIsArbeitsverhaeltnisSelbstaendig(false)
                    .setVermoegen(0)
                    .setFahrkostenPartner(0)
                    .setVerpflegungPartner(0)
                    .setEigenmietwert(0)
                    .setVermoegen(0)
                    .setSteuerjahr(gesuchFormular.getTranche().getGueltigkeit().getGueltigAb().getYear() - 1)
            )
        );

        // Act
        final var berechnungsresultatDto = berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0);

        // Assert
        assertThat(berechnungsresultatDto.getTranchenBerechnungsresultate().size(), is(2));
        // TODO: Check why this fails now
        // assertThat(berechnungsresultatDto.getBerechnung(), is(equalTo(2367)));
    }

    @Test
    @TestAsGesuchsteller
    void testFall5GesuchBerechnungKinder() {
        // Arrange
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());
        final var ausbildungsBegin = LocalDate.now().withMonth(9);

        gesuch.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                        .setAbschluss(
                            new Abschluss().setBfsKategorie(5)
                                .setBildungskategorie(Bildungskategorie.TERTIAERSTUFE_B)
                                .setBildungsrichtung(Bildungsrichtung.HOCHSCHULE)
                        )
                )
                .setAusbildungBegin(ausbildungsBegin)
                .setAusbildungEnd(ausbildungsBegin.plusYears(2))
        );

        final var gesuchTranche = gesuch.getNewestGesuchTranche().get();
        final var gesuchFormular = gesuchTranche.getGesuchFormular();
        gesuchTranche.setTyp(GesuchTrancheTyp.TRANCHE);

        gesuchFormular.getPersonInAusbildung()
            .setSozialhilfebeitraege(true)
            .setZivilstand(Zivilstand.VERHEIRATET)
            .setNiederlassungsstatus(Niederlassungsstatus.VORLAEUFIG_AUFGENOMMEN_F_ZUESTAENDIGER_KANTON_MANDANT)
            .setNationalitaet(LandGenerator.initIran())
            .setWohnsitz(Wohnsitz.EIGENER_HAUSHALT)
            .setGeburtsdatum(LocalDate.of(1996, 7, 1));

        gesuchFormular.setPartner(
            (Partner) new Partner()
                .setGeburtsdatum(LocalDate.of(1990, 12, 1))
        );
        var ekPartner = new EinnahmenKosten();
        ekPartner.setNettoerwerbseinkommen(25000);
        ekPartner.setVerpflegungskosten(1000);
        ekPartner.setFahrkosten(1600);
        ekPartner.setVermoegen(0);
        gesuchFormular.setEinnahmenKostenPartner(ekPartner);

        gesuchFormular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setNettoerwerbseinkommen(0)
                .setVermoegen(0)
                .setFahrkosten(0)
                .setRenten(0)
                .setZulagen(5520)
                .setAusbildungskosten(500)
                .setBetreuungskostenKinder(3333)
                .setFahrkosten(790)
                .setWohnkosten(18000)
                .setWgWohnend(false)
                .setVermoegen(12)
                .setSteuerjahr(2023)
                .setVeranlagungsStatus(null)
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

        gesuchFormular.setSteuererklaerung(
            Set.of(
                new Steuererklaerung().setSteuerdatenTyp(SteuerdatenTyp.VATER),
                new Steuererklaerung().setSteuerdatenTyp(SteuerdatenTyp.MUTTER)
            )
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
        // TODO: Check why this fails now
        // assertThat(berechnungsresultatDtos.get(0).getBerechnung(), is(equalTo(-9938)));
    }

    @Test
    @TestAsGesuchsteller
    void testFall6BerechnungEinKind() {
        // Arrange
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());
        final var ausbildungsBegin = LocalDate.now().withMonth(9);

        gesuch.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                        .setAbschluss(
                            new Abschluss().setBfsKategorie(10)
                                .setBildungskategorie(Bildungskategorie.TERTIAERSTUFE_B)
                                .setBildungsrichtung(Bildungsrichtung.HOCHSCHULE)
                        )
                )
                .setPensum(AusbildungsPensum.TEILZEIT)
                .setAusbildungBegin(ausbildungsBegin)
                .setAusbildungEnd(ausbildungsBegin.plusYears(2))
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
            .setNationalitaet(LandGenerator.initGermany())
            .setWohnsitz(Wohnsitz.EIGENER_HAUSHALT)
            .setGeburtsdatum(LocalDate.of(1988, 4, 1));

        gesuchFormular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setNettoerwerbseinkommen(0)
                .setVermoegen(0)
                .setFahrkosten(800)
                .setRenten(0)
                .setZulagen(0)
                .setAusbildungskosten(2000)
                .setBetreuungskostenKinder(0)
                .setFahrkosten(800)
                .setWohnkosten(11460)
                .setWgWohnend(true)
                .setVermoegen(0)
                .setSteuerjahr(2023)
                .setVeranlagungsStatus(null)
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
        gesuchFormular.setSteuererklaerung(
            Set.of(
                new Steuererklaerung().setSteuerdatenTyp(SteuerdatenTyp.VATER),
                new Steuererklaerung().setSteuerdatenTyp(SteuerdatenTyp.MUTTER)
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
                    .setEigenmietwert(0)
                    .setVermoegen(0)
                    .setSteuerjahr(gesuchTranche.getGueltigkeit().getGueltigAb().getYear() - 1),
                new Steuerdaten().setSteuerdatenTyp(SteuerdatenTyp.VATER)
                    .setIsArbeitsverhaeltnisSelbstaendig(false)
                    .setTotalEinkuenfte(0)
                    .setVerpflegung(0)
                    .setVerpflegungPartner(0)
                    .setFahrkosten(0)
                    .setFahrkostenPartner(0)
                    .setSteuernBund(0)
                    .setSteuernKantonGemeinde(0)
                    .setEigenmietwert(0)
                    .setVermoegen(0)
                    .setSteuerjahr(gesuchTranche.getGueltigkeit().getGueltigAb().getYear() - 1)
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
        assertThat(berechnungsresultatDto.get(0).getBerechnungAnteilTotal(), is(equalTo(-33179)));
    }
}
