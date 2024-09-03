package ch.dvbern.stip.api.auszahlung.sap.businesspartner.create;

import ch.dvbern.stip.api.auszahlung.sap.util.SoapUtils;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.soap.SOAPException;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BusinessPartnerCreateResponseTest {
    @Test
    void parseBusinessPartnerCreateResponseTest() throws IOException, JAXBException, SOAPException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerSuccessResponse.xml"),
            "UTF-8"
        );
        final var response = SoapUtils.parseSoapResponse(xml, BusinessPartnerCreateResponse.class);
        assertNotNull(response);
    }

    @Test
    void parseBusinessPartnerCreateAlreadyExistingDeliveryIdResponseTest() throws IOException, JAXBException, SOAPException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        final var response = SoapUtils.parseSoapResponse(xml, BusinessPartnerCreateResponse.class);
        assertNotNull(response);
    }

    @Test
    void parseBusinessPartnerCreateAlreadyExistingBusinessPartnerResponseTest() throws IOException, JAXBException, SOAPException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessParnterAlreadyExistingPartnerResponse.xml"),
            "UTF-8"
        );
        final var response = SoapUtils.parseSoapResponse(xml, BusinessPartnerCreateResponse.class);
        assertNotNull(response);
    }

}
