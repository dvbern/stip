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

package ch.dvbern.stip.api.gesuchtranche.service;

import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.dokument.entity.CustomDokumentTyp;
import ch.dvbern.stip.api.dokument.entity.Dokument;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.service.RequiredDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuchtranchehistory.service.GesuchTrancheHistoryService;
import ch.dvbern.stip.api.unterschriftenblatt.service.UnterschriftenblattService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class GesuchServiceDokumenteToUploadFlagsTest {
    @InjectMock
    GesuchTrancheValidatorService gesuchTrancheValidatorService;

    @InjectMock
    GesuchRepository gesuchRepository;
    private Gesuch gesuch;

    @InjectMock
    GesuchTrancheRepository gesuchTrancheRepository;

    @InjectMock
    RequiredDokumentService requiredDokumentService;

    @InjectMock
    UnterschriftenblattService unterschriftenblattService;

    @InjectMock
    GesuchTrancheHistoryService gesuchTrancheHistoryService;

    @Inject
    GesuchTrancheService gesuchTrancheService;

    GesuchTranche tranche1;
    GesuchTranche tranche2;

    GesuchDokument gesuchDokumentOfTranche1;
    GesuchDokument gesuchDokumentOfTranche2;
    DokumentTyp dokType = DokumentTyp.AUSBILDUNG_BESTAETIGUNG_AUSBILDUNGSSTAETTE;

    @BeforeEach
    void setUp() {
        // ignore unterschriftenblatt for this tesst
        when(unterschriftenblattService.getUnterschriftenblaetterToUpload(any())).thenReturn(List.of());
        // prepare a gesuch with 2 tranchen
        gesuch = new Gesuch().setGesuchTranchen(
            List.of(
                new GesuchTranche(),
                new GesuchTranche()
            )
        );
        // prepare tranchen for tests
        tranche1 = gesuch.getGesuchTranchen().get(0);
        tranche1.setId(UUID.randomUUID());
        tranche1.setTyp(GesuchTrancheTyp.TRANCHE);
        tranche1.setGesuch(gesuch);
        tranche2 = gesuch.getGesuchTranchen().get(1);
        tranche2.setId(UUID.randomUUID());
        tranche2.setTyp(GesuchTrancheTyp.TRANCHE);
        tranche2.setGesuch(gesuch);

        // prepare gesuchformular for tranche 1
        GesuchFormular gesuchFormular1 = new GesuchFormular();
        gesuchFormular1.setTranche(tranche1);
        tranche1.setGesuchFormular(gesuchFormular1);

        // prepare gesuchformular for tranche 2
        GesuchFormular gesuchFormular2 = new GesuchFormular();
        gesuchFormular2.setTranche(tranche2);
        tranche2.setGesuchFormular(gesuchFormular2);

        // prepare a (normal) gesuchdokument for each tranche
        gesuchDokumentOfTranche1 = new GesuchDokument().setGesuchTranche(tranche1).setDokumentTyp(dokType);
        tranche1.setGesuchDokuments(List.of(gesuchDokumentOfTranche1));
        gesuchDokumentOfTranche2 = new GesuchDokument().setGesuchTranche(tranche2).setDokumentTyp(dokType);
        tranche2.setGesuchDokuments(List.of(gesuchDokumentOfTranche2));

        // overall arrange
        when(gesuchTrancheRepository.requireById(tranche1.getId())).thenReturn(tranche1);
        when(gesuchTrancheRepository.requireById(tranche2.getId())).thenReturn(tranche2);
        when(gesuchTrancheHistoryService.getLatestTranche(tranche1.getId())).thenReturn(tranche1);
        when(gesuchTrancheHistoryService.getLatestTranche(tranche2.getId())).thenReturn(tranche2);
    }

    @Test
    void testGSCanDokumenteUebermitteln() {
        /*
         * Gesuchstatus must be EHLENDE_DOKUMENTE
        */
        // arrange
        when(gesuchTrancheRepository.requireById(tranche1.getId())).thenReturn(tranche1);
        when(gesuchTrancheRepository.requireById(tranche2.getId())).thenReturn(tranche2);
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);
        // act
        var dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUploadSB(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getGsCanDokumenteUebermitteln(), is(false));

        /*
         * There must be NO required documents
         */
        // arrange
        gesuch.setGesuchStatus(Gesuchstatus.FEHLENDE_DOKUMENTE);
        when(requiredDokumentService.getGSCanFehlendeDokumenteEinreichen(any())).thenCallRealMethod();
        when(requiredDokumentService.getRequiredDokumentsForGesuchFormular(any())).thenReturn(List.of());
        when(requiredDokumentService.getRequiredCustomDokumentsForGesuchFormular(any())).thenReturn(List.of());
        // act
        dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUploadSB(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getGsCanDokumenteUebermitteln(), is(true));

        // arrange
        when(requiredDokumentService.getRequiredDokumentsForGesuchFormular(any())).thenReturn(List.of(DokumentTyp.EK_BELEG_BETREUUNGSKOSTEN_KINDER));
        when(requiredDokumentService.getRequiredCustomDokumentsForGesuchFormular(any())).thenReturn(List.of(new CustomDokumentTyp()));
        // act
        dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUploadSB(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getGsCanDokumenteUebermitteln(), is(false));
    }

    @Test
    void sbCanFehlendeDokumenteUebermittelnTest() {
        /*
         * Gesuchstatus must be IN_BEARBEITUNG_SB
         */
        // arrange
        when(requiredDokumentService.getGSCanFehlendeDokumenteEinreichen(any())).thenReturn(false);
        when(requiredDokumentService.getSBCanFehlendeDokumenteUebermitteln(any())).thenCallRealMethod();
        when(requiredDokumentService.getRequiredDokumentsForGesuchFormular(any())).thenReturn(List.of());
        when(requiredDokumentService.getRequiredCustomDokumentsForGesuchFormular(any())).thenReturn(List.of());
        gesuch.setGesuchStatus(Gesuchstatus.BEREIT_FUER_BEARBEITUNG);
        // act
        var dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUploadSB(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getSbCanFehlendeDokumenteUebermitteln(), is(false));

        /*
         * At least one of the following must exist:
         * a change made by SB
         * a newly added custom GesuchDokument
         */
        // arrange
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);
        gesuchDokumentOfTranche1.setStatus(Dokumentstatus.AKZEPTIERT);
        gesuchDokumentOfTranche2.setStatus(Dokumentstatus.AKZEPTIERT);
        // act
        dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUploadSB(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getSbCanFehlendeDokumenteUebermitteln(), is(false));

        // arrange
        gesuchDokumentOfTranche1.setStatus(Dokumentstatus.AUSSTEHEND);
        gesuchDokumentOfTranche2.setStatus(Dokumentstatus.AKZEPTIERT);
        when(requiredDokumentService.getRequiredDokumentsForGesuchFormular(any())).thenReturn(List.of(gesuchDokumentOfTranche1.getDokumentTyp()));

        // act
        dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUploadSB(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getSbCanFehlendeDokumenteUebermitteln(), is(true));

        /*
         * check for ABGELEHNTE GesuchDokuments as well
         */
        // arrange
        gesuchDokumentOfTranche1.setStatus(Dokumentstatus.ABGELEHNT);
        gesuchDokumentOfTranche2.setStatus(Dokumentstatus.AKZEPTIERT);
        // act
        dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUploadSB(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getSbCanFehlendeDokumenteUebermitteln(), is(true));

        /*
         *  check that all tranchen of type aenderung are in tranchestatus UEBERPRUEFEN,
         *  otherwise the flag is false
         */
        // arrange
        tranche2.setTyp(GesuchTrancheTyp.AENDERUNG);
        tranche2.setStatus(GesuchTrancheStatus.UEBERPRUEFEN);
        dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUploadSB(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getSbCanFehlendeDokumenteUebermitteln(), is(true));

        // arrange
        tranche2.setStatus(GesuchTrancheStatus.AKZEPTIERT);
        // act
        dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUploadSB(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getSbCanFehlendeDokumenteUebermitteln(), is(true));

        /*
         * check that all files have been processed by SB
         */
        // arrange
        gesuchDokumentOfTranche1.setStatus(Dokumentstatus.AUSSTEHEND);
        gesuchDokumentOfTranche1.setDokumente(List.of(new Dokument()));
        // act
        dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUploadSB(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getSbCanFehlendeDokumenteUebermitteln(), is(false));

        /*
         * test with documents appearing in one producer
         * (e.g. when SB makes a change which requires a new document)
         */
        // arrange
        gesuchDokumentOfTranche2.setDokumente(List.of());
        gesuchDokumentOfTranche2.setStatus(Dokumentstatus.AUSSTEHEND);
        tranche2.setStatus(GesuchTrancheStatus.UEBERPRUEFEN);
        when(requiredDokumentService.getRequiredDokumentsForGesuchFormular(any())).thenReturn(List.of(DokumentTyp.EK_BELEG_BETREUUNGSKOSTEN_KINDER));
        // act
        dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUploadSB(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getSbCanFehlendeDokumenteUebermitteln(), is(false));
    }

    @Test
    void sbCanBearbeitungAbschliessenTest() {
        /*
         * all GesuchDokuments must be accepted
         */
        // arrange
        when(requiredDokumentService.getGSCanFehlendeDokumenteEinreichen(any())).thenReturn(false);
        when(requiredDokumentService.getSBCanBearbeitungAbschliessen(any())).thenCallRealMethod();
        Mockito.doNothing().when(gesuchTrancheValidatorService).validateBearbeitungAbschliessen(any());
        when(requiredDokumentService.getRequiredDokumentsForGesuchFormular(any())).thenReturn(List.of());
        when(requiredDokumentService.getRequiredCustomDokumentsForGesuchFormular(any())).thenReturn(List.of());
        gesuchDokumentOfTranche1.setStatus(Dokumentstatus.AKZEPTIERT);
        gesuchDokumentOfTranche2.setStatus(Dokumentstatus.AKZEPTIERT);
        // act
        var dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUploadSB(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getSbCanBearbeitungAbschliessen(), is(true));

        // arrange
        when(requiredDokumentService.getRequiredDokumentsForGesuchFormular(any())).thenReturn(List.of());
        when(requiredDokumentService.getRequiredCustomDokumentsForGesuchFormular(any())).thenReturn(List.of());
        gesuchDokumentOfTranche1.setStatus(Dokumentstatus.AKZEPTIERT);
        gesuchDokumentOfTranche2.setStatus(Dokumentstatus.ABGELEHNT);
        // act
        dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUploadSB(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getSbCanBearbeitungAbschliessen(), is(false));

        /*
         * No GesuchDokument should be required
         */
        // arrange
        when(requiredDokumentService.getRequiredDokumentsForGesuchFormular(any())).thenReturn(List.of(DokumentTyp.EK_BELEG_BETREUUNGSKOSTEN_KINDER));
        when(requiredDokumentService.getRequiredCustomDokumentsForGesuchFormular(any())).thenReturn(List.of());
        gesuchDokumentOfTranche1.setStatus(Dokumentstatus.AKZEPTIERT);
        gesuchDokumentOfTranche2.setStatus(Dokumentstatus.AKZEPTIERT);
        // act
        dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUploadSB(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getSbCanBearbeitungAbschliessen(), is(false));

        /*
         * also test validation
         */
        // arrange
        Mockito.doThrow(ValidationsException.class).when(gesuchTrancheValidatorService).validateBearbeitungAbschliessen(any());
        when(requiredDokumentService.getRequiredDokumentsForGesuchFormular(any())).thenReturn(List.of());
        when(requiredDokumentService.getRequiredCustomDokumentsForGesuchFormular(any())).thenReturn(List.of());
        gesuchDokumentOfTranche1.setStatus(Dokumentstatus.AKZEPTIERT);
        gesuchDokumentOfTranche2.setStatus(Dokumentstatus.AKZEPTIERT);
        // act
        dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUploadSB(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getSbCanBearbeitungAbschliessen(), is(false));
    }
}
