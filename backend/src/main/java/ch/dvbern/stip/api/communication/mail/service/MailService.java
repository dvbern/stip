package ch.dvbern.stip.api.communication.mail.service;

import java.io.File;
import java.util.List;

import ch.dvbern.stip.api.common.i18n.translations.AppLanguages;
import ch.dvbern.stip.api.common.i18n.translations.TL;
import ch.dvbern.stip.api.common.i18n.translations.TLProducer;
import ch.dvbern.stip.api.common.util.FileUtil;
import ch.dvbern.stip.api.config.service.ConfigService;
import ch.dvbern.stip.api.tenancy.service.TenantService;
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
    private final TL tl;
    private final ReactiveMailer reactiveMailer;
    private final ConfigService configService;
    private final TenantService tenantService;

    public void sendStandardNotificationEmail(
        String name,
        String vorname,
        String receiver,
        AppLanguages language){
        String subject = "Neue Nachricht";
        if(language == AppLanguages.FR){
            subject = "Nouveau message";
        }
        Templates.getStandardNotification(name,vorname,language)
            .to(receiver)
            .subject(subject)
            .send().subscribe().asCompletionStage();
    }

    public void sendGesuchEingereichtEmail(
        String name,
        String vorname,
        String receiver,
        AppLanguages language
    ) {
        Templates.getGesuchEingereicht(name, vorname, language)
            .to(receiver)
            .subject(TLProducer.defaultBundle().forAppLanguage(language).translate("stip.gesuch.eingereicht"))
            .send().subscribe().asCompletionStage();
    }

    public void sendBenutzerWelcomeEmail(WelcomeMailDto welcomeMailDto) {
        String redirectURI = configService.getWelcomeMailURI(
            tenantService.getCurrentTenant().getIdentifier(),
            welcomeMailDto.getRedirectUri()
        );

        Templates.benutzerWelcome(welcomeMailDto.getName(), welcomeMailDto.getVorname(), redirectURI)
            .to(welcomeMailDto.getEmail())
            .subject("Benuzter Erstellt/ Utilisateur Créé")
            .send().subscribe().asCompletionStage();
    }

    public Uni<Void> sendEmail(String to, String subject, String htmlContent) {
        return reactiveMailer.send(
            Mail.withHtml(
                to,
                subject,
                htmlContent
            )
        );
    }

    public void sendEmailSync(String to, String subject, String htmlContent) {
        mailer.send(
            Mail.withHtml(
                to,
                subject,
                htmlContent
            )
        );
    }

    public Uni<Void> sendEmailWithAttachment(String to, String subject, String htmlContent, List<File> attachments) {
        Mail mail = Mail.withHtml(to, subject,
            htmlContent
        );
        attachments.forEach(attachment -> mail.addAttachment(
            attachment.getName(),
            attachment,
            FileUtil.getFileMimeType(attachment)
        ));
        return reactiveMailer.send(mail);
    }

    public void sendEmailWithAttachmentSync(String to, String subject, String htmlContent, List<File> attachments) {
        Mail mail = Mail.withHtml(to, subject,
            htmlContent
        );
        attachments.forEach(attachment -> mail.addAttachment(
            attachment.getName(),
            attachment,
            FileUtil.getFileMimeType(attachment)
        ));
        mailer.send(mail);
    }

    @CheckedTemplate
    static class Templates {

        private Templates() {
        }

        public static MailTemplateInstance getStandardNotification(String name, String vorname, AppLanguages language) {
            return switch (language) {
                case FR -> getStandardNotificationFr(name, vorname);
                case DE -> getStandardNotificationDe(name, vorname);
            };
        }
        private static native MailTemplateInstance getStandardNotificationDe(String name, String vorname);

        private static native MailTemplateInstance getStandardNotificationFr(String name, String vorname);

        public static native MailTemplateInstance benutzerWelcome(String name, String vorname, String link);

        private static native MailTemplateInstance gesuchEingereichtDe(String name, String vorname);

        private static native MailTemplateInstance gesuchEingereichtFr(String name, String vorname);

        public static MailTemplateInstance getGesuchEingereicht(String name, String vorname, AppLanguages language) {
            return switch (language) {
                case FR -> gesuchEingereichtFr(name, vorname);
                case DE -> gesuchEingereichtDe(name, vorname);
            };
        }
    }
}
