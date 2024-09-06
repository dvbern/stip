package ch.dvbern.stip.api.auszahlung.resource;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.create.BusinessPartnerCreateRequest;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.create.BusinessPartnerCreateResponse;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.create.SenderParmsDelivery;
import ch.dvbern.stip.api.auszahlung.sap.importstatus.ImportStatusReadResponse;
import ch.dvbern.stip.api.auszahlung.service.*;
import ch.dvbern.stip.api.auszahlung.type.Kontoinhaber;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.stammdaten.type.Land;
import ch.dvbern.stip.generated.dto.*;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Request;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
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

    private static final BigDecimal DELIVERY_ID = EXAMPLE_DELIVERY_ID;

    @TestAsSachbearbeiter
    @Test
    void getImportStatusTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/getImportStatusExampleResponse.xml"),
            "UTF-8"
        );
        when(importStatusReadClient.getImportStatus(any())).thenReturn(xml);
        final var response = auszahlungSapService.getImportStatus(EXAMPLE_DELIVERY_ID);
        assertEquals(HttpStatus.SC_OK,response.getStatus());
        final var responseDto = (ImportStatusReadResponse) response.getEntity();
        assertEquals("S",responseDto.getRETURNCODE().get(0).getTYPE());
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @TestAsSachbearbeiter
    @Test
    void getImportStatusInvalidDeliveryIdTest(){
        when(importStatusReadClient.getImportStatus(any())).thenThrow(WebApplicationException.class);
        assertThrows(ConstraintViolationException.class, () -> auszahlungSapService.getImportStatus(BigDecimal.valueOf(-1)));
        assertThrows(ConstraintViolationException.class, () -> auszahlungSapService.getImportStatus(BigDecimal.valueOf(0)));
        assertThrows(ConstraintViolationException.class, () -> auszahlungSapService.getImportStatus(null));
    }

    @TestAsSachbearbeiter
    @Test
    void getImportStatusDeliveryIdNotFoundTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/getImportStatusDeliveryIdNotFoundExampleResponse.xml"),
            "UTF-8"
        );
        when(importStatusReadClient.getImportStatus(any())).thenReturn(xml);
        final var response = auszahlungSapService.getImportStatus(EXAMPLE_DELIVERY_ID);
        assertEquals(HttpStatus.SC_OK,response.getStatus());
        final var responseDto = (ImportStatusReadResponse) response.getEntity();
        assertEquals("E",responseDto.getRETURNCODE().get(0).getTYPE());
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    void createBusinessPartnerSucessTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerSuccessResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerCreateClient.createBusinessPartner(any())).thenReturn(xml);
        final var response = auszahlungSapService.createBusinessPartner(initAuszahlung(),EXAMPLE_EXTERNAL_ID,DELIVERY_ID);
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    void createBusinessPartnerAlreadyExistingParnterTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessParnterAlreadyExistingPartnerResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerCreateClient.createBusinessPartner(any())).thenReturn(xml);
        final var response = auszahlungSapService.createBusinessPartner(initAuszahlung(),EXAMPLE_EXTERNAL_ID,DELIVERY_ID);
        final var businessPartnerId = ((BusinessPartnerCreateResponse) response.getEntity()).getBUSINESSPARTNER().getHEADER().getBPARTNER();
        assertNotNull(businessPartnerId);
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    void createBusinessPartnerAlreadyExistingDeliveryIdTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerCreateClient.createBusinessPartner(any())).thenReturn(xml);
        final var response = auszahlungSapService.createBusinessPartner(initAuszahlung(),EXAMPLE_EXTERNAL_ID,DELIVERY_ID);
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    void createBusinessPartnerInvalidDtoTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerCreateClient.createBusinessPartner(any())).thenReturn(xml);
        final var response = auszahlungSapService.createBusinessPartner(initAuszahlung(),EXAMPLE_EXTERNAL_ID,DELIVERY_ID);
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    void changeBusinessPartnerInvalidDtoTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/changeBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        final Auszahlung requestDto1 = initAuszahlung();
        requestDto1.setAdresse(null);
        assertThrows(ConstraintViolationException.class, () -> auszahlungSapService.changeBusinessPartner(requestDto1,EXAMPLE_BUSINESS_PARTNER_ID,DELIVERY_ID));
        when(businessPartnerChangeClient.changeBusinessPartner(any())).thenReturn(xml);
        final var response = auszahlungSapService.changeBusinessPartner(initAuszahlung(),EXAMPLE_BUSINESS_PARTNER_ID,DELIVERY_ID);
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    void changeBusinessPartnerSuccessTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/changeBusinessPartnerSuccessResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerChangeClient.changeBusinessPartner(any())).thenReturn(xml);
        Auszahlung requestDto1 = initAuszahlung();
        final var response = auszahlungSapService.changeBusinessPartner(requestDto1,EXAMPLE_BUSINESS_PARTNER_ID,DELIVERY_ID);
        assertEquals(HttpStatus.SC_OK, response.getStatus());

    }

    @Test
    void createAuszahlungInvalidDtoTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/vendorPostingAlreadyExistingDeliveryResponse.xml"),
            "UTF-8"
        );
        final Auszahlung requestDto1 = initAuszahlung();
        requestDto1.setAdresse(null);
        assertThrows(ConstraintViolationException.class, () -> auszahlungSapService.createVendorPosting(requestDto1,EXAMPLE_BUSINESS_PARTNER_ID,DELIVERY_ID));

        when(vendorPostingCreateClient.createVendorPosting(any())).thenReturn(xml);
        final var response = auszahlungSapService.createVendorPosting(initAuszahlung(),EXAMPLE_BUSINESS_PARTNER_ID,DELIVERY_ID);
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    void createAuszahlungTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/vendorPostingCreateSuccess.xml"),
            "UTF-8"
        );
        when(vendorPostingCreateClient.createVendorPosting(any())).thenReturn(xml);
        final var response = auszahlungSapService.createVendorPosting(initAuszahlung(),EXAMPLE_BUSINESS_PARTNER_ID,DELIVERY_ID);
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Inject AuszahlungMapper auszahlungMapper;
    private Auszahlung initAuszahlung(){
        AuszahlungDto auszahlungDto = new AuszahlungDto();
        auszahlungDto.setKontoinhaber(Kontoinhaber.GESUCHSTELLER);
        auszahlungDto.setVorname("Brigitta1111111");
        auszahlungDto.setNachname("Flückke11111111");
        auszahlungDto.setIban("CH2089144635452242312");
        AdresseDto adresseDto = new AdresseDto();
        adresseDto.setStrasse("Bundesstadtstrasse");
        adresseDto.setOrt("Hauptstadtort");
        adresseDto.setPlz("9299");
        adresseDto.setLand(Land.CH);
        adresseDto.setHausnummer("9298");
        auszahlungDto.setAdresse(adresseDto);
        auszahlungDto.setBusinessPartnerId(1427);
        return auszahlungMapper.toEntity(auszahlungDto);
    }


}
