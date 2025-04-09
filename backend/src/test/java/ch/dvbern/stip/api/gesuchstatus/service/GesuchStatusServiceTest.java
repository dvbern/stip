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

package ch.dvbern.stip.api.gesuchstatus.service;

import java.util.List;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchvalidation.service.GesuchValidatorService;
import ch.dvbern.stip.api.notification.service.NotificationService;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.generated.dto.KommentarDto;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest
class GesuchStatusServiceTest {
    @Inject
    GesuchStatusService gesuchStatusService;
    @InjectMock
    GesuchValidatorService gesuchValidatorService;
    @InjectMock
    NotificationService notificationService;

    @InjectSpy
    MailService mailService;

    @BeforeEach
    void setUp() {
        Mockito.doNothing().when(gesuchValidatorService).validateGesuchForStatus(Mockito.any(), Mockito.any());
        Mockito.doNothing()
            .when(notificationService)
            .createGesuchStatusChangeWithCommentNotification(Mockito.any(), Mockito.any());
    }

    @Test
    void aMailShouldOnlyBeSentOncePerRequestTest() {
        Gesuch gesuch = new Gesuch();
        gesuch.setAusbildung(new Ausbildung().setFall(new Fall()));
        gesuch.setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_SB);
        var tranche = new GesuchTranche();
        tranche.setGesuchFormular(new GesuchFormular());
        tranche.getGesuchFormular()
            .setPersonInAusbildung(
                new PersonInAusbildung().setKorrespondenzSprache(Sprache.DEUTSCH).setEmail("test@test.com")
            );
        gesuch.setGesuchTranchen(List.of(tranche));

        var gesuchKommentarDto = new KommentarDto();
        gesuchKommentarDto.setText("");

        gesuchStatusService.sendStandardNotificationEmailForGesuch(gesuch);
        gesuchStatusService.sendStandardNotificationEmailForGesuch(gesuch);
        Mockito.verify(mailService, Mockito.times(1))
            .sendStandardNotificationEmails(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
    }
}
