package ch.dvbern.stip.api.sap.endpoints.businesspartner.change;

import ch.dvbern.stip.api.sap.generated.businesspartner.change.BusinessPartnerChangeResponse;
import ch.dvbern.stip.api.sap.service.endpoints.util.SoapUtils;
import ch.dvbern.stip.api.sap.util.SapMessageType;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.soap.SOAPException;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BusinessPartnerChangeResponseTest {
    @Test
    void parseBusinessPartnerChangeResponseSuccessTest() throws IOException, JAXBException, SOAPException {
        //arrange
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/changeBusinessPartnerSuccessResponse.xml"),
            "UTF-8"
        );
        //act
        final var response = SoapUtils.parseSoapResponse(xml, BusinessPartnerChangeResponse.class);

        //assert
        assertNotNull(response);
        assertDoesNotThrow(() -> {
            SoapUtils.parseSoapResponse(xml, BusinessPartnerChangeResponse.class);
        });
        assertEquals(SapMessageType.SUCCESS, SapMessageType.parse(response.getRETURNCODE().get(0).getTYPE()) );
    }

    @Test
    void parseBusinessPartnerChangeResponseAlreadyExistingDeliveryIdTest() throws IOException, JAXBException, SOAPException{
        //arrange
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/changeBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        //act
        final var response = SoapUtils.parseSoapResponse(xml, BusinessPartnerChangeResponse.class);
        assertNotNull(response);
        //assert
        assertDoesNotThrow(() -> {
            SoapUtils.parseSoapResponse(xml, BusinessPartnerChangeResponse.class);
        });
        assertEquals(SapMessageType.ERROR, SapMessageType.parse(response.getRETURNCODE().get(0).getTYPE()));
    }
}