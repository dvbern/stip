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

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.core.Is.is;

@RequiredArgsConstructor
class DarlehenFieldsRequiredConstraintValidatorTest {

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
        darlehen = new Darlehen();
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
    void atLeastOneReasonShouldBeSelectedTest() {
        darlehen.setWillDarlehen(true);
        assertThat(darlehenConstraintValidator.isValid(darlehen, null)).isFalse();
        darlehen.setGrundHoheGebuehren(true);
        assertThat(darlehenConstraintValidator.isValid(darlehen, null)).isTrue();
    }

    @Test
    void testWillDarlehenValidationTest() {
        darlehen.setWillDarlehen(true);
        darlehen.setGrundHoheGebuehren(true);
        assertThat(darlehenConstraintValidator.isValid(darlehen, null)).isTrue();
        darlehen.setWillDarlehen(false);
        assertThat(darlehenConstraintValidator.isValid(darlehen, null)).isTrue();

        // test empty values
        darlehen = new Darlehen();
        darlehen.setWillDarlehen(true);
        assertThat(darlehenConstraintValidator.isValid(darlehen, null)).isFalse();
        darlehen.setWillDarlehen(false);
        assertThat(darlehenConstraintValidator.isValid(darlehen, null)).isTrue();
    }

    @Test
    void avoidNegativeValuesTest() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        darlehen.setWillDarlehen(true);
        darlehen.setBetragDarlehen(-1);
        darlehen.setSchulden(-1);
        darlehen.setBetragBezogenKanton(-1);
        darlehen.setAnzahlBetreibungen(-1);
        darlehen.setGrundHoheGebuehren(true);
        var violations = validator.validate(darlehen);
        MatcherAssert.assertThat(violations.size(), is(4));
        darlehen.setBetragDarlehen(0);
        darlehen.setSchulden(0);
        darlehen.setBetragBezogenKanton(0);
        darlehen.setAnzahlBetreibungen(0);
        violations = validator.validate(darlehen);
        MatcherAssert.assertThat(violations.size(), is(0));
    }
}
