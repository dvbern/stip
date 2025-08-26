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

package ch.dvbern.stip.api.common.statemachines.gesuchtranche.handlers;

import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.api.communication.mail.service.MailServiceUtils;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.notification.service.NotificationService;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchTrancheFehlendeDokumenteNichtEingereichtHandler implements GesuchTrancheStatusChangeHandler {
    private final NotificationService notificationService;
    private final MailService mailService;

    @Override
    public void handle(
        GesuchTranche gesuchTranche
    ) {
        notificationService.createGesuchFehlendeDokumenteNichtEingereichtNotification(gesuchTranche.getGesuch());
        gesuchTranche.getGesuch().setNachfristDokumente(null);
        gesuchTranche.getGesuch().setEinreichedatum(null);
        MailServiceUtils.sendStandardNotificationEmailForGesuch(mailService, gesuchTranche.getGesuch());
    }
}
