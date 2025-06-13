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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.generated.dto.CreateAenderungsantragRequestDto;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class GesuchTrancheServiceTest {
    @InjectMock
    GesuchRepository gesuchRepository;
    private Gesuch gesuch;

    @InjectMock
    GesuchTrancheRepository gesuchTrancheRepository;

    @Inject
    GesuchTrancheService gesuchTrancheService;
    @InjectMock
    GesuchTrancheStatusService gesuchTrancheStatusService;

    @BeforeEach
    void setUp() {
        gesuch = new Gesuch().setGesuchTranchen(
            List.of(
                new GesuchTranche()
                    .setTyp(GesuchTrancheTyp.TRANCHE)
                    .setGueltigkeit(new DateRange(LocalDate.MIN, LocalDate.MAX))
            )
        );
    }

    @TestAsGesuchsteller
    @Test
    void onlyOneAenderungShouldBeAllowed() {
        // arrange
        gesuch.getGesuchTranchen().get(0).setTyp(GesuchTrancheTyp.AENDERUNG);
        gesuch.getGesuchTranchen().get(0).setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS);
        // act & assert
        assertTrue(gesuchTrancheService.openAenderungAlreadyExists(gesuch));
    }

    @TestAsGesuchsteller
    @Test
    void onlyOneAenderungShouldBeAllowed_Tranche() {
        // arrange
        gesuch.getGesuchTranchen().get(0).setTyp(GesuchTrancheTyp.TRANCHE);
        gesuch.getGesuchTranchen().get(0).setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS);
        // assert
        assertFalse(gesuchTrancheService.openAenderungAlreadyExists(gesuch));
    }

    @TestAsGesuchsteller
    @Test
    void aenderungShouldBeAllowedWhenStateAbgelehnt() {
        // arrange
        gesuch.getGesuchTranchen().get(0).setTyp(GesuchTrancheTyp.AENDERUNG);
        gesuch.getGesuchTranchen().get(0).setStatus(GesuchTrancheStatus.ABGELEHNT);
        // assert
        assertFalse(gesuchTrancheService.openAenderungAlreadyExists(gesuch));
    }

    @TestAsGesuchsteller
    @Test
    void aenderungShouldBeAllowedWhenStateAbgelehnt_Tranche() {
        // arrange
        gesuch.getGesuchTranchen().get(0).setTyp(GesuchTrancheTyp.TRANCHE);
        gesuch.getGesuchTranchen().get(0).setStatus(GesuchTrancheStatus.ABGELEHNT);
        // assert
        assertFalse(gesuchTrancheService.openAenderungAlreadyExists(gesuch));
    }

    @TestAsGesuchsteller
    @Test
    void aenderungShouldBeAllowedWhenStateAngenommen() {
        // arrange
        gesuch.getGesuchTranchen().get(0).setTyp(GesuchTrancheTyp.AENDERUNG);
        gesuch.getGesuchTranchen().get(0).setStatus(GesuchTrancheStatus.AKZEPTIERT);
        // assert
        assertFalse(gesuchTrancheService.openAenderungAlreadyExists(gesuch));
    }

    @TestAsGesuchsteller
    @Test
    void aenderungShouldBeAllowedWhenStateAngenommen_Tranche() {
        // arrange
        gesuch.getGesuchTranchen().get(0).setTyp(GesuchTrancheTyp.TRANCHE);
        gesuch.getGesuchTranchen().get(0).setStatus(GesuchTrancheStatus.AKZEPTIERT);
        // assert
        assertFalse(gesuchTrancheService.openAenderungAlreadyExists(gesuch));
    }

    @TestAsGesuchsteller
    @Test
    void gsShouldBeAbleToDeleteAenderungTest(){
        // arrange
        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuch.getGesuchTranchen().get(0));
        when(gesuchTrancheRepository.deleteById(any())).thenReturn(true);
        when(gesuchTrancheRepository.findById(any())).thenReturn(gesuch.getGesuchTranchen().get(0));
        gesuch.getGesuchTranchen().get(0).setTyp(GesuchTrancheTyp.AENDERUNG);
        gesuch.getGesuchTranchen().get(0).setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS);
        gesuch.getGesuchTranchen().get(0).setId(UUID.randomUUID());
        //assert
        assertDoesNotThrow(() -> gesuchTrancheService.deleteAenderung(gesuch.getGesuchTranchen().get(0).getId()));
    }

    @TestAsGesuchsteller
    @Test
    @Description("Aenderung create should only be possible when Gesuchstatus is IN_FREIGABE or VERFUEGT")
    void aenderungEinreichenAllowedStatesTest() {
        // arrange
        gesuch.getGesuchTranchen().get(0).setTyp(GesuchTrancheTyp.AENDERUNG);
        gesuch.getGesuchTranchen().get(0).setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS);
        gesuch.getGesuchTranchen().get(0).setId(UUID.randomUUID());
        gesuch.setGesuchStatus(Gesuchstatus.EINGEREICHT);
        gesuch.getGesuchTranchen().get(0).setGesuch(gesuch);
        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuch.getGesuchTranchen().get(0));
        when(gesuchTrancheRepository.requireAenderungById(any())).thenReturn(gesuch.getGesuchTranchen().get(0));
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);

        CreateAenderungsantragRequestDto requestDto = new CreateAenderungsantragRequestDto();

        assertThrows(
            ForbiddenException.class,
            () -> gesuchTrancheService.createAenderungsantrag(gesuch.getId(), requestDto)
        );
        Mockito.doNothing().when(gesuchTrancheStatusService).triggerStateMachineEvent(any(), any());

        gesuch.setGesuchStatus(Gesuchstatus.IN_FREIGABE);
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        assertDoesNotThrow(() -> gesuchTrancheService.aenderungEinreichen(gesuch.getGesuchTranchen().get(0).getId()));

        gesuch.setGesuchStatus(Gesuchstatus.VERFUEGT);
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        assertDoesNotThrow(() -> gesuchTrancheService.aenderungEinreichen(gesuch.getGesuchTranchen().get(0).getId()));
    }
}
