/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

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
