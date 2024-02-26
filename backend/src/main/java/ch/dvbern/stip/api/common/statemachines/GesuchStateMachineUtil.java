package ch.dvbern.stip.api.common.statemachines;

import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.delegates.Action1;
import com.github.oxo42.stateless4j.transitions.Transition;

public final class GesuchStateMachineUtil {
    public static void addExit(
        StateMachineConfig<Gesuchstatus, GesuchStatusChangeEvent> config,
        Action1<Transition<Gesuchstatus, GesuchStatusChangeEvent>> action) {
        for (final var status : Gesuchstatus.values()) {
            config.getRepresentation(status).addExitAction(action);
        }
    }
}
