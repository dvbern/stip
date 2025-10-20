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

package ch.dvbern.stip.api.sap.scheduledtask;

import ch.dvbern.stip.api.SapEndpointServiceMock;
import ch.dvbern.stip.api.benutzer.util.TestAsFreigabestelleAndSachbearbeiter;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.buchhaltung.type.SapStatus;
import ch.dvbern.stip.api.sap.repo.SapDeliveryRepository;
import ch.dvbern.stip.api.sap.service.SapService;
import ch.dvbern.stip.api.util.RequestSpecUtil;
import ch.dvbern.stip.api.util.StepwiseExtension;
import ch.dvbern.stip.api.util.TestClamAVEnvironment;
import ch.dvbern.stip.api.util.TestDatabaseEnvironment;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.api.AusbildungApiSpec;
import ch.dvbern.stip.generated.api.AuszahlungApiSpec;
import ch.dvbern.stip.generated.api.DokumentApiSpec;
import ch.dvbern.stip.generated.api.FallApiSpec;
import ch.dvbern.stip.generated.api.GesuchApiSpec;
import ch.dvbern.stip.generated.api.GesuchTrancheApiSpec;
import ch.dvbern.stip.generated.api.SteuerdatenApiSpec;
import ch.dvbern.stip.generated.dto.GesuchDtoSpec;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTestResource(TestDatabaseEnvironment.class)
@QuarkusTestResource(TestClamAVEnvironment.class)
@QuarkusTest
@RequiredArgsConstructor
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(StepwiseExtension.class)
@Slf4j
public class SapScheduledTaskFilterTest {
    private final FallApiSpec fallApiSpec = FallApiSpec.fall(RequestSpecUtil.quarkusSpec());
    private final AuszahlungApiSpec auszahlungApiSpec = AuszahlungApiSpec.auszahlung(RequestSpecUtil.quarkusSpec());
    private final AusbildungApiSpec ausbildungApiSpec = AusbildungApiSpec.ausbildung(RequestSpecUtil.quarkusSpec());
    private final GesuchApiSpec gesuchApiSpec = GesuchApiSpec.gesuch(RequestSpecUtil.quarkusSpec());
    private final GesuchTrancheApiSpec gesuchTrancheApiSpec =
        GesuchTrancheApiSpec.gesuchTranche(RequestSpecUtil.quarkusSpec());
    private final DokumentApiSpec dokumentApiSpec = DokumentApiSpec.dokument(RequestSpecUtil.quarkusSpec());
    private final SteuerdatenApiSpec steuerdatenApiSpec = SteuerdatenApiSpec.steuerdaten(RequestSpecUtil.quarkusSpec());

    @InjectSpy
    private final SapService sapService;

    @Inject
    SapEndpointServiceMock sapEndpointServiceMock;

    @Inject
    SapDeliveryRepository sapDeliveryRepository;

    private GesuchDtoSpec gesuch;

    @BeforeAll
    void setUp() {
        QuarkusMock.installMockForType(sapService, SapService.class);
    }

    @BeforeEach
    void setUpEach() {
        Mockito.doNothing().when(sapService).processPendingSapAction(any());
        Mockito.doNothing().when(sapService).processPendingCreateVendorPostingAction(any());
    }

    @Test
    @TestAsGesuchsteller
    @Order(1)
    void createGesuchAusbildungFall() {
        gesuch = TestUtil.createAndSubmitGesuch(
            fallApiSpec,
            ausbildungApiSpec,
            gesuchApiSpec,
            auszahlungApiSpec,
            dokumentApiSpec
        );
    }

    @Test
    @TestAsFreigabestelleAndSachbearbeiter
    @Order(2)
    void gesuchToInFreigabe() {
        sapEndpointServiceMock.setBusinessPartnerCreateResponse(SapEndpointServiceMock.ERROR_STRING);
        sapEndpointServiceMock.setImportStatusReadResponse(SapEndpointServiceMock.ERROR_STRING, SapStatus.FAILURE);

        TestUtil.gesuchToInFreigabe(
            gesuch,
            gesuchApiSpec,
            steuerdatenApiSpec,
            dokumentApiSpec,
            gesuchTrancheApiSpec
        );

        MatcherAssert
            .assertThat(sapDeliveryRepository.findAll().list().getFirst().getSapStatus(), equalTo(SapStatus.FAILURE));

        Mockito.doNothing().when(sapService).createBusinessPartnerOrGetStatus(any());

        Mockito.doNothing().when(sapService).createRemainderAuszahlungOrGetStatus(any());
        Mockito.doNothing().when(sapService).createVendorPostingOrGetStatus(any(), any(), any());
    }

}
