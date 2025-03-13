
package ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MannFrauEffSatzType complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="MannFrauEffSatzType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="Mann" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}EffSatzType" minOccurs="0"/>
 *         <element name="Frau" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}EffSatzType" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MannFrauEffSatzType", propOrder = {
    "mann",
    "frau"
})
public class MannFrauEffSatzType {

    @XmlElement(name = "Mann")
    protected EffSatzType mann;
    @XmlElement(name = "Frau")
    protected EffSatzType frau;

    /**
     * Gets the value of the mann property.
     *
     * @return
     *     possible object is
     *     {@link EffSatzType }
     *
     */
    public EffSatzType getMann() {
        return mann;
    }

    /**
     * Sets the value of the mann property.
     *
     * @param value
     *     allowed object is
     *     {@link EffSatzType }
     *
     */
    public void setMann(EffSatzType value) {
        this.mann = value;
    }

    /**
     * Gets the value of the frau property.
     *
     * @return
     *     possible object is
     *     {@link EffSatzType }
     *
     */
    public EffSatzType getFrau() {
        return frau;
    }

    /**
     * Sets the value of the frau property.
     *
     * @param value
     *     allowed object is
     *     {@link EffSatzType }
     *
     */
    public void setFrau(EffSatzType value) {
        this.frau = value;
    }

}
