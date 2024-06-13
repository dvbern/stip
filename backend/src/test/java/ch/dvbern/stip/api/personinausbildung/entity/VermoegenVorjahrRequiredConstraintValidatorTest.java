package ch.dvbern.stip.api.personinausbildung.entity;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.personinausbildung.type.Niederlassungsstatus;
import ch.dvbern.stip.api.plz.service.PlzOrtService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@QuarkusTest
@AllArgsConstructor
class VermoegenVorjahrRequiredConstraintValidatorTest {
    @Inject
    PlzOrtService plzOrtService;

    @Inject
    EntityManager entityManager;

    @Test
    void statusBRequiredTest() {
        final var validator = new VermoegenVorjahrRequiredConstraintValidator(plzOrtService, entityManager);

        final var pia = new PersonInAusbildung()
            .setNiederlassungsstatus(Niederlassungsstatus.AUFENTHALTSBEWILLIGUNG_B)
            .setVermoegenVorjahr(null);

        assertThat(validator.isValid(pia, null), is(false));

        pia.setVermoegenVorjahr(1);
        assertThat(validator.isValid(pia, null), is(true));
    }

    @Test
    void fluechtlingBRequiredTest() {
        final var validator = new VermoegenVorjahrRequiredConstraintValidator(plzOrtService, entityManager);

        final var pia = new PersonInAusbildung()
            .setNiederlassungsstatus(Niederlassungsstatus.FLUECHTLING)
            .setVermoegenVorjahr(null);

        assertThat(validator.isValid(pia, null), is(false));

        pia.setVermoegenVorjahr(1);
        assertThat(validator.isValid(pia, null), is(true));
    }

    @Test
    void statusCNotRequiredTest() {
        final var validator = new VermoegenVorjahrRequiredConstraintValidator(plzOrtService, entityManager);

        final var pia = new PersonInAusbildung()
            .setNiederlassungsstatus(Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_C)
            .setVermoegenVorjahr(null);

        assertThat(validator.isValid(pia, null), is(true));
    }

    @Test
    void schweizerNotRequiredTest() {
        final var validator = new VermoegenVorjahrRequiredConstraintValidator(plzOrtService, entityManager);

        final var pia = new PersonInAusbildung()
            .setNiederlassungsstatus(Niederlassungsstatus.NIEDERLASSUNGSBEWILLIGUNG_C)
            .setVermoegenVorjahr(null);

        assertThat(validator.isValid(pia, null), is(true));
    }

    @Test
    void wohntOutsideOfBernRequiredTest() {
        final var validator = new VermoegenVorjahrRequiredConstraintValidator(plzOrtService, entityManager);

        final var pia = new PersonInAusbildung()
            .setAdresse(new Adresse().setPlz("7000"))
            .setVermoegenVorjahr(null);

        assertThat(validator.isValid(pia, null), is(false));

        pia.setVermoegenVorjahr(1);
        assertThat(validator.isValid(pia, null), is(true));
    }

    @Test
    void wohntInBernNotRequiredTest() {
        final var validator = new VermoegenVorjahrRequiredConstraintValidator(plzOrtService, entityManager);

        final var pia = new PersonInAusbildung()
            .setAdresse(new Adresse().setPlz("3011"))
            .setVermoegenVorjahr(1);

        assertThat(validator.isValid(pia, null), is(false));

        pia.setVermoegenVorjahr(null);
        assertThat(validator.isValid(pia, null), is(true));
    }
}
