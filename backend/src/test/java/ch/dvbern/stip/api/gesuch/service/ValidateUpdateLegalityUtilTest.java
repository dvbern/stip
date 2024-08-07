package ch.dvbern.stip.api.gesuch.service;

import java.util.Set;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class ValidateUpdateLegalityUtilTest {
    @CsvSource({
        //role, dtoValue, existingValue, defaultValue, expected
        "Gesuchsteller,1,2,0,2",
        "Sachbearbeiter,1,2,0,1",
        "Admin,1,2,0,1",
    })
    @ParameterizedTest
    void getAndValidateLegalityValueTest(
        final String role,
        final String dtoValue,
        final String existingValue,
        final String defaultValue,
        final String expected
    ) {
        assertThat(ValidateUpdateLegalityUtil.getAndValidateLegalityValue(Set.of(role),dtoValue,
            existingValue,defaultValue), is(expected));
    }
    @CsvSource({
        //role,dtoValue,defaultValue,expected
        "Gesuchsteller,1,0,0",
        "Sachbearbeiter,1,0,1",
        "Admin,1,0,1"
    })
    @ParameterizedTest
    void getAndValidateLegalityValueNoExistingValueTest(
        final String role,
        final String dtoValue,
        final String defaultValue,
        final String expected
    ) {
        assertThat(ValidateUpdateLegalityUtil.getAndValidateLegalityValue(Set.of(role),
            dtoValue,null,defaultValue), is(expected));
    }
    @CsvSource({
        //role,defaultValue,expected
        "Gesuchsteller,0,0",
        "Sachbearbeiter,0,0",
        "Admin,0,0"
    })
    @ParameterizedTest
    void getAndValidateLegalityValueDefaultValueTest(
        final String role,
        final String defaultValue,
        final String expected
    ) {
        assertThat(ValidateUpdateLegalityUtil.getAndValidateLegalityValue(Set.of(role),
            null,null,defaultValue), is(expected));
    }
}
