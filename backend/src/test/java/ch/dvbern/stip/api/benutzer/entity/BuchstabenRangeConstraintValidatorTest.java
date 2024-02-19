package ch.dvbern.stip.api.benutzer.entity;

import ch.dvbern.stip.api.benutzer.entity.BuchstabenRangeConstraintValidator;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
    }
}
