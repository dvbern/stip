
package ch.dvbern.stip.api.sap.generated.business_partner;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BusinessPartner_HEADER complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="BusinessPartner_HEADER">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="BPARTNER">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="10"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="PARTN_CAT">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="10"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="PARTN_TYP">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="4"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="PARTN_GRP" minOccurs="0">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="4"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="PARTN_TAXKD" minOccurs="0">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <length value="1"/>
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
@XmlType(name = "BusinessPartner_HEADER", propOrder = {
    "bpartner",
    "partncat",
    "partntyp",
    "partngrp",
    "partntaxkd"
})
public class BusinessPartnerHEADER {

    /**
     * SAP ID eines Business Partners
     *
     */
    @XmlElement(name = "BPARTNER", required = true)
    protected String bpartner;
    /**
     * Geschäftspartnertyp: (1: Person, 2: Organisation )
     *
     */
    @XmlElement(name = "PARTN_CAT", required = true)
    protected String partncat;
    /**
     * Geschäftspartnerart
     *
     */
    @XmlElement(name = "PARTN_TYP", required = true)
    protected String partntyp;
    /**
     * Geschäftspartnergruppierung
     *
     */
    @XmlElement(name = "PARTN_GRP")
    protected String partngrp;
    @XmlElement(name = "PARTN_TAXKD")
    protected String partntaxkd;

    /**
     * SAP ID eines Business Partners
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBPARTNER() {
        return bpartner;
    }

    /**
     * Sets the value of the bpartner property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getBPARTNER()
     */
    public void setBPARTNER(String value) {
        this.bpartner = value;
    }

    /**
     * Geschäftspartnertyp: (1: Person, 2: Organisation )
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPARTNCAT() {
        return partncat;
    }

    /**
     * Sets the value of the partncat property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getPARTNCAT()
     */
    public void setPARTNCAT(String value) {
        this.partncat = value;
    }

    /**
     * Geschäftspartnerart
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPARTNTYP() {
        return partntyp;
    }

    /**
     * Sets the value of the partntyp property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getPARTNTYP()
     */
    public void setPARTNTYP(String value) {
        this.partntyp = value;
    }

    /**
     * Geschäftspartnergruppierung
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPARTNGRP() {
        return partngrp;
    }

    /**
     * Sets the value of the partngrp property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getPARTNGRP()
     */
    public void setPARTNGRP(String value) {
        this.partngrp = value;
    }

    /**
     * Gets the value of the partntaxkd property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPARTNTAXKD() {
        return partntaxkd;
    }

    /**
     * Sets the value of the partntaxkd property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPARTNTAXKD(String value) {
        this.partntaxkd = value;
    }

}
