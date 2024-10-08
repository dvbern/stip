package ch.dvbern.stip.api.common.statemachines;

import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.delegates.Action1;
import com.github.oxo42.stateless4j.transitions.Transition;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StateMachineUtil {
    public <S, C> void addExit(
        StateMachineConfig<S, C> config,
        Action1<Transition<S, C>> action,
        S[] possibleStates
    ) {
        for (final var status : possibleStates) {
            config.getRepresentation(status).addExitAction(action);
        }
    }
}
