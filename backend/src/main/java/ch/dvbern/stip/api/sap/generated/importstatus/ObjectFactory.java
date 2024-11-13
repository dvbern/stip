
package ch.dvbern.stip.api.sap.generated.importstatus;

import javax.xml.namespace.QName;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the ch.dvbern.stip.api.auszahlung.sap.importstatus package.
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

    private final static QName _ImportStatusReadResponse_QNAME = new QName("urn:be.ch:KTBE_ERP_FI:IMPORT_STATUS", "ImportStatusRead_Response");
    private final static QName _ImportStatusReadRequest_QNAME = new QName("urn:be.ch:KTBE_ERP_FI:IMPORT_STATUS", "ImportStatusRead_Request");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ch.dvbern.stip.api.auszahlung.sap.importstatus
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ImportStatusReadRequest }
     *
     */
    public ImportStatusReadRequest createImportStatusReadRequest() {
        return new ImportStatusReadRequest();
    }

    /**
     * Create an instance of {@link ImportStatusReadResponse }
     *
     */
    public ImportStatusReadResponse createImportStatusReadResponse() {
        return new ImportStatusReadResponse();
    }

    /**
     * Create an instance of {@link ImportStatusReadResponse.DELIVERY }
     *
     */
    public ImportStatusReadResponse.DELIVERY createImportStatusReadResponseDELIVERY() {
        return new ImportStatusReadResponse.DELIVERY();
    }

    /**
     * Create an instance of {@link ImportStatusReadResponse.DELIVERY.POSITION }
     *
     */
    public ImportStatusReadResponse.DELIVERY.POSITION createImportStatusReadResponseDELIVERYPOSITION() {
        return new ImportStatusReadResponse.DELIVERY.POSITION();
    }

    /**
     * Create an instance of {@link SenderParms }
     *
     */
    public SenderParms createSenderParms() {
        return new SenderParms();
    }

    /**
     * Create an instance of {@link ReturnCodeID }
     *
     */
    public ReturnCodeID createReturnCodeID() {
        return new ReturnCodeID();
    }

    /**
     * Create an instance of {@link ImportStatusReadRequest.FILTERPARMS }
     *
     */
    public ImportStatusReadRequest.FILTERPARMS createImportStatusReadRequestFILTERPARMS() {
        return new ImportStatusReadRequest.FILTERPARMS();
    }

    /**
     * Create an instance of {@link ImportStatusReadResponse.DELIVERY.POSITION.LOGS }
     *
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
        return new JAXBElement<ImportStatusReadResponse>(_ImportStatusReadResponse_QNAME, ImportStatusReadResponse.class, null, value);
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
        return new JAXBElement<ImportStatusReadRequest>(_ImportStatusReadRequest_QNAME, ImportStatusReadRequest.class, null, value);
    }

}
