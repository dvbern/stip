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

package ch.dvbern.stip.api.common.statemachines.gesuch.handlers;

import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.api.communication.mail.service.MailServiceUtils;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.notification.service.NotificationService;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class AenderungFehlendeDokumenteNichtEingereichtHandler implements GesuchStatusChangeHandler {
    private final NotificationService notificationService;
    private final MailService mailService;
    private final GesuchService gesuchService;

    @Override
    @Transactional
    public void handle(Gesuch gesuch) {
        if (!gesuch.isVerfuegt()) {
            illegalHandleCall();
        }
        notificationService.createGesuchFehlendeDokumenteNichtEingereichtText(gesuch);
        gesuch.setNachfristDokumente(null);
        gesuchService.resetGesuchZurueckweisen(gesuch);
        MailServiceUtils.sendStandardNotificationEmailForGesuch(mailService, gesuch);
    }
}
