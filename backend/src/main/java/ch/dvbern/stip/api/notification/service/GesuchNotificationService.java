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
import java.util.List;
import java.util.Optional;

import ch.dvbern.stip.api.common.entity.AbstractEntity;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
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
public class GesuchNotificationService {
    private final NotificationService notificationService;

    @Transactional
    public void createNeueVerfuegungNotificationAndSendStdMail(final Verfuegung verfuegung) {
        final var pia = verfuegung.getGesuch()
            .getNewestGesuchTranche()
            .orElseThrow(NotFoundException::new)
            .getGesuchFormular()
            .getPersonInAusbildung();
        final var validTypes =
            List.of(VerfuegungDokumentTyp.VERSENDETE_VERFUEGUNG, VerfuegungDokumentTyp.MANUELLE_NEGATIVE_VERFUEGUNG);
        final var mostRecentVerfuegungsDokument = verfuegung.getDokumente()
            .stream()
            .filter(d -> validTypes.contains(d.getTyp()))
            .max(Comparator.comparing(AbstractEntity::getTimestampErstellt));

        final var msg =
            Templates.getNeueVerfuegungText(pia.getVorname(), pia.getNachname(), pia.getKorrespondenzSprache())
                .render();

        notificationService.createNotificationAndSendStdMail(
            NotificationType.NEUE_VERFUEGUNG,
            verfuegung.getGesuch(),
            msg,
            Optional.empty(),
            Optional.of(mostRecentVerfuegungsDokument.get().getId())
        );
    }

    @Transactional
    public void createStatusChangeWithCommentNotificationAndSendStdMail(
        final Gesuch gesuch,
        final String kommentar
    ) {
        final var pia = gesuch.getLatestGesuchTranche().getGesuchFormular().getPersonInAusbildung();
        final var anrede = NotificationTemplateUtils.getAnredeText(pia.getAnrede(), pia.getKorrespondenzSprache());
        final var nachname = pia.getNachname();

        final String msg =
            Templates
                .getStatusChangeWithKommentarText(anrede, nachname, kommentar, pia.getKorrespondenzSprache())
                .render();

        notificationService
            .createNotificationAndSendStdMail(NotificationType.GESUCH_STATUS_CHANGE_WITH_COMMENT, gesuch, msg);
    }

    @Transactional
    public void createStatusChangeToEingereichtNotificationAndSendStdMail(final Gesuch gesuch) {
        final var pia = gesuch.getNewestGesuchTranche()
            .orElseThrow(NotFoundException::new)
            .getGesuchFormular()
            .getPersonInAusbildung();

        final String msg = Templates
            .getStatusChangeToEingereichtText(pia.getVorname(), pia.getNachname(), pia.getKorrespondenzSprache())
            .render();

        notificationService.createNotificationAndSendStdMail(NotificationType.GESUCH_EINGEREICHT, gesuch, msg);
    }

    @Transactional
    public void createStatusChangeToFehlendeDokumenteNotificationAndSendStdMail(final Gesuch gesuch) {
        final var pia = gesuch.getNewestGesuchTranche()
            .orElseThrow(NotFoundException::new)
            .getGesuchFormular()
            .getPersonInAusbildung();
        final var sachbearbeiter = gesuch.getAusbildung()
            .getFall()
            .getSachbearbeiterZuordnung()
            .getSachbearbeiter();

        final String msg = Templates.getStatusChangeToFehlendeDokumenteText(
            pia.getVorname(),
            pia.getNachname(),
            gesuch.getNachfristDokumente(),
            sachbearbeiter.getVorname(),
            sachbearbeiter.getNachname(),
            sachbearbeiter.getEmail(),
            pia.getKorrespondenzSprache()
        ).render();

        notificationService.createNotificationAndSendStdMail(NotificationType.FEHLENDE_DOKUMENTE, gesuch, msg);
    }

    @Transactional
    public void createFehlendeDokumenteEingereichtNotificationAndSendStdMail(final Gesuch gesuch) {
        final var pia = gesuch.getNewestGesuchTranche()
            .orElseThrow(NotFoundException::new)
            .getGesuchFormular()
            .getPersonInAusbildung();

        final String msg = Templates
            .getFehlendeDokumenteEingereichtText(pia.getVorname(), pia.getNachname(), pia.getKorrespondenzSprache())
            .render();

        notificationService
            .createNotificationAndSendStdMail(NotificationType.FEHLENDE_DOKUMENTE_EINGEREICHT, gesuch, msg);
    }

    @Transactional
    public void createFehlendeDokumenteNichtEingereichtNotificationAndSendStdMail(final Gesuch gesuch) {
        final var pia = gesuch.getNewestGesuchTranche()
            .orElseThrow(NotFoundException::new)
            .getGesuchFormular()
            .getPersonInAusbildung();

        final String msg = Templates.getFehlendeDokumenteNichtEingereichtText(
            pia.getVorname(),
            pia.getNachname(),
            pia.getKorrespondenzSprache()
        ).render();

        notificationService
            .createNotificationAndSendStdMail(NotificationType.FEHLENDE_DOKUMENTE_NICHT_EINGEREICHT, gesuch, msg);
    }

    @Transactional
    public void createNachfristDokumenteChangedNotificationAndSendStdMail(final Gesuch gesuch) {
        final var pia = gesuch.getNewestGesuchTranche()
            .orElseThrow(NotFoundException::new)
            .getGesuchFormular()
            .getPersonInAusbildung();

        final String msg =
            Templates
                .getNachfristDokumenteChangedText(
                    pia.getVorname(),
                    pia.getNachname(),
                    gesuch.getNachfristDokumente(),
                    pia.getKorrespondenzSprache()
                )
                .render();

        notificationService.createNotificationAndSendStdMail(NotificationType.NACHFRIST_DOKUMENTE_CHANGED, gesuch, msg);
    }

    @Transactional
    public void createAuszahlungFailedNotificationAndSendStdMail(final Gesuch gesuch) {
        final var pia = gesuch.getNewestGesuchTranche()
            .orElseThrow(NotFoundException::new)
            .getGesuchFormular()
            .getPersonInAusbildung();

        final String msg =
            Templates.getAuszahlungFailedText(pia.getVorname(), pia.getNachname(), pia.getKorrespondenzSprache())
                .render();

        notificationService.createNotificationAndSendStdMail(NotificationType.FAILED_AUSZAHLUNG, gesuch, msg);
    }

    @CheckedTemplate
    private static class Templates {
        public static TemplateInstance getStatusChangeToEingereichtText(
            final String vorname,
            final String nachname,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return statusChangeToEingereichtFR(vorname, nachname);
            }
            return statusChangeToEingereichtDE(vorname, nachname);
        }

        public static native TemplateInstance statusChangeToEingereichtDE(final String vorname, final String nachname);

        public static native TemplateInstance statusChangeToEingereichtFR(final String vorname, final String nachname);

        public static TemplateInstance getStatusChangeWithKommentarText(
            final String anrede,
            final String nachname,
            final String kommentar,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return statusChangeWithKommentarFR(anrede, nachname, kommentar);
            }
            return statusChangeWithKommentarDE(anrede, nachname, kommentar);
        }

        public static native TemplateInstance statusChangeWithKommentarDE(
            final String anrede,
            final String nachname,
            final String kommentar
        );

        public static native TemplateInstance statusChangeWithKommentarFR(
            final String anrede,
            final String nachname,
            final String kommentar
        );

        public static TemplateInstance getStatusChangeToFehlendeDokumenteText(
            final String vorname,
            final String nachname,
            final LocalDate nachreichefrist,
            final String sbVorname,
            final String sbNachname,
            final String sbEMail,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return statusChangeToFehlendeDokumenteFR(
                    vorname,
                    nachname,
                    DateUtil.formatDate(nachreichefrist),
                    sbVorname,
                    sbNachname,
                    sbEMail
                );
            }
            return statusChangeToFehlendeDokumenteDE(
                vorname,
                nachname,
                DateUtil.formatDate(nachreichefrist),
                sbVorname,
                sbNachname,
                sbEMail
            );
        }

        public static native TemplateInstance statusChangeToFehlendeDokumenteDE(
            final String vorname,
            final String nachname,
            final String nachreichefrist,
            final String sbVorname,
            final String sbNachname,
            final String sbEmail
        );

        public static native TemplateInstance statusChangeToFehlendeDokumenteFR(
            final String vorname,
            final String nachname,
            final String nachreichefrist,
            final String sbVorname,
            final String sbNachname,
            final String sbEmail
        );

        public static TemplateInstance getFehlendeDokumenteEingereichtText(
            final String vorname,
            final String nachname,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {

                return fehlendeDokumenteEingereichtFr(vorname, nachname);
            }
            return fehlendeDokumenteEingereichtDe(vorname, nachname);
        }

        public static native TemplateInstance fehlendeDokumenteEingereichtDe(
            final String vorname,
            final String nachname
        );

        public static native TemplateInstance fehlendeDokumenteEingereichtFr(
            final String vorname,
            final String nachname
        );

        public static TemplateInstance getFehlendeDokumenteNichtEingereichtText(
            final String vorname,
            final String nachname,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {

                return fehlendeDokumenteNichtEingereichtFr(vorname, nachname);
            }
            return fehlendeDokumenteNichtEingereichtDe(vorname, nachname);
        }

        public static native TemplateInstance fehlendeDokumenteNichtEingereichtDe(
            final String vorname,
            final String nachname
        );

        public static native TemplateInstance fehlendeDokumenteNichtEingereichtFr(
            final String vorname,
            final String nachname
        );

        public static TemplateInstance getNachfristDokumenteChangedText(
            final String vorname,
            final String nachname,
            final LocalDate fristende,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return nachfristDokumenteChangedFR(vorname, nachname, DateUtil.formatDate(fristende));
            }
            return nachfristDokumenteChangedDE(vorname, nachname, DateUtil.formatDate(fristende));
        }

        public static native TemplateInstance nachfristDokumenteChangedDE(
            final String vorname,
            final String nachname,
            final String fristende
        );

        public static native TemplateInstance nachfristDokumenteChangedFR(
            final String vorname,
            final String nachname,
            final String fristende
        );

        public static TemplateInstance getNeueVerfuegungText(
            final String vorname,
            final String nachname,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return neueVerfuegungFR(vorname, nachname);
            }
            return neueVerfuegungDE(vorname, nachname);
        }

        public static native TemplateInstance neueVerfuegungDE(final String vorname, final String nachname);

        public static native TemplateInstance neueVerfuegungFR(final String vorname, final String nachname);

        public static TemplateInstance getAuszahlungFailedText(
            final String vorname,
            final String nachname,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return auszahlungFailedFR(vorname, nachname);
            }
            return auszahlungFailedDE(vorname, nachname);
        }

        public static native TemplateInstance auszahlungFailedDE(final String vorname, final String nachname);

        public static native TemplateInstance auszahlungFailedFR(final String vorname, final String nachname);
    }
}
