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

import ch.dvbern.stip.api.ausbildung.entity.AusbildungUnterbruchAntrag;
import ch.dvbern.stip.api.common.util.DateUtil;
import ch.dvbern.stip.api.notification.type.NotificationType;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class AusbildungNotificationService {
    private final NotificationService notificationService;

    @Transactional
    public void createUnterbruchAntragEingereichtNotificationAndSendStdMail(
        final AusbildungUnterbruchAntrag antrag
    ) {
        final var gesuch = antrag.getGesuch();
        final var pia = gesuch.getNewestGesuchTranche()
            .orElseThrow(NotFoundException::new)
            .getGesuchFormular()
            .getPersonInAusbildung();

        final String msg = Templates.getUnterbruchAntragEingereichtText(
            antrag.getGueltigkeit().getGueltigAb(),
            antrag.getGueltigkeit().getGueltigBis(),
            pia.getKorrespondenzSprache()
        ).render();

        notificationService
            .createNotificationAndSendStdMail(NotificationType.AUSBILDUNG_UNTERBRUCH_ANTRAG_EINGEREICHT, gesuch, msg);
    }

    @Transactional
    public void createUnterbruchAntragAkzeptiertAbgelehntNotificationAndSendStdMail(
        final AusbildungUnterbruchAntrag antrag
    ) {
        final var gesuch = antrag.getGesuch();
        final var pia = gesuch.getNewestGesuchTranche()
            .orElseThrow(NotFoundException::new)
            .getGesuchFormular()
            .getPersonInAusbildung();

        final String msg = switch (antrag.getStatus()) {
            case AKZEPTIERT -> Templates.getUnterbruchAntragAkzeptiertText(
                antrag.getKommentarSB(),
                antrag.getGueltigkeit().getGueltigAb(),
                antrag.getGueltigkeit().getGueltigBis(),
                pia.getKorrespondenzSprache()
            ).render();
            case ABGELEHNT -> Templates.getUnterbruchAntragAbgelehntText(
                antrag.getKommentarSB(),
                antrag.getGueltigkeit().getGueltigAb(),
                antrag.getGueltigkeit().getGueltigBis(),
                pia.getKorrespondenzSprache()
            ).render();
            case null, default -> throw new IllegalStateException();
        };

        final NotificationType notificationType = switch (antrag.getStatus()) {
            case AKZEPTIERT -> NotificationType.AUSBILDUNG_UNTERBRUCH_ANTRAG_AKZEPTIERT;
            case ABGELEHNT -> NotificationType.AUSBILDUNG_UNTERBRUCH_ANTRAG_ABGELEHNT;
            case null, default -> throw new IllegalStateException();
        };

        notificationService.createNotificationAndSendStdMail(notificationType, gesuch, msg);
    }

    @CheckedTemplate
    private static class Templates {
        public static TemplateInstance getUnterbruchAntragAbgelehntText(
            final String kommentar,
            final LocalDate startDate,
            final LocalDate endDate,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return unterbruchAntragAbgelehntFR(
                    kommentar,
                    DateUtil.formatDate(startDate),
                    DateUtil.formatDate(endDate)
                );
            }
            return unterbruchAntragAbgelehntDE(kommentar, DateUtil.formatDate(startDate), DateUtil.formatDate(endDate));
        }

        public static native TemplateInstance unterbruchAntragAbgelehntDE(
            final String kommentar,
            final String startDate,
            final String endDate
        );

        public static native TemplateInstance unterbruchAntragAbgelehntFR(
            final String kommentar,
            final String startDate,
            final String endDate
        );

        public static TemplateInstance getUnterbruchAntragAkzeptiertText(
            final String kommentar,
            final LocalDate startDate,
            final LocalDate endDate,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return unterbruchAntragAkzeptiertFR(
                    kommentar,
                    DateUtil.formatDate(startDate),
                    DateUtil.formatDate(endDate)
                );
            }
            return unterbruchAntragAkzeptiertDE(
                kommentar,
                DateUtil.formatDate(startDate),
                DateUtil.formatDate(endDate)
            );
        }

        public static native TemplateInstance unterbruchAntragAkzeptiertDE(
            final String kommentar,
            final String startDate,
            final String endDate
        );

        public static native TemplateInstance unterbruchAntragAkzeptiertFR(
            final String kommentar,
            final String startDate,
            final String endDate
        );

        public static TemplateInstance getUnterbruchAntragEingereichtText(
            final LocalDate startDate,
            final LocalDate endDate,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return unterbruchAntragEingereichtFR(DateUtil.formatDate(startDate), DateUtil.formatDate(endDate));
            }
            return unterbruchAntragEingereichtDE(DateUtil.formatDate(startDate), DateUtil.formatDate(endDate));
        }

        public static native TemplateInstance unterbruchAntragEingereichtDE(
            final String startDate,
            final String endDate
        );

        public static native TemplateInstance unterbruchAntragEingereichtFR(
            final String startDate,
            final String endDate
        );
    }
}
