package ch.dvbern.stip.api.auszahlung.sap.businesspartner.read;

import ch.dvbern.stip.api.auszahlung.sap.businesspartner.change.BusinessPartnerChangeResponse;
import ch.dvbern.stip.api.auszahlung.sap.util.SoapUtils;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.soap.SOAPException;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class BusinessPartnerReadResponseTest {
    @Test
    void parseBusinessPartnerChangeResponseSuccessTest() throws IOException, JAXBException, SOAPException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/readBusinessPartnerExampleResponse.xml"),
            "UTF-8"
        );
        final var response = SoapUtils.parseSoapResponse(xml, BusinessPartnerReadResponse.class);
        assertNotNull(response);
    }
}
