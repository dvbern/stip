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

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.benutzer.service.BenutzerService;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.notification.entity.Notification;
import ch.dvbern.stip.api.notification.repo.NotificationRepository;
import ch.dvbern.stip.api.notification.type.NotificationType;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.generated.dto.KommentarDto;
import ch.dvbern.stip.generated.dto.NotificationDto;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class NotificationService {
    private final BenutzerService benutzerService;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Transactional
    public void deleteNotificationsForGesuch(final UUID gesuchId) {
        notificationRepository.deleteAllForGesuch(gesuchId);
    }

    @Transactional
    public List<NotificationDto> getNotificationsForCurrentUser() {
        return getNotificationsForUser(benutzerService.getCurrentBenutzer().getId());
    }

    @Transactional
    public List<NotificationDto> getNotificationsForUser(final UUID userId) {
        return notificationRepository.getAllForUser(userId).map(notificationMapper::toDto).toList();
    }

    @Transactional
    public void createGesuchNachfristDokumenteChangedNotification(final Gesuch gesuch) {
        final var pia = gesuch.getNewestGesuchTranche()
            .orElseThrow(NotFoundException::new)
            .getGesuchFormular()
            .getPersonInAusbildung();
        final var sprache = pia.getKorrespondenzSprache();

        Notification notification = new Notification()
            .setNotificationType(NotificationType.NACHFRIST_DOKUMENTE_CHANGED)
            .setGesuch(gesuch);
        var nachfristDokumente = "";
        if (Objects.nonNull(gesuch.getNachfristDokumente())) {
            nachfristDokumente = DateUtil.formatDate(gesuch.getNachfristDokumente());
        }
        String msg = Templates.getNachfristDokumenteChangedText(sprache, nachfristDokumente).render();
        notification.setNotificationText(msg);
        notificationRepository.persistAndFlush(notification);
    }

    @Transactional
    public void createGesuchEingereichtNotification(final Gesuch gesuch) {
        Notification notification = new Notification()
            .setNotificationType(NotificationType.GESUCH_EINGEREICHT)
            .setGesuch(gesuch);

        final var pia = gesuch.getNewestGesuchTranche()
            .orElseThrow(NotFoundException::new)
            .getGesuchFormular()
            .getPersonInAusbildung();
        final var sprache = pia.getKorrespondenzSprache();
        final var anrede = NotificationTemplateUtils.getAnredeText(pia.getAnrede(), sprache);
        final var nachname = pia.getNachname();

        String msg = Templates.getGesuchEingereichtText(anrede, nachname, sprache).render();
        notification.setNotificationText(msg);
        notificationRepository.persistAndFlush(notification);
    }

    @Transactional
    public void createAenderungAbgelehntNotification(final Gesuch gesuch, final KommentarDto kommentarDto) {
        Notification notification = new Notification()
            .setNotificationType(NotificationType.AENDERUNG_ABGELEHNT)
            .setGesuch(gesuch);
        final var pia = gesuch.getGesuchTranchen().get(0).getGesuchFormular().getPersonInAusbildung();
        final var sprache = pia.getKorrespondenzSprache();
        final var anrede = NotificationTemplateUtils.getAnredeText(pia.getAnrede(), sprache);
        final var nachname = pia.getNachname();

        final String msg = Templates.getAenderungAbgelehnt(anrede, nachname, kommentarDto.getText(), sprache).render();
        notification.setNotificationText(msg);
        notificationRepository.persistAndFlush(notification);
    }

    @Transactional
    public void createGesuchStatusChangeWithCommentNotification(final Gesuch gesuch, final KommentarDto kommentar) {
        Notification notification = new Notification()
            .setNotificationType(NotificationType.GESUCH_STATUS_CHANGE_WITH_COMMENT)
            .setGesuch(gesuch);

        final var pia = gesuch.getLatestGesuchTranche().getGesuchFormular().getPersonInAusbildung();
        final var sprache = pia.getKorrespondenzSprache();
        final var anrede = NotificationTemplateUtils.getAnredeText(pia.getAnrede(), sprache);
        final var nachname = pia.getNachname();
        String msg =
            Templates.getGesuchStatusChangeWithKommentarText(anrede, nachname, kommentar.getText(), sprache).render();
        notification.setNotificationText(msg);
        notificationRepository.persistAndFlush(notification);
    }

    public void createMissingDocumentNotification(final Gesuch gesuch) {
        Notification notification = new Notification()
            .setNotificationType(NotificationType.FEHLENDE_DOKUMENTE)
            .setGesuch(gesuch);
        final var pia = gesuch.getNewestGesuchTranche()
            .orElseThrow(NotFoundException::new)
            .getGesuchFormular()
            .getPersonInAusbildung();
        final var sprache = pia.getKorrespondenzSprache();
        final var numberOfDays =
            String.valueOf(DateUtil.getDaysBetween(LocalDate.now(), gesuch.getNachfristDokumente()));
        String msg = Templates.getGesuchFehlendeDokumenteText(
            sprache,
            numberOfDays,
            gesuch.getAusbildung()
                .getFall()
                .getSachbearbeiterZuordnung()
                .getSachbearbeiter()
                .getVorname(),
            gesuch.getAusbildung()
                .getFall()
                .getSachbearbeiterZuordnung()
                .getSachbearbeiter()
                .getNachname()
        ).render();
        notification.setNotificationText(msg);
        notificationRepository.persistAndFlush(notification);
    }

    public void createGesuchFehlendeDokumenteEinreichenNotification(final Gesuch gesuch) {
        final var pia = gesuch.getNewestGesuchTranche()
            .orElseThrow(NotFoundException::new)
            .getGesuchFormular()
            .getPersonInAusbildung();
        final var sprache = pia.getKorrespondenzSprache();
        final var anrede = NotificationTemplateUtils.getAnredeText(pia.getAnrede(), sprache);
        String msg = Templates.getFehlendeDokumenteEinreichenText(anrede, pia.getNachname(), sprache).render();

        Notification notification = new Notification()
            .setNotificationType(NotificationType.FEHLENDE_DOKUMENTE_EINREICHEN)
            .setGesuch(gesuch)
            .setNotificationText(msg);
        notificationRepository.persistAndFlush(notification);
    }

    public void createGesuchFehlendeDokumenteNichtEingereichtText(final Gesuch gesuch) {
        final var pia = gesuch.getNewestGesuchTranche()
            .orElseThrow(NotFoundException::new)
            .getGesuchFormular()
            .getPersonInAusbildung();
        final var sprache = pia.getKorrespondenzSprache();
        final var anrede = NotificationTemplateUtils.getAnredeText(pia.getAnrede(), sprache);
        final var nachname = pia.getNachname();
        final var sbVorname = gesuch.getAusbildung()
            .getFall()
            .getSachbearbeiterZuordnung()
            .getSachbearbeiter()
            .getVorname();
        final var sbNachname = gesuch.getAusbildung()
            .getFall()
            .getSachbearbeiterZuordnung()
            .getSachbearbeiter()
            .getNachname();
        String msg = Templates.getFehlendeDokumenteNichtEingereichtText(
            anrede,
            nachname,
            sbVorname,
            sbNachname,
            sprache
        ).render();

        Notification notification = new Notification()
            .setNotificationType(NotificationType.FEHLENDE_DOKUMENTE_NICHT_EINGEREICHT)
            .setGesuch(gesuch)
            .setNotificationText(msg);
        notificationRepository.persistAndFlush(notification);
    }

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance gesuchEingereichtDE(String anrede, String nachname);

        public static native TemplateInstance gesuchEingereichtFR(String anrede, String nachname);

        public static TemplateInstance getGesuchEingereichtText(
            final String anrede,
            final String nachname,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {

                return gesuchEingereichtFR(anrede, nachname);
            }
            return gesuchEingereichtDE(anrede, nachname);
        }

        public static native TemplateInstance gesuchStatusChangeWithKommentarDE(
            final String anrede,
            final String nachname,
            final String kommentar
        );

        public static native TemplateInstance gesuchStatusChangeWithKommentarFR(
            final String anrede,
            final String nachname,
            final String kommentar
        );

        public static TemplateInstance getGesuchStatusChangeWithKommentarText(
            final String anrede,
            final String nachname,
            final String kommentar,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return gesuchStatusChangeWithKommentarFR(anrede, nachname, kommentar);
            }
            return gesuchStatusChangeWithKommentarDE(anrede, nachname, kommentar);
        }

        public static native TemplateInstance gesuchFehlendeDokumenteDE(
            final String numberOfDays,
            final String sbVorname,
            final String sbNachname
        );

        public static native TemplateInstance gesuchFehlendeDokumenteFR(
            final String numberOfDays,
            final String sbVorname,
            final String sbNachname
        );

        public static TemplateInstance getGesuchFehlendeDokumenteText(
            final Sprache korrespondenzSprache,
            final String numberOfDays,
            final String sbVorname,
            final String sbNachname
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return gesuchFehlendeDokumenteFR(numberOfDays, sbVorname, sbNachname);
            }
            return gesuchFehlendeDokumenteDE(numberOfDays, sbVorname, sbNachname);
        }

        public static native TemplateInstance aenderungAbgelehntDE(
            final String anrede,
            final String nachname,
            final String kommentar
        );

        public static native TemplateInstance aenderungAbgelehntFR(
            final String anrede,
            final String nachname,
            final String kommentar
        );

        public static TemplateInstance getAenderungAbgelehnt(
            final String anrede,
            final String nachname,
            final String kommentar,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return aenderungAbgelehntFR(anrede, nachname, kommentar);
            }
            return aenderungAbgelehntDE(anrede, nachname, kommentar);
        }

        public static native TemplateInstance gesuchFehlendeDokumenteEinreichenDe(
            final String anrede,
            final String nachname
        );

        public static native TemplateInstance gesuchFehlendeDokumenteEinreichenFr(
            final String anrede,
            final String nachname
        );

        public static TemplateInstance getFehlendeDokumenteEinreichenText(
            final String anrede,
            final String nachname,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {

                return gesuchFehlendeDokumenteEinreichenFr(anrede, nachname);
            }
            return gesuchFehlendeDokumenteEinreichenDe(anrede, nachname);
        }

        public static native TemplateInstance gesuchFehlendeDokumenteNichtEingereichtDe(
            final String anrede,
            final String nachname,
            final String sbVorname,
            final String sbNachname
        );

        public static native TemplateInstance gesuchFehlendeDokumenteNichtEingereichtFr(
            final String anrede,
            final String nachname,
            final String sbVorname,
            final String sbNachname
        );

        public static TemplateInstance getFehlendeDokumenteNichtEingereichtText(
            final String anrede,
            final String nachname,
            final String sbVorname,
            final String sbNachname,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {

                return gesuchFehlendeDokumenteNichtEingereichtFr(anrede, nachname, sbVorname, sbNachname);
            }
            return gesuchFehlendeDokumenteNichtEingereichtDe(anrede, nachname, sbVorname, sbNachname);
        }

        public static native TemplateInstance nachfristDokumenteChangedDE(final String nachfristDokumente);

        public static native TemplateInstance nachfristDokumenteChangedFR(final String nachfristDokumente);

        public static TemplateInstance getNachfristDokumenteChangedText(
            final Sprache korrespondenzSprache,
            final String nachfristDokumente
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return nachfristDokumenteChangedFR(nachfristDokumente);
            }
            return nachfristDokumenteChangedDE(nachfristDokumente);
        }
    }
}
