package ch.dvbern.stip.api.gesuch.service;

import java.time.LocalDateTime;

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

    public void triggerStateMachineEvent(Gesuch gesuch, GesuchStatusChangeEvent event) {
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
