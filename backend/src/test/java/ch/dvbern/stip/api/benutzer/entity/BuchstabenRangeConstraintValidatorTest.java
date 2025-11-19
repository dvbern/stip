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

package ch.dvbern.stip.api.benutzer.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Execution(ExecutionMode.CONCURRENT)
class BuchstabenRangeConstraintValidatorTest {

    @Test
    void testBuchstabenRangeConstraintValidator() {
        BuchstabenRangeConstraintValidator buchstabenRangeConstraintValidator =
            new BuchstabenRangeConstraintValidator();

        // Unvalid Buchstaben Range
        assertThat(buchstabenRangeConstraintValidator.isValid("D-B", null), is(false));
        assertThat(buchstabenRangeConstraintValidator.isValid("B-D,C,", null), is(false));
        assertThat(buchstabenRangeConstraintValidator.isValid("B-D,,", null), is(false));

        // Valid Buchstaben Range
        assertThat(buchstabenRangeConstraintValidator.isValid("B-D", null), is(true));
        assertThat(buchstabenRangeConstraintValidator.isValid("B-D,R-U", null), is(true));
        assertThat(buchstabenRangeConstraintValidator.isValid("B-D,A,G", null), is(true));
        assertThat(buchstabenRangeConstraintValidator.isValid("A", null), is(true));
        assertThat(buchstabenRangeConstraintValidator.isValid("A,B", null), is(true));
        assertThat(buchstabenRangeConstraintValidator.isValid("A,B,C", null), is(true));
        assertThat(buchstabenRangeConstraintValidator.isValid("A-D,E,F", null), is(true));
        assertThat(buchstabenRangeConstraintValidator.isValid("SAA-SOA", null), is(true));
    }
}
