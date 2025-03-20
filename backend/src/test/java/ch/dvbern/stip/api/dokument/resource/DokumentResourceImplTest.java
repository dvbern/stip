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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.common.authorization.GesuchDokumentAuthorizer;
import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokumentKommentar;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentKommentarRepository;
import ch.dvbern.stip.api.dokument.service.CustomDokumentTypService;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.gesuch.util.GesuchTestUtil;
import ch.dvbern.stip.api.gesuchformular.service.GesuchFormularService;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.generated.api.DokumentResource;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
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
    @Inject
    GesuchFormularService gesuchFormularService;
    @InjectMock
    CustomDokumentTypService customDokumentTypService;
    @InjectMock
    GesuchDokumentAuthorizer gesuchDokumentAuthorizer;

    @BeforeEach
    void setUp() {
        GesuchDokumentKommentar kommentar = new GesuchDokumentKommentar();
        when(dokumentKommentarRepository.getByGesuchDokumentId(any()))
            .thenReturn(List.of(kommentar));
    }

    @Test
    @TestAsGesuchsteller
    // Gesuchsteller should be able to read all comments of a gesuch document
    void resourceShouldReturnCommentsOfADokument() {
        doNothing().when(gesuchDokumentAuthorizer).canRead(any());
        assertNotNull(
            dokumentResource.getGesuchDokumentKommentareGS(UUID.randomUUID())
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
        assertDoesNotThrow(() -> dokumentResource.gesuchDokumentAblehnen(UUID.randomUUID(), null));
    }

    @TestAsSachbearbeiter
    @Test
    void sbShouldBeAbleToAcceptDocumentTest() {
        doNothing().when(gesuchDokumentService).gesuchDokumentAkzeptieren(any());
        assertDoesNotThrow(() -> dokumentResource.gesuchDokumentAblehnen(UUID.randomUUID(), null));
    }

    @TestAsGesuchsteller
    @Test
    void testPageValidationForAusstehendeCustomGesuchDokument() {
        // arrange
        var gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_BEARBEITUNG_GS);
        var tranche = gesuch.getGesuchTranchen().get(0);
        tranche.getGesuchFormular().getPersonInAusbildung().setGeburtsdatum(LocalDate.now().minusYears(16));

        CustomDokumentTyp customDokumentTyp1 = new CustomDokumentTyp();
        customDokumentTyp1.setId(UUID.randomUUID());
        customDokumentTyp1.setType("test1");
        CustomDokumentTyp customDokumentTyp2 = new CustomDokumentTyp();
        customDokumentTyp2.setId(UUID.randomUUID());
        customDokumentTyp2.setType("test2");
        when(customDokumentTypService.getAllCustomDokumentTypsOfTranche(any()))
            .thenReturn(List.of(customDokumentTyp1, customDokumentTyp2));

        List<GesuchDokument> dokuments = new ArrayList<>();

        GesuchDokument customGesuchDokument1 = new GesuchDokument();
        customGesuchDokument1.setStatus(Dokumentstatus.AUSSTEHEND);
        customGesuchDokument1.setDokumente(List.of(new Dokument()));
        customGesuchDokument1.setCustomDokumentTyp(customDokumentTyp1);

        GesuchDokument customGesuchDokument2 = new GesuchDokument();
        customGesuchDokument2.setStatus(Dokumentstatus.AUSSTEHEND);
        customGesuchDokument2.setDokumente(List.of());
        customGesuchDokument2.setCustomDokumentTyp(customDokumentTyp2);

        for (DokumentTyp dokumentTyp : DokumentTyp.values()) {
            var gesuchDokument = new GesuchDokument();
            gesuchDokument.setStatus(Dokumentstatus.AUSSTEHEND);
            gesuchDokument.setDokumentTyp(dokumentTyp);
            gesuchDokument.setDokumente(List.of(new Dokument()));
            dokuments.add(gesuchDokument);
        }

        dokuments.add(customGesuchDokument1);
        dokuments.add(customGesuchDokument2);
        dokuments.forEach(dok -> dok.setGesuchTranche(tranche));
        tranche.getGesuchDokuments().addAll(dokuments);

        // act
        final var violations1 =
            gesuchFormularService.validatePages(tranche.getGesuchFormular());

        customGesuchDokument2.setDokumente(List.of(new Dokument()));

        final var violations2 =
            gesuchFormularService.validatePages(tranche.getGesuchFormular());

        // assert
        assertNotNull(violations1.getValidationWarnings());
        assertThat(violations2.getValidationWarnings()).isEmpty();
    }
}
