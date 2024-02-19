package ch.dvbern.stip.test.common.validation;

import ch.dvbern.stip.api.common.validation.AhvConstraintValidator;
import org.junit.jupiter.api.Test;

import static ch.dvbern.stip.test.util.TestConstants.AHV_NUMMER_VALID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class AhvConstraintValidatorTest {
    @Test
    void testAhvValidator() {
        AhvConstraintValidator ahvConstraintValidator = new AhvConstraintValidator();
        assertThat(ahvConstraintValidator.isValid(AHV_NUMMER_VALID, null), is(true));
        assertThat(ahvConstraintValidator.isValid("756.9217.0769.40", null), is(false));
        assertThat(ahvConstraintValidator.isValid("125.1000.0000.05", null), is(false));
        assertThat(ahvConstraintValidator.isValid("", null), is(false));
        assertThat(ahvConstraintValidator.isValid(null, null), is(false));
    }
}
