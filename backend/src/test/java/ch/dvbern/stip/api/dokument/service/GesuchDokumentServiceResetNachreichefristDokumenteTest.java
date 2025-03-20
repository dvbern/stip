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

package ch.dvbern.stip.api.dokument.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@RequiredArgsConstructor
public class GesuchDokumentServiceResetNachreichefristDokumenteTest {
    @Inject
    GesuchDokumentService gesuchDokumentService;
    @InjectMock
    GesuchDokumentRepository gesuchDokumentRepository;

    @InjectMock
    DokumentstatusService dokumentstatusService;

    Gesuch gesuch;
    GesuchDokument gesuchDokument1;
    GesuchDokument gesuchDokument2;
    GesuchTranche tranche;
    final LocalDate nachfrist = LocalDate.now().plusDays(3);

    @BeforeEach
    void setup() {
        gesuch = TestUtil.getFullGesuch();
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);
        tranche = gesuch.getGesuchTranchen().get(0);
        gesuchDokument1 = new GesuchDokument();
        gesuchDokument2 = new GesuchDokument();;

        gesuchDokument1.setDokumentTyp(DokumentTyp.EK_VERDIENST);
        gesuchDokument1.setStatus(Dokumentstatus.AUSSTEHEND);
        gesuchDokument1.setId(UUID.randomUUID());
        gesuchDokument1.setGesuchTranche(tranche);
        gesuchDokument2.setDokumentTyp(DokumentTyp.AUSBILDUNG_BESTAETIGUNG_AUSBILDUNGSSTAETTE);
        gesuchDokument2.setStatus(Dokumentstatus.AUSSTEHEND);
        gesuchDokument2.setId(UUID.randomUUID());
        gesuchDokument2.setGesuchTranche(tranche);

        when(gesuchDokumentRepository.requireById(gesuchDokument1.getId())).thenReturn(gesuchDokument1);
        when(gesuchDokumentRepository.requireById(gesuchDokument2.getId())).thenReturn(gesuchDokument2);

        gesuch.getGesuchTranchen().get(0).getGesuchDokuments().clear();
        gesuch.getGesuchTranchen().get(0).getGesuchDokuments().add(gesuchDokument1);
        gesuch.getGesuchTranchen().get(0).getGesuchDokuments().add(gesuchDokument2);
        gesuch.setNachfristDokumente(nachfrist);
    }

    @Test
    void nachfristDokumenteShouldBeRemovedWhenAllAccepted() {
        Mockito.doNothing().when(dokumentstatusService).triggerStatusChange(any(), any());
        gesuchDokument1.setStatus(Dokumentstatus.AKZEPTIERT);
        gesuchDokumentService.gesuchDokumentAkzeptieren(gesuchDokument1.getId());
        assertEquals(gesuch.getNachfristDokumente(), nachfrist);
        gesuchDokument2.setStatus(Dokumentstatus.AKZEPTIERT);
        gesuchDokumentService.gesuchDokumentAkzeptieren(gesuchDokument2.getId());
        // since all documents are accepted, reset nachreichefrist
        assertNull(gesuch.getNachfristDokumente());

    }

    @Test
    void nachfristDokumenteShouldBeRemovedWhenAllAcceptedOnDifferentTranchen() {
        GesuchTranche tranche2 = new GesuchTranche();

        // reset gesuchtranchen of gesuch to modifiable list
        ArrayList<GesuchTranche> tranches = new ArrayList<>();
        tranches.addAll(gesuch.getGesuchTranchen());
        gesuch.setGesuchTranchen(tranches);

        gesuch.getGesuchTranchen().add(tranche2);
        tranche2.setGesuch(gesuch);

        Mockito.doNothing().when(dokumentstatusService).triggerStatusChange(any(), any());
        gesuchDokument1.setStatus(Dokumentstatus.AKZEPTIERT);
        gesuchDokumentService.gesuchDokumentAkzeptieren(gesuchDokument1.getId());

        // also check 2nd tranche with a new dokument
        GesuchDokument gesuchDokument3 = new GesuchDokument();
        gesuchDokument3.setDokumentTyp(DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_VATER);
        gesuchDokument3.setId(UUID.randomUUID());
        gesuchDokument3.setGesuchTranche(tranche);
        tranche2.setGesuchDokuments(new ArrayList<>());
        tranche2.getGesuchDokuments().add(gesuchDokument3);
        tranche2.getGesuchDokuments().add(gesuchDokument1);
        gesuchDokument3.setStatus(Dokumentstatus.AUSSTEHEND);

        assertEquals(gesuch.getNachfristDokumente(), nachfrist);
        gesuchDokument2.setStatus(Dokumentstatus.AKZEPTIERT);
        gesuchDokument3.setStatus(Dokumentstatus.AKZEPTIERT);

        gesuchDokumentService.gesuchDokumentAkzeptieren(gesuchDokument2.getId());
        // since all documents are accepted, reset nachreichefrist
        assertNull(gesuch.getNachfristDokumente());

    }

    // todo KSTIP-1849: also test this with several tranchen
}
