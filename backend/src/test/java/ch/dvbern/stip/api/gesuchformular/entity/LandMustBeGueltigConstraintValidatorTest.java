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

package ch.dvbern.stip.api.gesuchformular.entity;

import java.lang.annotation.Annotation;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.gesuchformular.type.LandGueltigFor;
import ch.dvbern.stip.api.land.entity.Land;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class LandMustBeGueltigConstraintValidatorTest {
    @ParameterizedTest
    @EnumSource(LandGueltigFor.class)
    void testEmptyIsValid(final LandGueltigFor landGueltigFor) {
        // Arrange
        final var formular = new GesuchFormular();
        final var validator = getValidatorFor(landGueltigFor);

        // Act
        final var isValid = validator.isValid(formular, null);

        // Assert
        assertThat(isValid, is(true));
    }

    @Test
    void testPiaNationalitaet() {
        // Arrange
        final var formular = new GesuchFormular()
            .setPersonInAusbildung(new PersonInAusbildung().setNationalitaet(gueltig()));
        final var validator = getValidatorFor(LandGueltigFor.PERSON_IN_AUSBILDUNG_NATIONALITAET);

        // Act
        var isValid = validator.isValid(formular, null);

        // Assert
        assertThat(isValid, is(true));

        // Rearrange
        formular.getPersonInAusbildung().getNationalitaet().setGueltig(false);

        // Act
        isValid = validator.isValid(formular, null);

        // Assert
        assertThat(isValid, is(false));
    }

    @Test
    void testPiaAdresse() {
        // Arrange
        final var formular = new GesuchFormular()
            .setPersonInAusbildung(new PersonInAusbildung().setAdresse(new Adresse().setLand(gueltig())));
        final var validator = getValidatorFor(LandGueltigFor.PERSON_IN_AUSBILDUNG_ADRESSE);

        // Act
        var isValid = validator.isValid(formular, null);

        // Assert
        assertThat(isValid, is(true));

        // Rearrange
        formular.getPersonInAusbildung().getAdresse().getLand().setGueltig(false);

        // Act
        isValid = validator.isValid(formular, null);

        // Assert
        assertThat(isValid, is(false));
    }

    private static LandMustBeGueltigConstraintValidator getValidatorFor(final LandGueltigFor landGueltigFor) {
        final var validator = new LandMustBeGueltigConstraintValidator();
        validator.initialize(new MockLandMustBeGueltigConstraint(landGueltigFor));
        return validator;
    }

    private static Land gueltig() {
        return new Land().setGueltig(true);
    }

    @RequiredArgsConstructor
    private static final class MockLandMustBeGueltigConstraint implements LandMustBeGueltigConstraint {
        private final LandGueltigFor landGueltigFor;

        @Override
        public String message() {
            return "";
        }

        @Override
        public Class<?>[] groups() {
            return new Class[0];
        }

        @Override
        public LandGueltigFor landGueltigFor() {
            return landGueltigFor;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return null;
        }
    }
}
