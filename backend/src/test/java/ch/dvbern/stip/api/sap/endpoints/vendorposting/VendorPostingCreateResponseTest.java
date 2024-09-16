package ch.dvbern.stip.api.sap.endpoints.vendorposting;

import ch.dvbern.stip.api.sap.service.endpoints.util.SoapUtils;
import ch.dvbern.stip.api.sap.generated.vendorposting.VendorPostingCreateResponse;
import ch.dvbern.stip.api.sap.util.SapMessageType;
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
        //assert
        assertNotNull(response);
        assertDoesNotThrow(() -> {
            SoapUtils.parseSoapResponse(xml, VendorPostingCreateResponse.class);
        });
        assertEquals(SapMessageType.SUCCESS,SapMessageType.parse(response.getRETURNCODE().get(0).getTYPE()));
    }

    @Test
    void parseVendorPostingCreateResponseAlreadyExistingTest() throws IOException, JAXBException, SOAPException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/vendorPostingAlreadyExistingDeliveryResponse.xml"),
            "UTF-8"
        );
        final var response = SoapUtils.parseSoapResponse(xml, VendorPostingCreateResponse.class);
        //assert
        assertNotNull(response);
        assertDoesNotThrow(() -> {
            SoapUtils.parseSoapResponse(xml, VendorPostingCreateResponse.class);
        });
        assertEquals(SapMessageType.ERROR, SapMessageType.parse(response.getRETURNCODE().get(0).getTYPE()));
    }
}
