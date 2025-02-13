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

import java.util.HashSet;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.common.statemachines.StateMachineUtil;
import ch.dvbern.stip.api.common.statemachines.gesuchtranche.GesuchTrancheStatusConfigProducer;
import ch.dvbern.stip.api.common.statemachines.gesuchtranche.handlers.GesuchTrancheStatusStateChangeHandler;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatusChangeEvent;
import ch.dvbern.stip.generated.dto.KommentarDto;
import com.github.oxo42.stateless4j.StateMachine;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class GesuchTrancheStatusService {
    private final GesuchTrancheValidatorService validatorService;

    private final Instance<GesuchTrancheStatusStateChangeHandler> handlers;

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

    public boolean benutzerCanEdit(final Benutzer benutzer, final GesuchTrancheStatus gesuchTrancheStatus) {
        final var identifiers = benutzer.getRollen()
            .stream()
            .map(Rolle::getKeycloakIdentifier)
            .collect(Collectors.toSet());
        final var editStates = new HashSet<GesuchTrancheStatus>();
        if (identifiers.contains(OidcConstants.ROLE_GESUCHSTELLER)) {
            editStates.addAll(GesuchTrancheStatus.GESUCHSTELLER_CAN_EDIT);
        }

        if (identifiers.contains(OidcConstants.ROLE_SACHBEARBEITER)) {
            editStates.addAll(GesuchTrancheStatus.SACHBEARBEITER_CAN_EDIT);
        }

        if (identifiers.contains(OidcConstants.ROLE_ADMIN)) {
            editStates.addAll(GesuchTrancheStatus.ADMIN_CAN_EDIT);
        }

        return editStates.contains(gesuchTrancheStatus);
    }

    StateMachine<GesuchTrancheStatus, GesuchTrancheStatusChangeEvent> createStateMachine(
        final GesuchTranche gesuchTranche
    ) {
        var config = GesuchTrancheStatusConfigProducer.createStateMachineConfig(handlers);

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
