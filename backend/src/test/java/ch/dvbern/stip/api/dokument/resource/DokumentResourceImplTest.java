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
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.common.authorization.DokumentAuthorizer;
import ch.dvbern.stip.api.common.authorization.GesuchDokumentAuthorizer;
import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokumentKommentar;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentHistoryRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentKommentarHistoryRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentKommentarRepository;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.service.CustomDokumentTypService;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.GesuchDokumentStatus;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.util.GesuchTestUtil;
import ch.dvbern.stip.api.gesuchformular.service.GesuchFormularService;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranchehistory.repo.GesuchTrancheHistoryRepository;
import ch.dvbern.stip.api.gesuchtranchehistory.service.GesuchTrancheHistoryService;
import ch.dvbern.stip.api.land.entity.Land;
import ch.dvbern.stip.api.land.type.WellKnownLand;
import ch.dvbern.stip.generated.api.DokumentResource;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ch.dvbern.stip.api.util.TestConstants.GESUCHSTELLER_TEST_ID;
import static org.assertj.core.api.Assertions.assertThat;
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
    GesuchDokumentKommentarHistoryRepository gesuchDokumentKommentarHistoryRepository;
    @InjectMock
    GesuchDokumentRepository gesuchDokumentRepository;
    @InjectMock
    GesuchDokumentService gesuchDokumentService;
    @InjectMock
    GesuchTrancheHistoryService gesuchTrancheHistoryService;
    @InjectMock
    GesuchTrancheHistoryRepository gesuchTrancheHistoryRepository;
    @InjectMock
    GesuchTrancheRepository gesuchTrancheRepository;
    @InjectMock
    GesuchDokumentHistoryRepository gesuchDokumentHistoryRepository;
    @Inject
    GesuchFormularService gesuchFormularService;
    @InjectMock
    CustomDokumentTypService customDokumentTypService;
    @InjectMock
    DokumentAuthorizer dokumentAuthorizer;
    @InjectMock
    GesuchDokumentAuthorizer gesuchDokumentAuthorizer;

    @BeforeEach
    void setUp() {
        GesuchDokumentKommentar kommentar = new GesuchDokumentKommentar();
        when(dokumentKommentarRepository.getByGesuchDokumentId(any()))
            .thenReturn(List.of(kommentar));
        when(
            gesuchDokumentKommentarHistoryRepository.getGesuchDokumentKommentarOfGesuchDokumentAtRevision(any(), any())
        )
            .thenReturn(List.of(kommentar));
        GesuchDokument gesuchDokument = new GesuchDokument();
        gesuchDokument.setId(UUID.randomUUID());
        GesuchTranche tranche = new GesuchTranche().setGesuchDokuments(List.of(gesuchDokument));
        tranche.setId(UUID.randomUUID());
        gesuchDokument.setGesuchTranche(tranche);
        when(gesuchTrancheRepository.requireById(any())).thenReturn(tranche);
        Gesuch gesuch = new Gesuch();
        gesuch.setId(UUID.randomUUID());
        tranche.setGesuch(gesuch);
        when(gesuchDokumentRepository.requireById(any())).thenReturn(gesuchDokument);
        when(gesuchDokumentHistoryRepository.findInHistoryById(any())).thenReturn(gesuchDokument);
        when(gesuchTrancheHistoryService.getCurrentOrHistoricalTrancheForGS(any())).thenReturn(tranche);
        when(gesuchTrancheHistoryRepository.getLatestWhereGesuchStatusChangedToEingereicht(any(), any()))
            .thenReturn(Optional.empty());
    }

    @Test
    @TestAsGesuchsteller
    // Gesuchsteller should be able to read all comments of a gesuch document
    void resourceShouldReturnCommentsOfADokument() {
        doNothing().when(gesuchDokumentAuthorizer).canGetGesuchDokumentKommentar(any());
        when(gesuchDokumentRepository.requireById(any())).thenReturn(
            new GesuchDokument()
                .setGesuchTranche(
                    new GesuchTranche()
                        .setGesuch(
                            new Gesuch()
                                .setAusbildung(
                                    new Ausbildung()
                                        .setFall(
                                            new Fall()
                                                .setGesuchsteller(
                                                    (Benutzer) new Benutzer()
                                                        .setId(UUID.fromString(GESUCHSTELLER_TEST_ID))
                                                )
                                        )
                                )
                        )
                )
        );

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

    @TestAsGesuchsteller
    @Test
    void testPageValidationForAusstehendeCustomGesuchDokument() {
        // arrange
        var gesuch = GesuchTestUtil.setupValidGesuchInState(Gesuchstatus.IN_BEARBEITUNG_GS);
        var tranche = gesuch.getGesuchTranchen().get(0);
        tranche.getGesuchFormular().getPersonInAusbildung().setGeburtsdatum(LocalDate.now().minusYears(16));
        tranche.getGesuchFormular()
            .getPersonInAusbildung()
            .setNationalitaet(new Land().setLaendercodeBfs(WellKnownLand.CHE.getLaendercodeBfs()));

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
        customGesuchDokument1.setStatus(GesuchDokumentStatus.AUSSTEHEND);
        customGesuchDokument1.setDokumente(List.of(new Dokument()));
        customGesuchDokument1.setCustomDokumentTyp(customDokumentTyp1);

        GesuchDokument customGesuchDokument2 = new GesuchDokument();
        customGesuchDokument2.setStatus(GesuchDokumentStatus.AUSSTEHEND);
        customGesuchDokument2.setDokumente(List.of());
        customGesuchDokument2.setCustomDokumentTyp(customDokumentTyp2);

        for (DokumentTyp dokumentTyp : DokumentTyp.values()) {
            var gesuchDokument = new GesuchDokument();
            gesuchDokument.setStatus(GesuchDokumentStatus.AUSSTEHEND);
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
