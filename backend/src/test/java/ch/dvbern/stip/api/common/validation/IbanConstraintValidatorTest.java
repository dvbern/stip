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
