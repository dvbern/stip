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

package ch.dvbern.stip.api.sap.util;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import jakarta.ws.rs.InternalServerErrorException;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.ws.handler.MessageContext;
import jakarta.xml.ws.handler.soap.SOAPHandler;
import jakarta.xml.ws.handler.soap.SOAPMessageContext;
import lombok.extern.slf4j.Slf4j;
import org.jctools.queues.MessagePassingQueue.Consumer;

/*
 * This simple SOAPHandler will output the contents of incoming
 * and outgoing messages.
 */
@Slf4j
public class SOAPLoggingHandler implements SOAPHandler<SOAPMessageContext> {
    public Set<QName> getHeaders() {
        return Set.of();
    }

    public boolean handleMessage(SOAPMessageContext smc) {
        logToSystemOut(smc, LOG::info);
        return true;
    }

    public boolean handleFault(SOAPMessageContext smc) {
        logToSystemOut(smc, LOG::error);
        return true;
    }

    // nothing to clean up
    public void close(MessageContext messageContext) {
        // do Nothing
    }

    private static String prettyPrint(OutputStream xml) {
        try {
            Source xmlInput = new StreamSource(new StringReader(xml.toString()));
            StringWriter stringWriter = new StringWriter();
            StreamResult xmlOutput = new StreamResult(stringWriter);

            TransformerFactory transformerFactory = TransformerFactory.newDefaultInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);

            return xmlOutput.getWriter().toString();
        } catch (Exception e) {
            throw new InternalServerErrorException("Error pretty printing XML", e);
        }
    }

    /*
     * Check the MESSAGE_OUTBOUND_PROPERTY in the context
     * to see if this is an outgoing or incoming message.
     * Write a brief message to the print stream and
     * output the message. The writeTo() method can throw
     * SOAPException or IOException
     */
    private void logToSystemOut(SOAPMessageContext smc, Consumer<String> loggingFunction) {
        Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        if (outboundProperty.booleanValue()) {
            loggingFunction.accept("Outbound message:");
        } else {
            loggingFunction.accept("Inbound message:");
        }

        SOAPMessage message = smc.getMessage();
        try {
            final var outstream = new ByteArrayOutputStream();
            message.writeTo(outstream);
            loggingFunction.accept(prettyPrint(outstream));
            loggingFunction.accept(""); // just to add a newline
        } catch (Exception e) {
            LOG.error("Exception in handler: " + e);
        }
    }
}
