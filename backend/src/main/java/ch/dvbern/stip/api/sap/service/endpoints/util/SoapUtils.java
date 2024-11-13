/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.sap.service.endpoints.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SoapUtils {
    private static final String ENVELOPE_PREFIX = "soapenv";

    public <T> String buildXmlRequest(Object request, Class<T> requestType, SapEndpointName sapEndpoint) {
        JAXBContext contextObj = null;
        try {
            contextObj = JAXBContext.newInstance(requestType);
        } catch (JAXBException jaxbException) {
            throw new RuntimeException(jaxbException);
        }
        String endpointNamespace = new StringBuilder("urn:be.ch:").append(sapEndpoint.getName()).toString();
        SOAPMessage soapMessage = null;
        SOAPBody soapBody = null;
        try {
            MessageFactory messageFactory = MessageFactory.newInstance();
            soapMessage = messageFactory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
            soapEnvelope.setPrefix(ENVELOPE_PREFIX);
            soapEnvelope.addNamespaceDeclaration("urn", endpointNamespace);
            soapEnvelope.getHeader().setPrefix(ENVELOPE_PREFIX);
            soapBody = soapEnvelope.getBody();
            soapBody.setPrefix(ENVELOPE_PREFIX);
        } catch (SOAPException soapException) {
            throw new RuntimeException(soapException);
        }

        Marshaller marshallerObj = null;
        try {
            marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshallerObj.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshallerObj.marshal(request, soapBody);
        } catch (JAXBException jaxbException) {
            throw new RuntimeException(jaxbException);
        }

        try {
            soapMessage.saveChanges();

        } catch (SOAPException soapException) {
            throw new RuntimeException(soapException);
        }

        OutputStream outputStream = new ByteArrayOutputStream();
        try {
            soapMessage.writeTo(outputStream);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        } catch (SOAPException soapException) {
            throw new RuntimeException(soapException);
        }
        return outputStream.toString();
    }

    public <T> T parseSoapResponse(String xmlResponse, Class<T> responseType) {
        SOAPMessage message = null;
        try {
            message = MessageFactory.newInstance()
                .createMessage(
                    null,
                    new ByteArrayInputStream(xmlResponse.getBytes())
                );
        } catch (SOAPException soapException) {
            throw new RuntimeException(soapException);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }

        Object result = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(responseType);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            result = unmarshaller.unmarshal(message.getSOAPBody().extractContentAsDocument());
        } catch (JAXBException jaxbException) {
            throw new RuntimeException(jaxbException);
        } catch (SOAPException soapException) {
            throw new RuntimeException(soapException);
        }

        if (responseType.isInstance(result)) {
            return (T) result;
        } else if (result instanceof JAXBElement<?> element) {
            return (T) element.getValue();
        }
        return null;
    }

}
