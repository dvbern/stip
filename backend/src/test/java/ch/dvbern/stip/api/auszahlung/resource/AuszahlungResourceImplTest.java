package ch.dvbern.stip.api.auszahlung.resource;

import ch.dvbern.stip.api.auszahlung.sap.businesspartner.create.BusinessPartnerCreateRequest;
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
    AuszahlungSapService auszahlungSapService;

    private static final BigDecimal DELIVERY_ID = BigDecimal.valueOf(EXAMPLE_DELIVERY_ID);


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
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        final AuszahlungDto requestDto1 = initAuszahlungDto();
        //assertThrows(ConstraintViolationException.class, () -> auszahlungSapService.createBusinessPartner(requestDto1,DELIVERY_ID));

        when(businessPartnerCreateClient.createBusinessPartner(any())).thenReturn(xml);
        final var response = auszahlungSapService.createBusinessPartner(initAuszahlungDto(),DELIVERY_ID);
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    @TestAsSachbearbeiter
    void changeBusinessPartnerInvalidDtoTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        final AuszahlungDto requestDto1 = initAuszahlungDto();
        requestDto1.setAdresse(null);
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

   // @Test
    //@TestAsGesuchsteller
    void createAuszahlungAsGSTest(){
        when(vendorPostingCreateClient.createVendorPosting(any())).thenReturn("");
        //assertThrows(ForbiddenException.class, () -> auszahlungSapService.createVendorPosting(initAuszahlungDto(),DELIVERY_ID));
    }

    @Test
    @TestAsSachbearbeiter
    void createAuszahlungInvalidDtoTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        final AuszahlungDto requestDto1 = initAuszahlungDto();
        requestDto1.setAdresse(null);
        //assertThrows(ConstraintViolationException.class, () -> auszahlungSapService.createVendorPosting(null,DELIVERY_ID));
        assertThrows(ConstraintViolationException.class, () -> auszahlungSapService.createVendorPosting(requestDto1,DELIVERY_ID));

        when(vendorPostingCreateClient.createVendorPosting(any())).thenReturn(xml);
        final var response = auszahlungSapService.createVendorPosting(initAuszahlungDto(),DELIVERY_ID);
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
        final var response = auszahlungSapService.createVendorPosting(initAuszahlungDto(),DELIVERY_ID);
        assertEquals(HttpStatus.SC_OK, response.getStatus());
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
