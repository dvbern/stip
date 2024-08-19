package ch.dvbern.stip.api.auszahlung.service;

import ch.dvbern.stip.generated.dto.GetAuszahlungImportStatusRequestDto;
import ch.dvbern.stip.generated.dto.GetAuszahlungImportStatusResponseDto;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class AuszahlungSapServiceTest {
    private static final Integer SOME_KNOWN_DELIVERY_ID = 2761;
    @Inject
    AuszahlungSapService sapService;

    @RestClient
    @InjectMock
    ImportStatusReadClient importStatusReadClient;

    @Test
    void getImportStatusTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/sapExampleResponse.xml"),
            "UTF-8"
        );
        when(importStatusReadClient.getImportStatus(any())).thenReturn(xml);
        GetAuszahlungImportStatusRequestDto dto = new GetAuszahlungImportStatusRequestDto();
        dto.setDeliveryId(SOME_KNOWN_DELIVERY_ID);
        final var response = sapService.getImportStatus(dto);
        assertEquals(HttpStatus.SC_OK,response.getStatus());
        final var responseDto = (GetAuszahlungImportStatusResponseDto) response.getEntity();
        assertFalse(responseDto.getLogs().isEmpty());
        assertNotNull(responseDto.getStatus());
    }

    @Test
    void getImportStatusNonExistingDeliveryIdTest(){
        when(importStatusReadClient.getImportStatus(any())).thenThrow(WebApplicationException.class);
        GetAuszahlungImportStatusRequestDto dto = new GetAuszahlungImportStatusRequestDto();
        dto.setDeliveryId(0);
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
