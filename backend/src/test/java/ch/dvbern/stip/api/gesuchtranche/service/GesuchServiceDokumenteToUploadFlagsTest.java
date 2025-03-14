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
public class GesuchServiceDokumenteToUploadFlagsTest {

    @InjectMock
    GesuchRepository gesuchRepository;
    private Gesuch gesuch;

    @InjectMock
    GesuchTrancheRepository gesuchTrancheRepository;

    @InjectMock
    RequiredDokumentService requiredDokumentService;

    @InjectMock
    UnterschriftenblattService unterschriftenblattService;

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
    }

    /*
     * flag is true when
     * gesuchstatus must be FEHLENDE_DOKUMENTE
     * all required (also custom) gesuchdokuments of all tranchen must contain at least 1 file
     */
    @Test
    void testGSCanDokumenteUebermitteln() {
        // overall arrange
        when(gesuchTrancheRepository.requireById(tranche1.getId())).thenReturn(tranche1);
        when(gesuchTrancheRepository.requireById(tranche2.getId())).thenReturn(tranche2);

        /** condition1: Gesuchstatus must be FEHLENDE_DOKUMENTE **/
        // arrange
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);
        // act
        var dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUpload(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getGsCanDokumenteUebermitteln(), is(false));

        // reset gesuchstatus for next tests
        gesuch.setGesuchStatus(Gesuchstatus.FEHLENDE_DOKUMENTE);
        when(requiredDokumentService.getGSCanFehlendeDokumenteEinreichen(any())).thenCallRealMethod();

        /** condition2: no documents must be required anymore **/
        /* case: no missing documents */
        // arrange
        when(requiredDokumentService.getRequiredDokumentsForGesuchFormular(any())).thenReturn(List.of());
        when(requiredDokumentService.getRequiredCustomDokumentsForGesuchFormular(any())).thenReturn(List.of());

        // act
        dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUpload(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getGsCanDokumenteUebermitteln(), is(true));

        /* case: one normal missing & one custom missing document */
        // arrange
        when(requiredDokumentService.getRequiredDokumentsForGesuchFormular(any())).thenReturn(List.of(DokumentTyp.EK_BELEG_BETREUUNGSKOSTEN_KINDER));
        when(requiredDokumentService.getRequiredCustomDokumentsForGesuchFormular(any())).thenReturn(List.of(new CustomDokumentTyp()));
        // act
        dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUpload(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getGsCanDokumenteUebermitteln(), is(false));
    }

    @Test
    void sbCanFehlendeDokumenteUebermittelnTest() {
        when(requiredDokumentService.getGSCanFehlendeDokumenteEinreichen(any())).thenReturn(false);
        when(requiredDokumentService.getSBCanFehlendeDokumenteUebermitteln(any())).thenCallRealMethod();

        // ignore behaviour of producers for the following tests
        when(requiredDokumentService.getRequiredDokumentsForGesuchFormular(any())).thenReturn(List.of());
        when(requiredDokumentService.getRequiredCustomDokumentsForGesuchFormular(any())).thenReturn(List.of());

        /* condition 1:
        gesuchstatus is IN_BEARBEITUNG_SB
        or tranche of typ AENDERUNG is in tranchestatus UEBERPRUEFEN
         */
        // arrange
        gesuch.setGesuchStatus(Gesuchstatus.BEREIT_FUER_BEARBEITUNG);
        // act
        var dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUpload(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getSbCanFehlendeDokumenteUebermitteln(), is(false));

        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);

        /* condition 2:
        there are gesuchdokuments in status ausstehend without file (change made by sb, or custom dokument)
         */
        // arrange
        // all accepted -> no missing documents
        gesuchDokumentOfTranche1.setStatus(Dokumentstatus.AKZEPTIERT);
        gesuchDokumentOfTranche2.setStatus(Dokumentstatus.AKZEPTIERT);
        // act
        dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUpload(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getSbCanFehlendeDokumenteUebermitteln(), is(false));

        // 1 ausstehend, 1 denied -> missing documents
        // arrange
        gesuchDokumentOfTranche1.setStatus(Dokumentstatus.AUSSTEHEND);
        gesuchDokumentOfTranche2.setStatus(Dokumentstatus.AKZEPTIERT);
        // act
        dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUpload(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getSbCanFehlendeDokumenteUebermitteln(), is(true));

        /* condition 3:
        or gesuchdokuments in status abgelehnt
         */
        // arrange
        gesuchDokumentOfTranche1.setStatus(Dokumentstatus.ABGELEHNT);
        gesuchDokumentOfTranche2.setStatus(Dokumentstatus.AKZEPTIERT);
        // act
        dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUpload(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getSbCanFehlendeDokumenteUebermitteln(), is(true));

        /* check AENDERUNG status as well */
        // check that all tranchen of type aenderung are in tranchestatus UEBERPRUEFEN, otherwise the flag is false

        // set tranche 2 to be an AENDERUNG
        tranche2.setTyp(GesuchTrancheTyp.AENDERUNG);
        // arrange
        tranche2.setStatus(GesuchTrancheStatus.UEBERPRUEFEN);
        dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUpload(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getSbCanFehlendeDokumenteUebermitteln(), is(true));

        // change status of aenderung
        // arrange
        tranche2.setStatus(GesuchTrancheStatus.AKZEPTIERT);
        // act
        dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUpload(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getSbCanFehlendeDokumenteUebermitteln(), is(false));

        // check that all files have been processed
        // arrange
        gesuchDokumentOfTranche2.setStatus(Dokumentstatus.AUSSTEHEND);
        gesuchDokumentOfTranche2.setDokumente(List.of(new Dokument()));
        // act
        dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUpload(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getSbCanFehlendeDokumenteUebermitteln(), is(false));

        // test with documents appearing in one producer
        // arrange
        tranche2.setStatus(GesuchTrancheStatus.UEBERPRUEFEN);
        when(requiredDokumentService.getRequiredDokumentsForGesuchFormular(any())).thenReturn(List.of(DokumentTyp.EK_BELEG_BETREUUNGSKOSTEN_KINDER));
        // act
        dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUpload(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getSbCanFehlendeDokumenteUebermitteln(), is(true));
    }

    /*
     * all (existing)gesuchdokuments over all tranchen are AKZEPTIERT
     * no gesuchdokument is required (e.g. after a change by SB)
     * the logic used in pageValidation (endpoint bearbeitungAbschliessen) should also be considered during evaluation
     * of this flag
     */
    @Test
    void sbCanBearbeitungAbschliessenTest() {
        when(requiredDokumentService.getGSCanFehlendeDokumenteEinreichen(any())).thenReturn(false);
        when(requiredDokumentService.getSBCanBearbeitungAbschliessen(any())).thenCallRealMethod();
        // ignore validation for the moment
        Mockito.doNothing().when(requiredDokumentService).validateBearbeitungAbschliessenForAllTranchen(any());

        // case: no dokument required, all accepted (ignore page validation)
        // arrange
        when(requiredDokumentService.getRequiredDokumentsForGesuchFormular(any())).thenReturn(List.of());
        when(requiredDokumentService.getRequiredCustomDokumentsForGesuchFormular(any())).thenReturn(List.of());
        gesuchDokumentOfTranche1.setStatus(Dokumentstatus.AKZEPTIERT);
        gesuchDokumentOfTranche2.setStatus(Dokumentstatus.AKZEPTIERT);
        // act
        var dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUpload(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getSbCanBearbeitungAbschliessen(), is(true));

        // case: no dokument required, one not accepted (ignore page validation)
        // arrange
        when(requiredDokumentService.getRequiredDokumentsForGesuchFormular(any())).thenReturn(List.of());
        when(requiredDokumentService.getRequiredCustomDokumentsForGesuchFormular(any())).thenReturn(List.of());
        gesuchDokumentOfTranche1.setStatus(Dokumentstatus.AKZEPTIERT);
        gesuchDokumentOfTranche2.setStatus(Dokumentstatus.ABGELEHNT);
        // act
        dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUpload(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getSbCanBearbeitungAbschliessen(), is(false));

        // case: one doc required,  (ignore page validation)
        // arrange
        when(requiredDokumentService.getRequiredDokumentsForGesuchFormular(any())).thenReturn(List.of(DokumentTyp.EK_BELEG_BETREUUNGSKOSTEN_KINDER));
        when(requiredDokumentService.getRequiredCustomDokumentsForGesuchFormular(any())).thenReturn(List.of());
        gesuchDokumentOfTranche1.setStatus(Dokumentstatus.AKZEPTIERT);
        gesuchDokumentOfTranche2.setStatus(Dokumentstatus.AKZEPTIERT);
        // act
        dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUpload(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getSbCanBearbeitungAbschliessen(), is(false));

        //test with pageValidation
        Mockito.doThrow(ValidationsException.class).when(requiredDokumentService).validateBearbeitungAbschliessenForAllTranchen(any());
        // case: no dokument required, all accepted
        // arrange
        when(requiredDokumentService.getRequiredDokumentsForGesuchFormular(any())).thenReturn(List.of());
        when(requiredDokumentService.getRequiredCustomDokumentsForGesuchFormular(any())).thenReturn(List.of());
        gesuchDokumentOfTranche1.setStatus(Dokumentstatus.AKZEPTIERT);
        gesuchDokumentOfTranche2.setStatus(Dokumentstatus.AKZEPTIERT);
        // act
        dokumenteToUploadDto = gesuchTrancheService.getDokumenteToUpload(tranche1.getId());
        // assert
        assertThat(dokumenteToUploadDto.getSbCanBearbeitungAbschliessen(), is(false));
    }
}
