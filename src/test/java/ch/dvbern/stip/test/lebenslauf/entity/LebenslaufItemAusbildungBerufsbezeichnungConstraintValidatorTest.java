package ch.dvbern.stip.test.lebenslauf.entity;

import java.util.Arrays;
import java.util.function.Predicate;

import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItemAusbildungBerufsbezeichnungConstraintValidator;
import ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LebenslaufItemAusbildungBerufsbezeichnungConstraintValidatorTest {

	LebenslaufItemAusbildungBerufsbezeichnungConstraintValidator validator =
			new LebenslaufItemAusbildungBerufsbezeichnungConstraintValidator();

	@Test
	void shouldBeValidIfBildungsartNullAndBerufsbezeichnungNull() {
		LebenslaufItem lebenslaufItem = new LebenslaufItem()
				.setBildungsart(null)
				.setBerufsbezeichnung(null);

		assertThat(validator.isValid(lebenslaufItem, null), is(true));
	}

	@Test
	void shouldNotBeValidIfBildungsartEidgBerufsattestOrEidgFaehigkeitszeugnisAndBerufsbezeichnungNull() {
		for (LebenslaufAusbildungsArt bildungsart : getLebenslaufAusbildungsArtsOfBerufbezeichnung()) {
			LebenslaufItem lebenslaufItem = new LebenslaufItem()
					.setBildungsart(bildungsart)
					.setBerufsbezeichnung(null);
			assertThat(validator.isValid(lebenslaufItem, null), is(false));
		}
	}

	@Test
	void shouldBeValidIfBildungsartEidgBerufsattestOrEidgFaehigkeitszeugnisAndBerufsbezeichnungNotNull() {
		for (LebenslaufAusbildungsArt bildungsart : getLebenslaufAusbildungsArtsOfBerufbezeichnung()) {
			LebenslaufItem lebenslaufItem = new LebenslaufItem()
					.setBildungsart(bildungsart)
					.setBerufsbezeichnung("Berufsbezeichnung");
			assertThat(validator.isValid(lebenslaufItem, null), is(true));
		}
	}

	@Test
	void shouldNotBeValidIfBildungsartOtherThanEidgBerufsattestOrEidgFaehigkeitszeugnisAndBerufsbezeichnungNotNull() {
		var bildungsartenOtherThanEidgBerufsattestOrEidgFaehigkeitszeugnis = Arrays.stream(LebenslaufAusbildungsArt.values())
				.filter(isLebenslaufAusbildungsArtOfBerufsbezeichnung())
				.toArray(LebenslaufAusbildungsArt[]::new);

		for (LebenslaufAusbildungsArt bildungsart : bildungsartenOtherThanEidgBerufsattestOrEidgFaehigkeitszeugnis) {
			LebenslaufItem lebenslaufItem = new LebenslaufItem()
					.setBildungsart(bildungsart)
					.setBerufsbezeichnung("Berufsbezeichnung");
			assertThat(validator.isValid(lebenslaufItem, null), is(false));
		}
	}

	@Test
	void shouldBeValidIfBildungsartOtherThanEidgBerufsattestOrEidgFaehigkeitszeugnisAndBerufsbezeichnungNull() {
		var bildungsartenOtherThanEidgBerufsattestOrEidgFaehigkeitszeugnis = Arrays.stream(LebenslaufAusbildungsArt.values())
				.filter(isLebenslaufAusbildungsArtOfBerufsbezeichnung())
				.toArray(LebenslaufAusbildungsArt[]::new);

		for (LebenslaufAusbildungsArt bildungsart : bildungsartenOtherThanEidgBerufsattestOrEidgFaehigkeitszeugnis) {
			LebenslaufItem lebenslaufItem = new LebenslaufItem()
					.setBildungsart(bildungsart)
					.setBerufsbezeichnung(null);
			assertThat(validator.isValid(lebenslaufItem, null), is(true));
		}
	}

	@NotNull
	private static LebenslaufAusbildungsArt[] getLebenslaufAusbildungsArtsOfBerufbezeichnung() {
		return new LebenslaufAusbildungsArt[] {
				LebenslaufAusbildungsArt.EIDGENOESSISCHES_BERUFSATTEST,
				LebenslaufAusbildungsArt.EIDGENOESSISCHES_FAEHIGKEITSZEUGNIS };
	}

	@NotNull
	private static Predicate<LebenslaufAusbildungsArt> isLebenslaufAusbildungsArtOfBerufsbezeichnung() {
		return lebenslaufAusbildungsArt ->
				lebenslaufAusbildungsArt != LebenslaufAusbildungsArt.EIDGENOESSISCHES_BERUFSATTEST
						&& lebenslaufAusbildungsArt != LebenslaufAusbildungsArt.EIDGENOESSISCHES_FAEHIGKEITSZEUGNIS;
	}
}
