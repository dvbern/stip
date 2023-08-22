package ch.dvbern.stip.test.communication.mail.service;

import ch.dvbern.stip.api.communication.mail.service.MailService;
import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.ext.mail.MailMessage;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static ch.dvbern.stip.test.util.TestConstants.TEST_FILE_LOCATION;

@QuarkusTest
@Slf4j
class MailServiceTest {

	private static final String TO = "test@dvbern.ch";

	private static final String SUBJECT = "test";

	private static final String HTML_CONTENT = "<h1>hello email world<h1>";

	@Inject
	MockMailbox mailbox;

	@Inject
	MailService mailService;

	@BeforeEach
	void init() {
		mailbox.clear();
	}

	@Test
	void testMailAsync() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		// Call the asynchronous method with a subscription otherwise the async call is never made
		mailService.sendEmail(TO, SUBJECT, HTML_CONTENT).subscribe().with(
				(item) -> latch.countDown()
				, failure -> {
					LOG.error("Email couldn't be sent to: " + TO);
				});

		// Wait for the asynchronous operation to complete
		boolean completed = latch.await(2, TimeUnit.SECONDS);
		Assertions.assertTrue(completed);

		// verify that it was sent
		List<MailMessage> sent = mailbox.getMailMessagesSentTo(TO);
		Assertions.assertEquals(1, sent.size());
		MailMessage actual = sent.get(0);
		Assertions.assertEquals(HTML_CONTENT, actual.getHtml());
		Assertions.assertEquals(SUBJECT, actual.getSubject());
	}

	@Test
	void testMailWithAttachmentAsync() throws InterruptedException {
		File file = new File(TEST_FILE_LOCATION);
		List<File> files = new ArrayList<>();
		files.add(file);
		CountDownLatch latch = new CountDownLatch(1);
		// Call the asynchronous method with a subscription otherwise the async call is never made
		mailService.sendEmailWithAttachment(TO, SUBJECT, HTML_CONTENT, files).subscribe().with(
				(item) -> latch.countDown()
				, failure -> {
					LOG.error("Email couldn't be sent to: " + TO);
				});

		// Wait for the asynchronous operation to complete
		boolean completed = latch.await(2, TimeUnit.SECONDS);
		Assertions.assertTrue(completed);
		// verify that it was sent
		List<MailMessage> sent = mailbox.getMailMessagesSentTo(TO);
		Assertions.assertEquals(1, sent.size());
		MailMessage actual = sent.get(0);
		Assertions.assertEquals(HTML_CONTENT, actual.getHtml());
		Assertions.assertEquals(SUBJECT, actual.getSubject());
		Assertions.assertEquals(1, actual.getAttachment().size());
		Assertions.assertEquals("text/plain", actual.getAttachment().get(0).getContentType());
		Assertions.assertEquals(file.getName(), actual.getAttachment().get(0).getName());
	}

	@Test
	void testMailSync() throws InterruptedException {
		// Call the synchronous method
		mailService.sendEmailSync(TO, SUBJECT, HTML_CONTENT);
		// verify that it was sent
		List<MailMessage> sent = mailbox.getMailMessagesSentTo(TO);
		Assertions.assertEquals(1, sent.size());
		MailMessage actual = sent.get(0);
		Assertions.assertEquals(HTML_CONTENT, actual.getHtml());
		Assertions.assertEquals(SUBJECT, actual.getSubject());
	}

	@Test
	void testMailWithAttachmentSync() throws InterruptedException {
		File file = new File(TEST_FILE_LOCATION);
		List<File> files = new ArrayList<>();
		files.add(file);
		// Call the synchronous method
		mailService.sendEmailWithAttachmentSync(TO, SUBJECT, HTML_CONTENT, files);
		// verify that it was sent
		List<MailMessage> sent = mailbox.getMailMessagesSentTo(TO);
		Assertions.assertEquals(1, sent.size());
		MailMessage actual = sent.get(0);
		Assertions.assertEquals(HTML_CONTENT, actual.getHtml());
		Assertions.assertEquals(SUBJECT, actual.getSubject());
		Assertions.assertEquals(1, actual.getAttachment().size());
		Assertions.assertEquals("text/plain", actual.getAttachment().get(0).getContentType());
		Assertions.assertEquals(file.getName(), actual.getAttachment().get(0).getName());
	}
}