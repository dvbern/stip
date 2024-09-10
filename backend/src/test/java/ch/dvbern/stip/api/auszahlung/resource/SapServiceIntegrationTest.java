package ch.dvbern.stip.api.auszahlung.resource;

import ch.dvbern.stip.api.auszahlung.entity.Auszahlung;
import ch.dvbern.stip.api.auszahlung.service.*;
import ch.dvbern.stip.api.sap.service.SapAuszahlungService;
import ch.dvbern.stip.api.sap.service.SapEndpointService;
import ch.dvbern.stip.api.sap.util.SAPUtils;
import ch.dvbern.stip.api.auszahlung.type.Kontoinhaber;
import ch.dvbern.stip.api.stammdaten.type.Land;
import ch.dvbern.stip.generated.dto.AdresseDto;
import ch.dvbern.stip.generated.dto.AuszahlungDto;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.apache.http.HttpStatus;

import static ch.dvbern.stip.api.generator.entities.service.AuszahlungGenerator.initAuszahlung;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@QuarkusTest
class SapServiceIntegrationTest {
    /**
     * Important:
     * In this tests, the ACTIVE (INTEGRATION ENVIRONMENT) SAP is used,
     * NOT the mocks!
     *
     */

    @Inject
    SapEndpointService auszahlungSapService;

    @Inject
    SapAuszahlungService sapAuszahlungService;

    @Inject AuszahlungMapper auszahlungMapper;


    // @Test
    void getImportStatus(){
        final var response = auszahlungSapService.getImportStatus(SAPUtils.generateDeliveryId());
        assertEquals(HttpStatus.SC_OK,response.getStatus());
    }


    /*
    todo: if a new businesspartner is created, it can take up do 48 hours until he's available in SAP
     */
/*
    @Test
    void createBusinessPartnerTest() {
        fail();
        assertDoesNotThrow(() -> sapAuszahlungService.getOrCreateBusinessPartner(initAuszahlung()));
    }


 */
    //@Test
    void createBusinessPartnerTest_invalidRequest() {
        assertNull(sapAuszahlungService.getOrCreateBusinessPartner(initAuszahlung()));
    }

    /*
todo: if a new businesspartner is created, it can take up do 48 hours until he's available in SAP
 */
/*
    @Test
    void createVendorPostingTest_New_BusinessPartner_Success() {
        fail();
        Auszahlung auszahlung= initAuszahlung();
        assertNotNull(sapAuszahlungService.createVendorPosting(auszahlung));
    }

 */
    //@Test
    void createVendorPostingTest_Existing_BusinessPartner_Success() {
        Integer bpId = 1000569588;
        Auszahlung auszahlung= initAuszahlung();
        auszahlung.setSapBusinessPartnerId(bpId);
        assertNotNull(sapAuszahlungService.createVendorPosting(auszahlung));
    }


    //@Test
    void createVendorPostingTest_alreadyExistingDeliveryId(){
        Auszahlung auszahlung= initAuszahlung();
        auszahlung.setSapBusinessPartnerId(0);
        assertThrows(WebApplicationException.class,() -> {sapAuszahlungService.createVendorPosting(auszahlung);});
    }

}
