package ch.dvbern.stip.test.common.validation;

import ch.dvbern.stip.api.common.validation.IbanConstraintValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ch.dvbern.stip.test.util.TestConstants.*;
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
}
