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

package ch.dvbern.stip.api.dokument.resource;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.common.authorization.DokumentAuthorizer;
import ch.dvbern.stip.api.dokument.entity.GesuchDokumentKommentar;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentKommentarRepository;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.generated.api.DokumentResource;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@QuarkusTest
class DokumentResourceImplTest {
    @Inject
    DokumentResource dokumentResource;
    @InjectMock
    GesuchDokumentKommentarRepository dokumentKommentarRepository;
    @InjectMock
    GesuchDokumentService gesuchDokumentService;
    @InjectMock
    DokumentAuthorizer dokumentAuthorizer;

    @BeforeEach
    void setUp() {
        GesuchDokumentKommentar kommentar = new GesuchDokumentKommentar();
        when(dokumentKommentarRepository.getByTypAndGesuchTrancheId(any(), any())).thenReturn(List.of(kommentar));
    }

    @Test
    @TestAsGesuchsteller
    // Gesuchsteller should be able to read all comments of a gesuch document
    void resourceShouldReturnCommentsOfADokument() {
        assertNotNull(
            dokumentResource.getGesuchDokumentKommentare(DokumentTyp.EK_VERDIENST, UUID.randomUUID())
        );
    }

    @TestAsGesuchsteller
    @Test
    void gsShouldNotBeAbleToDenyDocumentTest() {
        doNothing().when(gesuchDokumentService).gesuchDokumentAblehnen(any(), any());
        assertThrows(
            io.quarkus.security.ForbiddenException.class,
            () -> dokumentResource.gesuchDokumentAblehnen(UUID.randomUUID(), null)
        );
    }

    @TestAsGesuchsteller
    @Test
    void gsShouldNotBeAbleToAcceptDocumentTest() {
        doNothing().when(gesuchDokumentService).gesuchDokumentAkzeptieren(any());
        assertThrows(
            io.quarkus.security.ForbiddenException.class,
            () -> dokumentResource.gesuchDokumentAkzeptieren(UUID.randomUUID())
        );
    }

    @TestAsSachbearbeiter
    @Test
    void sbShouldBeAbleToDenyDocumentTest() {
        doNothing().when(gesuchDokumentService).gesuchDokumentAblehnen(any(), any());
        doNothing().when(dokumentAuthorizer).canUpdateGesuchDokument(any());
        assertDoesNotThrow(() -> dokumentResource.gesuchDokumentAblehnen(UUID.randomUUID(), null));
    }

    @TestAsSachbearbeiter
    @Test
    void sbShouldBeAbleToAcceptDocumentTest() {
        doNothing().when(gesuchDokumentService).gesuchDokumentAkzeptieren(any());
        doNothing().when(dokumentAuthorizer).canUpdateGesuchDokument(any());
        assertDoesNotThrow(() -> dokumentResource.gesuchDokumentAblehnen(UUID.randomUUID(), null));
    }
}
