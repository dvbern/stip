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

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuchtranchehistory.repo.GesuchTrancheHistoryRepository;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.GesuchTrancheUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchUpdateDto;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.ForbiddenException;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus.GESUCHSTELLER_CAN_AENDERUNG_EINREICHEN;
import static ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus.IN_BEARBEITUNG_GS;
import static ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus.SACHBEARBEITER_CAN_EDIT;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class GesuchAuthorizerUpdateAenderungTest {
    @Inject
    BenutzerService benutzerService;
    @InjectMock
    private GesuchTrancheRepository gesuchTrancheRepository;
    @InjectMock
    private GesuchTrancheHistoryRepository gesuchTrancheHistoryRepository;
    @InjectMock
    private GesuchRepository gesuchRepository;
    @InjectMock
    private SozialdienstService sozialdienstService;

    @Inject
    GesuchTrancheAuthorizer gesuchTrancheAuthorizer;

    Gesuch gesuch;
    GesuchTranche aenderung;
    GesuchTranche tranche;

    GesuchUpdateDto gesuchUpdateDto;

    @BeforeEach
    void setUp() {
        // gesuch in status verfuegt
        gesuch = TestUtil.getFullGesuch();
        gesuch.setGesuchStatus(Gesuchstatus.VERSENDET);
        gesuch.getAusbildung().setFall(new Fall());

        // gs created an aenderung
        aenderung = new GesuchTranche();
        aenderung.setGesuch(gesuch);
        aenderung.setTyp(GesuchTrancheTyp.AENDERUNG);

        // reset gesuchtranchen to modifiable list
        ArrayList<GesuchTranche> tranches = new ArrayList<>();
        tranche = gesuch.getGesuchTranchen().get(0);
        tranches.addAll(gesuch.getGesuchTranchen());
        tranches.add(aenderung);
        gesuch.setGesuchTranchen(tranches);

        GesuchTrancheUpdateDto gesuchTrancheUpdateDto = new GesuchTrancheUpdateDto();
        gesuchTrancheUpdateDto.setId(UUID.randomUUID());
        gesuchUpdateDto = new GesuchUpdateDto();
        gesuchUpdateDto.setGesuchTrancheToWorkWith(gesuchTrancheUpdateDto);

        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(false);

        when(gesuchTrancheRepository.requireById(any())).thenReturn(aenderung);
        when(gesuchTrancheRepository.findById(any())).thenReturn(aenderung);
        when(gesuchTrancheRepository.findByIdOptional(any())).thenReturn(Optional.of(aenderung));
        when(gesuchTrancheHistoryRepository.getLatestExistingVersionOfTranche(any()))
            .thenReturn(Optional.of(aenderung));
        when(gesuchRepository.requireGesuchByTrancheId(any())).thenReturn(gesuch);
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
    }

    @TestAsSachbearbeiter
    @Test
    void canUpdateAenderungAsSB() {
        gesuch.getAusbildung().getFall().setGesuchsteller(benutzerService.getCurrentBenutzer());

        GESUCHSTELLER_CAN_AENDERUNG_EINREICHEN.stream()
            .filter(trancheStatus -> trancheStatus != IN_BEARBEITUNG_GS)
            .forEach(trancheStatus -> {
                aenderung.setStatus(trancheStatus);
                assertThrows(ForbiddenException.class, () -> {
                    gesuchTrancheAuthorizer.canUpdateTranche(aenderung);
                });
            });

        SACHBEARBEITER_CAN_EDIT.forEach(trancheStatus -> {
            aenderung.setStatus(trancheStatus);
            assertDoesNotThrow(() -> {
                gesuchTrancheAuthorizer.canUpdateTranche(aenderung);
            });
        });

        aenderung.setStatus(GesuchTrancheStatus.AKZEPTIERT);
        assertThrows(ForbiddenException.class, () -> {
            gesuchTrancheAuthorizer.canUpdateTranche(aenderung);
        });

        aenderung.setStatus(GesuchTrancheStatus.ABGELEHNT);
        assertThrows(ForbiddenException.class, () -> {
            gesuchTrancheAuthorizer.canUpdateTranche(aenderung);
        });
    }

    @TestAsSachbearbeiter
    // @Test todo KSTIP-2091: handle this case
    @Description(
        "Despite containing GS Role in default roles, SB (of this Gesuch) should be able to update when in GesuchTrancheStatus IN_BEARBEITUNG_GS"
    )
    void canUpdateAenderungAsInBearbeitungGSAsSBOfGesuch() {
        Benutzer benutzer = benutzerService.getCurrentBenutzer();

        // add role GS to SB, as it is existing in default roles
        Rolle gsRole = new Rolle();
        gsRole.setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER);
        Rolle sbRole = new Rolle();
        gsRole.setKeycloakIdentifier(OidcConstants.ROLE_SACHBEARBEITER);
        benutzer.setRollen(Set.of(gsRole, sbRole));

        // set sb to be GS of gesuch
        gesuch.getAusbildung().getFall().setGesuchsteller(benutzer);

        aenderung.setStatus(IN_BEARBEITUNG_GS);
        assertDoesNotThrow(() -> {
            gesuchTrancheAuthorizer.canUpdateTranche(aenderung);
        });
    }

    @TestAsSachbearbeiter
    @Test
    @Description(
        "Despite containing GS Role in default roles, SB (of another Gesuch) should not be able to update when in GesuchTrancheStatus IN_BEARBEITUNG_GS"
    )
    void canNotUpdateAenderungasInBearbeitungGSAsSBOfAnotherGesuch() {
        Benutzer benutzer = benutzerService.getCurrentBenutzer();
        Benutzer anotherBenutzer = benutzerService.getCurrentBenutzer();
        anotherBenutzer.setId(UUID.randomUUID());
        anotherBenutzer.setKeycloakId(UUID.randomUUID().toString());
        // add role GS to SB, as it is existing in default roles
        Rolle gsRole = new Rolle();
        gsRole.setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER);
        Rolle sbRole = new Rolle();
        gsRole.setKeycloakIdentifier(OidcConstants.ROLE_SACHBEARBEITER);
        benutzer.setRollen(Set.of(gsRole, sbRole));

        // current sb is NOT GS of gesuch
        gesuch.getAusbildung().getFall().setGesuchsteller(anotherBenutzer);

        aenderung.setStatus(IN_BEARBEITUNG_GS);
        assertThrows(ForbiddenException.class, () -> {
            gesuchTrancheAuthorizer.canUpdateTranche(aenderung);
        });
    }

    @Test
    @TestAsGesuchsteller
    void canUpdateAenderungAsGS() {
        gesuch.getAusbildung().getFall().setGesuchsteller(benutzerService.getCurrentBenutzer());

        GESUCHSTELLER_CAN_AENDERUNG_EINREICHEN.forEach(trancheStatus -> {
            aenderung.setStatus(trancheStatus);
            assertDoesNotThrow(() -> {
                gesuchTrancheAuthorizer.canUpdateTranche(aenderung);
            });
        });

        // todo: KSTIP-2091: remove filter clause when double roles (GS & SB) are not existing anymre.
        // until then, an SB should also be able to update an aenderung IN_BEARBEITUNG_GS when he/she is the GS of a
        // gesuch
        SACHBEARBEITER_CAN_EDIT.stream()
            .filter(trancheStatus -> trancheStatus != IN_BEARBEITUNG_GS)
            .forEach(trancheStatus -> {
                aenderung.setStatus(trancheStatus);
                assertThrows(ForbiddenException.class, () -> {
                    gesuchTrancheAuthorizer.canUpdateTranche(aenderung);
                });
            });

        aenderung.setStatus(GesuchTrancheStatus.AKZEPTIERT);
        assertThrows(ForbiddenException.class, () -> {
            gesuchTrancheAuthorizer.canUpdateTranche(aenderung);
        });

        aenderung.setStatus(GesuchTrancheStatus.ABGELEHNT);
        assertThrows(ForbiddenException.class, () -> {
            gesuchTrancheAuthorizer.canUpdateTranche(aenderung);
        });
    }

}
