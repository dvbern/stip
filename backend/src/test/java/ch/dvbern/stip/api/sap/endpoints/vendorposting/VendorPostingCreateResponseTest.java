package ch.dvbern.stip.api.sap.endpoints.vendorposting;

import ch.dvbern.stip.api.sap.service.endpoints.util.SoapUtils;
import ch.dvbern.stip.api.sap.service.endpoints.vendorposting.VendorPostingCreateResponse;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.soap.SOAPException;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class VendorPostingCreateResponseTest {
    @Test
    void parseVendorPostingCreateResponseSuccessTest() throws IOException, JAXBException, SOAPException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/vendorPostingCreateSuccess.xml"),
            "UTF-8"
        );
        final var response = SoapUtils.parseSoapResponse(xml, VendorPostingCreateResponse.class);
        assertNotNull(response);
    }

    @Test
    void parseVendorPostingCreateResponseAlreadyExistingTest() throws IOException, JAXBException, SOAPException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/vendorPostingAlreadyExistingDeliveryResponse.xml"),
            "UTF-8"
        );
        final var response = SoapUtils.parseSoapResponse(xml, VendorPostingCreateResponse.class);
        assertNotNull(response);
    }
}
