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

import java.time.ZonedDateTime;

import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.api.communication.mail.service.MailServiceUtils;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.service.GesuchService;
import ch.dvbern.stip.api.gesuchhistory.repo.GesuchHistoryRepository;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.notification.service.NotificationService;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
@RequiredArgsConstructor
public class KomplettEingereichtHandler implements GesuchStatusChangeHandler {
    private final MailService mailService;
    private final NotificationService notificationService;
    private final GesuchStatusService gesuchStatusService;
    private final GesuchService gesuchService;
    private final GesuchHistoryRepository gesuchHistoryRepository;

    @Override
    public void handle(Gesuch gesuch) {
        MailServiceUtils.sendStandardNotificationEmailForGesuch(mailService, gesuch);
        notificationService.createGesuchEingereichtNotification(gesuch);
        gesuch.getGesuchTranchen()
            .stream()
            .filter(tranche -> tranche.getStatus() == GesuchTrancheStatus.IN_BEARBEITUNG_GS)
            .forEach(tranche -> tranche.setStatus(GesuchTrancheStatus.UEBERPRUEFEN));

        // Ensure that we don't rely on the timezone of the server to be Europe/Zurich
        final var todayInZuerich = ZonedDateTime.now(DateUtil.ZUERICH_ZONE).toLocalDate();
        gesuch.setEinreichedatum(todayInZuerich);
    }

}
