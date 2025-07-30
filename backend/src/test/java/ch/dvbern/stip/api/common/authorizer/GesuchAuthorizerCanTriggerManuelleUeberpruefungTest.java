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

package ch.dvbern.stip.api.common.authorizer;

import java.util.UUID;

import ch.dvbern.stip.api.common.authorization.GesuchAuthorizer;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.gesuch.util.GesuchTestUtil;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import jakarta.ws.rs.ForbiddenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GesuchAuthorizerCanTriggerManuelleUeberpruefungTest {
    private Gesuch gesuch;
    private GesuchAuthorizer gesuchAuthorizer;

    private GesuchRepository gesuchRepository;
    private GesuchService gesuchService;
    private GesuchStatusService gesuchStatusService;

    @BeforeEach
    public void setup() {
        gesuchRepository = Mockito.mock(GesuchRepository.class);
        gesuchService = Mockito.mock(GesuchService.class);
        gesuchStatusService = Mockito.mock(GesuchStatusService.class);
        when(gesuchStatusService.gesuchIsInOneOfGesuchStatus(any(), any())).thenCallRealMethod();
        gesuchAuthorizer =
            new GesuchAuthorizer(null, gesuchRepository, gesuchStatusService, null, null, gesuchService, null);
    }

    @Test
    void sbCanTriggerManuellPruefenTest() {
        for (Gesuchstatus gesuchStatus : Gesuchstatus.values()) {
            gesuch = GesuchTestUtil.setupValidGesuchInState(gesuchStatus);
            when(gesuchRepository.requireById(any())).thenReturn(gesuch);

            if (Gesuchstatus.SACHBEARBEITER_CAN_TRIGGER_STATUS_CHECK.contains(gesuchStatus)) {
                assertDoesNotThrow(() -> gesuchAuthorizer.sbCanGesuchManuellPruefen(UUID.randomUUID()));
            } else {
                assertThrows(
                    ForbiddenException.class,
                    () -> gesuchAuthorizer.sbCanGesuchManuellPruefen(UUID.randomUUID())
                );
            }
        }
    }

    @Test
    void juristCanTriggerManuellPruefenTest() {
        for (Gesuchstatus gesuchStatus : Gesuchstatus.values()) {
            gesuch = GesuchTestUtil.setupValidGesuchInState(gesuchStatus);
            when(gesuchRepository.requireById(any())).thenReturn(gesuch);

            if (Gesuchstatus.JURIST_CAN_EDIT.contains(gesuchStatus)) {
                assertDoesNotThrow(() -> gesuchAuthorizer.juristCanGesuchManuellPruefen(UUID.randomUUID()));
            } else {
                final var uuid = UUID.randomUUID();
                assertThrows(
                    ForbiddenException.class,
                    () -> gesuchAuthorizer.juristCanGesuchManuellPruefen(uuid)
                );
            }
        }
    }
}
