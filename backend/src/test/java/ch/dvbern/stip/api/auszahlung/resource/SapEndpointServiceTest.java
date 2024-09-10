package ch.dvbern.stip.api.auszahlung.resource;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.generator.entities.service.AuszahlungGenerator;
import ch.dvbern.stip.api.sap.service.endpoints.businesspartner.create.BusinessPartnerCreateResponse;
import ch.dvbern.stip.api.sap.service.endpoints.clients.BusinessPartnerChangeClient;
import ch.dvbern.stip.api.sap.service.endpoints.clients.BusinessPartnerCreateClient;
import ch.dvbern.stip.api.sap.service.endpoints.clients.ImportStatusReadClient;
import ch.dvbern.stip.api.sap.service.endpoints.clients.VendorPostingCreateClient;
import ch.dvbern.stip.api.sap.service.endpoints.importstatus.ImportStatusReadResponse;
import ch.dvbern.stip.api.auszahlung.service.*;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.sap.service.*;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.WebApplicationException;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class SapEndpointServiceTest {
    private static final BigDecimal EXAMPLE_DELIVERY_ID = BigDecimal.valueOf(2761);
    private static final Integer EXAMPLE_BUSINESS_PARTNER_ID = 2762;
    private static final String EXAMPLE_EXTERNAL_ID = UUID.randomUUID().toString();
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

    @Inject
    SapEndpointService auszahlungSapService;
    @Inject AuszahlungMapper auszahlungMapper;

    private static final BigDecimal DELIVERY_ID = EXAMPLE_DELIVERY_ID;

    @TestAsSachbearbeiter
    @Test
    void getImportStatusTest() throws IOException {
        //arrange
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/getImportStatusExampleResponse.xml"),
            "UTF-8"
        );
        when(importStatusReadClient.getImportStatus(any())).thenReturn(xml);

        //act
        final var response = auszahlungSapService.getImportStatus(EXAMPLE_DELIVERY_ID);
        final var responseDto = (ImportStatusReadResponse) response.getEntity();

        //assert
        assertEquals(HttpStatus.SC_OK,response.getStatus());
        assertEquals("S",responseDto.getRETURNCODE().get(0).getTYPE());
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @TestAsSachbearbeiter
    @Test
    void getImportStatusInvalidDeliveryIdTest(){
        //arrange
        when(importStatusReadClient.getImportStatus(any())).thenThrow(WebApplicationException.class);
        // act & assert
        assertThrows(ConstraintViolationException.class, () -> auszahlungSapService.getImportStatus(BigDecimal.valueOf(-1)));
        assertThrows(ConstraintViolationException.class, () -> auszahlungSapService.getImportStatus(BigDecimal.valueOf(0)));
        assertThrows(ConstraintViolationException.class, () -> auszahlungSapService.getImportStatus(null));
    }

    @TestAsSachbearbeiter
    @Test
    void getImportStatusDeliveryIdNotFoundTest() throws IOException {
        //arrange
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/getImportStatusDeliveryIdNotFoundExampleResponse.xml"),
            "UTF-8"
        );
        when(importStatusReadClient.getImportStatus(any())).thenReturn(xml);

        //act
        final var response = auszahlungSapService.getImportStatus(EXAMPLE_DELIVERY_ID);
        final var responseDto = (ImportStatusReadResponse) response.getEntity();

        //assert
        assertEquals(HttpStatus.SC_OK,response.getStatus());
        assertEquals("E",responseDto.getRETURNCODE().get(0).getTYPE());
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    void createBusinessPartnerSucessTest() throws IOException {
        //arrange
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerSuccessResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerCreateClient.createBusinessPartner(any())).thenReturn(xml);
        //act
        final var response = auszahlungSapService.createBusinessPartner(AuszahlungGenerator.initAuszahlung(),EXAMPLE_EXTERNAL_ID,DELIVERY_ID);
        //assert
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    void createBusinessPartnerAlreadyExistingParnterTest() throws IOException {
        //arrange
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessParnterAlreadyExistingPartnerResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerCreateClient.createBusinessPartner(any())).thenReturn(xml);
        //act
        final var response = auszahlungSapService.createBusinessPartner(AuszahlungGenerator.initAuszahlung(),EXAMPLE_EXTERNAL_ID,DELIVERY_ID);
        final var businessPartnerId = ((BusinessPartnerCreateResponse) response.getEntity()).getBUSINESSPARTNER().getHEADER().getBPARTNER();

        //assert
        assertNotNull(businessPartnerId);
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    void createBusinessPartnerAlreadyExistingDeliveryIdTest() throws IOException {
        //arrange
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerCreateClient.createBusinessPartner(any())).thenReturn(xml);

        //act
        final var response = auszahlungSapService.createBusinessPartner(AuszahlungGenerator.initAuszahlung(),EXAMPLE_EXTERNAL_ID,DELIVERY_ID);

        //assert
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    void createBusinessPartnerInvalidDtoTest() throws IOException {
        //arrange
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerCreateClient.createBusinessPartner(any())).thenReturn(xml);

        //act
        final var response = auszahlungSapService.createBusinessPartner(AuszahlungGenerator.initAuszahlung(),EXAMPLE_EXTERNAL_ID,DELIVERY_ID);

        //assert
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    void changeBusinessPartnerAlreadyExistingDeliveryIdTest() throws IOException {
        //arrange
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/changeBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        final Auszahlung requestDto1 = AuszahlungGenerator.initAuszahlung();
        requestDto1.setAdresse(null);
        when(businessPartnerChangeClient.changeBusinessPartner(any())).thenReturn(xml);

        //act
        final var response = auszahlungSapService.changeBusinessPartner(AuszahlungGenerator.initAuszahlung(),EXAMPLE_BUSINESS_PARTNER_ID,DELIVERY_ID);

        //assert
        assertThrows(ConstraintViolationException.class, () -> auszahlungSapService.changeBusinessPartner(requestDto1,EXAMPLE_BUSINESS_PARTNER_ID,DELIVERY_ID));
        assertEquals(HttpStatus.SC_OK, response.getStatus());

    }

    @Test
    void changeBusinessPartnerSuccessTest() throws IOException {
        //arrange
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/changeBusinessPartnerSuccessResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerChangeClient.changeBusinessPartner(any())).thenReturn(xml);
        Auszahlung requestDto1 = AuszahlungGenerator.initAuszahlung();

        //act
        final var response = auszahlungSapService.changeBusinessPartner(requestDto1,EXAMPLE_BUSINESS_PARTNER_ID,DELIVERY_ID);

        //assert
        assertEquals(HttpStatus.SC_OK, response.getStatus());

    }

    @Test
    void createAuszahlungInvalidDtoTest() throws IOException {
        //arrange
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/vendorPostingAlreadyExistingDeliveryResponse.xml"),
            "UTF-8"
        );
        final Auszahlung requestDto1 = AuszahlungGenerator.initAuszahlung();
        requestDto1.setAdresse(null);
        when(vendorPostingCreateClient.createVendorPosting(any())).thenReturn(xml);

        //act
        final var response = auszahlungSapService.createVendorPosting(AuszahlungGenerator.initAuszahlung(),EXAMPLE_BUSINESS_PARTNER_ID,DELIVERY_ID);

        //assert
        assertThrows(ConstraintViolationException.class, () -> auszahlungSapService.createVendorPosting(requestDto1,EXAMPLE_BUSINESS_PARTNER_ID,DELIVERY_ID));
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    void createAuszahlungTest() throws IOException {
        //arrange
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/vendorPostingCreateSuccess.xml"),
            "UTF-8"
        );
        when(vendorPostingCreateClient.createVendorPosting(any())).thenReturn(xml);

        //act
        final var response = auszahlungSapService.createVendorPosting(AuszahlungGenerator.initAuszahlung(),EXAMPLE_BUSINESS_PARTNER_ID,DELIVERY_ID);

        //assert
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }



}
