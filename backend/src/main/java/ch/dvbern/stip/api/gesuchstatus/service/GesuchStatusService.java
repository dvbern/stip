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

package ch.dvbern.stip.api.gesuchstatus.service;

import java.time.LocalDateTime;
import java.util.List;

import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.common.statemachines.StateMachineUtil;
import ch.dvbern.stip.api.common.statemachines.gesuchstatus.GesuchStatusConfigProducer;
import ch.dvbern.stip.api.common.statemachines.gesuchstatus.handlers.GesuchStatusStateChangeHandler;
import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.api.communication.mail.service.MailServiceUtils;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchvalidation.service.GesuchValidatorService;
import ch.dvbern.stip.api.notification.service.NotificationService;
import ch.dvbern.stip.generated.dto.KommentarDto;
import com.github.oxo42.stateless4j.StateMachine;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class GesuchStatusService {
    private final GesuchValidatorService validationService;
    private final MailService mailService;
    private final NotificationService notificationService;

    private final Instance<GesuchStatusStateChangeHandler> handlers;

    @Transactional
    public void triggerStateMachineEvent(final Gesuch gesuch, final GesuchStatusChangeEvent event) {
        triggerStateMachineEventWithComment(gesuch, event, null, false);
    }

    @Transactional
    public void triggerStateMachineEventWithComment(
        final Gesuch gesuch,
        final GesuchStatusChangeEvent event,
        final KommentarDto kommentarDto,
        final boolean sendNotificationIfPossible
    ) {
        final var sm = createStateMachine(gesuch, kommentarDto);
        sm.fire(GesuchStatusChangeEventTrigger.createTrigger(event), gesuch);

        if (kommentarDto != null && sendNotificationIfPossible) {
            MailServiceUtils.sendStandardNotificationEmailForGesuch(mailService, gesuch);
            notificationService.createGesuchStatusChangeWithCommentNotification(gesuch, kommentarDto);
        }
    }

    public void bulkTriggerStateMachineEvent(
        final List<Gesuch> gesuche,
        final GesuchStatusChangeEvent event
    ) {
        for (final Gesuch gesuch : gesuche) {
            try {
                triggerStateMachineEvent(gesuch, event);
            } catch (ValidationsException ignored) {
                // ignored
            }
        }
    }

    public boolean canFire(final Gesuch gesuch, final GesuchStatusChangeEvent target) {
        final var sm = createStateMachine(gesuch, null);
        return sm.canFire(target);
    }

    private StateMachine<Gesuchstatus, GesuchStatusChangeEvent> createStateMachine(
        final Gesuch gesuch,
        final KommentarDto kommentarDto
    ) {
        final var config = GesuchStatusConfigProducer.createStateMachineConfig(handlers);

        StateMachineUtil.addExit(
            config,
            transition -> validationService.validateGesuchForStatus(gesuch, transition.getDestination()),
            Gesuchstatus.values()
        );

        return new StateMachine<>(
            gesuch.getGesuchStatus(),
            gesuch::getGesuchStatus,
            status -> gesuch.setGesuchStatus(status)
                .setGesuchStatusAenderungDatum(LocalDateTime.now())
                .setComment(kommentarDto == null ? "" : kommentarDto.getText()),
            config
        );
    }
}
