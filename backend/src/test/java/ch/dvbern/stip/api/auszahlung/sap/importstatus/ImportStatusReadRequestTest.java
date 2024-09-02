package ch.dvbern.stip.api.auszahlung.sap.importstatus;

import com.savoirtech.json.JsonComparatorBuilder;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
//import org.glassfish.expressly.util.MessageFactory;
import jakarta.xml.soap.*;
import org.junit.jupiter.api.Test;
import org.w3c.dom.NodeList;
import org.xmlunit.matchers.CompareMatcher;
import wiremock.org.custommonkey.xmlunit.XMLUnit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

//import jakarta.xml.soap.MessageFactory;
/*
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

 */

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


class ImportStatusReadRequestTest {
    private static final String EXPECTED = """
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:urn="urn:be.ch:KTBE_ERP_FI:IMPORT_STATUS"><soapenv:Header/><soapenv:Body><urn:ImportStatusRead_Request><SENDER><SYSID>2080</SYSID></SENDER><FILTER_PARMS><DELIVERY_ID>2761</DELIVERY_ID></FILTER_PARMS></urn:ImportStatusRead_Request></soapenv:Body></soapenv:Envelope>
        """;

    private String getRequestAsXmlString(Object request, JAXBContext contextObj) throws JAXBException, SOAPException, IOException {

        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
        soapEnvelope.setPrefix("soapenv");
        soapEnvelope.addNamespaceDeclaration("urn", "urn:be.ch:KTBE_ERP_FI:IMPORT_STATUS");
        soapEnvelope.getHeader().setPrefix("soapenv");
        SOAPBody soapBody = soapEnvelope.getBody();
        soapBody.setPrefix("soapenv");

        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshallerObj.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshallerObj.marshal(request, soapBody);
        soapMessage.saveChanges();

        OutputStream outputStream = new ByteArrayOutputStream();
        soapMessage.writeTo(outputStream);
        String xml = outputStream.toString();
        return xml;
    }

    @Test
    void CreateImportStatusReadRequestTest() throws JAXBException, SOAPException, IOException {
        XMLUnit.setIgnoreWhitespace(true);
        JAXBContext contextObj = JAXBContext.newInstance(ImportStatusReadRequest.class);
        ImportStatusReadRequest request = new ImportStatusReadRequest();
        ImportStatusReadRequest.FILTERPARMS filterparms = new ImportStatusReadRequest.FILTERPARMS();
        filterparms.setDELIVERYID(BigDecimal.valueOf(2761));
        SenderParms senderParms = new SenderParms();
        senderParms.setSYSID(BigInteger.valueOf(2080));
        request.setFILTERPARMS(filterparms);
        request.setSENDER(senderParms);
        final var actual = getRequestAsXmlString(request, contextObj);
        assertNotNull(actual);
        assertThat(actual, CompareMatcher.isSimilarTo(EXPECTED));

    }
}
