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

package ch.dvbern.stip.api.common.statemachines.gesuchstatus.handlers;

import java.util.Comparator;

import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.api.communication.mail.service.MailServiceUtils;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.notification.service.NotificationService;
import ch.dvbern.stip.api.verfuegung.entity.Verfuegung;
import com.github.oxo42.stateless4j.transitions.Transition;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
@RequiredArgsConstructor
public class VersendetHandler implements GesuchStatusStateChangeHandler {
    private final NotificationService notificationService;
    private final MailService mailService;

    @Override
    public boolean handles(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition) {
        return transition.getDestination() == Gesuchstatus.VERSENDET;
    }

    @Override
    public void handle(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition, Gesuch gesuch) {
        final var latestVerfuegung =
            gesuch.getVerfuegungs().stream().max(Comparator.comparing(Verfuegung::getTimestampErstellt));

        if (latestVerfuegung.isPresent()) {
            notificationService.createNeueVerfuegungNotification(latestVerfuegung.get());
            MailServiceUtils.sendStandardNotificationEmailForGesuch(mailService, gesuch);
        }
    }
}
