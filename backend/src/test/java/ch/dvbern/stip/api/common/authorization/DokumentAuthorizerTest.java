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
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

class DokumentAuthorizerTest {
    private DokumentAuthorizer dokumentAuthorizer;
    private BenutzerService benutzerService;
    private GesuchDokumentRepository gesuchDokumentRepository;

    private GesuchDokument gesuchDokument;

    @BeforeEach
    void setUp() {
        benutzerService = Mockito.mock(BenutzerService.class);
        var mockBenutzer = new Benutzer();
        mockBenutzer.setRollen(Set.of(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_SACHBEARBEITER)));
        Mockito.when(benutzerService.getCurrentBenutzer()).thenReturn(mockBenutzer);

        gesuchDokumentRepository = Mockito.mock(GesuchDokumentRepository.class);
        gesuchDokument = new GesuchDokument();
        gesuchDokument.setGesuchTranche(new GesuchTranche().setGesuch(new Gesuch()));
        Mockito.when(gesuchDokumentRepository.requireById(any())).thenReturn(gesuchDokument);

        dokumentAuthorizer = new DokumentAuthorizer(benutzerService, gesuchDokumentRepository);
    }

    @Test
    void gesuchDokumentOfTrancheSuccess() {
        // arrange
        gesuchDokument.getGesuchTranche().setTyp(GesuchTrancheTyp.TRANCHE);
        gesuchDokument.getGesuchTranche().getGesuch().setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);

        // act/assert
        assertDoesNotThrow(() -> dokumentAuthorizer.canUpdateGesuchDokument(UUID.randomUUID()));
    }

    @Test
    void gesuchDokumentOfTrancheFail() {
        // arrange
        gesuchDokument.getGesuchTranche().setTyp(GesuchTrancheTyp.TRANCHE);
        gesuchDokument.getGesuchTranche().getGesuch().setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);

        // act/assert
        assertThrows(IllegalStateException.class, () -> dokumentAuthorizer.canUpdateGesuchDokument(UUID.randomUUID()));
    }

    @Test
    void gesuchDokumentOfAenderungSuccess() {
        // arrange
        gesuchDokument.getGesuchTranche().setTyp(GesuchTrancheTyp.AENDERUNG);
        gesuchDokument.getGesuchTranche().setStatus(GesuchTrancheStatus.UEBERPRUEFEN);

        // act/assert
        assertDoesNotThrow(() -> dokumentAuthorizer.canUpdateGesuchDokument(UUID.randomUUID()));
    }

    @Test
    void gesuchDokumentOfAenderungFail() {
        // arrange
        gesuchDokument.getGesuchTranche().setTyp(GesuchTrancheTyp.AENDERUNG);
        gesuchDokument.getGesuchTranche().setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS);

        // act/assert
        assertThrows(IllegalStateException.class, () -> dokumentAuthorizer.canUpdateGesuchDokument(UUID.randomUUID()));
    }
}
