package ch.dvbern.stip.api.personinausbildung.entity;

import ch.dvbern.stip.api.personinausbildung.entity.EinreisedatumRequiredIfNiederlassungsstatusConstraintValidator;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class EinreisedatumRequiredIfNiederlassungsstatusConstraintValidatorTest {
    @Test
    void einreisedatumRequiredIfAufenthaltsbewilligung() {
        final var pia = new PersonInAusbildung();
        pia.setNiederlassungsstatus(Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B);

        final var validator = new EinreisedatumRequiredIfNiederlassungsstatusConstraintValidator();
        assertThat(validator.isValid(pia, null), is(false));

        pia.setEinreisedatum(LocalDate.now());
        assertThat(validator.isValid(pia, null), is(true));
    }

    @Test
    void einreisedatumNotRequiredIfNiederlassungsstatus() {
        final var pia = new PersonInAusbildung();
        pia.setNiederlassungsstatus(Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_C);

        final var validator = new EinreisedatumRequiredIfNiederlassungsstatusConstraintValidator();
        assertThat(validator.isValid(pia, null), is(true));
    }

    @Test
    void einreisedatumNotRequiredIfFluechtling() {
        final var pia = new PersonInAusbildung();
        pia.setNiederlassungsstatus(Niederlassungsstatus.FLUECHTLING);

        final var validator = new EinreisedatumRequiredIfNiederlassungsstatusConstraintValidator();
        assertThat(validator.isValid(pia, null), is(true));
    }
}
