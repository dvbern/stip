package ch.dvbern.stip.api.communication.mail.service;

import ch.dvbern.stip.api.common.i18n.StipEmailMessages;
import ch.dvbern.stip.api.common.i18n.StipMessagesResourceBundle;
import ch.dvbern.stip.api.common.util.FileUtil;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.MailTemplate.MailTemplateInstance;
import io.quarkus.mailer.Mailer;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.quarkus.qute.CheckedTemplate;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.Locale;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final Mailer mailer;
    private final ReactiveMailer reactiveMailer;

    public void sendGesuchNichtKomplettEingereichtEmail(
        String name,
        String vorname,
        String email,
        Locale local) {
        Templates.getGesuchNichtKomplettEingereichtMailTemplate(name, vorname, local.getLanguage())
            .to(email)
            .subject(StipMessagesResourceBundle.getMessage(StipEmailMessages.FEHLENDE_DOKUMENTE_SUBJECT.getMessage(), local))
            .send().subscribe().asCompletionStage();
    }

    public void sendGesuchNichtKomplettEingereichtNachfristEmail(String name, String vorname, String email, Locale local) {
        Templates.getGesuchNichtKomplettEingereichtNachfristTemplate(name, vorname, local.getLanguage())
            .to(email)
            .subject(StipMessagesResourceBundle.getMessage(StipEmailMessages.NICHT_KOMPLTETT_EINGEREICHT_NACHFRIST_SUBJECT.getMessage(), local))
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

        private static native MailTemplateInstance gesuchNichtKomplettEingereichtDe(String name, String vorname);

        private static native MailTemplateInstance gesuchNichtKomplettEingereichtFr(String name, String vorname);

        private static native MailTemplateInstance gesuchNichtKomplettEingereichtNachfristDe(String name, String vorname);

        private static native MailTemplateInstance gesuchNichtKomplettEingereichtNachfristFr(String name, String vorname);

        public static MailTemplateInstance getGesuchNichtKomplettEingereichtMailTemplate(
            String name,
            String vorname,
            String language) {
            return language.equals("fr") ?
                gesuchNichtKomplettEingereichtFr(name, vorname) :
                gesuchNichtKomplettEingereichtDe(name, vorname);
        }

        public static MailTemplateInstance getGesuchNichtKomplettEingereichtNachfristTemplate(
            String name,
            String vorname,
            String language) {
            return language.equals("fr") ?
                gesuchNichtKomplettEingereichtNachfristFr(name, vorname) :
                gesuchNichtKomplettEingereichtNachfristDe(name, vorname);
        }
    }

}
