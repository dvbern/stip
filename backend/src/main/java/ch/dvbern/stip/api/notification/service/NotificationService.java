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
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.darlehen.entity.FreiwilligDarlehen;
import ch.dvbern.stip.api.delegieren.entity.Delegierung;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.notification.entity.Notification;
import ch.dvbern.stip.api.notification.repo.NotificationRepository;
import ch.dvbern.stip.api.notification.type.NotificationType;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.api.verfuegung.entity.Verfuegung;
import ch.dvbern.stip.api.verfuegung.type.VerfuegungDokumentTyp;
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

    @Transactional
    public void createDarlehenAbgelehntNotification(final FreiwilligDarlehen freiwilligDarlehen) {
        createDarlehenNotification(NotificationType.DARLEHEN_ABGELEHNT, freiwilligDarlehen, Optional.empty());
    }

    @Transactional
    public void createDarlehenAkzeptiertNotification(final FreiwilligDarlehen freiwilligDarlehen) {
        createDarlehenNotification(NotificationType.DARLEHEN_AKZEPTIERT, freiwilligDarlehen, Optional.empty());
    }

    @Transactional
    public void createDarlehenEingegebenNotification(final FreiwilligDarlehen freiwilligDarlehen) {
        createDarlehenNotification(NotificationType.DARLEHEN_EINGEGEBEN, freiwilligDarlehen, Optional.empty());
    }

    @Transactional
    public void createDarlehenZurueckgewiesenNotification(
        final FreiwilligDarlehen freiwilligDarlehen,
        String kommentar
    ) {
        createDarlehenNotification(
            NotificationType.DARLEHEN_ZURUECKGEWIESEN,
            freiwilligDarlehen,
            Optional.of(kommentar)
        );
    }

    private void createDarlehenNotification(
        final NotificationType notificationType,
        final FreiwilligDarlehen freiwilligDarlehen,
        Optional<String> kommentar
    ) {
        final var fall = freiwilligDarlehen.getFall();
        final var absender =
            freiwilligDarlehen.getFall().getSachbearbeiterZuordnung().getSachbearbeiter().getFullName();
        final var pia =
            freiwilligDarlehen.getFall()
                .getLatestGesuch()
                .getLatestGesuchTranche()
                .getGesuchFormular()
                .getPersonInAusbildung();
        final var anrede = NotificationTemplateUtils.getAnredeText(pia.getAnrede(), pia.getKorrespondenzSprache());

        final Notification notification = new Notification()
            .setNotificationType(notificationType)
            .setFall(fall);
        setAbsender(absender, notification);

        final String msg = switch (notificationType) {
            case DARLEHEN_ABGELEHNT -> Templates
                .getDarlehenAbgelehnt(
                    anrede,
                    pia.getNachname(),
                    freiwilligDarlehen.getKommentar(),
                    pia.getKorrespondenzSprache()
                )
                .render();
            case DARLEHEN_AKZEPTIERT -> Templates
                .getDarlehenAkzeptiert(
                    anrede,
                    pia.getNachname(),
                    freiwilligDarlehen.getKommentar(),
                    pia.getKorrespondenzSprache()
                )
                .render();
            case DARLEHEN_EINGEGEBEN -> Templates
                .getDarlehenEingegeben(anrede, pia.getNachname(), pia.getKorrespondenzSprache())
                .render();
            case DARLEHEN_ZURUECKGEWIESEN -> Templates
                .getDarlehenZurueckgewiesen(anrede, pia.getNachname(), kommentar.get(), pia.getKorrespondenzSprache())
                .render();
            default -> throw new IllegalStateException("Unexpected value: " + notificationType);
        };

        notification.setNotificationText(msg);
        notificationRepository.persistAndFlush(notification);
    }

    @Transactional
    public void createDelegierungAufgeloestNotification(final Delegierung delegierung) {
        createDelegierungNotification(NotificationType.DELEGIERUNG_AUFGELOEST, delegierung);
    }

    @Transactional
    public void createDelegierungAbgelehntNotification(final Delegierung delegierung) {
        createDelegierungNotification(NotificationType.DELEGIERUNG_ABGELEHNT, delegierung);
    }

    @Transactional
    public void createDelegierungAngenommenNotification(final Delegierung delegierung) {
        createDelegierungNotification(NotificationType.DELEGIERUNG_ANGENOMMEN, delegierung);
    }

    private void createDelegierungNotification(final NotificationType notificationType, final Delegierung delegierung) {
        final var fall = delegierung.getDelegierterFall();
        final var absender = delegierung.getSozialdienst().getSozialdienstAdmin().getFullName();
        final var persoenlicheAngaben = delegierung.getPersoenlicheAngaben();

        Notification notification = new Notification()
            .setNotificationType(notificationType)
            .setFall(fall);
        setAbsender(absender, notification);

        final String msg = switch (notificationType) {
            case DELEGIERUNG_ANGENOMMEN -> Templates
                .getDelegierungAngenommen(persoenlicheAngaben.getSprache(), delegierung.getSozialdienst().getName())
                .render();
            case DELEGIERUNG_ABGELEHNT -> Templates
                .getDelegierungAbgelehnt(persoenlicheAngaben.getSprache(), delegierung.getSozialdienst().getName())
                .render();
            case DELEGIERUNG_AUFGELOEST -> Templates
                .getDelegierungAufgeloest(persoenlicheAngaben.getSprache(), delegierung.getSozialdienst().getName())
                .render();
            default -> throw new IllegalStateException("Unexpected value: " + notificationType);
        };
        notification.setNotificationText(msg);
        notificationRepository.persistAndFlush(notification);
    }

    @Transactional
    public void deleteNotificationsForFall(final UUID fallId) {
        notificationRepository.deleteAllForFall(fallId);
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
            .setFall(gesuch.getAusbildung().getFall());
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
            .setFall(gesuch.getAusbildung().getFall());
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
            .setFall(gesuch.getAusbildung().getFall());
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
            .setFall(gesuch.getAusbildung().getFall());
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
            .setFall(gesuch.getAusbildung().getFall());
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
            .setFall(gesuch.getAusbildung().getFall());
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
            .setFall(gesuch.getAusbildung().getFall())
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
        final var mostRecentVerfuegungsDokument = verfuegung.getDokumente()
            .stream()
            .filter(
                d -> d.getTyp() == VerfuegungDokumentTyp.VERSENDETE_VERFUEGUNG
            )
            .max(Comparator.comparing(AbstractEntity::getTimestampErstellt));
        final var sprache = pia.getKorrespondenzSprache();

        final var msg = Templates.getNeueVerfuegungText(sprache).render();
        Notification notification = new Notification()
            .setNotificationType(NotificationType.NEUE_VERFUEGUNG)
            .setFall(verfuegung.getGesuch().getAusbildung().getFall())
            .setNotificationText(msg)
            .setContextId(mostRecentVerfuegungsDokument.get().getId());
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
            .setFall(gesuch.getAusbildung().getFall())
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
            .setFall(gesuch.getAusbildung().getFall())
            .setNotificationText(message);
        setAbsender(gesuch, notification);
        notificationRepository.persistAndFlush(notification);
    }

    private void setAbsender(final Gesuch gesuch, Notification notification) {
        final var absender =
            gesuch.getAusbildung().getFall().getSachbearbeiterZuordnung().getSachbearbeiter().getFullName();
        setAbsender(absender, notification);
    }

    private void setAbsender(String absender, Notification notification) {
        notification.setAbsender(absender);
    }

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance darlehenAbgelehntDE(String anrede, String nachname, String kommentar);

        public static native TemplateInstance darlehenAbgelehntFR(String anrede, String nachname, String kommentar);

        public static TemplateInstance getDarlehenAbgelehnt(
            final String anrede,
            final String nachname,
            final String kommentar,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return darlehenAbgelehntFR(anrede, nachname, kommentar);
            }
            return darlehenAbgelehntDE(anrede, nachname, kommentar);
        }

        public static native TemplateInstance darlehenAkzeptiertDE(String anrede, String nachname, String kommentar);

        public static native TemplateInstance darlehenAkzeptiertFR(String anrede, String nachname, String kommentar);

        public static TemplateInstance getDarlehenAkzeptiert(
            final String anrede,
            final String nachname,
            final String kommentar,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return darlehenAkzeptiertFR(anrede, nachname, kommentar);
            }
            return darlehenAkzeptiertDE(anrede, nachname, kommentar);
        }

        public static native TemplateInstance darlehenZurueckgewiesenDE(
            String anrede,
            String nachname,
            String kommentar
        );

        public static native TemplateInstance darlehenZurueckgewiesenFR(
            String anrede,
            String nachname,
            String kommentar
        );

        public static TemplateInstance getDarlehenZurueckgewiesen(
            final String anrede,
            final String nachname,
            final String kommentar,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return darlehenZurueckgewiesenFR(anrede, nachname, kommentar);
            }
            return darlehenZurueckgewiesenDE(anrede, nachname, kommentar);
        }

        public static native TemplateInstance darlehenEingegebenDE(String anrede, String nachname);

        public static native TemplateInstance darlehenEingegebenFR(String anrede, String nachname);

        public static TemplateInstance getDarlehenEingegeben(
            final String anrede,
            final String nachname,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return darlehenEingegebenFR(anrede, nachname);
            }
            return darlehenEingegebenDE(anrede, nachname);
        }

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

        public static native TemplateInstance delegierungAufgeloestDE(final String sozialdienstName);

        public static native TemplateInstance delegierungAufgeloestFR(final String sozialdienstName);

        public static TemplateInstance getDelegierungAufgeloest(
            final Sprache korrespondenzSprache,
            final String sozialdienstName
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return delegierungAufgeloestFR(sozialdienstName);
            }
            return delegierungAufgeloestDE(sozialdienstName);
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
