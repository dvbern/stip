package ch.dvbern.stip.api.gesuch.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class ValidateUpdateLegalityUtilTest {
    @CsvFileSource(resources = "data.csv", numLinesToSkip = 1)
    @ParameterizedTest
    void getAndValidateLegalityValueTest(String role, String dtoValue, String existingValue, String defaultValue, String expected) {
        assertThat(ValidateUpdateLegalityUtil.getAndValidateLegalityValue(Set.of(role),dtoValue,existingValue,defaultValue), is(expected));
    }
    @CsvFileSource(resources = "dataNullValues.csv", numLinesToSkip = 1)
    @ParameterizedTest
    void getAndValidateLegalityValueNoExistingValueTest(String role, String dtoValue, String defaultValue, String expected) {
        assertThat(ValidateUpdateLegalityUtil.getAndValidateLegalityValue(Set.of(role),dtoValue,null,defaultValue), is(expected));
    }
    @CsvFileSource(resources = "dataDefaultValues.csv", numLinesToSkip = 1)
    @ParameterizedTest
    void getAndValidateLegalityValueDefaultValueTest(String role,String defaultValue, String expected) {
        assertThat(ValidateUpdateLegalityUtil.getAndValidateLegalityValue(Set.of(role),null,null,defaultValue), is(expected));
    }
}
