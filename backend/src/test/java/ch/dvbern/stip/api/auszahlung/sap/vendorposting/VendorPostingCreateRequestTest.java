package ch.dvbern.stip.api.auszahlung.sap.vendorposting;

import ch.dvbern.stip.api.auszahlung.sap.util.SapEndpointName;
import ch.dvbern.stip.api.auszahlung.sap.util.SoapUtils;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.soap.SOAPException;
import org.junit.jupiter.api.Test;
import wiremock.org.custommonkey.xmlunit.XMLUnit;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

class VendorPostingCreateRequestTest {
    @Test
    void CreateVendorPostingCreateRequestTest() throws JAXBException, SOAPException, IOException {
        XMLUnit.setIgnoreWhitespace(true);

        VendorPostingCreateRequest request = new VendorPostingCreateRequest();
        SenderParmsDelivery senderParms = new SenderParmsDelivery();
        senderParms.setSYSID(BigInteger.valueOf(2080));
        senderParms.setDELIVERYID(BigDecimal.valueOf(1122339312));
        request.setSENDER(senderParms);

        assertDoesNotThrow(() ->{
            SoapUtils.buildXmlRequest(request, VendorPostingCreateRequest.class, SapEndpointName.VENDORPOSTING);});
        final var actual = SoapUtils.buildXmlRequest(request, VendorPostingCreateRequest.class, SapEndpointName.VENDORPOSTING);
        assertNotNull(actual);
        //assertThat(actual, CompareMatcher.isSimilarTo(EXPECTED));

    }
}
