package ch.dvbern.stip.api.common.authorizer;

import java.util.Set;
import java.util.UUID;

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
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp;
import io.quarkus.security.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GesuchTrancheAuthorizerCanDeleteTest {
    private BenutzerService benutzerService;
    private Benutzer currentBenutzer;
    private Benutzer otherBenutzer;
    private Gesuch gesuch;
    private GesuchTrancheAuthorizer authorizer;
    private GesuchTranche gesuchTranche_inBearbeitungGS;

    private GesuchTrancheRepository gesuchTrancheRepository;
    private GesuchRepository gesuchRepository;

    @BeforeEach
    void setup(){
        benutzerService = Mockito.mock(BenutzerService.class);
        currentBenutzer = new Benutzer().setKeycloakId(UUID.randomUUID().toString());
        currentBenutzer.getRollen().add(new Rolle().setKeycloakIdentifier(OidcConstants.ROLE_GESUCHSTELLER));
        UUID currentBenutzerId = UUID.randomUUID();
        currentBenutzer.setId(currentBenutzerId);

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

        gesuch = new Gesuch()
            .setFall(new Fall()
                .setGesuchsteller(currentBenutzer)
            );

        authorizer = new GesuchTrancheAuthorizer(benutzerService, gesuchTrancheRepository, gesuchRepository);

        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuchTranche_inBearbeitungGS);
        when(gesuchTrancheRepository.findById(any())).thenReturn(gesuchTranche_inBearbeitungGS);
        when(gesuchRepository.requireGesuchByTrancheId(any())).thenReturn(gesuch);
    }

    @Test
    void gsCanotDeleteOwnAenderungInWrongStateTest(){
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
        assertThrows(UnauthorizedException.class, () -> authorizer.canDeleteAenderung(uuid));
    }
    @Test
    void gsCanNotDeleteTrancheTest(){
        // arrange
        gesuchTranche_inBearbeitungGS.setTyp(GesuchTrancheTyp.TRANCHE);
        final var uuid = UUID.randomUUID();
        // assert
        assertThrows(UnauthorizedException.class, () -> authorizer.canDeleteAenderung(uuid));
    }
    @Test
    void gsCanDeleteAenderungOnlyOwnAenderungTest(){
        // arrange
        gesuchTranche_inBearbeitungGS.setTyp(GesuchTrancheTyp.AENDERUNG);
        final var uuid = UUID.randomUUID();
        // assert
        assertDoesNotThrow(() -> authorizer.canDeleteAenderung(uuid));
    }
    @Test
    void gsCannotDeleteWithoutRoleTest(){
        // arrange
        currentBenutzer.setRollen(Set.of());
        when(benutzerService.getCurrentBenutzer()).thenReturn(currentBenutzer);
        final var uuid = UUID.randomUUID();
        // assert
        assertThrows(UnauthorizedException.class, () -> authorizer.canDeleteAenderung(uuid));
    }
    @Test
    void gsCannotDeleteOtherAenderungTest(){
        // arrange
        currentBenutzer.setRollen(Set.of());
        final var uuid = UUID.randomUUID();
        // assert
        assertThrows(UnauthorizedException.class, () -> authorizer.canDeleteAenderung(uuid));
    }
    @Test
    void adminCanNotDeleteAenderungAenderungTest(){
        // arrange
        currentBenutzer.setRollen(Set.of(new Rolle()
            .setKeycloakIdentifier(OidcConstants.ROLE_ADMIN)));
        final var uuid = UUID.randomUUID();
        //assert
        assertThrows(UnauthorizedException.class, () -> authorizer.canDeleteAenderung(uuid));
    }

    @Test
    void sbCanNotDeleteAenderungAenderungTest(){
        // arrange
        currentBenutzer.setRollen(Set.of(new Rolle()
            .setKeycloakIdentifier(OidcConstants.ROLE_SACHBEARBEITER)));
        final var uuid = UUID.randomUUID();
        //assert
        assertThrows(UnauthorizedException.class, () -> authorizer.canDeleteAenderung(uuid));
    }
}
