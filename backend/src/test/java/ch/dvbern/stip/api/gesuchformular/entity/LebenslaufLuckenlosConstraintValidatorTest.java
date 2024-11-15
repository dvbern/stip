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

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class LebenslaufLuckenlosConstraintValidatorTest {
    LebenslaufLuckenlosConstraintValidator lebenslaufLuckenlosConstraintValidator =
        new LebenslaufLuckenlosConstraintValidator();

    @NotNull
    private static LebenslaufItem createLebenslaufItem(LocalDate von, LocalDate bis) {
        return new LebenslaufItem()
            .setVon(von)
            .setBis(bis);
    }

    @Test
    void isLebenslaufLuckenlosOkTest() {
        GesuchFormular gesuchFormular = initFormular();
        LebenslaufItem lebenslaufItem = createLebenslaufItem(
            gesuchFormular.getPersonInAusbildung().getGeburtsdatum(),
            gesuchFormular.getAusbildung().getAusbildungBegin()
        );
        Set<LebenslaufItem> lebenslaufItemSet = new HashSet<>();
        lebenslaufItemSet.add(lebenslaufItem);
        gesuchFormular.setLebenslaufItems(lebenslaufItemSet);
        assertThat(lebenslaufLuckenlosConstraintValidator.isValid(gesuchFormular, null), is(true));
        final var lebenslaufItemZweiStart = LocalDate.of(2022, 11, 30);
        lebenslaufItem.setBis(lebenslaufItemZweiStart);
        LebenslaufItem lebenslaufItemZwei = createLebenslaufItem(
            lebenslaufItemZweiStart,
            gesuchFormular.getAusbildung().getAusbildungBegin()
        );
        lebenslaufItemSet.clear();
        lebenslaufItemSet.add(lebenslaufItem);
        lebenslaufItemSet.add(lebenslaufItemZwei);
        gesuchFormular.setLebenslaufItems(lebenslaufItemSet);
        assertThat(lebenslaufLuckenlosConstraintValidator.isValid(gesuchFormular, null), is(true));
    }

    @Test
    void lebenslaufLuckenlosStartZuFruehTest() {
        GesuchFormular gesuchFormular = initFormularWithLebenslaufItem(
            LocalDate.of(2000, 5, 11),
            LocalDate.of(2023, 12, 31)
        );
        assertThat(lebenslaufLuckenlosConstraintValidator.isValid(gesuchFormular, null), is(false));
    }

    @Test
    void lebenslaufLuckenlosStartZuSpaetTest() {
        GesuchFormular gesuchFormular = initFormularWithLebenslaufItem(
            LocalDate.of(2025, 10, 1),
            LocalDate.of(2027, 12, 31)
        );
        assertThat(lebenslaufLuckenlosConstraintValidator.isValid(gesuchFormular, null), is(false));
    }

    @Test
    void lebenslaufLuckenlosEndZuSpaetTest() {
        GesuchFormular gesuchFormular = initFormularWithLebenslaufItem(
            LocalDate.of(2017, 10, 1),
            LocalDate.of(2023, 12, 31)
        );
        assertThat(lebenslaufLuckenlosConstraintValidator.isValid(gesuchFormular, null), is(false));
    }

    @Test
    void lebenslaufLuckenlosStopZuSpaetTest() {
        GesuchFormular gesuchFormular = initFormularWithLebenslaufItem(
            LocalDate.of(2016, 8, 1),
            LocalDate.of(2024, 12, 31)
        );
        assertThat(lebenslaufLuckenlosConstraintValidator.isValid(gesuchFormular, null), is(false));
    }

    @Test
    void lebenslaufItemStopZuFruehTest() {
        GesuchFormular gesuchFormular = initFormularWithLebenslaufItem(
            LocalDate.of(2016, 8, 1),
            LocalDate.of(2022, 6, 30)
        );

        LebenslaufItem lebenslaufItemZwei = createLebenslaufItem(
            LocalDate.of(2022, 7, 1),
            LocalDate.of(2023, 11, 30)
        );
        gesuchFormular.getLebenslaufItems().add(lebenslaufItemZwei);

        assertThat(lebenslaufLuckenlosConstraintValidator.isValid(gesuchFormular, null), is(false));
    }

    @Test
    void isLebenslaufLuckenlosKoTest() {
        GesuchFormular gesuchFormular = initFormularWithLebenslaufItem(
            LocalDate.of(2016, 8, 1),
            LocalDate.of(2022, 6, 30)
        );

        LebenslaufItem lebenslaufItemZwei =
            createLebenslaufItem(
                LocalDate.of(2022, 8, 1),
                LocalDate.of(2023, 12, 31)
            );
        gesuchFormular.getLebenslaufItems().add(lebenslaufItemZwei);

        assertThat(lebenslaufLuckenlosConstraintValidator.isValid(gesuchFormular, null), is(false));
    }

    @Test
    void lebenslaufLuckenlosYoungerThan16OkTest() {
        GesuchFormular gesuchFormular = initFormular();
        gesuchFormular.getPersonInAusbildung()
            .setGeburtsdatum(gesuchFormular.getAusbildung().getAusbildungBegin().minusYears(15));
        assertThat(lebenslaufLuckenlosConstraintValidator.isValid(gesuchFormular, null), is(true));
    }

    @Test
    void lebenslaufLuckenlosStartsBarelyEarlyEnoughOkTest() {
        // Arrange
        GesuchFormular gesuchFormular = initFormular();
        gesuchFormular.getPersonInAusbildung()
            .setGeburtsdatum(
                LocalDate.of(2000, 1, 16)
            );
        gesuchFormular.getAusbildung()
            .setAusbildungBegin(
                LocalDate.of(2016, 7, 31)
            );

        // Act/Assert
        assertThat(lebenslaufLuckenlosConstraintValidator.isValid(gesuchFormular, null), is(true));

        // Arrange
        gesuchFormular.getPersonInAusbildung()
            .setGeburtsdatum(
                LocalDate.of(2000, 12, 16)
            );

        // Act/Assert
        assertThat(lebenslaufLuckenlosConstraintValidator.isValid(gesuchFormular, null), is(true));
    }

    @Test
    void lebenslaufLuckenlosStartsBarelyTooLateFailTest() {
        // Arrange
        GesuchFormular gesuchFormular = initFormular();
        gesuchFormular.getPersonInAusbildung()
            .setGeburtsdatum(
                LocalDate.of(2000, 1, 16)
            );
        gesuchFormular.getAusbildung()
            .setAusbildungBegin(
                LocalDate.of(2016, 8, 1)
            );

        // Act/Assert
        assertThat(lebenslaufLuckenlosConstraintValidator.isValid(gesuchFormular, null), is(false));

        // Arrange
        gesuchFormular.getPersonInAusbildung()
            .setGeburtsdatum(
                LocalDate.of(2000, 12, 16)
            );

        // Act/Assert
        assertThat(lebenslaufLuckenlosConstraintValidator.isValid(gesuchFormular, null), is(false));
    }

    @NotNull
    private GesuchFormular initFormularWithLebenslaufItem(LocalDate von, LocalDate bis) {
        Set<LebenslaufItem> lebenslaufItemSet = new HashSet<>();
        lebenslaufItemSet.add(createLebenslaufItem(von, bis));

        return initFormular().setLebenslaufItems(lebenslaufItemSet);
    }

    private GesuchFormular initFormular() {
        GesuchFormular gesuchFormular = new GesuchFormular();
        PersonInAusbildung personInAusbildung = new PersonInAusbildung();
        personInAusbildung.setGeburtsdatum(LocalDate.of(2000, 5, 12));
        gesuchFormular.setPersonInAusbildung(personInAusbildung);
        Ausbildung ausbildung = new Ausbildung();
        ausbildung.setAusbildungBegin(LocalDate.of(2024, 01, 01));
        gesuchFormular.setTranche(new GesuchTranche().setGesuch(new Gesuch().setAusbildung(ausbildung)));
        return gesuchFormular;
    }
}
