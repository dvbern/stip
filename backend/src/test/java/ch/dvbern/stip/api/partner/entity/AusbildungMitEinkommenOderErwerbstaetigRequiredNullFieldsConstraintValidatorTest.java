package ch.dvbern.stip.api.partner.entity;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AusbildungMitEinkommenOderErwerbstaetigRequiredNullFieldsConstraintValidatorTest {

    @Test
    public void ausbildungMitEinkommenOderErwerbstaetigTrueAndJahreseinkommenFieldNotNullShouldNotBeValid() {
        AusbildungMitEinkommenOderErwerbstaetigRequiredNullFieldsConstraintValidator validator =
            new AusbildungMitEinkommenOderErwerbstaetigRequiredNullFieldsConstraintValidator();

        Partner partner = new Partner();
        partner.setAusbildungMitEinkommenOderErwerbstaetig(false);
        partner.setJahreseinkommen(1);

        assertThat(validator.isValid(partner, null), is(false));
    }

    @Test
    public void ausbildungMitEinkommenOderErwerbstaetigTrueAndVerpflegungskostenFieldSetShouldNotBeValid() {
        AusbildungMitEinkommenOderErwerbstaetigRequiredNullFieldsConstraintValidator validator =
            new AusbildungMitEinkommenOderErwerbstaetigRequiredNullFieldsConstraintValidator();

        Partner partner = new Partner();
        partner.setAusbildungMitEinkommenOderErwerbstaetig(false);
        partner.setVerpflegungskosten(1);

        assertThat(validator.isValid(partner, null), is(false));
    }

    @Test
    public void ausbildungMitEinkommenOderErwerbstaetigTrueAndFahrkostenFieldSetShouldNotBeValid() {
        AusbildungMitEinkommenOderErwerbstaetigRequiredNullFieldsConstraintValidator validator =
            new AusbildungMitEinkommenOderErwerbstaetigRequiredNullFieldsConstraintValidator();

        Partner partner = new Partner();
        partner.setAusbildungMitEinkommenOderErwerbstaetig(false);
        partner.setFahrkosten(1);

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
