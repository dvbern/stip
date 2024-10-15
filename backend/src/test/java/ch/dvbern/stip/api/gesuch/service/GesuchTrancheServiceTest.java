package ch.dvbern.stip.api.gesuch.service;

import java.time.LocalDate;
import java.util.List;

import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheTyp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GesuchTrancheServiceTest {
    private Gesuch gesuch;
    private GesuchTrancheService gesuchTrancheService;

    @BeforeEach
    void setUp() {
        gesuch = new Gesuch().setGesuchTranchen(List.of(new GesuchTranche()
            .setGueltigkeit(new DateRange(LocalDate.MIN, LocalDate.MAX)))
        );

        gesuchTrancheService = new GesuchTrancheService(
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
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
}
