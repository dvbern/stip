package ch.dvbern.stip.api.gesuch.service;

import ch.dvbern.stip.api.benutzer.util.TestAsAdmin;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ValidateUpdateLegalityUtilTest {

    public static String[][] getTestData(){
        return new String[][]{
            // role, dtoValue, existingValue, defaultValue, expected
            {"Gesuchsteller",String.valueOf(1),String.valueOf(2),String.valueOf(3),String.valueOf(2)}
            //{"Sachbearbeiter","2012","",""},
            //{"Admin",null,null,null}
        };
    }

    @CsvFileSource(resources = "data.csv", numLinesToSkip = 1)
    @ParameterizedTest
    void getAndValidateLegalityValueTest(String role, String dtoValue, String existingValue, String defaultValue, String expected) {
        assertThat(ValidateUpdateLegalityUtil.getAndValidateLegalityValue(Set.of(role),dtoValue,existingValue,defaultValue), is(expected));
    }

}
