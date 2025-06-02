
package ch.dvbern.stip.api.sap.generated.business_partner;

import javax.xml.namespace.QName;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the ch.be.ktbe_mdg.business_partner package.
 * <p>An ObjectFactory allows you to programmatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

    private static final QName _BusinessPartnerReadResponse_QNAME = new QName("urn:be.ch:KTBE_MDG:BUSINESS_PARTNER", "BusinessPartnerRead_Response");
    private static final QName _BusinessPartnerReadRequest_QNAME = new QName("urn:be.ch:KTBE_MDG:BUSINESS_PARTNER", "BusinessPartnerRead_Request");
    private static final QName _BusinessPartnerORGDATAFOUNDATIONDATE_QNAME = new QName("", "FOUNDATIONDATE");
    private static final QName _BusinessPartnerORGDATALIQUIDATIONDATE_QNAME = new QName("", "LIQUIDATIONDATE");
    private static final QName _BusinessPartnerPERSDATABIRTHDATE_QNAME = new QName("", "BIRTHDATE");
    private static final QName _BusinessPartnerPERSDATADEATHDATE_QNAME = new QName("", "DEATHDATE");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ch.be.ktbe_mdg.business_partner
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BusinessPartnerORGDATA }
     *
     * @return
     *     the new instance of {@link BusinessPartnerORGDATA }
     */
    public BusinessPartnerORGDATA createBusinessPartnerORGDATA() {
        return new BusinessPartnerORGDATA();
    }

    /**
     * Create an instance of {@link BusinessPartnerPERSDATA }
     *
     * @return
     *     the new instance of {@link BusinessPartnerPERSDATA }
     */
    public BusinessPartnerPERSDATA createBusinessPartnerPERSDATA() {
        return new BusinessPartnerPERSDATA();
    }

    /**
     * Create an instance of {@link BusinessPartnerReadRequest }
     *
     * @return
     *     the new instance of {@link BusinessPartnerReadRequest }
     */
    public BusinessPartnerReadRequest createBusinessPartnerReadRequest() {
        return new BusinessPartnerReadRequest();
    }

    /**
     * Create an instance of {@link BusinessPartnerReadResponse }
     *
     * @return
     *     the new instance of {@link BusinessPartnerReadResponse }
     */
    public BusinessPartnerReadResponse createBusinessPartnerReadResponse() {
        return new BusinessPartnerReadResponse();
    }

    /**
     * Create an instance of {@link AdditionalData }
     *
     * @return
     *     the new instance of {@link AdditionalData }
     */
    public AdditionalData createAdditionalData() {
        return new AdditionalData();
    }

    /**
     * Create an instance of {@link BusinessPartnerPAYMENTDETAIL }
     *
     * @return
     *     the new instance of {@link BusinessPartnerPAYMENTDETAIL }
     */
    public BusinessPartnerPAYMENTDETAIL createBusinessPartnerPAYMENTDETAIL() {
        return new BusinessPartnerPAYMENTDETAIL();
    }

    /**
     * Create an instance of {@link BusinessPartnerIDKEYS }
     *
     * @return
     *     the new instance of {@link BusinessPartnerIDKEYS }
     */
    public BusinessPartnerIDKEYS createBusinessPartnerIDKEYS() {
        return new BusinessPartnerIDKEYS();
    }

    /**
     * Create an instance of {@link BusinessPartnerADDRESS }
     *
     * @return
     *     the new instance of {@link BusinessPartnerADDRESS }
     */
    public BusinessPartnerADDRESS createBusinessPartnerADDRESS() {
        return new BusinessPartnerADDRESS();
    }

    /**
     * Create an instance of {@link BusinessPartnerHEADER }
     *
     * @return
     *     the new instance of {@link BusinessPartnerHEADER }
     */
    public BusinessPartnerHEADER createBusinessPartnerHEADER() {
        return new BusinessPartnerHEADER();
    }

    /**
     * Create an instance of {@link BusinessPartnerCOMMUNICATION }
     *
     * @return
     *     the new instance of {@link BusinessPartnerCOMMUNICATION }
     */
    public BusinessPartnerCOMMUNICATION createBusinessPartnerCOMMUNICATION() {
        return new BusinessPartnerCOMMUNICATION();
    }

    /**
     * Create an instance of {@link ReturnCode }
     *
     * @return
     *     the new instance of {@link ReturnCode }
     */
    public ReturnCode createReturnCode() {
        return new ReturnCode();
    }

    /**
     * Create an instance of {@link SenderParms }
     *
     * @return
     *     the new instance of {@link SenderParms }
     */
    public SenderParms createSenderParms() {
        return new SenderParms();
    }

    /**
     * Create an instance of {@link BusinessPartnerORGDATA.FOUNDATIONDATE }
     *
     * @return
     *     the new instance of {@link BusinessPartnerORGDATA.FOUNDATIONDATE }
     */
    public BusinessPartnerORGDATA.FOUNDATIONDATE createBusinessPartnerORGDATAFOUNDATIONDATE() {
        return new BusinessPartnerORGDATA.FOUNDATIONDATE();
    }

    /**
     * Create an instance of {@link BusinessPartnerORGDATA.LIQUIDATIONDATE }
     *
     * @return
     *     the new instance of {@link BusinessPartnerORGDATA.LIQUIDATIONDATE }
     */
    public BusinessPartnerORGDATA.LIQUIDATIONDATE createBusinessPartnerORGDATALIQUIDATIONDATE() {
        return new BusinessPartnerORGDATA.LIQUIDATIONDATE();
    }

    /**
     * Create an instance of {@link BusinessPartnerPERSDATA.BIRTHDATE }
     *
     * @return
     *     the new instance of {@link BusinessPartnerPERSDATA.BIRTHDATE }
     */
    public BusinessPartnerPERSDATA.BIRTHDATE createBusinessPartnerPERSDATABIRTHDATE() {
        return new BusinessPartnerPERSDATA.BIRTHDATE();
    }

    /**
     * Create an instance of {@link BusinessPartnerPERSDATA.DEATHDATE }
     *
     * @return
     *     the new instance of {@link BusinessPartnerPERSDATA.DEATHDATE }
     */
    public BusinessPartnerPERSDATA.DEATHDATE createBusinessPartnerPERSDATADEATHDATE() {
        return new BusinessPartnerPERSDATA.DEATHDATE();
    }

    /**
     * Create an instance of {@link BusinessPartnerReadRequest.FILTERPARMS }
     *
     * @return
     *     the new instance of {@link BusinessPartnerReadRequest.FILTERPARMS }
     */
    public BusinessPartnerReadRequest.FILTERPARMS createBusinessPartnerReadRequestFILTERPARMS() {
        return new BusinessPartnerReadRequest.FILTERPARMS();
    }

    /**
     * Create an instance of {@link BusinessPartnerReadResponse.BUSINESSPARTNER }
     *
     * @return
     *     the new instance of {@link BusinessPartnerReadResponse.BUSINESSPARTNER }
     */
    public BusinessPartnerReadResponse.BUSINESSPARTNER createBusinessPartnerReadResponseBUSINESSPARTNER() {
        return new BusinessPartnerReadResponse.BUSINESSPARTNER();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BusinessPartnerReadResponse }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BusinessPartnerReadResponse }{@code >}
     */
    @XmlElementDecl(namespace = "urn:be.ch:KTBE_MDG:BUSINESS_PARTNER", name = "BusinessPartnerRead_Response")
    public JAXBElement<BusinessPartnerReadResponse> createBusinessPartnerReadResponse(BusinessPartnerReadResponse value) {
        return new JAXBElement<>(_BusinessPartnerReadResponse_QNAME, BusinessPartnerReadResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BusinessPartnerReadRequest }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BusinessPartnerReadRequest }{@code >}
     */
    @XmlElementDecl(namespace = "urn:be.ch:KTBE_MDG:BUSINESS_PARTNER", name = "BusinessPartnerRead_Request")
    public JAXBElement<BusinessPartnerReadRequest> createBusinessPartnerReadRequest(BusinessPartnerReadRequest value) {
        return new JAXBElement<>(_BusinessPartnerReadRequest_QNAME, BusinessPartnerReadRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BusinessPartnerORGDATA.FOUNDATIONDATE }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BusinessPartnerORGDATA.FOUNDATIONDATE }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "FOUNDATIONDATE", scope = BusinessPartnerORGDATA.class, defaultValue = "1000-01-01")
    public JAXBElement<BusinessPartnerORGDATA.FOUNDATIONDATE> createBusinessPartnerORGDATAFOUNDATIONDATE(
        BusinessPartnerORGDATA.FOUNDATIONDATE value) {
        return new JAXBElement<>(_BusinessPartnerORGDATAFOUNDATIONDATE_QNAME, BusinessPartnerORGDATA.FOUNDATIONDATE.class, BusinessPartnerORGDATA.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BusinessPartnerORGDATA.LIQUIDATIONDATE }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BusinessPartnerORGDATA.LIQUIDATIONDATE }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "LIQUIDATIONDATE", scope = BusinessPartnerORGDATA.class, defaultValue = "1000-01-01")
    public JAXBElement<BusinessPartnerORGDATA.LIQUIDATIONDATE> createBusinessPartnerORGDATALIQUIDATIONDATE(
        BusinessPartnerORGDATA.LIQUIDATIONDATE value) {
        return new JAXBElement<>(_BusinessPartnerORGDATALIQUIDATIONDATE_QNAME, BusinessPartnerORGDATA.LIQUIDATIONDATE.class, BusinessPartnerORGDATA.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BusinessPartnerPERSDATA.BIRTHDATE }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BusinessPartnerPERSDATA.BIRTHDATE }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "BIRTHDATE", scope = BusinessPartnerPERSDATA.class, defaultValue = "1000-01-01")
    public JAXBElement<BusinessPartnerPERSDATA.BIRTHDATE> createBusinessPartnerPERSDATABIRTHDATE(BusinessPartnerPERSDATA.BIRTHDATE value) {
        return new JAXBElement<>(_BusinessPartnerPERSDATABIRTHDATE_QNAME, BusinessPartnerPERSDATA.BIRTHDATE.class, BusinessPartnerPERSDATA.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BusinessPartnerPERSDATA.DEATHDATE }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BusinessPartnerPERSDATA.DEATHDATE }{@code >}
     */
    @XmlElementDecl(namespace = "", name = "DEATHDATE", scope = BusinessPartnerPERSDATA.class, defaultValue = "1000-01-01")
    public JAXBElement<BusinessPartnerPERSDATA.DEATHDATE> createBusinessPartnerPERSDATADEATHDATE(BusinessPartnerPERSDATA.DEATHDATE value) {
        return new JAXBElement<>(_BusinessPartnerPERSDATADEATHDATE_QNAME, BusinessPartnerPERSDATA.DEATHDATE.class, BusinessPartnerPERSDATA.class, value);
    }

}
