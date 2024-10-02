package ch.dvbern.stip.api.gesuch.service;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuch.util.GesuchTestUtil;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jdk.jfr.Description;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest
class GesuchStatusServiceTest {
    @Inject
    GesuchStatusService gesuchStatusService;

    @Test
    @Description("Statusuebergang aus 'In Freigabe' to 'Verfuegt' should work correctly")
    void statusUebergang_InFreigabe_Verfuegt_Test() {
        Gesuch gesuch = GesuchTestUtil.setupValidGesuch();
        gesuch.setGesuchStatus(Gesuchstatus.IN_FREIGABE);
        assertDoesNotThrow(() ->gesuchStatusService.triggerStateMachineEvent(gesuch,
            GesuchStatusChangeEvent.VERFUEGT));
    }

    @Test
    @Description("Statusuebergang aus 'In Freigabe' to 'Bereit fuer Bearbeitung' should work correctly")
    void statusUebergang_InFreigabe_BereitFuerBearbeitung_Test() {
        Gesuch gesuch = GesuchTestUtil.setupValidGesuch();
        gesuch.setGesuchStatus(Gesuchstatus.IN_FREIGABE);
        assertDoesNotThrow(() ->gesuchStatusService.triggerStateMachineEvent(gesuch,
            GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG));
    }
}
