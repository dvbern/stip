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
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
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
    private GesuchRepository gesuchRepository;
    private DokumentRepository dokumentRepository;
    private GesuchDokumentRepository gesuchDokumentRepository;
    private GesuchTrancheRepository gesuchTrancheRepository;
    private BenutzerService benutzerService;
    private Benutzer currentBenutzer;

    private Gesuch gesuch;

    @BeforeEach
    void setUp() {
        UUID currentBenutzerId = UUID.randomUUID();
        gesuch = TestUtil.setupGesuchWithCustomDokument();
        gesuchRepository = Mockito.mock(GesuchRepository.class);
        dokumentRepository = Mockito.mock(DokumentRepository.class);
        benutzerService = Mockito.mock(BenutzerService.class);
        currentBenutzer = new Benutzer().setKeycloakId(UUID.randomUUID().toString());
        currentBenutzer.setId(currentBenutzerId);
        gesuchDokumentRepository = Mockito.mock(GesuchDokumentRepository.class);
        gesuchTrancheRepository = Mockito.mock(GesuchTrancheRepository.class);
        authorizer = new CustomGesuchDokumentTypAuthorizer(
            dokumentRepository, gesuchDokumentRepository, gesuchRepository, gesuchTrancheRepository, benutzerService
        );
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        gesuch.getGesuchTranchen().get(0).setGesuch(gesuch);
        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuch.getGesuchTranchen().get(0));
        var gesuchDok = new GesuchDokument();
        gesuchDok.setDokumente(List.of());
        when(gesuchDokumentRepository.findByGesuchTrancheAndCustomDokumentType(any(), any()))
            .thenReturn(Optional.of(gesuchDok));
        when(benutzerService.getCurrentBenutzer()).thenReturn(currentBenutzer);
    }

    @Test
    void canDeleteTypShouldFailAsGS() {
        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER));
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);
        assertThrows(ForbiddenException.class, () -> {
            authorizer.canDeleteTyp(
                UUID.randomUUID(),
                gesuch.getCurrentGesuchTranche().getGesuchDokuments().get(0).getCustomDokumentTyp().getId()
            );
        });
    }

    @Test
    void canDeleteTypShouldFail() {
        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_ADMIN));
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);
        assertThrows(ForbiddenException.class, () -> {
            authorizer.canDeleteTyp(
                UUID.randomUUID(),
                gesuch.getCurrentGesuchTranche().getGesuchDokuments().get(0).getCustomDokumentTyp().getId()
            );
        });
    }

    @Test
    void canDeleteTypShouldSuccess() {
        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_SACHBEARBEITER));

        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);
        assertDoesNotThrow(() -> {
            authorizer.canDeleteTyp(
                UUID.randomUUID(),
                gesuch.getCurrentGesuchTranche().getGesuchDokuments().get(0).getCustomDokumentTyp().getId()
            );
        });
    }

    @Test
    void canDeleteCustomDokumentShouldFailAsSB() {
        // custom dokument attached
        Dokument dokument = new Dokument();
        CustomDokumentTyp customDokumentTyp = new CustomDokumentTyp();
        customDokumentTyp.setId(UUID.randomUUID());
        GesuchDokument gesuchDokument = new GesuchDokument();
        gesuchDokument.setCustomDokumentTyp(customDokumentTyp);
        dokument.setGesuchDokumente(List.of(gesuchDokument));
        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_SACHBEARBEITER));
        when(dokumentRepository.findByIdOptional(any())).thenReturn(Optional.of(dokument));

        final var idToCheck = UUID.randomUUID();
        assertThrows(ForbiddenException.class, () -> authorizer.canDeleteDokument(idToCheck));
    }

    @Test
    void canDeleteCustomDokumentShouldSuccessAsSB() {
        // no custom dokument attached
        Dokument dokument = new Dokument();
        GesuchDokument gesuchDokument = new GesuchDokument();
        dokument.setGesuchDokumente(List.of(gesuchDokument));
        when(dokumentRepository.findByIdOptional(any())).thenReturn(Optional.of(dokument));

        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_SACHBEARBEITER));
        assertDoesNotThrow(() -> {
            authorizer.canDeleteDokument(UUID.randomUUID());
        });
    }

}
