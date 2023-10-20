package ch.dvbern.stip.test.common.validation;

import ch.dvbern.stip.api.common.validation.IbanConstraintValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ch.dvbern.stip.test.util.TestConstants.IBAN_CH_NUMMER_VALID;
import static ch.dvbern.stip.test.util.TestConstants.IBAN_LI_NUMMER_VALID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class IbanConstraintValidatorTest {
    private IbanConstraintValidator ibanValidator;

    @BeforeEach
    void setup() {
        ibanValidator = new IbanConstraintValidator();
    }

    @Test
    void testIbanValid() {
        assertThat(ibanValidator.isValid(IBAN_CH_NUMMER_VALID, null), is(true));
        assertThat(ibanValidator.isValid(IBAN_LI_NUMMER_VALID, null), is(true));
    }

    @Test
    void testNonSwissIbanInvalid() {
        assertThat(ibanValidator.isValid("LV97HABA0012345678910", null), is(false));
        assertThat(ibanValidator.isValid("AT483200000012345864", null), is(false));
    }

    @Test
    void testEmptyIbanInvalid() {
        assertThat(ibanValidator.isValid("", null), is(false));
    }

    @Test
    void testChecksumInvalid() {
        assertThat(ibanValidator.isValid("CH0004835012345678009", null), is(false));
    }
}
