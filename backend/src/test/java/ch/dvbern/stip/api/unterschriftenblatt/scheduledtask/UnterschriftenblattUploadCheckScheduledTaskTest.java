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

package ch.dvbern.stip.api.unterschriftenblatt.scheduledtask;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.service.seeding.GesuchTestSeeding;
import ch.dvbern.stip.api.common.statemachines.gesuch.handlers.VerfuegungDruckbereitHandler;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.util.GesuchTestUtil;
import ch.dvbern.stip.api.gesuchsjahr.entity.Gesuchsjahr;
import ch.dvbern.stip.api.gesuchsperioden.service.GesuchsperiodenService;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.statusprotokoll.service.StatusprotokollService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@QuarkusTest
class UnterschriftenblattUploadCheckScheduledTaskTest {

    @Inject
    UnterschriftenblattUploadCheckScheduledTask scheduledTask;

    @InjectMock
    GesuchRepository gesuchRepository;

    @InjectMock
    GesuchsperiodenService gesuchsperiodenService;

    @InjectMock
    StatusprotokollService statusprotokollService;

    @InjectMock
    VerfuegungDruckbereitHandler verfuegungDruckbereitHandler;

    Gesuch gesuch;

    @BeforeEach
    void setUp() {
        gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.WARTEN_AUF_UNTERSCHRIFTENBLATT);
        gesuch.setGesuchStatusAenderungDatum(LocalDateTime.now().minusDays(2));

        var gesuchsjahr = new Gesuchsjahr();
        gesuchsjahr.setTechnischesJahr(2025);
        var gesuchperiode = GesuchTestSeeding.getGesuchsperiode(gesuchsjahr);
        gesuch.setGesuchsperiode(gesuchperiode);

        when(gesuchRepository.getAllWartenAufUnterschriftenblatt())
            .thenReturn(Stream.of(gesuch));
    }

    @Test
    void automaticChangeOfGesuchStatusToDruckbereit_shouldWork() {
        // act & assert
        assertDoesNotThrow(() -> scheduledTask.run());
        // todo KSTIP-2663 move call of addBerechnungsblattToDocument to another state transtition
        /*
         * try {
         * // verify that correct boolean value (addAllBerechnungsblaetter = false) has been passed
         * verify(berechnungsblattService, times(1))
         * .addBerechnungsblattToDocument(any(), any(), any(), org.mockito.ArgumentMatchers.eq(false));
         * } catch (IOException e) {
         * fail();
         * }
         *
         */
        // verify that the flag has been set to true & that gesuch is in correct state
        // todo KSTIP-2663 move gesuch.isVerfuegt() to another state transtition
        // assertThat(gesuch.isVerfuegt(), is(true));
        assertThat(gesuch.getGesuchStatus(), is(Gesuchstatus.VERFUEGUNG_DRUCKBEREIT));
    }

}
