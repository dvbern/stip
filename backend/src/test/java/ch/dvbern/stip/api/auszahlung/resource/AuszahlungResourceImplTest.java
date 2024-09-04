package ch.dvbern.stip.api.auszahlung.resource;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.create.BusinessPartnerCreateRequest;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.create.BusinessPartnerCreateRequestMapper;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.create.SenderParmsDelivery;
import ch.dvbern.stip.api.auszahlung.sap.importstatus.ImportStatusReadResponse;
import ch.dvbern.stip.api.auszahlung.service.*;
import ch.dvbern.stip.api.auszahlung.service.sap.SAPUtils;
import ch.dvbern.stip.api.auszahlung.service.sap.SapMessageType;
import ch.dvbern.stip.api.auszahlung.type.Kontoinhaber;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.stammdaten.type.Land;
import ch.dvbern.stip.generated.dto.*;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.WebApplicationException;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;
import io.quarkus.security.ForbiddenException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class AuszahlungResourceImplTest {
    private static final Integer EXAMPLE_DELIVERY_ID = 2761;
    private static final Integer EXAMPLE_EXT_ID = 889994;

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

    @InjectMock
    @RestClient
    SearchBusinessPartnerClient searchBusinessPartnerClient;

    @Inject
    AuszahlungSapService auszahlungSapService;

    private static final BigDecimal DELIVERY_ID = BigDecimal.valueOf(EXAMPLE_DELIVERY_ID);

    @TestAsSachbearbeiter
    @Test
    void triggerAuszahlungNonExistingBusinessPartner_Privatperson() {
        AuszahlungDto auszahlungDto = initAuszahlungDto();
        final var searchResult = auszahlungSapService.searchBusinessPartner(auszahlungDto);
        fail("Not implemented yet");
    }

    @TestAsSachbearbeiter
    @Test
    void triggerAuszahlungExistingBusinessPartner_Privatperson() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/BusinessPartnerSearchExampleResponse.xml"),
            "UTF-8"
        );
        when(searchBusinessPartnerClient.searchBusinessPartner(any())).thenReturn(xml);
        AuszahlungDto auszahlungDto = initAuszahlungDto();

        final var searchResult = auszahlungSapService.searchBusinessPartner(auszahlungDto);
        fail("Not implemented yet");

    }

    @TestAsSachbearbeiter
    @Test
    void triggerAuszahlungNonExistingBusinessPartner_Organisation() {
        AuszahlungDto auszahlungDto = initAuszahlungDto();
        fail("Not implemented yet");

    }

    @TestAsSachbearbeiter
    @Test
    void triggerAuszahlungExistingBusinessPartner_Organisation() {
        AuszahlungDto auszahlungDto = initAuszahlungDto();
        fail("Not implemented yet");
    }

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
        assertThrows(ConstraintViolationException.class, () -> auszahlungSapService.getImportStatus(-1));
        assertThrows(ConstraintViolationException.class, () -> auszahlungSapService.getImportStatus(0));
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
    //@TestAsGesuchsteller
    void createBusinessPartnerAsGSTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerSuccessResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerCreateClient.createBusinessPartner(any())).thenReturn(xml);
        assertThrows(ForbiddenException.class, () -> auszahlungSapService.createBusinessPartner(initAuszahlungDto(),DELIVERY_ID));
    }


    private BusinessPartnerCreateRequest initBusinessPartnerCreateRequest() {
        BusinessPartnerCreateRequest request = new BusinessPartnerCreateRequest();
        BusinessPartnerCreateRequest.BUSINESSPARTNER businessPartner = new BusinessPartnerCreateRequest.BUSINESSPARTNER();
        BusinessPartnerCreateRequest.BUSINESSPARTNER.HEADER header = new BusinessPartnerCreateRequest.BUSINESSPARTNER.HEADER();
        businessPartner.setHEADER(header);
        BusinessPartnerCreateRequest.BUSINESSPARTNER.IDKEYS idkeys = new BusinessPartnerCreateRequest.BUSINESSPARTNER.IDKEYS();
        businessPartner.setIDKEYS(idkeys);
        request.setBUSINESSPARTNER(businessPartner);
        ch.dvbern.stip.api.auszahlung.sap.businesspartner.create.SenderParmsDelivery senderParms = new SenderParmsDelivery();
        senderParms.setSYSID(BigInteger.valueOf(2080));
        senderParms.setDELIVERYID(BigDecimal.valueOf(1122339312));
        request.setSENDER(senderParms);
        return request;
    }

    @Test
    //@TestAsSachbearbeiter
    void createBusinessPartnerSucessTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerSuccessResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerCreateClient.createBusinessPartner(any())).thenReturn(xml);

        final var response = auszahlungSapService.createBusinessPartner(initAuszahlungDto(),DELIVERY_ID);
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    //@TestAsSachbearbeiter
    void createBusinessPartnerAlreadyExistingParnterTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessParnterAlreadyExistingPartnerResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerCreateClient.createBusinessPartner(any())).thenReturn(xml);
        final var response = auszahlungSapService.createBusinessPartner(initAuszahlungDto(),DELIVERY_ID);
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    //@TestAsSachbearbeiter
    void createBusinessPartnerAlreadyExistingDeliveryIdTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerCreateClient.createBusinessPartner(any())).thenReturn(xml);
        final var response = auszahlungSapService.createBusinessPartner(initAuszahlungDto(),DELIVERY_ID);
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    //@TestAsSachbearbeiter
    void createBusinessPartnerInvalidDtoTest() throws IOException {
        //todo: rewrite test
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        final AuszahlungDto requestDto1 = initAuszahlungDto();
        assertThrows(ConstraintViolationException.class, () -> auszahlungSapService.createBusinessPartner(requestDto1,DELIVERY_ID));

        when(businessPartnerCreateClient.createBusinessPartner(any())).thenReturn(xml);
        final var response = auszahlungSapService.createBusinessPartner(initAuszahlungDto(),DELIVERY_ID);
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    @TestAsGesuchsteller
    void changeBusinessPartnerAsGSTest(){
        when(businessPartnerChangeClient.changeBusinessPartner(any())).thenReturn("");
        assertThrows(ForbiddenException.class, () -> auszahlungSapService.changeBusinessPartner(initAuszahlungDto(),DELIVERY_ID));
    }

    @Test
    @TestAsSachbearbeiter
    void changeBusinessPartnerInvalidDtoTest() throws IOException {
        String xml = IOUtils.toString(//todo: add proper file & rewrite test
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        final AuszahlungDto requestDto1 = initAuszahlungDto();
        requestDto1.setAdresse(null);
        assertThrows(ConstraintViolationException.class, () -> auszahlungSapService.changeBusinessPartner(null,DELIVERY_ID));
        assertThrows(ConstraintViolationException.class, () -> auszahlungSapService.changeBusinessPartner(requestDto1,DELIVERY_ID));
        when(businessPartnerChangeClient.changeBusinessPartner(any())).thenReturn(xml);
        final var response = auszahlungSapService.changeBusinessPartner(initAuszahlungDto(),DELIVERY_ID);
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    @TestAsSachbearbeiter
    void changeBusinessPartnerSuccessTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/changeBusinessPartnerSuccessResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerChangeClient.changeBusinessPartner(any())).thenReturn(xml);
        final var response = auszahlungSapService.changeBusinessPartner(initAuszahlungDto(),DELIVERY_ID);
        assertEquals(HttpStatus.SC_OK, response.getStatus());

    }

    @Test
    @TestAsGesuchsteller
    void createAuszahlungAsGSTest(){
        when(vendorPostingCreateClient.createVendorPosting(any())).thenReturn("");
        assertThrows(ForbiddenException.class, () -> auszahlungSapService.createAuszahlung(initAuszahlungDto(),DELIVERY_ID));
    }

    @Test
    @TestAsSachbearbeiter
    void createAuszahlungInvalidDtoTest() throws IOException {
        String xml = IOUtils.toString(//todo: add proper file & rewrite test
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        final AuszahlungDto requestDto1 = initAuszahlungDto();
        requestDto1.setAdresse(null);
        assertThrows(ConstraintViolationException.class, () -> auszahlungSapService.createAuszahlung(null,DELIVERY_ID));
        assertThrows(ConstraintViolationException.class, () -> auszahlungSapService.createAuszahlung(requestDto1,DELIVERY_ID));

        when(vendorPostingCreateClient.createVendorPosting(any())).thenReturn(xml);
        final var response = auszahlungSapService.createAuszahlung(initAuszahlungDto(),DELIVERY_ID);
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    @TestAsSachbearbeiter
    void createAuszahlungTest() throws IOException { //todo: add proper xml
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerSuccessResponse.xml"),
            "UTF-8"
        );
        when(vendorPostingCreateClient.createVendorPosting(any())).thenReturn(xml);
        final var response = auszahlungSapService.createAuszahlung(initAuszahlungDto(),DELIVERY_ID);
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }



    @Test
    void searchExistingBusinessPartnerTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/BusinessPartnerSearchExampleResponse.xml"),
            "UTF-8"
        );
        when(searchBusinessPartnerClient.searchBusinessPartner(any())).thenReturn(xml);
        AuszahlungDto auszahlungDto = initAuszahlungDto();
        final var response = auszahlungSapService.searchBusinessPartner(auszahlungDto);
        assertEquals(HttpStatus.SC_OK, response.getStatus());
        SapMessageType messageType = SAPUtils.parseSapMessageType((String) response.getEntity());
        assertEquals(SapMessageType.S, messageType);
        String businessPartnerId = SAPUtils.parseBusinessPartnerId((String) response.getEntity());
        assertNotNull(businessPartnerId);
    }

    @Test
    void searchNonExistingBusinessPartnerTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/SearchBusinessParnterNonExistingResponse.xml"),
            "UTF-8"
        );
        when(searchBusinessPartnerClient.searchBusinessPartner(any())).thenReturn(xml);
        AuszahlungDto auszahlungDto = initAuszahlungDto();
        auszahlungDto.setVorname("xyz");
        final var response = auszahlungSapService.searchBusinessPartner(auszahlungDto);
        assertEquals(HttpStatus.SC_OK, response.getStatus());
        SapMessageType messageType = SAPUtils.parseSapMessageType((String) response.getEntity());
        assertEquals(SapMessageType.E, messageType);
        String businessPartnerId = SAPUtils.parseBusinessPartnerId((String) response.getEntity());
        assertNull(businessPartnerId);
    }
    private AuszahlungDto initAuszahlungDto(){
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
        return auszahlungDto;
    }


}
