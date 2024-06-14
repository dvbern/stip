package ch.dvbern.stip.api.personinausbildung.entity;

import java.time.LocalDate;

import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class EinreisedatumRequiredIfNiederlassungsstatusConstraintValidatorTest {
    // Removed strange test named foo

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
