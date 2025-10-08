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
import java.util.Objects;

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
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters2;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;

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
            new EnumMap<GesuchTrancheStatusChangeEvent, TriggerWithParameters2<GesuchTranche, String, GesuchTrancheStatusChangeEvent>>(
                GesuchTrancheStatusChangeEvent.class
            );

        for (GesuchTrancheStatusChangeEvent event : GesuchTrancheStatusChangeEvent.values()) {
            triggers.put(event, config.setTriggerParameters(event, GesuchTranche.class, String.class));
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
                triggers.get(GesuchTrancheStatusChangeEvent.FEHLENDE_DOKUMENTE_EINREICHEN),
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
            .permit(GesuchTrancheStatusChangeEvent.FEHLENDE_DOKUMENTE_EINREICHEN, GesuchTrancheStatus.UEBERPRUEFEN)
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
        final var gesuchAndComment = extractGesuchAndCommentFromStateMachineArgs(args);
        final var gesuchTranche = gesuchAndComment.getLeft();
        final var comment = gesuchAndComment.getRight();

        statusprotokollService.createStatusprotokoll(
            transition.getDestination().toString(),
            transition.getSource().toString(),
            StatusprotokollEntryTyp.AENDERUNG,
            comment,
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

    private Pair<GesuchTranche, String> extractGesuchAndCommentFromStateMachineArgs(Object[] args) {
        if (
            args.length == 2
            && args[0] instanceof GesuchTranche gesuchTranche
        ) {
            if (args[1] instanceof String comment) {
                return Pair.of(gesuchTranche, comment);
            }
            if (Objects.isNull(args[1])) {
                return Pair.of(gesuchTranche, gesuchTranche.getComment());
            }
        }
        throw new AppErrorException(
            "State Transition args sollte ein String+GesuchTranche Objekt enthalten, es gibt ein Problem in den Statemachine args"
        );
    }
}
