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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.ausbildung.entity.Abschluss;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsstaette;
import ch.dvbern.stip.api.ausbildung.type.Bildungskategorie;
import ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung;
import ch.dvbern.stip.api.common.type.Anrede;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.generator.depricated.entities.service.LandGenerator;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.oneOf;

@QuarkusTest
@QuarkusTestResource(TestDatabaseEnvironment.class)
@Slf4j
class BerechnungServiceTest {

    @Inject
    BerechnungService berechnungService;

    @Test
    void wasEingereichtAfterDueDateFalseTest() {
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());
        final var now = LocalDate.now();

        gesuch.getGesuchsperiode().setEinreichefristNormal(now);
        gesuch.setEinreichedatum(now);

        final var wasEingereichtAfterDueDate = DateUtil.wasEingereichtAfterDueDate(gesuch);
        assertThat(wasEingereichtAfterDueDate, is(false));
    }

    @Test
    void wasEingereichtAfterDueDateTrueTest() {
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());
        gesuch.getGesuchsperiode().setEinreichefristNormal(LocalDate.now());

        final var wasEingereichtAfterDueDate = DateUtil.wasEingereichtAfterDueDate(gesuch);
        assertThat(wasEingereichtAfterDueDate, is(true));
    }

    @Test
    void getActualDurationRoundDownTest() {
        final var monthsToBeBetween = 7;
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());
        final var currentGesuchTranche = gesuch.getGesuchTranchen().get(0).setTyp(GesuchTrancheTyp.TRANCHE);
        currentGesuchTranche
            .setGueltigkeit(
                new DateRange().setGueltigBis(LocalDate.now().plusMonths(monthsToBeBetween - 2).withDayOfMonth(1))
            );
        gesuch.setGesuchTranchen(
            List.of(
                currentGesuchTranche,
                (GesuchTranche) new GesuchTranche()
                    .setGesuchFormular(
                        new GesuchFormular()
                            .setPersonInAusbildung(
                                new PersonInAusbildung()
                            )
                    )
                    .setTyp(GesuchTrancheTyp.TRANCHE)
                    .setGueltigkeit(
                        new DateRange().setGueltigBis(LocalDate.now().plusMonths(monthsToBeBetween).withDayOfMonth(1))
                    )
                    .setId(UUID.randomUUID()),
                (GesuchTranche) new GesuchTranche()
                    .setGesuchFormular(
                        new GesuchFormular()
                            .setPersonInAusbildung(
                                new PersonInAusbildung()
                            )
                    )
                    .setTyp(GesuchTrancheTyp.AENDERUNG)
                    .setGueltigkeit(
                        new DateRange()
                            .setGueltigBis(LocalDate.now().plusMonths(monthsToBeBetween - 4).withDayOfMonth(1))
                    )
                    .setId(UUID.randomUUID()),
                (GesuchTranche) new GesuchTranche()
                    .setGesuchFormular(
                        new GesuchFormular()
                            .setPersonInAusbildung(
                                new PersonInAusbildung()
                            )
                    )
                    .setTyp(GesuchTrancheTyp.AENDERUNG)
                    .setGueltigkeit(
                        new DateRange()
                            .setGueltigBis(LocalDate.now().plusMonths(monthsToBeBetween + 2).withDayOfMonth(1))
                    )
                    .setId(UUID.randomUUID())
            )
        );
        var eingereicht = LocalDate.now().withDayOfMonth(15);
        gesuch.setEinreichedatum(eingereicht);

        var monthsBetween = DateUtil.getStipendiumDurationRoundDown(gesuch);
        assertThat(monthsBetween, equalTo(monthsToBeBetween));

        eingereicht = LocalDate.now().withDayOfMonth(16);
        gesuch.setEinreichedatum(eingereicht);

        monthsBetween = DateUtil.getStipendiumDurationRoundDown(gesuch);
        assertThat(monthsBetween, equalTo(monthsToBeBetween - 1));
    }

    @Test
    void getActualDurationRoundUpTest() {
        final var monthsToBeBetween = 7;
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());
        final var currentGesuchTranche = gesuch.getGesuchTranchen().get(0).setTyp(GesuchTrancheTyp.TRANCHE);
        currentGesuchTranche
            .setGueltigkeit(
                new DateRange().setGueltigBis(LocalDate.now().plusMonths(monthsToBeBetween - 2).withDayOfMonth(1))
            );
        gesuch.setGesuchTranchen(
            List.of(
                currentGesuchTranche,
                (GesuchTranche) new GesuchTranche()
                    .setGesuchFormular(
                        new GesuchFormular()
                            .setPersonInAusbildung(
                                new PersonInAusbildung()
                            )
                    )
                    .setTyp(GesuchTrancheTyp.TRANCHE)
                    .setGueltigkeit(
                        new DateRange().setGueltigBis(LocalDate.now().plusMonths(monthsToBeBetween).withDayOfMonth(1))
                    )
                    .setId(UUID.randomUUID()),
                (GesuchTranche) new GesuchTranche()
                    .setGesuchFormular(
                        new GesuchFormular()
                            .setPersonInAusbildung(
                                new PersonInAusbildung()
                            )
                    )
                    .setTyp(GesuchTrancheTyp.AENDERUNG)
                    .setGueltigkeit(
                        new DateRange()
                            .setGueltigBis(LocalDate.now().plusMonths(monthsToBeBetween - 4).withDayOfMonth(1))
                    )
                    .setId(UUID.randomUUID()),
                (GesuchTranche) new GesuchTranche()
                    .setGesuchFormular(
                        new GesuchFormular()
                            .setPersonInAusbildung(
                                new PersonInAusbildung()
                            )
                    )
                    .setTyp(GesuchTrancheTyp.AENDERUNG)
                    .setGueltigkeit(
                        new DateRange()
                            .setGueltigBis(LocalDate.now().plusMonths(monthsToBeBetween + 2).withDayOfMonth(1))
                    )
                    .setId(UUID.randomUUID())
            )
        );
        final var eingereicht = LocalDate.now().withDayOfMonth(27);
        gesuch.setEinreichedatum(eingereicht);

        final var monthsBetween = DateUtil.getStipendiumDurationRoundDown(gesuch);
        assertThat(monthsBetween, equalTo(monthsToBeBetween - 1));
    }

    @Test
    void testSubtractionOfEinkommensFreibetrag() {
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());
        final var gueltigkeit = DateRange.getFruehlingOrHerbst(LocalDate.now());

        gesuch.setAusbildung(
            new Ausbildung()
                .setAusbildungsgang(
                    new Ausbildungsgang()
                )
                .setAusbildungBegin(gueltigkeit.getGueltigAb())
                .setAusbildungEnd(gueltigkeit.getGueltigBis())
        );
        gesuch.getAusbildung()
            .getAusbildungsgang()
            .setAbschluss(
                new Abschluss().setBfsKategorie(9)
                    .setBildungskategorie(
                        Bildungskategorie.SEKUNDARSTUFE_II
                    )
                    .setBildungsrichtung(Bildungsrichtung.HOCHSCHULE)
                    .setAusbildungsgaenge(List.of(gesuch.getAusbildung().getAusbildungsgang()))
            );
        gesuch.getAusbildung()
            .getAusbildungsgang()
            .setAusbildungsstaette(
                new Ausbildungsstaette().setAusbildungsgaenge(List.of(gesuch.getAusbildung().getAusbildungsgang()))
            );

        final var gesuchFormular = gesuch.getNewestGesuchTranche().get().getGesuchFormular();
        gesuchFormular.getPersonInAusbildung()
            .setNationalitaet(LandGenerator.initSwitzerland())
            .setZivilstand(Zivilstand.LEDIG)
            .setSozialversicherungsnummer(TestConstants.AHV_NUMMER_VALID_PERSON_IN_AUSBILDUNG)
            .setAdresse(
                new Adresse().setPlz("1321")
                    .setLand(LandGenerator.initSwitzerland())
                    .setStrasse("asd")
                    .setHausnummer("1")
                    .setOrt("asd")
            )
            .setHeimatort("Bern")
            .setHeimatortPLZ("3006")
            .setAnrede(Anrede.HERR)
            .setTelefonnummer("0987654321")
            .setEmail("asd@asd.ch")
            .setKorrespondenzSprache(Sprache.DEUTSCH)
            .setWohnsitz(Wohnsitz.EIGENER_HAUSHALT)
            .setNachname("a")
            .setVorname("a")
            .setGeburtsdatum(LocalDate.now().minusYears(18).minusDays(1));

        gesuchFormular.setEinnahmenKosten(
            new EinnahmenKosten()
                .setNettoerwerbseinkommen(5000)
                .setErgaenzungsleistungen(0)
                .setWohnkosten(1000)
                .setAusbildungskosten(3000)
                .setFahrkosten(1000)
                .setZulagen(0)
                .setBetreuungskostenKinder(0)
                .setSteuerjahr(2000)
                .setRenten(0)
                .setVermoegen(null)
                .setArbeitspensumProzent(100)
        );

        gesuchFormular.setFamiliensituation(
            new Familiensituation()
                .setElternVerheiratetZusammen(false)
                .setGerichtlicheAlimentenregelung(false)
                .setElternteilUnbekanntVerstorben(true)
                .setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.VERSTORBEN)
                .setVaterUnbekanntVerstorben(ElternAbwesenheitsGrund.VERSTORBEN)
        );

        var sekResult = berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0);

        gesuch.getAusbildung()
            .getAusbildungsgang()
            .getAbschluss()
            .setBildungskategorie(Bildungskategorie.TERTIAERSTUFE_B);

        var terResult = berechnungService.getBerechnungsresultatFromGesuch(gesuch, 1, 0);

        assertThat(sekResult.getBerechnungStipendium(), equalTo(terResult.getBerechnungStipendium() - 6000));
    }

    @Test
    void testGetMonateMitDarlehen() {
        final var gesuch = TestUtil.getGesuchForBerechnung(UUID.randomUUID());

        var monateMitDarlehen = berechnungService.getMonateMitDarlehen(gesuch);
        assertThat(monateMitDarlehen, equalTo(0));

        gesuch.getAusbildung().setAusbildungBegin(LocalDate.now().minusYears(4));
        monateMitDarlehen = berechnungService.getMonateMitDarlehen(gesuch);
        assertThat(monateMitDarlehen, equalTo(12));

        gesuch.getAusbildung().setAusbildungBegin(LocalDate.now().minusYears(1));
        gesuch.getGesuchTranchen()
            .get(0)
            .getGesuchFormular()
            .getLebenslaufItems()
            .add(
                new LebenslaufItem()
                    .setVon(LocalDate.now().minusYears(3))
                    .setBis(LocalDate.now().minusYears(1))
                    .setAbschluss(new Abschluss().setBildungskategorie(Bildungskategorie.TERTIAERSTUFE_B))
            );
        monateMitDarlehen = berechnungService.getMonateMitDarlehen(gesuch);
        assertThat(monateMitDarlehen, oneOf(6, 7));
    }

}
