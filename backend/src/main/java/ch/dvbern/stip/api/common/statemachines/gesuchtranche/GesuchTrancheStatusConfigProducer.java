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

package ch.dvbern.stip.api.common.statemachines.gesuchtranche;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import ch.dvbern.stip.api.common.exception.AppErrorException;
import ch.dvbern.stip.api.common.statemachines.gesuchtranche.handlers.AkzeptiertHandler;
import ch.dvbern.stip.api.common.statemachines.gesuchtranche.handlers.GesuchTrancheFehlendeDokumenteEinreichenHandler;
import ch.dvbern.stip.api.common.statemachines.gesuchtranche.handlers.GesuchTrancheFehlendeDokumenteHandler;
import ch.dvbern.stip.api.common.statemachines.gesuchtranche.handlers.GesuchTrancheFehlendeDokumenteNichtEingereichtHandler;
import ch.dvbern.stip.api.common.statemachines.gesuchtranche.handlers.GesuchTrancheStatusChangeHandler;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatusChangeEvent;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.transitions.Transition;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters1;
import jakarta.enterprise.inject.Instance;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class GesuchTrancheStatusConfigProducer {

    public StateMachineConfig<GesuchTrancheStatus, GesuchTrancheStatusChangeEvent> createStateMachineConfig(
        Instance<GesuchTrancheStatusChangeHandler> handlers
    ) {
        final StateMachineConfig<GesuchTrancheStatus, GesuchTrancheStatusChangeEvent> config =
            new StateMachineConfig<>();
        Map<GesuchTrancheStatusChangeEvent, TriggerWithParameters1<GesuchTranche, GesuchTrancheStatusChangeEvent>> triggers =
            new HashMap<>();

        for (GesuchTrancheStatusChangeEvent event : GesuchTrancheStatusChangeEvent.values()) {
            triggers.put(event, config.setTriggerParameters(event, GesuchTranche.class));
        }

        config.configure(GesuchTrancheStatus.IN_BEARBEITUNG_GS)
            .permit(GesuchTrancheStatusChangeEvent.UEBERPRUEFEN, GesuchTrancheStatus.UEBERPRUEFEN)
            .onEntryFrom(
                triggers.get(GesuchTrancheStatusChangeEvent.IN_BEARBEITUNG_GS),
                (
                    gesuchTranche
                ) -> selectHandlerForClass(handlers, GesuchTrancheFehlendeDokumenteNichtEingereichtHandler.class)
                    .ifPresent(handler -> handler.handle(gesuchTranche))
            );

        config.configure(GesuchTrancheStatus.UEBERPRUEFEN)
            .permit(GesuchTrancheStatusChangeEvent.ABLEHNEN, GesuchTrancheStatus.IN_BEARBEITUNG_GS)
            .permit(GesuchTrancheStatusChangeEvent.AKZEPTIERT, GesuchTrancheStatus.AKZEPTIERT)
            .permit(GesuchTrancheStatusChangeEvent.MANUELLE_AENDERUNG, GesuchTrancheStatus.MANUELLE_AENDERUNG)
            .permit(GesuchTrancheStatusChangeEvent.FEHLENDE_DOKUMENTE, GesuchTrancheStatus.FEHLENDE_DOKUMENTE)
            .onEntryFrom(
                triggers.get(GesuchTrancheStatusChangeEvent.UEBERPRUEFEN),
                (
                    gesuchTranche
                ) -> selectHandlerForClass(handlers, GesuchTrancheFehlendeDokumenteEinreichenHandler.class)
                    .ifPresent(handler -> handler.handle(gesuchTranche))
            );

        config.configure(GesuchTrancheStatus.MANUELLE_AENDERUNG)
            .permit(GesuchTrancheStatusChangeEvent.AKZEPTIERT, GesuchTrancheStatus.AKZEPTIERT);

        config.configure(GesuchTrancheStatus.ABGELEHNT);

        config.configure(GesuchTrancheStatus.AKZEPTIERT)
            .onEntryFrom(
                triggers.get(GesuchTrancheStatusChangeEvent.AKZEPTIERT),
                (gesuchTranche) -> selectHandlerForClass(handlers, AkzeptiertHandler.class)
                    .ifPresent(handler -> handler.handle(gesuchTranche))
            );

        config.configure(GesuchTrancheStatus.FEHLENDE_DOKUMENTE)
            .permit(GesuchTrancheStatusChangeEvent.UEBERPRUEFEN, GesuchTrancheStatus.UEBERPRUEFEN)
            .permit(GesuchTrancheStatusChangeEvent.IN_BEARBEITUNG_GS, GesuchTrancheStatus.IN_BEARBEITUNG_GS)
            .onEntryFrom(
                triggers.get(GesuchTrancheStatusChangeEvent.FEHLENDE_DOKUMENTE),
                (gesuchTranche) -> selectHandlerForClass(handlers, GesuchTrancheFehlendeDokumenteHandler.class)
                    .ifPresent(handler -> handler.handle(gesuchTranche))
            );

        for (final var status : GesuchTrancheStatus.values()) {
            var state = config.getRepresentation(status);
            if (state == null) {
                LOG.error("Status '{}' ist nicht in der State Machine abgebildet", status);
                continue;
            }

            state.addEntryAction(GesuchTrancheStatusConfigProducer::logTransition);
        }

        return config;
    }

    @SuppressWarnings("unchecked")
    private <T extends GesuchTrancheStatusChangeHandler> Optional<T> selectHandlerForClass(
        final Instance<GesuchTrancheStatusChangeHandler> handlers,
        final Class<T> forClass
    ) {
        return handlers.stream()
            .filter(handler -> handler.getClass().equals(forClass))
            .map(handler -> (T) handler)
            .findFirst();
    }

    private void logTransition(
        Transition<GesuchTrancheStatus, GesuchTrancheStatusChangeEvent> transition,
        Object[] args
    ) {
        GesuchTranche gesuchTranche = extractGesuchFromStateMachineArgs(args);

        LOG.info(
            "KSTIP: GesuchTranche mit id {} wurde von Status {} nach Status {} durch event {} geandert",
            gesuchTranche.getId(),
            transition.getSource(),
            transition.getDestination(),
            transition.getTrigger()
        );
    }

    private GesuchTranche extractGesuchFromStateMachineArgs(Object[] args) {
        if (args.length == 0 || !(args[0] instanceof GesuchTranche gesuchTranche)) {
            throw new AppErrorException(
                "State Transition args sollte einen GesuchTranche Objekt enthalten, es gibt einen Problem in die "
                + "Statemachine args"
            );
        }

        return gesuchTranche;
    }
}
