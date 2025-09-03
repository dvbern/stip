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
import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.api.communication.mail.service.MailServiceUtils;
import ch.dvbern.stip.api.delegieren.entity.Delegierung;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.notification.entity.Notification;
import ch.dvbern.stip.api.notification.repo.NotificationRepository;
import ch.dvbern.stip.api.notification.type.NotificationType;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.api.verfuegung.entity.Verfuegung;
import ch.dvbern.stip.generated.dto.KommentarDto;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final GesuchRepository gesuchRepository;
    private final MailService mailService;

    @Transactional
    public void createDelegierungAbgelehntNotification(final Delegierung delegierung) {
        final var gesuch = gesuchRepository.findAllForFall(delegierung.getDelegierterFall().getId())
            .max(Comparator.comparing(Gesuch::getTimestampErstellt))
            .orElseThrow();
        final var sprache = delegierung.getPersoenlicheAngaben().getSprache();

        Notification notification = new Notification()
            .setNotificationType(NotificationType.DELEGIERUNG_ABGELEHNT)
            .setGesuch(gesuch);
        setAbsender(gesuch, notification);

        final String msg = Templates.getDelegierungAbgelehnt(sprache, delegierung.getSozialdienst().getName()).render();
        notification.setNotificationText(msg);
        notificationRepository.persistAndFlush(notification);

        MailServiceUtils.sendStandardNotificationEmailForGesuch(mailService, gesuch);
    }

    @Transactional
    public void createDelegierungAngenommenNotification(final Delegierung delegierung) {
        final var gesuch = gesuchRepository.findAllForFall(delegierung.getDelegierterFall().getId())
            .max(Comparator.comparing(Gesuch::getTimestampErstellt))
            .orElseThrow();
        final var sprache = gesuch.getNewestGesuchTranche()
            .orElseThrow(NotFoundException::new)
            .getGesuchFormular()
            .getPersonInAusbildung()
            .getKorrespondenzSprache();

        Notification notification = new Notification()
            .setNotificationType(NotificationType.DELEGIERUNG_ANGENOMMEN)
            .setGesuch(gesuch);
        setAbsender(gesuch, notification);

        final String msg =
            Templates.getDelegierungAngenommen(sprache, delegierung.getSozialdienst().getName()).render();
        notification.setNotificationText(msg);
        notificationRepository.persistAndFlush(notification);

        MailServiceUtils.sendStandardNotificationEmailForGesuch(mailService, gesuch);
    }

    @Transactional
    public void deleteNotificationsForGesuch(final UUID gesuchId) {
        notificationRepository.deleteAllForGesuch(gesuchId);
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
        setAbsender(gesuch, notification);
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
        setAbsender(gesuch, notification);

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
    public void createAenderungEingereichtNotification(final Gesuch gesuch) {
        Notification notification = new Notification()
            .setNotificationType(NotificationType.AENDERUNG_EINGEREICHT)
            .setGesuch(gesuch);
        setAbsender(gesuch, notification);

        final var pia = gesuch.getGesuchTranchen().get(0).getGesuchFormular().getPersonInAusbildung();
        final var sprache = pia.getKorrespondenzSprache();
        final var anrede = NotificationTemplateUtils.getAnredeText(pia.getAnrede(), sprache);
        final var nachname = pia.getNachname();

        final String msg = Templates.getAenderungEingereicht(anrede, nachname, sprache).render();
        notification.setNotificationText(msg);
        notificationRepository.persistAndFlush(notification);
    }

    @Transactional
    public void createAenderungAbgelehntNotification(
        final Gesuch gesuch,
        final GesuchTranche aenderung,
        final KommentarDto kommentarDto
    ) {
        Notification notification = new Notification()
            .setNotificationType(NotificationType.AENDERUNG_ABGELEHNT)
            .setGesuch(gesuch);
        setAbsender(gesuch, notification);

        final var pia = aenderung.getGesuchFormular().getPersonInAusbildung();
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
        setAbsender(gesuch, notification);

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
        setAbsender(gesuch, notification);

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
        setAbsender(gesuch, notification);

        notificationRepository.persistAndFlush(notification);
    }

    public void createNeueVerfuegungNotification(final Verfuegung verfuegung) {
        final var pia = verfuegung.getGesuch()
            .getNewestGesuchTranche()
            .orElseThrow(NotFoundException::new)
            .getGesuchFormular()
            .getPersonInAusbildung();
        final var sprache = pia.getKorrespondenzSprache();

        final var msg = Templates.getNeueVerfuegungText(sprache).render();
        Notification notification = new Notification()
            .setNotificationType(NotificationType.NEUE_VERFUEGUNG)
            .setGesuch(verfuegung.getGesuch())
            .setNotificationText(msg)
            .setContextId(verfuegung.getId());
        setAbsender(verfuegung.getGesuch(), notification);
        notificationRepository.persistAndFlush(notification);
    }

    public void createGesuchFehlendeDokumenteNichtEingereichtNotification(final Gesuch gesuch) {
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
        setAbsender(gesuch, notification);
        notificationRepository.persistAndFlush(notification);
    }

    public void createFailedAuszahlungBuchhaltungNotification(final Gesuch gesuch) {
        final var pia = gesuch.getNewestGesuchTranche()
            .orElseThrow(NotFoundException::new)
            .getGesuchFormular()
            .getPersonInAusbildung();
        final var sprache = pia.getKorrespondenzSprache();
        String message = Templates.getFailedAuszahlungBuchhaltungText(sprache).render();
        Notification notification = new Notification()
            .setNotificationType(NotificationType.FAILED_AUSZAHLUNG)
            .setGesuch(gesuch)
            .setNotificationText(message);
        setAbsender(gesuch, notification);
        notificationRepository.persistAndFlush(notification);
    }

    private void setAbsender(final Gesuch gesuch, Notification notification) {
        final var absender =
            gesuch.getAusbildung().getFall().getSachbearbeiterZuordnung().getSachbearbeiter().getFullName();
        notification.setAbsender(absender);
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

        public static native TemplateInstance aenderungEingereichtDE(
            final String anrede,
            final String nachname
        );

        public static native TemplateInstance aenderungEingereichtFR(
            final String anrede,
            final String nachname
        );

        public static TemplateInstance getAenderungEingereicht(
            final String anrede,
            final String nachname,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return aenderungEingereichtFR(anrede, nachname);
            }
            return aenderungEingereichtDE(anrede, nachname);
        }

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

        public static native TemplateInstance delegierungAbgelehntDE(final String sozialdienstName);

        public static native TemplateInstance delegierungAbgelehntFR(final String sozialdienstName);

        public static TemplateInstance getDelegierungAbgelehnt(
            final Sprache korrespondenzSprache,
            final String sozialdienstName
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return delegierungAbgelehntFR(sozialdienstName);
            }
            return delegierungAbgelehntDE(sozialdienstName);
        }

        public static native TemplateInstance delegierungAngenommenDE(final String sozialdienstName);

        public static native TemplateInstance delegierungAngenommenFR(final String sozialdienstName);

        public static TemplateInstance getDelegierungAngenommen(
            final Sprache korrespondenzSprache,
            final String sozialdienstName
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return delegierungAngenommenFR(sozialdienstName);
            }
            return delegierungAngenommenDE(sozialdienstName);
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

        public static native TemplateInstance neueVerfuegungDE();

        public static native TemplateInstance neueVerfuegungFR();

        public static TemplateInstance getNeueVerfuegungText(final Sprache korrespondenzSprache) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return neueVerfuegungFR();
            }
            return neueVerfuegungDE();
        }

        public static native TemplateInstance failedAuszahlungBuchhaltungDe();

        public static native TemplateInstance failedAuszahlungBuchhaltungFr();

        public static TemplateInstance getFailedAuszahlungBuchhaltungText(final Sprache korrespondenzSprache) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return failedAuszahlungBuchhaltungDe();
            }
            return failedAuszahlungBuchhaltungDe();
        }
    }
}
