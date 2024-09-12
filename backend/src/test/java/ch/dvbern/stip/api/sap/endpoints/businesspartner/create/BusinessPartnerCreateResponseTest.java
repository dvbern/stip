package ch.dvbern.stip.api.sap.endpoints.businesspartner.create;

import ch.dvbern.stip.api.sap.generated.businesspartner.create.BusinessPartnerCreateResponse;
import ch.dvbern.stip.api.sap.service.endpoints.util.SoapUtils;
import ch.dvbern.stip.api.sap.util.SapMessageType;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.soap.SOAPException;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BusinessPartnerCreateResponseTest {
    @Test
    void parseBusinessPartnerCreateResponseTest() throws IOException, JAXBException, SOAPException {
        //arrange
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerSuccessResponse.xml"),
            "UTF-8"
        );
        //act
        final var response = SoapUtils.parseSoapResponse(xml, BusinessPartnerCreateResponse.class);
        //assert
        assertNotNull(response);
        assertDoesNotThrow(() -> {
            SoapUtils.parseSoapResponse(xml, BusinessPartnerCreateResponse.class);
        });
        assertEquals(SapMessageType.SUCCESS, SapMessageType.parse(response.getRETURNCODE().get(0).getTYPE()) );
    }

    @Test
    void parseBusinessPartnerCreateAlreadyExistingDeliveryIdResponseTest() throws IOException, JAXBException, SOAPException {
        //arrange
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessPartnerAlreadyExistingDeliveryIdResponse.xml"),
            "UTF-8"
        );
        //act
        final var response = SoapUtils.parseSoapResponse(xml, BusinessPartnerCreateResponse.class);
        //assert
        assertNotNull(response);
        assertDoesNotThrow(() -> {
            SoapUtils.parseSoapResponse(xml, BusinessPartnerCreateResponse.class);
        });
        assertEquals(SapMessageType.ERROR, SapMessageType.parse(response.getRETURNCODE().get(0).getTYPE()));

    }

    @Test
    void parseBusinessPartnerCreateAlreadyExistingBusinessPartnerResponseTest() throws IOException, JAXBException, SOAPException {
        //arrange
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/createBusinessParnterAlreadyExistingPartnerResponse.xml"),
            "UTF-8"
        );
        //act
        final var response = SoapUtils.parseSoapResponse(xml, BusinessPartnerCreateResponse.class);
        //assert
        assertNotNull(response);
        assertDoesNotThrow(() -> {
            SoapUtils.parseSoapResponse(xml, BusinessPartnerCreateResponse.class);
        });
        assertEquals(SapMessageType.ERROR, SapMessageType.parse(response.getRETURNCODE().get(0).getTYPE()));
    }

}
