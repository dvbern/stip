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
import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheStatusService;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import io.quarkus.security.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GesuchAuthorizerCanGetBerechnungTest {
    private BenutzerService benutzerService;
    private Benutzer currentBenutzer;
    private Gesuch gesuch;
    private GesuchAuthorizer authorizer;

    private GesuchRepository gesuchRepository;
    private GesuchTrancheRepository gesuchTrancheRepository;
    private GesuchTrancheStatusService gesuchTrancheStatusService;

    @BeforeEach
    void setUp() {
        UUID currentBenutzerId = UUID.randomUUID();
        benutzerService = Mockito.mock(BenutzerService.class);
        currentBenutzer = new Benutzer().setKeycloakId(UUID.randomUUID().toString());
        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER));
        currentBenutzer.setId(currentBenutzerId);

        final var gesuchTranche_inBearbeitungGS = new GesuchTranche()
            .setGesuch(gesuch)
            .setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS);

        UUID otherBenutzerId = UUID.randomUUID();
        final var otherBenutzer = new Benutzer();
        otherBenutzer.setId(otherBenutzerId);
        otherBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER));

        when(benutzerService.getCurrentBenutzer()).thenReturn(currentBenutzer);

        gesuchRepository = Mockito.mock(GesuchRepository.class);
        gesuchTrancheRepository = Mockito.mock(GesuchTrancheRepository.class);
        gesuchTrancheStatusService = Mockito.mock(GesuchTrancheStatusService.class);
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
    @TestAsGesuchsteller
    void testGesuchStatusCorrectForBerechnung() {
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        assertThrows(UnauthorizedException.class, () -> {
            authorizer.canGetBerechnung(gesuch.getId());
        });
        Gesuchstatus.SACHBEARBEITER_CAN_GET_BERECHNUNG.forEach(gesuchstatus -> {
            gesuch.setGesuchStatus(gesuchstatus);
            assertDoesNotThrow(() -> authorizer.canGetBerechnung(gesuch.getId()));

        });
    }
}
