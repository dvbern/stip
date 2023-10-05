package ch.dvbern.stip.test.lebenslauf.entity;

import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItemAusbildungBerufsbezeichnungConstraintValidator;
import ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

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
		getLebenslaufAusbildungsArtsOfBerufbezeichnung().forEach(bildungsart -> {
			LebenslaufItem lebenslaufItem = new LebenslaufItem()
					.setBildungsart(bildungsart)
					.setBerufsbezeichnung(null);
			assertThat(validator.isValid(lebenslaufItem, null), is(false));
		});
	}

	@Test
	void shouldBeValidIfBildungsartEidgBerufsattestOrEidgFaehigkeitszeugnisAndBerufsbezeichnungNotNull() {
		getLebenslaufAusbildungsArtsOfBerufbezeichnung().forEach(bildungsart -> {
			LebenslaufItem lebenslaufItem = new LebenslaufItem()
					.setBildungsart(bildungsart)
					.setBerufsbezeichnung("Berufsbezeichnung");
			assertThat(validator.isValid(lebenslaufItem, null), is(true));
		});
	}

	@Test
	void shouldNotBeValidIfBildungsartOtherThanEidgBerufsattestOrEidgFaehigkeitszeugnisAndBerufsbezeichnungNotNull() {
		Arrays.stream(LebenslaufAusbildungsArt.values())
				.filter(isLebenslaufAusbildungsArtOfBerufsbezeichnung().negate())
				.forEach(bildungsartenOtherThanEidgBerufsattestOrEidgFaehigkeitszeugnis -> {
					LebenslaufItem lebenslaufItem = new LebenslaufItem()
							.setBildungsart(bildungsartenOtherThanEidgBerufsattestOrEidgFaehigkeitszeugnis)
							.setBerufsbezeichnung("Berufsbezeichnung");
					assertThat(validator.isValid(lebenslaufItem, null), is(false));
				});
	}

	@Test
	void shouldBeValidIfBildungsartOtherThanEidgBerufsattestOrEidgFaehigkeitszeugnisAndBerufsbezeichnungNull() {
		Arrays.stream(LebenslaufAusbildungsArt.values())
				.filter(isLebenslaufAusbildungsArtOfBerufsbezeichnung().negate())
				.forEach(bildungsartenOtherThanEidgBerufsattestOrEidgFaehigkeitszeugnis -> {
			LebenslaufItem lebenslaufItem = new LebenslaufItem()
					.setBildungsart(bildungsartenOtherThanEidgBerufsattestOrEidgFaehigkeitszeugnis)
					.setBerufsbezeichnung(null);
			assertThat(validator.isValid(lebenslaufItem, null), is(true));
		});
	}

	@NotNull
	private static List<LebenslaufAusbildungsArt> getLebenslaufAusbildungsArtsOfBerufbezeichnung() {
		return List.of(
				LebenslaufAusbildungsArt.EIDGENOESSISCHES_BERUFSATTEST,
				LebenslaufAusbildungsArt.EIDGENOESSISCHES_FAEHIGKEITSZEUGNIS);
	}

	@NotNull
	private static Predicate<LebenslaufAusbildungsArt> isLebenslaufAusbildungsArtOfBerufsbezeichnung() {
		return lebenslaufAusbildungsArt -> getLebenslaufAusbildungsArtsOfBerufbezeichnung().contains(lebenslaufAusbildungsArt);
	}
}
