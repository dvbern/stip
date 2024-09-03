package ch.dvbern.stip.api.auszahlung.sap.importstatus;

import ch.dvbern.stip.api.auszahlung.sap.util.SoapUtils;
import ch.dvbern.stip.api.auszahlung.service.ImportStatusReadClient;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class ImportStatusReadResponseTest {
    @Test
    void parseImportStatusResponseTest() throws IOException, JAXBException, SOAPException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/getImportStatusExampleResponse.xml"),
            "UTF-8"
        );
        SOAPMessage message = MessageFactory.newInstance().createMessage(null,
            new ByteArrayInputStream(xml.getBytes()));
        Unmarshaller unmarshaller = JAXBContext.newInstance(ImportStatusReadResponse.class).createUnmarshaller();
        final var response = (ImportStatusReadResponse) unmarshaller.unmarshal(message.getSOAPBody().extractContentAsDocument());
        assertNotNull(response);
    }

    @Test
    void parseImportStatusResponseTest2() throws IOException, JAXBException, SOAPException, ClassNotFoundException {
        String xml = IOUtils.toString(
            this.getClass().getResourceAsStream("/auszahlung/getImportStatusExampleResponse.xml"),
            "UTF-8"
        );
        final var response = SoapUtils.parseSoapResponse(xml,ImportStatusReadResponse.class);
        assertNotNull(response);
    }
}
