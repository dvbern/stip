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
import java.util.UUID;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static ch.dvbern.stip.api.generator.entities.GesuchGenerator.initGesuch;

class GesuchTest {

    @Test
    void getTrancheByIdNotPresent() {
        Gesuch gesuch = initGesuch();
        MatcherAssert.assertThat(gesuch.getGesuchTrancheById(UUID.randomUUID()).isPresent(), Matchers.is(false));
    }

    @Test
    void getTrancheByIdPresent() {
        UUID testId = UUID.randomUUID();
        Gesuch gesuch = initGesuch();
        gesuch.getGesuchTranchen().get(0).setId(testId);

        MatcherAssert.assertThat(gesuch.getGesuchTrancheById(testId).isPresent(), Matchers.is(true));
    }

    @Test
    void getTrancheByIdPresentMultipleTranchen() {
        UUID testId = UUID.randomUUID();
        Gesuch gesuch = initGesuch();
        gesuch.getGesuchTranchen().add((GesuchTranche) new GesuchTranche().setId(testId));
        MatcherAssert.assertThat(gesuch.getGesuchTrancheById(testId).isPresent(), Matchers.is(true));
    }

    @Test
    void getTrancheByDateNotPresent() {
        var gesuch = initGesuch();
        LocalDate ausserhalbPeriode = gesuch.getGesuchsperiode().getGesuchsperiodeStopp().plusDays(1);
        MatcherAssert.assertThat(gesuch.getAllTranchenValidOnDate(ausserhalbPeriode).isPresent(), Matchers.is(false));
    }

    @Test
    void getTrancheByDatePresent() {
        Gesuch gesuch = initGesuch();
        LocalDate innerhalbPeriode = gesuch.getGesuchsperiode().getGesuchsperiodeStopp();
        MatcherAssert.assertThat(gesuch.getAllTranchenValidOnDate(innerhalbPeriode).isPresent(), Matchers.is(true));
    }
}
