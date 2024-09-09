package ch.dvbern.stip.api.auszahlung.resource;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.sap.importstatus.ImportStatusReadResponse;
import ch.dvbern.stip.api.auszahlung.service.*;
import ch.dvbern.stip.api.auszahlung.service.sap.SAPUtils;
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
class SapServiceIntegrationTest {
    @RestClient
    ImportStatusReadClient importStatusReadClient;

    @RestClient
    BusinessPartnerCreateClient businessPartnerCreateClient;

    @RestClient
    BusinessPartnerChangeClient businessPartnerChangeClient;

    @RestClient
    VendorPostingCreateClient vendorPostingCreateClient;

    @Inject
    SapEndpointService auszahlungSapService;

    @Inject
    SapAuszahlungService sapAuszahlungService;

    @Test
    void getImportStatus() throws IOException {
        final var response = auszahlungSapService.getImportStatus(SAPUtils.generateDeliveryId());
        assertEquals(HttpStatus.SC_OK,response.getStatus());
    }

    @Test
    void createBusinessPartnerTest() { //todo: problem if bp is not active yet
        fail();
        assertDoesNotThrow(() -> sapAuszahlungService.getOrCreateBusinessPartner(initAuszahlung()));
    }

    @Test
    void createBusinessPartnerTest_invalidRequest() {
        assertNull(sapAuszahlungService.getOrCreateBusinessPartner(initAuszahlung()));
    }


    @Test
    void createVendorPostingTest_New_BusinessPartner_Success() {//todo: problem if bp is not active yet
        fail();
        Auszahlung auszahlung= initAuszahlung();
        assertNotNull(sapAuszahlungService.createVendorPosting(auszahlung));
    }

    @Test
    void createVendorPostingTest_Existing_BusinessPartner_Success() {
        Integer bpId = 1000569588;
        Auszahlung auszahlung= initAuszahlung();
        auszahlung.setSapBusinessPartnerId(bpId);
        assertNotNull(sapAuszahlungService.createVendorPosting(auszahlung));
    }


    @Test
    void createVendorPostingTest_alreadyExistingDeliveryId(){
        Auszahlung auszahlung= initAuszahlung();
        auszahlung.setSapBusinessPartnerId(0);
        assertDoesNotThrow(() -> {sapAuszahlungService.createVendorPosting(auszahlung);});
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
