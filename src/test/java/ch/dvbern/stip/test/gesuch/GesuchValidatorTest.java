package ch.dvbern.stip.test.gesuch;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import ch.dvbern.stip.api.stammdaten.type.Land;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.util.Set;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GesuchValidatorTest {

	private final Validator validator;

	@Test
	void testFieldValidationErrorForPersonInAusbildung() {
		PersonInAusbildung personInAusbildung = new PersonInAusbildung();
		personInAusbildung.setAdresse(new Adresse());
		// Beim Land CH muss der Heimatort nicht leer sein
		personInAusbildung.getAdresse().setLand(Land.CH);
		// Beim nicht IZV muessen die IZV PLZ und Ort nicht leer sein
		personInAusbildung.setIdentischerZivilrechtlicherWohnsitz(false);
		// Beim andere Nationalitaet als CH muesst die Niederlassungsstatus gegeben werden
		personInAusbildung.setNationalitaet(Land.FR);
		// Beim Wohnsitz MUTTER_VATER muessen die Anteile Feldern nicht null sein
		personInAusbildung.setWohnsitz(Wohnsitz.MUTTER_VATER);
		Gesuch gesuch = prepareDummyGesuch();
		gesuch.getGesuchFormularToWorkWith().setPersonInAusbildung(personInAusbildung);
		Set<ConstraintViolation<Gesuch>> violations = validator.validate(gesuch);
		assertThat(violations.isEmpty(), is(false));
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_NACHNAME_NOTBLANK_MESSAGE)), is(true));
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_VORNAME_NOTBLANK_MESSAGE)), is(true));
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_IZW_FIELD_REQUIRED_MESSAGE)), is(true));
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_LAND_CH_FIELD_REQUIRED_MESSAGE)), is(true));
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_NIEDERLASSUNGSSTATUS_FIELD_REQUIRED_MESSAGE)), is(true));
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_WOHNSITZ_ANTEIL_FIELD_REQUIRED_MESSAGE)), is(true));

		// Die Anteil muessen wenn gegeben einen 100% Pensum im Total entsprechend, groessere oder kleiner Angaben sind rejektiert
		gesuch.getGesuchFormularToWorkWith().getPersonInAusbildung().setWohnsitzAnteilMutter(new BigDecimal(40.00));
		gesuch.getGesuchFormularToWorkWith().getPersonInAusbildung().setWohnsitzAnteilVater(new BigDecimal(50.00));
		violations = validator.validate(gesuch);
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_WOHNSITZ_ANTEIL_BERECHNUNG_MESSAGE)), is(true));
		gesuch.getGesuchFormularToWorkWith().getPersonInAusbildung().setWohnsitzAnteilVater(new BigDecimal(60.00));
		violations = validator.validate(gesuch);
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_WOHNSITZ_ANTEIL_BERECHNUNG_MESSAGE)), is(false));
	}

	@Test
	void testNullFieldValidationErrorForPersonInAusbildung() {
		PersonInAusbildung personInAusbildung = new PersonInAusbildung();
		personInAusbildung.setAdresse(new Adresse());
		// Wohnsitz Anteil muessen leer sein beim Wohnsitz != MUTTER_VATER
		personInAusbildung.setWohnsitz(Wohnsitz.FAMILIE);
		personInAusbildung.setWohnsitzAnteilVater(BigDecimal.ONE);
		// Beim IZV muessen die IZV Ort und PLZ leer sein
		personInAusbildung.setIdentischerZivilrechtlicherWohnsitz(true);
		personInAusbildung.setIdentischerZivilrechtlicherWohnsitzOrt("Test");
		// Beim Land != CH duerfen keinen Heimatort erfasst werden
		personInAusbildung.getAdresse().setLand(Land.FR);
		personInAusbildung.setHeimatort("");
		// Beim CH Nationalitatet duerfen keine Niederlassungsstatus gegeben werden
		personInAusbildung.setNationalitaet(Land.CH);
		personInAusbildung.setNiederlassungsstatus(Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_B);
		Gesuch gesuch = prepareDummyGesuch();
		gesuch.getGesuchFormularToWorkWith().setPersonInAusbildung(personInAusbildung);
		Set<ConstraintViolation<Gesuch>> violations = validator.validate(gesuch);
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_IZW_FIELD_REQUIRED_NULL_MESSAGE)), is(true));
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_WOHNSITZ_ANTEIL_FIELD_REQUIRED_NULL_MESSAGE)), is(true));
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_LAND_CH_FIELD_REQUIRED_NULL_MESSAGE)), is(true));
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_NIEDERLASSUNGSSTATUS_FIELD_REQUIRED_NULL_MESSAGE)), is(true));
	}

	@Test
	void testNullFieldValidationErrorForAusbildung() {
		Ausbildung ausbildung = new Ausbildung();
		Gesuch gesuch = prepareDummyGesuch();
		gesuch.getGesuchFormularToWorkWith().setAusbildung(ausbildung);
		Set<ConstraintViolation<Gesuch>> violations = validator.validate(gesuch);
		// Die Ausbildungsgang und Staette muessen bei keine alternative Ausbildung gegeben werden
		assertThat(violations.isEmpty(), is(false));
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_AUSBILDUNG_FIELD_REQUIRED_MESSAGE)), is(true));
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_ALTERNATIVE_AUSBILDUNG_FIELD_REQUIRED_MESSAGE)), is(false));
		// Die alternative Ausbildungsgang und Staette muessen bei alternative Ausbildung gegeben werden
		gesuch.getGesuchFormularToWorkWith().getAusbildung().setAusbildungNichtGefunden(true);
		violations = validator.validate(gesuch);
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_ALTERNATIVE_AUSBILDUNG_FIELD_REQUIRED_MESSAGE)), is(true));
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_AUSBILDUNG_FIELD_REQUIRED_MESSAGE)), is(false));
	}

	@Test
	void testFieldValidationErrorForAusbildung() {
		Ausbildung ausbildung = new Ausbildung();
		ausbildung.setAlternativeAusbildungsgang("ausbildungsgang alt");
		Gesuch gesuch = prepareDummyGesuch();
		gesuch.getGesuchFormularToWorkWith().setAusbildung(ausbildung);
		// Die alternative Ausbildungsgang und Staette muessen bei keine alternative Ausbildung null sein
		Set<ConstraintViolation<Gesuch>> violations = validator.validate(gesuch);
		assertThat(violations.isEmpty(), is(false));
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_ALTERNATIVE_AUSBILDUNG_FIELD_REQUIRED_NULL_MESSAGE)), is(true));
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_AUSBILDUNG_FIELD_REQUIRED_NULL_MESSAGE)), is(false));
		// Die Ausbildungsgang und Staette muessen bei alternative Ausbildung null sein
		gesuch.getGesuchFormularToWorkWith().getAusbildung().setAusbildungNichtGefunden(true);
		gesuch.getGesuchFormularToWorkWith().getAusbildung().setAusbildungsgang(new Ausbildungsgang());
		violations = validator.validate(gesuch);
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_AUSBILDUNG_FIELD_REQUIRED_NULL_MESSAGE)), is(true));
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_ALTERNATIVE_AUSBILDUNG_FIELD_REQUIRED_NULL_MESSAGE)), is(false));

	}

	@Test
	void testFieldValidationErrorFamiliensituation() {
		Familiensituation familiensituation = new Familiensituation();
		familiensituation.setObhut(Elternschaftsteilung.GEMEINSAM);
		familiensituation.setGerichtlicheAlimentenregelung(true);
		Gesuch gesuch = prepareDummyGesuch();
		gesuch.getGesuchFormularToWorkWith().setFamiliensituation(familiensituation);
		Set<ConstraintViolation<Gesuch>> violations = validator.validate(gesuch);
		assertThat(violations.isEmpty(), is(false));
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_WER_ZAHLT_ALIMENTE_FIELD_REQUIRED_MESSAGE)), is(true));
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_OBHUT_GEMEINSAM_FIELD_REQUIRED_MESSAGE)), is(true));
	}

	@Test
	void testNullFieldValidationErrorFamiliensituation() {
		Familiensituation familiensituation = new Familiensituation();
		familiensituation.setObhut(Elternschaftsteilung.VATER);
		familiensituation.setObhutVater(BigDecimal.ONE);
		familiensituation.setGerichtlicheAlimentenregelung(false);
		familiensituation.setWerZahltAlimente(Elternschaftsteilung.GEMEINSAM);
		Gesuch gesuch = prepareDummyGesuch();
		gesuch.getGesuchFormularToWorkWith().setFamiliensituation(familiensituation);
		Set<ConstraintViolation<Gesuch>> violations = validator.validate(gesuch);
		assertThat(violations.isEmpty(), is(false));
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_WER_ZAHLT_ALIMENTE_FIELD_REQUIRED_NULL_MESSAGE)), is(true));
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_OBHUT_GEMEINSAM_FIELD_REQUIRED_NULL_MESSAGE)), is(true));
	}

	private Gesuch prepareDummyGesuch() {
		Gesuch gesuch = new Gesuch();
		gesuch.setGesuchFormularToWorkWith(new GesuchFormular());
		gesuch.setFall(new Fall());
		gesuch.setGesuchsperiode(new Gesuchsperiode());
		return gesuch;
	}
}
