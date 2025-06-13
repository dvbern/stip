
package ch.dvbern.stip.api.sap.generated.general;

import java.math.BigInteger;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReturnCodeID complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="ReturnCodeID">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="TYPE">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="1"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="MSG_NR">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               <totalDigits value="3"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="MESSAGE">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="220"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="ID" minOccurs="0">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="20"/>
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
@XmlType(name = "ReturnCodeID", propOrder = {
    "type",
    "msgnr",
    "message",
    "id"
})
public class ReturnCodeID {

    /**
     * S = Success, W = Warning, E = Error, I = Info
     *
     */
    @XmlElement(name = "TYPE", required = true)
    protected String type;
    /**
     * Eindeutige ID der Nachricht
     *
     */
    @XmlElement(name = "MSG_NR", required = true)
    protected BigInteger msgnr;
    /**
     * Text der Nachricht
     *
     */
    @XmlElement(name = "MESSAGE", required = true)
    protected String message;
    /**
     * Nachrichtenklasse
     *
     */
    @XmlElement(name = "ID")
    protected String id;

    /**
     * S = Success, W = Warning, E = Error, I = Info
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTYPE() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getTYPE()
     */
    public void setTYPE(String value) {
        this.type = value;
    }

    /**
     * Eindeutige ID der Nachricht
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getMSGNR() {
        return msgnr;
    }

    /**
     * Sets the value of the msgnr property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     * @see #getMSGNR()
     */
    public void setMSGNR(BigInteger value) {
        this.msgnr = value;
    }

    /**
     * Text der Nachricht
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMESSAGE() {
        return message;
    }

    /**
     * Sets the value of the message property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getMESSAGE()
     */
    public void setMESSAGE(String value) {
        this.message = value;
    }

    /**
     * Nachrichtenklasse
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getID()
     */
    public void setID(String value) {
        this.id = value;
    }

}
