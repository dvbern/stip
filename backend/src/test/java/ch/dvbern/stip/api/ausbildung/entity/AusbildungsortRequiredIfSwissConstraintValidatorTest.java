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

package ch.dvbern.stip.api.ausbildung.entity;

import java.util.stream.Stream;

import ch.dvbern.stip.api.land.entity.Land;
import lombok.Getter;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class AusbildungsortRequiredIfSwissConstraintValidatorTest {
    @Getter
    private static class TestParameter {
        private final Ausbildung ausbildung = new Ausbildung();
        private final Boolean expected;

        TestParameter(boolean isAusland, String ort, String plz, boolean hasLand, boolean expected) {
            ausbildung.setAusbildungsort(ort);
            ausbildung.setAusbildungsortPLZ(plz);
            ausbildung.setLand(hasLand ? new Land() : null);
            ausbildung.setIsAusbildungAusland(isAusland);
            this.expected = expected;
        }

        public String toString() {
            return String.format(
                "Ausbildung %s %s %s",
                getExpectedText(),
                getTypeText(),
                getSetValuesText()
            );
        }

        private String getTypeText() {
            return ausbildung.getIsAusbildungAusland() ? "im Ausland" : "in der Schweiz";
        }

        private String getSetValuesText() {
            final var ortPlzText =
                ausbildung.getAusbildungsort() == null || ausbildung.getAusbildungsortPLZ() == null ? "ohne Ort / PLZ"
                    : "mit Ort / PLZ";
            final var landText = ausbildung.getLand() == null ? "ohne Land" : "mit Land";
            return String.format("%s und %s", ortPlzText, landText);
        }

        private String getExpectedText() {
            return expected ? "ist gültig" : "ist ungültig";
        }
    }

    private static class RequiredFieldsIfAusbildungIsAusland implements ArgumentsProvider {
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                // With Ort / PLZ
                Arguments.of(new TestParameter(false, "Bern", "3011", false, true)),
                Arguments.of(new TestParameter(true, "Bern", "3011", false, false)),
                // With Land
                Arguments.of(new TestParameter(false, null, null, true, false)),
                Arguments.of(new TestParameter(true, null, null, true, true)),
                // Both Values set
                Arguments.of(new TestParameter(true, "Bern", "3011", true, false)),
                Arguments.of(new TestParameter(false, "Bern", "3011", true, false))
            );
        }
    }

    @ParameterizedTest
    @ArgumentsSource(RequiredFieldsIfAusbildungIsAusland.class)
    void testValidCombinations(TestParameter parameter) {
        final var validator = new ch.dvbern.stip.api.ausbildung.entity.RequiredFieldsIfAusbildungIsAusland();

        assertThat(
            parameter.toString(),
            validator.isValid(parameter.getAusbildung(), null),
            is(parameter.getExpected())
        );
    }
}
