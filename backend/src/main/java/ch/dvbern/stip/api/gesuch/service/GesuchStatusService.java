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

package ch.dvbern.stip.api.gesuch.service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.stream.Collectors;

import ch.dvbern.stip.api.benutzer.entity.Benutzer;
import ch.dvbern.stip.api.benutzer.entity.Rolle;
import ch.dvbern.stip.api.common.statemachines.StateMachineUtil;
import ch.dvbern.stip.api.common.util.OidcConstants;
import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.api.communication.mail.service.MailServiceUtils;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.notification.service.NotificationService;
import ch.dvbern.stip.generated.dto.KommentarDto;
import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import lombok.RequiredArgsConstructor;

@RequestScoped
@RequiredArgsConstructor
public class GesuchStatusService {
    private final StateMachineConfig<Gesuchstatus, GesuchStatusChangeEvent> config;
    private final GesuchValidatorService validationService;
    private final MailService mailService;
    private final NotificationService notificationService;

    @Transactional
    public void triggerStateMachineEvent(final Gesuch gesuch, final GesuchStatusChangeEvent event) {
        triggerStateMachineEventWithComment(gesuch, event, null);
    }

    @Transactional(TxType.REQUIRES_NEW)
    public void triggerStateMachineEventWithComment(
        final Gesuch gesuch,
        final GesuchStatusChangeEvent event,
        final KommentarDto kommentarDto
    ) {
        StateMachineUtil.addExit(
            config,
            transition -> validationService.validateGesuchForStatus(gesuch, transition.getDestination()),
            Gesuchstatus.values()
        );

        final var sm = new StateMachine<>(
            gesuch.getGesuchStatus(),
            gesuch::getGesuchStatus,
            status -> gesuch.setGesuchStatus(status)
                .setGesuchStatusAenderungDatum(LocalDateTime.now())
                .setComment(kommentarDto == null ? "" : kommentarDto.getText()),
            config
        );

        sm.fire(GesuchStatusChangeEventTrigger.createTrigger(event), gesuch);

        if (kommentarDto != null) {
            MailServiceUtils.sendStandardNotificationEmailForGesuch(mailService, gesuch);
            notificationService.createGesuchStatusChangeWithCommentNotification(gesuch, kommentarDto);
        }
    }

    public boolean benutzerCanEdit(final Benutzer benutzer, final Gesuchstatus gesuchstatus) {
        final var identifiers = benutzer.getRollen()
            .stream()
            .map(Rolle::getKeycloakIdentifier)
            .collect(Collectors.toSet());
        final var editStates = new HashSet<Gesuchstatus>();
        if (identifiers.contains(OidcConstants.ROLE_GESUCHSTELLER)) {
            editStates.addAll(Gesuchstatus.GESUCHSTELLER_CAN_EDIT);
        }
        if (identifiers.contains(OidcConstants.ROLE_SACHBEARBEITER)) {
            editStates.addAll(Gesuchstatus.SACHBEARBEITER_CAN_EDIT);
        }
        if (identifiers.contains(OidcConstants.ROLE_ADMIN)) {
            editStates.addAll(Gesuchstatus.ADMIN_CAN_EDIT);
        }

        return editStates.contains(gesuchstatus);
    }
}
