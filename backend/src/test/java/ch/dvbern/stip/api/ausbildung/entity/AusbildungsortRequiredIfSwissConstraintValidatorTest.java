package ch.dvbern.stip.api.ausbildung.entity;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class AusbildungsortRequiredIfSwissConstraintValidatorTest {
    @Test
    void test() {
        final var ausbildung = new Ausbildung()
            .setAusbildungAusland(true);

        final var validator = new AusbildungsortRequiredIfSwissConstraintValidator();

        assertThat(validator.isValid(ausbildung, null), is(false));

        ausbildung.setAusbildungsort("Bern");
        assertThat(validator.isValid(ausbildung, null), is(false));

        ausbildung.setAusbildungAusland(false);
        assertThat(validator.isValid(ausbildung, null), is(true));

        ausbildung.setAusbildungsort(null);
        assertThat(validator.isValid(ausbildung, null), is(false));
    }
}
