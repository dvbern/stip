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

package ch.dvbern.stip.api.darlehen.entity;

import java.time.LocalDate;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class DarlehenFieldsRequiredConstraintValidatorTest {

    private GesuchFormular gesuchFormular;
    private Darlehen darlehen;
    private DarlehenConstraintValidator darlehenConstraintValidator;

    private static GesuchFormular prepareGesuchFormularMitDarlehen() {
        GesuchFormular gesuchFormular = new GesuchFormular();
        PersonInAusbildung personInAusbildung = new PersonInAusbildung();
        gesuchFormular.setPersonInAusbildung(personInAusbildung);
        gesuchFormular.setDarlehen(new Darlehen());
        gesuchFormular.setTranche(new GesuchTranche().setGesuch(new Gesuch().setAusbildung(new Ausbildung())));
        return gesuchFormular;
    }

    @BeforeEach
    void setUp() {
        gesuchFormular = prepareGesuchFormularMitDarlehen();
        darlehen = gesuchFormular.getDarlehen();
        darlehen.setGrundAnschaffungenFuerAusbildung(false);
        darlehen.setGrundNichtBerechtigt(false);
        darlehen.setGrundZweitausbildung(false);
        darlehen.setGrundHoheGebuehren(false);
        darlehen.setGrundAusbildungZwoelfJahre(false);
        darlehen.setBetragDarlehen(0);
        darlehen.setSchulden(0);
        darlehen.setBetragBezogenKanton(0);
        darlehen.setAnzahlBetreibungen(0);

        darlehenConstraintValidator = new DarlehenConstraintValidator();
    }

    @Test
    void testDarlehenRequiredAgeOfPiaConstraintValidator() {
        // Geburtsdatum null soll keine Validation Fehler verfen als nicht validbar
        darlehen.setWillDarlehen(true);
        assertThat(darlehenConstraintValidator.isValid(gesuchFormular, null))
            .isFalse();
        darlehen.setWillDarlehen(false);
        assertThat(darlehenConstraintValidator.isValid(gesuchFormular, null))
            .isTrue();

        // Minderjaehrig
        darlehen.setWillDarlehen(true);
        gesuchFormular.getPersonInAusbildung().setGeburtsdatum(LocalDate.now().minusYears(17));
        assertThat(darlehenConstraintValidator.isValid(gesuchFormular, null))
            .isFalse();
        darlehen.setWillDarlehen(false);
        assertThat(darlehenConstraintValidator.isValid(gesuchFormular, null))
            .isTrue();

        // Volljaehrig mit Darlehen Antwort
        darlehen.setWillDarlehen(true);
        gesuchFormular.getPersonInAusbildung().setGeburtsdatum(LocalDate.now().minusYears(18));
        assertThat(darlehenConstraintValidator.isValid(gesuchFormular, null))
            .isTrue();

        // Volljaehrig ohne darlehen Antwort
        darlehen.setWillDarlehen(false);
        assertThat(darlehenConstraintValidator.isValid(gesuchFormular, null))
            .isTrue();
    }

    @Test
    void testWillDarlehenValidationTest() {
        // test with pre filled values & pia >= 18 years old
        gesuchFormular.getPersonInAusbildung().setGeburtsdatum(LocalDate.now().minusYears(18));
        darlehen.setWillDarlehen(true);
        assertThat(darlehenConstraintValidator.isValid(gesuchFormular, null)).isTrue();
        darlehen.setWillDarlehen(false);
        assertThat(darlehenConstraintValidator.isValid(gesuchFormular, null)).isTrue();

        // test empty values
        gesuchFormular.setDarlehen(new Darlehen());
        gesuchFormular.getDarlehen().setWillDarlehen(true);
        assertThat(darlehenConstraintValidator.isValid(gesuchFormular, null)).isFalse();
        gesuchFormular.getDarlehen().setWillDarlehen(false);
        assertThat(darlehenConstraintValidator.isValid(gesuchFormular, null)).isTrue();
    }
}
