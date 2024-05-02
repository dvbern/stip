package ch.dvbern.stip.api.gesuch.entity;

import java.util.Set;

import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.kind.entity.Kind;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class BetreuungskostenRequiredConstraintValidatorTest {
    @Test
    void isValidTest() {
        final var validator = new EinnahmenKostenBetreuungskostenRequiredConstraintValidator();

        final var formular = new GesuchFormular()
            .setKinds(Set.of(new Kind()))
            .setEinnahmenKosten(new EinnahmenKosten());

        assertThat(validator.isValid(formular, null), is(false));

        formular.setEinnahmenKosten(new EinnahmenKosten().setBetreuungskostenKinder(1));

        assertThat(validator.isValid(formular, null), is(true));
    }
}
