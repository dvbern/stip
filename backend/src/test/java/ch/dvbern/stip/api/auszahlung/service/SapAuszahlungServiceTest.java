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

package ch.dvbern.stip.api.auszahlung.service;

import java.io.IOException;
import java.math.BigDecimal;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.sap.generated.importstatus.ImportStatusReadResponse;
import ch.dvbern.stip.api.sap.service.SapAuszahlungService;
import ch.dvbern.stip.api.sap.service.SapEndpointService;
import ch.dvbern.stip.api.sap.service.endpoints.clients.BusinessPartnerChangeClient;
import ch.dvbern.stip.api.sap.service.endpoints.clients.BusinessPartnerCreateClient;
import ch.dvbern.stip.api.sap.service.endpoints.clients.BusinessPartnerReadClient;
import ch.dvbern.stip.api.sap.service.endpoints.clients.ImportStatusReadClient;
import ch.dvbern.stip.api.sap.service.endpoints.clients.VendorPostingCreateClient;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import static ch.dvbern.stip.api.generator.entities.service.AuszahlungGenerator.initAuszahlung;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class SapAuszahlungServiceTest {
    private static final BigDecimal EXAMPLE_DELIVERY_ID = BigDecimal.valueOf(2761);
    private static final Integer EXAMPLE_BUSINESS_PARTNER_ID = 2762;
    @RestClient
    @InjectMock
    ImportStatusReadClient importStatusReadClient;

    @RestClient
    @InjectMock
    BusinessPartnerCreateClient businessPartnerCreateClient;

    @RestClient
    @InjectMock
    BusinessPartnerChangeClient businessPartnerChangeClient;

    @RestClient
    @InjectMock
    VendorPostingCreateClient vendorPostingCreateClient;

    @RestClient
    @InjectMock
    BusinessPartnerReadClient businessPartnerReadClient;

    @Inject
    SapEndpointService auszahlungSapService;

    @Inject
    SapAuszahlungService sapAuszahlungService;

    @BeforeEach
    void setUp() throws IOException {
        String importStatusXmlResponse = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/getImportStatusExampleResponse.xml"),
            "UTF-8"
        );
        String readBusinessPartnerResponse = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/readBusinessPartnerExampleResponse.xml"),
            "UTF-8"
        );
        when(importStatusReadClient.getImportStatus(any())).thenReturn(importStatusXmlResponse);
        when(businessPartnerReadClient.readBusinessPartner(any())).thenReturn(readBusinessPartnerResponse);
    }

    void getImportStatus() throws IOException {
        // arrange
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/getImportStatusExampleResponse.xml"),
            "UTF-8"
        );
        when(importStatusReadClient.getImportStatus(any())).thenReturn(xml);
        // act
        final var response = auszahlungSapService.getImportStatus(EXAMPLE_DELIVERY_ID);
        final var responseDto = (ImportStatusReadResponse) response.getEntity();

        // assert
        assertEquals(HttpStatus.SC_OK, response.getStatus());
        assertEquals("S", responseDto.getRETURNCODE().get(0).getTYPE());
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    void createBusinessPartnerTest() throws IOException {
        /**
         * Important: it can take up to 48 hours until a newly created user will be set active in SAP!
         */
        // arrange
        String firstCreateResponse = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerSuccessResponse.xml"),
            "UTF-8"
        );
        String secondCreateResponse = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessParnterAlreadyExistingPartnerResponse.xml"),
            "UTF-8"
        );
        Auszahlung auszahlung = initAuszahlung();
        auszahlung.setSapBusinessPartnerId(null);
        when(businessPartnerCreateClient.createBusinessPartner(any()))
            .thenReturn(firstCreateResponse)
            .thenReturn(secondCreateResponse);

        // act & assert
        assertNull(sapAuszahlungService.getOrCreateBusinessPartner(auszahlung));
    }

    @Test
    void createBusinessPartnerTest_invalidRequest() {
        // arrange
        Auszahlung auszahlung = initAuszahlung();
        auszahlung.setSapBusinessPartnerId(null);
        when(businessPartnerCreateClient.createBusinessPartner(any())).thenThrow(WebApplicationException.class);

        // act & assert
        assertNull(sapAuszahlungService.getOrCreateBusinessPartner(auszahlung));
    }

    @Test
    void updateBusinessPartnerTest_Success() throws IOException {
        // arrange
        String createResponse = IOUtils.toString(
            this.getClass()
                .getResourceAsStream("/auszahlung/changeBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerChangeClient.changeBusinessPartner(any())).thenReturn(createResponse);
        Auszahlung auszahlung = initAuszahlung();
        auszahlung.setSapBusinessPartnerId(EXAMPLE_BUSINESS_PARTNER_ID);

        // act & assert
        assertNotNull(sapAuszahlungService.getOrCreateBusinessPartner(auszahlung));
    }

    @Test
    void updateBusinessPartnerTest_invalidRequest(){
        //arrange
        when(businessPartnerChangeClient.changeBusinessPartner(any())).thenThrow(WebApplicationException.class);
        Auszahlung auszahlung= initAuszahlung();
        auszahlung.setSapBusinessPartnerId(EXAMPLE_BUSINESS_PARTNER_ID);

        //act & assert
        assertThrows(WebApplicationException.class, () -> {sapAuszahlungService.getOrCreateBusinessPartner(auszahlung);});
    }

    @Test
    void createVendorPostingTest_Success() throws IOException {
        // arrange
        String existingBusinessPartnerResonse = IOUtils.toString(
            this.getClass()
                .getResourceAsStream("/auszahlung/changeBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerChangeClient.changeBusinessPartner(any())).thenReturn(existingBusinessPartnerResonse);
        String vendorPostingCreateResponse = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/vendorPostingCreateSuccess.xml"),
            "UTF-8"
        );
        when(vendorPostingCreateClient.createVendorPosting(any())).thenReturn(vendorPostingCreateResponse);
        Auszahlung auszahlung = initAuszahlung();
        auszahlung.setSapBusinessPartnerId(EXAMPLE_BUSINESS_PARTNER_ID);

        // act & assert
        getImportStatus();
        assertNotNull(sapAuszahlungService.createVendorPosting(auszahlung));
    }

    @Test
    void createVendorPostingTest_alreadyExistingPayment() throws IOException {
        // arrange
        String existingBusinessPartnerResonse = IOUtils.toString(
            this.getClass()
                .getResourceAsStream("/auszahlung/changeBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerChangeClient.changeBusinessPartner(any())).thenReturn(existingBusinessPartnerResonse);
        String vendorPostingCreateResponse = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/vendorPostingCreateSuccess.xml"),
            "UTF-8"
        );
        when(vendorPostingCreateClient.createVendorPosting(any())).thenReturn(vendorPostingCreateResponse);
        Auszahlung auszahlung = initAuszahlung();
        auszahlung.setSapBusinessPartnerId(EXAMPLE_BUSINESS_PARTNER_ID);

        // act & assert
        getImportStatus();
        assertNotNull(sapAuszahlungService.createVendorPosting(auszahlung));
    }

    @Test
    void createVendorPostingTest_alreadyExistingDeliveryId() throws IOException {
        // arrange
        String existingBusinessPartnerResonse = IOUtils.toString(
            this.getClass()
                .getResourceAsStream("/auszahlung/changeBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerChangeClient.changeBusinessPartner(any())).thenReturn(existingBusinessPartnerResonse);
        String vendorPostingCreateResponse = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/vendorPostingAlreadyExistingDeliveryResponse.xml"),
            "UTF-8"
        );
        when(vendorPostingCreateClient.createVendorPosting(any())).thenReturn(vendorPostingCreateResponse);
        Auszahlung auszahlung = initAuszahlung();
        auszahlung.setSapBusinessPartnerId(EXAMPLE_BUSINESS_PARTNER_ID);

        // act & assert
        // todo KSTIP-1229: how to handle no action events?
        assertDoesNotThrow(() -> {
            sapAuszahlungService.createVendorPosting(auszahlung);
        });
    }

    @Test
    void createVendorPostingTest_invalidRequest() throws IOException {
        // arrange
        String existingBusinessPartnerResonse = IOUtils.toString(
            this.getClass()
                .getResourceAsStream("/auszahlung/changeBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        Auszahlung auszahlung = initAuszahlung();
        auszahlung.setSapBusinessPartnerId(EXAMPLE_BUSINESS_PARTNER_ID);
        when(businessPartnerChangeClient.changeBusinessPartner(any())).thenReturn(existingBusinessPartnerResonse);
        when(vendorPostingCreateClient.createVendorPosting(any())).thenThrow(WebApplicationException.class);

        // act & assert
        assertThrows(WebApplicationException.class, () -> {
            sapAuszahlungService.createVendorPosting(auszahlung);
        });
    }

}
