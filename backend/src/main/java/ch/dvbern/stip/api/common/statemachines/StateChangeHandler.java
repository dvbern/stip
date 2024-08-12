package ch.dvbern.stip.api.common.statemachines;

import com.github.oxo42.stateless4j.transitions.Transition;

public interface StateChangeHandler<S, C, A> {
    boolean handles(Transition<S, C> transition);

    void handle(Transition<S, C> transition, A gesuch);
}
