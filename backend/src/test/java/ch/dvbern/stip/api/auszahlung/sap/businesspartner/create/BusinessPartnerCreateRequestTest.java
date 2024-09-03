package ch.dvbern.stip.api.auszahlung.sap.businesspartner.create;

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

class BusinessPartnerCreateRequestTest {
    private static final String EXPECTED = """
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:urn="urn:be.ch:KTBE_MDG:BUSINESS_PARTNER">
   <soapenv:Header/>
   <soapenv:Body>
      <urn:BusinessPartnerCreate_Request>
         <SENDER>
            <SYSID>2080</SYSID>
            <DELIVERY_ID>1122339312</DELIVERY_ID>
            <!--Optional:-->
            <VERSION>1.0</VERSION>
         </SENDER>
         <BUSINESS_PARTNER>
            <HEADER>
               <PARTN_CAT>2</PARTN_CAT>
            </HEADER>
            <ID_KEYS>
               <EXT_ID>930500</EXT_ID>
               <!--Optional:-->
               <AHV_NR/>
               <!--Optional:-->
               <UID_NR/>
               <!--Optional:-->
               <ZPV_NR/>
            </ID_KEYS>
            <!--Optional:-->
            <PERS_DATA>
               <FIRSTNAME/>
               <LASTNAME/>
               <CORRESPONDLANGUAGEISO/>
               <!--Optional:-->
               <BIRTHNAME/>
               <!--Optional:-->
               <MIDDLENAME/>
               <!--Optional:-->
               <SECONDNAME/>
               <!--Optional:-->
               <TITLE_ACA2/>
               <!--Optional:-->
               <TITLE_SPPL/>
               <!--Optional:-->
               <PREFIX1/>
               <!--Optional:-->
               <PREFIX2/>
               <!--Optional:-->
               <NICKNAME/>
               <!--Optional:-->
               <INITIALS/>
               <!--Optional:-->
               <NAMEFORMAT/>
               <!--Optional:-->
               <NAMCOUNTRY/>
               <!--Optional:-->
               <NAMCOUNTRYISO/>
               <!--Optional:-->
               <BIRTHPLACE/>
               <!--Optional:-->
               <MARITALSTATUS/>
               <!--Optional:-->
               <CORRESPONDLANGUAGE/>
               <!--Optional:-->
               <FULLNAME/>
               <!--Optional:-->
               <EMPLOYER/>
               <!--Optional:-->
               <OCCUPATION/>
               <!--Optional:-->
               <NATIONALITY/>
               <!--Optional:-->
               <NATIONALITYISO/>
               <!--Optional:-->
               <COUNTRYORIGIN/>
               <!--Optional:-->
               <BIRTHDT_STATUS/>
            </PERS_DATA>
            <!--Optional:-->
            <ORG_DATA>
               <NAME1>Exquisit AG_9302</NAME1>
               <!--Optional:-->
               <NAME2/>
               <!--Optional:-->
               <NAME3/>
               <!--Optional:-->
               <NAME4/>
               <LANGU_ISO>DE</LANGU_ISO>
               <!--Optional:-->
               <LEGALFORM/>
               <!--Optional:-->
               <INDUSTRYSECTOR/>
               <!--Optional:-->
               <LOC_NO_1/>
               <!--Optional:-->
               <LOC_NO_2/>
               <!--Optional:-->
               <CHK_DIGIT/>
               <!--Optional:-->
               <LEGALORG/>
            </ORG_DATA>
            <!--1 or more repetitions:-->
            <ADDRESS>
               <ADR_KIND>XXDEFAULT</ADR_KIND>
               <!--Optional:-->
               <C_O_NAME/>
               <CITY>Exquisitcity</CITY>
               <!--Optional:-->
               <DISTRICT/>
               <POSTL_COD1>9302</POSTL_COD1>
               <!--Optional:-->
               <PO_BOX/>
               <STREET>Exxquisitstrasse</STREET>
               <!--Optional:-->
               <HOUSE_NO>9302</HOUSE_NO>
               <COUNTRY>CH</COUNTRY>
               <!--Optional:-->
               <POSTL_COD2/>
            </ADDRESS>
            <!--Zero or more repetitions:-->
            <COMMUNICATION>
               <!--Optional:-->
               <EMAIL/>
               <!--Optional:-->
               <TELEPHONE/>
               <!--Optional:-->
               <MOBILE/>
               <!--Optional:-->
               <FAX/>
            </COMMUNICATION>
            <!--Zero or more repetitions:-->
            <PAYMENT_DETAIL>
               <IBAN/>
               <!--Optional:-->
               <ACCOUNTHOLDER/>
            </PAYMENT_DETAIL>
            <!--Zero or more repetitions:-->
         </BUSINESS_PARTNER>
      </urn:BusinessPartnerCreate_Request>
   </soapenv:Body>
</soapenv:Envelope>
                            """;

    @Test
    void CreateBusinessPartnerChangeRequestTest() throws JAXBException, SOAPException, IOException {
        XMLUnit.setIgnoreWhitespace(true);

        BusinessPartnerCreateRequest request = new BusinessPartnerCreateRequest();
        BusinessPartnerCreateRequest.BUSINESSPARTNER businessPartner = new BusinessPartnerCreateRequest.BUSINESSPARTNER();
        BusinessPartnerCreateRequest.BUSINESSPARTNER.HEADER header = new BusinessPartnerCreateRequest.BUSINESSPARTNER.HEADER();
        //header.setBPARTNER(1000569588);
        businessPartner.setHEADER(header);
        BusinessPartnerCreateRequest.BUSINESSPARTNER.IDKEYS idkeys = new BusinessPartnerCreateRequest.BUSINESSPARTNER.IDKEYS();
        businessPartner.setIDKEYS(idkeys);
        request.setBUSINESSPARTNER(businessPartner);

        ch.dvbern.stip.api.auszahlung.sap.businesspartner.create.SenderParmsDelivery senderParms = new SenderParmsDelivery();
        senderParms.setSYSID(BigInteger.valueOf(2080));
        senderParms.setDELIVERYID(BigDecimal.valueOf(1122339312));
        request.setSENDER(senderParms);

        assertDoesNotThrow(() ->{SoapUtils.buildXmlRequest(request, BusinessPartnerCreateRequest.class, SapEndpointName.BUSINESPARTNER);});
        final var actual = SoapUtils.buildXmlRequest(request, BusinessPartnerCreateRequest.class, SapEndpointName.BUSINESPARTNER);
        assertNotNull(actual);
        //assertThat(actual, CompareMatcher.isSimilarTo(EXPECTED));

    }
}
