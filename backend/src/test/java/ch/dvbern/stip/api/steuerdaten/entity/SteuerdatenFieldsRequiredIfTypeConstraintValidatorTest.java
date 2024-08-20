package ch.dvbern.stip.api.steuerdaten.entity;

import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class SteuerdatenFieldsRequiredIfTypeConstraintValidatorTest {
    private SteuerdatenFieldsRequiredIfTypeConstraintValidator validator;

    @BeforeEach
    void setup() {
        validator = new SteuerdatenFieldsRequiredIfTypeConstraintValidator();
    }

    @Test
    void testSozialhilfebeitraege() {
        // Arrange
        final var steuerdaten = new Steuerdaten()
            .setSteuerdatenTyp(SteuerdatenTyp.FAMILIE)
            .setErgaenzungsleistungenPartner(100);

        // Act
        var isValid = validator.isValid(steuerdaten, null);

        // Assert
        assertThat(isValid, is(false));

        // Rearrange
        steuerdaten.setSozialhilfebeitraegePartner(100);

        // Act
        isValid = validator.isValid(steuerdaten, null);

        // Assert
        assertThat(isValid, is(true));
    }

    @Test
    void testErgaenzungsleistungen() {
        // Arrange
        final var steuerdaten = new Steuerdaten()
            .setSteuerdatenTyp(SteuerdatenTyp.FAMILIE)
            .setSozialhilfebeitraegePartner(100);

        // Act
        var isValid = validator.isValid(steuerdaten, null);

        // Assert
        assertThat(isValid, is(false));

        // Rearrange
        steuerdaten.setErgaenzungsleistungenPartner(100);

        // Act
        isValid = validator.isValid(steuerdaten, null);

        // Assert
        assertThat(isValid, is(true));
    }

    @Test
    void testIfNotFamilie() {
        // Arrange
        final var steuerdaten = new Steuerdaten()
            .setSteuerdatenTyp(SteuerdatenTyp.MUTTER);

        // Act
        var isValid = validator.isValid(steuerdaten, null);

        // Assert
        assertThat(isValid, is(true));
    }
}
