package ch.dvbern.stip.api.auszahlung.sap.importstatus;

import ch.dvbern.stip.api.auszahlung.sap.util.SapEndpointName;
import ch.dvbern.stip.api.auszahlung.sap.util.SoapUtils;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.soap.SOAPException;
import org.junit.jupiter.api.Test;
import org.xmlunit.matchers.CompareMatcher;
import wiremock.org.custommonkey.xmlunit.XMLUnit;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;


class ImportStatusReadRequestTest {
    private static final String EXPECTED = """
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:urn="urn:be.ch:KTBE_ERP_FI:IMPORT_STATUS"><soapenv:Header/><soapenv:Body><urn:ImportStatusRead_Request><SENDER><SYSID>2080</SYSID></SENDER><FILTER_PARMS><DELIVERY_ID>2761</DELIVERY_ID></FILTER_PARMS></urn:ImportStatusRead_Request></soapenv:Body></soapenv:Envelope>
        """;
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
        final var actual = SoapUtils.buildXmlRequest(request, contextObj, SapEndpointName.IMPORT_STATUS);
        assertNotNull(actual);
        assertThat(actual, CompareMatcher.isSimilarTo(EXPECTED));

    }
}
