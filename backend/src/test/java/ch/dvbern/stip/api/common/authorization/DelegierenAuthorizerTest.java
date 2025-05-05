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

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.delegieren.entity.Delegierung;
import ch.dvbern.stip.api.delegieren.repo.DelegierungRepository;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.sozialdienst.entity.Sozialdienst;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import ch.dvbern.stip.api.sozialdienstbenutzer.entity.SozialdienstBenutzer;
import ch.dvbern.stip.api.sozialdienstbenutzer.repo.SozialdienstBenutzerRepository;
import ch.dvbern.stip.api.sozialdienstbenutzer.service.SozialdienstBenutzerService;
import ch.dvbern.stip.generated.dto.DelegierterMitarbeiterAendernDto;
import io.quarkus.security.UnauthorizedException;
import jakarta.ws.rs.ForbiddenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DelegierenAuthorizerTest {
    private DelegierenAuthorizer delegierenAuthorizer;

    private BenutzerService benutzerService;
    private SozialdienstBenutzerService sozialdienstBenutzerService;
    private FallRepository fallRepository;
    private DelegierungRepository delegierungRepository;
    private SozialdienstService sozialdienstService;
    private SozialdienstBenutzerRepository sozialdienstBenutzerRepository;

    @BeforeEach
    void setUp() {
        benutzerService = Mockito.mock(BenutzerService.class);
        sozialdienstBenutzerService = Mockito.mock(SozialdienstBenutzerService.class);
        fallRepository = Mockito.mock(FallRepository.class);
        delegierungRepository = Mockito.mock(DelegierungRepository.class);
        sozialdienstService = Mockito.mock(SozialdienstService.class);
        sozialdienstBenutzerRepository = Mockito.mock(SozialdienstBenutzerRepository.class);

        delegierenAuthorizer = new DelegierenAuthorizer(
            benutzerService, sozialdienstBenutzerService, fallRepository, delegierungRepository, sozialdienstService,
            sozialdienstBenutzerRepository
        );

    }

    private void setupSozialdienstMitarbeiter() {
        var sozialdienstBenutzer = new SozialdienstBenutzer();
        sozialdienstBenutzer.setId(UUID.randomUUID());
        when(sozialdienstBenutzerService.getCurrentSozialdienstBenutzer())
            .thenReturn(Optional.of(sozialdienstBenutzer));
    }

    private void setupDelegierung() {
        // arrange
        final var sozialdienstbenutzer = sozialdienstBenutzerService.getCurrentSozialdienstBenutzer().get();

        // current sozialdienstbenutzer is mitarbeiter of the current sozialdienst
        var sozialdienst = new Sozialdienst();
        sozialdienst.setId(UUID.randomUUID());
        sozialdienst.setSozialdienstBenutzers(List.of(sozialdienstbenutzer));

        // the fall is delegiert to the current sozialdienst-mitarbeiter
        var fall = new Fall();
        var delegierung = new Delegierung();
        delegierung.setSozialdienst(sozialdienst);
        delegierung.setDelegierterMitarbeiter(sozialdienstbenutzer);
        delegierung.setDelegierungAngenommen(true);
        when(delegierungRepository.requireById(any())).thenReturn(delegierung);
        fall.setDelegierung(delegierung);

        when(fallRepository.requireById(any())).thenReturn(fall);
        when(sozialdienstService.getSozialdienstOfCurrentSozialdienstBenutzer()).thenReturn(sozialdienst);
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(true);
    }

    @Test
    void canReadDelegierung_shouldWork_asSozialdienstMitarbeiterOfSozialdienst(){
        // arrange
        when(sozialdienstService.getSozialdienstOfCurrentSozialdienstBenutzer()).thenReturn(new Sozialdienst());
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(true);

        // act & assert
        assertDoesNotThrow(() -> delegierenAuthorizer.canReadDelegierung());
    }

    @Test
    void canReadDelegierung_shouldFail_asNONSozialdienstMitarbeiterOfSozialdienst() {
        // arrange
        when(sozialdienstService.getSozialdienstOfCurrentSozialdienstBenutzer()).thenReturn(new Sozialdienst());
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(false);

        // act & assert
        assertThrows(ForbiddenException.class, () -> delegierenAuthorizer.canReadDelegierung());
    }

    @Test
    void canReadFallDashboard_shouldWork_asDelegierterMitarbeiterOfSozialdienst() {
        // arrange
        setupSozialdienstMitarbeiter();
        setupDelegierung();

        when(sozialdienstService.getSozialdienstOfCurrentSozialdienstBenutzer()).thenReturn(new Sozialdienst());
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(true);

        // act & assert
        assertDoesNotThrow(() -> delegierenAuthorizer.canReadFallDashboard(UUID.randomUUID()));
    }

    @Test
    void canReadFallDashboard_shouldFail_asNOTDelegierterMitarbeiterOfSozialdienst() {
        // arrange
        setupSozialdienstMitarbeiter();
        setupDelegierung();
        // override current sozialdienstbenutzer through a a non-delegated one
        when(sozialdienstBenutzerService.getCurrentSozialdienstBenutzer())
            .thenReturn(Optional.of(new SozialdienstBenutzer()));

        when(sozialdienstService.getSozialdienstOfCurrentSozialdienstBenutzer()).thenReturn(new Sozialdienst());
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(true);

        // act & assert
        assertThrows(ForbiddenException.class, () -> delegierenAuthorizer.canReadFallDashboard(UUID.randomUUID()));
    }

    @Test
    void canDelegate_shouldFail_whenNotGesuchsteller() {
        // arrange
        setupSozialdienstMitarbeiter();
        setupDelegierung();
        final Benutzer benutzer = sozialdienstBenutzerService.getCurrentSozialdienstBenutzer().get();
        when(benutzerService.getCurrentBenutzer()).thenReturn(benutzer);

        // act & assert
        assertThrows(ForbiddenException.class, () -> delegierenAuthorizer.canDelegate(UUID.randomUUID()));
    }

    @Test
    void canDelegate_shouldFail_whenGesuchstellerButWithDelegation() {
        // arrange
        setupSozialdienstMitarbeiter();
        setupDelegierung();
        Benutzer gesuchsteller = new Benutzer();
        gesuchsteller.setId(UUID.randomUUID());
        Rolle gesuchstellerRole = new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER);
        gesuchsteller.setRollen(Set.of(gesuchstellerRole));
        when(benutzerService.getCurrentBenutzer()).thenReturn(gesuchsteller);

        var fall = fallRepository.requireById(UUID.randomUUID());
        fall.setGesuchsteller(gesuchsteller);

        // act & assert
        assertThrows(UnauthorizedException.class, () -> delegierenAuthorizer.canDelegate(UUID.randomUUID()));
    }

    @Test
    void canDelegate_shouldWork_whenGesuchstellerAndWithoutDelegation() {
        // arrange
        Benutzer gesuchsteller = new Benutzer();
        gesuchsteller.setId(UUID.randomUUID());
        Rolle gesuchstellerRole = new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER);
        gesuchsteller.setRollen(Set.of(gesuchstellerRole));
        when(benutzerService.getCurrentBenutzer()).thenReturn(gesuchsteller);

        var fall = new Fall();
        fall.setGesuchsteller(gesuchsteller);
        when(fallRepository.requireById(any())).thenReturn(fall);

        // act & assert
        assertDoesNotThrow(() -> delegierenAuthorizer.canDelegate(UUID.randomUUID()));
    }

    @Test
    void canDelegierterMitarbeiterAendern_shouldWork_whenBothBenutzerMitarbeiterOfSameSozialdienst() {
        // arrange
        setupSozialdienstMitarbeiter();
        setupDelegierung();

        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(true);
        when(sozialdienstBenutzerRepository.requireById(any())).thenReturn(new SozialdienstBenutzer());
        when(sozialdienstService.isBenutzerMitarbeiterOfSozialdienst(any(), any())).thenReturn(true);

        // act & assert
        assertDoesNotThrow(
            () -> delegierenAuthorizer
                .canDelegierterMitarbeiterAendern(UUID.randomUUID(), new DelegierterMitarbeiterAendernDto())
        );
    }

    @Test
    void canDelegierterMitarbeiterAendern_shouldFail_whenNOTBothBenutzerMitarbeiterOfSameSozialdienst() {
        // arrange
        setupSozialdienstMitarbeiter();
        setupDelegierung();

        // sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(true);
        when(sozialdienstBenutzerRepository.requireById(any())).thenReturn(new SozialdienstBenutzer());
        when(sozialdienstService.isBenutzerMitarbeiterOfSozialdienst(any(), any())).thenReturn(false);

        // act & assert
        assertThrows(
            ForbiddenException.class,
            () -> delegierenAuthorizer
                .canDelegierterMitarbeiterAendern(UUID.randomUUID(), new DelegierterMitarbeiterAendernDto())
        );

        // arrange
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(false);
        when(sozialdienstBenutzerRepository.requireById(any())).thenReturn(new SozialdienstBenutzer());
        when(sozialdienstService.isBenutzerMitarbeiterOfSozialdienst(any(), any())).thenReturn(true);

        // act & assert
        assertThrows(
            ForbiddenException.class,
            () -> delegierenAuthorizer
                .canDelegierterMitarbeiterAendern(UUID.randomUUID(), new DelegierterMitarbeiterAendernDto())
        );

        // arrange
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(false);
        when(sozialdienstBenutzerRepository.requireById(any())).thenReturn(new SozialdienstBenutzer());
        when(sozialdienstService.isBenutzerMitarbeiterOfSozialdienst(any(), any())).thenReturn(false);

        // act & assert
        assertThrows(
            ForbiddenException.class,
            () -> delegierenAuthorizer
                .canDelegierterMitarbeiterAendern(UUID.randomUUID(), new DelegierterMitarbeiterAendernDto())
        );
    }

}
