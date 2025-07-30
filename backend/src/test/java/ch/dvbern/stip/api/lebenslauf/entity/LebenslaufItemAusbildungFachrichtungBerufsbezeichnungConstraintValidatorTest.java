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

package ch.dvbern.stip.api.lebenslauf.entity;

import java.util.List;

import ch.dvbern.stip.api.ausbildung.entity.Abschluss;
import ch.dvbern.stip.api.ausbildung.type.AbschlussZusatzfrage;
import ch.dvbern.stip.api.util.TestUtil;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class LebenslaufItemAusbildungFachrichtungBerufsbezeichnungConstraintValidatorTest {

    LebenslaufItemAusbildungFachrichtungBerufsbezeichnungConstraintValidator validator =
        new LebenslaufItemAusbildungFachrichtungBerufsbezeichnungConstraintValidator();

    @NotNull
    private static List<Abschluss> getAbschlusseWithZusatzfrage() {
        return List.of(
            new Abschluss().setZusatzfrage(AbschlussZusatzfrage.BERUFSBEZEICHNUNG),
            new Abschluss().setZusatzfrage(AbschlussZusatzfrage.FACHRICHTUNG)
        );
    }

    @Test
    void shouldBeValidIfBildungsartNullAndBerufsbezeichnungNull() {
        LebenslaufItem lebenslaufItem = new LebenslaufItem()
            .setAbschluss(null)
            .setFachrichtungBerufsbezeichnung(null);

        assertThat(validator.isValid(lebenslaufItem, TestUtil.initValidatorContext()), is(true));
    }

    @Test
    void shouldNotBeValidIfZusatzfrageAndFachrichtungBerufsbezeichnungNull() {
        getAbschlusseWithZusatzfrage().forEach(
            abschluss -> {
                LebenslaufItem lebenslaufItem = new LebenslaufItem()
                    .setAbschluss(abschluss)
                    .setFachrichtungBerufsbezeichnung(null);
                assertThat(validator.isValid(lebenslaufItem, TestUtil.initValidatorContext()), is(false));
            }
        );
    }

    @Test
    void shouldBeValidIfZusatzfrageAndFachrichtungBerufsbezeichnungNotNull() {
        getAbschlusseWithZusatzfrage().forEach(
            abschluss -> {
                LebenslaufItem lebenslaufItem = new LebenslaufItem()
                    .setAbschluss(abschluss)
                    .setFachrichtungBerufsbezeichnung("FachrichtungBerufsbezeichnung");
                assertThat(validator.isValid(lebenslaufItem, TestUtil.initValidatorContext()), is(true));
            }
        );
    }

    @Test
    void shouldNotBeValidIfNotZusatzfrageAndFachrichtungBerufsbezeichnungNotNull() {
        LebenslaufItem lebenslaufItem = new LebenslaufItem()
            .setAbschluss(new Abschluss())
            .setFachrichtungBerufsbezeichnung("FachrichtungBerufsbezeichnung");
        assertThat(validator.isValid(lebenslaufItem, TestUtil.initValidatorContext()), is(false));
    }
}
