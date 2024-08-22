package ch.dvbern.stip.api.auszahlung.service.sap;

import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class SAPUtilsTest {
    @Test
    void generateIdsTest() {
        assertNotNull(SAPUtils.generateBusinessPartnerId());
        assertNotNull(SAPUtils.generateDeliveryId());
    }
    @Test
    void parseSapMessageTypeTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerSuccessResponse.xml"),
            "UTF-8"
        );
        assertTrue(SAPUtils.parseSapMessageType(xml).equals(SapMessageType.S));

       xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessParnterAlreadyExistingPartnerResponse.xml"),
            "UTF-8"
        );
        assertTrue(SAPUtils.parseSapMessageType(xml).equals(SapMessageType.E));
    }

    @Test
    void parseBusinessPartnerIdTest() throws IOException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/BusinessPartnerSearchExampleResponse.xml"),
            "UTF-8"
        );
        String businessPartnerId = SAPUtils.parseBusinessPartnerId(xml);
        assertNotNull(businessPartnerId);
        assertTrue(businessPartnerId.matches("^\\d+$"));

        xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessParnterAlreadyExistingPartnerResponse.xml"),
            "UTF-8"
        );
       businessPartnerId = SAPUtils.parseBusinessPartnerId(xml);
        assertNotNull(businessPartnerId);
        assertTrue(businessPartnerId.matches("^\\d+$"));


        xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/getImportStatusExampleResponse.xml"),
            "UTF-8"
        );
        assertNull(SAPUtils.parseBusinessPartnerId(xml));
    }
}
