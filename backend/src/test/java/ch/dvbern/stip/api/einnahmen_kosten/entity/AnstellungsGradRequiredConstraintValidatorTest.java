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

package ch.dvbern.stip.api.einnahmen_kosten.entity;

import ch.dvbern.stip.api.common.validation.ValidationsConstant;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class AnstellungsGradRequiredConstraintValidatorTest {
    private Validator validator;
    private EinnahmenKosten einnahmenKosten;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        einnahmenKosten = new EinnahmenKosten();
        einnahmenKosten.setFahrkosten(0);
        einnahmenKosten.setRenten(0);
        einnahmenKosten.setSteuerjahr(2023);
    }

    @Test
    void whenEinkommenPositive_anstellungsgradShouldBeRequired() {
        einnahmenKosten.setNettoerwerbseinkommen(2);
        var violations = validator.validate(einnahmenKosten);

        assertThat(violations.size(), is(1));
        violations.forEach(
            violation -> assertThat(
                violation.getMessageTemplate(),
                is(ValidationsConstant.VALIDATION_EINNAHME_KOSTEN_ANSTELLUNGSGRAD_MESSAGE)
            )
        );
    }

    @Test
    void whenEinkommenIsZero_anstellungsgradShouldBeOptionall() {
        einnahmenKosten.setNettoerwerbseinkommen(0);
        var violations = validator.validate(einnahmenKosten);
        assertThat(violations.isEmpty(), is(true));
    }
}
