package ch.dvbern.stip.api.auszahlung.service;

import ch.dvbern.stip.api.config.service.ConfigService;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest
class AuszahlungSapServiceTest {
    @Inject
    AuszahlungSapService sapService;

    @Inject
    ConfigService configService;

    @RestClient
    @Inject
    AuszahlungImportStatusSapServiceClient auszahlungImportStatusSapServiceClient;
    @Test
    void renderRequestTest(){
        //assertTrue(sapService.SST_073_ImportStatusRead != null);
        final var output = sapService.renderRequest("test","test");
        assertNotNull(output);
    }

    @Test
    void sendRequestTest(){
        final var output = sapService.renderRequest("2080","2761");
        final var response  = auszahlungImportStatusSapServiceClient.getImportStatus(output);
        assertNotNull(response);
    }
}
