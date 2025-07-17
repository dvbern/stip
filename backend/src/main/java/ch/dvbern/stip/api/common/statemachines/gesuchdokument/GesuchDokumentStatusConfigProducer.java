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

package ch.dvbern.stip.api.common.statemachines.gesuchdokument;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import ch.dvbern.stip.api.common.exception.AppErrorException;
import ch.dvbern.stip.api.common.statemachines.gesuchdokument.handlers.GesuchDokumentAbgelehntToAusstehendStatusChangeHandler;
import ch.dvbern.stip.api.common.statemachines.gesuchdokument.handlers.GesuchDokumentStatusChangeHandler;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.GesuchDokumentStatus;
import ch.dvbern.stip.api.dokument.type.GesuchDokumentStatusChangeEvent;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.transitions.Transition;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters1;
import jakarta.enterprise.inject.Instance;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class GesuchDokumentStatusConfigProducer {
    public StateMachineConfig<GesuchDokumentStatus, GesuchDokumentStatusChangeEvent> createStateMachineConfig(
        Instance<GesuchDokumentStatusChangeHandler> handlers
    ) {
        final StateMachineConfig<GesuchDokumentStatus, GesuchDokumentStatusChangeEvent> config =
            new StateMachineConfig<>();
        Map<GesuchDokumentStatusChangeEvent, TriggerWithParameters1<GesuchDokument, GesuchDokumentStatusChangeEvent>> triggers =
            new HashMap<>();

        for (GesuchDokumentStatusChangeEvent event : GesuchDokumentStatusChangeEvent.values()) {
            triggers.put(event, config.setTriggerParameters(event, GesuchDokument.class));
        }

        config.configure(GesuchDokumentStatus.AUSSTEHEND)
            .permit(GesuchDokumentStatusChangeEvent.ABGELEHNT, GesuchDokumentStatus.ABGELEHNT)
            .permit(GesuchDokumentStatusChangeEvent.AKZEPTIERT, GesuchDokumentStatus.AKZEPTIERT)
            .onEntryFrom(
                triggers.get(GesuchDokumentStatusChangeEvent.AUSSTEHEND),
                (
                    gesuchTranche
                ) -> selectHandlerForClass(handlers, GesuchDokumentAbgelehntToAusstehendStatusChangeHandler.class)
                    .ifPresent(handler -> handler.handle(gesuchTranche))
            );

        config.configure(GesuchDokumentStatus.ABGELEHNT)
            .permit(GesuchDokumentStatusChangeEvent.AUSSTEHEND, GesuchDokumentStatus.AUSSTEHEND);

        config.configure(GesuchDokumentStatus.AKZEPTIERT)
            .permit(GesuchDokumentStatusChangeEvent.AUSSTEHEND, GesuchDokumentStatus.AUSSTEHEND);

        for (final var status : GesuchDokumentStatus.values()) {
            var state = config.getRepresentation(status);
            if (state == null) {
                LOG.error("Status '{}' ist nicht in der State Machine abgebildet", status);
                continue;
            }

            state.addEntryAction(GesuchDokumentStatusConfigProducer::logTransition);
        }

        return config;
    }

    @SuppressWarnings("unchecked")
    private <T extends GesuchDokumentStatusChangeHandler> Optional<T> selectHandlerForClass(
        final Instance<GesuchDokumentStatusChangeHandler> handlers,
        final Class<T> forClass
    ) {
        return handlers.stream()
            .filter(handler -> handler.getClass().equals(forClass))
            .map(handler -> (T) handler)
            .findFirst();
    }

    private void logTransition(
        Transition<GesuchDokumentStatus, GesuchDokumentStatusChangeEvent> transition,
        Object[] args
    ) {
        GesuchDokument gesuchDokument = extractGesuchFromStateMachineArgs(args);

        LOG.info(
            "KSTIP: Gesuchdokument mit id {} wurde von Status {} nach Status {} durch event {} geandert",
            gesuchDokument.getId(),
            transition.getSource(),
            transition.getDestination(),
            transition.getTrigger()
        );
    }

    private GesuchDokument extractGesuchFromStateMachineArgs(Object[] args) {
        if (args.length == 0 || !(args[0] instanceof GesuchDokument)) {
            throw new AppErrorException(
                "State Transition args sollten einen Gesuch Objekt enthalten, es gibt ein Problem in den "
                + "Statemachine args"
            );
        }
        return (GesuchDokument) args[0];
    }
}
