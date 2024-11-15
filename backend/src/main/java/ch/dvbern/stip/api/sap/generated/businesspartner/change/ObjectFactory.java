
package ch.dvbern.stip.api.sap.generated.businesspartner.change;

import javax.xml.namespace.QName;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the ch.dvbern.stip.api.auszahlung.sap.businesspartner.change package.
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

    private final static QName _BusinessPartnerChangeRequest_QNAME = new QName("urn:be.ch:KTBE_MDG:BUSINESS_PARTNER", "BusinessPartnerChange_Request");
    private final static QName _BusinessPartnerChangeResponse_QNAME = new QName("urn:be.ch:KTBE_MDG:BUSINESS_PARTNER", "BusinessPartnerChange_Response");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ch.dvbern.stip.api.auszahlung.sap.businesspartner.change
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link BusinessPartnerChangeRequest }
     *
     */
    public BusinessPartnerChangeRequest createBusinessPartnerChangeRequest() {
        return new BusinessPartnerChangeRequest();
    }

    /**
     * Create an instance of {@link BusinessPartnerChangeRequest.BUSINESSPARTNER }
     *
     */
    public BusinessPartnerChangeRequest.BUSINESSPARTNER createBusinessPartnerChangeRequestBUSINESSPARTNER() {
        return new BusinessPartnerChangeRequest.BUSINESSPARTNER();
    }

    /**
     * Create an instance of {@link BusinessPartnerChangeResponse }
     *
     */
    public BusinessPartnerChangeResponse createBusinessPartnerChangeResponse() {
        return new BusinessPartnerChangeResponse();
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
     * Create an instance of {@link SenderParmsDelivery }
     *
     */
    public SenderParmsDelivery createSenderParmsDelivery() {
        return new SenderParmsDelivery();
    }

    /**
     * Create an instance of {@link BusinessPartnerChangeRequest.BUSINESSPARTNER.HEADER }
     *
     */
    public BusinessPartnerChangeRequest.BUSINESSPARTNER.HEADER createBusinessPartnerChangeRequestBUSINESSPARTNERHEADER() {
        return new BusinessPartnerChangeRequest.BUSINESSPARTNER.HEADER();
    }

    /**
     * Create an instance of {@link BusinessPartnerChangeRequest.BUSINESSPARTNER.IDKEYS }
     *
     */
    public BusinessPartnerChangeRequest.BUSINESSPARTNER.IDKEYS createBusinessPartnerChangeRequestBUSINESSPARTNERIDKEYS() {
        return new BusinessPartnerChangeRequest.BUSINESSPARTNER.IDKEYS();
    }

    /**
     * Create an instance of {@link BusinessPartnerChangeRequest.BUSINESSPARTNER.PERSDATA }
     *
     */
    public BusinessPartnerChangeRequest.BUSINESSPARTNER.PERSDATA createBusinessPartnerChangeRequestBUSINESSPARTNERPERSDATA() {
        return new BusinessPartnerChangeRequest.BUSINESSPARTNER.PERSDATA();
    }

    /**
     * Create an instance of {@link BusinessPartnerChangeRequest.BUSINESSPARTNER.ORGDATA }
     *
     */
    public BusinessPartnerChangeRequest.BUSINESSPARTNER.ORGDATA createBusinessPartnerChangeRequestBUSINESSPARTNERORGDATA() {
        return new BusinessPartnerChangeRequest.BUSINESSPARTNER.ORGDATA();
    }

    /**
     * Create an instance of {@link BusinessPartnerChangeRequest.BUSINESSPARTNER.ADDRESS }
     *
     */
    public BusinessPartnerChangeRequest.BUSINESSPARTNER.ADDRESS createBusinessPartnerChangeRequestBUSINESSPARTNERADDRESS() {
        return new BusinessPartnerChangeRequest.BUSINESSPARTNER.ADDRESS();
    }

    /**
     * Create an instance of {@link BusinessPartnerChangeRequest.BUSINESSPARTNER.COMMUNICATION }
     *
     */
    public BusinessPartnerChangeRequest.BUSINESSPARTNER.COMMUNICATION createBusinessPartnerChangeRequestBUSINESSPARTNERCOMMUNICATION() {
        return new BusinessPartnerChangeRequest.BUSINESSPARTNER.COMMUNICATION();
    }

    /**
     * Create an instance of {@link BusinessPartnerChangeRequest.BUSINESSPARTNER.PAYMENTDETAIL }
     *
     */
    public BusinessPartnerChangeRequest.BUSINESSPARTNER.PAYMENTDETAIL createBusinessPartnerChangeRequestBUSINESSPARTNERPAYMENTDETAIL() {
        return new BusinessPartnerChangeRequest.BUSINESSPARTNER.PAYMENTDETAIL();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BusinessPartnerChangeRequest }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BusinessPartnerChangeRequest }{@code >}
     */
    @XmlElementDecl(namespace = "urn:be.ch:KTBE_MDG:BUSINESS_PARTNER", name = "BusinessPartnerChange_Request")
    public JAXBElement<BusinessPartnerChangeRequest> createBusinessPartnerChangeRequest(BusinessPartnerChangeRequest value) {
        return new JAXBElement<BusinessPartnerChangeRequest>(_BusinessPartnerChangeRequest_QNAME, BusinessPartnerChangeRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BusinessPartnerChangeResponse }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BusinessPartnerChangeResponse }{@code >}
     */
    @XmlElementDecl(namespace = "urn:be.ch:KTBE_MDG:BUSINESS_PARTNER", name = "BusinessPartnerChange_Response")
    public JAXBElement<BusinessPartnerChangeResponse> createBusinessPartnerChangeResponse(BusinessPartnerChangeResponse value) {
        return new JAXBElement<BusinessPartnerChangeResponse>(_BusinessPartnerChangeResponse_QNAME, BusinessPartnerChangeResponse.class, null, value);
    }

}
