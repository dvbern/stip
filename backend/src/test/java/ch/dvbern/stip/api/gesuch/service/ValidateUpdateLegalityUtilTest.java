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

package ch.dvbern.stip.api.gesuch.service;

import java.util.Set;

import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@Execution(ExecutionMode.CONCURRENT)
class ValidateUpdateLegalityUtilTest {
    @CsvSource(
        {
            // role, dtoValue, existingValue, defaultValue, expected
            "V0_Gesuchsteller,1,2,0,2",
            "V0_Sachbearbeiter,1,2,0,1",
            "V0_Sachbearbeiter-Admin,1,2,0,1",
        }
    )
    @ParameterizedTest
    void getAndValidateLegalityValueTest(
        final String role,
        final String dtoValue,
        final String existingValue,
        final String defaultValue,
        final String expected
    ) {
        assertThat(
            ValidateUpdateLegalityUtil.getAndValidateLegalityValue(
                Set.of(role),
                dtoValue,
                existingValue,
                defaultValue
            ),
            is(expected)
        );
    }

    @CsvSource(
        {
            // role,dtoValue,defaultValue,expected
            "V0_Gesuchsteller,1,0,0",
            "V0_Sachbearbeiter,1,0,1",
            "V0_Sachbearbeiter-Admin,1,0,1"
        }
    )
    @ParameterizedTest
    void getAndValidateLegalityValueNoExistingValueTest(
        final String role,
        final String dtoValue,
        final String defaultValue,
        final String expected
    ) {
        assertThat(
            ValidateUpdateLegalityUtil.getAndValidateLegalityValue(
                Set.of(role),
                dtoValue,
                null,
                defaultValue
            ),
            is(expected)
        );
    }

    @CsvSource(
        {
            // role,defaultValue,expected
            "V0_Gesuchsteller,0,0",
            "V0_Sachbearbeiter,0,0",
            "V0_Sachbearbeiter-Admin,0,0"
        }
    )
    @ParameterizedTest
    void getAndValidateLegalityValueDefaultValueTest(
        final String role,
        final String defaultValue,
        final String expected
    ) {
        assertThat(
            ValidateUpdateLegalityUtil.getAndValidateLegalityValue(
                Set.of(role),
                null,
                null,
                defaultValue
            ),
            is(expected)
        );
    }
}
