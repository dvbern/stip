
package ch.dvbern.stip.api.sap.generated.general;

import java.math.BigDecimal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Struktur für Zusatzdaten
 *
 * <p>Java class for AdditionalData complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="AdditionalData">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="KEY">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="40"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="VALUE">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="255"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="INDEX" minOccurs="0">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *               <totalDigits value="10"/>
 *               <fractionDigits value="0"/>
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
@XmlType(name = "AdditionalData", propOrder = {
    "key",
    "value",
    "index"
})
public class AdditionalData {

    /**
     * Schlüssel zum Feld
     *
     */
    @XmlElement(name = "KEY", required = true)
    protected String key;
    /**
     * Feldwert
     *
     */
    @XmlElement(name = "VALUE", required = true)
    protected String value;
    /**
     * Position (optional)
     *
     */
    @XmlElement(name = "INDEX")
    protected BigDecimal index;

    /**
     * Schlüssel zum Feld
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getKEY() {
        return key;
    }

    /**
     * Sets the value of the key property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getKEY()
     */
    public void setKEY(String value) {
        this.key = value;
    }

    /**
     * Feldwert
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getVALUE() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getVALUE()
     */
    public void setVALUE(String value) {
        this.value = value;
    }

    /**
     * Position (optional)
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getINDEX() {
        return index;
    }

    /**
     * Sets the value of the index property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     * @see #getINDEX()
     */
    public void setINDEX(BigDecimal value) {
        this.index = value;
    }

}
