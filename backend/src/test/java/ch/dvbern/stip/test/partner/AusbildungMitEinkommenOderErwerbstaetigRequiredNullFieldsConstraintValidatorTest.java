package ch.dvbern.stip.test.partner;

import ch.dvbern.stip.api.partner.entity.AusbildungMitEinkommenOderErwerbstaetigRequiredNullFieldsConstraintValidator;
import ch.dvbern.stip.api.partner.entity.Partner;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AusbildungMitEinkommenOderErwerbstaetigRequiredNullFieldsConstraintValidatorTest {

    @Test
    public void ausbildungMitEinkommenOderErwerbstaetigTrueAndJahreseinkommenFieldNotNullShouldNotBeValid() {
        AusbildungMitEinkommenOderErwerbstaetigRequiredNullFieldsConstraintValidator validator =
            new AusbildungMitEinkommenOderErwerbstaetigRequiredNullFieldsConstraintValidator();

        Partner partner = new Partner();
        partner.setAusbildungMitEinkommenOderErwerbstaetig(false);
        partner.setJahreseinkommen(BigDecimal.ONE);

        assertThat(validator.isValid(partner, null), is(false));
    }

    @Test
    public void ausbildungMitEinkommenOderErwerbstaetigTrueAndVerpflegungskostenFieldSetShouldNotBeValid() {
        AusbildungMitEinkommenOderErwerbstaetigRequiredNullFieldsConstraintValidator validator =
            new AusbildungMitEinkommenOderErwerbstaetigRequiredNullFieldsConstraintValidator();

        Partner partner = new Partner();
        partner.setAusbildungMitEinkommenOderErwerbstaetig(false);
        partner.setVerpflegungskosten(BigDecimal.ONE);

        assertThat(validator.isValid(partner, null), is(false));
    }

    @Test
    public void ausbildungMitEinkommenOderErwerbstaetigTrueAndFahrkostenFieldSetShouldNotBeValid() {
        AusbildungMitEinkommenOderErwerbstaetigRequiredNullFieldsConstraintValidator validator =
            new AusbildungMitEinkommenOderErwerbstaetigRequiredNullFieldsConstraintValidator();

        Partner partner = new Partner();
        partner.setAusbildungMitEinkommenOderErwerbstaetig(false);
        partner.setFahrkosten(BigDecimal.ONE);

        assertThat(validator.isValid(partner, null), is(false));
    }

    @Test
    public void ausbildungMitEinkommenOderErwerbstaetigTrueAndJahreseinkommenFahrkostenVerpelgungskostenFieldsNullShouldNBValid() {
        AusbildungMitEinkommenOderErwerbstaetigRequiredNullFieldsConstraintValidator validator =
            new AusbildungMitEinkommenOderErwerbstaetigRequiredNullFieldsConstraintValidator();

        Partner partner = new Partner();
        partner.setAusbildungMitEinkommenOderErwerbstaetig(false);
        partner.setFahrkosten(null);
        partner.setJahreseinkommen(null);
        partner.setVerpflegungskosten(null);

        assertThat(validator.isValid(partner, null), is(true));
    }
}
