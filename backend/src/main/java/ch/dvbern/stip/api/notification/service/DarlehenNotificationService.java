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

import java.util.Optional;

import ch.dvbern.stip.api.darlehen.entity.FreiwilligDarlehen;
import ch.dvbern.stip.api.notification.type.NotificationType;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateInstance;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class DarlehenNotificationService {
    private final NotificationService notificationService;

    @Transactional
    public void createAbgelehntNotificationAndSendStdMail(final FreiwilligDarlehen freiwilligDarlehen) {
        final var absender =
            freiwilligDarlehen.getFall().getSachbearbeiterZuordnung().getSachbearbeiter().getFullName();
        final var pia =
            freiwilligDarlehen.getFall()
                .getLatestGesuch()
                .getLatestGesuchTranche()
                .getGesuchFormular()
                .getPersonInAusbildung();

        final String msg = Templates
            .getAbgelehnt(
                pia.getVorname(),
                pia.getNachname(),
                pia.getKorrespondenzSprache()
            )
            .render();

        notificationService.createNotificationAndSendStdMail(
            NotificationType.DARLEHEN_ABGELEHNT,
            freiwilligDarlehen.getRelatedGesuch(),
            msg,
            Optional.of(absender),
            Optional.empty()
        );
    }

    @Transactional
    public void createAkzeptiertNotificationAndSendStdMail(final FreiwilligDarlehen freiwilligDarlehen) {
        final var absender =
            freiwilligDarlehen.getFall().getSachbearbeiterZuordnung().getSachbearbeiter().getFullName();
        final var pia =
            freiwilligDarlehen.getFall()
                .getLatestGesuch()
                .getLatestGesuchTranche()
                .getGesuchFormular()
                .getPersonInAusbildung();

        final String msg = Templates
            .getAkzeptiert(
                pia.getVorname(),
                pia.getNachname(),
                pia.getKorrespondenzSprache()
            )
            .render();

        notificationService.createNotificationAndSendStdMail(
            NotificationType.DARLEHEN_AKZEPTIERT,
            freiwilligDarlehen.getRelatedGesuch(),
            msg,
            Optional.of(absender),
            Optional.empty()
        );
    }

    @Transactional
    public void createEingegebenNotificationAndSendStdMail(final FreiwilligDarlehen freiwilligDarlehen) {
        final var absender =
            freiwilligDarlehen.getFall().getSachbearbeiterZuordnung().getSachbearbeiter().getFullName();
        final var pia =
            freiwilligDarlehen.getFall()
                .getLatestGesuch()
                .getLatestGesuchTranche()
                .getGesuchFormular()
                .getPersonInAusbildung();

        final String msg = Templates
            .getEingegeben(pia.getVorname(), pia.getNachname(), pia.getKorrespondenzSprache())
            .render();

        notificationService.createNotificationAndSendStdMail(
            NotificationType.DARLEHEN_EINGEGEBEN,
            freiwilligDarlehen.getRelatedGesuch(),
            msg,
            Optional.of(absender),
            Optional.empty()
        );
    }

    @Transactional
    public void createZurueckgewiesenNotificationAndSendStdMail(
        final FreiwilligDarlehen freiwilligDarlehen,
        String kommentar
    ) {

        final var absender =
            freiwilligDarlehen.getFall().getSachbearbeiterZuordnung().getSachbearbeiter().getFullName();
        final var pia =
            freiwilligDarlehen.getFall()
                .getLatestGesuch()
                .getLatestGesuchTranche()
                .getGesuchFormular()
                .getPersonInAusbildung();

        final String msg = Templates
            .getZurueckgewiesen(pia.getVorname(), pia.getNachname(), kommentar, pia.getKorrespondenzSprache())
            .render();

        notificationService.createNotificationAndSendStdMail(
            NotificationType.DARLEHEN_ZURUECKGEWIESEN,
            freiwilligDarlehen.getRelatedGesuch(),
            msg,
            Optional.of(absender),
            Optional.empty()
        );
    }

    @CheckedTemplate
    private static class Templates {
        public static TemplateInstance getAbgelehnt(
            final String vorname,
            final String nachname,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return abgelehntFR(vorname, nachname);
            }
            return abgelehntDE(vorname, nachname);
        }

        public static native TemplateInstance abgelehntDE(final String vorname, final String nachname);

        public static native TemplateInstance abgelehntFR(final String vorname, final String nachname);

        public static TemplateInstance getAkzeptiert(
            final String vorname,
            final String nachname,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return akzeptiertFR(vorname, nachname);
            }
            return akzeptiertDE(vorname, nachname);
        }

        public static native TemplateInstance akzeptiertDE(final String vorname, final String nachname);

        public static native TemplateInstance akzeptiertFR(final String vorname, final String nachname);

        public static TemplateInstance getZurueckgewiesen(
            final String vorname,
            final String nachname,
            final String kommentar,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return zurueckgewiesenFR(vorname, nachname, kommentar);
            }
            return zurueckgewiesenDE(vorname, nachname, kommentar);
        }

        public static native TemplateInstance zurueckgewiesenDE(
            final String vorname,
            final String nachname,
            final String kommentar
        );

        public static native TemplateInstance zurueckgewiesenFR(
            final String vorname,
            final String nachname,
            final String kommentar
        );

        public static TemplateInstance getEingegeben(
            final String vorname,
            final String nachname,
            final Sprache korrespondenzSprache
        ) {
            if (korrespondenzSprache.equals(Sprache.FRANZOESISCH)) {
                return eingegebenFR(vorname, nachname);
            }
            return eingegebenDE(vorname, nachname);
        }

        public static native TemplateInstance eingegebenDE(final String vorname, final String nachname);

        public static native TemplateInstance eingegebenFR(final String vorname, final String nachname);
    }
}
