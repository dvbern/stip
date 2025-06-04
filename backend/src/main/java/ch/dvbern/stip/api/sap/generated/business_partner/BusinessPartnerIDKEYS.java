
package ch.dvbern.stip.api.sap.generated.business_partner;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BusinessPartner_ID_KEYS complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="BusinessPartner_ID_KEYS">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="EXT_ID">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="20"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="AHV_NR">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="13"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="UID_NR">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="12"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="ZPV_NR">
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
@XmlType(name = "BusinessPartner_ID_KEYS", propOrder = {
    "extid",
    "ahvnr",
    "uidnr",
    "zpvnr"
})
public class BusinessPartnerIDKEYS {

    /**
     * Geschäftspartnernummer im externen System
     *
     */
    @XmlElement(name = "EXT_ID", required = true)
    protected String extid;
    /**
     * AHV-Nummer
     *
     */
    @XmlElement(name = "AHV_NR", required = true)
    protected String ahvnr;
    /**
     * Unternehmens-Identifikationsnummer
     *
     */
    @XmlElement(name = "UID_NR", required = true)
    protected String uidnr;
    /**
     * ZPV-Nummer
     *
     */
    @XmlElement(name = "ZPV_NR", required = true)
    protected String zpvnr;

    /**
     * Geschäftspartnernummer im externen System
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEXTID() {
        return extid;
    }

    /**
     * Sets the value of the extid property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getEXTID()
     */
    public void setEXTID(String value) {
        this.extid = value;
    }

    /**
     * AHV-Nummer
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAHVNR() {
        return ahvnr;
    }

    /**
     * Sets the value of the ahvnr property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getAHVNR()
     */
    public void setAHVNR(String value) {
        this.ahvnr = value;
    }

    /**
     * Unternehmens-Identifikationsnummer
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUIDNR() {
        return uidnr;
    }

    /**
     * Sets the value of the uidnr property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getUIDNR()
     */
    public void setUIDNR(String value) {
        this.uidnr = value;
    }

    /**
     * ZPV-Nummer
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getZPVNR() {
        return zpvnr;
    }

    /**
     * Sets the value of the zpvnr property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getZPVNR()
     */
    public void setZPVNR(String value) {
        this.zpvnr = value;
    }

}
