package ch.dvbern.stip.test.ausbildung;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.entity.AusbildungEndDateMustBeAfterStartConstraintValidator;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class AusbildungEndDateMustBeAfterStartConstraintValidatorTest {
    @Test
    void ausbildungEndDateMustBeAfterStart() {
        final var validator = new AusbildungEndDateMustBeAfterStartConstraintValidator();
        final var ausbildung = new Ausbildung()
            .setAusbildungBegin(LocalDate.now())
            .setAusbildungEnd(LocalDate.now().minusYears(1));

        assertThat(validator.isValid(ausbildung, null), is(false));

        ausbildung.setAusbildungEnd(LocalDate.now().plusYears(1));

        assertThat(validator.isValid(ausbildung, null), is(true));
    }
}
