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

package ch.dvbern.stip.api.common.authorization;

import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import jakarta.ws.rs.ForbiddenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Execution(ExecutionMode.CONCURRENT)
class GesuchAuthorizerCanGetBerechnungTest {
    private Gesuch gesuch;
    private GesuchAuthorizer authorizer;

    private GesuchRepository gesuchRepository;
    private GesuchStatusService gesuchStatusService;

    @BeforeEach
    void setUp() {
        gesuchRepository = Mockito.mock(GesuchRepository.class);
        gesuchStatusService = Mockito.mock(GesuchStatusService.class);
        when(gesuchStatusService.gesuchIsInOneOfGesuchStatus(any(), any())).thenCallRealMethod();
        when(gesuchStatusService.canGetBerechnung(any())).thenCallRealMethod();
        gesuch = new Gesuch()
            .setAusbildung(
                new Ausbildung()
                    .setFall(
                        new Fall()
                    )
            );
        authorizer = new GesuchAuthorizer(
            null,
            gesuchRepository,
            gesuchStatusService,
            null,
            null,
            null,
            null
        );

        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
    }

    @Test
    @TestAsGesuchsteller
    void testGesuchStatusCorrectForBerechnung() {
        gesuch.setId(UUID.randomUUID());
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        assertThrows(ForbiddenException.class, () -> {
            authorizer.canGetBerechnung(gesuch.getId());
        });
        Gesuchstatus.SACHBEARBEITER_CAN_GET_BERECHNUNG.forEach(gesuchstatus -> {
            gesuch.setGesuchStatus(gesuchstatus);
            assertDoesNotThrow(() -> authorizer.canGetBerechnung(gesuch.getId()));

        });
    }
}
