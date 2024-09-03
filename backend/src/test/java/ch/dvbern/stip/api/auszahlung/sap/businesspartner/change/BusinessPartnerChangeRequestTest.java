package ch.dvbern.stip.api.auszahlung.sap.businesspartner.change;

import ch.dvbern.stip.api.auszahlung.sap.importstatus.ImportStatusReadRequest;
import ch.dvbern.stip.api.auszahlung.sap.businesspartner.change.SenderParmsDelivery;
import ch.dvbern.stip.api.auszahlung.sap.util.SapEndpointName;
import ch.dvbern.stip.api.auszahlung.sap.util.SoapUtils;
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
import static org.junit.jupiter.api.Assertions.*;

class BusinessPartnerChangeRequestTest {
    private static final String EXPECTED = """
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:urn="urn:be.ch:KTBE_MDG:BUSINESS_PARTNER">
    <soapenv:Header/>
    <soapenv:Body>
        <urn:BusinessPartnerChange_Request>
            <SENDER>
                <SYSID>2080</SYSID>
                <DELIVERY_ID>27612</DELIVERY_ID>
            </SENDER>
            <BUSINESS_PARTNER>
                <HEADER>
                    <BPARTNER>1000569588</BPARTNER>
                </HEADER>
                <ID_KEYS/>
            </BUSINESS_PARTNER>
        </urn:BusinessPartnerChange_Request>
    </soapenv:Body>
</soapenv:Envelope>
                            """;

    @Test
    void CreateBusinessPartnerChangeRequestTest() throws JAXBException, SOAPException, IOException {
        XMLUnit.setIgnoreWhitespace(true);

        BusinessPartnerChangeRequest request = new BusinessPartnerChangeRequest();
        BusinessPartnerChangeRequest.BUSINESSPARTNER businessPartner = new BusinessPartnerChangeRequest.BUSINESSPARTNER();
        BusinessPartnerChangeRequest.BUSINESSPARTNER.HEADER header = new BusinessPartnerChangeRequest.BUSINESSPARTNER.HEADER();
        header.setBPARTNER(1000569588);
        businessPartner.setHEADER(header);
        BusinessPartnerChangeRequest.BUSINESSPARTNER.IDKEYS idkeys = new BusinessPartnerChangeRequest.BUSINESSPARTNER.IDKEYS();
        businessPartner.setIDKEYS(idkeys);
        request.setBUSINESSPARTNER(businessPartner);

        SenderParmsDelivery senderParms = new SenderParmsDelivery();
        senderParms.setSYSID(BigInteger.valueOf(2080));
        senderParms.setDELIVERYID(BigDecimal.valueOf(27612));
        request.setSENDER(senderParms);

        assertDoesNotThrow(() -> {SoapUtils.buildXmlRequest(request, BusinessPartnerChangeRequest.class, SapEndpointName.BUSINESPARTNER);});
        final var actual = SoapUtils.buildXmlRequest(request, BusinessPartnerChangeRequest.class, SapEndpointName.BUSINESPARTNER);
        assertNotNull(actual);
        //assertThat(actual, CompareMatcher.isSimilarTo(EXPECTED));

    }
}
