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

package ch.dvbern.stip.api.communication.mail.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.common.i18n.translations.AppLanguages;
import ch.dvbern.stip.api.common.i18n.translations.TLProducer;
import ch.dvbern.stip.api.common.util.FileUtil;
import ch.dvbern.stip.api.delegieren.entity.PersoenlicheAngaben;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.notification.service.MailAlreadySentCheckerService;
import ch.dvbern.stip.api.tenancy.service.TenantConfigService;
import ch.dvbern.stip.generated.dto.WelcomeMailDto;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.MailTemplate.MailTemplateInstance;
import io.quarkus.mailer.Mailer;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.quarkus.qute.CheckedTemplate;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class MailService {
    private final Mailer mailer;
    private final ReactiveMailer reactiveMailer;
    private final TenantConfigService tenantConfigService;
    private final MailAlreadySentCheckerService mailAlreadySentCheckerService;

    public void sendStandardNotificationEmailForGesuch(final Gesuch gesuch) {
        final var pia = gesuch.getLatestGesuchTranche().getGesuchFormular().getPersonInAusbildung();
        sendStandardNotificationEmails(
            pia.getNachname(),
            pia.getVorname(),
            AppLanguages.fromLocale(pia.getKorrespondenzSprache().getLocale()),
            gatherRecipients(pia.getEmail(), gesuch.getAusbildung().getFall())
        );
    }

    public void sendStandardNotificationEmailForFall(
        final PersoenlicheAngaben persoenlicheAngaben,
        final Fall fall
    ) {
        sendStandardNotificationEmails(
            persoenlicheAngaben.getNachname(),
            persoenlicheAngaben.getVorname(),
            AppLanguages.fromLocale(persoenlicheAngaben.getSprache().getLocale()),
            gatherRecipients(persoenlicheAngaben.getEmail(), fall)
        );
    }

    private List<String> gatherRecipients(final String primaryEmail, final Fall fall) {
        final var result = new ArrayList<String>();

        final var delegierung = fall.getDelegierung();
        if (delegierung != null && delegierung.getDelegierterMitarbeiter() != null) {
            result.add(delegierung.getDelegierterMitarbeiter().getEmail());
        }

        result.add(primaryEmail);

        return result;
    }

    void sendStandardNotificationEmails(
        final String nachname,
        final String vorname,
        final AppLanguages language,
        final List<String> recipients
    ) {
        if (recipients.isEmpty()) {
            throw new IllegalArgumentException("recipients cannot be empty");
        }

        if (mailAlreadySentCheckerService.isStandardNotificationSent()) {
            LOG.info("Trying to send standard notification mail, although one has already been sent this request");
            return;
        }

        mailAlreadySentCheckerService.sentStandardNotification();

        Templates.getStandardNotification(nachname, vorname, language)
            .to(recipients.toArray(String[]::new))
            .subject(TLProducer.defaultBundle().forAppLanguage(language).translate("stip.standard.notification"))
            .send()
            .onFailure()
            .invoke(this::handleFailure)
            .subscribe()
            .asCompletionStage();
    }

    public void sendStandardNotificationEmail(
        String name,
        String vorname,
        String receiver,
        AppLanguages language
    ) {
        sendStandardNotificationEmails(name, vorname, language, List.of(receiver));
    }

    public void sendBenutzerWelcomeEmail(WelcomeMailDto welcomeMailDto) {
        String redirectURI = tenantConfigService.getWelcomeMailURI(
            welcomeMailDto.getRedirectUri()
        );

        Templates.benutzerWelcome(welcomeMailDto.getName(), welcomeMailDto.getVorname(), redirectURI)
            .to(welcomeMailDto.getEmail())
            .subject("Benuzter Erstellt/ Utilisateur Créé")
            .send()
            .onFailure()
            .invoke(this::handleFailure)
            .subscribe()
            .asCompletionStage();
    }

    Uni<Void> sendEmail(String to, String subject, String htmlContent) {
        return reactiveMailer.send(
            Mail.withHtml(
                to,
                subject,
                htmlContent
            )
        );
    }

    void sendEmailSync(String to, String subject, String htmlContent) {
        mailer.send(
            Mail.withHtml(
                to,
                subject,
                htmlContent
            )
        );
    }

    public Uni<Void> sendEmailWithAttachment(String to, String subject, String htmlContent, List<File> attachments) {
        Mail mail = Mail.withHtml(
            to,
            subject,
            htmlContent
        );
        attachments.forEach(
            attachment -> mail.addAttachment(
                attachment.getName(),
                attachment,
                FileUtil.getFileMimeType(attachment)
            )
        );
        return reactiveMailer.send(mail);
    }

    public void sendEmailWithAttachmentSync(String to, String subject, String htmlContent, List<File> attachments) {
        Mail mail = Mail.withHtml(
            to,
            subject,
            htmlContent
        );
        attachments.forEach(
            attachment -> mail.addAttachment(
                attachment.getName(),
                attachment,
                FileUtil.getFileMimeType(attachment)
            )
        );
        mailer.send(mail);
    }

    private void handleFailure(final Throwable failure) {
        LOG.error("Failed to send email", failure);
    }

    @CheckedTemplate
    static class Templates {

        private Templates() {}

        public static MailTemplateInstance getStandardNotification(String name, String vorname, AppLanguages language) {
            return switch (language) {
                case FR -> standardNotificationFr(name, vorname);
                case DE -> standardNotificationDe(name, vorname);
            };
        }

        private static native MailTemplateInstance standardNotificationDe(String name, String vorname);

        private static native MailTemplateInstance standardNotificationFr(String name, String vorname);

        public static native MailTemplateInstance benutzerWelcome(String name, String vorname, String link);
    }
}
