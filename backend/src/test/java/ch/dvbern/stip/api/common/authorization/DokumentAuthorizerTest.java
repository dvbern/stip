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

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DokumentAuthorizerTest {
    private DokumentAuthorizer dokumentAuthorizer;
    private BenutzerService benutzerService;
    private SozialdienstService sozialdienstService;
    private GesuchRepository gesuchRepository;
    private GesuchTrancheRepository gesuchTrancheRepository;
    private GesuchDokumentRepository gesuchDokumentRepository;

    private GesuchDokument gesuchDokument;

    @BeforeEach
    void setUp() {
        sozialdienstService = Mockito.mock(SozialdienstService.class);
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(false);
        benutzerService = Mockito.mock(BenutzerService.class);
        final var mockBenutzer = new Benutzer();
        mockBenutzer.setRollen(Set.of(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_SACHBEARBEITER)));
        Mockito.when(benutzerService.getCurrentBenutzer()).thenReturn(mockBenutzer);
        final var currentBenutzer = new Benutzer().setKeycloakId(UUID.randomUUID().toString());
        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER));

        gesuchRepository = Mockito.mock(GesuchRepository.class);
        final var gesuch = new Gesuch()
            .setAusbildung(
                new Ausbildung()
                    .setFall(
                        new Fall()
                            .setGesuchsteller(currentBenutzer)
                    )
            );
        Mockito.when(gesuchRepository.requireGesuchForDokument(any())).thenReturn(gesuch);
        gesuchDokumentRepository = Mockito.mock(GesuchDokumentRepository.class);
        gesuchDokument = new GesuchDokument();
        gesuchDokument.setGesuchTranche(new GesuchTranche().setGesuch(new Gesuch()));
        Mockito.when(gesuchDokumentRepository.requireById(any())).thenReturn(gesuchDokument);

        gesuchTrancheRepository = Mockito.mock(GesuchTrancheRepository.class);
        Mockito.when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuchDokument.getGesuchTranche());

        dokumentAuthorizer = new DokumentAuthorizer(
            gesuchRepository,
            benutzerService,
            gesuchTrancheRepository,
            gesuchDokumentRepository,
            sozialdienstService
        );
    }

    @Test
    void gesuchDokumentOfTrancheSuccess() {
        // arrange
        gesuchDokument.getGesuchTranche().setTyp(GesuchTrancheTyp.TRANCHE);
        gesuchDokument.getGesuchTranche().getGesuch().setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);

        // act/assert
        final var dokumentId = UUID.randomUUID();
        assertDoesNotThrow(() -> dokumentAuthorizer.canUpdateGesuchDokument(dokumentId));
    }

    @Test
    void gesuchDokumentOfTrancheFail() {
        // arrange
        gesuchDokument.getGesuchTranche().setTyp(GesuchTrancheTyp.TRANCHE);
        gesuchDokument.getGesuchTranche().getGesuch().setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);

        // act/assert
        final var dokumentId = UUID.randomUUID();
        assertThrows(IllegalStateException.class, () -> dokumentAuthorizer.canUpdateGesuchDokument(dokumentId));
    }

    @Test
    void gesuchDokumentOfAenderungSuccess() {
        // arrange
        gesuchDokument.getGesuchTranche().setTyp(GesuchTrancheTyp.AENDERUNG);
        gesuchDokument.getGesuchTranche().setStatus(GesuchTrancheStatus.UEBERPRUEFEN);

        // act/assert
        final var dokumentId = UUID.randomUUID();
        assertDoesNotThrow(() -> dokumentAuthorizer.canUpdateGesuchDokument(dokumentId));
    }

    @Test
    void gesuchDokumentOfAenderungFail() {
        // arrange
        gesuchDokument.getGesuchTranche().setTyp(GesuchTrancheTyp.AENDERUNG);
        gesuchDokument.getGesuchTranche().setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS);

        // act/assert
        final var dokumentId = UUID.randomUUID();
        assertThrows(IllegalStateException.class, () -> dokumentAuthorizer.canUpdateGesuchDokument(dokumentId));
    }
}
