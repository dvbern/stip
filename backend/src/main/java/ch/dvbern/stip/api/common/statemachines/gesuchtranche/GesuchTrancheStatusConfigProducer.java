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

import java.util.Optional;

import ch.dvbern.stip.api.common.exception.AppErrorException;
import ch.dvbern.stip.api.common.statemachines.gesuchtranche.handlers.GesuchTrancheStatusStateChangeHandler;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatusChangeEvent;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.transitions.Transition;
import jakarta.enterprise.inject.Instance;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class GesuchTrancheStatusConfigProducer {

    public StateMachineConfig<GesuchTrancheStatus, GesuchTrancheStatusChangeEvent> createStateMachineConfig(
        Instance<GesuchTrancheStatusStateChangeHandler> handlers
    ) {
        final StateMachineConfig<GesuchTrancheStatus, GesuchTrancheStatusChangeEvent> config =
            new StateMachineConfig<>();

        config.configure(GesuchTrancheStatus.IN_BEARBEITUNG_GS)
            .permit(GesuchTrancheStatusChangeEvent.UEBERPRUEFEN, GesuchTrancheStatus.UEBERPRUEFEN);

        config.configure(GesuchTrancheStatus.UEBERPRUEFEN)
            .permit(GesuchTrancheStatusChangeEvent.ABLEHNEN, GesuchTrancheStatus.IN_BEARBEITUNG_GS)
            .permit(GesuchTrancheStatusChangeEvent.AKZEPTIERT, GesuchTrancheStatus.AKZEPTIERT)
            .permit(GesuchTrancheStatusChangeEvent.MANUELLE_AENDERUNG, GesuchTrancheStatus.MANUELLE_AENDERUNG)
            .permit(GesuchTrancheStatusChangeEvent.FEHLENDE_DOKUMENTE, GesuchTrancheStatus.FEHLENDE_DOKUMENTE);

        config.configure(GesuchTrancheStatus.MANUELLE_AENDERUNG)
            .permit(GesuchTrancheStatusChangeEvent.AKZEPTIERT, GesuchTrancheStatus.AKZEPTIERT);

        config.configure(GesuchTrancheStatus.ABGELEHNT);
        config.configure(GesuchTrancheStatus.AKZEPTIERT);

        config.configure(GesuchTrancheStatus.FEHLENDE_DOKUMENTE)
            .permit(GesuchTrancheStatusChangeEvent.UEBERPRUEFEN, GesuchTrancheStatus.UEBERPRUEFEN)
            .permit(GesuchTrancheStatusChangeEvent.IN_BEARBEITUNG_GS, GesuchTrancheStatus.IN_BEARBEITUNG_GS);

        for (final var status : GesuchTrancheStatus.values()) {
            final var state = config.getRepresentation(status);
            state.addEntryAction(
                (transition, args) -> {
                    final var gesuchTranche = extractGesuchFromStateMachineArgs(args);
                    final var handler = getHandlerFor(handlers, transition);
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
            );
        }

        return config;
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

    private Optional<GesuchTrancheStatusStateChangeHandler> getHandlerFor(
        Instance<GesuchTrancheStatusStateChangeHandler> handlers,
        final Transition<GesuchTrancheStatus, GesuchTrancheStatusChangeEvent> transition
    ) {
        return handlers.stream().filter(handler -> handler.handles(transition)).findFirst();
    }
}
