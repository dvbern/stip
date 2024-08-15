package ch.dvbern.stip.api.auszahlung.service;

import ch.dvbern.stip.generated.dto.GetAuszahlungImportStatusRequestDto;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest
class AuszahlungSapServiceTest {
    private static final String SOME_KNOWN_DELIVERY_ID = "2761";
    @Inject
    AuszahlungSapService sapService;

    @Test
    void getImportStatusTest(){
        GetAuszahlungImportStatusRequestDto dto = new GetAuszahlungImportStatusRequestDto();
        dto.setDeliveryId(SOME_KNOWN_DELIVERY_ID);
        dto.setSysId("2080");
        //assertTrue(sapService.SST_073_ImportStatusRead != null);
        final var response = sapService.getImportStatus(dto);
        assertNotNull(response);
    }

    @Test
    void createBusinessPartnerTest(){
        fail("Not yet implemented");
        final var response = sapService.createBusinessPartner("2080","2761");
        assertNotNull(response);
    }
}
