package ch.dvbern.stip.test.gesuch;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.stammdaten.type.Land;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
	void testFieldValidationErrorPersonInAusbildung() {
		PersonInAusbildung personInAusbildung = new PersonInAusbildung();
		personInAusbildung.setAdresse(new Adresse());
		personInAusbildung.getAdresse().setLand(Land.CH);
		personInAusbildung.setIdentischerZivilrechtlicherWohnsitz(false);
		personInAusbildung.setNationalitaet(Land.FR);
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
	}

	@Test
	void testFieldValidationErrorAusbildung() {
		Ausbildung ausbildung = new Ausbildung();
		Gesuch gesuch = prepareDummyGesuch();
		gesuch.getGesuchFormularToWorkWith().setAusbildung(ausbildung);
		Set<ConstraintViolation<Gesuch>> violations = validator.validate(gesuch);
		assertThat(violations.isEmpty(), is(false));
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_AUSBILDUNG_FIELD_REQUIRED_MESSAGE)), is(true));
		gesuch.getGesuchFormularToWorkWith().getAusbildung().setAusbildungNichtGefunden(true);
		violations = validator.validate(gesuch);
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_ALTERNATIVE_AUSBILDUNG_FIELD_REQUIRED_MESSAGE)), is(true));
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

	private Gesuch prepareDummyGesuch() {
		Gesuch gesuch = new Gesuch();
		gesuch.setGesuchFormularToWorkWith(new GesuchFormular());
		gesuch.setFall(new Fall());
		gesuch.setGesuchsperiode(new Gesuchsperiode());
		return gesuch;
	}
}
