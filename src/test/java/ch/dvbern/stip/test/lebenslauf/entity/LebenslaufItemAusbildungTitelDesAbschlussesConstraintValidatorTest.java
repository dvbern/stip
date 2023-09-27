package ch.dvbern.stip.test.lebenslauf.entity;

import java.util.Arrays;
import java.util.function.Predicate;

import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItemAusbildungBerufsbezeichnungConstraintValidator;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItemAusbildungFachrichtungConstraintValidator;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItemAusbildungTitelDesAbschlussesConstraintValidator;
import ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

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
		for (LebenslaufAusbildungsArt bildungsart : getLebenslaufAusbildungsArtsForFachrichtung()) {
			LebenslaufItem lebenslaufItem = new LebenslaufItem()
					.setBildungsart(bildungsart)
					.setTitelDesAbschlusses(null);
			assertThat(validator.isValid(lebenslaufItem, null), is(false));
		}
	}

	@Test
	void shouldBeValidIfBildungsartAndererAusbildungsabschlussAndTitelDesAbschlussesNotNull() {
		for (LebenslaufAusbildungsArt bildungsart : getLebenslaufAusbildungsArtsForFachrichtung()) {
			LebenslaufItem lebenslaufItem = new LebenslaufItem()
					.setBildungsart(bildungsart)
					.setTitelDesAbschlusses("Fachrichtung");
			assertThat(validator.isValid(lebenslaufItem, null), is(true));
		}
	}

	@Test
	void shouldNotBeValidIfBildungsartOtherThanAndererAusbildungsabschlussAndTitelDesAbschlussesNotNull() {
		var bildungsartenOtherThanAndererAusbildungsabschluss = Arrays.stream(LebenslaufAusbildungsArt.values())
				.filter(isLebenslaufAusbildungsArtOfFachrichtung())
				.toArray(LebenslaufAusbildungsArt[]::new);

		for (LebenslaufAusbildungsArt bildungsart : bildungsartenOtherThanAndererAusbildungsabschluss) {
			LebenslaufItem lebenslaufItem = new LebenslaufItem()
					.setBildungsart(bildungsart)
					.setTitelDesAbschlusses("Fachrichtung");
			assertThat(validator.isValid(lebenslaufItem, null), is(false));
		}
	}

	@Test
	void shouldBeValidIfBildungsartOtherThanAndererAusbildungsabschlussAndTitelDesAbschlussesNull() {
		var bildungsartenOtherThanAndererAusbildungsabschluss = Arrays.stream(LebenslaufAusbildungsArt.values())
				.filter(isLebenslaufAusbildungsArtOfFachrichtung())
				.toArray(LebenslaufAusbildungsArt[]::new);

		for (LebenslaufAusbildungsArt bildungsart : bildungsartenOtherThanAndererAusbildungsabschluss) {
			LebenslaufItem lebenslaufItem = new LebenslaufItem()
					.setBildungsart(bildungsart)
					.setTitelDesAbschlusses(null);
			assertThat(validator.isValid(lebenslaufItem, null), is(true));
		}
	}

	@NotNull
	private static LebenslaufAusbildungsArt[] getLebenslaufAusbildungsArtsForFachrichtung() {
		return new LebenslaufAusbildungsArt[] {
				LebenslaufAusbildungsArt.ANDERER_BILDUNGSABSCHLUSS,
		};
	}

	@NotNull
	private static Predicate<LebenslaufAusbildungsArt> isLebenslaufAusbildungsArtOfFachrichtung() {
		return lebenslaufAusbildungsArt ->
				lebenslaufAusbildungsArt != LebenslaufAusbildungsArt.ANDERER_BILDUNGSABSCHLUSS;
	}
}
