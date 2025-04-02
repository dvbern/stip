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

import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.GesuchAuthorizer;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheStatusService;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import jakarta.ws.rs.ForbiddenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GesuchAuthorizerCanDeleteTest {
    private Benutzer currentBenutzer;
    private Benutzer sbBenutzer;
    private Benutzer adminBenutzer;
    private Gesuch gesuch;
    private GesuchAuthorizer authorizer;
    private BenutzerService benutzerService;
    private GesuchTrancheStatusService gesuchTrancheStatusService;

    @BeforeEach
    void setUp() {
        UUID currentBenutzerId = UUID.randomUUID();
        benutzerService = Mockito.mock(BenutzerService.class);
        currentBenutzer = new Benutzer().setKeycloakId(UUID.randomUUID().toString());
        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER));
        currentBenutzer.setId(currentBenutzerId);

        sbBenutzer = new Benutzer().setKeycloakId(UUID.randomUUID().toString());
        sbBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_SACHBEARBEITER));
        sbBenutzer.setId(UUID.randomUUID());

        adminBenutzer = new Benutzer().setKeycloakId(UUID.randomUUID().toString());
        adminBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_ADMIN));
        adminBenutzer.setId(UUID.randomUUID());
        gesuchTrancheStatusService = Mockito.mock(GesuchTrancheStatusService.class);

        final var gesuchTranche_inBearbeitungGS = new GesuchTranche()
            .setGesuch(gesuch)
            .setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS);

        UUID otherBenutzerId = UUID.randomUUID();
        final var otherBenutzer = new Benutzer();
        otherBenutzer.setId(otherBenutzerId);
        otherBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER));

        when(benutzerService.getCurrentBenutzer()).thenReturn(currentBenutzer);

        GesuchRepository gesuchRepository = Mockito.mock(GesuchRepository.class);
        GesuchTrancheRepository gesuchTrancheRepository = Mockito.mock(GesuchTrancheRepository.class);
        final var fallRepository = Mockito.mock(FallRepository.class);
        final var gesuchStatusService = Mockito.mock(GesuchStatusService.class);
        final var sozialdienstService = Mockito.mock(SozialdienstService.class);

        gesuch = new Gesuch()
            .setAusbildung(
                new Ausbildung()
                    .setFall(
                        new Fall()
                            .setGesuchsteller(currentBenutzer)
                    )
            );
        final var fall = new Fall().setGesuchsteller(currentBenutzer);
        authorizer = new GesuchAuthorizer(
            benutzerService,
            gesuchRepository,
            gesuchTrancheRepository,
            gesuchStatusService,
            gesuchTrancheStatusService,
            fallRepository,
            sozialdienstService,
            null
        );

        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuchTranche_inBearbeitungGS);
        when(gesuchTrancheRepository.findById(any())).thenReturn(gesuchTranche_inBearbeitungGS);
        when(gesuchRepository.requireGesuchByTrancheId(any())).thenReturn(gesuch);
        when(fallRepository.requireById(any())).thenReturn(fall);
        when(gesuchStatusService.benutzerCanEdit(any(), any())).thenReturn(true);
    }

    @Test
    void canUpdateOwnTest() {
        // arrange
        final var uuid = UUID.randomUUID();
        var gesuchTranche = new GesuchTranche();
        gesuchTranche.setId(uuid);
        gesuchTranche.setGesuch(gesuch);

        // assert
        assertDoesNotThrow(() -> authorizer.canUpdateTranche(gesuchTranche));
    }

    @Test
    void canReadChangesTest() {
        // arrange
        final var uuid = UUID.randomUUID();
        assertThrows(ForbiddenException.class, () -> authorizer.canReadChanges(uuid));

        when(benutzerService.getCurrentBenutzer()).thenReturn(sbBenutzer);

        // assert
        assertDoesNotThrow(() -> authorizer.canReadChanges(uuid));

        // arrange
        when(benutzerService.getCurrentBenutzer()).thenReturn(adminBenutzer);

        // assert
        assertDoesNotThrow(() -> authorizer.canReadChanges(uuid));
    }

    @Test
    void canDeleteOwnTest() {
        // arrange
        final var uuid = UUID.randomUUID();

        // assert
        assertDoesNotThrow(() -> authorizer.canDelete(uuid));
    }

    @Test
    void cannotDeleteAnotherTest() {
        // arrange
        currentBenutzer.setRollen(Set.of());
        final var uuid = UUID.randomUUID();

        // assert
        assertThrows(ForbiddenException.class, () -> {
            authorizer.canDelete(uuid);
        });
    }

    @Test
    void adminCanDeleteTest() {
        // arrange
        currentBenutzer.setRollen(Set.of(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_ADMIN)));
        final var uuid = UUID.randomUUID();

        // assert
        assertDoesNotThrow(() -> authorizer.canDelete(uuid));
    }
}
