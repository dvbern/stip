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

import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import jakarta.ws.rs.ForbiddenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

class GesuchAuthorizerCanUpdateEinreichefristTest {
    private Gesuch gesuch;
    private GesuchAuthorizer authorizer;

    private BenutzerService benutzerService;
    private GesuchRepository gesuchRepository;

    @BeforeEach
    void setUp() {
        gesuch = new Gesuch();
        gesuchRepository = Mockito.mock(GesuchRepository.class);
        benutzerService = Mockito.mock(BenutzerService.class);
        when(gesuchRepository.requireById(Mockito.any())).thenReturn(gesuch);
        authorizer = new GesuchAuthorizer(benutzerService, gesuchRepository, null, null, null, null, null, null, null);
    }

    @Test
    void canUpdateEinreichefristShouldFailAsGS() {
        when(benutzerService.getCurrentBenutzer()).thenReturn(new Benutzer().setRollen(Set.of(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER))));
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);
        assertThrows(ForbiddenException.class, () -> {
            authorizer.canUpdateEinreichefrist(UUID.randomUUID());
        });
    }

    @Test
    void canUpdateEinreichefristShouldFailAsSB() {
        when(benutzerService.getCurrentBenutzer()).thenReturn(new Benutzer().setRollen(Set.of(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_SACHBEARBEITER))));
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);
        assertThrows(ForbiddenException.class, () -> {
            authorizer.canUpdateEinreichefrist(UUID.randomUUID());
        });
    }

    @Test
    void canUpdateEinreichefristShouldSuccess() {
        when(benutzerService.getCurrentBenutzer()).thenReturn(new Benutzer().setRollen(Set.of(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_SACHBEARBEITER))));
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);
        assertDoesNotThrow(() -> {
            authorizer.canUpdateEinreichefrist(UUID.randomUUID());
        });
    }

}
