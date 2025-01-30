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
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import ch.dvbern.stip.api.common.i18n.translations.AppLanguages;
import ch.dvbern.stip.api.common.i18n.translations.TLProducer;
import ch.dvbern.stip.generated.dto.WelcomeMailDto;
import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.ext.mail.MailMessage;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ch.dvbern.stip.api.util.TestConstants.TEST_FILE_LOCATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@QuarkusTest
@Slf4j
class MailServiceTest {

    private static final String TO = "test@dvbern.ch";

    private static final String SUBJECT = "test";

    private static final String HTML_CONTENT = "<h1>hello email world<h1>";

    private static final String TEST_EMAIL_DE_STRING = "Gesuch wurde übermittelt";

    private static final String TEST_EMAIL = "jean@bat.ch";

    private static final String TEST_STANDARD_EMAIL_DE_STRING = "neue Nachricht";
    private static final String TEST_STANDARD_EMAIL_FR_STRING = "nouveax message";

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
        mailService.sendEmail(TO, SUBJECT, HTML_CONTENT)
            .subscribe()
            .with(
                (item) -> latch.countDown(),
                failure -> {
                    LOG.error("Email couldn't be sent to: " + TO);
                }
            );

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
        mailService.sendEmailWithAttachment(TO, SUBJECT, HTML_CONTENT, files)
            .subscribe()
            .with(
                (item) -> latch.countDown(),
                failure -> {
                    LOG.error("Email couldn't be sent to: " + TO);
                }
            );

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

    @Test
    void sendWelcomeEmail() {
        WelcomeMailDto welcomeMailDto = new WelcomeMailDto()
            .name("WelcomeEmailTestName")
            .vorname("WelcomeEmailTestVorname")
            .email(TEST_EMAIL)
            .redirectUri("localhost:4200");
        mailService.sendBenutzerWelcomeEmail(welcomeMailDto);
        List<MailMessage> sent = mailbox.getMailMessagesSentTo(TEST_EMAIL);
        Assertions.assertEquals(1, sent.size());
        MailMessage actual = sent.get(0);
        assertThat(actual.getTo()).contains(TEST_EMAIL);
        assertThat(actual.getSubject()).isNotBlank();
        assertThat(actual.getHtml()).contains(welcomeMailDto.getName());
        assertThat(actual.getHtml()).contains(welcomeMailDto.getVorname());
        assertThat(actual.getHtml()).contains(welcomeMailDto.getRedirectUri());
    }

    @Test
    void sendStandardNotificationEmail() {
        mailService.sendStandardNotificationEmail("", "", TEST_EMAIL, AppLanguages.DE);
        List<MailMessage> sent = mailbox.getMailMessagesSentTo(TEST_EMAIL);
        Assertions.assertEquals(1, sent.size());
        MailMessage actual = sent.get(0);
        actual.getSubject();
        assertThat(actual.getSubject()).isNotBlank();
        assertThat(actual.getSubject()).isEqualTo(
            TLProducer.defaultBundle()
                .forAppLanguage(AppLanguages.DE)
                .translate("stip.standard.notification")
        );
        assertThat(actual.getHtml()).contains(TEST_STANDARD_EMAIL_DE_STRING);

        mailService.sendStandardNotificationEmail("", "", TEST_EMAIL, AppLanguages.fromLocale(Locale.FRENCH));
        sent = mailbox.getMailMessagesSentTo(TEST_EMAIL);
        Assertions.assertEquals(2, sent.size());
        actual = sent.get(1);
        actual.getSubject();
        assertThat(actual.getSubject()).isNotBlank();
        assertThat(actual.getSubject()).isEqualTo(
            TLProducer.defaultBundle()
                .forAppLanguage(AppLanguages.FR)
                .translate("stip.standard.notification")
        );
        assertThat(actual.getHtml()).doesNotContain(TEST_STANDARD_EMAIL_FR_STRING);
    }

    @Test
    void sendStandardNotificationEmailsSingleRecipient() {
        final var recipient = "test-mitarbeiter@bat.ch";
        mailService.sendStandardNotificationEmails(
            "Test",
            "GS",
            AppLanguages.DE,
            List.of(recipient)
        );

        final var received = mailbox.getMailMessagesSentTo(recipient);
        assertThat(received).hasSize(1);

        final var message = received.get(0);
        assertThat(message.getCc()).isEmpty();
    }

    @Test
    void sendStandardNotificationEmailsMultipleRecipients() {
        final var recipient = "test-mitarbeiter@bat.ch";
        final var cc = "test-gs@bat.ch";
        mailService.sendStandardNotificationEmails(
            "Test",
            "GS",
            AppLanguages.DE,
            List.of(recipient, cc)
        );

        final var received = mailbox.getMailMessagesSentTo(recipient);
        assertThat(received).hasSize(1);

        final var message = received.get(0);
        assertThat(message.getCc()).hasSize(1);
        assertThat(message.getCc().get(0)).isEqualTo(cc);
    }

    @Test
    void sendStandardNotificationEmailsEmptyRecipient() {
        final var recipients = List.<String>of();
        assertThatThrownBy(
            () -> mailService.sendStandardNotificationEmails(
                "Test",
                "GS",
                AppLanguages.DE,
                recipients
            )
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
