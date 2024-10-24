package ch.dvbern.stip.api.notification.service;

import java.util.List;

import ch.dvbern.stip.api.common.type.Anrede;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuch.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuch.service.GesuchValidatorService;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import ch.dvbern.stip.api.notification.entity.Notification;
import ch.dvbern.stip.api.notification.repo.NotificationRepository;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.vertx.ext.mail.MailMessage;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
@Slf4j
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NotificationServiceTest {
    @Inject
    MockMailbox mailbox;

    @Inject
    GesuchStatusService gesuchStatusService;

    NotificationRepository notificationRepositoryMock;

    private final PersonInAusbildung personInAusbildung = ((PersonInAusbildung) new PersonInAusbildung()
        .setEmail("PiaEmail@test.ch")
        .setKorrespondenzSprache(Sprache.DEUTSCH)
        .setNachname("PiaNachnameTest")
        .setVorname("PiaVornameTest"))
        .setAnrede(Anrede.FRAU);

    @BeforeAll
    void setup() {
        GesuchValidatorService gesuchValidatorServiceMock = Mockito.mock(GesuchValidatorService.class);
        Mockito.doNothing().when(gesuchValidatorServiceMock).validateGesuchForStatus(any(Gesuch.class), any(
            Gesuchstatus.class));
        QuarkusMock.installMockForType(gesuchValidatorServiceMock, GesuchValidatorService.class);

        notificationRepositoryMock = Mockito.mock(NotificationRepository.class);
        Mockito.doNothing().when(notificationRepositoryMock).persistAndFlush(any(Notification.class));
        QuarkusMock.installMockForType(notificationRepositoryMock, NotificationRepository.class);
    }

    @BeforeEach
    void init() {
        mailbox.clear();
    }

    @Test
    void gesuchEinreichenSuccessful() {
        GesuchFormular gesuchFormular = new GesuchFormular()
            .setPersonInAusbildung(personInAusbildung);

        GesuchTranche gesuchTranche = new GesuchTranche()
            .setGesuchFormular(gesuchFormular);

        Gesuch gesuch = new Gesuch()
            .setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS)
            .setGesuchTranchen(List.of(gesuchTranche));

        gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.EINGEREICHT);

        List<MailMessage> sent = mailbox.getMailMessagesSentTo(personInAusbildung.getEmail());
        Mockito.verify(notificationRepositoryMock).persistAndFlush(any(Notification.class));

        assertThat(sent).hasSize(1);
        assertThat(sent.get(0).getHtml()).contains("Guten Tag ", personInAusbildung.getVorname(), " ", personInAusbildung.getNachname());
    }
}
