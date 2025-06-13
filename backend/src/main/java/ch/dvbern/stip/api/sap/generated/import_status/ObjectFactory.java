
package ch.dvbern.stip.api.sap.generated.import_status;

import javax.xml.namespace.QName;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the ch.be.ktbe_erp_fi.import_status package.
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

    private static final QName _ImportStatusReadResponse_QNAME = new QName("urn:be.ch:KTBE_ERP_FI:IMPORT_STATUS", "ImportStatusRead_Response");
    private static final QName _ImportStatusReadRequest_QNAME = new QName("urn:be.ch:KTBE_ERP_FI:IMPORT_STATUS", "ImportStatusRead_Request");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ch.be.ktbe_erp_fi.import_status
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ImportStatusReadRequest }
     *
     * @return
     *     the new instance of {@link ImportStatusReadRequest }
     */
    public ImportStatusReadRequest createImportStatusReadRequest() {
        return new ImportStatusReadRequest();
    }

    /**
     * Create an instance of {@link ImportStatusReadResponse }
     *
     * @return
     *     the new instance of {@link ImportStatusReadResponse }
     */
    public ImportStatusReadResponse createImportStatusReadResponse() {
        return new ImportStatusReadResponse();
    }

    /**
     * Create an instance of {@link ImportStatusReadResponse.DELIVERY }
     *
     * @return
     *     the new instance of {@link ImportStatusReadResponse.DELIVERY }
     */
    public ImportStatusReadResponse.DELIVERY createImportStatusReadResponseDELIVERY() {
        return new ImportStatusReadResponse.DELIVERY();
    }

    /**
     * Create an instance of {@link ImportStatusReadResponse.DELIVERY.POSITION }
     *
     * @return
     *     the new instance of {@link ImportStatusReadResponse.DELIVERY.POSITION }
     */
    public ImportStatusReadResponse.DELIVERY.POSITION createImportStatusReadResponseDELIVERYPOSITION() {
        return new ImportStatusReadResponse.DELIVERY.POSITION();
    }

    /**
     * Create an instance of {@link ImportStatusReadRequest.FILTERPARMS }
     *
     * @return
     *     the new instance of {@link ImportStatusReadRequest.FILTERPARMS }
     */
    public ImportStatusReadRequest.FILTERPARMS createImportStatusReadRequestFILTERPARMS() {
        return new ImportStatusReadRequest.FILTERPARMS();
    }

    /**
     * Create an instance of {@link ImportStatusReadResponse.DELIVERY.POSITION.LOGS }
     *
     * @return
     *     the new instance of {@link ImportStatusReadResponse.DELIVERY.POSITION.LOGS }
     */
    public ImportStatusReadResponse.DELIVERY.POSITION.LOGS createImportStatusReadResponseDELIVERYPOSITIONLOGS() {
        return new ImportStatusReadResponse.DELIVERY.POSITION.LOGS();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ImportStatusReadResponse }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ImportStatusReadResponse }{@code >}
     */
    @XmlElementDecl(namespace = "urn:be.ch:KTBE_ERP_FI:IMPORT_STATUS", name = "ImportStatusRead_Response")
    public JAXBElement<ImportStatusReadResponse> createImportStatusReadResponse(ImportStatusReadResponse value) {
        return new JAXBElement<>(_ImportStatusReadResponse_QNAME, ImportStatusReadResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ImportStatusReadRequest }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link ImportStatusReadRequest }{@code >}
     */
    @XmlElementDecl(namespace = "urn:be.ch:KTBE_ERP_FI:IMPORT_STATUS", name = "ImportStatusRead_Request")
    public JAXBElement<ImportStatusReadRequest> createImportStatusReadRequest(ImportStatusReadRequest value) {
        return new JAXBElement<>(_ImportStatusReadRequest_QNAME, ImportStatusReadRequest.class, null, value);
    }

}
