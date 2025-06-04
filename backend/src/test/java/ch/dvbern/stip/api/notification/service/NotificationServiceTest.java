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

import java.time.LocalDate;
import java.util.List;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.common.type.Anrede;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchstatus.service.GesuchStatusService;
import ch.dvbern.stip.api.gesuchstatus.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.gesuchvalidation.service.GesuchValidatorService;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
@Slf4j
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

    @BeforeEach
    void setup() {
        GesuchValidatorService gesuchValidatorServiceMock = Mockito.mock(GesuchValidatorService.class);
        Mockito.doNothing()
            .when(gesuchValidatorServiceMock)
            .validateGesuchForTransition(any(), any());
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
            .setGesuchFormular(gesuchFormular)
            .setTyp(GesuchTrancheTyp.TRANCHE)
            .setGueltigkeit(new DateRange().setGueltigBis(LocalDate.now()));

        Gesuch gesuch = new Gesuch()
            .setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS)
            .setGesuchTranchen(List.of(gesuchTranche))
            .setAusbildung(new Ausbildung().setFall(new Fall()));

        gesuchStatusService.triggerStateMachineEvent(gesuch, GesuchStatusChangeEvent.EINGEREICHT);

        List<MailMessage> sent = mailbox.getMailMessagesSentTo(personInAusbildung.getEmail());
        Mockito.verify(notificationRepositoryMock).persistAndFlush(any(Notification.class));

        assertThat(sent).hasSize(1);
        assertThat(sent.get(0).getHtml())
            .contains("Guten Tag ", personInAusbildung.getVorname(), " ", personInAusbildung.getNachname());
    }
}
