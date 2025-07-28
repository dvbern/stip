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

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import ch.dvbern.stip.api.ausbildung.entity.Abschluss;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LebenslaufAusbildungUeberschneidenConstraintValidatorTest {

    @Test
    void isValidTest() {
        LebenslaufAusbildungUeberschneidenConstraintValidator lebenslaufAusbildungUeberschneidenConstraintValidator =
            new LebenslaufAusbildungUeberschneidenConstraintValidator();
        GesuchFormular gesuchFormular = new GesuchFormular();
        Set<LebenslaufItem> lebenslaufItemSet = new HashSet<>();
        lebenslaufItemSet.add(
            createAusibldungLebenslaufItemWithDate(
                LocalDate.of(2000, 1, 1),
                LocalDate.of(2001, 7, 30)
            )
        );
        lebenslaufItemSet.add(
            createAusibldungLebenslaufItemWithDate(
                LocalDate.of(2001, 7, 31),
                LocalDate.of(2002, 7, 31)
            )
        );
        gesuchFormular.setLebenslaufItems(lebenslaufItemSet);
        assertThat(lebenslaufAusbildungUeberschneidenConstraintValidator.isValid(gesuchFormular, null)).isTrue();
        lebenslaufItemSet.add(
            createAusibldungLebenslaufItemWithDate(
                LocalDate.of(2002, 7, 31),
                LocalDate.of(2003, 7, 31)
            )
        );
        gesuchFormular.setLebenslaufItems(lebenslaufItemSet);
        assertThat(
            lebenslaufAusbildungUeberschneidenConstraintValidator.isValid(
                gesuchFormular,
                null
            )
        ).isFalse();
    }

    private LebenslaufItem createAusibldungLebenslaufItemWithDate(LocalDate von, LocalDate bis) {
        LebenslaufItem lebenslaufItem = new LebenslaufItem();
        lebenslaufItem.setVon(von);
        lebenslaufItem.setBis(bis);
        lebenslaufItem.setAbschluss(new Abschluss());
        return lebenslaufItem;
    }
}
