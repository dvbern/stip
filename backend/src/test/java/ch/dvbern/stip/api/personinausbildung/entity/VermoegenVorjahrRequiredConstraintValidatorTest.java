package ch.dvbern.stip.api.personinausbildung.entity;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class VermoegenVorjahrRequiredConstraintValidatorTest {
    @Test
    void statusBRequiredTest() {
        final var validator = new VermoegenVorjahrRequiredConstraintValidator();

        final var pia = new PersonInAusbildung()
            .setNiederlassungsstatus(Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B)
            .setVermoegenVorjahr(null);

        assertThat(validator.isValid(pia, null), is(false));

        pia.setVermoegenVorjahr(1);
        assertThat(validator.isValid(pia, null), is(true));
    }

    @Test
    void fluechtlingBRequiredTest() {
        final var validator = new VermoegenVorjahrRequiredConstraintValidator();

        final var pia = new PersonInAusbildung()
            .setNiederlassungsstatus(Niederlassungsstatus.FLUECHTLING)
            .setVermoegenVorjahr(null);

        assertThat(validator.isValid(pia, null), is(false));

        pia.setVermoegenVorjahr(1);
        assertThat(validator.isValid(pia, null), is(true));
    }

    @Test
    void statusCNotRequiredTest() {
        final var validator = new VermoegenVorjahrRequiredConstraintValidator();

        final var pia = new PersonInAusbildung()
            .setNiederlassungsstatus(Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_C)
            .setVermoegenVorjahr(null);

        assertThat(validator.isValid(pia, null), is(true));
    }

    @Test
    void schweizerNotRequiredTest() {
        final var validator = new VermoegenVorjahrRequiredConstraintValidator();

        final var pia = new PersonInAusbildung()
            .setNiederlassungsstatus(Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_C)
            .setVermoegenVorjahr(null);

        assertThat(validator.isValid(pia, null), is(true));
    }

    @Test
    void wohntOutsideOfBernRequiredTest() {
        final var validator = new VermoegenVorjahrRequiredConstraintValidator();

        final var pia = new PersonInAusbildung()
            .setAdresse(new Adresse().setPlz("7000"))
            .setVermoegenVorjahr(null);

        assertThat(validator.isValid(pia, null), is(false));

        pia.setVermoegenVorjahr(1);
        assertThat(validator.isValid(pia, null), is(true));
    }

    @Test
    void wohntInBernNotRequiredTest() {
        final var validator = new VermoegenVorjahrRequiredConstraintValidator();

        final var pia = new PersonInAusbildung()
            .setAdresse(new Adresse().setPlz("3000"))
            .setVermoegenVorjahr(1);

        assertThat(validator.isValid(pia, null), is(false));

        pia.setVermoegenVorjahr(null);
        assertThat(validator.isValid(pia, null), is(true));
    }
}
