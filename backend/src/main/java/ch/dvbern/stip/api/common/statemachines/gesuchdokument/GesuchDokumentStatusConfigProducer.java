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

import java.util.Optional;

import ch.dvbern.stip.api.common.exception.AppErrorException;
import ch.dvbern.stip.api.common.statemachines.gesuchdokument.handlers.GesuchDokumentAbgelehntToAusstehendStatusChangeHandler;
import ch.dvbern.stip.api.common.statemachines.gesuchdokument.handlers.GesuchDokumentStatusChangeHandler;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.type.Dokumentstatus;
import ch.dvbern.stip.api.dokument.type.DokumentstatusChangeEvent;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.transitions.Transition;
import jakarta.enterprise.inject.Instance;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class GesuchDokumentStatusConfigProducer {
    public StateMachineConfig<Dokumentstatus, DokumentstatusChangeEvent> createStateMachineConfig(
        Instance<GesuchDokumentStatusChangeHandler> handlers
    ) {
        final StateMachineConfig<Dokumentstatus, DokumentstatusChangeEvent> config = new StateMachineConfig<>();
        config.configure(Dokumentstatus.AUSSTEHEND)
            .permit(DokumentstatusChangeEvent.ABGELEHNT, Dokumentstatus.ABGELEHNT)
            .permit(DokumentstatusChangeEvent.AKZEPTIERT, Dokumentstatus.AKZEPTIERT);

        config.configure(Dokumentstatus.ABGELEHNT)
            .permit(DokumentstatusChangeEvent.AUSSTEHEND, Dokumentstatus.AUSSTEHEND)
            .onEntryFrom(
                selectHandlerForClass(handlers, GesuchDokumentAbgelehntToAusstehendStatusChangeHandler.class).get()
                    .trigger(config, DokumentstatusChangeEvent.AUSSTEHEND),
                (
                    gesuchTranche
                ) -> selectHandlerForClass(handlers, GesuchDokumentAbgelehntToAusstehendStatusChangeHandler.class)
                    .ifPresent(handler -> handler.handle(gesuchTranche))
            );
        config.configure(Dokumentstatus.AKZEPTIERT)
            .permit(DokumentstatusChangeEvent.AUSSTEHEND, Dokumentstatus.AUSSTEHEND);

        for (final var status : Dokumentstatus.values()) {
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

    private void logTransition(Transition<Dokumentstatus, DokumentstatusChangeEvent> transition, Object[] args) {
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
