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

import ch.dvbern.stip.api.common.validation.ValidationsConstant;
import ch.dvbern.stip.api.util.TestUtil;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AusbildungsPensumRequiredConstraintValidatorTest {
    private Partner partner;
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        partner = TestUtil.getFullGesuch().getGesuchTranchen().get(0).getGesuchFormular().getPartner();
    }

    @Test
    public void AusbildungsPensumRequired_ifIsInAusbildung() {
        partner.setInAusbildung(true);
        var violations = validator.validate(partner);
        assertThat(violations.size(), is(1));
        violations.forEach(
            violation -> assertThat(
                violation.getMessageTemplate(),
                is(ValidationsConstant.VALIDATION_PARTNER_AUSBILDUNGS_PENSUM_MESSAGE)
            )
        );
    }

    @Test

    public void AusbildungsPensumOptional_ifIsNOTInAusbildung() {
        partner.setInAusbildung(false);
        var violations = validator.validate(partner);
        assertTrue(violations.isEmpty());
    }

}
