
package ch.dvbern.stip.api.sap.generated.business_partner;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BusinessPartner_ADDRESS complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="BusinessPartner_ADDRESS">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="ADR_KIND">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="10"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="C_O_NAME">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="40"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="CITY">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="40"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="DISTRICT">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="40"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="POSTL_COD1">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="10"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="PO_BOX">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="10"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="STREET">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="40"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="HOUSE_NO">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="10"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="COUNTRY">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="3"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="POSTL_COD2" minOccurs="0">
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
@XmlType(name = "BusinessPartner_ADDRESS", propOrder = {
    "adrkind",
    "coname",
    "city",
    "district",
    "postlcod1",
    "pobox",
    "street",
    "houseno",
    "country",
    "postlcod2"
})
public class BusinessPartnerADDRESS {

    /**
     * Adressart (XXDEFAULT = Standardadresse)
     *
     */
    @XmlElement(name = "ADR_KIND", required = true)
    protected String adrkind;
    /**
     * c/o-Name
     *
     */
    @XmlElement(name = "C_O_NAME", required = true)
    protected String coname;
    /**
     * Ort
     *
     */
    @XmlElement(name = "CITY", required = true)
    protected String city;
    /**
     * Ortsteil
     *
     */
    @XmlElement(name = "DISTRICT", required = true)
    protected String district;
    /**
     * Postleitzahl des Orts
     *
     */
    @XmlElement(name = "POSTL_COD1", required = true)
    protected String postlcod1;
    /**
     * Postfach
     *
     */
    @XmlElement(name = "PO_BOX", required = true)
    protected String pobox;
    /**
     * Straße
     *
     */
    @XmlElement(name = "STREET", required = true)
    protected String street;
    /**
     * Hausnummer
     *
     */
    @XmlElement(name = "HOUSE_NO", required = true)
    protected String houseno;
    /**
     * Länderschlüssel
     *
     */
    @XmlElement(name = "COUNTRY", required = true)
    protected String country;
    /**
     * Postleitzahl zum Postfach
     *
     */
    @XmlElement(name = "POSTL_COD2")
    protected String postlcod2;

    /**
     * Adressart (XXDEFAULT = Standardadresse)
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getADRKIND() {
        return adrkind;
    }

    /**
     * Sets the value of the adrkind property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getADRKIND()
     */
    public void setADRKIND(String value) {
        this.adrkind = value;
    }

    /**
     * c/o-Name
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCONAME() {
        return coname;
    }

    /**
     * Sets the value of the coname property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getCONAME()
     */
    public void setCONAME(String value) {
        this.coname = value;
    }

    /**
     * Ort
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCITY() {
        return city;
    }

    /**
     * Sets the value of the city property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getCITY()
     */
    public void setCITY(String value) {
        this.city = value;
    }

    /**
     * Ortsteil
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDISTRICT() {
        return district;
    }

    /**
     * Sets the value of the district property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getDISTRICT()
     */
    public void setDISTRICT(String value) {
        this.district = value;
    }

    /**
     * Postleitzahl des Orts
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPOSTLCOD1() {
        return postlcod1;
    }

    /**
     * Sets the value of the postlcod1 property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getPOSTLCOD1()
     */
    public void setPOSTLCOD1(String value) {
        this.postlcod1 = value;
    }

    /**
     * Postfach
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPOBOX() {
        return pobox;
    }

    /**
     * Sets the value of the pobox property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getPOBOX()
     */
    public void setPOBOX(String value) {
        this.pobox = value;
    }

    /**
     * Straße
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSTREET() {
        return street;
    }

    /**
     * Sets the value of the street property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getSTREET()
     */
    public void setSTREET(String value) {
        this.street = value;
    }

    /**
     * Hausnummer
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getHOUSENO() {
        return houseno;
    }

    /**
     * Sets the value of the houseno property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getHOUSENO()
     */
    public void setHOUSENO(String value) {
        this.houseno = value;
    }

    /**
     * Länderschlüssel
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCOUNTRY() {
        return country;
    }

    /**
     * Sets the value of the country property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getCOUNTRY()
     */
    public void setCOUNTRY(String value) {
        this.country = value;
    }

    /**
     * Postleitzahl zum Postfach
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPOSTLCOD2() {
        return postlcod2;
    }

    /**
     * Sets the value of the postlcod2 property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getPOSTLCOD2()
     */
    public void setPOSTLCOD2(String value) {
        this.postlcod2 = value;
    }

}
