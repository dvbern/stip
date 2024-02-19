package ch.dvbern.stip.test.gesuch;

import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.UniqueSvNumberConstraintValidator;
import ch.dvbern.stip.api.gesuch.service.GesuchFormularMapper;
import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.test.generator.entities.GesuchGenerator;
import ch.dvbern.stip.test.util.TestConstants;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class UniqueSvConstraintValidatorTest {
    UniqueSvNumberConstraintValidator validator =
        new UniqueSvNumberConstraintValidator();

    GesuchFormular gesuchFormular = new GesuchFormular();

    @Inject
    GesuchFormularMapper gesuchFormularMapper;

    @BeforeEach
    void setUp() {
        gesuchFormular = gesuchFormularMapper.partialUpdate(GesuchGenerator.createGesuch()
            .getGesuchTrancheToWorkWith()
            .getGesuchFormular(), gesuchFormular);
    }

    @Test
    void onlyPersonInAusbildung() {
        gesuchFormular.setPartner(null)
            .setElterns(null);

        assertThat(gesuchFormular.getPersonInAusbildung(), notNullValue());
        assertThat(validator.isValid(gesuchFormular, null), is(true));
    }

    @Test
    void personInAusbildungAndPartnerSameSvNumber() {
        gesuchFormular.setPartner(new Partner());
        gesuchFormular.getPartner()
            .setSozialversicherungsnummer(gesuchFormular.getPersonInAusbildung().getSozialversicherungsnummer());

        assertThat(validator.isValid(gesuchFormular, null), is(false));
    }

    @Test
    void personInAusbildungAndElternSameSvNumber() {
        gesuchFormular.getElterns().stream().toList().get(0)
            .setSozialversicherungsnummer(gesuchFormular.getPersonInAusbildung().getSozialversicherungsnummer());

        assertThat(validator.isValid(gesuchFormular, null), is(false));
    }

    @Test
    void elternSameSvNumber() {
        gesuchFormular.getElterns()
            .forEach(eltern -> eltern.setSozialversicherungsnummer(TestConstants.AHV_NUMMER_VALID));
        assertThat(validator.isValid(gesuchFormular, null), is(false));
    }

    @Test
    void formularAllPersonUniqueAhvNumber() {
        gesuchFormular.setPartner(new Partner());
        assertThat(gesuchFormular.getPersonInAusbildung(), notNullValue());
        assertThat(gesuchFormular.getPartner(), notNullValue());
        assertThat(gesuchFormular.getElterns(), notNullValue());
        assertThat(validator.isValid(gesuchFormular, null), is(true));
    }

}
