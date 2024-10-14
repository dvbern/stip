package ch.dvbern.stip.api.common.authorizer;

import java.util.Set;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.authorization.GesuchAuthorizer;
import ch.dvbern.stip.api.common.authorization.GesuchTrancheAuthorizer;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuch.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import io.quarkus.security.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class GesuchAuthorizerCanDeleteTest {
    private BenutzerService benutzerService;
    private GesuchStatusService gesuchStatusService;
    private FallRepository fallRepository;
    private Fall fall;
    private Benutzer currentBenutzer;
    private Benutzer otherBenutzer;
    private Gesuch gesuch;
    private GesuchAuthorizer authorizer;
    private GesuchTranche gesuchTranche_inBearbeitungGS;

    private GesuchTrancheRepository gesuchTrancheRepository;
    private GesuchRepository gesuchRepository;
    @BeforeEach
    void setUp() {
        benutzerService = Mockito.mock(BenutzerService.class);
        currentBenutzer = new Benutzer().setKeycloakId(UUID.randomUUID().toString());
        currentBenutzer.getRollen().add(new Rolle()
            .setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER));
        UUID currentBenutzerId = UUID.randomUUID();
        currentBenutzer.setId(currentBenutzerId);

        gesuchTranche_inBearbeitungGS = new GesuchTranche()
            .setGesuch(gesuch)
            .setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS);

        UUID otherBenutzerId = UUID.randomUUID();
        otherBenutzer = new Benutzer();
        otherBenutzer.setId(otherBenutzerId);
        otherBenutzer.getRollen().add(new Rolle()
            .setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER));

        when(benutzerService.getCurrentBenutzer()).thenReturn(currentBenutzer);

        gesuchRepository = Mockito.mock(GesuchRepository.class);
        gesuchTrancheRepository = Mockito.mock(GesuchTrancheRepository.class);
        fallRepository = Mockito.mock(FallRepository.class);
        gesuchStatusService = Mockito.mock(GesuchStatusService.class);

        gesuch = new Gesuch()
            .setFall(new Fall()
                .setGesuchsteller(currentBenutzer)
            );
        fall = new Fall().setGesuchsteller(currentBenutzer);
        authorizer = new GesuchAuthorizer(benutzerService,gesuchRepository
            ,gesuchStatusService,fallRepository);

        when(gesuchRepository.requireById(any()))
            .thenReturn(gesuch
            );
        when(gesuchTrancheRepository.requireById(any()))
            .thenReturn(gesuchTranche_inBearbeitungGS
            );
        when(gesuchTrancheRepository.findById(any()))
            .thenReturn(gesuchTranche_inBearbeitungGS
            );
        when(gesuchRepository.requireGesuchByTrancheId(any())).thenReturn(gesuch);
        when(fallRepository.requireById(any())).thenReturn(fall);
        when(gesuchStatusService.benutzerCanEdit(any(),any())).thenReturn(true);
    }

    @Test
    void canUpdateOwnTest() {
        // arrange
        final var uuid = UUID.randomUUID();
        // assert
        assertDoesNotThrow(() -> authorizer.canUpdate(uuid));
    }

    @Test
    void canDeleteOwnTest() {
        // arrange
        authorizer = new GesuchAuthorizer(benutzerService,gesuchRepository,
            null,null );
        final var uuid = UUID.randomUUID();
        // assert
        assertDoesNotThrow(() -> authorizer.canDelete(uuid));
    }

    @Test
    void cannotDeleteAnotherTest() {
        // arrange
        currentBenutzer.setRollen(Set.of());
        final var authorizer = new GesuchAuthorizer(benutzerService,gesuchRepository,
            null,null );
        final var uuid = UUID.randomUUID();
        // assert
        assertThrows(UnauthorizedException.class, () -> {
            authorizer.canDelete(uuid);
        });
    }

    @Test
    void adminCanDeleteTest() {
        // arrange
        final var authorizer = new GesuchAuthorizer(benutzerService,gesuchRepository,
            null,null );
        final var uuid = UUID.randomUUID();
        // assert
        assertDoesNotThrow(() -> authorizer.canDelete(uuid));
    }
}
