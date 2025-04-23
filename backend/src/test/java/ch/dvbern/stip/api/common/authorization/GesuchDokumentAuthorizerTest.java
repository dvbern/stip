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
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.repo.GesuchDokumentRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

class GesuchDokumentAuthorizerTest {
    private GesuchDokumentAuthorizer gesuchDokumentAuthorizer;

    private GesuchDokument gesuchDokument;
    private BenutzerService benutzerService;
    private Benutzer mockBenutzer;

    @BeforeEach
    void setUp() {
        // setup default benutzer for the moment
        setupSBBenutzer();

        final var gesuchDokumentRepository = Mockito.mock(GesuchDokumentRepository.class);
        gesuchDokument = new GesuchDokument();
        gesuchDokument.setGesuchTranche(new GesuchTranche().setGesuch(new Gesuch()));
        Mockito.when(gesuchDokumentRepository.requireById(any())).thenReturn(gesuchDokument);

        final var gesuchTrancheRepository = Mockito.mock(GesuchTrancheRepository.class);
        Mockito.when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuchDokument.getGesuchTranche());

        gesuchDokumentAuthorizer = new GesuchDokumentAuthorizer(
            gesuchTrancheRepository,
            benutzerService,
            null,
            gesuchDokumentRepository,
            null,
            null,
            null
        );
    }

    private void setupSBBenutzer() {
        setupBenutzerOfRole(OidcConstants.ROLE_SACHBEARBEITER);
    }

    private void setupGSBenutzer() {
        setupBenutzerOfRole(OidcConstants.ROLE_GESUCHSTELLER);
    }

    private void setupSozMABenutzer() {
        setupBenutzerOfRole(OidcConstants.ROLE_SOZIALDIENST_MITARBEITER);
    }

    private void setupBenutzerOfRole(final String role) {
        benutzerService = Mockito.mock(BenutzerService.class);
        mockBenutzer = new Benutzer();
        mockBenutzer.setRollen(Set.of(new Rolle().setKeycloakIdentifier(role)));
        Mockito.when(benutzerService.getCurrentBenutzer()).thenReturn(mockBenutzer);
    }

    /**
     * Test Authorizer as Sachbearbeiter
     */

    @Test
    @Description("An SB can update a GesuchDokument of a tranche when gesuch is in Gesuchstatus IN_BEARBEITUNG_SB")
    void canUpdateGesuchDokumentShouldSuccessAsSBForNormalTrancheOfGesuchstatus_IN_BEARBEITUNG_SB() {
        // arrange
        setupSBBenutzer();
        gesuchDokument.getGesuchTranche().setTyp(GesuchTrancheTyp.TRANCHE);
        gesuchDokument.getGesuchTranche().getGesuch().setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);

        // act/assert
        final var dokumentId = UUID.randomUUID();
        assertDoesNotThrow(() -> gesuchDokumentAuthorizer.canUpdateGesuchDokument(dokumentId));
    }

    @Test
    @Description("An SB can NOT update a GesuchDokument of a tranche when gesuch is in Gesuchstatus IN_BEARBEITUNG_GS")
    void canUpdateGesuchDokumentShouldFailAsSBForNormalTrancheOfGesuchstatus_IN_BEARBEITUNG_GS() {
        // arrange
        setupSBBenutzer();
        gesuchDokument.getGesuchTranche().setTyp(GesuchTrancheTyp.TRANCHE);
        gesuchDokument.getGesuchTranche().getGesuch().setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);

        // act/assert
        final var dokumentId = UUID.randomUUID();
        assertThrows(IllegalStateException.class, () -> gesuchDokumentAuthorizer.canUpdateGesuchDokument(dokumentId));
    }

    @Test
    @Description("An SB can update a GesuchDokument of an aenderung when TrancheStatus is UEBERPRUEFEN")
    void canUpdateGesuchDokumentShouldSuccessAsSBForAenderungOfTrancheStatus_UEBERPRUEFEN() {
        // arrange
        setupSBBenutzer();
        gesuchDokument.getGesuchTranche().setTyp(GesuchTrancheTyp.AENDERUNG);
        gesuchDokument.getGesuchTranche().setStatus(GesuchTrancheStatus.UEBERPRUEFEN);

        // act/assert
        final var dokumentId = UUID.randomUUID();
        assertDoesNotThrow(() -> gesuchDokumentAuthorizer.canUpdateGesuchDokument(dokumentId));
    }

    @Test
    @Description("An SB can NOT update a GesuchDokument of an aenderung when TrancheStatus is IN_BEARBEITUNG_GS")
    void canUpdateGesuchDokumentShouldFailAsSBForAenderungOfTrancheStatus_IN_BEARBEITUNG_GS() {
        // arrange
        setupSBBenutzer();
        gesuchDokument.getGesuchTranche().setTyp(GesuchTrancheTyp.AENDERUNG);
        gesuchDokument.getGesuchTranche().setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS);

        // act/assert
        final var dokumentId = UUID.randomUUID();
        assertThrows(IllegalStateException.class, () -> gesuchDokumentAuthorizer.canUpdateGesuchDokument(dokumentId));
    }

    /**
     * Test Authorizer as Gesuchsteller
     */
    @Test
    @Description("A GS can NOT update a GesuchDokument of a tranche when gesuch is in Gesuchstatus IN_BEARBEITUNG_SB")
    void canUpdateGesuchDokumentShouldFailAsGSForNormalTrancheOfGesuchstatus_IN_BEARBEITUNG_SB() {
        // arrange
        setupGSBenutzer();
        gesuchDokument.getGesuchTranche().setTyp(GesuchTrancheTyp.TRANCHE);
        gesuchDokument.getGesuchTranche().getGesuch().setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);

        // act/assert
        final var dokumentId = UUID.randomUUID();
        assertDoesNotThrow(() -> gesuchDokumentAuthorizer.canUpdateGesuchDokument(dokumentId));
    }

    @Test
    @Description("A GS can update a GesuchDokument of a tranche when gesuch is in Gesuchstatus IN_BEARBEITUNG_GS")
    void canUpdateGesuchDokumentShouldSuccessAsGSForNormalTrancheOfGesuchstatus_IN_BEARBEITUNG_GS() {
        // arrange
        setupGSBenutzer();
        gesuchDokument.getGesuchTranche().setTyp(GesuchTrancheTyp.TRANCHE);
        gesuchDokument.getGesuchTranche().getGesuch().setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);

        // act/assert
        final var dokumentId = UUID.randomUUID();
        assertThrows(IllegalStateException.class, () -> gesuchDokumentAuthorizer.canUpdateGesuchDokument(dokumentId));
    }

    @Test
    @Description("A GS can NOT update a GesuchDokument of an aenderung when TrancheStatus is UEBERPRUEFEN")
    void canUpdateGesuchDokumentShouldFailAsGSForAenderungOfTrancheStatus_UEBERPRUEFEN() {
        // arrange
        setupGSBenutzer();
        gesuchDokument.getGesuchTranche().setTyp(GesuchTrancheTyp.AENDERUNG);
        gesuchDokument.getGesuchTranche().setStatus(GesuchTrancheStatus.UEBERPRUEFEN);

        // act/assert
        final var dokumentId = UUID.randomUUID();
        assertDoesNotThrow(() -> gesuchDokumentAuthorizer.canUpdateGesuchDokument(dokumentId));
    }

    @Test
    @Description("An GS can update a GesuchDokument of an aenderung when TrancheStatus is IN_BEARBEITUNG_GS")
    void canUpdateGesuchDokumentShouldSuccessAsGSForAenderungOfTrancheStatus_IN_BEARBEITUNG_GS() {
        // arrange
        setupGSBenutzer();
        gesuchDokument.getGesuchTranche().setTyp(GesuchTrancheTyp.AENDERUNG);
        gesuchDokument.getGesuchTranche().setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS);

        // act/assert
        final var dokumentId = UUID.randomUUID();
        assertThrows(IllegalStateException.class, () -> gesuchDokumentAuthorizer.canUpdateGesuchDokument(dokumentId));
    }

    /**
     * Test Authorizer as Sozialdienstmitarbeiter
     */

    @Test
    @Description("A GS can NOT update a GesuchDokument of a tranche when gesuch is in Gesuchstatus IN_BEARBEITUNG_SB")
    void canUpdateGesuchDokumentShouldFailAsSozMAForNormalTrancheOfGesuchstatus_IN_BEARBEITUNG_SB() {
        // arrange
        setupSozMABenutzer();
        gesuchDokument.getGesuchTranche().setTyp(GesuchTrancheTyp.TRANCHE);
        gesuchDokument.getGesuchTranche().getGesuch().setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);

        // act/assert
        final var dokumentId = UUID.randomUUID();
        assertDoesNotThrow(() -> gesuchDokumentAuthorizer.canUpdateGesuchDokument(dokumentId));
    }

    @Test
    @Description("A GS can update a GesuchDokument of a tranche when gesuch is in Gesuchstatus IN_BEARBEITUNG_GS")
    void canUpdateGesuchDokumentShouldSuccessAsAsSozMAForNormalTrancheOfGesuchstatus_IN_BEARBEITUNG_GS() {
        // arrange
        setupSozMABenutzer();
        gesuchDokument.getGesuchTranche().setTyp(GesuchTrancheTyp.TRANCHE);
        gesuchDokument.getGesuchTranche().getGesuch().setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);

        // act/assert
        final var dokumentId = UUID.randomUUID();
        assertThrows(IllegalStateException.class, () -> gesuchDokumentAuthorizer.canUpdateGesuchDokument(dokumentId));
    }

    @Test
    @Description("A GS can NOT update a GesuchDokument of an aenderung when TrancheStatus is UEBERPRUEFEN")
    void canUpdateGesuchDokumentShouldFailAsSozMAForAenderungOfTrancheStatus_UEBERPRUEFEN() {
        // arrange
        setupSozMABenutzer();
        gesuchDokument.getGesuchTranche().setTyp(GesuchTrancheTyp.AENDERUNG);
        gesuchDokument.getGesuchTranche().setStatus(GesuchTrancheStatus.UEBERPRUEFEN);

        // act/assert
        final var dokumentId = UUID.randomUUID();
        assertDoesNotThrow(() -> gesuchDokumentAuthorizer.canUpdateGesuchDokument(dokumentId));
    }

    @Test
    @Description("An GS can update a GesuchDokument of an aenderung when TrancheStatus is IN_BEARBEITUNG_GS")
    void canUpdateGesuchDokumentShouldSuccessAsSozMAForAenderungOfTrancheStatus_IN_BEARBEITUNG_GS() {
        // arrange
        setupSozMABenutzer();
        gesuchDokument.getGesuchTranche().setTyp(GesuchTrancheTyp.AENDERUNG);
        gesuchDokument.getGesuchTranche().setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS);

        // act/assert
        final var dokumentId = UUID.randomUUID();
        assertThrows(IllegalStateException.class, () -> gesuchDokumentAuthorizer.canUpdateGesuchDokument(dokumentId));
    }

}
