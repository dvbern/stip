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
import ch.dvbern.stip.api.delegieren.entity.Delegierung;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.sozialdienst.entity.Sozialdienst;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import ch.dvbern.stip.api.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;

class DokumentAuthorizerTest {
    private DokumentAuthorizer dokumentAuthorizer;

    private GesuchDokument gesuchDokument;
    private Gesuch gesuch;
    private SozialdienstService sozialdienstService;

    @BeforeEach
    void setUp() {
        final var benutzerService = Mockito.mock(BenutzerService.class);
        final var mockBenutzer = new Benutzer();
        mockBenutzer.setRollen(Set.of(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_SACHBEARBEITER)));
        Mockito.when(benutzerService.getCurrentBenutzer()).thenReturn(mockBenutzer);

        final var gesuchRepository = Mockito.mock(GesuchRepository.class);
        sozialdienstService = Mockito.mock(SozialdienstService.class);

        gesuch = TestUtil.getFullGesuch();
        var fall = new Fall();
        fall.setAusbildungs(Set.of(gesuch.getAusbildung()));
        gesuch.getAusbildung().setFall(fall);

        final var gesuchDokumentRepository = Mockito.mock(GesuchDokumentRepository.class);
        gesuchDokument = new GesuchDokument();
        gesuchDokument.setGesuchTranche(new GesuchTranche().setGesuch(new Gesuch()));

        Mockito.when(gesuchDokumentRepository.requireById(any())).thenReturn(gesuchDokument);
        final var gesuchTrancheRepository = Mockito.mock(GesuchTrancheRepository.class);
        Mockito.when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuchDokument.getGesuchTranche());
        // skip this step in authorizer
        Mockito.when(gesuchTrancheRepository.requireById(any())).thenReturn(new GesuchTranche().setGesuch(gesuch));
        dokumentAuthorizer = new DokumentAuthorizer(
            gesuchRepository, benutzerService, gesuchTrancheRepository, gesuchDokumentRepository, sozialdienstService
        );
    }

    @Test
    void canUploadShouldSucceedWhenDelegatedAndSozialdienstMitarbeiter() {
        setupSozialdienstMitarbeiter();
        setupDelegation(gesuch.getAusbildung().getFall());
        assertDoesNotThrow(() -> dokumentAuthorizer.canUpload(UUID.randomUUID()));
    }

    private void setupDelegation(Fall fall) {
        // sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst
        Mockito.when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(true);
        var delegierung = new Delegierung();
        var sozialdienst = new Sozialdienst();
        sozialdienst.setId(UUID.randomUUID());
        delegierung.setSozialdienst(sozialdienst);
        fall.setDelegierung(delegierung);
    }

    private void setupSozialdienstMitarbeiter() {
        final var benutzerService = Mockito.mock(BenutzerService.class);
        final var mockBenutzer = new Benutzer();
        // setup matching role & role in default_roles (Gesuchsteller)
        mockBenutzer.setRollen(
            Set.of(
                new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_SOZIALDIENST_MITARBEITER)
                    .setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER)
            )
        );
        Mockito.when(benutzerService.getCurrentBenutzer()).thenReturn(mockBenutzer);
    }
}
