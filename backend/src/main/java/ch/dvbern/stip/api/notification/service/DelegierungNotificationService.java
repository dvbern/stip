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

import ch.dvbern.stip.api.delegieren.entity.Delegierung;
import ch.dvbern.stip.api.notification.type.NotificationType;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class DelegierungNotificationService {
    private final NotificationService notificationService;

    @Transactional
    public void createAbgelehntNotificationAndSendStdMail(final Delegierung delegierung) {
        final var fall = delegierung.getFall();
        final var absender = delegierung.getSozialdienst().getSozialdienstAdmin().getFullName();
        final var persoenlicheAngaben = delegierung.getPersoenlicheAngaben();

        final String msg = Templates
            .getAbgelehnt(
                persoenlicheAngaben.getVorname(),
                persoenlicheAngaben.getNachname(),
                delegierung.getSozialdienst().getName(),
                persoenlicheAngaben.getSprache()
            )
            .render();

        notificationService
            .createNotificationAndSendStdMail(
                NotificationType.DELEGIERUNG_ABGELEHNT,
                fall,
                absender,
                persoenlicheAngaben,
                msg
            );
    }

    @Transactional
    public void createAngenommenNotificationAndSendStdMail(final Delegierung delegierung) {
        final var fall = delegierung.getFall();
        final var absender = delegierung.getSozialdienst().getSozialdienstAdmin().getFullName();
        final var persoenlicheAngaben = delegierung.getPersoenlicheAngaben();

        final String msg = Templates
            .getAngenommen(
                persoenlicheAngaben.getVorname(),
                persoenlicheAngaben.getNachname(),
                delegierung.getSozialdienst().getName(),
                persoenlicheAngaben.getSprache()
            )
            .render();

        notificationService
            .createNotificationAndSendStdMail(
                NotificationType.DELEGIERUNG_ANGENOMMEN,
                fall,
                absender,
                persoenlicheAngaben,
                msg
            );
    }

    @Transactional
    public void createAufgeloestNotificationAndSendStdMail(final Delegierung delegierung) {
        final var fall = delegierung.getFall();
        final var absender = delegierung.getSozialdienst().getSozialdienstAdmin().getFullName();
        final var persoenlicheAngaben = delegierung.getPersoenlicheAngaben();

        final String msg = Templates
            .getAufgeloest(
                persoenlicheAngaben.getVorname(),
                persoenlicheAngaben.getNachname(),
                delegierung.getSozialdienst().getName(),
                persoenlicheAngaben.getSprache()
            )
            .render();

        notificationService
            .createNotificationAndSendStdMail(
                NotificationType.DELEGIERUNG_AUFGELOEST,
                fall,
                absender,
                persoenlicheAngaben,
                msg
            );
    }

    @CheckedTemplate
    private static class Templates {
        public static TemplateInstance getAbgelehnt(
            final String vorname,
            final String nachname,
            final String sozialdienst,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return abgelehntFR(vorname, nachname, sozialdienst);
            }
            return abgelehntDE(vorname, nachname, sozialdienst);
        }

        public static native TemplateInstance abgelehntDE(
            final String vorname,
            final String nachname,
            final String sozialdienst
        );

        public static native TemplateInstance abgelehntFR(
            final String vorname,
            final String nachname,
            final String sozialdienst
        );

        public static TemplateInstance getAngenommen(
            final String vorname,
            final String nachname,
            final String sozialdienst,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return angenommenFR(vorname, nachname, sozialdienst);
            }
            return angenommenDE(vorname, nachname, sozialdienst);
        }

        public static native TemplateInstance angenommenDE(
            final String vorname,
            final String nachname,
            final String sozialdienst
        );

        public static native TemplateInstance angenommenFR(
            final String vorname,
            final String nachname,
            final String sozialdienst
        );

        public static TemplateInstance getAufgeloest(
            final String vorname,
            final String nachname,
            final String sozialdienst,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return aufgeloestFR(vorname, nachname, sozialdienst);
            }
            return aufgeloestDE(vorname, nachname, sozialdienst);
        }

        public static native TemplateInstance aufgeloestDE(
            final String vorname,
            final String nachname,
            final String sozialdienst
        );

        public static native TemplateInstance aufgeloestFR(
            final String vorname,
            final String nachname,
            final String sozialdienst
        );
    }
}
