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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.repo.CustomDokumentTypRepository;
import ch.dvbern.stip.api.dokument.repo.DokumentRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.util.TestUtil;
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
    private CustomDokumentTyp customDokumentTyp;
    private GesuchRepository gesuchRepository;
    private DokumentRepository dokumentRepository;
    private GesuchDokumentRepository gesuchDokumentRepository;
    private GesuchTrancheRepository gesuchTrancheRepository;
    private CustomDokumentTypRepository customDokumentTypRepository;
    private BenutzerService benutzerService;
    private Benutzer currentBenutzer;

    private Gesuch gesuch;

    @BeforeEach
    void setUp() {
        UUID currentBenutzerId = UUID.randomUUID();
        gesuch = TestUtil.setupGesuchWithCustomDokument();
        var gesuchDokument = new GesuchDokument();
        gesuchDokument.setId(UUID.randomUUID());
        gesuchDokument.setGesuchTranche(gesuch.getGesuchTranchen().get(0));
        customDokumentTyp = new CustomDokumentTyp();
        customDokumentTyp.setId(UUID.randomUUID());
        customDokumentTyp.setDescription("test");
        customDokumentTyp.setGesuchDokument(gesuchDokument);
        gesuchRepository = Mockito.mock(GesuchRepository.class);
        dokumentRepository = Mockito.mock(DokumentRepository.class);
        benutzerService = Mockito.mock(BenutzerService.class);
        currentBenutzer = new Benutzer().setKeycloakId(UUID.randomUUID().toString());
        currentBenutzer.setId(currentBenutzerId);
        gesuchDokumentRepository = Mockito.mock(GesuchDokumentRepository.class);
        gesuchTrancheRepository = Mockito.mock(GesuchTrancheRepository.class);
        customDokumentTypRepository = Mockito.mock(CustomDokumentTypRepository.class);
        authorizer = new CustomGesuchDokumentTypAuthorizer(
            dokumentRepository, customDokumentTypRepository, gesuchDokumentRepository, gesuchTrancheRepository,
            benutzerService
        );
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        gesuch.getGesuchTranchen().get(0).setGesuch(gesuch);
        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuch.getGesuchTranchen().get(0));
        var gesuchDok = new GesuchDokument();
        gesuchDok.setDokumente(List.of());
        when(gesuchDokumentRepository.findByCustomDokumentTyp(any()))
            .thenReturn(Optional.of(gesuchDok));
        when(benutzerService.getCurrentBenutzer()).thenReturn(currentBenutzer);
        when(customDokumentTypRepository.requireById(any())).thenReturn(customDokumentTyp);
    }

    // a GS should not be allowed to delete a CustomDokumentType (only a SB should be able)
    @Test
    void canDeleteTypShouldFailAsGS() {
        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER));
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);
        assertThrows(ForbiddenException.class, () -> {
            authorizer.canDeleteTyp(
                UUID.randomUUID()
            );
        });
    }

    @Test
    void canDeleteTypShouldFailAsGSWhenInBearbeitungSB() {
        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER));
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);
        assertThrows(ForbiddenException.class, () -> {
            authorizer.canDeleteTyp(
                UUID.randomUUID()
            );
        });
    }

    @Test
    void canDeleteTypShouldFailAsAdmin() {
        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_ADMIN));
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);
        assertThrows(ForbiddenException.class, () -> {
            authorizer.canDeleteTyp(
                UUID.randomUUID()
            );
        });
    }

    @Test
    void canDeleteTypShouldSuccessAsSB() {
        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_SACHBEARBEITER));

        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);
        assertDoesNotThrow(() -> {
            authorizer.canDeleteTyp(
                UUID.randomUUID()
            );
        });
    }

    @Test
    void canCreateTypShouldFailWhenNotInBearbeitungSB() {
        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_SACHBEARBEITER));

        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);

        assertDoesNotThrow(() -> {
            authorizer.canCreateCustomDokumentTyp(UUID.randomUUID());
        });

        gesuch.setGesuchStatus(Gesuchstatus.IN_FREIGABE);
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        assertThrows(ForbiddenException.class, () -> {
            authorizer.canCreateCustomDokumentTyp(UUID.randomUUID());
        });
    }

}
