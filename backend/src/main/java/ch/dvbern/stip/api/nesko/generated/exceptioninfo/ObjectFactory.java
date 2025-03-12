
package ch.dvbern.stip.api.nesko.generated.exceptioninfo;

import javax.xml.namespace.QName;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the ch.be.fin.sv.schemas.base._20070131.exceptioninfo package.
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

    private static final QName _FaultBase_QNAME = new QName("http://sv.fin.be.ch/schemas/base/20070131/ExceptionInfo", "FaultBase");
    private static final QName _InfrastructureFault_QNAME = new QName("http://sv.fin.be.ch/schemas/base/20070131/ExceptionInfo", "InfrastructureFault");
    private static final QName _InvalidArgumentsFault_QNAME = new QName("http://sv.fin.be.ch/schemas/base/20070131/ExceptionInfo", "InvalidArgumentsFault");
    private static final QName _BusinessFault_QNAME = new QName("http://sv.fin.be.ch/schemas/base/20070131/ExceptionInfo", "BusinessFault");
    private static final QName _PermissionDeniedFault_QNAME = new QName("http://sv.fin.be.ch/schemas/base/20070131/ExceptionInfo", "PermissionDeniedFault");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ch.be.fin.sv.schemas.base._20070131.exceptioninfo
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FaultBase }
     *
     * @return
     *     the new instance of {@link FaultBase }
     */
    public FaultBase createFaultBase() {
        return new FaultBase();
    }

    /**
     * Create an instance of {@link InfrastructureFault }
     *
     * @return
     *     the new instance of {@link InfrastructureFault }
     */
    public InfrastructureFault createInfrastructureFault() {
        return new InfrastructureFault();
    }

    /**
     * Create an instance of {@link InvalidArgumentsFault }
     *
     * @return
     *     the new instance of {@link InvalidArgumentsFault }
     */
    public InvalidArgumentsFault createInvalidArgumentsFault() {
        return new InvalidArgumentsFault();
    }

    /**
     * Create an instance of {@link BusinessFault }
     *
     * @return
     *     the new instance of {@link BusinessFault }
     */
    public BusinessFault createBusinessFault() {
        return new BusinessFault();
    }

    /**
     * Create an instance of {@link PermissionDeniedFault }
     *
     * @return
     *     the new instance of {@link PermissionDeniedFault }
     */
    public PermissionDeniedFault createPermissionDeniedFault() {
        return new PermissionDeniedFault();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FaultBase }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link FaultBase }{@code >}
     */
    @XmlElementDecl(namespace = "http://sv.fin.be.ch/schemas/base/20070131/ExceptionInfo", name = "FaultBase")
    public JAXBElement<FaultBase> createFaultBase(FaultBase value) {
        return new JAXBElement<>(_FaultBase_QNAME, FaultBase.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InfrastructureFault }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link InfrastructureFault }{@code >}
     */
    @XmlElementDecl(namespace = "http://sv.fin.be.ch/schemas/base/20070131/ExceptionInfo", name = "InfrastructureFault")
    public JAXBElement<InfrastructureFault> createInfrastructureFault(InfrastructureFault value) {
        return new JAXBElement<>(_InfrastructureFault_QNAME, InfrastructureFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link InvalidArgumentsFault }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link InvalidArgumentsFault }{@code >}
     */
    @XmlElementDecl(namespace = "http://sv.fin.be.ch/schemas/base/20070131/ExceptionInfo", name = "InvalidArgumentsFault")
    public JAXBElement<InvalidArgumentsFault> createInvalidArgumentsFault(InvalidArgumentsFault value) {
        return new JAXBElement<>(_InvalidArgumentsFault_QNAME, InvalidArgumentsFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BusinessFault }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link BusinessFault }{@code >}
     */
    @XmlElementDecl(namespace = "http://sv.fin.be.ch/schemas/base/20070131/ExceptionInfo", name = "BusinessFault")
    public JAXBElement<BusinessFault> createBusinessFault(BusinessFault value) {
        return new JAXBElement<>(_BusinessFault_QNAME, BusinessFault.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PermissionDeniedFault }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link PermissionDeniedFault }{@code >}
     */
    @XmlElementDecl(namespace = "http://sv.fin.be.ch/schemas/base/20070131/ExceptionInfo", name = "PermissionDeniedFault")
    public JAXBElement<PermissionDeniedFault> createPermissionDeniedFault(PermissionDeniedFault value) {
        return new JAXBElement<>(_PermissionDeniedFault_QNAME, PermissionDeniedFault.class, null, value);
    }

}
