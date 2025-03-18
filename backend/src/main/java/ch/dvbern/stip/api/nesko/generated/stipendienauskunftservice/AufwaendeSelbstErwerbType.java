
package ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AufwaendeSelbstErwerbType complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="AufwaendeSelbstErwerbType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="PersoenlicheBeitraegeSaeule2" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}EffSatzType" minOccurs="0"/>
 *         <element name="ERBelasteteAnteileSaeule2" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}EffSatzType" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AufwaendeSelbstErwerbType", propOrder = {
    "persoenlicheBeitraegeSaeule2",
    "erBelasteteAnteileSaeule2"
})
public class AufwaendeSelbstErwerbType {

    /**
     * Summe persoenlicher Beitraege des Inhabers an 2. Saeule
     *
     */
    @XmlElement(name = "PersoenlicheBeitraegeSaeule2")
    protected EffSatzType persoenlicheBeitraegeSaeule2;
    /**
     * Summe der ER belasteten Anteile der 2. Saeule
     *
     */
    @XmlElement(name = "ERBelasteteAnteileSaeule2")
    protected EffSatzType erBelasteteAnteileSaeule2;

    /**
     * Summe persoenlicher Beitraege des Inhabers an 2. Saeule
     *
     * @return
     *     possible object is
     *     {@link EffSatzType }
     *
     */
    public EffSatzType getPersoenlicheBeitraegeSaeule2() {
        return persoenlicheBeitraegeSaeule2;
    }

    /**
     * Sets the value of the persoenlicheBeitraegeSaeule2 property.
     *
     * @param value
     *     allowed object is
     *     {@link EffSatzType }
     *
     * @see #getPersoenlicheBeitraegeSaeule2()
     */
    public void setPersoenlicheBeitraegeSaeule2(EffSatzType value) {
        this.persoenlicheBeitraegeSaeule2 = value;
    }

    /**
     * Summe der ER belasteten Anteile der 2. Saeule
     *
     * @return
     *     possible object is
     *     {@link EffSatzType }
     *
     */
    public EffSatzType getERBelasteteAnteileSaeule2() {
        return erBelasteteAnteileSaeule2;
    }

    /**
     * Sets the value of the erBelasteteAnteileSaeule2 property.
     *
     * @param value
     *     allowed object is
     *     {@link EffSatzType }
     *
     * @see #getERBelasteteAnteileSaeule2()
     */
    public void setERBelasteteAnteileSaeule2(EffSatzType value) {
        this.erBelasteteAnteileSaeule2 = value;
    }

}
