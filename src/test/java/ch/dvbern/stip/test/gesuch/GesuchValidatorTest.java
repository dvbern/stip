package ch.dvbern.stip.test.gesuch;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.stammdaten.type.Land;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import java.util.Set;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_ALTERNATIVE_AUSBILDUNG_FIELD_REQUIRED_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_AUSBILDUNG_FIELD_REQUIRED_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_IZW_FIELD_REQUIRED_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_LAND_CH_FIELD_REQUIRED_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_NACHNAME_NOTBLANK_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_NIEDERLASSUNGSSTATUS_FIELD_REQUIRED_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_VORNAME_NOTBLANK_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_WOHNSITZ_ANTEIL_FIELD_REQUIRED_MESSAGE;
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
		Gesuch gesuch = new Gesuch();
		gesuch.setGesuchFormularToWorkWith(new GesuchFormular());
		gesuch.getGesuchFormularToWorkWith().setPersonInAusbildung(personInAusbildung);
		gesuch.setFall(new Fall());
		gesuch.setGesuchsperiode(new Gesuchsperiode());
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
		Gesuch gesuch = new Gesuch();
		gesuch.setGesuchFormularToWorkWith(new GesuchFormular());
		gesuch.getGesuchFormularToWorkWith().setAusbildung(ausbildung);
		gesuch.setFall(new Fall());
		gesuch.setGesuchsperiode(new Gesuchsperiode());
		Set<ConstraintViolation<Gesuch>> violations = validator.validate(gesuch);
		assertThat(violations.isEmpty(), is(false));
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_AUSBILDUNG_FIELD_REQUIRED_MESSAGE)), is(true));
		gesuch.getGesuchFormularToWorkWith().getAusbildung().setAusbildungNichtGefunden(true);
		violations = validator.validate(gesuch);
		assertThat(violations.stream().anyMatch(gesuchConstraintViolation -> gesuchConstraintViolation.getMessageTemplate().equals(VALIDATION_ALTERNATIVE_AUSBILDUNG_FIELD_REQUIRED_MESSAGE)), is(true));
	}
}
