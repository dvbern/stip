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

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import io.quarkus.security.ForbiddenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CustomGesuchDokumentTypAuthorizerTest {
    private CustomGesuchDokumentTypAuthorizer authorizer;
    private GesuchRepository gesuchRepository;
    private DokumentRepository dokumentRepository;
    private BenutzerService benutzerService;
    private Benutzer currentBenutzer;

    private Gesuch gesuch;

    @BeforeEach
    void setUp() {
        UUID currentBenutzerId = UUID.randomUUID();
        gesuch = new Gesuch();
        gesuchRepository = Mockito.mock(GesuchRepository.class);
        dokumentRepository = Mockito.mock(DokumentRepository.class);
        benutzerService = Mockito.mock(BenutzerService.class);
        currentBenutzer = new Benutzer().setKeycloakId(UUID.randomUUID().toString());
        currentBenutzer.setId(currentBenutzerId);
        authorizer = new CustomGesuchDokumentTypAuthorizer(dokumentRepository, gesuchRepository, benutzerService);
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        when(benutzerService.getCurrentBenutzer()).thenReturn(currentBenutzer);

    }

    @Test
    void canDeleteTypShouldFailAsGS() {
        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER));
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);
        assertThrows(ForbiddenException.class, () -> {
            authorizer.canDeleteTyp(UUID.randomUUID());
        });
    }

    @Test
    void canDeleteTypShouldFail() {
        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_ADMIN));

        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);
        assertThrows(ForbiddenException.class, () -> {
            authorizer.canDeleteTyp(UUID.randomUUID());
        });
    }

    @Test
    void canDeleteTypShouldSuccess() {
        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_SACHBEARBEITER));

        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);
        assertDoesNotThrow(() -> {
            authorizer.canDeleteTyp(UUID.randomUUID());
        });
    }

}
