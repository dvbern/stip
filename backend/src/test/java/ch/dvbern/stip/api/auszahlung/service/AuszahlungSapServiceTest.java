package ch.dvbern.stip.api.auszahlung.service;

import ch.dvbern.stip.generated.dto.GetAuszahlungImportStatusRequestDto;
import ch.dvbern.stip.generated.dto.GetAuszahlungImportStatusResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.xml.bind.JAXBException;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static com.mysema.commons.lang.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest
class AuszahlungSapServiceTest {
    private static final String SOME_KNOWN_DELIVERY_ID = "2761";
    @Inject
    AuszahlungSapService sapService;

    @Test
    void getImportStatusTest() throws JAXBException {
        GetAuszahlungImportStatusRequestDto dto = new GetAuszahlungImportStatusRequestDto();
        dto.setDeliveryId(SOME_KNOWN_DELIVERY_ID);
        dto.setSysId("2080");
        final var response = sapService.getImportStatus(dto);
        assertEquals(HttpStatus.SC_OK,response.getStatus());
        final var responseDto = (GetAuszahlungImportStatusResponseDto) response.getEntity();
        assertFalse(responseDto.getLogs().isEmpty());
        assertNotNull(responseDto.getStatus());
    }

    @Test
    void getImportStatusInvalidDeliveryIdTest(){
        GetAuszahlungImportStatusRequestDto dto = new GetAuszahlungImportStatusRequestDto();
        dto.setDeliveryId("");
        dto.setSysId("2080");
        final var response = sapService.getImportStatus(dto);
        assertEquals(HttpStatus.SC_BAD_REQUEST,response.getStatus());

    }

    @Test
    void getImportStatusNonExistingDeliveryIdTest() throws JAXBException {
        GetAuszahlungImportStatusRequestDto dto = new GetAuszahlungImportStatusRequestDto();
        dto.setDeliveryId("");
        dto.setSysId("2080");
        final var response = sapService.getImportStatus(dto);
        assertEquals(HttpStatus.SC_BAD_REQUEST,response.getStatus());
    }
    @Test
    void createBusinessPartnerTest(){
        fail("Not yet implemented");
        final var response = sapService.createBusinessPartner("2080","2761");
        assertNotNull(response);
    }

}
