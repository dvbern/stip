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

import java.util.EnumMap;

import ch.dvbern.stip.api.common.exception.AppErrorException;
import ch.dvbern.stip.api.common.statemachines.gesuchtranche.handlers.AkzeptiertHandler;
import ch.dvbern.stip.api.common.statemachines.gesuchtranche.handlers.GesuchTrancheFehlendeDokumenteEinreichenHandler;
import ch.dvbern.stip.api.common.statemachines.gesuchtranche.handlers.GesuchTrancheFehlendeDokumenteHandler;
import ch.dvbern.stip.api.common.statemachines.gesuchtranche.handlers.GesuchTrancheFehlendeDokumenteNichtEingereichtHandler;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatusChangeEvent;
import ch.dvbern.stip.api.statusprotokoll.service.StatusprotokollService;
import ch.dvbern.stip.api.statusprotokoll.type.StatusprotokollEntryTyp;
import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.transitions.Transition;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters1;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class GesuchTrancheStatusConfigProducer {
    private final GesuchTrancheFehlendeDokumenteNichtEingereichtHandler gesuchTrancheFehlendeDokumenteNichtEingereichtHandler;
    private final GesuchTrancheFehlendeDokumenteEinreichenHandler gesuchTrancheFehlendeDokumenteEinreichenHandler;
    private final AkzeptiertHandler akzeptiertHandler;
    private final GesuchTrancheFehlendeDokumenteHandler gesuchTrancheFehlendeDokumenteHandler;
    private final StatusprotokollService statusprotokollService;

    public StateMachineConfig<GesuchTrancheStatus, GesuchTrancheStatusChangeEvent> createStateMachineConfig() {
        final StateMachineConfig<GesuchTrancheStatus, GesuchTrancheStatusChangeEvent> config =
            new StateMachineConfig<>();
        final var triggers =
            new EnumMap<GesuchTrancheStatusChangeEvent, TriggerWithParameters1<GesuchTranche, GesuchTrancheStatusChangeEvent>>(
                GesuchTrancheStatusChangeEvent.class
            );

        for (GesuchTrancheStatusChangeEvent event : GesuchTrancheStatusChangeEvent.values()) {
            triggers.put(event, config.setTriggerParameters(event, GesuchTranche.class));
        }

        config.configure(GesuchTrancheStatus.IN_BEARBEITUNG_GS)
            .permit(GesuchTrancheStatusChangeEvent.UEBERPRUEFEN, GesuchTrancheStatus.UEBERPRUEFEN)
            .onEntryFrom(
                triggers.get(GesuchTrancheStatusChangeEvent.IN_BEARBEITUNG_GS),
                gesuchTrancheFehlendeDokumenteNichtEingereichtHandler::handle
            );

        config.configure(GesuchTrancheStatus.UEBERPRUEFEN)
            .permit(GesuchTrancheStatusChangeEvent.ABLEHNEN, GesuchTrancheStatus.IN_BEARBEITUNG_GS)
            .permit(GesuchTrancheStatusChangeEvent.AKZEPTIERT, GesuchTrancheStatus.AKZEPTIERT)
            .permit(GesuchTrancheStatusChangeEvent.MANUELLE_AENDERUNG, GesuchTrancheStatus.MANUELLE_AENDERUNG)
            .permit(GesuchTrancheStatusChangeEvent.FEHLENDE_DOKUMENTE, GesuchTrancheStatus.FEHLENDE_DOKUMENTE)
            .onEntryFrom(
                triggers.get(GesuchTrancheStatusChangeEvent.UEBERPRUEFEN),
                gesuchTrancheFehlendeDokumenteEinreichenHandler::handle
            );

        config.configure(GesuchTrancheStatus.MANUELLE_AENDERUNG)
            .permit(GesuchTrancheStatusChangeEvent.AKZEPTIERT, GesuchTrancheStatus.AKZEPTIERT);

        config.configure(GesuchTrancheStatus.ABGELEHNT);

        config.configure(GesuchTrancheStatus.AKZEPTIERT)
            .onEntryFrom(
                triggers.get(GesuchTrancheStatusChangeEvent.AKZEPTIERT),
                akzeptiertHandler::handle
            );

        config.configure(GesuchTrancheStatus.FEHLENDE_DOKUMENTE)
            .permit(GesuchTrancheStatusChangeEvent.UEBERPRUEFEN, GesuchTrancheStatus.UEBERPRUEFEN)
            .permit(GesuchTrancheStatusChangeEvent.IN_BEARBEITUNG_GS, GesuchTrancheStatus.IN_BEARBEITUNG_GS)
            .onEntryFrom(
                triggers.get(GesuchTrancheStatusChangeEvent.FEHLENDE_DOKUMENTE),
                gesuchTrancheFehlendeDokumenteHandler::handle
            );

        for (final var status : GesuchTrancheStatus.values()) {
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
        Transition<GesuchTrancheStatus, GesuchTrancheStatusChangeEvent> transition,
        Object[] args
    ) {
        GesuchTranche gesuchTranche = extractGesuchFromStateMachineArgs(args);

        statusprotokollService.createStatusprotokoll(
            transition.getDestination().toString(),
            transition.getSource().toString(),
            StatusprotokollEntryTyp.AENDERUNG,
            gesuchTranche.getComment(),
            gesuchTranche.getGesuch()

        );

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
