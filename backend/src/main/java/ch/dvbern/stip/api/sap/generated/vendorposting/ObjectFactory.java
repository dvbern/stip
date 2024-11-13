
package ch.dvbern.stip.api.sap.generated.vendorposting;

import javax.xml.namespace.QName;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the ch.dvbern.stip.api.auszahlung.sap.vendorposting package.
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

    private final static QName _VendorPostingCreateResponse_QNAME = new QName("urn:be.ch:KTBE_ERP_FI:VENDOR_POSTING", "VendorPostingCreate_Response");
    private final static QName _VendorPostingCreateRequest_QNAME = new QName("urn:be.ch:KTBE_ERP_FI:VENDOR_POSTING", "VendorPostingCreate_Request");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ch.dvbern.stip.api.auszahlung.sap.vendorposting
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link VendorPostingCreateRequest }
     *
     */
    public VendorPostingCreateRequest createVendorPostingCreateRequest() {
        return new VendorPostingCreateRequest();
    }

    /**
     * Create an instance of {@link VendorPostingCreateRequest.VENDORPOSTING }
     *
     */
    public VendorPostingCreateRequest.VENDORPOSTING createVendorPostingCreateRequestVENDORPOSTING() {
        return new VendorPostingCreateRequest.VENDORPOSTING();
    }

    /**
     * Create an instance of {@link VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL }
     *
     */
    public VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL createVendorPostingCreateRequestVENDORPOSTINGPAYMENTDETAIL() {
        return new VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL();
    }

    /**
     * Create an instance of {@link VendorPostingCreateResponse }
     *
     */
    public VendorPostingCreateResponse createVendorPostingCreateResponse() {
        return new VendorPostingCreateResponse();
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
     * Create an instance of {@link VendorPostingCreateRequest.VENDORPOSTING.HEADER }
     *
     */
    public VendorPostingCreateRequest.VENDORPOSTING.HEADER createVendorPostingCreateRequestVENDORPOSTINGHEADER() {
        return new VendorPostingCreateRequest.VENDORPOSTING.HEADER();
    }

    /**
     * Create an instance of {@link VendorPostingCreateRequest.VENDORPOSTING.VENDOR }
     *
     */
    public VendorPostingCreateRequest.VENDORPOSTING.VENDOR createVendorPostingCreateRequestVENDORPOSTINGVENDOR() {
        return new VendorPostingCreateRequest.VENDORPOSTING.VENDOR();
    }

    /**
     * Create an instance of {@link VendorPostingCreateRequest.VENDORPOSTING.GLACCOUNT }
     *
     */
    public VendorPostingCreateRequest.VENDORPOSTING.GLACCOUNT createVendorPostingCreateRequestVENDORPOSTINGGLACCOUNT() {
        return new VendorPostingCreateRequest.VENDORPOSTING.GLACCOUNT();
    }

    /**
     * Create an instance of {@link VendorPostingCreateRequest.VENDORPOSTING.POSITION }
     *
     */
    public VendorPostingCreateRequest.VENDORPOSTING.POSITION createVendorPostingCreateRequestVENDORPOSTINGPOSITION() {
        return new VendorPostingCreateRequest.VENDORPOSTING.POSITION();
    }

    /**
     * Create an instance of {@link VendorPostingCreateRequest.VENDORPOSTING.ATTACHMENT }
     *
     */
    public VendorPostingCreateRequest.VENDORPOSTING.ATTACHMENT createVendorPostingCreateRequestVENDORPOSTINGATTACHMENT() {
        return new VendorPostingCreateRequest.VENDORPOSTING.ATTACHMENT();
    }

    /**
     * Create an instance of {@link VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL.IBAN }
     *
     */
    public VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL.IBAN createVendorPostingCreateRequestVENDORPOSTINGPAYMENTDETAILIBAN() {
        return new VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL.IBAN();
    }

    /**
     * Create an instance of {@link VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL.QRIBAN }
     *
     */
    public VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL.QRIBAN createVendorPostingCreateRequestVENDORPOSTINGPAYMENTDETAILQRIBAN() {
        return new VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL.QRIBAN();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VendorPostingCreateResponse }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link VendorPostingCreateResponse }{@code >}
     */
    @XmlElementDecl(namespace = "urn:be.ch:KTBE_ERP_FI:VENDOR_POSTING", name = "VendorPostingCreate_Response")
    public JAXBElement<VendorPostingCreateResponse> createVendorPostingCreateResponse(VendorPostingCreateResponse value) {
        return new JAXBElement<VendorPostingCreateResponse>(_VendorPostingCreateResponse_QNAME, VendorPostingCreateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link VendorPostingCreateRequest }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link VendorPostingCreateRequest }{@code >}
     */
    @XmlElementDecl(namespace = "urn:be.ch:KTBE_ERP_FI:VENDOR_POSTING", name = "VendorPostingCreate_Request")
    public JAXBElement<VendorPostingCreateRequest> createVendorPostingCreateRequest(VendorPostingCreateRequest value) {
        return new JAXBElement<VendorPostingCreateRequest>(_VendorPostingCreateRequest_QNAME, VendorPostingCreateRequest.class, null, value);
    }

}
