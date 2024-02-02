package ch.dvbern.stip.test.gesuch;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildungsgang;
import ch.dvbern.stip.api.common.type.Bildungsart;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.gesuch.entity.AusbildungskostenStufeRequiredConstraintValidator;
import ch.dvbern.stip.api.gesuch.entity.EinnahmenKostenDarlehenRequiredConstraintValidator;
import ch.dvbern.stip.api.gesuch.entity.EinnahmenKostenZulagenRequiredConstraintValidator;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.test.util.TestUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class EinnahmenKostenValidatorTest {

	@Test
	void testEinnahmenKostenZulagenRequiredConstraintValidator() {
		EinnahmenKostenZulagenRequiredConstraintValidator einnahmenKostenZulagenRequiredConstraintValidator =
				new EinnahmenKostenZulagenRequiredConstraintValidator();
		GesuchFormular gesuchFormular = prepareGesuchFormularMitEinnahmenKosten();
		assertThat(einnahmenKostenZulagenRequiredConstraintValidator.isValid(gesuchFormular, null))
				.isFalse();
		gesuchFormular.getEinnahmenKosten().setZulagen(BigDecimal.ONE);
		assertThat(einnahmenKostenZulagenRequiredConstraintValidator.isValid(gesuchFormular, null))
				.isTrue();
	}

	@Test
	void testEinnahmenKostenDarlehenRequiredConstraintValidator() {
		EinnahmenKostenDarlehenRequiredConstraintValidator einnahmenKostenDarlehenRequiredConstraintValidator =
				new EinnahmenKostenDarlehenRequiredConstraintValidator();
		// Geburtsdatum null soll keine Validation Fehler verfen als nicht validbar
		GesuchFormular gesuchFormular = prepareGesuchFormularMitEinnahmenKosten();
		assertThat(einnahmenKostenDarlehenRequiredConstraintValidator.isValid(gesuchFormular, null))
				.isTrue();
		// Minderjaehrig
		gesuchFormular.getPersonInAusbildung().setGeburtsdatum(LocalDate.now().minusYears(17));
		assertThat(einnahmenKostenDarlehenRequiredConstraintValidator.isValid(gesuchFormular, null))
				.isTrue();
		// Volljaehrig ohne darlehen Antwort
		gesuchFormular.getPersonInAusbildung().setGeburtsdatum(LocalDate.now().minusYears(18));
		assertThat(einnahmenKostenDarlehenRequiredConstraintValidator.isValid(gesuchFormular, null))
				.isFalse();
		// Volljaehrig mit Darlehen Antwort
		gesuchFormular.getEinnahmenKosten().setWillDarlehen(false);
		assertThat(einnahmenKostenDarlehenRequiredConstraintValidator.isValid(gesuchFormular, null))
				.isTrue();
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
				.isFalse();
		gesuchFormular.getEinnahmenKosten().setAusbildungskostenSekundarstufeZwei(BigDecimal.ONE);
		assertThat(ausbildungskostenStufeRequiredConstraintValidator.isValid(gesuchFormular, null))
				.isTrue();
		gesuchFormular.getAusbildung().getAusbildungsgang().setAusbildungsrichtung(Bildungsart.FACHHOCHSCHULEN);
		assertThat(ausbildungskostenStufeRequiredConstraintValidator.isValid(gesuchFormular, TestUtil.initValidatorContext()))
				.isFalse();
		gesuchFormular.getEinnahmenKosten().setAusbildungskostenTertiaerstufe(BigDecimal.ONE);
		assertThat(ausbildungskostenStufeRequiredConstraintValidator.isValid(gesuchFormular, TestUtil.initValidatorContext()))
				.isTrue();
	}

	private static GesuchFormular prepareGesuchFormularMitEinnahmenKosten() {
		GesuchFormular gesuchFormular = new GesuchFormular();
		gesuchFormular.setEinnahmenKosten(new EinnahmenKosten());
		PersonInAusbildung personInAusbildung = new PersonInAusbildung();
		gesuchFormular.setPersonInAusbildung(personInAusbildung);
		Kind kind = new Kind();
		Set kindSet = new HashSet<Kind>();
		kindSet.add(kind);
		gesuchFormular.setKinds(kindSet);
		return gesuchFormular;
	}
	}
