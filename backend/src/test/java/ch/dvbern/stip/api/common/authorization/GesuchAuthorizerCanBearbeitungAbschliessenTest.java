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

import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.util.TestUtil;
import jakarta.ws.rs.ForbiddenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GesuchAuthorizerCanBearbeitungAbschliessenTest {
    private GesuchAuthorizer gesuchAuthorizer;
    private Gesuch gesuch;
    private GesuchRepository gesuchRepository;

    @BeforeEach
    void setUp() {
        gesuch = TestUtil.getFullGesuch();
        gesuchRepository = Mockito.mock(GesuchRepository.class);
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        gesuchAuthorizer = new GesuchAuthorizer(null, gesuchRepository, null, null, null, null, null, null);
    }

    @Test
    @TestAsSachbearbeiter
    void canBearbeitungAbschliessenShouldSucceedWhenNotInBearbeitungGS() {
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);
        assertDoesNotThrow(() -> gesuchAuthorizer.canBearbeitungAbschliessen(gesuch.getId()));
    }

    @Test
    @TestAsSachbearbeiter
    void canBearbeitungAbschliessenShouldFailWhenInBearbeitungGS() {
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);
        assertThrows(ForbiddenException.class, () -> gesuchAuthorizer.canBearbeitungAbschliessen(gesuch.getId()));
    }
}
