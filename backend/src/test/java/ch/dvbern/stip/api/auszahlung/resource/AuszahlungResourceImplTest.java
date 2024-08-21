package ch.dvbern.stip.api.auszahlung.resource;

import ch.dvbern.stip.api.auszahlung.service.*;
import ch.dvbern.stip.api.auszahlung.type.Kontoinhaber;
import ch.dvbern.stip.api.benutzer.util.TestAsGesuchsteller;
import ch.dvbern.stip.api.benutzer.util.TestAsSachbearbeiter;
import ch.dvbern.stip.api.stammdaten.type.Land;
import ch.dvbern.stip.generated.api.AuszahlungResource;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class AuszahlungResourceImplTest {
    private static final Integer EXAMPLE_DELIVERY_ID = 2761;
    private static final Integer EXAMPLE_EXT_ID = 889994;
    @Inject
    AuszahlungResource auszahlungResource;

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

    @TestAsSachbearbeiter
    @Test
    void getImportStatusTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/getImportStatusExampleResponse.xml"),
            "UTF-8"
        );
        when(importStatusReadClient.getImportStatus(any())).thenReturn(xml);
        final var response = auszahlungResource.getImportStatus(EXAMPLE_DELIVERY_ID);
        assertEquals(HttpStatus.SC_OK,response.getStatus());
        final var responseDto = (GetAuszahlungImportStatusResponseDto) response.getEntity();
        assertFalse(responseDto.getLogs().isEmpty());
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @TestAsSachbearbeiter
    @Test
    void getImportStatusInvalidDeliveryIdTest(){
        when(importStatusReadClient.getImportStatus(any())).thenThrow(WebApplicationException.class);
        assertThrows(ConstraintViolationException.class, () -> auszahlungResource.getImportStatus(-1));
        assertThrows(ConstraintViolationException.class, () -> auszahlungResource.getImportStatus(0));
        assertThrows(ConstraintViolationException.class, () -> auszahlungResource.getImportStatus(null));

    }

    @TestAsGesuchsteller
    @Test
    void getImportStatusTestAsGS() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/getImportStatusExampleResponse.xml"),
            "UTF-8"
        );
        when(importStatusReadClient.getImportStatus(any())).thenReturn(xml);
        assertThrows(ForbiddenException.class, () ->  auszahlungResource.getImportStatus(EXAMPLE_DELIVERY_ID));
    }

    @Test
    @TestAsGesuchsteller
    void createBusinessPartnerAsGSTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerSuccessResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerCreateClient.createBusinessPartner(any())).thenReturn(xml);
        assertThrows(ForbiddenException.class, () -> auszahlungResource.createKreditor(initCreateAuszahlungKreditorDto()));
    }

    @Test
    @TestAsSachbearbeiter
    void createBusinessPartnerSucessTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerSuccessResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerCreateClient.createBusinessPartner(any())).thenReturn(xml);
        final var response = auszahlungResource.createKreditor(initCreateAuszahlungKreditorDto());
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    @TestAsSachbearbeiter
    void createBusinessPartnerAlreadyExistingParnterTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessParnterAlreadyExistingPartnerResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerCreateClient.createBusinessPartner(any())).thenReturn(xml);
        final var response = auszahlungResource.createKreditor(initCreateAuszahlungKreditorDto());
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    @TestAsSachbearbeiter
    void createBusinessPartnerAlreadyExistingDeliveryIdTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerCreateClient.createBusinessPartner(any())).thenReturn(xml);
        final var response = auszahlungResource.createKreditor(initCreateAuszahlungKreditorDto());
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    @TestAsSachbearbeiter
    void createBusinessPartnerInvalidDtoTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        final CreateAuszahlungKreditorDto requestDto1 = initCreateAuszahlungKreditorDto();
        assertThrows(ConstraintViolationException.class, () -> auszahlungResource.createKreditor(null));
        requestDto1.setExtId(null);
        assertThrows(ConstraintViolationException.class, () -> auszahlungResource.createKreditor(requestDto1));

        final CreateAuszahlungKreditorDto requestDto2 = initCreateAuszahlungKreditorDto();
        requestDto2.setAuszahlung(null);
        assertThrows(ConstraintViolationException.class, () -> auszahlungResource.createKreditor(requestDto2));

        final CreateAuszahlungKreditorDto requestDto3 = initCreateAuszahlungKreditorDto();
        requestDto3.setDeliveryId(null);
        assertThrows(ConstraintViolationException.class, () -> auszahlungResource.createKreditor(requestDto3));

        when(businessPartnerCreateClient.createBusinessPartner(any())).thenReturn(xml);
        final var response = auszahlungResource.createKreditor(initCreateAuszahlungKreditorDto());
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    @TestAsGesuchsteller
    void changeBusinessPartnerAsGSTest(){
        when(businessPartnerChangeClient.changeBusinessPartner(any())).thenReturn("");
        assertThrows(ForbiddenException.class, () -> auszahlungResource.changeKreditor((initChangeAuszahlungKreditorDto())));
    }

    @Test
    @TestAsSachbearbeiter
    void changeBusinessPartnerInvalidDtoTest() throws IOException {
        String xml = IOUtils.toString(//todo: add proper file
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        final ChangeAuszahlungKreditorDto requestDto1 = initChangeAuszahlungKreditorDto();
        assertThrows(ConstraintViolationException.class, () -> auszahlungResource.changeKreditor(null));
        requestDto1.setExtId(null);
        assertThrows(ConstraintViolationException.class, () -> auszahlungResource.changeKreditor(requestDto1));

        final ChangeAuszahlungKreditorDto requestDto2 = initChangeAuszahlungKreditorDto();
        requestDto2.setAuszahlung(null);
        assertThrows(ConstraintViolationException.class, () -> auszahlungResource.changeKreditor(requestDto2));

        final ChangeAuszahlungKreditorDto requestDto3 = initChangeAuszahlungKreditorDto();
        requestDto3.setDeliveryId(null);
        assertThrows(ConstraintViolationException.class, () -> auszahlungResource.changeKreditor(requestDto3));

        final ChangeAuszahlungKreditorDto requestDto4 = initChangeAuszahlungKreditorDto();
        requestDto4.setBusinessPartnerId(null);
        assertThrows(ConstraintViolationException.class, () -> auszahlungResource.changeKreditor(requestDto4));

        when(businessPartnerChangeClient.changeBusinessPartner(any())).thenReturn(xml);
        final var response = auszahlungResource.changeKreditor(initChangeAuszahlungKreditorDto());
        assertEquals(HttpStatus.SC_OK, response.getStatus());
    }

    @Test
    @TestAsSachbearbeiter
    void changeBusinessPartnerTest() throws IOException { //todo: add proper xml
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerSuccessResponse.xml"),
            "UTF-8"
        );
        when(businessPartnerChangeClient.changeBusinessPartner(any())).thenReturn(xml);
        final var response = auszahlungResource.changeKreditor(initChangeAuszahlungKreditorDto());
        assertEquals(HttpStatus.SC_OK, response.getStatus());

    }

    @Test
    @TestAsGesuchsteller
    void createAuszahlungAsGSTest(){
        when(vendorPostingCreateClient.createVendorPosting(any())).thenReturn("");
        assertThrows(ForbiddenException.class, () -> auszahlungResource.createAuszahlung((initCreateAuszahlungDto())));
    }

    @Test
    @TestAsSachbearbeiter
    void createAuszahlungInvalidDtoTest() throws IOException {
        String xml = IOUtils.toString(//todo: add proper file
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        final CreateAuszahlungDto requestDto1 = initCreateAuszahlungDto();
        assertThrows(ConstraintViolationException.class, () -> auszahlungResource.createAuszahlung(null));
        requestDto1.setDeliveryId(null);
        assertThrows(ConstraintViolationException.class, () -> auszahlungResource.createAuszahlung(requestDto1));

        final CreateAuszahlungDto requestDto2 = initCreateAuszahlungDto();
        requestDto2.setAuszahlung(null);
        assertThrows(ConstraintViolationException.class, () -> auszahlungResource.createAuszahlung(requestDto2));

        final CreateAuszahlungDto requestDto3 = initCreateAuszahlungDto();
        requestDto3.setVendorNo(null);
        assertThrows(ConstraintViolationException.class, () -> auszahlungResource.createAuszahlung(requestDto3));

        when(vendorPostingCreateClient.createVendorPosting(any())).thenReturn(xml);
        final var response = auszahlungResource.createAuszahlung(initCreateAuszahlungDto());
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
        final var response = auszahlungResource.createAuszahlung(initCreateAuszahlungDto());
        assertEquals(HttpStatus.SC_OK, response.getStatus());

    }

    private CreateAuszahlungKreditorDto initCreateAuszahlungKreditorDto() {
        AuszahlungDto auszahlungDto = new AuszahlungDto();
        auszahlungDto.setKontoinhaber(Kontoinhaber.GESUCHSTELLER);
        auszahlungDto.setIban("CH5589144649329557546");
        auszahlungDto.setNachname("Muster");
        auszahlungDto.setVorname("Hans");
        AdresseDto adresseDto = new AdresseDto();
        adresseDto.setHausnummer("1a");
        adresseDto.setStrasse("Musterstrasse");
        adresseDto.setOrt("Bern");
        adresseDto.setPlz("3011");
        adresseDto.setLand(Land.CH);
        auszahlungDto.setAdresse(adresseDto);
        CreateAuszahlungKreditorDto kreditorDto = new CreateAuszahlungKreditorDto();
        kreditorDto.setAuszahlung(auszahlungDto);
        kreditorDto.setExtId(EXAMPLE_EXT_ID);
        kreditorDto.setDeliveryId(EXAMPLE_DELIVERY_ID);
        return kreditorDto;
    }

    private ChangeAuszahlungKreditorDto initChangeAuszahlungKreditorDto() {
        AuszahlungDto auszahlungDto = new AuszahlungDto();
        auszahlungDto.setKontoinhaber(Kontoinhaber.GESUCHSTELLER);
        auszahlungDto.setIban("CH5589144649329557546");
        auszahlungDto.setNachname("Muster");
        auszahlungDto.setVorname("Hans");
        AdresseDto adresseDto = new AdresseDto();
        adresseDto.setHausnummer("1a");
        adresseDto.setStrasse("Musterstrasse");
        adresseDto.setOrt("Bern");
        adresseDto.setPlz("3011");
        adresseDto.setLand(Land.CH);
        auszahlungDto.setAdresse(adresseDto);
        ChangeAuszahlungKreditorDto kreditorDto = new ChangeAuszahlungKreditorDto();
        kreditorDto.setBusinessPartnerId(0);
        kreditorDto.setAuszahlung(auszahlungDto);
        kreditorDto.setExtId(EXAMPLE_EXT_ID);
        kreditorDto.setDeliveryId(EXAMPLE_DELIVERY_ID);
        return kreditorDto;
    }

    private CreateAuszahlungDto initCreateAuszahlungDto() {
        CreateAuszahlungDto createAuszahlungDto = new CreateAuszahlungDto();
        AuszahlungDto auszahlungDto = new AuszahlungDto();
        auszahlungDto.setKontoinhaber(Kontoinhaber.GESUCHSTELLER);
        auszahlungDto.setIban("CH5589144649329557546");
        auszahlungDto.setNachname("Muster");
        auszahlungDto.setVorname("Hans");
        AdresseDto adresseDto = new AdresseDto();
        adresseDto.setHausnummer("1a");
        adresseDto.setStrasse("Musterstrasse");
        adresseDto.setOrt("Bern");
        adresseDto.setPlz("3011");
        adresseDto.setLand(Land.CH);
        auszahlungDto.setAdresse(adresseDto);
        createAuszahlungDto.setAuszahlung(auszahlungDto);
        createAuszahlungDto.setDeliveryId(EXAMPLE_DELIVERY_ID);
        createAuszahlungDto.setPositionsText("blabala");
        createAuszahlungDto.setZahlungszweck("blabla");
        createAuszahlungDto.setVendorNo(1234);
        return createAuszahlungDto;
    }
}
