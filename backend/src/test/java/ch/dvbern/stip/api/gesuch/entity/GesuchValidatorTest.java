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

package ch.dvbern.stip.api.gesuch.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.bildungskategorie.entity.Bildungskategorie;
import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.darlehen.entity.Darlehen;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.api.generator.entities.service.LandGenerator;
import ch.dvbern.stip.api.geschwister.entity.Geschwister;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchformular.validation.GesuchEinreichenValidationGroup;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.land.entity.Land;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt;
import ch.dvbern.stip.api.lebenslauf.type.Taetigkeitsart;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.entity.ZustaendigeKESB;
import ch.dvbern.stip.api.personinausbildung.entity.ZustaendigerKanton;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_AHV_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_ALTERNATIVE_AUSBILDUNG_FIELD_REQUIRED_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_ALTERNATIVE_AUSBILDUNG_FIELD_REQUIRED_NULL_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_AUSBILDUNG_BESUCHT_BMS_VALID;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_AUSBILDUNG_FIELD_REQUIRED_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_AUSBILDUNG_FIELD_REQUIRED_NULL_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_DARLEHEN_NOT_VALID_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_DARLEHEN_REQUIRED_VOLLJAEHRIG_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_EINNAHMEN_KOSTEN_ALIMENTE_REQUIRED_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_EINNAHMEN_KOSTEN_ZULAGEN_REQUIRED_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_FAMILIENSITUATION_ELTERN_ENTITY_REQUIRED_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_HEIMATORT_FIELD_REQUIRED_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_HEIMATORT_FIELD_REQUIRED_NULL_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_IZW_FIELD_REQUIRED_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_IZW_FIELD_REQUIRED_NULL_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_LEBENSLAUFITEM_ART_FIELD_REQUIRED_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_LEBENSLAUFITEM_ART_FIELD_REQUIRED_NULL_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_LEBENSLAUF_LUCKENLOS_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_NACHNAME_NOTBLANK_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_NIEDERLASSUNGSSTATUS_FIELD_REQUIRED_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_NIEDERLASSUNGSSTATUS_FIELD_REQUIRED_NULL_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_OBHUT_GEMEINSAM_BERECHNUNG_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_VORNAME_NOTBLANK_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_WER_ZAHLT_ALIMENTE_FIELD_REQUIRED_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_WER_ZAHLT_ALIMENTE_FIELD_REQUIRED_NULL_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_WOHNSITZ_ANTEIL_BERECHNUNG_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_WOHNSITZ_ANTEIL_FIELD_REQUIRED_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_WOHNSITZ_ANTEIL_FIELD_REQUIRED_NULL_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_ZUSTAENDIGER_KANTON_FIELD_REQUIRED_NULL_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_ZUSTAENDIGE_KESB_FIELD_REQUIRED_NULL_MESSAGE;
import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GesuchValidatorTest {

    private final Validator validator;

    @Test
    void testFieldValidationErrorForPersonInAusbildung() {
        String[] constraintMessages = { VALIDATION_NACHNAME_NOTBLANK_MESSAGE, VALIDATION_VORNAME_NOTBLANK_MESSAGE,
            VALIDATION_IZW_FIELD_REQUIRED_MESSAGE, VALIDATION_HEIMATORT_FIELD_REQUIRED_MESSAGE,
            VALIDATION_WOHNSITZ_ANTEIL_FIELD_REQUIRED_MESSAGE, VALIDATION_AHV_MESSAGE,
            VALIDATION_NIEDERLASSUNGSSTATUS_FIELD_REQUIRED_NULL_MESSAGE };
        PersonInAusbildung personInAusbildung = new PersonInAusbildung();
        personInAusbildung.setAdresse(new Adresse());
        // Beim Land CH muss der Heimatort nicht leer sein
        personInAusbildung.getAdresse().setLand(LandGenerator.initSwitzerland());
        // Bei PLZ != 3xxx, muss das vermoegenVorjahr nicht leer sein
        personInAusbildung.getAdresse().setPlz("7000");
        // Beim nicht IZV muessen die IZV PLZ und Ort nicht leer sein
        personInAusbildung.setIdentischerZivilrechtlicherWohnsitz(false);
        // Beim Nationalitaet CH muesst die Niederlassungsstatus nicht gegeben werden
        personInAusbildung.setNationalitaet(LandGenerator.initSwitzerland());
        personInAusbildung.setNiederlassungsstatus(Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_C);
        // Beim Wohnsitz MUTTER_VATER muessen die Anteile Feldern nicht null sein
        personInAusbildung.setWohnsitz(Wohnsitz.MUTTER_VATER);
        Gesuch gesuch = prepareDummyGesuch();
        GesuchTranche gesuchTranche = gesuch.getGesuchTranchen().get(0);
        gesuchTranche.setId(UUID.randomUUID());
        gesuchTranche.getGesuchFormular().setPersonInAusbildung(personInAusbildung);
        gesuchTranche.getGesuchFormular().setTranche(gesuchTranche);
        assertAllMessagesPresent(constraintMessages, gesuch);

        // Die Anteil muessen wenn gegeben einen 100% Pensum im Total entsprechend, groessere oder kleiner Angaben
        // sind rejektiert
        gesuchTranche.getGesuchFormular().getPersonInAusbildung().setWohnsitzAnteilMutter(new BigDecimal("40.00"));
        gesuchTranche.getGesuchFormular().getPersonInAusbildung().setWohnsitzAnteilVater(new BigDecimal("50.00"));

        assertOneMessage(
            VALIDATION_WOHNSITZ_ANTEIL_BERECHNUNG_MESSAGE,
            gesuch,
            true
        );
        gesuchTranche.getGesuchFormular().getPersonInAusbildung().setWohnsitzAnteilVater(new BigDecimal("60.00"));
        assertOneMessage(
            VALIDATION_WOHNSITZ_ANTEIL_BERECHNUNG_MESSAGE,
            gesuch,
            false
        );
    }

    @Test
    void testNullFieldValidationErrorForPersonInAusbildung() {
        String[] constraintMessages =
            { VALIDATION_IZW_FIELD_REQUIRED_NULL_MESSAGE, VALIDATION_WOHNSITZ_ANTEIL_FIELD_REQUIRED_NULL_MESSAGE,
                VALIDATION_HEIMATORT_FIELD_REQUIRED_NULL_MESSAGE,
                VALIDATION_NIEDERLASSUNGSSTATUS_FIELD_REQUIRED_MESSAGE
            };
        PersonInAusbildung personInAusbildung = new PersonInAusbildung();
        // Wohnsitz Anteil muessen leer sein beim Wohnsitz != MUTTER_VATER
        personInAusbildung.setWohnsitz(Wohnsitz.FAMILIE);
        personInAusbildung.setWohnsitzAnteilVater(BigDecimal.ONE);
        // Beim IZV muessen die IZV Ort und PLZ leer sein
        personInAusbildung.setIdentischerZivilrechtlicherWohnsitz(true);
        personInAusbildung.setIdentischerZivilrechtlicherWohnsitzOrt("Test");
        // Beim Nationalitaet != CH duerfen keinen Heimatort erfasst werden
        personInAusbildung.setNationalitaet(LandGenerator.initGermany());
        personInAusbildung.setHeimatort("");

        Gesuch gesuch = prepareDummyGesuch();
        gesuch.getGesuchTranchen().get(0).getGesuchFormular().setPersonInAusbildung(personInAusbildung);
        assertAllMessagesPresent(constraintMessages, gesuch);
    }

    @ParameterizedTest
    @CsvSource(
        {
            // niederlassungsstatus | zustaendigerKanton
            "VORLAEUFIG_AUFGENOMMEN_F_ANDERER_ZUESTAENDIGER_KANTON,",
            "NIEDERLASSUNGSBEWILLIGUNG_C,BERN"
        }
    )
    void testNullFieldValidationErrorForPersonInAusbildungZustaendigerKantonRequired(
        Niederlassungsstatus niederlassungsstatus,
        ZustaendigerKanton zustaendigerKanton
    ) {
        String[] constraintMessages =
            { VALIDATION_IZW_FIELD_REQUIRED_NULL_MESSAGE, VALIDATION_WOHNSITZ_ANTEIL_FIELD_REQUIRED_NULL_MESSAGE,
                VALIDATION_HEIMATORT_FIELD_REQUIRED_NULL_MESSAGE,
                VALIDATION_ZUSTAENDIGER_KANTON_FIELD_REQUIRED_NULL_MESSAGE
            };
        PersonInAusbildung personInAusbildung = new PersonInAusbildung();
        // Wohnsitz Anteil muessen leer sein beim Wohnsitz != MUTTER_VATER
        personInAusbildung.setWohnsitz(Wohnsitz.FAMILIE);
        personInAusbildung.setWohnsitzAnteilVater(BigDecimal.ONE);
        // Beim IZV muessen die IZV Ort und PLZ leer sein
        personInAusbildung.setIdentischerZivilrechtlicherWohnsitz(true);
        personInAusbildung.setIdentischerZivilrechtlicherWohnsitzOrt("Test");
        // Beim Nationalitaet != CH duerfen keinen Heimatort erfasst werden
        personInAusbildung.setNationalitaet(LandGenerator.initGermany());
        personInAusbildung.setHeimatort("");
        // Bei Niederlassungsstatus == Fluechtling muss der ZustaendigerKanton angegeben werden
        personInAusbildung.setNiederlassungsstatus(niederlassungsstatus);
        personInAusbildung.setZustaendigerKanton(zustaendigerKanton);

        Gesuch gesuch = prepareDummyGesuch();
        gesuch.getGesuchTranchen().get(0).getGesuchFormular().setPersonInAusbildung(personInAusbildung);
        assertAllMessagesPresent(constraintMessages, gesuch);
    }

    @ParameterizedTest
    @CsvSource(
        value = {
            // vormundschaft | zustaendigeKESB
            "true,",
            "false, KESB_BERN"
        }
    )
    void testNullFieldValidationErrorForPersonInAusbildungZustaendigeKESB(
        boolean vormundschaft,
        ZustaendigeKESB zustaendigeKESB
    ) {
        PersonInAusbildung personInAusbildung = new PersonInAusbildung();
        // If vormundschaft is required then the zustaendigeKESB needs to be set and vice versa
        personInAusbildung.setVormundschaft(vormundschaft);
        personInAusbildung.setZustaendigeKESB(zustaendigeKESB);

        Gesuch gesuch = prepareDummyGesuch();
        gesuch.getGesuchTranchen().get(0).getGesuchFormular().setPersonInAusbildung(personInAusbildung);
        assertOneMessage(VALIDATION_ZUSTAENDIGE_KESB_FIELD_REQUIRED_NULL_MESSAGE, gesuch, true);
    }

    @Test
    void testNullFieldValidationErrorForAusbildung() {
        Gesuch gesuch = prepareDummyGesuch();

        gesuch.getAusbildung().setAusbildungBegin(LocalDate.now().with(firstDayOfMonth()));
        gesuch.getAusbildung().setAusbildungEnd(LocalDate.now().plusMonths(1).with(lastDayOfMonth()));

        GesuchTranche gesuchTranche = gesuch.getGesuchTranchen().get(0);
        Set<ConstraintViolation<Ausbildung>> violations = validator.validate(gesuch.getAusbildung());

        // Die Ausbildungsgang und Staette muessen bei keine alternative Ausbildung gegeben werden
        assertOneMessage(VALIDATION_AUSBILDUNG_FIELD_REQUIRED_MESSAGE, gesuch.getAusbildung(), true);
        assertOneMessage(VALIDATION_ALTERNATIVE_AUSBILDUNG_FIELD_REQUIRED_MESSAGE, gesuch.getAusbildung(), false);
        // Die alternative Ausbildungsgang und Staette muessen bei alternative Ausbildung gegeben werden
        gesuchTranche.getGesuchFormular().getAusbildung().setAusbildungNichtGefunden(true);
        assertOneMessage(VALIDATION_AUSBILDUNG_FIELD_REQUIRED_MESSAGE, gesuch.getAusbildung(), false);
        assertOneMessage(VALIDATION_ALTERNATIVE_AUSBILDUNG_FIELD_REQUIRED_MESSAGE, gesuch.getAusbildung(), true);
    }

    @Test
    void testFieldValidationErrorForAusbildung() {
        Gesuch gesuch = prepareDummyGesuch();
        gesuch.getAusbildung().setAusbildungNichtGefunden(false);
        gesuch.getAusbildung().setAlternativeAusbildungsgang("ausbildungsgang alt");
        gesuch.getAusbildung().setAusbildungBegin(LocalDate.now().with(firstDayOfMonth()));
        gesuch.getAusbildung().setAusbildungEnd(LocalDate.now().plusMonths(1).with(lastDayOfMonth()));
        // Die alternative Ausbildungsgang und Staette muessen bei keine alternative Ausbildung null sein
        assertOneMessage(VALIDATION_ALTERNATIVE_AUSBILDUNG_FIELD_REQUIRED_NULL_MESSAGE, gesuch.getAusbildung(), true);
        assertOneMessage(VALIDATION_AUSBILDUNG_FIELD_REQUIRED_NULL_MESSAGE, gesuch.getAusbildung(), false);

        // Die Ausbildungsgang und Staette muessen bei alternative Ausbildung null sein
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().getAusbildung().setAusbildungNichtGefunden(true);
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular()
            .getAusbildung()
            .setAusbildungsgang(new Ausbildungsgang());
        assertOneMessage(VALIDATION_AUSBILDUNG_FIELD_REQUIRED_NULL_MESSAGE, gesuch.getAusbildung(), true);
        assertOneMessage(VALIDATION_ALTERNATIVE_AUSBILDUNG_FIELD_REQUIRED_NULL_MESSAGE, gesuch.getAusbildung(), false);

        /*
         * besuchtBMS-Flag kann nur auf true gesetzt werden,
         * wenn die Ausbildungskategorie den Wert 4 oder 5 hat
         */
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular()
            .getAusbildung()
            .getAusbildungsgang()
            .setBildungskategorie(new Bildungskategorie());
        // Test Ausbildung Validation for BFS value = 4: both true/false valid
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular()
            .getAusbildung()
            .getAusbildungsgang()
            .getBildungskategorie()
            .setBfs(4);
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().getAusbildung().setBesuchtBMS(false);
        assertOneMessage(VALIDATION_AUSBILDUNG_BESUCHT_BMS_VALID, gesuch.getAusbildung(), false);
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().getAusbildung().setBesuchtBMS(true);
        assertOneMessage(VALIDATION_AUSBILDUNG_BESUCHT_BMS_VALID, gesuch.getAusbildung(), false);

        // Test Ausbildung Validation for BFS value = 5: both true/false valid
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular()
            .getAusbildung()
            .getAusbildungsgang()
            .getBildungskategorie()
            .setBfs(5);
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().getAusbildung().setBesuchtBMS(false);
        assertOneMessage(VALIDATION_AUSBILDUNG_BESUCHT_BMS_VALID, gesuch.getAusbildung(), false);
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().getAusbildung().setBesuchtBMS(true);
        assertOneMessage(VALIDATION_AUSBILDUNG_BESUCHT_BMS_VALID, gesuch.getAusbildung(), false);

        // Test Ausbildung Validation for BFS value = 0
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular()
            .getAusbildung()
            .getAusbildungsgang()
            .getBildungskategorie()
            .setBfs(0);
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().getAusbildung().setBesuchtBMS(true);
        assertOneMessage(VALIDATION_AUSBILDUNG_BESUCHT_BMS_VALID, gesuch.getAusbildung(), true);
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().getAusbildung().setBesuchtBMS(false);
        assertOneMessage(VALIDATION_AUSBILDUNG_BESUCHT_BMS_VALID, gesuch.getAusbildung(), false);
    }

    @Test
    void testFieldValidationErrorFamiliensituation() {
        String[] constraintMessages = {
            VALIDATION_WER_ZAHLT_ALIMENTE_FIELD_REQUIRED_MESSAGE
        };
        Familiensituation familiensituation = new Familiensituation();
        // beim Obhut gemeinsam muessen die Obhut Mutter und Vater Feldern nicht null sein
        // beim gerichtliche Alimentregelung muesst die Wer Zahlt Alimente ausgewaehlt werden
        familiensituation.setGerichtlicheAlimentenregelung(true);
        Gesuch gesuch = prepareDummyGesuch();
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setFamiliensituation(familiensituation);
        assertAllMessagesPresent(constraintMessages, gesuch);

        // korrekte Werten Meldung soll weg
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular()
            .getFamiliensituation();
        assertAllMessagesNotPresent(new String[] { VALIDATION_OBHUT_GEMEINSAM_BERECHNUNG_MESSAGE }, gesuch);
    }

    @Test
    void testNullFieldValidationErrorFamiliensituation() {
        String[] constraintMessages = {
            VALIDATION_WER_ZAHLT_ALIMENTE_FIELD_REQUIRED_NULL_MESSAGE
        };
        Familiensituation familiensituation = new Familiensituation();
        // beim Obhut != gemeinsam muessen die Obhut Mutter und Vater Feldern null sein
        // beim keine gerichtliche Alimentregelung muesst die Wer Zahlt Alimente nicht ausgewaehlt werden
        familiensituation.setGerichtlicheAlimentenregelung(false);
        familiensituation.setWerZahltAlimente(Elternschaftsteilung.GEMEINSAM);
        Gesuch gesuch = prepareDummyGesuch();
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setFamiliensituation(familiensituation);
        assertAllMessagesPresent(constraintMessages, gesuch);
    }

    @Test
    void testFieldValidationErrorEltern() {
        String[] constraintMessages = {
            VALIDATION_IZW_FIELD_REQUIRED_MESSAGE
        };
        Eltern eltern = new Eltern();
        eltern.setIdentischerZivilrechtlicherWohnsitz(false);
        Gesuch gesuch = prepareDummyGesuch();
        Set<Eltern> elternSet = new HashSet<>();
        elternSet.add(eltern);
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setElterns(elternSet);
        assertAllMessagesPresent(constraintMessages, gesuch);
    }

    @Test
    void testNullFieldValidationErrorEltern() {
        String[] constraintMessages = {
            VALIDATION_IZW_FIELD_REQUIRED_NULL_MESSAGE
        };
        Eltern eltern = new Eltern();
        eltern.setIdentischerZivilrechtlicherWohnsitz(true);
        eltern.setIdentischerZivilrechtlicherWohnsitzPLZ("1234");
        Gesuch gesuch = prepareDummyGesuch();
        Set<Eltern> elternSet = new HashSet<>();
        elternSet.add(eltern);
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setElterns(elternSet);
        assertAllMessagesPresent(constraintMessages, gesuch);
    }

    @Test
    void testFieldValidationErrorGeschwister() {
        Geschwister geschwister = new Geschwister();
        // beim Wohnsitz MUTTER_VATER muessen die Anteil Mutter und Vater Feldern nicht null sein
        geschwister.setWohnsitz(Wohnsitz.MUTTER_VATER);
        Set<Geschwister> geschwisterSet = new HashSet<>();
        geschwisterSet.add(geschwister);
        Gesuch gesuch = prepareDummyGesuch();
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setGeschwisters(geschwisterSet);

        assertAllMessagesPresent(new String[] { VALIDATION_WOHNSITZ_ANTEIL_FIELD_REQUIRED_MESSAGE }, gesuch);

        // Test die Wohnsitzanteil Berechnung:
        geschwister.setWohnsitzAnteilVater(new BigDecimal("55.00"));
        geschwister.setWohnsitzAnteilMutter(new BigDecimal("55.00"));
        geschwisterSet = new HashSet<>();
        geschwisterSet.add(geschwister);
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setGeschwisters(geschwisterSet);
        assertAllMessagesPresent(new String[] { VALIDATION_WOHNSITZ_ANTEIL_BERECHNUNG_MESSAGE }, gesuch);

        geschwister.setWohnsitzAnteilMutter(new BigDecimal("45.00"));
        geschwisterSet = new HashSet<>();
        geschwisterSet.add(geschwister);
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setGeschwisters(geschwisterSet);
        assertAllMessagesNotPresent(new String[] { VALIDATION_WOHNSITZ_ANTEIL_BERECHNUNG_MESSAGE }, gesuch);
    }

    @Test
    void testNullFieldValidationErrorGeschwister() {
        Geschwister geschwister = new Geschwister();
        // beim Wohnsitz != MUTTER_VATER muessen die Anteil Mutter und Vater Feldern null sein
        geschwister.setWohnsitz(Wohnsitz.FAMILIE);
        geschwister.setWohnsitzAnteilMutter(BigDecimal.TEN);
        Set<Geschwister> geschwisterSet = new HashSet<>();
        geschwisterSet.add(geschwister);
        Gesuch gesuch = prepareDummyGesuch();
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setGeschwisters(geschwisterSet);
        assertAllMessagesPresent(new String[] { VALIDATION_WOHNSITZ_ANTEIL_FIELD_REQUIRED_NULL_MESSAGE }, gesuch);
    }

    @Test
    void testFieldValidationErrorKind() {
        Kind kind = new Kind();
        // feld wohnsitzAnteilPia darf nicht null sein
        Set<Kind> kindSet = new HashSet<>();
        kindSet.add(kind);
        Gesuch gesuch = prepareDummyGesuch();
        kind.setWohnsitzAnteilPia(null);
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setKinds(kindSet);
        assertAllMessagesPresent(new String[] { "{jakarta.validation.constraints.NotNull.message}" }, gesuch);
        // Test die Wohnsitzanteil Berechnung:
        kind.setWohnsitzAnteilPia(100);
        kindSet = new HashSet<>();
        kindSet.add(kind);
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setKinds(kindSet);
        kind.setWohnsitzAnteilPia(55);
        kindSet = new HashSet<>();
        kindSet.add(kind);
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setKinds(kindSet);
        assertAllMessagesNotPresent(new String[] { VALIDATION_WOHNSITZ_ANTEIL_BERECHNUNG_MESSAGE }, gesuch);
    }

    @Test
    void testNullLebenslaufItemArtValidationError() {
        LebenslaufItem lebenslaufItem = new LebenslaufItem();
        Set<LebenslaufItem> lebenslaufItemSet = new HashSet<>();
        lebenslaufItemSet.add(lebenslaufItem);
        Gesuch gesuch = prepareDummyGesuch();
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setLebenslaufItems(lebenslaufItemSet);
        assertAllMessagesPresent(new String[] { VALIDATION_LEBENSLAUFITEM_ART_FIELD_REQUIRED_MESSAGE }, gesuch);
    }

    @Test
    void testLebenslaufItemArtValidationError() {

        LebenslaufItem lebenslaufItem = new LebenslaufItem();
        lebenslaufItem.setBildungsart(LebenslaufAusbildungsArt.BACHELOR_FACHHOCHSCHULE);
        lebenslaufItem.setTaetigkeitsart(Taetigkeitsart.ERWERBSTAETIGKEIT);
        Set<LebenslaufItem> lebenslaufItemSet = new HashSet<>();
        lebenslaufItemSet.add(lebenslaufItem);
        Gesuch gesuch = prepareDummyGesuch();
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setLebenslaufItems(lebenslaufItemSet);
        assertAllMessagesPresent(new String[] { VALIDATION_LEBENSLAUFITEM_ART_FIELD_REQUIRED_NULL_MESSAGE }, gesuch);
    }

    @Test
    void testGesuchEinreichenValidationEltern() {
        Familiensituation familiensituation = new Familiensituation();
        familiensituation.setElternVerheiratetZusammen(false);
        familiensituation.setElternteilUnbekanntVerstorben(true);
        familiensituation.setVaterUnbekanntVerstorben(ElternAbwesenheitsGrund.VERSTORBEN);
        familiensituation.setMutterUnbekanntVerstorben(ElternAbwesenheitsGrund.WEDER_NOCH);
        Gesuch gesuch = prepareDummyGesuch();
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setFamiliensituation(familiensituation);
        Set<ConstraintViolation<Gesuch>> violations = validator.validate(
            gesuch,
            GesuchEinreichenValidationGroup.class
        );
        assertThat(
            violations.stream()
                .anyMatch(
                    gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate()
                        .equals(VALIDATION_FAMILIENSITUATION_ELTERN_ENTITY_REQUIRED_MESSAGE)
                ),
            is(true)
        );
        Eltern eltern = new Eltern();
        eltern.setElternTyp(ElternTyp.MUTTER);
        Set<Eltern> elternSet = new HashSet<>();
        elternSet.add(eltern);
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setElterns(elternSet);
        Adresse adresse = new Adresse();
        Land land = new Land();
        adresse.setLand(land);
        eltern.setAdresse(adresse);

        assertOneMessage(
            VALIDATION_FAMILIENSITUATION_ELTERN_ENTITY_REQUIRED_MESSAGE,
            gesuch,
            false,
            GesuchEinreichenValidationGroup.class
        );
    }

    @Test
    void testGesuchEinreichenValidationLebenslauf() {
        LebenslaufItem lebenslaufItem = new LebenslaufItem();
        lebenslaufItem.setBildungsart(LebenslaufAusbildungsArt.BACHELOR_FACHHOCHSCHULE);
        lebenslaufItem.setTaetigkeitsart(Taetigkeitsart.ERWERBSTAETIGKEIT);
        lebenslaufItem.setVon(LocalDate.of(2020, 10, 1));
        lebenslaufItem.setBis(LocalDate.of(2020, 12, 1));
        Set<LebenslaufItem> lebenslaufItemSet = new HashSet<>();
        lebenslaufItemSet.add(lebenslaufItem);
        Gesuch gesuch = prepareDummyGesuch();
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setLebenslaufItems(lebenslaufItemSet);
        PersonInAusbildung personInAusbildung = new PersonInAusbildung();
        personInAusbildung.setGeburtsdatum(LocalDate.of(2000, 5, 12));
        personInAusbildung.setZivilstand(Zivilstand.LEDIG);
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setPersonInAusbildung(personInAusbildung);
        Adresse adresse = new Adresse();
        Land land = new Land();
        adresse.setLand(land);
        personInAusbildung.setAdresse(adresse);
        gesuch.getAusbildung().setAusbildungBegin(LocalDate.of(2024, 01, 01));

        assertOneMessage(
            VALIDATION_LEBENSLAUF_LUCKENLOS_MESSAGE,
            gesuch,
            true,
            GesuchEinreichenValidationGroup.class
        );
    }

    @Test
    void testGesuchEinreichenValidationEinnahmenKostenEltern() {
        Familiensituation familiensituation = new Familiensituation();
        familiensituation.setElternVerheiratetZusammen(false);
        familiensituation.setGerichtlicheAlimentenregelung(true);
        familiensituation.setWerZahltAlimente(Elternschaftsteilung.VATER);
        familiensituation.setMutterWiederverheiratet(false);
        Gesuch gesuch = prepareDummyGesuch();
        Ausbildung ausbildung = new Ausbildung();
        ausbildung.setAusbildungBegin(LocalDate.now().minusDays(1));
        Fall fall = new Fall();
        Auszahlung auszahlung = new Auszahlung();
        fall.setAuszahlung(auszahlung);
        ausbildung.setFall(fall);
        gesuch.setAusbildung(ausbildung);
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setFamiliensituation(familiensituation);
        EinnahmenKosten einnahmenKosten = new EinnahmenKosten();
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setEinnahmenKosten(einnahmenKosten);
        assertOneMessage(
            VALIDATION_EINNAHMEN_KOSTEN_ALIMENTE_REQUIRED_MESSAGE,
            gesuch,
            true,
            GesuchEinreichenValidationGroup.class
        );
    }

    @Test
    void testGesuchEinreichenValidationEinnahmenKostenPersonInAusbildung() {
        PersonInAusbildung personInAusbildung = new PersonInAusbildung();
        personInAusbildung.setGeburtsdatum(LocalDate.of(2000, 5, 12));
        personInAusbildung.setWohnsitz(Wohnsitz.FAMILIE);
        Adresse adresse = new Adresse();
        Land land = new Land();
        adresse.setLand(land);
        personInAusbildung.setAdresse(adresse);
        Gesuch gesuch = prepareDummyGesuch();
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setPersonInAusbildung(personInAusbildung);
        personInAusbildung.setZivilstand(Zivilstand.LEDIG);
        Kind kind = new Kind();
        Set<Kind> kindSet = new HashSet<Kind>();
        kindSet.add(kind);
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setKinds(kindSet);
        EinnahmenKosten einnahmenKosten = new EinnahmenKosten();
        gesuch.setGesuchsperiode(null);
        Ausbildung ausbildung = new Ausbildung();
        ausbildung.setAusbildungBegin(LocalDate.now().minusDays(1));
        Fall fall = new Fall();
        Auszahlung auszahlung = new Auszahlung();
        fall.setAuszahlung(auszahlung);
        ausbildung.setFall(fall);
        gesuch.setAusbildung(ausbildung);
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setEinnahmenKosten(einnahmenKosten);
        assertOneMessage(
            VALIDATION_EINNAHMEN_KOSTEN_ZULAGEN_REQUIRED_MESSAGE,
            gesuch,
            true,
            GesuchEinreichenValidationGroup.class
        );
    }

    @Test
    void testGesuchEinreichenValidationDarlehenPersonInAusbildungMinderjaehrig() {
        PersonInAusbildung personInAusbildung = new PersonInAusbildung();
        personInAusbildung.setGeburtsdatum(LocalDate.now().minusYears(16));
        personInAusbildung.setWohnsitz(Wohnsitz.FAMILIE);
        Adresse adresse = new Adresse();
        Land land = new Land();
        adresse.setLand(land);
        personInAusbildung.setAdresse(adresse);
        Gesuch gesuch = prepareDummyGesuch();
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setPersonInAusbildung(personInAusbildung);
        personInAusbildung.setZivilstand(Zivilstand.LEDIG);
        Kind kind = new Kind();
        Set<Kind> kindSet = new HashSet<Kind>();
        kindSet.add(kind);
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setKinds(kindSet);
        EinnahmenKosten einnahmenKosten = new EinnahmenKosten();
        Darlehen darlehen = new Darlehen();
        darlehen.setWillDarlehen(true);
        darlehen.setGrundNichtBerechtigt(false);
        darlehen.setGrundHoheGebuehren(false);
        darlehen.setGrundAnschaffungenFuerAusbildung(false);
        darlehen.setGrundZweitausbildung(false);
        darlehen.setGrundAusbildungZwoelfJahre(true);
        gesuch.setGesuchsperiode(null);
        Ausbildung ausbildung = new Ausbildung();
        ausbildung.setAusbildungBegin(LocalDate.now().minusDays(1));
        Fall fall = new Fall();
        Auszahlung auszahlung = new Auszahlung();
        fall.setAuszahlung(auszahlung);
        ausbildung.setFall(fall);
        gesuch.setAusbildung(ausbildung);
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setEinnahmenKosten(einnahmenKosten);
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setDarlehen(darlehen);
        assertOneMessage(
            VALIDATION_DARLEHEN_REQUIRED_VOLLJAEHRIG_MESSAGE,
            gesuch,
            true,
            GesuchEinreichenValidationGroup.class
        );
        assertOneMessage(
            VALIDATION_DARLEHEN_NOT_VALID_MESSAGE,
            darlehen,
            false,
            GesuchEinreichenValidationGroup.class
        );
        assertOneMessage(
            VALIDATION_EINNAHMEN_KOSTEN_ZULAGEN_REQUIRED_MESSAGE,
            gesuch,
            true,
            GesuchEinreichenValidationGroup.class
        );

        gesuch.getNewestGesuchTranche().orElseThrow().getGesuchFormular().setDarlehen(null);
        assertOneMessage(
            VALIDATION_DARLEHEN_REQUIRED_VOLLJAEHRIG_MESSAGE,
            gesuch,
            false,
            GesuchEinreichenValidationGroup.class
        );
    }

    @Test
    void testGesuchEinreichenValidationDarlehenPersonInAusbildungVolljaehrig() {
        PersonInAusbildung personInAusbildung = new PersonInAusbildung();
        personInAusbildung.setGeburtsdatum(LocalDate.now().minusYears(18));
        personInAusbildung.setWohnsitz(Wohnsitz.FAMILIE);
        Adresse adresse = new Adresse();
        Land land = new Land();
        adresse.setLand(land);
        personInAusbildung.setAdresse(adresse);
        Gesuch gesuch = prepareDummyGesuch();
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setPersonInAusbildung(personInAusbildung);
        personInAusbildung.setZivilstand(Zivilstand.LEDIG);
        Kind kind = new Kind();
        Set<Kind> kindSet = new HashSet<Kind>();
        kindSet.add(kind);
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setKinds(kindSet);
        EinnahmenKosten einnahmenKosten = new EinnahmenKosten();
        Darlehen darlehen = new Darlehen();
        darlehen.setWillDarlehen(true);
        darlehen.setGrundNichtBerechtigt(false);
        darlehen.setGrundHoheGebuehren(false);
        darlehen.setGrundAnschaffungenFuerAusbildung(false);
        darlehen.setGrundZweitausbildung(false);
        darlehen.setGrundAusbildungZwoelfJahre(false);
        gesuch.setGesuchsperiode(null);
        Ausbildung ausbildung = new Ausbildung();
        ausbildung.setAusbildungBegin(LocalDate.now().minusDays(1));
        Fall fall = new Fall();
        Auszahlung auszahlung = new Auszahlung();
        fall.setAuszahlung(auszahlung);
        ausbildung.setFall(fall);
        gesuch.setAusbildung(ausbildung);
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setEinnahmenKosten(einnahmenKosten);
        getGesuchTrancheFromGesuch(gesuch).getGesuchFormular().setDarlehen(darlehen);
        assertOneMessage(
            VALIDATION_EINNAHMEN_KOSTEN_ZULAGEN_REQUIRED_MESSAGE,
            gesuch,
            true,
            GesuchEinreichenValidationGroup.class
        );
        assertOneMessage(
            VALIDATION_DARLEHEN_REQUIRED_VOLLJAEHRIG_MESSAGE,
            gesuch,
            false,
            GesuchEinreichenValidationGroup.class
        );
    }

    private void assertOneMessage(String message, AbstractEntity entity, boolean present, Class<?> groups) {
        var violations = validator.validate(entity, groups);
        assertThat(
            message,
            violations.stream()
                .anyMatch(
                    gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate()
                        .equals(message)
                ),
            is(present)
        );
    }

    private void assertOneMessage(String message, AbstractEntity entity, boolean present) {
        var violations = validator.validate(entity);
        assertThat(
            message,
            violations.stream()
                .anyMatch(
                    gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate()
                        .equals(message)
                ),
            is(present)
        );
    }

    private void assertAllMessagesPresent(String[] messages, Gesuch gesuch) {
        assertAll(messages, gesuch, true);
    }

    private void assertAllMessagesNotPresent(String[] messages, Gesuch gesuch) {
        assertAll(messages, gesuch, false);
    }

    private void assertAll(String[] messages, Gesuch gesuch, boolean expected) {
        Set<ConstraintViolation<Gesuch>> violations = validator.validate(gesuch);
        if (expected) {
            assertThat(
                String.format(
                    "Expected at least %s messages, got %s:\nExpected messages: %s",
                    messages.length,
                    violations.size(),
                    Arrays.toString(messages)
                ),
                violations.size() >= messages.length,
                is(true)
            );
        }
        for (String message : messages) {
            assertThat(
                message,
                violations.stream()
                    .anyMatch(
                        gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate()
                            .equals(message)
                    ),
                is(expected)
            );
        }
    }

    private Gesuch prepareDummyGesuch() {
        Gesuch gesuch = new Gesuch();
        GesuchTranche gesuchTranche = new GesuchTranche().setGesuchFormular(new GesuchFormular());
        gesuchTranche.setId(UUID.randomUUID());
        gesuchTranche.setTyp(GesuchTrancheTyp.TRANCHE);
        gesuch.getGesuchTranchen().add(gesuchTranche);
        Ausbildung ausbildung = new Ausbildung();
        ausbildung
            .setFall(new Fall())
            .setAusbildungBegin(LocalDate.now().minusDays(1));
        gesuch.setAusbildung(ausbildung);
        gesuch.setGesuchsperiode(new Gesuchsperiode());
        gesuchTranche.setGesuch(gesuch);
        gesuchTranche.getGesuchFormular().setTranche(gesuchTranche);
        return gesuch;
    }

    private GesuchTranche getGesuchTrancheFromGesuch(Gesuch gesuch) {
        return gesuch.getGesuchTranchen().get(0);
    }
}
