
package ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice;

import java.math.BigDecimal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EffSatzType complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="EffSatzType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="Effektiv" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}BetragType"/>
 *         <element name="Satzbestimmend" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}BetragType"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EffSatzType", propOrder = {
    "effektiv",
    "satzbestimmend"
})
public class EffSatzType {

    @XmlElement(name = "Effektiv", required = true)
    protected BigDecimal effektiv;
    @XmlElement(name = "Satzbestimmend", required = true)
    protected BigDecimal satzbestimmend;

    /**
     * Gets the value of the effektiv property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getEffektiv() {
        return effektiv;
    }

    /**
     * Sets the value of the effektiv property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setEffektiv(BigDecimal value) {
        this.effektiv = value;
    }

    /**
     * Gets the value of the satzbestimmend property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getSatzbestimmend() {
        return satzbestimmend;
    }

    /**
     * Sets the value of the satzbestimmend property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setSatzbestimmend(BigDecimal value) {
        this.satzbestimmend = value;
    }

}
