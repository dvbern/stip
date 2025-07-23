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

import java.util.EnumMap;

import ch.dvbern.stip.api.common.exception.AppErrorException;
import ch.dvbern.stip.api.common.statemachines.gesuchdokument.handlers.GesuchDokumentAbgelehntToAusstehendStatusChangeHandler;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.GesuchDokumentStatus;
import ch.dvbern.stip.api.dokument.type.GesuchDokumentStatusChangeEvent;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.transitions.Transition;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters1;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class GesuchDokumentStatusConfigProducer {
    private final GesuchDokumentAbgelehntToAusstehendStatusChangeHandler gesuchDokumentAbgelehntToAusstehendStatusChangeHandler;

    public StateMachineConfig<GesuchDokumentStatus, GesuchDokumentStatusChangeEvent> createStateMachineConfig() {
        final StateMachineConfig<GesuchDokumentStatus, GesuchDokumentStatusChangeEvent> config =
            new StateMachineConfig<>();
        final var triggers =
            new EnumMap<GesuchDokumentStatusChangeEvent, TriggerWithParameters1<GesuchDokument, GesuchDokumentStatusChangeEvent>>(
                GesuchDokumentStatusChangeEvent.class
            );

        for (GesuchDokumentStatusChangeEvent event : GesuchDokumentStatusChangeEvent.values()) {
            triggers.put(event, config.setTriggerParameters(event, GesuchDokument.class));
        }

        config.configure(GesuchDokumentStatus.AUSSTEHEND)
            .permit(GesuchDokumentStatusChangeEvent.ABGELEHNT, GesuchDokumentStatus.ABGELEHNT)
            .permit(GesuchDokumentStatusChangeEvent.AKZEPTIERT, GesuchDokumentStatus.AKZEPTIERT)
            .onEntryFrom(
                triggers.get(GesuchDokumentStatusChangeEvent.AUSSTEHEND),
                this.gesuchDokumentAbgelehntToAusstehendStatusChangeHandler::handle
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

            state.addEntryAction(this::logTransition);
        }

        return config;
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
