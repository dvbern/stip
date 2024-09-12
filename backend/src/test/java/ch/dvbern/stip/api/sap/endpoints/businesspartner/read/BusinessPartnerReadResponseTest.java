package ch.dvbern.stip.api.sap.endpoints.businesspartner.read;

import ch.dvbern.stip.api.sap.service.endpoints.businesspartner.read.BusinessPartnerReadResponse;
import ch.dvbern.stip.api.sap.service.endpoints.util.SoapUtils;
import ch.dvbern.stip.api.sap.util.SapMessageType;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.soap.SOAPException;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BusinessPartnerReadResponseTest {
    @Test
    void parseBusinessPartnerChangeResponseSuccessTest() throws IOException, JAXBException, SOAPException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/readBusinessPartnerExampleResponse.xml"),
            "UTF-8"
        );
        final var response = SoapUtils.parseSoapResponse(xml, BusinessPartnerReadResponse.class);
        //assert
        assertNotNull(response);
        assertDoesNotThrow(() -> {
            SoapUtils.parseSoapResponse(xml, BusinessPartnerReadResponse.class);
        });
        assertEquals(SapMessageType.SUCCESS,SapMessageType.parse(response.getRETURNCODE().get(0).getTYPE()));
    }
}
