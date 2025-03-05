
package ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice;

import javax.xml.namespace.QName;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlElementDecl;
import jakarta.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the ch.be.fin.sv.schemas.neskovanp._20190716.stipendienauskunftservice package.
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

    private static final QName _GetSteuerdaten_QNAME = new QName("http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService", "GetSteuerdaten");
    private static final QName _GetSteuerdatenResponse_QNAME = new QName("http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService", "GetSteuerdatenResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ch.be.fin.sv.schemas.neskovanp._20190716.stipendienauskunftservice
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetSteuerdaten }
     *
     * @return
     *     the new instance of {@link GetSteuerdaten }
     */
    public GetSteuerdaten createGetSteuerdaten() {
        return new GetSteuerdaten();
    }

    /**
     * Create an instance of {@link GetSteuerdatenResponse }
     *
     * @return
     *     the new instance of {@link GetSteuerdatenResponse }
     */
    public GetSteuerdatenResponse createGetSteuerdatenResponse() {
        return new GetSteuerdatenResponse();
    }

    /**
     * Create an instance of {@link PersonType }
     *
     * @return
     *     the new instance of {@link PersonType }
     */
    public PersonType createPersonType() {
        return new PersonType();
    }

    /**
     * Create an instance of {@link EffSatzType }
     *
     * @return
     *     the new instance of {@link EffSatzType }
     */
    public EffSatzType createEffSatzType() {
        return new EffSatzType();
    }

    /**
     * Create an instance of {@link MannFrauEffSatzType }
     *
     * @return
     *     the new instance of {@link MannFrauEffSatzType }
     */
    public MannFrauEffSatzType createMannFrauEffSatzType() {
        return new MannFrauEffSatzType();
    }

    /**
     * Create an instance of {@link AufwaendeSelbstErwerbType }
     *
     * @return
     *     the new instance of {@link AufwaendeSelbstErwerbType }
     */
    public AufwaendeSelbstErwerbType createAufwaendeSelbstErwerbType() {
        return new AufwaendeSelbstErwerbType();
    }

    /**
     * Create an instance of {@link SteuerdatenType }
     *
     * @return
     *     the new instance of {@link SteuerdatenType }
     */
    public SteuerdatenType createSteuerdatenType() {
        return new SteuerdatenType();
    }

    /**
     * Create an instance of {@link KindType }
     *
     * @return
     *     the new instance of {@link KindType }
     */
    public KindType createKindType() {
        return new KindType();
    }

    /**
     * Create an instance of {@link KinderType }
     *
     * @return
     *     the new instance of {@link KinderType }
     */
    public KinderType createKinderType() {
        return new KinderType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSteuerdaten }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetSteuerdaten }{@code >}
     */
    @XmlElementDecl(namespace = "http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService", name = "GetSteuerdaten")
    public JAXBElement<GetSteuerdaten> createGetSteuerdaten(GetSteuerdaten value) {
        return new JAXBElement<>(_GetSteuerdaten_QNAME, GetSteuerdaten.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetSteuerdatenResponse }{@code >}
     *
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link GetSteuerdatenResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService", name = "GetSteuerdatenResponse")
    public JAXBElement<GetSteuerdatenResponse> createGetSteuerdatenResponse(GetSteuerdatenResponse value) {
        return new JAXBElement<>(_GetSteuerdatenResponse_QNAME, GetSteuerdatenResponse.class, null, value);
    }

}
