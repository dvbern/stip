
package ch.dvbern.stip.api.sap.generated.business_partner;

import java.math.BigDecimal;
import java.math.BigInteger;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SenderParmsDelivery complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="SenderParmsDelivery">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="SYSID">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               <totalDigits value="4"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="BETRIEB" minOccurs="0">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="12"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="DELIVERY_ID">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               <totalDigits value="19"/>
 *               <fractionDigits value="0"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="VERSION" minOccurs="0">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="10"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SenderParmsDelivery", propOrder = {
    "sysid",
    "betrieb",
    "deliveryid",
    "version"
})
public class SenderParmsDelivery {

    /**
     *  4-Stellige ID der Fachapplikation/Umsystem/Liefersystem
     *
     */
    @XmlElement(name = "SYSID", required = true)
    protected BigInteger sysid;
    /**
     * Betrieb/Mandant. Nur bei Mehrmandantensystemen relevant
     *
     */
    @XmlElement(name = "BETRIEB")
    protected String betrieb;
    /**
     * Für das Liefersystem eindeutige Id zum Identifizieren der Lieferung (SST-übergreifend)
     *
     */
    @XmlElement(name = "DELIVERY_ID", required = true)
    protected BigDecimal deliveryid;
    /**
     * Version der Schnittstelle. Wird erst im Life Cycle wichtig, wenn neue Versionen der Schnittstelle notwendig werden. Defaultwert ist 1.0
     *
     */
    @XmlElement(name = "VERSION", defaultValue = "1.0")
    protected String version;

    /**
     *  4-Stellige ID der Fachapplikation/Umsystem/Liefersystem
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getSYSID() {
        return sysid;
    }

    /**
     * Sets the value of the sysid property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     * @see #getSYSID()
     */
    public void setSYSID(BigInteger value) {
        this.sysid = value;
    }

    /**
     * Betrieb/Mandant. Nur bei Mehrmandantensystemen relevant
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBETRIEB() {
        return betrieb;
    }

    /**
     * Sets the value of the betrieb property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getBETRIEB()
     */
    public void setBETRIEB(String value) {
        this.betrieb = value;
    }

    /**
     * Für das Liefersystem eindeutige Id zum Identifizieren der Lieferung (SST-übergreifend)
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getDELIVERYID() {
        return deliveryid;
    }

    /**
     * Sets the value of the deliveryid property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     * @see #getDELIVERYID()
     */
    public void setDELIVERYID(BigDecimal value) {
        this.deliveryid = value;
    }

    /**
     * Version der Schnittstelle. Wird erst im Life Cycle wichtig, wenn neue Versionen der Schnittstelle notwendig werden. Defaultwert ist 1.0
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getVERSION() {
        return version;
    }

    /**
     * Sets the value of the version property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getVERSION()
     */
    public void setVERSION(String value) {
        this.version = value;
    }

}
