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

package ch.dvbern.stip.api.common.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ch.dvbern.stip.api.util.TestConstants.IBAN_AT_NUMMER_VALID;
import static ch.dvbern.stip.api.util.TestConstants.IBAN_CH_NUMMER_INVALID;
import static ch.dvbern.stip.api.util.TestConstants.IBAN_CH_NUMMER_VALID;
import static ch.dvbern.stip.api.util.TestConstants.IBAN_LI_NUMMER_VALID;
import static ch.dvbern.stip.api.util.TestConstants.QR_IBAN_CH_INVALID;
import static ch.dvbern.stip.api.util.TestConstants.QR_IBAN_CH_VALID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class IbanConstraintValidatorTest {
    private IbanConstraintValidator ibanValidator;

    @BeforeEach
    void setup() {
        ibanValidator = new IbanConstraintValidator();
    }

    @Test
    void testIbanValid() {
        assertThat(ibanValidator.isValid(IBAN_CH_NUMMER_VALID, null), is(true));
    }

    @Test
    void testNonSwissIbanInvalid() {
        assertThat(ibanValidator.isValid(IBAN_LI_NUMMER_VALID, null), is(false));
        assertThat(ibanValidator.isValid(IBAN_AT_NUMMER_VALID, null), is(false));
    }

    @Test
    void testEmptyIbanInvalid() {
        assertThat(ibanValidator.isValid("", null), is(false));
    }

    @Test
    void testChecksumInvalid() {
        assertThat(ibanValidator.isValid(IBAN_CH_NUMMER_INVALID, null), is(false));
    }

    @Test
    void testValidQrIbanNotValid() {
        assertThat(ibanValidator.isValid(QR_IBAN_CH_VALID, null), is(false));
    }

    @Test
    void testInvalidQrIbanNotValid() {
        assertThat(ibanValidator.isValid(QR_IBAN_CH_INVALID, null), is(false));
    }
}
