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

package ch.dvbern.stip.api.ausbildung.entity;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class AusbildungEndDateMustBeAfterStartConstraintValidatorTest {
    @Test
    void ausbildungEndDateMustBeAfterStart() {
        final var validator = new AusbildungEndDateMustBeAfterStartConstraintValidator();
        final var ausbildung = new Ausbildung()
            .setAusbildungBegin(LocalDate.now())
            .setAusbildungEnd(LocalDate.now().minusYears(1));

        assertThat(validator.isValid(ausbildung, null), is(false));

        ausbildung.setAusbildungEnd(LocalDate.now().plusYears(1));

        assertThat(validator.isValid(ausbildung, null), is(true));
    }
}
