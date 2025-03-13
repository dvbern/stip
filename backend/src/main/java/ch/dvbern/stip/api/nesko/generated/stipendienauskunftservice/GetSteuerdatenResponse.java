
package ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetSteuerdatenResponse complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="GetSteuerdatenResponse">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="Steuerjahr" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="AngefragtePerson" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}PersonType"/>
 *         <element name="Partner" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}PersonType" minOccurs="0"/>
 *         <element name="Steuerdaten" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}SteuerdatenType"/>
 *         <element name="Kinder" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}KinderType" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetSteuerdatenResponse", propOrder = {
    "steuerjahr",
    "angefragtePerson",
    "partner",
    "steuerdaten",
    "kinder"
})
public class GetSteuerdatenResponse {

    /**
     * Beinhaltet den beim Aufruf übermittelten Wert
     *
     */
    @XmlElement(name = "Steuerjahr")
    protected int steuerjahr;
    /**
     * Personendaten der angefragten Person
     *
     */
    @XmlElement(name = "AngefragtePerson", required = true)
    protected PersonType angefragtePerson;
    /**
     * Personendaten des Partners der angefragten Person
     *
     */
    @XmlElement(name = "Partner")
    protected PersonType partner;
    /**
     * Steuerdaten für die angefragte Person und den Partner
     *
     */
    @XmlElement(name = "Steuerdaten", required = true)
    protected SteuerdatenType steuerdaten;
    /**
     * Anzahl Kinder und Angaben zu jedem Kind
     *
     */
    @XmlElement(name = "Kinder")
    protected KinderType kinder;

    /**
     * Beinhaltet den beim Aufruf übermittelten Wert
     *
     */
    public int getSteuerjahr() {
        return steuerjahr;
    }

    /**
     * Sets the value of the steuerjahr property.
     *
     */
    public void setSteuerjahr(int value) {
        this.steuerjahr = value;
    }

    /**
     * Personendaten der angefragten Person
     *
     * @return
     *     possible object is
     *     {@link PersonType }
     *
     */
    public PersonType getAngefragtePerson() {
        return angefragtePerson;
    }

    /**
     * Sets the value of the angefragtePerson property.
     *
     * @param value
     *     allowed object is
     *     {@link PersonType }
     *
     * @see #getAngefragtePerson()
     */
    public void setAngefragtePerson(PersonType value) {
        this.angefragtePerson = value;
    }

    /**
     * Personendaten des Partners der angefragten Person
     *
     * @return
     *     possible object is
     *     {@link PersonType }
     *
     */
    public PersonType getPartner() {
        return partner;
    }

    /**
     * Sets the value of the partner property.
     *
     * @param value
     *     allowed object is
     *     {@link PersonType }
     *
     * @see #getPartner()
     */
    public void setPartner(PersonType value) {
        this.partner = value;
    }

    /**
     * Steuerdaten für die angefragte Person und den Partner
     *
     * @return
     *     possible object is
     *     {@link SteuerdatenType }
     *
     */
    public SteuerdatenType getSteuerdaten() {
        return steuerdaten;
    }

    /**
     * Sets the value of the steuerdaten property.
     *
     * @param value
     *     allowed object is
     *     {@link SteuerdatenType }
     *
     * @see #getSteuerdaten()
     */
    public void setSteuerdaten(SteuerdatenType value) {
        this.steuerdaten = value;
    }

    /**
     * Anzahl Kinder und Angaben zu jedem Kind
     *
     * @return
     *     possible object is
     *     {@link KinderType }
     *
     */
    public KinderType getKinder() {
        return kinder;
    }

    /**
     * Sets the value of the kinder property.
     *
     * @param value
     *     allowed object is
     *     {@link KinderType }
     *
     * @see #getKinder()
     */
    public void setKinder(KinderType value) {
        this.kinder = value;
    }

}
