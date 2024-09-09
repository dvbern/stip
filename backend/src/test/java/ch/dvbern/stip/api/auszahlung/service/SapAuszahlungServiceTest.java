package ch.dvbern.stip.api.auszahlung.service;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.sap.importstatus.ImportStatusReadResponse;
import ch.dvbern.stip.api.auszahlung.type.Kontoinhaber;
import ch.dvbern.stip.api.stammdaten.type.Land;
import ch.dvbern.stip.generated.dto.AdresseDto;
import ch.dvbern.stip.generated.dto.AuszahlungDto;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    void createBusinessPartnerTest() throws IOException {
        /**
         * Important: in order to create a new (non-existing in SAP) businesspartner,
         * the "Create"-Endpoint has to be called twice!
         * first call: create businesspartner
         * second call: extract busniesspartner-id from response in order to proceed
         */
        String firstCreateResponse = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerSuccessResponse.xml"),
            "UTF-8"
        );
        String secondCreateResponse = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessParnterAlreadyExistingPartnerResponse.xml"),
            "UTF-8"
        );

        when(businessPartnerCreateClient.createBusinessPartner(any()))
            .thenReturn(firstCreateResponse)
            .thenReturn(secondCreateResponse);
        assertNull(sapAuszahlungService.getOrCreateBusinessPartner(initAuszahlung()));
    }

    @Test
    void createBusinessPartnerTest_invalidRequest() {
        when(businessPartnerCreateClient.createBusinessPartner(any())).thenThrow(WebApplicationException.class);
        assertNull(sapAuszahlungService.getOrCreateBusinessPartner(initAuszahlung()));
    }

    @Test
    void updateBusinessPartnerTest_Success() throws IOException {
        String createResponse = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/changeBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerChangeClient.changeBusinessPartner(any())).thenReturn(createResponse);
        Auszahlung auszahlung= initAuszahlung();
        auszahlung.setSapBusinessPartnerId(EXAMPLE_BUSINESS_PARTNER_ID);
        assertNotNull(sapAuszahlungService.getOrCreateBusinessPartner(auszahlung));
    }

    @Test
    void updateBusinessPartnerTest_invalidRequest(){
        when(businessPartnerChangeClient.changeBusinessPartner(any())).thenThrow(WebApplicationException.class);
        Auszahlung auszahlung= initAuszahlung();
        auszahlung.setSapBusinessPartnerId(EXAMPLE_BUSINESS_PARTNER_ID);
        assertThrows(WebApplicationException.class, () -> {sapAuszahlungService.getOrCreateBusinessPartner(auszahlung);});
    }

    @Test
    void createVendorPostingTest_Success() throws IOException {
        String existingBusinessPartnerResonse = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/changeBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerChangeClient.changeBusinessPartner(any())).thenReturn(existingBusinessPartnerResonse);
        String vendorPostingCreateResponse = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/vendorPostingCreateSuccess.xml"),
            "UTF-8"
        );        when(vendorPostingCreateClient.createVendorPosting(any())).thenReturn(vendorPostingCreateResponse);
        Auszahlung auszahlung= initAuszahlung();
        auszahlung.setSapBusinessPartnerId(EXAMPLE_BUSINESS_PARTNER_ID);

        getImportStatus();
        assertNotNull(sapAuszahlungService.createVendorPosting(auszahlung));
    }

    @Test
    void createVendorPostingTest_alreadyExistingPayment() throws IOException {
        String existingBusinessPartnerResonse = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/changeBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerChangeClient.changeBusinessPartner(any())).thenReturn(existingBusinessPartnerResonse);
        String vendorPostingCreateResponse = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/vendorPostingCreateSuccess.xml"),
            "UTF-8"
        );        when(vendorPostingCreateClient.createVendorPosting(any())).thenReturn(vendorPostingCreateResponse);
        Auszahlung auszahlung= initAuszahlung();
        auszahlung.setSapBusinessPartnerId(EXAMPLE_BUSINESS_PARTNER_ID);

        getImportStatus();
        assertNotNull(sapAuszahlungService.createVendorPosting(auszahlung));
    }

    @Test
    void createVendorPostingTest_alreadyExistingDeliveryId() throws IOException {
        String existingBusinessPartnerResonse = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/changeBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerChangeClient.changeBusinessPartner(any())).thenReturn(existingBusinessPartnerResonse);
        String vendorPostingCreateResponse = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/vendorPostingAlreadyExistingDeliveryResponse.xml"),
            "UTF-8"
        );        when(vendorPostingCreateClient.createVendorPosting(any())).thenReturn(vendorPostingCreateResponse);
        Auszahlung auszahlung= initAuszahlung();
        auszahlung.setSapBusinessPartnerId(EXAMPLE_BUSINESS_PARTNER_ID);
        //assertThrows(WebApplicationException.class, () -> {sapAuszahlungService.createVendorPosting(auszahlung);});
        //todo: how to handle no action events?
        assertDoesNotThrow(() -> {sapAuszahlungService.createVendorPosting(auszahlung);});
    }

    @Test
    void createVendorPostingTest_invalidRequest() throws IOException {
        String existingBusinessPartnerResonse = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/changeBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        Auszahlung auszahlung= initAuszahlung();
        auszahlung.setSapBusinessPartnerId(EXAMPLE_BUSINESS_PARTNER_ID);
        when(businessPartnerChangeClient.changeBusinessPartner(any())).thenReturn(existingBusinessPartnerResonse);
        when(vendorPostingCreateClient.createVendorPosting(any())).thenThrow(WebApplicationException.class);

        assertThrows(WebApplicationException.class, () -> {sapAuszahlungService.createVendorPosting(auszahlung);});
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
