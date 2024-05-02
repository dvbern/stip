package ch.dvbern.stip.api.partner.entity;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AusbildungMitEinkommenOderErwerbstaetigRequiredFieldsConstraintValidatorTest {

    @Test
    public void ausbildungMitEinkommenOderErwerbstaetigTrueAndJahreseinkommenFieldNullShouldNotBeValid() {
        AusbildungMitEinkommenOderErwerbstaetigRequiredFieldsConstraintValidator validator =
            new AusbildungMitEinkommenOderErwerbstaetigRequiredFieldsConstraintValidator();

        Partner partner = new Partner();
        partner.setAusbildungMitEinkommenOderErwerbstaetig(true);
        partner.setJahreseinkommen(null);

        assertThat(validator.isValid(partner, null), is(false));
    }

    @Test
    public void ausbildungMitEinkommenOderErwerbstaetigTrueAndVerpflegungskostenFieldNullShouldNotBeValid() {
        AusbildungMitEinkommenOderErwerbstaetigRequiredFieldsConstraintValidator validator =
            new AusbildungMitEinkommenOderErwerbstaetigRequiredFieldsConstraintValidator();

        Partner partner = new Partner();
        partner.setAusbildungMitEinkommenOderErwerbstaetig(true);
        partner.setVerpflegungskosten(null);

        assertThat(validator.isValid(partner, null), is(false));
    }

    @Test
    public void ausbildungMitEinkommenOderErwerbstaetigTrueAndFahrkostenFieldNullShouldNotBeValid() {
        AusbildungMitEinkommenOderErwerbstaetigRequiredFieldsConstraintValidator validator =
            new AusbildungMitEinkommenOderErwerbstaetigRequiredFieldsConstraintValidator();

        Partner partner = new Partner();
        partner.setAusbildungMitEinkommenOderErwerbstaetig(true);
        partner.setFahrkosten(null);

        assertThat(validator.isValid(partner, null), is(false));
    }

    @Test
    public void ausbildungMitEinkommenOderErwerbstaetigTrueAndJahreseinkommenFahrkostenVerpelgungskostenFieldSetShouldNotBeValid() {
        AusbildungMitEinkommenOderErwerbstaetigRequiredFieldsConstraintValidator validator =
            new AusbildungMitEinkommenOderErwerbstaetigRequiredFieldsConstraintValidator();

        Partner partner = new Partner();
        partner.setAusbildungMitEinkommenOderErwerbstaetig(true);
        partner.setFahrkosten(1);
        partner.setJahreseinkommen(1);
        partner.setVerpflegungskosten(1);

        assertThat(validator.isValid(partner, null), is(true));
    }
}
