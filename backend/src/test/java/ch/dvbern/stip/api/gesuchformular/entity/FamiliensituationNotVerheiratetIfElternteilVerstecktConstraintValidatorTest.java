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

import java.util.Set;

import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class FamiliensituationNotVerheiratetIfElternteilVerstecktConstraintValidatorTest {
    private FamiliensituationNotVerheiratetIfElternteilVerstecktConstraintValidator validator;

    @BeforeEach
    void setup() {
        validator = new FamiliensituationNotVerheiratetIfElternteilVerstecktConstraintValidator();
    }

    @Test
    void emptyElternteileAndNullIsValid() {
        final var gesuchFormular = setupWithVersteckteEltern();

        assertThat(validator.isValid(gesuchFormular, null), is(true));
    }

    @Test
    void verstecktesElternteilAndNullIsValid() {
        final var gesuchFormular = setupWithVersteckteEltern(ElternTyp.VATER);

        assertThat(validator.isValid(gesuchFormular, null), is(true));
    }

    @Test
    void verstecktesElternteilAndVerheiratetIsInvalid() {
        final var gesuchFormular = setupWithVersteckteEltern(ElternTyp.VATER)
            .setFamiliensituation(new Familiensituation().setElternVerheiratetZusammen(true));

        assertThat(validator.isValid(gesuchFormular, null), is(false));
    }

    @Test
    void verstecktesElternteilAndNotVerheiratetIsValid() {
        final var gesuchFormular = setupWithVersteckteEltern(ElternTyp.VATER)
            .setFamiliensituation(new Familiensituation().setElternVerheiratetZusammen(false));

        assertThat(validator.isValid(gesuchFormular, null), is(true));
    }

    private GesuchFormular setupWithVersteckteEltern(ElternTyp... eltern) {
        return new GesuchFormular().setVersteckteEltern(Set.of(eltern));
    }
}
