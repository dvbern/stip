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
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class MailService {

	@CheckedTemplate
	static class Templates {
		private static native MailTemplateInstance gesuchNichtKomplettEingereicht_de(String vorname, String name);

		private static native MailTemplateInstance gesuchNichtKomplettEingereicht_fr(String vorname, String name);

		public static native MailTemplateInstance gesuchNichtKomplettEingereichtNachfrist_de(String vorname, String name);
		public static native MailTemplateInstance gesuchNichtKomplettEingereichtNachfrist_fr(String vorname, String name);

		public static MailTemplateInstance getGesuchNichtKomplettEingereichtMailTemplate(
				String name,
				String vorname,
				String language) {
			return language.equals("fr") ?
					gesuchNichtKomplettEingereicht_fr(name, vorname) :
					gesuchNichtKomplettEingereicht_de(name, vorname);
		}

		public static MailTemplateInstance getGesuchNichtKomplettEingereichtNachfristTemplate(
				String name,
				String vorname,
				String language) {
			return language.equals("fr") ?
					gesuchNichtKomplettEingereichtNachfrist_fr(name, vorname) :
					gesuchNichtKomplettEingereichtNachfrist_de(name, vorname);
		}
	}

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

	public Uni<Void> sendGesuchNichtKomplettEingereichtNachfristEmail(String name, String vorname, String email, Locale local) {
		return Templates.getGesuchNichtKomplettEingereichtNachfristTemplate(vorname, name, local.getLanguage())
				.to(email)
				.subject(StipMessagesResourceBundle.getMessage(StipEmailMessages.NICHT_KOMPLTETT_EINGEREICHT_NACHFRIST_SUBJECT.getMessage(), local))
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
		attachments.forEach(attachment -> mail.addAttachment(
				attachment.getName(),
				attachment,
				FileUtil.getFileMimeType(attachment)));
		return reactiveMailer.send(mail);
	}

	public Void sendEmailWithAttachmentSync(String to, String subject, String htmlContent, List<File> attachments) {
		Mail mail = Mail.withHtml(to, subject,
				htmlContent);
		attachments.forEach(attachment -> mail.addAttachment(
				attachment.getName(),
				attachment,
				FileUtil.getFileMimeType(attachment)));
		mailer.send(mail);
		return null;
	}

}
