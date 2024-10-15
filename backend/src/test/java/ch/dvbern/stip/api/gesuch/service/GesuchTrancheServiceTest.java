package ch.dvbern.stip.api.gesuch.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.repo.GesuchTrancheRepository;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class GesuchTrancheServiceTest {
    private Gesuch gesuch;

    @InjectMock
    private GesuchTrancheRepository gesuchTrancheRepository;

    @Inject
    private GesuchTrancheService gesuchTrancheService;
    @Inject
    private BenutzerService benutzerService;

    @BeforeEach
    void setUp() {
        gesuch = new Gesuch().setGesuchTranchen(List.of(new GesuchTranche()
            .setGueltigkeit(new DateRange(LocalDate.MIN, LocalDate.MAX)))
        );
    }

    @TestAsGesuchsteller
    @Test
    void onlyOneAenderungShouldBeAllowed() {
        // arrange
        gesuch.getCurrentGesuchTranche().setTyp(GesuchTrancheTyp.AENDERUNG);
        gesuch.getGesuchTranchen().get(0).setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS);
        // act & assert
        assertTrue(gesuchTrancheService.openAenderungAlreadyExists(gesuch));
    }

    @TestAsGesuchsteller
    @Test
    void onlyOneAenderungShouldBeAllowed_Tranche() {
        // arrange
        gesuch.getCurrentGesuchTranche().setTyp(GesuchTrancheTyp.TRANCHE);
        gesuch.getGesuchTranchen().get(0).setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS);
        // assert
        assertFalse(gesuchTrancheService.openAenderungAlreadyExists(gesuch));
    }

    @TestAsGesuchsteller
    @Test
    void aenderungShouldBeAllowedWhenStateAbgelehnt() {
        // arrange
        gesuch.getCurrentGesuchTranche().setTyp(GesuchTrancheTyp.AENDERUNG);
        gesuch.getGesuchTranchen().get(0).setStatus(GesuchTrancheStatus.ABGELEHNT);
        // assert
        assertFalse(gesuchTrancheService.openAenderungAlreadyExists(gesuch));
    }

    @TestAsGesuchsteller
    @Test
    void aenderungShouldBeAllowedWhenStateAbgelehnt_Tranche() {
        // arrange
        gesuch.getCurrentGesuchTranche().setTyp(GesuchTrancheTyp.TRANCHE);
        gesuch.getGesuchTranchen().get(0).setStatus(GesuchTrancheStatus.ABGELEHNT);
        // assert
        assertFalse(gesuchTrancheService.openAenderungAlreadyExists(gesuch));
    }

    @TestAsGesuchsteller
    @Test
    void aenderungShouldBeAllowedWhenStateAngenommen() {
        // arrange
        gesuch.getCurrentGesuchTranche().setTyp(GesuchTrancheTyp.AENDERUNG);
        gesuch.getGesuchTranchen().get(0).setStatus(GesuchTrancheStatus.AKZEPTIERT);
        // assert
        assertFalse(gesuchTrancheService.openAenderungAlreadyExists(gesuch));
    }

    @TestAsGesuchsteller
    @Test
    void aenderungShouldBeAllowedWhenStateAngenommen_Tranche() {
        // arrange
        gesuch.getCurrentGesuchTranche().setTyp(GesuchTrancheTyp.TRANCHE);
        gesuch.getGesuchTranchen().get(0).setStatus(GesuchTrancheStatus.AKZEPTIERT);
        // assert
        assertFalse(gesuchTrancheService.openAenderungAlreadyExists(gesuch));
    }

    @TestAsGesuchsteller
    @Test
    void gsShouldBeAbleToDeleteAenderungTest(){
        // arrange
        when(gesuchTrancheRepository.requireById(any())).thenReturn(gesuch.getGesuchTranchen().get(0));
        when(gesuchTrancheRepository.deleteById(any())).thenReturn(true);
        gesuch.getCurrentGesuchTranche().setTyp(GesuchTrancheTyp.AENDERUNG);
        gesuch.getGesuchTranchen().get(0).setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS);
        gesuch.getGesuchTranchen().get(0).setId(UUID.randomUUID());
        //assert
        assertDoesNotThrow(() -> gesuchTrancheService.deleteAenderung(gesuch.getGesuchTranchen().get(0).getId()));
    }
}
