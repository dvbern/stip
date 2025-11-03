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

package ch.dvbern.stip.api.sap.scheduledtask;

import java.time.LocalDate;
import java.util.List;

import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchsperioden.repo.GesuchsperiodeRepository;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.sap.service.SapService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class SapScheduledTaskFilterTest {
    @InjectSpy
    private final SapService sapService;

    @InjectMock
    GesuchRepository gesuchRepository;

    @InjectMock
    GesuchsperiodeRepository gesuchsperiodeRepository;

    @BeforeAll
    void setUp() {
        QuarkusMock.installMockForType(sapService, SapService.class);
        QuarkusMock.installMockForType(gesuchRepository, GesuchRepository.class);
        QuarkusMock.installMockForType(gesuchsperiodeRepository, GesuchsperiodeRepository.class);
    }

    @BeforeEach
    void setUpEach() {
        Mockito.doNothing().when(sapService).createRemainderAuszahlungOrGetStatus(any());
    }

    @Test
    void testProcessRemainderAuszahlungActions() {
        final var now = LocalDate.now();

        final var gesuchsperiode = new Gesuchsperiode();
        gesuchsperiode.setZweiterAuszahlungsterminTag(now.getDayOfMonth());
        gesuchsperiode.setZweiterAuszahlungsterminMonat(3);
        Mockito.when(gesuchsperiodeRepository.listAll()).thenReturn(List.of(gesuchsperiode));

        final var testGesuch = new Gesuch();
        testGesuch.setGesuchsperiode(gesuchsperiode);
        final var testGesuchTranche = new GesuchTranche();
        testGesuchTranche.setGueltigkeit(
            new DateRange().setGueltigAb(now.minusMonths(gesuchsperiode.getZweiterAuszahlungsterminMonat()))
        );
        testGesuch.setGesuchTranchen(List.of(testGesuchTranche));

        Mockito.when(gesuchRepository.findGesuchsByGesuchsperiodeIdWithPendingRemainderPayment(any()))
            .thenReturn(List.of(testGesuch));
        sapService.processRemainderAuszahlungActions();
        Mockito.verify(sapService, Mockito.times(1)).createRemainderAuszahlungOrGetStatus(any());
    }
}
