package ch.dvbern.stip.api.ausbildung.entity;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class AusbildungsortRequiredIfSwissConstraintValidatorTest {
    @Test
    void test() {
        final var ausbildung = new Ausbildung()
            .setIsAusbildungAusland(true);

        final var validator = new AusbildungsortRequiredIfSwissConstraintValidator();

        assertThat("Ausbildung im Ausland ohne Ort", validator.isValid(ausbildung, null), is(true));

        ausbildung.setAusbildungsort("Bern");
        assertThat("Ausbildung im Ausland mit Ort", validator.isValid(ausbildung, null), is(false));

        ausbildung.setIsAusbildungAusland(false);
        assertThat("Ausbildung in CH mit Ort", validator.isValid(ausbildung, null), is(true));

        ausbildung.setAusbildungsort(null);
        assertThat("Ausbildung in CH ohne Ort", validator.isValid(ausbildung, null), is(false));
    }
}
