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

package ch.dvbern.stip.api.sap.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import ch.dvbern.stip.api.SapEndpointServiceMock;
import ch.dvbern.stip.api.adresse.entity.Adresse;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.buchhaltung.entity.Buchhaltung;
import ch.dvbern.stip.api.buchhaltung.repo.BuchhaltungRepository;
import ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType;
import ch.dvbern.stip.api.buchhaltung.type.SapStatus;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.communication.mail.service.MailService;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.fall.repo.FallRepository;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.land.entity.Land;
import ch.dvbern.stip.api.notification.service.NotificationService;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.api.sap.entity.SapDelivery;
import ch.dvbern.stip.api.sap.repo.SapDeliveryRepository;
import ch.dvbern.stip.api.zahlungsverbindung.entity.Zahlungsverbindung;
import ch.dvbern.stip.api.zahlungsverbindung.repo.ZahlungsverbindungRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class SapServiceTest {
    @Inject
    SapService sapService;

    @Inject
    SapEndpointServiceMock sapEndpointServiceMock;

    @InjectMock
    BuchhaltungRepository buchhaltungRepositoryMock;

    @InjectMock
    GesuchRepository gesuchRepositoryMock;

    @InjectMock
    FallRepository fallRepositoryMock;

    @InjectMock
    ZahlungsverbindungRepository zahlungsverbindungRepositoryMock;

    @InjectMock
    SapDeliveryRepository sapDeliveryRepositoryMock;

    @InjectMock
    NotificationService notificationServiceMock;

    @InjectMock
    MailService mailServiceMock;

    @Transactional
    @BeforeAll
    void setUp() {
        QuarkusMock.installMockForType(sapDeliveryRepositoryMock, SapDeliveryRepository.class);
        QuarkusMock.installMockForType(buchhaltungRepositoryMock, BuchhaltungRepository.class);
        QuarkusMock.installMockForType(gesuchRepositoryMock, GesuchRepository.class);
        QuarkusMock.installMockForType(fallRepositoryMock, FallRepository.class);
        QuarkusMock.installMockForType(zahlungsverbindungRepositoryMock, ZahlungsverbindungRepository.class);
        QuarkusMock.installMockForType(notificationServiceMock, NotificationService.class);
        QuarkusMock.installMockForType(mailServiceMock, MailService.class);
    }

    @BeforeEach
    void setUpEach() {
        Mockito.doAnswer(invocationOnMock -> {
            final var buchhaltung = invocationOnMock.getArgument(0, Buchhaltung.class);
            buchhaltung.setId(UUID.randomUUID());
            return buchhaltung;
        }
        ).when(buchhaltungRepositoryMock).persistAndFlush(any());

        Mockito.doAnswer(invocationOnMock -> {
            final var sapDelivery = invocationOnMock.getArgument(0, SapDelivery.class);
            sapDelivery.setId(UUID.randomUUID());
            return sapDelivery;
        }).when(sapDeliveryRepositoryMock).persistAndFlush(any());
    }

    @Test
    void testCreateInitialAuszahlungOrGetStatusSimple() {
        // Arrange
        final var gesuch = prepareGesuchForSapService();

        Mockito.when(buchhaltungRepositoryMock.findPendingBuchhaltungEntryOfFall(any(), any()))
            .thenReturn(Optional.empty());

        final var stipendiumsBetrag = 1000;
        final var lastEntryStipendiumBuchhaltung = new Buchhaltung()
            .setBetrag(stipendiumsBetrag)
            .setSaldo(stipendiumsBetrag)
            .setBuchhaltungType(BuchhaltungType.STIPENDIUM);
        lastEntryStipendiumBuchhaltung.setId(UUID.randomUUID());
        Mockito.when(buchhaltungRepositoryMock.findStipendiumsEntrysForGesuch(any()))
            .thenReturn(Stream.of(lastEntryStipendiumBuchhaltung));

        Mockito.when(buchhaltungRepositoryMock.findAllForFallId(any()))
            .thenReturn(Stream.of(lastEntryStipendiumBuchhaltung));

        gesuch.getAusbildung().getFall().setBuchhaltungs(new ArrayList<>());
        gesuch.getAusbildung().getFall().getBuchhaltungs().add(lastEntryStipendiumBuchhaltung);

        // Act
        sapService.createInitialAuszahlungOrGetStatus(UUID.randomUUID());

        // Assert
        assertThat(
            gesuch.getAusbildung()
                .getFall()
                .getBuchhaltungs()
                .get(gesuch.getAusbildung().getFall().getBuchhaltungs().size() - 1)
                .getSapDeliverys()
                .size(),
            Matchers.greaterThanOrEqualTo(1)
        );
    }

    @Test
    void testCreateInitialAuszahlungOrGetStatusMultipleCalls() {
        // Arrange
        final var gesuch = prepareGesuchForSapService();

        Mockito.when(buchhaltungRepositoryMock.findPendingBuchhaltungEntryOfFall(any(), any()))
            .thenReturn(Optional.empty());

        final var stipendiumsBetrag = 1000;
        final var lastEntryStipendiumBuchhaltung = new Buchhaltung()
            .setBetrag(stipendiumsBetrag)
            .setSaldo(stipendiumsBetrag)
            .setBuchhaltungType(BuchhaltungType.STIPENDIUM);
        lastEntryStipendiumBuchhaltung.setId(UUID.randomUUID());
        Mockito.when(buchhaltungRepositoryMock.findStipendiumsEntrysForGesuch(any()))
            .thenReturn(Stream.of(lastEntryStipendiumBuchhaltung));

        Mockito.when(buchhaltungRepositoryMock.findAllForFallId(any()))
            .thenReturn(Stream.of(lastEntryStipendiumBuchhaltung));

        final var auszahlungBuchhaltungForGesuch =
            new Buchhaltung().setBuchhaltungType(BuchhaltungType.AUSZAHLUNG_INITIAL);
        auszahlungBuchhaltungForGesuch.setBetrag(stipendiumsBetrag);
        gesuch.getAusbildung().getFall().setBuchhaltungs(new ArrayList<>());
        gesuch.getAusbildung().getFall().getBuchhaltungs().add(lastEntryStipendiumBuchhaltung);

        sapEndpointServiceMock.setImportStatusReadResponse(SapEndpointServiceMock.SUCCESS_STRING, SapStatus.FAILURE);

        // Act
        sapService.createInitialAuszahlungOrGetStatus(UUID.randomUUID());
        final var relevantBuchhaltung = gesuch.getAusbildung()
            .getFall()
            .getBuchhaltungs()
            .get(gesuch.getAusbildung().getFall().getBuchhaltungs().size() - 1);

        // Assert
        assertThat(relevantBuchhaltung.getSapDeliverys().size(), Matchers.greaterThanOrEqualTo(1));

        // Arrange
        Mockito.when(buchhaltungRepositoryMock.findPendingBuchhaltungEntryOfFall(any(), any()))
            .thenReturn(Optional.of(relevantBuchhaltung));

        relevantBuchhaltung.getSapDeliverys().get(0).setTimestampErstellt(LocalDateTime.now());
        relevantBuchhaltung.getSapDeliverys().get(0).setTimestampMutiert(LocalDateTime.now());

        // Act
        sapService.createInitialAuszahlungOrGetStatus(UUID.randomUUID());

        // Assert
        assertThat(relevantBuchhaltung.getSapDeliverys().size(), Matchers.greaterThanOrEqualTo(1));

        // Arrange
        relevantBuchhaltung.getSapDeliverys()
            .forEach(
                sapDelivery -> sapDelivery
                    .setTimestampErstellt(LocalDateTime.now().minusHours(SapService.HOURS_BETWEEN_SAP_TRIES + 1))
            );
        relevantBuchhaltung.getSapDeliverys()
            .forEach(
                sapDelivery -> sapDelivery
                    .setTimestampMutiert(LocalDateTime.now().minusHours(SapService.HOURS_BETWEEN_SAP_TRIES + 1))
            );

        // Act
        sapService.createInitialAuszahlungOrGetStatus(UUID.randomUUID());

        // Assert
        assertThat(relevantBuchhaltung.getSapDeliverys().size(), Matchers.greaterThanOrEqualTo(2));

        // Arrange
        relevantBuchhaltung.getSapDeliverys()
            .forEach(sapDelivery -> sapDelivery.setTimestampErstellt(LocalDateTime.now()));
        relevantBuchhaltung.getSapDeliverys()
            .forEach(sapDelivery -> sapDelivery.setTimestampMutiert(LocalDateTime.now()));

        // Act
        sapService.createInitialAuszahlungOrGetStatus(UUID.randomUUID());

        // Assert
        assertThat(relevantBuchhaltung.getSapDeliverys().size(), Matchers.greaterThanOrEqualTo(2));

        // Arrange
        relevantBuchhaltung.getSapDeliverys()
            .forEach(
                sapDelivery -> sapDelivery
                    .setTimestampErstellt(LocalDateTime.now().minusHours(SapService.HOURS_BETWEEN_SAP_TRIES + 1))
            );
        relevantBuchhaltung.getSapDeliverys()
            .forEach(
                sapDelivery -> sapDelivery
                    .setTimestampMutiert(LocalDateTime.now().minusHours(SapService.HOURS_BETWEEN_SAP_TRIES + 1))
            );

        // Act
        sapService.createInitialAuszahlungOrGetStatus(UUID.randomUUID());

        // Assert
        Mockito.verify(notificationServiceMock, Mockito.times(1)).createFailedAuszahlungBuchhaltungNotification(any());
        assertThat(relevantBuchhaltung.getSapDeliverys().size(), Matchers.greaterThanOrEqualTo(3));
        assertThat(relevantBuchhaltung.getSapStatus(), Matchers.equalTo(SapStatus.FAILURE));
        assertThrows(
            IllegalStateException.class,
            () -> sapService.createInitialAuszahlungOrGetStatus(UUID.randomUUID())
        );
    }

    @Test
    void testRetryFailedAuszahlungBuchhaltunManually() {
        // Arrange
        final var gesuch = prepareGesuchForSapService();

        Mockito.when(buchhaltungRepositoryMock.findPendingBuchhaltungEntryOfFall(any(), any()))
            .thenReturn(Optional.empty());

        final var stipendiumsBetrag = 1000;
        final var lastEntryStipendiumBuchhaltung = new Buchhaltung()
            .setBetrag(stipendiumsBetrag)
            .setSaldo(stipendiumsBetrag)
            .setBuchhaltungType(BuchhaltungType.STIPENDIUM);
        lastEntryStipendiumBuchhaltung.setId(UUID.randomUUID());
        Mockito.when(buchhaltungRepositoryMock.findStipendiumsEntrysForGesuch(any()))
            .thenReturn(Stream.of(lastEntryStipendiumBuchhaltung));

        Mockito.doAnswer(invocation -> Stream.of(lastEntryStipendiumBuchhaltung))
            .when(buchhaltungRepositoryMock)
            .findAllForFallId(any());

        gesuch.getAusbildung().getFall().setBuchhaltungs(new ArrayList<>());
        gesuch.getAusbildung().getFall().getBuchhaltungs().add(lastEntryStipendiumBuchhaltung);

        // Act | Assert
        assertThrows(
            BadRequestException.class,
            () -> sapService.retryAuszahlungBuchhaltung(gesuch.getAusbildung().getFall())
        );

        // Arrange
        sapService.createInitialAuszahlungOrGetStatus(UUID.randomUUID());
        final var relevantBuchhaltung = gesuch.getAusbildung()
            .getFall()
            .getBuchhaltungs()
            .get(gesuch.getAusbildung().getFall().getBuchhaltungs().size() - 1);

        Mockito.when(buchhaltungRepositoryMock.findPendingBuchhaltungEntryOfFall(any(), any()))
            .thenReturn(Optional.of(relevantBuchhaltung));

        relevantBuchhaltung.getSapDeliverys().get(0).setTimestampErstellt(LocalDateTime.now());
        relevantBuchhaltung.getSapDeliverys().get(0).setTimestampMutiert(LocalDateTime.now());

        sapService.createInitialAuszahlungOrGetStatus(UUID.randomUUID());

        relevantBuchhaltung.getSapDeliverys()
            .forEach(
                sapDelivery -> sapDelivery
                    .setTimestampErstellt(LocalDateTime.now().minusHours(SapService.HOURS_BETWEEN_SAP_TRIES + 1))
            );
        relevantBuchhaltung.getSapDeliverys()
            .forEach(
                sapDelivery -> sapDelivery
                    .setTimestampMutiert(LocalDateTime.now().minusHours(SapService.HOURS_BETWEEN_SAP_TRIES + 1))
            );

        sapService.createInitialAuszahlungOrGetStatus(UUID.randomUUID());

        relevantBuchhaltung.getSapDeliverys()
            .forEach(sapDelivery -> sapDelivery.setTimestampErstellt(LocalDateTime.now()));
        relevantBuchhaltung.getSapDeliverys()
            .forEach(sapDelivery -> sapDelivery.setTimestampMutiert(LocalDateTime.now()));

        sapService.createInitialAuszahlungOrGetStatus(UUID.randomUUID());

        relevantBuchhaltung.getSapDeliverys()
            .forEach(
                sapDelivery -> sapDelivery
                    .setTimestampErstellt(LocalDateTime.now().minusHours(SapService.HOURS_BETWEEN_SAP_TRIES + 1))
            );
        relevantBuchhaltung.getSapDeliverys()
            .forEach(
                sapDelivery -> sapDelivery
                    .setTimestampMutiert(LocalDateTime.now().minusHours(SapService.HOURS_BETWEEN_SAP_TRIES + 1))
            );

        sapService.createInitialAuszahlungOrGetStatus(UUID.randomUUID());

        Mockito.when(buchhaltungRepositoryMock.findPendingBuchhaltungEntryOfFall(any(), any()))
            .thenReturn(Optional.empty());
        Mockito.when(buchhaltungRepositoryMock.findStipendiumsEntrysForGesuch(any()))
            .thenReturn(Stream.of(lastEntryStipendiumBuchhaltung));
        final var noBuchhaltungBefore = gesuch.getAusbildung().getFall().getBuchhaltungs().size();

        // Act
        sapService.retryAuszahlungBuchhaltung(gesuch.getAusbildung().getFall());

        // Assert
        assertEquals(gesuch.getAusbildung().getFall().getBuchhaltungs().size(), noBuchhaltungBefore + 1);
    }

    private Gesuch prepareGesuchForSapService() {
        final var gesuch = new Gesuch();
        final var gesuchTranche = new GesuchTranche()
            .setGueltigkeit(
                new DateRange().setGueltigAb(LocalDate.now())
            )
            .setTyp(
                GesuchTrancheTyp.TRANCHE
            );
        final var pia = new PersonInAusbildung().setKorrespondenzSprache(
            Sprache.DEUTSCH
        );
        pia.setVorname("").setNachname("");
        final var gesuchFormular = new GesuchFormular()
            .setPersonInAusbildung(pia);
        gesuchTranche.setGesuchFormular(gesuchFormular);
        gesuch.setGesuchTranchen(List.of(gesuchTranche));
        gesuch.setGesuchsperiode(
            new Gesuchsperiode()
                .setZweiterAuszahlungsterminMonat(12)
        );
        final var zahlungsverbindung = new Zahlungsverbindung()
            .setSapBusinessPartnerId(123)
            .setAdresse(new Adresse().setLand(new Land()));
        gesuch.setAusbildung(
            new Ausbildung()
                .setFall(
                    new Fall().setAuszahlung(
                        new Auszahlung().setZahlungsverbindung(zahlungsverbindung)
                    )
                )
        );
        gesuch.getAusbildung().getFall().setAusbildungs(Set.of(gesuch.getAusbildung()));
        gesuch.getAusbildung().setGesuchs(List.of(gesuch));
        Mockito.when(gesuchRepositoryMock.requireById(any())).thenReturn(gesuch);
        return gesuch;
    }

}
