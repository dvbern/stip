
package ch.dvbern.stip.api.sap.generated.businesspartner.create;

import javax.xml.namespace.QName;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the ch.dvbern.stip.api.auszahlung.sap.businesspartner.create package.
 * <p>An ObjectFactory allows you to programatically
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

    private final static QName _BusinessPartnerCreateRequest_QNAME = new QName("urn:be.ch:KTBE_MDG:BUSINESS_PARTNER", "BusinessPartnerCreate_Request");
    private final static QName _BusinessPartnerCreateResponse_QNAME = new QName("urn:be.ch:KTBE_MDG:BUSINESS_PARTNER", "BusinessPartnerCreate_Response");
    private final static QName _BusinessPartnerORGDATAFOUNDATIONDATE_QNAME = new QName("", "FOUNDATIONDATE");
    private final static QName _BusinessPartnerORGDATALIQUIDATIONDATE_QNAME = new QName("", "LIQUIDATIONDATE");
    private final static QName _BusinessPartnerPERSDATABIRTHDATE_QNAME = new QName("", "BIRTHDATE");
    private final static QName _BusinessPartnerPERSDATADEATHDATE_QNAME = new QName("", "DEATHDATE");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ch.dvbern.stip.api.auszahlung.sap.businesspartner.create
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BusinessPartnerORGDATA }
     *
     */
    public BusinessPartnerORGDATA createBusinessPartnerORGDATA() {
        return new BusinessPartnerORGDATA();
    }

    /**
     * Create an instance of {@link BusinessPartnerPERSDATA }
     *
     */
    public BusinessPartnerPERSDATA createBusinessPartnerPERSDATA() {
        return new BusinessPartnerPERSDATA();
    }

    /**
     * Create an instance of {@link BusinessPartnerCreateResponse }
     *
     */
    public BusinessPartnerCreateResponse createBusinessPartnerCreateResponse() {
        return new BusinessPartnerCreateResponse();
    }

    /**
     * Create an instance of {@link BusinessPartnerCreateRequest }
     *
     */
    public BusinessPartnerCreateRequest createBusinessPartnerCreateRequest() {
        return new BusinessPartnerCreateRequest();
    }

    /**
     * Create an instance of {@link BusinessPartnerCreateRequest.BUSINESSPARTNER }
     *
     */
    public BusinessPartnerCreateRequest.BUSINESSPARTNER createBusinessPartnerCreateRequestBUSINESSPARTNER() {
        return new BusinessPartnerCreateRequest.BUSINESSPARTNER();
    }

    /**
     * Create an instance of {@link AdditionalData }
     *
     */
    public AdditionalData createAdditionalData() {
        return new AdditionalData();
    }

    /**
     * Create an instance of {@link ReturnCode }
     *
     */
    public ReturnCode createReturnCode() {
        return new ReturnCode();
    }

    /**
     * Create an instance of {@link BusinessPartnerIDKEYS }
     *
     */
    public BusinessPartnerIDKEYS createBusinessPartnerIDKEYS() {
        return new BusinessPartnerIDKEYS();
    }

    /**
     * Create an instance of {@link SenderParmsDelivery }
     *
     */
    public SenderParmsDelivery createSenderParmsDelivery() {
        return new SenderParmsDelivery();
    }

    /**
     * Create an instance of {@link BusinessPartnerHEADER }
     *
     */
    public BusinessPartnerHEADER createBusinessPartnerHEADER() {
        return new BusinessPartnerHEADER();
    }

    /**
     * Create an instance of {@link BusinessPartnerORGDATA.FOUNDATIONDATE }
     *
     */
    public BusinessPartnerORGDATA.FOUNDATIONDATE createBusinessPartnerORGDATAFOUNDATIONDATE() {
        return new BusinessPartnerORGDATA.FOUNDATIONDATE();
    }

    /**
     * Create an instance of {@link BusinessPartnerORGDATA.LIQUIDATIONDATE }
     *
     */
    public BusinessPartnerORGDATA.LIQUIDATIONDATE createBusinessPartnerORGDATALIQUIDATIONDATE() {
        return new BusinessPartnerORGDATA.LIQUIDATIONDATE();
    }

    /**
     * Create an instance of {@link BusinessPartnerPERSDATA.BIRTHDATE }
     *
     */
    public BusinessPartnerPERSDATA.BIRTHDATE createBusinessPartnerPERSDATABIRTHDATE() {
        return new BusinessPartnerPERSDATA.BIRTHDATE();
    }

    /**
     * Create an instance of {@link BusinessPartnerPERSDATA.DEATHDATE }
     *
     */
    public BusinessPartnerPERSDATA.DEATHDATE createBusinessPartnerPERSDATADEATHDATE() {
        return new BusinessPartnerPERSDATA.DEATHDATE();
    }

    /**
     * Create an instance of {@link BusinessPartnerCreateResponse.BUSINESSPARTNER }
     *
     */
    public BusinessPartnerCreateResponse.BUSINESSPARTNER createBusinessPartnerCreateResponseBUSINESSPARTNER() {
        return new BusinessPartnerCreateResponse.BUSINESSPARTNER();
    }

    /**
     * Create an instance of {@link BusinessPartnerCreateRequest.BUSINESSPARTNER.HEADER }
     *
     */
    public BusinessPartnerCreateRequest.BUSINESSPARTNER.HEADER createBusinessPartnerCreateRequestBUSINESSPARTNERHEADER() {
        return new BusinessPartnerCreateRequest.BUSINESSPARTNER.HEADER();
    }

    /**
     * Create an instance of {@link BusinessPartnerCreateRequest.BUSINESSPARTNER.IDKEYS }
     *
     */
    public BusinessPartnerCreateRequest.BUSINESSPARTNER.IDKEYS createBusinessPartnerCreateRequestBUSINESSPARTNERIDKEYS() {
        return new BusinessPartnerCreateRequest.BUSINESSPARTNER.IDKEYS();
    }

    /**
     * Create an instance of {@link BusinessPartnerCreateRequest.BUSINESSPARTNER.PERSDATA }
     *
     */
    public BusinessPartnerCreateRequest.BUSINESSPARTNER.PERSDATA createBusinessPartnerCreateRequestBUSINESSPARTNERPERSDATA() {
        return new BusinessPartnerCreateRequest.BUSINESSPARTNER.PERSDATA();
    }

    /**
     * Create an instance of {@link BusinessPartnerCreateRequest.BUSINESSPARTNER.ORGDATA }
     *
     */
    public BusinessPartnerCreateRequest.BUSINESSPARTNER.ORGDATA createBusinessPartnerCreateRequestBUSINESSPARTNERORGDATA() {
        return new BusinessPartnerCreateRequest.BUSINESSPARTNER.ORGDATA();
    }

    /**
     * Create an instance of {@link BusinessPartnerCreateRequest.BUSINESSPARTNER.ADDRESS }
     *
     */
    public BusinessPartnerCreateRequest.BUSINESSPARTNER.ADDRESS createBusinessPartnerCreateRequestBUSINESSPARTNERADDRESS() {
        return new BusinessPartnerCreateRequest.BUSINESSPARTNER.ADDRESS();
    }

    /**
     * Create an instance of {@link BusinessPartnerCreateRequest.BUSINESSPARTNER.COMMUNICATION }
     *
     */
    public BusinessPartnerCreateRequest.BUSINESSPARTNER.COMMUNICATION createBusinessPartnerCreateRequestBUSINESSPARTNERCOMMUNICATION() {
        return new BusinessPartnerCreateRequest.BUSINESSPARTNER.COMMUNICATION();
    }

    /**
     * Create an instance of {@link BusinessPartnerCreateRequest.BUSINESSPARTNER.PAYMENTDETAIL }
     *
     */
    public BusinessPartnerCreateRequest.BUSINESSPARTNER.PAYMENTDETAIL createBusinessPartnerCreateRequestBUSINESSPARTNERPAYMENTDETAIL() {
        return new BusinessPartnerCreateRequest.BUSINESSPARTNER.PAYMENTDETAIL();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BusinessPartnerCreateRequest }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BusinessPartnerCreateRequest }{@code >}
     */
    @XmlElementDecl(namespace = "urn:be.ch:KTBE_MDG:BUSINESS_PARTNER", name = "BusinessPartnerCreate_Request")
    public JAXBElement<BusinessPartnerCreateRequest> createBusinessPartnerCreateRequest(BusinessPartnerCreateRequest value) {
        return new JAXBElement<BusinessPartnerCreateRequest>(_BusinessPartnerCreateRequest_QNAME, BusinessPartnerCreateRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BusinessPartnerCreateResponse }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BusinessPartnerCreateResponse }{@code >}
     */
    @XmlElementDecl(namespace = "urn:be.ch:KTBE_MDG:BUSINESS_PARTNER", name = "BusinessPartnerCreate_Response")
    public JAXBElement<BusinessPartnerCreateResponse> createBusinessPartnerCreateResponse(BusinessPartnerCreateResponse value) {
        return new JAXBElement<BusinessPartnerCreateResponse>(_BusinessPartnerCreateResponse_QNAME, BusinessPartnerCreateResponse.class, null, value);
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
    public JAXBElement<BusinessPartnerORGDATA.FOUNDATIONDATE> createBusinessPartnerORGDATAFOUNDATIONDATE(BusinessPartnerORGDATA.FOUNDATIONDATE value) {
        return new JAXBElement<BusinessPartnerORGDATA.FOUNDATIONDATE>(_BusinessPartnerORGDATAFOUNDATIONDATE_QNAME, BusinessPartnerORGDATA.FOUNDATIONDATE.class, BusinessPartnerORGDATA.class, value);
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
    public JAXBElement<BusinessPartnerORGDATA.LIQUIDATIONDATE> createBusinessPartnerORGDATALIQUIDATIONDATE(BusinessPartnerORGDATA.LIQUIDATIONDATE value) {
        return new JAXBElement<BusinessPartnerORGDATA.LIQUIDATIONDATE>(_BusinessPartnerORGDATALIQUIDATIONDATE_QNAME, BusinessPartnerORGDATA.LIQUIDATIONDATE.class, BusinessPartnerORGDATA.class, value);
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
        return new JAXBElement<BusinessPartnerPERSDATA.BIRTHDATE>(_BusinessPartnerPERSDATABIRTHDATE_QNAME, BusinessPartnerPERSDATA.BIRTHDATE.class, BusinessPartnerPERSDATA.class, value);
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
        return new JAXBElement<BusinessPartnerPERSDATA.DEATHDATE>(_BusinessPartnerPERSDATADEATHDATE_QNAME, BusinessPartnerPERSDATA.DEATHDATE.class, BusinessPartnerPERSDATA.class, value);
    }

}
