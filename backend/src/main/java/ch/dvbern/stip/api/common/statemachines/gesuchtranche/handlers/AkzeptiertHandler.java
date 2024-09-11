package ch.dvbern.stip.api.common.statemachines.gesuchtranche.handlers;

import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.service.GesuchTrancheService;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatusChangeEvent;
import com.github.oxo42.stateless4j.transitions.Transition;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class AkzeptiertHandler implements GesuchTrancheStatusStateChangeHandler {
    private final GesuchTrancheService gesuchTrancheService;

    @Override
    public boolean handles(Transition<GesuchTrancheStatus, GesuchTrancheStatusChangeEvent> transition) {
        return transition.getSource() == GesuchTrancheStatus.UEBERPRUEFEN &&
            transition.getDestination() == GesuchTrancheStatus.AKZEPTIERT;
    }

    @Override
    public void handle(
        Transition<GesuchTrancheStatus, GesuchTrancheStatusChangeEvent> transition,
        GesuchTranche gesuchTranche
    ) {
        gesuchTrancheService.aenderungEinbinden(gesuchTranche);
    }
}
