package ch.dvbern.stip.test.gesuch;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.LebenslaufLuckenlosConstraintValidator;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LebenslaufLuckenlosConstraintValidatorTest {
	LebenslaufLuckenlosConstraintValidator lebenslaufLuckenlosConstraintValidator =
			new LebenslaufLuckenlosConstraintValidator();

	@Test
	void isLebenslaufLuckenlosOkTest() {
		GesuchFormular gesuchFormular = initFormular();
		LebenslaufItem lebenslaufItem = new LebenslaufItem();
		lebenslaufItem.setVon(LocalDate.of(2016, 8, 1));
		lebenslaufItem.setBis(LocalDate.of(2023, 12, 31));
		Set<LebenslaufItem> lebenslaufItemSet = new HashSet<>();
		lebenslaufItemSet.add(lebenslaufItem);
		gesuchFormular.setLebenslaufItems(lebenslaufItemSet);
		assertThat(lebenslaufLuckenlosConstraintValidator.isValid(gesuchFormular, null)
				, is(true));
		lebenslaufItem.setBis(LocalDate.of(2022, 11, 30));
		LebenslaufItem lebenslaufItemZwei = new LebenslaufItem();
		lebenslaufItemZwei.setVon(LocalDate.of(2022, 8, 1));
		lebenslaufItemZwei.setBis(LocalDate.of(2023, 12, 31));
		lebenslaufItemSet.clear();
		lebenslaufItemSet.add(lebenslaufItem);
		lebenslaufItemSet.add(lebenslaufItemZwei);
		gesuchFormular.setLebenslaufItems(lebenslaufItemSet);
		assertThat(lebenslaufLuckenlosConstraintValidator.isValid(gesuchFormular, null)
				, is(true));
	}

	@Test
	void isLebenslaufLuckenlosStartZuFruehTest() {
		GesuchFormular gesuchFormular = initFormular();
		LebenslaufItem lebenslaufItem = new LebenslaufItem();
		lebenslaufItem.setVon(LocalDate.of(2016, 10, 1));
		lebenslaufItem.setBis(LocalDate.of(2023, 12, 31));
		Set<LebenslaufItem> lebenslaufItemSet = new HashSet<>();
		lebenslaufItemSet.add(lebenslaufItem);
		gesuchFormular.setLebenslaufItems(lebenslaufItemSet);
		assertThat(lebenslaufLuckenlosConstraintValidator.isValid(gesuchFormular, null)
				, is(false));
	}

	@Test
	void isLebenslaufLuckenlosStopZuSpaetTest() {
		GesuchFormular gesuchFormular = initFormular();
		LebenslaufItem lebenslaufItem = new LebenslaufItem();
		lebenslaufItem.setVon(LocalDate.of(2016, 8, 1));
		lebenslaufItem.setBis(LocalDate.of(2024, 12, 31));
		Set<LebenslaufItem> lebenslaufItemSet = new HashSet<>();
		lebenslaufItemSet.add(lebenslaufItem);
		gesuchFormular.setLebenslaufItems(lebenslaufItemSet);
		assertThat(lebenslaufLuckenlosConstraintValidator.isValid(gesuchFormular, null)
				, is(false));
	}

	@Test
	void isLebenslaufLuckenlosKoTest() {
		GesuchFormular gesuchFormular = initFormular();
		LebenslaufItem lebenslaufItem = new LebenslaufItem();
		lebenslaufItem.setVon(LocalDate.of(2016, 8, 1));
		lebenslaufItem.setBis(LocalDate.of(2022, 6, 30));
		Set<LebenslaufItem> lebenslaufItemSet = new HashSet<>();
		lebenslaufItemSet.add(lebenslaufItem);
		LebenslaufItem lebenslaufItemZwei = new LebenslaufItem();
		lebenslaufItemZwei.setVon(LocalDate.of(2022, 8, 1));
		lebenslaufItemZwei.setBis(LocalDate.of(2023, 12, 31));
		lebenslaufItemSet.add(lebenslaufItemZwei);
		gesuchFormular.setLebenslaufItems(lebenslaufItemSet);
		assertThat(lebenslaufLuckenlosConstraintValidator.isValid(gesuchFormular, null)
				, is(false));
	}

	private GesuchFormular initFormular() {
		GesuchFormular gesuchFormular = new GesuchFormular();
		PersonInAusbildung personInAusbildung = new PersonInAusbildung();
		personInAusbildung.setGeburtsdatum(LocalDate.of(2000, 5, 12));
		gesuchFormular.setPersonInAusbildung(personInAusbildung);
		Ausbildung ausbildung = new Ausbildung();
		ausbildung.setAusbildungBegin(LocalDate.of(2024, 01, 01));
		gesuchFormular.setAusbildung(ausbildung);
		return gesuchFormular;
	}
}
