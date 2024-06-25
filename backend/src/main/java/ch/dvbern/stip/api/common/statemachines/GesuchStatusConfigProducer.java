package ch.dvbern.stip.api.common.statemachines;

import java.util.Optional;

import ch.dvbern.stip.api.common.exception.AppErrorException;
import ch.dvbern.stip.api.common.statemachines.handlers.StateChangeHandler;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.transitions.Transition;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Dependent
@RequiredArgsConstructor
@Slf4j
public class GesuchStatusConfigProducer {
    private final StateMachineConfig<Gesuchstatus, GesuchStatusChangeEvent> config = new StateMachineConfig<>();

    private final Instance<StateChangeHandler> handlers;

    @Produces
    public StateMachineConfig<Gesuchstatus, GesuchStatusChangeEvent> createStateMachineConfig() {
        config.configure(Gesuchstatus.IN_BEARBEITUNG_GS)
            .permit(GesuchStatusChangeEvent.GESUCH_EINGEREICHT, Gesuchstatus.GESUCH_EINGEREICHT);

        config.configure(Gesuchstatus.GESUCH_EINGEREICHT)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
            .permit(GesuchStatusChangeEvent.ABKLAERUNG_DURCH_RECHSTABTEILUNG, Gesuchstatus.ABKLAERUNG_DURCH_RECHSTABTEILUNG)
            .permit(GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN, Gesuchstatus.ANSPRUCH_MANUELL_PRUEFEN)
            .permit(GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT, Gesuchstatus.NICHT_ANSPRUCHSBERECHTIGT);

        // TODO KSTIP-1162: Is this OK?
        config.configure(Gesuchstatus.ABKLAERUNG_DURCH_RECHSTABTEILUNG)
            .permit(GesuchStatusChangeEvent.GESUCH_EINGEREICHT, Gesuchstatus.GESUCH_EINGEREICHT);

        config.configure(Gesuchstatus.ANSPRUCH_MANUELL_PRUEFEN)
            .permit(GesuchStatusChangeEvent.NICHT_BEITRAGSBERECHTIGT, Gesuchstatus.NICHT_BEITRAGSBERECHTIGT)
            .permit(GesuchStatusChangeEvent.JOUR_FIX, Gesuchstatus.JOUR_FIX)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG);

        config.configure(Gesuchstatus.NICHT_ANSPRUCHSBERECHTIGT)
            .permit(GesuchStatusChangeEvent.NICHT_BEITRAGSBERECHTIGT, Gesuchstatus.NICHT_BEITRAGSBERECHTIGT)
            .permit(GesuchStatusChangeEvent.JOUR_FIX, Gesuchstatus.JOUR_FIX)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG);

        // TODO KSTIP-1162: Manuelle Status Recksetzung
        // TODO KSTIP-1162: Gesuch akzeptiere zwischenstand? Nein? Da Businesslogik entscheidet welchen event fired wird
        config.configure(Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
            .permit(GesuchStatusChangeEvent.IN_BEARBEITUNG_SB, Gesuchstatus.IN_BEARBEITUNG_SB)
            .permit(GesuchStatusChangeEvent.GESUCH_ABGELEHNT, Gesuchstatus.GESUCH_ABGELEHNT);

        config.configure(Gesuchstatus.IN_BEARBEITUNG_SB)
            .permit(GesuchStatusChangeEvent.FEHLENDE_DOKUMENTE, Gesuchstatus.FEHLENDE_DOKUMENTE)
            .permit(GesuchStatusChangeEvent.JOUR_FIX, Gesuchstatus.JOUR_FIX)
            .permit(GesuchStatusChangeEvent.VERFUEGT, Gesuchstatus.VERFUEGT);

        config.configure(Gesuchstatus.FEHLENDE_DOKUMENTE)
            .permit(GesuchStatusChangeEvent.DOKUMENTE_NACHGELIEFERT, Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
            .permit(GesuchStatusChangeEvent.KEINE_DOKUMENTE_NACHGELIEFERT, Gesuchstatus.IN_BEARBEITUNG_GS);

        config.configure(Gesuchstatus.JOUR_FIX)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
            .permit(GesuchStatusChangeEvent.FEHLENDE_DOKUMENTE, Gesuchstatus.FEHLENDE_DOKUMENTE)
            .permit(GesuchStatusChangeEvent.NICHT_BEITRAGSBERECHTIGT, Gesuchstatus.NICHT_BEITRAGSBERECHTIGT);

        config.configure(Gesuchstatus.VERFUEGT)
            .permit(GesuchStatusChangeEvent.IN_FREIGABE, Gesuchstatus.IN_FREIGABE)
            .permit(GesuchStatusChangeEvent.WARTEN_AUF_UNTERSCHRIFTENBLATT, Gesuchstatus.WARTEN_AUF_UNTERSCHRIFTENBLATT)
            .permit(GesuchStatusChangeEvent.VERSANDBEREIT, Gesuchstatus.VERSANDBEREIT);

        config.configure(Gesuchstatus.IN_FREIGABE)
            .permit(GesuchStatusChangeEvent.VERFUEGT, Gesuchstatus.VERFUEGT)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG);

        config.configure(Gesuchstatus.WARTEN_AUF_UNTERSCHRIFTENBLATT)
            .permit(GesuchStatusChangeEvent.VERSANDBEREIT, Gesuchstatus.VERSANDBEREIT);

        config.configure(Gesuchstatus.VERSANDBEREIT)
            .permit(GesuchStatusChangeEvent.VERSENDET, Gesuchstatus.VERSENDET);

        config.configure(Gesuchstatus.VERSENDET)
            .permit(GesuchStatusChangeEvent.KEIN_STIPENDIEN_ANSPRUCH, Gesuchstatus.KEIN_STIPENDIEN_ANSPRUCH);

        for (final var status : Gesuchstatus.values()) {
            var state = config.getRepresentation(status);
            if (state == null) {
                LOG.error("Status '{}' ist nicht in der State Machine abgebildet", status);
                continue;
            }

            state.addEntryAction(this::logTransition);
            state.addEntryAction(this::onStateEntry);
        }

        return config;
    }

    private void logTransition(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition, Object[] args) {
        Gesuch gesuch = extractGesuchFromStateMachineArgs(args);

        LOG.info("KSTIP: Gesuch mit id {} wurde von Status {} nach Status {} durch event {} geandert", gesuch.getId(),
            transition.getSource(), transition.getDestination(), transition.getTrigger()
        );
    }

    private Gesuch extractGesuchFromStateMachineArgs(Object[] args) {
        if (args.length == 0 || !(args[0] instanceof Gesuch)) {
            throw new AppErrorException(
                "State Transition args sollte einen Gesuch Objekt enthalten, es gibt einen Problem in die "
                    + "Statemachine args");
        }
        return (Gesuch) args[0];
    }

    private Optional<StateChangeHandler> getHandlerFor(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition) {
        return handlers.stream().filter(x -> x.handles(transition)).findFirst();
    }

    private void onStateEntry(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition, Object[] args) {
        final var gesuch = extractGesuchFromStateMachineArgs(args);
        final var handler = getHandlerFor(transition);
        if (handler.isPresent()) {
            handler.get().handle(transition, gesuch);
        } else {
            LOG.info(
                "Es gibt kein handler für den die Transition von {} nach {}",
                transition.getSource(),
                transition.getDestination()
            );
        }
    }
}
