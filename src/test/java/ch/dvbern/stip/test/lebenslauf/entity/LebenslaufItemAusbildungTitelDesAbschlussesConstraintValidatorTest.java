package ch.dvbern.stip.test.lebenslauf.entity;

import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItemAusbildungTitelDesAbschlussesConstraintValidator;
import ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LebenslaufItemAusbildungTitelDesAbschlussesConstraintValidatorTest {

	LebenslaufItemAusbildungTitelDesAbschlussesConstraintValidator validator =
			new LebenslaufItemAusbildungTitelDesAbschlussesConstraintValidator();

	@Test
	void shouldBeValidIfBildungsartNullAndTitelDesAbschlussesNull() {
		LebenslaufItem lebenslaufItem = new LebenslaufItem()
				.setBildungsart(null)
				.setTitelDesAbschlusses(null);

		assertThat(validator.isValid(lebenslaufItem, null), is(true));
	}

	@Test
	void shouldNotBeValidIfBildungsartAndererAusbildungsabschlussAndTitelDesAbschlussesNull() {
		LebenslaufItem lebenslaufItem = new LebenslaufItem()
				.setBildungsart(LebenslaufAusbildungsArt.ANDERER_BILDUNGSABSCHLUSS)
				.setTitelDesAbschlusses(null);
		assertThat(validator.isValid(lebenslaufItem, null), is(false));
	}

	@Test
	void shouldBeValidIfBildungsartAndererAusbildungsabschlussAndTitelDesAbschlussesNotNull() {
		LebenslaufItem lebenslaufItem = new LebenslaufItem()
				.setBildungsart(LebenslaufAusbildungsArt.ANDERER_BILDUNGSABSCHLUSS)
				.setTitelDesAbschlusses("Fachrichtung");
		assertThat(validator.isValid(lebenslaufItem, null), is(true));
	}

	@Test
	void shouldNotBeValidIfBildungsartOtherThanAndererAusbildungsabschlussAndTitelDesAbschlussesNotNull() {
		Arrays.stream(LebenslaufAusbildungsArt.values())
				.filter(isLebenslaufAusbildungsArtNotAndererBildungsabschluss())
				.forEach(bildungsart -> {
						LebenslaufItem lebenslaufItem = new LebenslaufItem()
							.setBildungsart(bildungsart)
							.setTitelDesAbschlusses("Fachrichtung");
						assertThat(validator.isValid(lebenslaufItem, null), is(false));
				});
	}

	@Test
	void shouldBeValidIfBildungsartOtherThanAndererAusbildungsabschlussAndTitelDesAbschlussesNull() {
		Arrays.stream(LebenslaufAusbildungsArt.values())
				.filter(isLebenslaufAusbildungsArtNotAndererBildungsabschluss())
				.forEach(bildungsart -> {
					LebenslaufItem lebenslaufItem = new LebenslaufItem()
							.setBildungsart(bildungsart)
							.setTitelDesAbschlusses(null);
					assertThat(validator.isValid(lebenslaufItem, null), is(true));
				});
	}

	@NotNull
	private static Predicate<LebenslaufAusbildungsArt> isLebenslaufAusbildungsArtNotAndererBildungsabschluss() {
		return lebenslaufAusbildungsArt ->
				lebenslaufAusbildungsArt != LebenslaufAusbildungsArt.ANDERER_BILDUNGSABSCHLUSS;
	}
}
