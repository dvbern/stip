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

package ch.dvbern.stip.api.gesuch.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.lebenslauf.entity.LebenslaufItem;
import ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import org.junit.jupiter.api.Test;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class NoOverlapInAusbildungenConstraintValidatorTest {
    @Test
    void hasOverlap() {
        final var ausbildung = new Ausbildung()
            .setAusbildungBegin(LocalDate.now().minusMonths(2).with(firstDayOfMonth()))
            .setAusbildungEnd(LocalDate.now().plusMonths(2).with(lastDayOfMonth()));

        final var lebenslaufItems = new ArrayList<LebenslaufItem>();
        lebenslaufItems.add(
            new LebenslaufItem()
                .setBildungsart(LebenslaufAusbildungsArt.BACHELOR_FACHHOCHSCHULE)
                .setVon(LocalDate.now().minusMonths(1).with(firstDayOfMonth()))
                .setBis(LocalDate.now().plusMonths(1).with(lastDayOfMonth()))
        );

        final var validator = new NoOverlapInAusbildungenConstraintValidator();
        assertThat(validator.hasOverlap(ausbildung, lebenslaufItems.stream()), is(true));
    }

    @Test
    void hasNoOverlap() {
        final var ausbildung = new Ausbildung()
            .setAusbildungBegin(LocalDate.now().plusMonths(2).with(firstDayOfMonth()))
            .setAusbildungEnd(LocalDate.now().plusMonths(4).with(lastDayOfMonth()));

        final var lebenslaufItems = new ArrayList<LebenslaufItem>();
        lebenslaufItems.add(
            new LebenslaufItem()
                .setBildungsart(LebenslaufAusbildungsArt.BACHELOR_FACHHOCHSCHULE)
                .setVon(LocalDate.now().minusMonths(1).with(firstDayOfMonth()))
                .setBis(LocalDate.now().plusMonths(1).with(lastDayOfMonth()))
        );

        final var validator = new NoOverlapInAusbildungenConstraintValidator();
        assertThat(validator.hasOverlap(ausbildung, lebenslaufItems.stream()), is(false));
    }

    @Test
    void isValidTest() {
        final var pia = (PersonInAusbildung) new PersonInAusbildung()
            .setGeburtsdatum(LocalDate.of(2000, 1, 1));
        final var ausbildung = new Ausbildung()
            .setAusbildungBegin(LocalDate.of(2024, 1, 1))
            .setAusbildungEnd(LocalDate.of(2024, 4, 1).with(lastDayOfMonth()));
        final var gesuchFormular = new GesuchFormular()
            .setAusbildung(ausbildung)
            .setPersonInAusbildung(pia);

        // Create LebenslaufItem with overlap
        var lebenslaufItems = new HashSet<LebenslaufItem>();
        lebenslaufItems.add(
            new LebenslaufItem()
                .setBildungsart(LebenslaufAusbildungsArt.BACHELOR_FACHHOCHSCHULE)
                .setVon(LocalDate.of(2024, 2, 1))
                .setBis(LocalDate.of(2024, 3, 1).with(lastDayOfMonth()))
        );

        gesuchFormular.setLebenslaufItems(lebenslaufItems);

        final var validator = new NoOverlapInAusbildungenConstraintValidator();
        assertThat(validator.isValid(gesuchFormular, null), is(false));

        // Create LebenslaufItem without overlap
        lebenslaufItems = new HashSet<>();
        lebenslaufItems.add(
            new LebenslaufItem()
                .setBildungsart(LebenslaufAusbildungsArt.BACHELOR_FACHHOCHSCHULE)
                .setVon(LocalDate.of(2023, 2, 1))
                .setBis(LocalDate.of(2023, 3, 1).with(lastDayOfMonth()))
        );

        gesuchFormular.setLebenslaufItems(lebenslaufItems);
        assertThat(validator.isValid(gesuchFormular, null), is(true));
    }
}
