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

package ch.dvbern.stip.api.notification.service;

import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.notification.entity.Notification;
import ch.dvbern.stip.api.notification.repo.NotificationRepository;
import ch.dvbern.stip.api.notification.type.NotificationType;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.generated.dto.KommentarDto;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class AenderungNotificationService {
    private final NotificationRepository notificationRepository;
    private final MailService mailService;

    @Transactional
    public void createEingereichtNotificationAndSendStdMail(final Gesuch gesuch) {
        final var pia = gesuch.getGesuchTranchen().get(0).getGesuchFormular().getPersonInAusbildung();

        final String msg =
            Templates.getEingereicht(pia.getVorname(), pia.getNachname(), pia.getKorrespondenzSprache()).render();

        final Notification notification = new Notification()
            .setNotificationType(NotificationType.AENDERUNG_EINGEREICHT)
            .setFall(gesuch.getAusbildung().getFall())
            .setNotificationText(msg);
        NotificationUtil.setAbsender(gesuch, notification);

        notificationRepository.persistAndFlush(notification);
        mailService.sendStandardNotificationEmailForGesuch(gesuch);
    }

    @Transactional
    public void createAbgelehntNotificationAndSendStdMail(
        final Gesuch gesuch,
        final GesuchTranche aenderung,
        final KommentarDto kommentarDto
    ) {

        final var pia = aenderung.getGesuchFormular().getPersonInAusbildung();

        final String msg = Templates
            .getAbgelehnt(pia.getVorname(), pia.getNachname(), kommentarDto.getText(), pia.getKorrespondenzSprache())
            .render();

        final Notification notification = new Notification()
            .setNotificationType(NotificationType.AENDERUNG_ABGELEHNT)
            .setFall(gesuch.getAusbildung().getFall())
            .setNotificationText(msg);
        NotificationUtil.setAbsender(gesuch, notification);

        notificationRepository.persistAndFlush(notification);
        mailService.sendStandardNotificationEmailForGesuch(gesuch);
    }

    @Transactional
    public void createInitiatedBySachbearbeiterNotificationAndSendStdMail(
        final Gesuch gesuch,
        final KommentarDto kommentar
    ) {
        final var pia = gesuch.getNewestGesuchTranche()
            .orElseThrow(NotFoundException::new)
            .getGesuchFormular()
            .getPersonInAusbildung();

        final String msg =
            Templates.getInitiatedBySachbearbeiter(kommentar.getText(), pia.getKorrespondenzSprache()).render();

        final Notification notification = new Notification()
            .setNotificationType(NotificationType.GESUCH_IN_BEARBEITUNG_AS_AENDERUNG)
            .setFall(gesuch.getAusbildung().getFall())
            .setNotificationText(msg);
        NotificationUtil.setAbsender(gesuch, notification);

        notificationRepository.persistAndFlush(notification);
        mailService.sendStandardNotificationEmailForGesuch(gesuch);
    }

    @CheckedTemplate
    private static class Templates {
        public static TemplateInstance getAbgelehnt(
            final String vorname,
            final String nachname,
            final String kommentar,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return abgelehntFR(vorname, nachname, kommentar);
            }
            return abgelehntDE(vorname, nachname, kommentar);
        }

        public static native TemplateInstance abgelehntDE(
            final String vorname,
            final String nachname,
            final String kommentar
        );

        public static native TemplateInstance abgelehntFR(
            final String vorname,
            final String nachname,
            final String kommentar
        );

        public static TemplateInstance getEingereicht(
            final String vorname,
            final String nachname,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return eingereichtFR(vorname, nachname);
            }
            return eingereichtDE(vorname, nachname);
        }

        public static native TemplateInstance eingereichtDE(final String vorname, final String nachname);

        public static native TemplateInstance eingereichtFR(final String vorname, final String nachname);

        public static TemplateInstance getInitiatedBySachbearbeiter(
            final String kommentar,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return initiatedBySachbearbeiterFR(kommentar);
            }
            return initiatedBySachbearbeiterDE(kommentar);
        }

        public static native TemplateInstance initiatedBySachbearbeiterDE(final String kommentar);

        public static native TemplateInstance initiatedBySachbearbeiterFR(final String kommentar);
    }
}
