package ch.dvbern.stip.api.common.statemachines.gesuch.handlers;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.unterschriftenblatt.service.UnterschriftenblattService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@ApplicationScoped
@Slf4j
@RequiredArgsConstructor
public class DatenschutzDruckbereitHandler implements GesuchStatusChangeHandler {
    private final UnterschriftenblattService unterschriftenblattService;
    private final GesuchStatusService gesuchStatusService;

    @Transactional
    @Override
    public void handle(Gesuch gesuch) {
        // todo kstip-2663: add log/status message
        if(unterschriftenblattService.getUnterschriftenblaetterToUpload(gesuch).isEmpty()) {
            gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG);
        }else {
            // todo KSTIP-2663 handle other case properly. But for now change Gesuchstatus to the following state
            gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG);
        }
    }
}
