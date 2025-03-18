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
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatusChangeEvent;
import ch.dvbern.stip.api.notification.service.NotificationService;
import com.github.oxo42.stateless4j.transitions.Transition;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class GesuchTrancheFehlendeDokumenteHandler implements GesuchTrancheStatusStateChangeHandler {
    private final GesuchDokumentService gesuchDokumentService;
    private final NotificationService notificationService;
    private final MailService mailService;

    private void sendFehlendeDokumenteNotifications(Gesuch gesuch) {
        notificationService.createMissingDocumentNotification(gesuch);
        MailServiceUtils.sendStandardNotificationEmailForGesuch(mailService, gesuch);
    }

    @Override
    public boolean handles(Transition<GesuchTrancheStatus, GesuchTrancheStatusChangeEvent> transition) {
        return transition.getDestination() == GesuchTrancheStatus.FEHLENDE_DOKUMENTE;
    }

    @Override
    public void handle(
        Transition<GesuchTrancheStatus, GesuchTrancheStatusChangeEvent> transition,
        GesuchTranche gesuchTranche
    ) {
        gesuchDokumentService.setAbgelehnteDokumenteToAusstehendForGesuch(gesuchTranche.getGesuch());
        sendFehlendeDokumenteNotifications(gesuchTranche.getGesuch());
    }
}
