package ch.dvbern.stip.api.auszahlung.service;

import ch.dvbern.stip.generated.dto.GetAuszahlungImportStatusRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;
@QuarkusTest
class AuszahlungSapServiceTest {
    private static final String SOME_KNOWN_DELIVERY_ID = "2761";
    @Inject
    AuszahlungSapService sapService;

    @Test
    void getImportStatusTest() throws JsonProcessingException, JAXBException {
        GetAuszahlungImportStatusRequestDto dto = new GetAuszahlungImportStatusRequestDto();
        dto.setDeliveryId(SOME_KNOWN_DELIVERY_ID);
        dto.setSysId("2080");
        final var response = sapService.getImportStatus(dto);
        assertNotNull(response);
    }

    @Test
    void createBusinessPartnerTest(){
        fail("Not yet implemented");
        final var response = sapService.createBusinessPartner("2080","2761");
        assertNotNull(response);
    }

    @Test
    void dummy2() throws JAXBException {
        String response = "    <SOAP:Envelope xmlns:SOAP='http://schemas.xmlsoap.org/soap/envelope/'>\n" +
            "    <SOAP:Header/>\n" +
            "    <SOAP:Body xmlns:urn='urn:be.ch:KTBE_ERP_FI:IMPORT_STATUS'>\n" +
            "    <n0:ImportStatusRead_Response xmlns:n0='urn:be.ch:KTBE_ERP_FI:IMPORT_STATUS' xmlns:prx='urn:sap.com:proxy:QEA:/1SAI/TASF1A86D86632931183F59:757'>\n" +
            "    <DELIVERY>\n" +
            "    <DELIVERY_ID>2761</DELIVERY_ID>\n" +
            "    <TYPE>077 - GP-Change</TYPE>\n" +
            "    <STATUS>1</STATUS>\n" +
            "    <KTEXT>Erfolgreich verarbeitet</KTEXT>\n" +
            "    <LAST_ACTION>2024-08-15T14:30:30Z</LAST_ACTION>\n" +
            "    <COUNTER_SUCCESS>1</COUNTER_SUCCESS>\n" +
            "    <COUNTER_ERROR>0</COUNTER_ERROR>\n" +
            "    <COUNTER_INITIAL>0</COUNTER_INITIAL>\n" +
            "    <COUNTER_TOTAL>1</COUNTER_TOTAL>\n" +
            "    \n" +
            "    <POSITION>\n" +
            "    <POSID>1</POSID>\n" +
            "    <STATUS>SUCCESS</STATUS>\n" +
            "    <SAP_KEY>1000569588</SAP_KEY>\n" +
            "    <EXT_KEY>848931</EXT_KEY>\n" +
            "    \n" +
            "    <LOGS>\n" +
            "    <DATETIME>2024-07-29T14:48:46Z</DATETIME>\n" +
            "    <TYPE>I</TYPE>\n" +
            "    <MSG_NR>32</MSG_NR>\n" +
            "    <MESSAGE>Anderungsantrag 161705 (Schnittstellenänderung (2080)) wurde aktiviert</MESSAGE>\n" +
            "    <ID>USMD5</ID>\n" +
            "    </LOGS>\n" +
            "    \n" +
            "    <LOGS>\n" +
            "    <DATETIME>2024-07-29T14:48:40Z</DATETIME>\n" +
            "    <TYPE>I</TYPE>\n" +
            "    <MSG_NR>52</MSG_NR>\n" +
            "    <MESSAGE>Änderungsantragsnummer 000000161705 wurde erstellt.</MESSAGE>\n" +
            "    <ID>ZCA_SSC</ID>\n" +
            "    </LOGS>\n" +
            "    \n" +
            "    <LOGS><DATETIME>2024-07-29T14:48:40Z</DATETIME><TYPE>S</TYPE><MSG_NR>51</MSG_NR><MESSAGE>SAP Geschäftspartner wurde erfolgreich geändert.</MESSAGE><ID>ZCA_SSC</ID></LOGS>\n" +
            "    </POSITION></DELIVERY><RETURN_CODE><TYPE>S</TYPE><MSG_NR>200</MSG_NR><MESSAGE>Es wurden 1 Deliveries erfolgreich übermittelt.</MESSAGE><ID>ZCA_SSC</ID></RETURN_CODE>\n" +
            "    </n0:ImportStatusRead_Response></SOAP:Body></SOAP:Envelope>";
        JAXBContext context = JAXBContext.newInstance(GetAuszahlungImportStatusResponse.class);
        String shortendResponse = response.substring(response.indexOf("<POSITION>"), response.indexOf("</POSITION>")+ "</POSITION>".length());
        StringReader reader = new StringReader(shortendResponse);
        final var parsed = (GetAuszahlungImportStatusResponse) context.createUnmarshaller()
            .unmarshal(reader);
        assertNotNull(parsed);
    }

}
