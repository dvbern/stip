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

package ch.dvbern.stip.berechnung.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.util.TestUtil;
import io.quarkus.test.junit.QuarkusTest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@RequiredArgsConstructor
@Slf4j
class BerechnungServiceTest {

    @Test
    void wasEingereichtAfterDueDateFalseTest() {
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());
        final var now = LocalDate.now();

        gesuch.getGesuchsperiode().setEinreichefristNormal(now);
        gesuch.setEinreichedatum(now);

        final var wasEingereichtAfterDueDate = DateUtil.wasEingereichtAfterDueDate(gesuch);
        assertThat(wasEingereichtAfterDueDate, is(false));
    }

    @Test
    void wasEingereichtAfterDueDateTrueTest() {
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());
        gesuch.getGesuchsperiode().setEinreichefristNormal(LocalDate.now());

        final var eingereicht = LocalDate.now().plusDays(1);
        final var wasEingereichtAfterDueDate = DateUtil.wasEingereichtAfterDueDate(gesuch);
        assertThat(wasEingereichtAfterDueDate, is(true));
    }

    @Test
    void getActualDurationRoundDownTest() {
        final var monthsToBeBetween = 7;
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());
        final var currentGesuchTranche = gesuch.getGesuchTranchen().get(0).setTyp(GesuchTrancheTyp.TRANCHE);
        currentGesuchTranche
            .setGueltigkeit(
                new DateRange().setGueltigBis(LocalDate.now().plusMonths(monthsToBeBetween - 2).withDayOfMonth(1))
            );
        gesuch.setGesuchTranchen(
            List.of(
                currentGesuchTranche,
                (GesuchTranche) new GesuchTranche()
                    .setGesuchFormular(
                        new GesuchFormular()
                            .setPersonInAusbildung(
                                new PersonInAusbildung()
                            )
                    )
                    .setTyp(GesuchTrancheTyp.TRANCHE)
                    .setGueltigkeit(
                        new DateRange().setGueltigBis(LocalDate.now().plusMonths(monthsToBeBetween).withDayOfMonth(1))
                    )
                    .setId(UUID.randomUUID()),
                (GesuchTranche) new GesuchTranche()
                    .setGesuchFormular(
                        new GesuchFormular()
                            .setPersonInAusbildung(
                                new PersonInAusbildung()
                            )
                    )
                    .setTyp(GesuchTrancheTyp.AENDERUNG)
                    .setGueltigkeit(
                        new DateRange()
                            .setGueltigBis(LocalDate.now().plusMonths(monthsToBeBetween - 4).withDayOfMonth(1))
                    )
                    .setId(UUID.randomUUID()),
                (GesuchTranche) new GesuchTranche()
                    .setGesuchFormular(
                        new GesuchFormular()
                            .setPersonInAusbildung(
                                new PersonInAusbildung()
                            )
                    )
                    .setTyp(GesuchTrancheTyp.AENDERUNG)
                    .setGueltigkeit(
                        new DateRange()
                            .setGueltigBis(LocalDate.now().plusMonths(monthsToBeBetween + 2).withDayOfMonth(1))
                    )
                    .setId(UUID.randomUUID())
            )
        );
        final var eingereicht = LocalDate.now().withDayOfMonth(1);
        gesuch.setEinreichedatum(eingereicht);

        final var monthsBetween = DateUtil.getStipendiumDurationRoundDown(gesuch);
        assertThat(monthsBetween, equalTo(monthsToBeBetween));
    }

    @Test
    void getActualDurationRoundUpTest() {
        final var monthsToBeBetween = 7;
        final var gesuch = TestUtil.getBaseGesuchForBerechnung(UUID.randomUUID());
        final var currentGesuchTranche = gesuch.getGesuchTranchen().get(0).setTyp(GesuchTrancheTyp.TRANCHE);
        currentGesuchTranche
            .setGueltigkeit(
                new DateRange().setGueltigBis(LocalDate.now().plusMonths(monthsToBeBetween - 2).withDayOfMonth(1))
            );
        gesuch.setGesuchTranchen(
            List.of(
                currentGesuchTranche,
                (GesuchTranche) new GesuchTranche()
                    .setGesuchFormular(
                        new GesuchFormular()
                            .setPersonInAusbildung(
                                new PersonInAusbildung()
                            )
                    )
                    .setTyp(GesuchTrancheTyp.TRANCHE)
                    .setGueltigkeit(
                        new DateRange().setGueltigBis(LocalDate.now().plusMonths(monthsToBeBetween).withDayOfMonth(1))
                    )
                    .setId(UUID.randomUUID()),
                (GesuchTranche) new GesuchTranche()
                    .setGesuchFormular(
                        new GesuchFormular()
                            .setPersonInAusbildung(
                                new PersonInAusbildung()
                            )
                    )
                    .setTyp(GesuchTrancheTyp.AENDERUNG)
                    .setGueltigkeit(
                        new DateRange()
                            .setGueltigBis(LocalDate.now().plusMonths(monthsToBeBetween - 4).withDayOfMonth(1))
                    )
                    .setId(UUID.randomUUID()),
                (GesuchTranche) new GesuchTranche()
                    .setGesuchFormular(
                        new GesuchFormular()
                            .setPersonInAusbildung(
                                new PersonInAusbildung()
                            )
                    )
                    .setTyp(GesuchTrancheTyp.AENDERUNG)
                    .setGueltigkeit(
                        new DateRange()
                            .setGueltigBis(LocalDate.now().plusMonths(monthsToBeBetween + 2).withDayOfMonth(1))
                    )
                    .setId(UUID.randomUUID())
            )
        );
        final var eingereicht = LocalDate.now().withDayOfMonth(27);
        gesuch.setEinreichedatum(eingereicht);

        final var monthsBetween = DateUtil.getStipendiumDurationRoundDown(gesuch);
        assertThat(monthsBetween, equalTo(monthsToBeBetween - 1));
    }

}
