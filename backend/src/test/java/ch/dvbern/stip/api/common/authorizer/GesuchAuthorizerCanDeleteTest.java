package ch.dvbern.stip.api.common.authorizer;

import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.GesuchAuthorizer;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.service.GesuchStatusService;
import io.quarkus.security.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GesuchAuthorizerCanDeleteTest {
    @Test
    void canDeleteOwnTest() {
        var currentBenutzer = new Benutzer().setKeycloakId(UUID.randomUUID().toString());
        currentBenutzer.setRollen(Set.of(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER)));
        final var benutzerService = Mockito.mock(BenutzerService.class);
        when(benutzerService.getCurrentBenutzer()).thenReturn(currentBenutzer);

        final var gesuchRepository = Mockito.mock(GesuchRepository.class);
        when(gesuchRepository.requireById(any()))
            .thenReturn(new Gesuch()
                .setFall(new Fall()
                    .setGesuchsteller(currentBenutzer)
                )
            );

        final var gesuchStatusService = Mockito.mock(GesuchStatusService.class);
        when(gesuchStatusService.benutzerCanEdit(any(),any())).thenReturn(true);

        final var authorizer = new GesuchAuthorizer(benutzerService,gesuchRepository, gesuchStatusService,null);
        final var uuid = UUID.randomUUID();
        assertDoesNotThrow(() -> authorizer.canDelete(uuid));
    }

    @Test
    void cannotDeleteAnotherTest() {
        final var currentBenutzer = new Benutzer().setKeycloakId(UUID.randomUUID().toString());
        final var benutzerService = Mockito.mock(BenutzerService.class);
        when(benutzerService.getCurrentBenutzer()).thenReturn(currentBenutzer);

        final var gesuchRepository = Mockito.mock(GesuchRepository.class);
        when(gesuchRepository.requireById(any()))
            .thenReturn(new Gesuch()
                .setFall(new Fall()
                    .setGesuchsteller(new Benutzer().setKeycloakId(UUID.randomUUID().toString()))
                )
            );
        final var gesuchStatusService = Mockito.mock(GesuchStatusService.class);
        when(gesuchStatusService.benutzerCanEdit(any(),any())).thenReturn(true);
        final var authorizer = new GesuchAuthorizer(benutzerService,gesuchRepository, gesuchStatusService,null);
        final var uuid = UUID.randomUUID();
        assertThrows(UnauthorizedException.class, () -> {
            authorizer.canDelete(uuid);
        });
    }

    @Test
    void adminCanDeleteTest() {
        final var benutzerService = Mockito.mock(BenutzerService.class);
        when(benutzerService.getCurrentBenutzer())
            .thenReturn(new Benutzer().setRollen(Set.of(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_ADMIN))));
        final var gesuchStatusService = Mockito.mock(GesuchStatusService.class);
        when(gesuchStatusService.benutzerCanEdit(any(),any())).thenReturn(true);
        final var gesuchRepository = Mockito.mock(GesuchRepository.class);
        when(gesuchRepository.requireById(any()))
            .thenReturn(new Gesuch()
                .setFall(new Fall()
                    .setGesuchsteller(new Benutzer().setKeycloakId(UUID.randomUUID().toString()))
                )
            );
        final var authorizer = new GesuchAuthorizer(benutzerService,gesuchRepository, gesuchStatusService,null);
        final var uuid = UUID.randomUUID();
        assertDoesNotThrow(() -> authorizer.canDelete(uuid));
    }
}
