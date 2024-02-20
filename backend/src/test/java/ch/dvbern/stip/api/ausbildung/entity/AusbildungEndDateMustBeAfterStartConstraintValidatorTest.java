package ch.dvbern.stip.api.ausbildung.entity;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

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
