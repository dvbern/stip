package ch.dvbern.stip.api.gesuch.service;

import java.time.LocalDateTime;

import ch.dvbern.stip.api.common.statemachines.GesuchStateMachineUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchStatusService {
    private final StateMachineConfig<Gesuchstatus, GesuchStatusChangeEvent> config;
    private final GesuchValidatorService validationService;

    public void triggerStateMachineEvent(Gesuch gesuch, GesuchStatusChangeEvent event) {
        GesuchStateMachineUtil.addExit(
            config,
            transition -> validationService.validateGesuchForStatus(gesuch, transition.getDestination())
        );

        final var sm = new StateMachine<>(
            gesuch.getGesuchStatus(),
            gesuch::getGesuchStatus,
            s -> gesuch.setGesuchStatus(s)
                .setGesuchStatusAenderungDatum(LocalDateTime.now()),
            config
        );

        sm.fire(GesuchStatusChangeEventTrigger.createTrigger(event), gesuch);
    }
}
