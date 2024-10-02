package ch.dvbern.stip.api.gesuch.service;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuch.util.GesuchTestUtil;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.ManyToOne;
import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class GesuchStatusServiceTest {
    @Inject
    GesuchStatusService gesuchStatusService;
    @InjectMock
    GesuchRepository gesuchRepository;

    Gesuch gesuch;

    @BeforeEach
    void setUp() {
        gesuch = GesuchTestUtil.setupValidGesuch();
        when(gesuchRepository.requireById(any())).thenReturn(gesuch);
    }

    @Test
    @Description("Statusuebergang aus 'In Freigabe' to 'Verfuegt' should work correctly")
    void statusUebergang_InFreigabe_Verfuegt_Test() {
        gesuch.setGesuchStatus(Gesuchstatus.IN_FREIGABE);
        assertDoesNotThrow(() ->gesuchStatusService.triggerStateMachineEvent(gesuch,
            GesuchStatusChangeEvent.VERFUEGT));
        assertSame(Gesuchstatus.VERFUEGT,gesuch.getGesuchStatus());
    }

    @Test
    @Description("Statusuebergang aus 'In Freigabe' to 'Bereit fuer Bearbeitung' should work correctly")
    void statusUebergang_InFreigabe_BereitFuerBearbeitung_Test() {
        gesuch.setGesuchStatus(Gesuchstatus.IN_FREIGABE);
        assertDoesNotThrow(() ->gesuchStatusService.triggerStateMachineEvent(gesuch,
            GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG));
        assertSame(Gesuchstatus.BEREIT_FUER_BEARBEITUNG,gesuch.getGesuchStatus());
    }

    @Test
    @Description("Statusuebergang aus 'Versandbereit' to 'Versendet' should work correctly")
    void statusUebergang_Versandbereit_Versendet_Test() {
        gesuch.setGesuchStatus(Gesuchstatus.VERSANDBEREIT);
        assertDoesNotThrow(() ->gesuchStatusService.triggerStateMachineEvent(gesuch,
            GesuchStatusChangeEvent.VERSENDET));
        assertSame(Gesuchstatus.VERSENDET,gesuch.getGesuchStatus());

    }
}
