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

package ch.dvbern.stip.api.common.authorizer;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.GesuchTrancheAuthorizer;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.delegieren.entity.Delegierung;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuchtranchehistory.service.GesuchTrancheHistoryService;
import ch.dvbern.stip.api.sozialdienst.entity.Sozialdienst;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import ch.dvbern.stip.api.sozialdienstbenutzer.entity.SozialdienstBenutzer;
import jakarta.ws.rs.ForbiddenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Execution(ExecutionMode.CONCURRENT)
public class GesuchTrancheAuthorizerCanDeleteTest {
    private BenutzerService benutzerService;
    private Benutzer currentBenutzer;
    private Benutzer otherBenutzer;
    private Gesuch gesuch;
    private GesuchTrancheAuthorizer authorizer;
    private GesuchTranche gesuchTranche_inBearbeitungGS;

    private GesuchTrancheRepository gesuchTrancheRepository;
    private GesuchRepository gesuchRepository;
    private SozialdienstService sozialdienstService;
    private GesuchTrancheHistoryService gesuchTrancheHistoryService;

    @BeforeEach
    void setup() {
        benutzerService = Mockito.mock(BenutzerService.class);
        currentBenutzer = new Benutzer().setKeycloakId(UUID.randomUUID().toString());
        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER));
        UUID currentBenutzerId = UUID.randomUUID();
        currentBenutzer.setId(currentBenutzerId);

        gesuch = new Gesuch()
            .setAusbildung(
                new Ausbildung()
                    .setFall(
                        new Fall()
                            .setGesuchsteller(currentBenutzer)
                    )
            );

        gesuchTranche_inBearbeitungGS = new GesuchTranche()
            .setGesuch(gesuch)
            .setTyp(GesuchTrancheTyp.AENDERUNG)
            .setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS);

        UUID otherBenutzerId = UUID.randomUUID();
        otherBenutzer = new Benutzer();
        otherBenutzer.setId(otherBenutzerId);
        otherBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER));

        when(benutzerService.getCurrentBenutzer()).thenReturn(currentBenutzer);

        gesuchRepository = Mockito.mock(GesuchRepository.class);
        gesuchTrancheRepository = Mockito.mock(GesuchTrancheRepository.class);
        sozialdienstService = Mockito.mock(SozialdienstService.class);
        gesuchTrancheHistoryService = Mockito.mock(GesuchTrancheHistoryService.class);

        authorizer = new GesuchTrancheAuthorizer(
            benutzerService,
            gesuchTrancheRepository,
            gesuchRepository,
            sozialdienstService,
            gesuchTrancheHistoryService,
            null
        );

        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuchTranche_inBearbeitungGS);
        when(gesuchTrancheRepository.findById(any())).thenReturn(gesuchTranche_inBearbeitungGS);
        when(gesuchTrancheRepository.requireAenderungById(any())).thenReturn(gesuchTranche_inBearbeitungGS);
        when(gesuchRepository.requireGesuchByTrancheId(any())).thenReturn(gesuch);
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(false);
        when(gesuchTrancheHistoryService.getLatestTranche(any())).thenReturn(gesuchTranche_inBearbeitungGS);
    }

    @Test
    void gsCanotDeleteOwnAenderungInWrongStateTest() {
        // arrange
        final var gesuchTranche_wrongState = new GesuchTranche()
            .setGesuch(gesuch)
            .setStatus(GesuchTrancheStatus.AKZEPTIERT);

        when(gesuchTrancheRepository.requireById(any()))
            .thenReturn(gesuchTranche_wrongState);
        when(gesuchTrancheRepository.findById(any()))
            .thenReturn(gesuchTranche_wrongState);
        final var uuid = UUID.randomUUID();
        // assert
        assertThrows(ForbiddenException.class, () -> authorizer.canDeleteAenderung(uuid));
    }

    @Test
    void gsCanNotDeleteTrancheTest() {
        // arrange
        gesuchTranche_inBearbeitungGS.setTyp(GesuchTrancheTyp.TRANCHE);
        final var uuid = UUID.randomUUID();
        // assert
        assertThrows(ForbiddenException.class, () -> authorizer.canDeleteAenderung(uuid));
    }

    @Test
    void gsCanDeleteAenderungOnlyOwnAenderungTest() {
        // arrange
        gesuchTranche_inBearbeitungGS.setTyp(GesuchTrancheTyp.AENDERUNG);
        final var uuid = UUID.randomUUID();
        // assert
        assertDoesNotThrow(() -> authorizer.canDeleteAenderung(uuid));
    }

    @Test
    void gsCannotDeleteOtherAenderungTest() {
        // arrange
        currentBenutzer.setRollen(Set.of());
        final var uuid = UUID.randomUUID();
        gesuch.getAusbildung().getFall().setGesuchsteller(otherBenutzer);
        // assert
        assertThrows(ForbiddenException.class, () -> authorizer.canDeleteAenderung(uuid));
    }

    @Test
    void adminCanNotDeleteAenderungAenderungTest() {
        // arrange
        currentBenutzer.setRollen(
            Set.of(
                new Rolle()
                    .setKeycloakIdentifier(OidcConstants.ROLE_SACHBEARBEITER_ADMIN)
            )
        );
        gesuch.getAusbildung().getFall().setGesuchsteller(otherBenutzer);
        final var uuid = UUID.randomUUID();
        // assert
        assertThrows(ForbiddenException.class, () -> authorizer.canDeleteAenderung(uuid));
    }

    @Test
    void sbCanNotDeleteAenderungAenderungTest() {
        // arrange
        currentBenutzer.setRollen(
            Set.of(
                new Rolle()
                    .setKeycloakIdentifier(OidcConstants.ROLE_SACHBEARBEITER)
            )
        );
        final var uuid = UUID.randomUUID();
        gesuch.getAusbildung().getFall().setGesuchsteller(otherBenutzer);
        // assert
        assertThrows(ForbiddenException.class, () -> authorizer.canDeleteAenderung(uuid));
    }

    @Test
    void gsCannotAenderungEinreichenWhenGesuchDelegated() {
        // arrange
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(false);
        currentBenutzer.setRollen(
            Set.of(
                new Rolle()
                    .setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER)
            )
        );
        Fall fall = new Fall();
        fall.setGesuchsteller(new Benutzer());
        Sozialdienst sozialdienst = new Sozialdienst();
        sozialdienst.setId(UUID.randomUUID());
        Delegierung delegierung = new Delegierung();
        delegierung.setSozialdienst(sozialdienst);
        fall.setDelegierung(delegierung);
        Ausbildung ausbildung = new Ausbildung();
        ausbildung.setFall(fall);
        ausbildung.setGesuchs(List.of(gesuch));
        gesuch.setAusbildung(ausbildung);

        final var uuid = UUID.randomUUID();
        // gesuchTrancheRepository.requireAenderungById(gesuchTrancheId)
        // assert
        assertThrows(ForbiddenException.class, () -> authorizer.canAenderungEinreichen(uuid));
    }

    @Test
    void sozialdienstMitarbeiterCanAenderungEinreichenWhenGesuchDelegated() {
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(true);
        // arrange
        currentBenutzer.setRollen(
            Set.of(
                new Rolle()
                    .setKeycloakIdentifier(OidcConstants.ROLE_SOZIALDIENST_MITARBEITER)
            )
        );
        Fall fall = new Fall();
        fall.setGesuchsteller(new Benutzer());
        Sozialdienst sozialdienst = new Sozialdienst();
        sozialdienst.setId(UUID.randomUUID());
        Delegierung delegierung = new Delegierung();
        delegierung.setSozialdienst(sozialdienst);
        delegierung.setDelegierterMitarbeiter(new SozialdienstBenutzer());
        fall.setDelegierung(delegierung);
        Ausbildung ausbildung = new Ausbildung();
        ausbildung.setFall(fall);
        ausbildung.setGesuchs(List.of(gesuch));
        gesuch.setAusbildung(ausbildung);
        fall.setAusbildungs(Set.of(ausbildung));

        final var uuid = UUID.randomUUID();
        // assert
        assertDoesNotThrow(() -> authorizer.canAenderungEinreichen(uuid));
    }
}
