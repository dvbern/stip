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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import ch.dvbern.stip.api.SapEndpointServiceMock;
import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.buchhaltung.entity.Buchhaltung;
import ch.dvbern.stip.api.buchhaltung.service.BuchhaltungService;
import ch.dvbern.stip.api.common.util.DateRange;
import ch.dvbern.stip.api.fall.entity.Fall;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.repo.GesuchRepository;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.type.Sprache;
import ch.dvbern.stip.api.sap.repo.SapDeliveryRepository;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.zahlungsverbindung.entity.Zahlungsverbindung;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Slf4j
public class SapServiceTest {
    @Inject
    SapService sapService;

    @InjectMock
    BuchhaltungService buchhaltungServiceMock;

    @InjectMock
    GesuchRepository gesuchRepositoryMock;

    @InjectMock
    SapDeliveryRepository sapDeliveryRepositoryMock;

    @Inject
    SapEndpointServiceMock sapEndpointServiceMock;

    @Transactional
    @BeforeAll
    void setUp() {
        QuarkusMock.installMockForType(buchhaltungServiceMock, BuchhaltungService.class);
        QuarkusMock.installMockForType(gesuchRepositoryMock, GesuchRepository.class);
        QuarkusMock.installMockForType(sapDeliveryRepositoryMock, SapDeliveryRepository.class);
    }

    @Test
    void testCreateInitialAuszahlungOrGetStatus() {
        final var gesuch = new Gesuch();
        final var gesuchTranche = new GesuchTranche().setGueltigkeit(new DateRange().setGueltigAb(LocalDate.now()))
            .setTyp(
                GesuchTrancheTyp.TRANCHE
            );
        final var pia = new PersonInAusbildung().setKorrespondenzSprache(
            Sprache.DEUTSCH
        );
        pia.setVorname("").setNachname("");
        final var gesuchFormular = new GesuchFormular().setPersonInAusbildung(pia);
        gesuchTranche.setGesuchFormular(gesuchFormular);
        gesuch.setGesuchTranchen(List.of(gesuchTranche));
        gesuch.setGesuchsperiode(new Gesuchsperiode().setZweiterAuszahlungsterminMonat(12));
        final var zahlungsverbindung = new Zahlungsverbindung()
            .setSapBusinessPartnerId(123);
        gesuch.setAusbildung(
            new Ausbildung()
                .setFall(new Fall().setAuszahlung(new Auszahlung().setZahlungsverbindung(zahlungsverbindung)))
        );
        Mockito.when(gesuchRepositoryMock.requireById(any())).thenReturn(gesuch);

        Mockito.when(buchhaltungServiceMock.findLatestPendingBuchhaltungAuszahlungOpt(any(), any()))
            .thenReturn(Optional.empty());
        final var lastEntryStipendiumBuchhaltung = new Buchhaltung()
            .setBetrag(1000);
        Mockito.when(buchhaltungServiceMock.getLastEntryStipendiumOpt(any()))
            .thenReturn(Optional.of(lastEntryStipendiumBuchhaltung));
        final var latestNotFailedBuchhaltung = new Buchhaltung()
            .setSaldo(1000);
        Mockito.when(buchhaltungServiceMock.getLatestNotFailedBuchhaltungEntry(any()))
            .thenReturn(latestNotFailedBuchhaltung);
        final var auszahlungBuchhaltungForGesuch = new Buchhaltung();
        auszahlungBuchhaltungForGesuch.setId(UUID.randomUUID());
        auszahlungBuchhaltungForGesuch.setBetrag(1000);
        Mockito.when(buchhaltungServiceMock.createAuszahlungBuchhaltungForGesuch(any(), any(), any()))
            .thenReturn(auszahlungBuchhaltungForGesuch);

        sapService.createInitialAuszahlungOrGetStatus(UUID.randomUUID());
        assertThat(auszahlungBuchhaltungForGesuch.getSapDeliverys().size(), Matchers.greaterThanOrEqualTo(1));
    }

}
