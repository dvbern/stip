package ch.dvbern.stip.api.sap.endpoints.importstatus;

import ch.dvbern.stip.api.sap.generated.importstatus.ImportStatusReadResponse;
import ch.dvbern.stip.api.sap.service.endpoints.util.SoapUtils;
import jakarta.xml.bind.JAXBException;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;
import jakarta.xml.soap.SOAPException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ImportStatusReadResponseTest {
    @Test
    void parseImportStatusResponseTest() throws IOException, JAXBException, SOAPException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/getImportStatusExampleResponse.xml"),
            "UTF-8"
        );
        final var response = SoapUtils.parseSoapResponse(xml, ImportStatusReadResponse.class);
        assertNotNull(response);
    }

    @Test
    void parseImportStatusNotFoundResponseTest() throws IOException, JAXBException, SOAPException {
        String xml = IOUtils.toString(
                this.getClass().getResourceAsStream("/auszahlung/getImportStatusDeliveryIdNotFoundExampleResponse.xml"),
                "UTF-8"
        );
        final var response = SoapUtils.parseSoapResponse(xml,ImportStatusReadResponse.class);
        assertNotNull(response);
    }


}
