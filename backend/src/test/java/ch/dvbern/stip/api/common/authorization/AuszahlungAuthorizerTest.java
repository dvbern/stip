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

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.delegieren.entity.Delegierung;
import ch.dvbern.stip.api.delegieren.repo.DelegierungRepository;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.sozialdienst.entity.Sozialdienst;
import ch.dvbern.stip.api.sozialdienst.service.SozialdienstService;
import ch.dvbern.stip.api.sozialdienstbenutzer.entity.SozialdienstBenutzer;
import ch.dvbern.stip.api.sozialdienstbenutzer.repo.SozialdienstBenutzerRepository;
import ch.dvbern.stip.api.sozialdienstbenutzer.service.SozialdienstBenutzerService;
import jakarta.ws.rs.ForbiddenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuszahlungAuthorizerTest {
    private AuszahlungAuthorizer auszahlungAuthorizer;

    private BenutzerService benutzerService;
    private SozialdienstBenutzerService sozialdienstBenutzerService;
    private FallRepository fallRepository;
    private GesuchRepository gesuchRepository;
    private DelegierungRepository delegierungRepository;
    private SozialdienstService sozialdienstService;
    private SozialdienstBenutzerRepository sozialdienstBenutzerRepository;

    @BeforeEach
    void setUp() {
        gesuchRepository = Mockito.mock(GesuchRepository.class);
        benutzerService = Mockito.mock(BenutzerService.class);
        sozialdienstBenutzerService = Mockito.mock(SozialdienstBenutzerService.class);
        fallRepository = Mockito.mock(FallRepository.class);
        delegierungRepository = Mockito.mock(DelegierungRepository.class);
        sozialdienstService = Mockito.mock(SozialdienstService.class);
        sozialdienstBenutzerRepository = Mockito.mock(SozialdienstBenutzerRepository.class);

        auszahlungAuthorizer = new AuszahlungAuthorizer(benutzerService, sozialdienstService, gesuchRepository);

    }

    private void setupGesuchstellerAsCurrentBenutzer() {
        setFallAndGesuchstellerOfGesuch();
        var benutzer = gesuchRepository.requireById(UUID.randomUUID()).getAusbildung().getFall().getGesuchsteller();
        when(benutzerService.getCurrentBenutzer()).thenReturn(benutzer);

    }

    private void setFallAndGesuchstellerOfGesuch() {
        Rolle rolle = new Rolle();
        rolle.setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER);
        var benutzer = new Benutzer();
        benutzer.setRollen(Set.of(rolle));

        gesuchRepository.requireById(UUID.randomUUID()).getAusbildung().getFall().setGesuchsteller(benutzer);
    }

    private void setupSozialdienstMitarbeiterAsCurrentBenutzer() {
        var sozialdienstBenutzer = new SozialdienstBenutzer();
        sozialdienstBenutzer.setId(UUID.randomUUID());
        Rolle rolle = new Rolle();
        rolle.setKeycloakIdentifier(OidcConstants.ROLE_SOZIALDIENST_MITARBEITER);
        sozialdienstBenutzer.setRollen(Set.of(rolle));
        when(sozialdienstBenutzerService.getCurrentSozialdienstBenutzer())
            .thenReturn(Optional.of(sozialdienstBenutzer));
        var benutzer = (Benutzer) sozialdienstBenutzer;
        when(benutzerService.getCurrentBenutzer()).thenReturn(benutzer);
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

        var ausbildung = new Ausbildung();
        var gesuch = new Gesuch();
        gesuch.setAusbildung(ausbildung);
        ausbildung.setGesuchs(List.of(gesuch));
        ausbildung.setFall(fall);
        fall.setAusbildungs(Set.of(ausbildung));

        when(fallRepository.requireById(any())).thenReturn(fall);
        when(sozialdienstService.getSozialdienstOfCurrentSozialdienstBenutzer()).thenReturn(sozialdienst);
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(true);
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
    }

    private void setupGesuchWithoutDelegierung() {
        // arrange
        final var sozialdienstbenutzer = sozialdienstBenutzerService.getCurrentSozialdienstBenutzer().get();

        // current sozialdienstbenutzer is mitarbeiter of the current sozialdienst
        var sozialdienst = new Sozialdienst();
        sozialdienst.setId(UUID.randomUUID());
        sozialdienst.setSozialdienstBenutzers(List.of(sozialdienstbenutzer));

        // the fall is delegiert to the current sozialdienst-mitarbeiter
        var fall = new Fall();

        var ausbildung = new Ausbildung();
        var gesuch = new Gesuch();
        gesuch.setAusbildung(ausbildung);
        ausbildung.setGesuchs(List.of(gesuch));
        ausbildung.setFall(fall);
        fall.setAusbildungs(Set.of(ausbildung));

        when(fallRepository.requireById(any())).thenReturn(fall);
        when(sozialdienstService.getSozialdienstOfCurrentSozialdienstBenutzer()).thenReturn(sozialdienst);
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(true);
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
    }

    @Test
    void canUpdateOrCreateAuszahlung_shouldWork_whenDelegated_and_asSozialdienstMitarbeiterOfSozialdienst() {
        setupSozialdienstMitarbeiterAsCurrentBenutzer();
        setupDelegierung();

        // arrange
        when(sozialdienstService.getSozialdienstOfCurrentSozialdienstBenutzer()).thenReturn(new Sozialdienst());
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(true);

        // act & assert
        assertDoesNotThrow(() -> auszahlungAuthorizer.canUpdateAuszahlungForGesuch(UUID.randomUUID()));
    }

    @Test
    void canUpdateOrCreateAuszahlung_shouldNOTWork_withoutDelegation_and_asSozialdienstMitarbeiterOfSozialdienst() {
        setupSozialdienstMitarbeiterAsCurrentBenutzer();
        setupGesuchWithoutDelegierung();

        // arrange
        when(sozialdienstService.getSozialdienstOfCurrentSozialdienstBenutzer()).thenReturn(new Sozialdienst());
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(true);

        // act & assert
        assertThrows(
            ForbiddenException.class,
            () -> auszahlungAuthorizer.canUpdateAuszahlungForGesuch(UUID.randomUUID())
        );
    }

    @Test
    void canUpdateOrCreateAuszahlung_shouldWork_withoutDelegation_and_asGesuchsteller() {
        // arrange
        setupSozialdienstMitarbeiterAsCurrentBenutzer();
        setupGesuchWithoutDelegierung();
        setupGesuchstellerAsCurrentBenutzer();

        // act & assert
        assertDoesNotThrow(() -> auszahlungAuthorizer.canUpdateAuszahlungForGesuch(UUID.randomUUID()));

    }

    @Test
    void canUpdateOrCreateAuszahlung_shouldNOTWork_whenDelegated_and_asGesuchsteller() {
        // arrange
        setupSozialdienstMitarbeiterAsCurrentBenutzer();
        setupDelegierung();
        setupGesuchstellerAsCurrentBenutzer();
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(false);

        // act & assert
        assertThrows(
            ForbiddenException.class,
            () -> auszahlungAuthorizer.canUpdateAuszahlungForGesuch(UUID.randomUUID())
        );
    }

    @Test
    void canRead_shouldWork_withoutDelegation_asGesuchsteller() {
        // arrange
        setupSozialdienstMitarbeiterAsCurrentBenutzer();
        setupGesuchWithoutDelegierung();
        setupGesuchstellerAsCurrentBenutzer();
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(false);

        // act & assert
        assertDoesNotThrow(() -> auszahlungAuthorizer.canReadAuszahlungForGesuch(UUID.randomUUID()));
    }

    @Test
    void canRead_shouldWork_withDelegation_asGesuchsteller() {
        // arrange
        setupSozialdienstMitarbeiterAsCurrentBenutzer();
        setupDelegierung();
        setupGesuchstellerAsCurrentBenutzer();
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(false);

        // act & assert
        assertDoesNotThrow(() -> auszahlungAuthorizer.canReadAuszahlungForGesuch(UUID.randomUUID()));
    }

    @Test
    void canRead_shouldWork_asSozialdienstmitarbeiter_whenDelegated() {
        // arrange
        setupSozialdienstMitarbeiterAsCurrentBenutzer();
        setupDelegierung();
        setFallAndGesuchstellerOfGesuch();

        // arrange
        when(sozialdienstService.getSozialdienstOfCurrentSozialdienstBenutzer()).thenReturn(new Sozialdienst());
        when(sozialdienstService.isCurrentBenutzerMitarbeiterOfSozialdienst(any())).thenReturn(true);

        assertDoesNotThrow(() -> auszahlungAuthorizer.canReadAuszahlungForGesuch(UUID.randomUUID()));
    }

    @Test
    void canRead_shoulNOTdWork_asSozialdienstmitarbeiter_withoutDelegation() {
        // arrange
        setupSozialdienstMitarbeiterAsCurrentBenutzer();
        setupGesuchWithoutDelegierung();
        setFallAndGesuchstellerOfGesuch();
        // act & assert
        assertThrows(
            ForbiddenException.class,
            () -> auszahlungAuthorizer.canReadAuszahlungForGesuch(UUID.randomUUID())
        );
    }
}
