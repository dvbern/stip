package ch.dvbern.stip.api.common.statemachines.handlers;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import com.github.oxo42.stateless4j.transitions.Transition;

public interface StateChangeHandler {
    boolean handles(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition);

    void handle(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition, Gesuch gesuch);
}
