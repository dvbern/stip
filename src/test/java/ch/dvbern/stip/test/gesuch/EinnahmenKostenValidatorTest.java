package ch.dvbern.stip.test.gesuch;

import java.math.BigDecimal;
import java.time.LocalDate;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.common.type.Bildungsart;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.gesuch.entity.AusbildungskostenStufeRequiredConstraintValidator;
import ch.dvbern.stip.api.gesuch.entity.EinnahmenKostenDarlehenRequiredConstraintValidator;
import ch.dvbern.stip.api.gesuch.entity.EinnahmenKostenZulagenRequiredConstraintValidator;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.hibernate.validator.messageinterpolation.ExpressionLanguageFeatureLevel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EinnahmenKostenValidatorTest {

	@Test
	void testEinnahmenKostenZulagenRequiredConstraintValidator() {
		EinnahmenKostenZulagenRequiredConstraintValidator einnahmenKostenZulagenRequiredConstraintValidator =
				new EinnahmenKostenZulagenRequiredConstraintValidator();
		GesuchFormular gesuchFormular = prepareGesuchFormularMitEinnahmenKosten();
		gesuchFormular.getPersonInAusbildung().setKinder(true);
		assertThat(einnahmenKostenZulagenRequiredConstraintValidator.isValid(gesuchFormular, null))
				.isEqualTo(false);
		gesuchFormular.getEinnahmenKosten().setZulagen(BigDecimal.ONE);
		assertThat(einnahmenKostenZulagenRequiredConstraintValidator.isValid(gesuchFormular, null))
				.isEqualTo(true);
	}

	@Test
	void testEinnahmenKostenDarlehenRequiredConstraintValidator() {
		EinnahmenKostenDarlehenRequiredConstraintValidator einnahmenKostenDarlehenRequiredConstraintValidator =
				new EinnahmenKostenDarlehenRequiredConstraintValidator();
		// Geburtsdatum null soll keine Validation Fehler verfen als nicht validbar
		GesuchFormular gesuchFormular = prepareGesuchFormularMitEinnahmenKosten();
		assertThat(einnahmenKostenDarlehenRequiredConstraintValidator.isValid(gesuchFormular, null))
				.isEqualTo(true);
		// Minderjaehrig
		gesuchFormular.getPersonInAusbildung().setGeburtsdatum(LocalDate.now().minusYears(17));
		assertThat(einnahmenKostenDarlehenRequiredConstraintValidator.isValid(gesuchFormular, null))
				.isEqualTo(true);
		// Volljaehrig ohne darlehen Antwort
		gesuchFormular.getPersonInAusbildung().setGeburtsdatum(LocalDate.now().minusYears(18));
		assertThat(einnahmenKostenDarlehenRequiredConstraintValidator.isValid(gesuchFormular, null))
				.isEqualTo(false);
		// Volljaehrig mit Darlehen Antwort
		gesuchFormular.getEinnahmenKosten().setWillDarlehen(false);
		assertThat(einnahmenKostenDarlehenRequiredConstraintValidator.isValid(gesuchFormular, null))
				.isEqualTo(true);
	}

	@Test
	void testAusbildungskostenStufeRequiredConstraintValidator() {
		AusbildungskostenStufeRequiredConstraintValidator ausbildungskostenStufeRequiredConstraintValidator =
				new AusbildungskostenStufeRequiredConstraintValidator();
		GesuchFormular gesuchFormular = prepareGesuchFormularMitEinnahmenKosten();
		Ausbildung ausbildung = new Ausbildung();
		ausbildung.setAusbildungsgang(new Ausbildungsgang());
		ausbildung.getAusbildungsgang().setAusbildungsrichtung(Bildungsart.BERUFSMATURITAET_NACH_LEHRE);
		gesuchFormular.setAusbildung(ausbildung);
		assertThat(ausbildungskostenStufeRequiredConstraintValidator.isValid(gesuchFormular, null))
				.isEqualTo(false);
		gesuchFormular.getEinnahmenKosten().setAusbildungskostenSekundarstufeZwei(BigDecimal.ONE);
		assertThat(ausbildungskostenStufeRequiredConstraintValidator.isValid(gesuchFormular, null))
				.isEqualTo(true);
		gesuchFormular.getAusbildung().getAusbildungsgang().setAusbildungsrichtung(Bildungsart.FACHHOCHSCHULEN);
		assertThat(ausbildungskostenStufeRequiredConstraintValidator.isValid(gesuchFormular, initValidatorContext()))
				.isEqualTo(false);
		gesuchFormular.getEinnahmenKosten().setAusbildungskostenTertiaerstufe(BigDecimal.ONE);
		assertThat(ausbildungskostenStufeRequiredConstraintValidator.isValid(gesuchFormular, initValidatorContext()))
				.isEqualTo(true);
	}

	private static GesuchFormular prepareGesuchFormularMitEinnahmenKosten() {
		GesuchFormular gesuchFormular = new GesuchFormular();
		gesuchFormular.setEinnahmenKosten(new EinnahmenKosten());
		PersonInAusbildung personInAusbildung = new PersonInAusbildung();
		gesuchFormular.setPersonInAusbildung(personInAusbildung);
		return gesuchFormular;
	}

	private ConstraintValidatorContext initValidatorContext() {
		return new ConstraintValidatorContextImpl(null, PathImpl.createRootPath(),null,null,
				ExpressionLanguageFeatureLevel.DEFAULT, ExpressionLanguageFeatureLevel.DEFAULT);
	}

}
