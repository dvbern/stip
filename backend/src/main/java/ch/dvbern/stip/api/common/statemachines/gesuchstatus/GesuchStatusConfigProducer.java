/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.common.statemachines.gesuchstatus;

import ch.dvbern.stip.api.common.exception.AppErrorException;
import ch.dvbern.stip.api.common.statemachines.gesuchstatus.handlers.GesuchStatusStateChangeHandler;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.transitions.Transition;
import jakarta.enterprise.inject.Instance;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@UtilityClass
@Slf4j
public class GesuchStatusConfigProducer {

    public StateMachineConfig<Gesuchstatus, GesuchStatusChangeEvent> createStateMachineConfig(
        Instance<GesuchStatusStateChangeHandler> handlers
    ) {
        final StateMachineConfig<Gesuchstatus, GesuchStatusChangeEvent> config = new StateMachineConfig<>();

        config.configure(Gesuchstatus.IN_BEARBEITUNG_GS)
            .permit(GesuchStatusChangeEvent.EINGEREICHT, Gesuchstatus.EINGEREICHT);

        config.configure(Gesuchstatus.EINGEREICHT)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
            .permit(
                GesuchStatusChangeEvent.ABKLAERUNG_DURCH_RECHSTABTEILUNG,
                Gesuchstatus.ABKLAERUNG_DURCH_RECHSTABTEILUNG
            )
            .permit(GesuchStatusChangeEvent.JURISTISCHE_ABKLAERUNG, Gesuchstatus.JURISTISCHE_ABKLAERUNG)
            .permit(GesuchStatusChangeEvent.ANSPRUCH_MANUELL_PRUEFEN, Gesuchstatus.ANSPRUCH_MANUELL_PRUEFEN)
            .permit(GesuchStatusChangeEvent.NICHT_ANSPRUCHSBERECHTIGT, Gesuchstatus.NICHT_ANSPRUCHSBERECHTIGT);

        config.configure(Gesuchstatus.ABKLAERUNG_DURCH_RECHSTABTEILUNG)
            .permit(GesuchStatusChangeEvent.EINGEREICHT, Gesuchstatus.EINGEREICHT)
            .permit(GesuchStatusChangeEvent.NICHT_BEITRAGSBERECHTIGT, Gesuchstatus.NICHT_BEITRAGSBERECHTIGT)
            .permit(GesuchStatusChangeEvent.NEGATIVE_VERFUEGUNG, Gesuchstatus.NEGATIVE_VERFUEGUNG);

        config.configure(Gesuchstatus.ANSPRUCH_MANUELL_PRUEFEN)
            .permit(GesuchStatusChangeEvent.JURISTISCHE_ABKLAERUNG, Gesuchstatus.JURISTISCHE_ABKLAERUNG)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
            .permit(GesuchStatusChangeEvent.NICHT_BEITRAGSBERECHTIGT, Gesuchstatus.NICHT_BEITRAGSBERECHTIGT)
            .permit(GesuchStatusChangeEvent.NEGATIVE_VERFUEGUNG, Gesuchstatus.NEGATIVE_VERFUEGUNG);

        config.configure(Gesuchstatus.NICHT_ANSPRUCHSBERECHTIGT)
            .permit(GesuchStatusChangeEvent.JURISTISCHE_ABKLAERUNG, Gesuchstatus.JURISTISCHE_ABKLAERUNG)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
            .permit(GesuchStatusChangeEvent.NICHT_BEITRAGSBERECHTIGT, Gesuchstatus.NICHT_BEITRAGSBERECHTIGT)
            .permit(GesuchStatusChangeEvent.NEGATIVE_VERFUEGUNG, Gesuchstatus.NEGATIVE_VERFUEGUNG);

        config.configure(Gesuchstatus.BEREIT_FUER_BEARBEITUNG)
            .permit(GesuchStatusChangeEvent.IN_BEARBEITUNG_SB, Gesuchstatus.IN_BEARBEITUNG_SB);

        config.configure(Gesuchstatus.IN_BEARBEITUNG_SB)
            .permit(GesuchStatusChangeEvent.FEHLENDE_DOKUMENTE, Gesuchstatus.FEHLENDE_DOKUMENTE)
            .permit(GesuchStatusChangeEvent.JURISTISCHE_ABKLAERUNG, Gesuchstatus.JURISTISCHE_ABKLAERUNG)
            .permit(GesuchStatusChangeEvent.VERFUEGT, Gesuchstatus.VERFUEGT)
            .permit(GesuchStatusChangeEvent.IN_FREIGABE, Gesuchstatus.IN_FREIGABE)
            .permit(GesuchStatusChangeEvent.IN_BEARBEITUNG_GS, Gesuchstatus.IN_BEARBEITUNG_GS)
            .permit(GesuchStatusChangeEvent.NEGATIVE_VERFUEGUNG, Gesuchstatus.NEGATIVE_VERFUEGUNG);

        config.configure(Gesuchstatus.FEHLENDE_DOKUMENTE)
            .permit(GesuchStatusChangeEvent.IN_BEARBEITUNG_GS, Gesuchstatus.IN_BEARBEITUNG_GS)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG);

        config.configure(Gesuchstatus.JURISTISCHE_ABKLAERUNG)
            .permit(GesuchStatusChangeEvent.BEREIT_FUER_BEARBEITUNG, Gesuchstatus.BEREIT_FUER_BEARBEITUNG);

        config.configure(Gesuchstatus.VERFUEGT)
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
            .permit(GesuchStatusChangeEvent.KEIN_STIPENDIENANSPRUCH, Gesuchstatus.KEIN_STIPENDIENANSPRUCH)
            .permit(GesuchStatusChangeEvent.STIPENDIENANSPRUCH, Gesuchstatus.STIPENDIENANSPRUCH);

        config.configure(Gesuchstatus.NEGATIVE_VERFUEGUNG)
            .permit(GesuchStatusChangeEvent.VERSANDBEREIT, Gesuchstatus.VERSANDBEREIT);

        // These aren't strictly necessary, but the Statusdiagramm isn't 100% complete yet and these are likely needed
        config.configure(Gesuchstatus.NICHT_BEITRAGSBERECHTIGT);
        config.configure(Gesuchstatus.KEIN_STIPENDIENANSPRUCH);
        config.configure(Gesuchstatus.STIPENDIENANSPRUCH);
        config.configure(Gesuchstatus.GESUCH_ABGELEHNT);

        for (final var status : Gesuchstatus.values()) {
            var state = config.getRepresentation(status);
            if (state == null) {
                LOG.error("Status '{}' ist nicht in der State Machine abgebildet", status);
                continue;
            }

            state.addEntryAction(GesuchStatusConfigProducer::logTransition);
            state.addEntryAction(
                (transition, args) -> {
                    final var gesuch = extractGesuchFromStateMachineArgs(args);
                    final var handler = getHandlerFor(handlers, transition);
                    if (handler.isPresent()) {
                        handler.get().handle(transition, gesuch);
                    } else {
                        LOG.info(
                            "No handler exists for Gesuchstatus transition {} -> {}",
                            transition.getSource(),
                            transition.getDestination()
                        );
                    }
                }
            );
        }

        return config;
    }

    private void logTransition(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition, Object[] args) {
        Gesuch gesuch = extractGesuchFromStateMachineArgs(args);

        LOG.info(
            "KSTIP: Gesuch mit id {} wurde von Status {} nach Status {} durch event {} geandert",
            gesuch.getId(),
            transition.getSource(),
            transition.getDestination(),
            transition.getTrigger()
        );
    }

    private Gesuch extractGesuchFromStateMachineArgs(Object[] args) {
        if (args.length == 0 || !(args[0] instanceof Gesuch)) {
            throw new AppErrorException(
                "State Transition args sollten einen Gesuch Objekt enthalten, es gibt ein Problem in den "
                + "Statemachine args"
            );
        }
        return (Gesuch) args[0];
    }

    private Optional<GesuchStatusStateChangeHandler> getHandlerFor(
        Instance<GesuchStatusStateChangeHandler> handlers,
        final Transition<Gesuchstatus, GesuchStatusChangeEvent> transition
    ) {
        return handlers.stream().filter(x -> x.handles(transition)).findFirst();
    }
}
