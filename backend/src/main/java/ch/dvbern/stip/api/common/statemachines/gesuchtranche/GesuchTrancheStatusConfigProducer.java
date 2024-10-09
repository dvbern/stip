package ch.dvbern.stip.api.common.statemachines.gesuchtranche;

import java.util.Optional;

import ch.dvbern.stip.api.common.exception.AppErrorException;
import ch.dvbern.stip.api.common.statemachines.gesuchtranche.handlers.GesuchTrancheStatusStateChangeHandler;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatusChangeEvent;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.transitions.Transition;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Dependent
@RequiredArgsConstructor
public class GesuchTrancheStatusConfigProducer {
    private final StateMachineConfig<GesuchTrancheStatus, GesuchTrancheStatusChangeEvent> config =
        new StateMachineConfig<>();

    private final Instance<GesuchTrancheStatusStateChangeHandler> handlers;

    @Produces
    public StateMachineConfig<GesuchTrancheStatus, GesuchTrancheStatusChangeEvent> createStateMachineConfig() {
        config.configure(GesuchTrancheStatus.IN_BEARBEITUNG_GS)
            .permit(GesuchTrancheStatusChangeEvent.UEBERPRUEFEN, GesuchTrancheStatus.UEBERPRUEFEN);

        config.configure(GesuchTrancheStatus.UEBERPRUEFEN)
            .permit(GesuchTrancheStatusChangeEvent.ABLEHNEN, GesuchTrancheStatus.IN_BEARBEITUNG_GS)
            .permit(GesuchTrancheStatusChangeEvent.AKZETPIERT, GesuchTrancheStatus.AKZEPTIERT)
            .permit(GesuchTrancheStatusChangeEvent.MANUELLE_AENDERUNG, GesuchTrancheStatus.MANUELLE_AENDERUNG);

        config.configure(GesuchTrancheStatus.MANUELLE_AENDERUNG)
            .permit(GesuchTrancheStatusChangeEvent.AKZETPIERT, GesuchTrancheStatus.AKZEPTIERT);

        config.configure(GesuchTrancheStatus.ABGELEHNT);
        config.configure(GesuchTrancheStatus.AKZEPTIERT);

        for (final var status : GesuchTrancheStatus.values()) {
            final var state = config.getRepresentation(status);
            state.addEntryAction(this::onStateEntry);
        }

        return config;
    }

    private GesuchTranche extractGesuchFromStateMachineArgs(Object[] args) {
        if (args.length == 0 || !(args[0] instanceof GesuchTranche gesuchTranche)) {
            throw new AppErrorException(
                "State Transition args sollte einen GesuchTranche Objekt enthalten, es gibt einen Problem in die "
                    + "Statemachine args");
        }

        return gesuchTranche;
    }

    private Optional<GesuchTrancheStatusStateChangeHandler> getHandlerFor(
        final Transition<GesuchTrancheStatus, GesuchTrancheStatusChangeEvent> transition
    ) {
        return handlers.stream().filter(handler -> handler.handles(transition)).findFirst();
    }

    private void onStateEntry(
        final Transition<GesuchTrancheStatus, GesuchTrancheStatusChangeEvent> transition,
        final Object[] args
    ) {
        final var gesuchTranche = extractGesuchFromStateMachineArgs(args);
        final var handler = getHandlerFor(transition);
        if (handler.isPresent()) {
            handler.get().handle(transition, gesuchTranche);
        } else {
            LOG.info(
                "No handler exists for GesuchTrancheStatus transition {} -> {}",
                transition.getSource(),
                transition.getDestination()
            );
        }
    }
}
