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

package ch.dvbern.stip.api.zahlungsverbindung.entity;

import ch.dvbern.stip.api.adresse.entity.Adresse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VornameNachnameOrInstitutionRequiredConstraintValidatorTest {

    private final VornameNachnameOrInstitutionRequiredConstraintValidator validator =
        new VornameNachnameOrInstitutionRequiredConstraintValidator();

    @Test
    void testValidWithVornameAndNachname() {
        Zahlungsverbindung zahlungsverbindung = new Zahlungsverbindung();
        zahlungsverbindung.setVorname("Max");
        zahlungsverbindung.setNachname("Mustermann");
        zahlungsverbindung.setAdresse(new Adresse());
        zahlungsverbindung.setIban("CH1234567890");

        assertTrue(validator.isValid(zahlungsverbindung, null));
    }

    @Test
    void testValidWithInstitution() {
        Zahlungsverbindung zahlungsverbindung = new Zahlungsverbindung();
        zahlungsverbindung.setInstitution("Test Institution");
        zahlungsverbindung.setAdresse(new Adresse());
        zahlungsverbindung.setIban("CH1234567890");

        assertTrue(validator.isValid(zahlungsverbindung, null));
    }

    @Test
    void testValidWithBothVornameNachnameAndInstitution() {
        Zahlungsverbindung zahlungsverbindung = new Zahlungsverbindung();
        zahlungsverbindung.setVorname("Max");
        zahlungsverbindung.setNachname("Mustermann");
        zahlungsverbindung.setInstitution("Test Institution");
        zahlungsverbindung.setAdresse(new Adresse());
        zahlungsverbindung.setIban("CH1234567890");

        assertTrue(validator.isValid(zahlungsverbindung, null));
    }

    @Test
    void testInvalidWithNoFields() {
        Zahlungsverbindung zahlungsverbindung = new Zahlungsverbindung();
        zahlungsverbindung.setAdresse(new Adresse());
        zahlungsverbindung.setIban("CH1234567890");

        assertFalse(validator.isValid(zahlungsverbindung, null));
    }

    @Test
    void testInvalidWithOnlyVorname() {
        Zahlungsverbindung zahlungsverbindung = new Zahlungsverbindung();
        zahlungsverbindung.setVorname("Max");
        zahlungsverbindung.setAdresse(new Adresse());
        zahlungsverbindung.setIban("CH1234567890");

        assertFalse(validator.isValid(zahlungsverbindung, null));
    }

    @Test
    void testInvalidWithOnlyNachname() {
        Zahlungsverbindung zahlungsverbindung = new Zahlungsverbindung();
        zahlungsverbindung.setNachname("Mustermann");
        zahlungsverbindung.setAdresse(new Adresse());
        zahlungsverbindung.setIban("CH1234567890");

        assertFalse(validator.isValid(zahlungsverbindung, null));
    }

    @Test
    void testInvalidWithEmptyStrings() {
        Zahlungsverbindung zahlungsverbindung = new Zahlungsverbindung();
        zahlungsverbindung.setVorname("");
        zahlungsverbindung.setNachname("");
        zahlungsverbindung.setInstitution("");
        zahlungsverbindung.setAdresse(new Adresse());
        zahlungsverbindung.setIban("CH1234567890");

        assertFalse(validator.isValid(zahlungsverbindung, null));
    }

    @Test
    void testInvalidWithBlankStrings() {
        Zahlungsverbindung zahlungsverbindung = new Zahlungsverbindung();
        zahlungsverbindung.setVorname("   ");
        zahlungsverbindung.setNachname("   ");
        zahlungsverbindung.setInstitution("   ");
        zahlungsverbindung.setAdresse(new Adresse());
        zahlungsverbindung.setIban("CH1234567890");

        assertFalse(validator.isValid(zahlungsverbindung, null));
    }

    @Test
    void testValidWithNullZahlungsverbindung() {
        assertTrue(validator.isValid(null, null));
    }
}
