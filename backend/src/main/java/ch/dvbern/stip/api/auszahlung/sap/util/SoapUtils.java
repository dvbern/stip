package ch.dvbern.stip.api.auszahlung.sap.util;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPException;
import lombok.experimental.UtilityClass;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@UtilityClass
public class SoapUtils {
    private static final String ENVELOPE_PREFIX ="soapenv";
    public String buildXmlRequest(Object request, JAXBContext contextObj, SapEndpointName sapEndpoint) throws JAXBException, SOAPException, IOException {
        String endpointNamespace = new StringBuilder("urn:be.ch:").append(sapEndpoint.getName()).toString();
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
        soapEnvelope.setPrefix(ENVELOPE_PREFIX);
        soapEnvelope.addNamespaceDeclaration("urn", endpointNamespace);
        soapEnvelope.getHeader().setPrefix(ENVELOPE_PREFIX);
        SOAPBody soapBody = soapEnvelope.getBody();
        soapBody.setPrefix(ENVELOPE_PREFIX);

        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshallerObj.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshallerObj.marshal(request, soapBody);
        soapMessage.saveChanges();

        OutputStream outputStream = new ByteArrayOutputStream();
        soapMessage.writeTo(outputStream);
        return outputStream.toString();
    }

    public<T> T parseSoapResponse(String xmlResponse, Class<T> responseType) throws SOAPException, IOException, JAXBException {
        SOAPMessage message = MessageFactory.newInstance().createMessage(null,
            new ByteArrayInputStream(xmlResponse.getBytes()));
        Unmarshaller unmarshaller = JAXBContext.newInstance(responseType).createUnmarshaller();
        return (T) unmarshaller.unmarshal(message.getSOAPBody().extractContentAsDocument());
    }
}
