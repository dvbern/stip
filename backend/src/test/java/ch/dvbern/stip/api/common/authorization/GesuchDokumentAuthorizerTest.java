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
import java.util.stream.Stream;

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
import jakarta.ws.rs.ForbiddenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import static ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus.BEREIT_FUER_BEARBEITUNG;
import static ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus.IN_BEARBEITUNG_GS;
import static ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus.IN_BEARBEITUNG_SB;
import static ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus.VERFUEGUNG_VERSENDET;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@Execution(ExecutionMode.CONCURRENT)
class GesuchDokumentAuthorizerTest {
    private GesuchDokumentAuthorizer authorizer;

    private GesuchDokument gesuchDokument;
    private BenutzerService benutzerService;
    private Benutzer mockBenutzer;

    private GesuchTrancheRepository gesuchTrancheRepository;
    private GesuchDokumentRepository gesuchDokumentRepository;

    @BeforeEach
    void setUp() {
        benutzerService = Mockito.mock(BenutzerService.class);

        gesuchDokumentRepository = Mockito.mock(GesuchDokumentRepository.class);
        gesuchDokument = new GesuchDokument();
        gesuchDokument.setGesuchTranche(new GesuchTranche().setGesuch(new Gesuch()));
        Mockito.when(gesuchDokumentRepository.requireById(any())).thenReturn(gesuchDokument);

        gesuchTrancheRepository = Mockito.mock(GesuchTrancheRepository.class);
        Mockito.when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuchDokument.getGesuchTranche());

        authorizer = new GesuchDokumentAuthorizer(
            gesuchTrancheRepository,
            benutzerService,
            gesuchDokumentRepository,
            null,
            null,
            null,
            null
        );
    }

    private void setupBenutzerOfRole(final String role) {
        benutzerService = Mockito.mock(BenutzerService.class);
        mockBenutzer = new Benutzer();
        mockBenutzer.setRollen(Set.of(new Rolle().setKeycloakIdentifier(role)));
        Mockito.when(benutzerService.getCurrentBenutzer()).thenReturn(mockBenutzer);
    }

    static Stream<Arguments> provideDataToSucceed() {
        return Stream.of(
            Arguments
                // Setup for SB
                .of(
                    OidcConstants.ROLE_SACHBEARBEITER,
                    GesuchTrancheTyp.TRANCHE,
                    // Gesuchstatus
                    IN_BEARBEITUNG_SB,
                    // Tranchestatus (if AENDERUNG)
                    GesuchTrancheStatus.UEBERPRUEFEN
                ),
            Arguments
                // Setup for SB
                .of(
                    OidcConstants.ROLE_SACHBEARBEITER,
                    GesuchTrancheTyp.AENDERUNG,
                    // setup to not throw (succeed)
                    VERFUEGUNG_VERSENDET,
                    // Tranchestatus (if AENDERUNG)
                    GesuchTrancheStatus.UEBERPRUEFEN
                ),
            // Setup for Admin

            Arguments
                // Setup for SB
                .of(
                    OidcConstants.ROLE_SACHBEARBEITER_ADMIN,
                    GesuchTrancheTyp.TRANCHE,
                    // Gesuchstatus
                    IN_BEARBEITUNG_SB,
                    // Tranchestatus (if AENDERUNG)
                    GesuchTrancheStatus.UEBERPRUEFEN
                ),
            org.junit.jupiter.params.provider.Arguments
                // Setup for SB
                .of(
                    OidcConstants.ROLE_SACHBEARBEITER_ADMIN,
                    GesuchTrancheTyp.AENDERUNG,
                    // setup to not throw (succeed)
                    VERFUEGUNG_VERSENDET,
                    // Tranchestatus (if AENDERUNG)
                    GesuchTrancheStatus.UEBERPRUEFEN
                )
        );
    }

    static Stream<Arguments> provideDataToFail() {
        return Stream.of(
            Arguments
                .of(
                    OidcConstants.ROLE_GESUCHSTELLER,
                    GesuchTrancheTyp.TRANCHE,
                    // Gesuchstatus
                    BEREIT_FUER_BEARBEITUNG,
                    // Tranchestatus (if AENDERUNG)
                    null
                ),
            Arguments
                .of(
                    OidcConstants.ROLE_GESUCHSTELLER,
                    GesuchTrancheTyp.AENDERUNG,
                    // setup to not throw (succeed)
                    VERFUEGUNG_VERSENDET,
                    // Tranchestatus (if AENDERUNG)
                    GesuchTrancheStatus.IN_BEARBEITUNG_GS
                ),
            Arguments
                .of(
                    OidcConstants.ROLE_SOZIALDIENST_MITARBEITER,
                    GesuchTrancheTyp.TRANCHE,
                    // Gesuchstatus
                    IN_BEARBEITUNG_GS,
                    // Tranchestatus (if AENDERUNG)
                    null
                ),
            Arguments
                .of(
                    OidcConstants.ROLE_SOZIALDIENST_MITARBEITER,
                    GesuchTrancheTyp.AENDERUNG,
                    // setup to not throw (succeed)
                    VERFUEGUNG_VERSENDET,
                    // Tranchestatus (if AENDERUNG)
                    GesuchTrancheStatus.FEHLENDE_DOKUMENTE
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideDataToSucceed")
    void canUpdateGesuchDokumentShouldSuccedTest(
        String role,
        GesuchTrancheTyp gesuchTrancheTyp,
        Gesuchstatus gesuchStatus,
        GesuchTrancheStatus trancheStatus
    ) {
        // overall arrange
        setupBenutzerOfRole(role);
        authorizer = new GesuchDokumentAuthorizer(
            gesuchTrancheRepository,
            benutzerService,
            gesuchDokumentRepository,
            null,
            null,
            null,
            null
        );

        final var dokumentId = UUID.randomUUID();
        gesuchDokument.getGesuchTranche().setTyp(gesuchTrancheTyp);

        // arrange (success)
        gesuchDokument.getGesuchTranche().getGesuch().setGesuchStatus(gesuchStatus);
        if (gesuchTrancheTyp == GesuchTrancheTyp.AENDERUNG) {
            gesuchDokument.getGesuchTranche().setTyp(gesuchTrancheTyp);
            gesuchDokument.getGesuchTranche().setStatus(trancheStatus);
        }

        // act/assert
        assertDoesNotThrow(() -> authorizer.canUpdateGesuchDokument(dokumentId));
    }

    @ParameterizedTest
    @MethodSource("provideDataToFail")
    void canUpdateGesuchDokumentShouldFailTest(
        String role,
        GesuchTrancheTyp gesuchTrancheTyp,
        Gesuchstatus gesuchStatus,
        GesuchTrancheStatus trancheStatus

    ) {
        // overall arrange
        setupBenutzerOfRole(role);
        authorizer = new GesuchDokumentAuthorizer(
            gesuchTrancheRepository,
            benutzerService,
            gesuchDokumentRepository,
            null,
            null,
            null,
            null
        );

        final var dokumentId = UUID.randomUUID();
        gesuchDokument.getGesuchTranche().setTyp(gesuchTrancheTyp);

        // arrange
        gesuchDokument.getGesuchTranche().getGesuch().setGesuchStatus(gesuchStatus);
        if (gesuchTrancheTyp == GesuchTrancheTyp.AENDERUNG) {
            gesuchDokument.getGesuchTranche().setTyp(gesuchTrancheTyp);
            gesuchDokument.getGesuchTranche().setStatus(trancheStatus);
        }

        // act/assert
        assertThrows(ForbiddenException.class, () -> authorizer.canUpdateGesuchDokument(dokumentId));
    }
}
