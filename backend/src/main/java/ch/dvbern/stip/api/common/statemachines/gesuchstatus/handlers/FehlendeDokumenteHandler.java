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

import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.api.communication.mail.service.MailServiceUtils;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus;
import ch.dvbern.stip.api.notification.service.NotificationService;
import com.github.oxo42.stateless4j.transitions.Transition;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class FehlendeDokumenteHandler implements GesuchStatusStateChangeHandler {
    private final GesuchDokumentService gesuchDokumentService;
    private final NotificationService notificationService;
    private final MailService mailService;

    @Override
    public boolean handles(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition) {
        return transition.getDestination() == Gesuchstatus.FEHLENDE_DOKUMENTE;
    }

    @Override
    public void handle(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition, Gesuch gesuch) {
        /*
         * KSTIP-1750: do not delete the GesuchDokument (in state ABGELEHNT) anymore,
         * but only the attached files
         */
        // gesuchDokumentService.deleteAbgelehnteDokumenteForGesuch(gesuch);
        gesuch.getGesuchTranchen()
            .stream()
            .filter(tranche -> tranche.getStatus() == GesuchTrancheStatus.UEBERPRUEFEN)
            .forEach(tranche -> tranche.setStatus(GesuchTrancheStatus.IN_BEARBEITUNG_GS));
        sendFehlendeDokumenteNotifications(gesuch);
    }

    private void sendFehlendeDokumenteNotifications(Gesuch gesuch) {
        notificationService.createMissingDocumentNotification(gesuch);
        MailServiceUtils.sendStandardNotificationEmailForGesuch(mailService, gesuch);
    }
}
