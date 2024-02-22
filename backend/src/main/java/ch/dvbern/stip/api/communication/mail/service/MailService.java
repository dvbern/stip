package ch.dvbern.stip.api.communication.mail.service;

import java.io.File;
import java.util.List;
import java.util.Locale;

import ch.dvbern.stip.api.common.i18n.StipEmailMessages;
import ch.dvbern.stip.api.common.i18n.StipMessagesResourceBundle;
import ch.dvbern.stip.api.common.util.FileUtil;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.MailTemplate.MailTemplateInstance;
import io.quarkus.mailer.Mailer;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.TemplateException;
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

    public void sendGesuchEingereichtEmail(
        String name,
        String vorname,
        String receiver,
        Locale locale
    ) {
        Templates.getGesuchEingereicht(name, vorname, locale.getLanguage())
            .to(receiver)
            .subject(StipMessagesResourceBundle.getMessage(
                StipEmailMessages.EINGEREICHT.getMessage(),
                locale
            ))
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

        private static native MailTemplateInstance gesuchEingereichtDe(String name, String vorname);

        private static native MailTemplateInstance gesuchEingereichtFr(String name, String vorname);

        public static MailTemplateInstance getGesuchEingereicht(String name, String vorname, String language) {
            return switch (language) {
                case "de" -> gesuchEingereichtDe(name, vorname);
                case "fr" -> gesuchEingereichtFr(name, vorname);
                default -> throw new TemplateException(String.format(
                    "Es gibt kein Gesuch eingereicht mail template für die Sprache %s",
                    language)
                );
            };
        }
    }
}
