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

import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.kind.entity.Kind;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class BetreuungskostenRequiredConstraintValidatorTest {
    @Test
    void isValidTest() {
        final var validator = new EinnahmenKostenBetreuungskostenRequiredConstraintValidator();

        final var formular = new GesuchFormular()
            .setKinds(Set.of(new Kind()))
            .setEinnahmenKosten(new EinnahmenKosten());

        assertThat(validator.isValid(formular, null), is(false));

        formular.setEinnahmenKosten(new EinnahmenKosten().setBetreuungskostenKinder(1));

        assertThat(validator.isValid(formular, null), is(true));
    }
}
