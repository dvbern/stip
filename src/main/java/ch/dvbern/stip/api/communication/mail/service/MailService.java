package ch.dvbern.stip.api.communication.mail.service;

import java.io.File;
import java.util.List;

import ch.dvbern.stip.api.common.util.FileUtil;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.MailTemplate.MailTemplateInstance;
import io.quarkus.mailer.Mailer;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.quarkus.qute.CheckedTemplate;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
public class MailService {


	@CheckedTemplate
	static class Templates {
		public static native MailTemplateInstance gesuchNichtKomplettEingereicht(String vorname, String name);
	}

	private final Mailer mailer;
	private final ReactiveMailer reactiveMailer;


	public Uni<Void> sendGesuchNichtKomplettEingereichtMEmail(String name, String vorname, String email) {
		return Templates.gesuchNichtKomplettEingereicht(vorname, name)
				.to(email)
				.subject("Gesuch übermittelt - Fehlende Dokumente")
				.send();
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


	public Void sendEmailSync(String to, String subject, String htmlContent) {
		mailer.send(
				Mail.withHtml(
						to,
						subject,
						htmlContent
				)
		);
		return null;
	}

	public Uni<Void> sendEmailWithAttachment(String to, String subject, String htmlContent, List<File> attachments) {
		Mail mail = Mail.withHtml(to, subject,
						htmlContent);
		attachments.forEach(attachment -> mail.addAttachment(attachment.getName(), attachment, FileUtil.getFileMimeType(attachment)));
		return reactiveMailer.send(mail);
	}

	public Void sendEmailWithAttachmentSync(String to, String subject, String htmlContent, List<File> attachments) {
		Mail mail = Mail.withHtml(to, subject,
				htmlContent);
		attachments.forEach(attachment -> mail.addAttachment(attachment.getName(), attachment, FileUtil.getFileMimeType(attachment)));
		mailer.send(mail);
		return null;
	}

}
