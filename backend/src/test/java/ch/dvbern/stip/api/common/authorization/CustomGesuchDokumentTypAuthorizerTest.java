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

import java.util.ArrayList;
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
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import ch.dvbern.stip.api.util.TestUtil;
import jakarta.ws.rs.ForbiddenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.Mockito;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Execution(ExecutionMode.CONCURRENT)
class CustomGesuchDokumentTypAuthorizerTest {
    private CustomGesuchDokumentTypAuthorizer customGesuchDokumentTypAuthorizer;
    private CustomDokumentTyp customDokumentTyp;
    private GesuchRepository gesuchRepository;
    private GesuchDokumentRepository gesuchDokumentRepository;
    private GesuchTrancheRepository gesuchTrancheRepository;
    private CustomDokumentTypRepository customDokumentTypRepository;
    private BenutzerService benutzerService;
    private SozialdienstService sozialdienstService;
    private GesuchDokumentAuthorizer gesuchDokumentAuthorizer;
    private Benutzer currentBenutzer;
    private GesuchDokument currentGesuchDokument;
    private Gesuch gesuch;

    @BeforeEach
    void setUp() {
        UUID currentBenutzerId = UUID.randomUUID();
        gesuch = TestUtil.setupGesuchWithCustomDokument();
        var gesuchDokument = new GesuchDokument();
        gesuchDokument.setId(UUID.randomUUID());
        gesuch.getGesuchTranchen().get(0).setTyp(GesuchTrancheTyp.TRANCHE);
        gesuchDokument.setGesuchTranche(gesuch.getGesuchTranchen().get(0));
        customDokumentTyp = new CustomDokumentTyp();
        customDokumentTyp.setId(UUID.randomUUID());
        customDokumentTyp.setDescription("test");
        customDokumentTyp.setGesuchDokument(gesuchDokument);
        gesuchRepository = Mockito.mock(GesuchRepository.class);
        benutzerService = Mockito.mock(BenutzerService.class);
        currentBenutzer = new Benutzer().setKeycloakId(UUID.randomUUID().toString());
        currentBenutzer.setId(currentBenutzerId);
        gesuchDokumentRepository = Mockito.mock(GesuchDokumentRepository.class);
        gesuchTrancheRepository = Mockito.mock(GesuchTrancheRepository.class);
        customDokumentTypRepository = Mockito.mock(CustomDokumentTypRepository.class);
        sozialdienstService = Mockito.mock(SozialdienstService.class);

        gesuchDokumentAuthorizer = new GesuchDokumentAuthorizer(
            gesuchTrancheRepository, benutzerService, gesuchDokumentRepository, null, sozialdienstService, null, null
        );

        customGesuchDokumentTypAuthorizer = new CustomGesuchDokumentTypAuthorizer(
            customDokumentTypRepository, gesuchDokumentRepository, gesuchTrancheRepository,
            benutzerService, sozialdienstService, gesuchDokumentAuthorizer
        );

        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        gesuch.getGesuchTranchen().get(0).setGesuch(gesuch);
        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuch.getGesuchTranchen().get(0));
        currentGesuchDokument = new GesuchDokument();
        currentGesuchDokument.setDokumente(List.of());
        when(gesuchDokumentRepository.findByCustomDokumentTyp(any()))
            .thenReturn(Optional.of(currentGesuchDokument));
        when(benutzerService.getCurrentBenutzer()).thenReturn(currentBenutzer);
        when(customDokumentTypRepository.requireById(any())).thenReturn(customDokumentTyp);
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(false);
        when(gesuchDokumentRepository.findByCustomDokumentTyp(any()))
            .thenReturn(Optional.ofNullable(customDokumentTyp.getGesuchDokument()));
    }

    @Test
    void canDeleteTypShouldFailAsAdmin() {
        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_SACHBEARBEITER_ADMIN));
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);
        assertThrows(ForbiddenException.class, () -> {
            customGesuchDokumentTypAuthorizer.canDeleteTyp(
                UUID.randomUUID()
            );
        });
    }

    @Test
    void canDeleteTypShouldSuccessAsSB() {
        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_SACHBEARBEITER));
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);
        assertDoesNotThrow(() -> {
            customGesuchDokumentTypAuthorizer.canDeleteTyp(
                UUID.randomUUID()
            );
        });
    }

    @Test
    void canCreateTypShouldFailWhenNotInBearbeitungSB() {
        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_SACHBEARBEITER));

        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);
        gesuch.getGesuchTranchen().get(0).setTyp(GesuchTrancheTyp.TRANCHE);
        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuch.getGesuchTranchen().get(0));
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);

        assertDoesNotThrow(() -> {
            customGesuchDokumentTypAuthorizer.canCreateCustomDokumentTyp(UUID.randomUUID());
        });

        gesuch.setGesuchStatus(Gesuchstatus.IN_FREIGABE);
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        assertThrows(ForbiddenException.class, () -> {
            customGesuchDokumentTypAuthorizer.canCreateCustomDokumentTyp(UUID.randomUUID());
        });
    }

    @Test
    void canCreateTypShouldNOTFailWhenCurrentTrancheIsAenderungOfStatusUeberpruefen() {
        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_SACHBEARBEITER));
        gesuch = new Gesuch();
        gesuch.setGesuchTranchen(new ArrayList<>());
        gesuch.setGesuchStatus(Gesuchstatus.VERFUEGUNG_VERSENDET);
        var gesuchTranche = new GesuchTranche().setTyp(GesuchTrancheTyp.AENDERUNG)
            .setStatus(GesuchTrancheStatus.UEBERPRUEFEN)
            .setGesuch(gesuch);
        gesuch.getGesuchTranchen()
            .add(gesuchTranche);
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuchTranche);
        assertDoesNotThrow(() -> {
            customGesuchDokumentTypAuthorizer.canCreateCustomDokumentTyp(UUID.randomUUID());
        });

        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);
        gesuchTranche.setStatus(GesuchTrancheStatus.AKZEPTIERT);
        assertThrows(ForbiddenException.class, () -> {
            customGesuchDokumentTypAuthorizer.canCreateCustomDokumentTyp(UUID.randomUUID());
        });
    }

    @Test
    void canDeleteTypShouldNOTFailWhenCurrentTrancheIsAenderungOfStatusUeberpruefen() {
        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_SACHBEARBEITER));
        gesuch = new Gesuch();
        gesuch.setGesuchTranchen(new ArrayList<>());
        gesuch.setGesuchStatus(Gesuchstatus.VERFUEGUNG_VERSENDET);
        var gesuchTranche = new GesuchTranche().setTyp(GesuchTrancheTyp.AENDERUNG)
            .setStatus(GesuchTrancheStatus.UEBERPRUEFEN)
            .setGesuch(gesuch);
        gesuch.getGesuchTranchen()
            .add(gesuchTranche);
        currentGesuchDokument.setGesuchTranche(gesuchTranche);

        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);
        gesuchTranche.setTyp(GesuchTrancheTyp.AENDERUNG);
        gesuchTranche.setStatus(GesuchTrancheStatus.AKZEPTIERT);

        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuchTranche);
        when(gesuchDokumentRepository.requireById(any())).thenReturn(currentGesuchDokument);
        when(gesuchDokumentRepository.findByCustomDokumentTyp(any()))
            .thenReturn(Optional.ofNullable(currentGesuchDokument));

        assertThrows(ForbiddenException.class, () -> {
            customGesuchDokumentTypAuthorizer.canDeleteTyp(UUID.randomUUID());
        });

        gesuchTranche.setStatus(GesuchTrancheStatus.UEBERPRUEFEN);
        assertDoesNotThrow(() -> {
            customGesuchDokumentTypAuthorizer.canDeleteTyp(UUID.randomUUID());
        });
    }

}
