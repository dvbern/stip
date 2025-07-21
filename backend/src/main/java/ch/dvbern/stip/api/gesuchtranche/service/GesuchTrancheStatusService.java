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

package ch.dvbern.stip.api.gesuchtranche.service;

import java.util.List;

import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.common.statemachines.StateMachineUtil;
import ch.dvbern.stip.api.common.statemachines.gesuchtranche.GesuchTrancheStatusConfigProducer;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatusChangeEvent;
import ch.dvbern.stip.generated.dto.KommentarDto;
import com.github.oxo42.stateless4j.StateMachine;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class GesuchTrancheStatusService {
    private final GesuchTrancheValidatorService validatorService;
    private final GesuchTrancheStatusConfigProducer configProducer;

    public void bulkTriggerStateMachineEvent(
        final List<GesuchTranche> gesuchTranches,
        final GesuchTrancheStatusChangeEvent event
    ) {
        for (final GesuchTranche gesuchTranche : gesuchTranches) {
            try {
                triggerStateMachineEvent(gesuchTranche, event);
            } catch (ValidationsException ignored) {
                // ignored
            }
        }
    }

    @Transactional
    public void triggerStateMachineEvent(
        final GesuchTranche gesuchTranche,
        final GesuchTrancheStatusChangeEvent event
    ) {
        final var sm = createStateMachine(gesuchTranche);
        sm.fire(GesuchTrancheStatusChangeEventTrigger.createTrigger(event), gesuchTranche);
    }

    @Transactional
    public void triggerStateMachineEventWithComment(
        final GesuchTranche gesuchTranche,
        final GesuchTrancheStatusChangeEvent event,
        final KommentarDto kommentarDto
    ) {
        final var sm = createStateMachine(gesuchTranche);
        sm.fire(
            GesuchTrancheStatusChangeEventTriggerWithComment.createTrigger(event),
            gesuchTranche,
            kommentarDto.getText()
        );

        // TODO: KSTIP-XXXX - Save kommentarDto.getText() in Nachricht and Protokoll
    }

    StateMachine<GesuchTrancheStatus, GesuchTrancheStatusChangeEvent> createStateMachine(
        final GesuchTranche gesuchTranche
    ) {
        var config = configProducer.createStateMachineConfig();

        StateMachineUtil.addExit(
            config,
            transition -> validatorService.validateGesuchTrancheForStatus(gesuchTranche, transition.getDestination()),
            GesuchTrancheStatus.values()
        );

        return new StateMachine<>(
            gesuchTranche.getStatus(),
            gesuchTranche::getStatus,
            gesuchTranche::setStatus,
            config
        );
    }
}
