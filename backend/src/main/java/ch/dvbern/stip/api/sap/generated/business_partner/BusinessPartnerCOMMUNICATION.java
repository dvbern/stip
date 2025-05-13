
package ch.dvbern.stip.api.sap.generated.business_partner;

import java.math.BigInteger;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BusinessPartner_COMMUNICATION complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="BusinessPartner_COMMUNICATION">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="CONSNUMBER">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               <totalDigits value="3"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="EMAIL">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="241"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="TELEPHONE">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="30"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="MOBILE">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="30"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="FAX">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="30"/>
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
@XmlType(name = "BusinessPartner_COMMUNICATION", propOrder = {
    "consnumber",
    "email",
    "telephone",
    "mobile",
    "fax"
})
public class BusinessPartnerCOMMUNICATION {

    /**
     * ID der Kommunikation
     *
     */
    @XmlElement(name = "CONSNUMBER", required = true)
    protected BigInteger consnumber;
    /**
     * Email-Adresse
     *
     */
    @XmlElement(name = "EMAIL", required = true)
    protected String email;
    /**
     * Telefon-Nr.: Vorwahl + Anschluß
     *
     */
    @XmlElement(name = "TELEPHONE", required = true)
    protected String telephone;
    /**
     * Mobiltelefon-Nr.: Vorwahl + Anschluß
     *
     */
    @XmlElement(name = "MOBILE", required = true)
    protected String mobile;
    /**
     * Fax-Nr.: Vorwahl + Anschluss
     *
     */
    @XmlElement(name = "FAX", required = true)
    protected String fax;

    /**
     * ID der Kommunikation
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getCONSNUMBER() {
        return consnumber;
    }

    /**
     * Sets the value of the consnumber property.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     * @see #getCONSNUMBER()
     */
    public void setCONSNUMBER(BigInteger value) {
        this.consnumber = value;
    }

    /**
     * Email-Adresse
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEMAIL() {
        return email;
    }

    /**
     * Sets the value of the email property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getEMAIL()
     */
    public void setEMAIL(String value) {
        this.email = value;
    }

    /**
     * Telefon-Nr.: Vorwahl + Anschluß
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTELEPHONE() {
        return telephone;
    }

    /**
     * Sets the value of the telephone property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getTELEPHONE()
     */
    public void setTELEPHONE(String value) {
        this.telephone = value;
    }

    /**
     * Mobiltelefon-Nr.: Vorwahl + Anschluß
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMOBILE() {
        return mobile;
    }

    /**
     * Sets the value of the mobile property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getMOBILE()
     */
    public void setMOBILE(String value) {
        this.mobile = value;
    }

    /**
     * Fax-Nr.: Vorwahl + Anschluss
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFAX() {
        return fax;
    }

    /**
     * Sets the value of the fax property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getFAX()
     */
    public void setFAX(String value) {
        this.fax = value;
    }

}
