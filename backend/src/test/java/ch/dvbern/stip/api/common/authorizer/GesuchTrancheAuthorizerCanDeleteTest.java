package ch.dvbern.stip.api.common.authorizer;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.GesuchTrancheAuthorizer;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import io.quarkus.security.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GesuchTrancheAuthorizerCanDeleteTest {
    @Test
    void gsCanotDeleteOwnAenderungInWrongStateTest(){
        final var currentBenutzer = new Benutzer().setKeycloakId(UUID.randomUUID().toString());
        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER));
        final var benutzerService = Mockito.mock(BenutzerService.class);
        when(benutzerService.getCurrentBenutzer()).thenReturn(currentBenutzer);

        final var gesuch = new Gesuch()
            .setFall(new Fall()
                .setGesuchsteller(currentBenutzer)
            );
        final var gesuchTranche_wrongState = new GesuchTranche()
            .setGesuch(gesuch)
            .setStatus(GesuchTrancheStatus.AKZEPTIERT);

        final var gesuchRepository = Mockito.mock(GesuchRepository.class);
        when(gesuchRepository.requireById(any()))
            .thenReturn(gesuch
            );
        final var gesuchTrancheRepository = Mockito.mock(GesuchTrancheRepository.class);
        when(gesuchTrancheRepository.requireById(any()))
            .thenReturn(gesuchTranche_wrongState
            );
        when(gesuchTrancheRepository.findById(any()))
            .thenReturn(gesuchTranche_wrongState
            );
        when(gesuchRepository.requireGesuchByTrancheId(any())).thenReturn(gesuch);

        final var authorizer = new GesuchTrancheAuthorizer(benutzerService,gesuchTrancheRepository,gesuchRepository);
        final var uuid = UUID.randomUUID();
        assertThrows(UnauthorizedException.class, () -> authorizer.canDelete(uuid));
    }
    @Test
    void gsCanDeleteOnlyOwnAenderungTest(){
        final var currentBenutzer = new Benutzer().setKeycloakId(UUID.randomUUID().toString());
        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER));
        final var benutzerService = Mockito.mock(BenutzerService.class);
        when(benutzerService.getCurrentBenutzer()).thenReturn(currentBenutzer);

        final var gesuch = new Gesuch()
            .setFall(new Fall()
                .setGesuchsteller(currentBenutzer)
            );
        final var gesuchTranche_inBearbeitungGS = new GesuchTranche()
            .setGesuch(gesuch)
            .setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS);

        final var gesuchRepository = Mockito.mock(GesuchRepository.class);
        when(gesuchRepository.requireById(any()))
            .thenReturn(gesuch
            );
        final var gesuchTrancheRepository = Mockito.mock(GesuchTrancheRepository.class);
        when(gesuchTrancheRepository.requireById(any()))
            .thenReturn(gesuchTranche_inBearbeitungGS
            );
        when(gesuchTrancheRepository.findById(any()))
            .thenReturn(gesuchTranche_inBearbeitungGS
            );
        when(gesuchRepository.requireGesuchByTrancheId(any())).thenReturn(gesuch);

        final var authorizer = new GesuchTrancheAuthorizer(benutzerService,gesuchTrancheRepository,gesuchRepository);
        final var uuid = UUID.randomUUID();
        assertDoesNotThrow(() -> authorizer.canDelete(uuid));
    }
    @Test
    void gsCannotDeleteWithoutRoleTest(){
        UUID currentBenutzerId = UUID.randomUUID();
        var currentBenutzer = new Benutzer();
        currentBenutzer.setId(currentBenutzerId);

        final var benutzerService = Mockito.mock(BenutzerService.class);
        when(benutzerService.getCurrentBenutzer()).thenReturn(currentBenutzer);

        final var gesuch = new Gesuch()
            .setFall(new Fall()
                .setGesuchsteller(currentBenutzer)
            );
        final var gesuchTranche_inBearbeitungGS = new GesuchTranche()
            .setGesuch(gesuch)
            .setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS);

        final var gesuchRepository = Mockito.mock(GesuchRepository.class);
        when(gesuchRepository.requireById(any()))
            .thenReturn(gesuch
            );
        final var gesuchTrancheRepository = Mockito.mock(GesuchTrancheRepository.class);
        when(gesuchTrancheRepository.requireById(any()))
            .thenReturn(gesuchTranche_inBearbeitungGS
            );
        when(gesuchTrancheRepository.findById(any()))
            .thenReturn(gesuchTranche_inBearbeitungGS
            );
        when(gesuchRepository.requireGesuchByTrancheId(any())).thenReturn(gesuch);

        final var authorizer = new GesuchTrancheAuthorizer(benutzerService,gesuchTrancheRepository,gesuchRepository);
        final var uuid = UUID.randomUUID();
        assertThrows(UnauthorizedException.class, () -> authorizer.canDelete(uuid));
    }
    @Test
    void gsCannotDeleteOtherAenderungTest(){
        UUID currentBenutzerId = UUID.randomUUID();
        UUID otherBenutzerId = UUID.randomUUID();
        var currentBenutzer = new Benutzer();
        currentBenutzer.setId(currentBenutzerId);
        var otherBenutzer = new Benutzer();
        otherBenutzer.setId(otherBenutzerId);
        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER));
        otherBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER));

        final var benutzerService = Mockito.mock(BenutzerService.class);
        when(benutzerService.getCurrentBenutzer()).thenReturn(currentBenutzer);

        final var gesuch = new Gesuch()
            .setFall(new Fall()
                .setGesuchsteller(otherBenutzer)
            );
        final var gesuchTranche_inBearbeitungGS = new GesuchTranche()
            .setGesuch(gesuch)
            .setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS);

        final var gesuchRepository = Mockito.mock(GesuchRepository.class);
        when(gesuchRepository.requireById(any()))
            .thenReturn(gesuch
            );
        final var gesuchTrancheRepository = Mockito.mock(GesuchTrancheRepository.class);
        when(gesuchTrancheRepository.requireById(any()))
            .thenReturn(gesuchTranche_inBearbeitungGS
            );
        when(gesuchTrancheRepository.findById(any()))
            .thenReturn(gesuchTranche_inBearbeitungGS
            );
        when(gesuchRepository.requireGesuchByTrancheId(any())).thenReturn(gesuch);

        final var authorizer = new GesuchTrancheAuthorizer(benutzerService,gesuchTrancheRepository,gesuchRepository);
        final var uuid = UUID.randomUUID();
        assertThrows(UnauthorizedException.class, () -> authorizer.canDelete(uuid));
    }
    @Test
    void adminCanDeleteAenderungTest(){
        UUID currentBenutzerAsAdminId = UUID.randomUUID();
        UUID otherBenutzerId = UUID.randomUUID();
        var currentBenutzerAsAdmin = new Benutzer();
        currentBenutzerAsAdmin.setId(currentBenutzerAsAdminId);
        var otherBenutzer = new Benutzer();
        otherBenutzer.setId(otherBenutzerId);
        currentBenutzerAsAdmin.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_ADMIN));
        otherBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER));

        final var benutzerService = Mockito.mock(BenutzerService.class);
        when(benutzerService.getCurrentBenutzer()).thenReturn(currentBenutzerAsAdmin);

        final var gesuch = new Gesuch()
            .setFall(new Fall()
                .setGesuchsteller(otherBenutzer)
            );
        final var gesuchTranche_inBearbeitungGS = new GesuchTranche()
            .setGesuch(gesuch)
            .setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS);

        final var gesuchRepository = Mockito.mock(GesuchRepository.class);
        when(gesuchRepository.requireById(any()))
            .thenReturn(gesuch
            );
        final var gesuchTrancheRepository = Mockito.mock(GesuchTrancheRepository.class);
        when(gesuchTrancheRepository.requireById(any()))
            .thenReturn(gesuchTranche_inBearbeitungGS
            );
        when(gesuchTrancheRepository.findById(any()))
            .thenReturn(gesuchTranche_inBearbeitungGS
            );
        when(gesuchRepository.requireGesuchByTrancheId(any())).thenReturn(gesuch);

        final var authorizer = new GesuchTrancheAuthorizer(benutzerService,gesuchTrancheRepository,gesuchRepository);
        final var uuid = UUID.randomUUID();
        assertDoesNotThrow(() -> authorizer.canDelete(uuid));
    }
}
