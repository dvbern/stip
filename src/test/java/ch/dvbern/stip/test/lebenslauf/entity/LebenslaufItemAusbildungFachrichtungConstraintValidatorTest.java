package ch.dvbern.stip.test.lebenslauf.entity;

import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItemAusbildungFachrichtungConstraintValidator;
import ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class LebenslaufItemAusbildungFachrichtungConstraintValidatorTest {

	LebenslaufItemAusbildungFachrichtungConstraintValidator validator =
			new LebenslaufItemAusbildungFachrichtungConstraintValidator();

	@Test
	void shouldBeValidIfBildungsartNullAndFachrichtungNull() {
		LebenslaufItem lebenslaufItem = new LebenslaufItem()
				.setBildungsart(null)
				.setFachrichtung(null);

		assertThat(validator.isValid(lebenslaufItem, null), is(true));
	}

	@Test
	void shouldNotBeValidIfBildungsartBachelorOrMasterAndFachrichtungNull() {
		getLebenslaufAusbildungsArtsForFachrichtung().forEach(bildungsart -> {
			LebenslaufItem lebenslaufItem = new LebenslaufItem()
					.setBildungsart(bildungsart)
					.setFachrichtung(null);
			assertThat(validator.isValid(lebenslaufItem, null), is(false));
		});
	}

	@Test
	void shouldBeValidIfBildungsartBachelorOrMasterAndFachrichtungNotNull() {
		getLebenslaufAusbildungsArtsForFachrichtung().forEach(bildungsart -> {
			LebenslaufItem lebenslaufItem = new LebenslaufItem()
					.setBildungsart(bildungsart)
					.setFachrichtung("Fachrichtung");
			assertThat(validator.isValid(lebenslaufItem, null), is(true));
		});
	}

	@Test
	void shouldNotBeValidIfBildungsartOtherThanBachelorOrMasterAndFachrichtungNotNull() {
		Arrays.stream(LebenslaufAusbildungsArt.values())
				.filter(isLebenslaufAusbildungsArtOfFachrichtung().negate())
				.forEach(bildungsarteOtherThanBachelorOrMaster -> {
					LebenslaufItem lebenslaufItem = new LebenslaufItem()
							.setBildungsart(bildungsarteOtherThanBachelorOrMaster)
							.setFachrichtung("Fachrichtung");
					assertThat(validator.isValid(lebenslaufItem, null), is(false));
				});
	}

	@Test
	void shouldBeValidIfBildungsartOtherThanBachelorOrMasterAndFachrichtungNull() {
		var bildungsartenOtherThanBachelorOrMaster = Arrays.stream(LebenslaufAusbildungsArt.values())
				.filter(isLebenslaufAusbildungsArtOfFachrichtung().negate())
				.toArray(LebenslaufAusbildungsArt[]::new);

		for (LebenslaufAusbildungsArt bildungsart : bildungsartenOtherThanBachelorOrMaster) {
			LebenslaufItem lebenslaufItem = new LebenslaufItem()
					.setBildungsart(bildungsart)
					.setFachrichtung(null);
			assertThat(validator.isValid(lebenslaufItem, null), is(true));
		}
	}

	@NotNull
	private static List<LebenslaufAusbildungsArt> getLebenslaufAusbildungsArtsForFachrichtung() {
		return List.of(
				LebenslaufAusbildungsArt.BACHELOR_FACHHOCHSCHULE,
				LebenslaufAusbildungsArt.BACHELOR_HOCHSCHULE_UNI,
				LebenslaufAusbildungsArt.MASTER);
	}

	@NotNull
	private static Predicate<LebenslaufAusbildungsArt> isLebenslaufAusbildungsArtOfFachrichtung() {
		return lebenslaufAusbildungsArt -> getLebenslaufAusbildungsArtsForFachrichtung().contains(lebenslaufAusbildungsArt);
	}
}
