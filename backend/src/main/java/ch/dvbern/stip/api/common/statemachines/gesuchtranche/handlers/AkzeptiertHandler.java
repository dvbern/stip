package ch.dvbern.stip.api.common.statemachines.gesuchtranche.handlers;

import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatusChangeEvent;
import com.github.oxo42.stateless4j.transitions.Transition;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class AkzeptiertHandler implements GesuchTrancheStatusStateChangeHandler {
    @Override
    public boolean handles(Transition<GesuchTrancheStatus, GesuchTrancheStatusChangeEvent> transition) {
        return transition.getSource() == GesuchTrancheStatus.UEBERPRUEFEN &&
            transition.getDestination() == GesuchTrancheStatus.AKZETPIERT;
    }

    @Override
    public void handle(Transition<GesuchTrancheStatus, GesuchTrancheStatusChangeEvent> transition, GesuchTranche gesuch) {
        // TODO KSTIP-1104: Call service to truncate other Tranchen
    }
}
