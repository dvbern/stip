
package ch.dvbern.stip.api.sap.generated.business_partner;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.datatype.XMLGregorianCalendar;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BusinessPartnerSearch_Request complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="BusinessPartnerSearch_Request">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="SENDER" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}SenderParms"/>
 *         <element name="FILTER_PARMS">
 *           <complexType>
 *             <complexContent>
 *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 <sequence>
 *                   <element name="ONLY_WITH_REF" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   <element name="READ_ALL_DETAILS" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *                   <element name="OFFSET" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                         <totalDigits value="6"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="LIMIT" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                         <totalDigits value="6"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                 </sequence>
 *               </restriction>
 *             </complexContent>
 *           </complexType>
 *         </element>
 *         <element name="FILTER_PARMS_ID" minOccurs="0">
 *           <complexType>
 *             <complexContent>
 *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 <sequence>
 *                   <element name="BPARTNER" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         <maxLength value="10"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="EXT_ID" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         <maxLength value="20"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="AHV_NR" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         <maxLength value="13"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="UID_NR" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         <maxLength value="12"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="ZPV_NR" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         <maxLength value="10"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="DELIVERY_ID" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         <totalDigits value="19"/>
 *                         <fractionDigits value="0"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                 </sequence>
 *               </restriction>
 *             </complexContent>
 *           </complexType>
 *         </element>
 *         <element name="FILTER_PARMS_BASE" minOccurs="0">
 *           <complexType>
 *             <complexContent>
 *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 <sequence>
 *                   <element name="PARTN_CAT" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         <enumeration value="1"/>
 *                         <enumeration value="2"/>
 *                         <length value="1"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="NAME" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         <maxLength value="40"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="VORNAME" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         <maxLength value="40"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="GEBDAT" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *                 </sequence>
 *               </restriction>
 *             </complexContent>
 *           </complexType>
 *         </element>
 *         <element name="FILTER_PARMS_ADDR" minOccurs="0">
 *           <complexType>
 *             <complexContent>
 *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 <sequence>
 *                   <element name="ADR_KIND" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         <maxLength value="10"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="C_O_NAME" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         <maxLength value="40"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="CITY" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         <maxLength value="40"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="DISTRICT" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         <maxLength value="40"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="POSTL_COD1" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         <maxLength value="10"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="PO_BOX" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         <maxLength value="10"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="STREET" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         <maxLength value="40"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="HOUSE_NO" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         <maxLength value="10"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="COUNTRY" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         <maxLength value="3"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                 </sequence>
 *               </restriction>
 *             </complexContent>
 *           </complexType>
 *         </element>
 *         <element name="FILTER_PARMS_COMM" minOccurs="0">
 *           <complexType>
 *             <complexContent>
 *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 <sequence>
 *                   <element name="EMAIL" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         <maxLength value="241"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="TELEPHONE" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         <maxLength value="30"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="MOBILE" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         <maxLength value="30"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="FAX" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         <maxLength value="30"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                 </sequence>
 *               </restriction>
 *             </complexContent>
 *           </complexType>
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
@XmlType(name = "BusinessPartnerSearch_Request", propOrder = {
    "sender",
    "filterparms",
    "filterparmsid",
    "filterparmsbase",
    "filterparmsaddr",
    "filterparmscomm"
})
public class BusinessPartnerSearchRequest {

    @XmlElement(name = "SENDER", required = true)
    protected SenderParms sender;
    @XmlElement(name = "FILTER_PARMS", required = true)
    protected FILTERPARMS filterparms;
    @XmlElement(name = "FILTER_PARMS_ID")
    protected FILTERPARMSID filterparmsid;
    @XmlElement(name = "FILTER_PARMS_BASE")
    protected FILTERPARMSBASE filterparmsbase;
    @XmlElement(name = "FILTER_PARMS_ADDR")
    protected FILTERPARMSADDR filterparmsaddr;
    @XmlElement(name = "FILTER_PARMS_COMM")
    protected FILTERPARMSCOMM filterparmscomm;

    /**
     * Gets the value of the sender property.
     *
     * @return
     *     possible object is
     *     {@link SenderParms }
     *
     */
    public SenderParms getSENDER() {
        return sender;
    }

    /**
     * Sets the value of the sender property.
     *
     * @param value
     *     allowed object is
     *     {@link SenderParms }
     *
     */
    public void setSENDER(SenderParms value) {
        this.sender = value;
    }

    /**
     * Gets the value of the filterparms property.
     *
     * @return
     *     possible object is
     *     {@link FILTERPARMS }
     *
     */
    public FILTERPARMS getFILTERPARMS() {
        return filterparms;
    }

    /**
     * Sets the value of the filterparms property.
     *
     * @param value
     *     allowed object is
     *     {@link FILTERPARMS }
     *
     */
    public void setFILTERPARMS(FILTERPARMS value) {
        this.filterparms = value;
    }

    /**
     * Gets the value of the filterparmsid property.
     *
     * @return
     *     possible object is
     *     {@link FILTERPARMSID }
     *
     */
    public FILTERPARMSID getFILTERPARMSID() {
        return filterparmsid;
    }

    /**
     * Sets the value of the filterparmsid property.
     *
     * @param value
     *     allowed object is
     *     {@link FILTERPARMSID }
     *
     */
    public void setFILTERPARMSID(FILTERPARMSID value) {
        this.filterparmsid = value;
    }

    /**
     * Gets the value of the filterparmsbase property.
     *
     * @return
     *     possible object is
     *     {@link FILTERPARMSBASE }
     *
     */
    public FILTERPARMSBASE getFILTERPARMSBASE() {
        return filterparmsbase;
    }

    /**
     * Sets the value of the filterparmsbase property.
     *
     * @param value
     *     allowed object is
     *     {@link FILTERPARMSBASE }
     *
     */
    public void setFILTERPARMSBASE(FILTERPARMSBASE value) {
        this.filterparmsbase = value;
    }

    /**
     * Gets the value of the filterparmsaddr property.
     *
     * @return
     *     possible object is
     *     {@link FILTERPARMSADDR }
     *
     */
    public FILTERPARMSADDR getFILTERPARMSADDR() {
        return filterparmsaddr;
    }

    /**
     * Sets the value of the filterparmsaddr property.
     *
     * @param value
     *     allowed object is
     *     {@link FILTERPARMSADDR }
     *
     */
    public void setFILTERPARMSADDR(FILTERPARMSADDR value) {
        this.filterparmsaddr = value;
    }

    /**
     * Gets the value of the filterparmscomm property.
     *
     * @return
     *     possible object is
     *     {@link FILTERPARMSCOMM }
     *
     */
    public FILTERPARMSCOMM getFILTERPARMSCOMM() {
        return filterparmscomm;
    }

    /**
     * Sets the value of the filterparmscomm property.
     *
     * @param value
     *     allowed object is
     *     {@link FILTERPARMSCOMM }
     *
     */
    public void setFILTERPARMSCOMM(FILTERPARMSCOMM value) {
        this.filterparmscomm = value;
    }


    /**
     * <p>Java class for anonymous complex type</p>.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.</p>
     *
     * <pre>{@code
     * <complexType>
     *   <complexContent>
     *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       <sequence>
     *         <element name="ONLY_WITH_REF" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         <element name="READ_ALL_DETAILS" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
     *         <element name="OFFSET" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *               <totalDigits value="6"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="LIMIT" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *               <totalDigits value="6"/>
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
    @XmlType(name = "", propOrder = {
        "onlywithref",
        "readalldetails",
        "offset",
        "limit"
    })
    public static class FILTERPARMS {

        /**
         * Nur Geschäftspartner mit Referenz lesen
         *
         */
        @XmlElement(name = "ONLY_WITH_REF", defaultValue = "true")
        protected boolean onlywithref;
        /**
         * Alle Details lesen / (bei false nur Basis-Daten)
         *
         */
        @XmlElement(name = "READ_ALL_DETAILS", defaultValue = "false")
        protected boolean readalldetails;
        @XmlElement(name = "OFFSET", defaultValue = "0")
        protected BigInteger offset;
        @XmlElement(name = "LIMIT", defaultValue = "100")
        protected BigInteger limit;

        /**
         * Nur Geschäftspartner mit Referenz lesen
         *
         */
        public boolean isONLYWITHREF() {
            return onlywithref;
        }

        /**
         * Sets the value of the onlywithref property.
         *
         */
        public void setONLYWITHREF(boolean value) {
            this.onlywithref = value;
        }

        /**
         * Alle Details lesen / (bei false nur Basis-Daten)
         *
         */
        public boolean isREADALLDETAILS() {
            return readalldetails;
        }

        /**
         * Sets the value of the readalldetails property.
         *
         */
        public void setREADALLDETAILS(boolean value) {
            this.readalldetails = value;
        }

        /**
         * Gets the value of the offset property.
         *
         * @return
         *     possible object is
         *     {@link BigInteger }
         *
         */
        public BigInteger getOFFSET() {
            return offset;
        }

        /**
         * Sets the value of the offset property.
         *
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *
         */
        public void setOFFSET(BigInteger value) {
            this.offset = value;
        }

        /**
         * Gets the value of the limit property.
         *
         * @return
         *     possible object is
         *     {@link BigInteger }
         *
         */
        public BigInteger getLIMIT() {
            return limit;
        }

        /**
         * Sets the value of the limit property.
         *
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *
         */
        public void setLIMIT(BigInteger value) {
            this.limit = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type</p>.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.</p>
     *
     * <pre>{@code
     * <complexType>
     *   <complexContent>
     *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       <sequence>
     *         <element name="ADR_KIND" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               <maxLength value="10"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="C_O_NAME" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               <maxLength value="40"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="CITY" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               <maxLength value="40"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="DISTRICT" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               <maxLength value="40"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="POSTL_COD1" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               <maxLength value="10"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="PO_BOX" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               <maxLength value="10"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="STREET" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               <maxLength value="40"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="HOUSE_NO" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               <maxLength value="10"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="COUNTRY" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               <maxLength value="3"/>
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
    @XmlType(name = "", propOrder = {
        "adrkind",
        "coname",
        "city",
        "district",
        "postlcod1",
        "pobox",
        "street",
        "houseno",
        "country"
    })
    public static class FILTERPARMSADDR {

        /**
         * Adressart (XXDEFAULT = Standardadresse)
         *
         */
        @XmlElement(name = "ADR_KIND")
        protected String adrkind;
        /**
         * c/o-Name
         *
         */
        @XmlElement(name = "C_O_NAME")
        protected String coname;
        /**
         * Ort
         *
         */
        @XmlElement(name = "CITY")
        protected String city;
        @XmlElement(name = "DISTRICT")
        protected String district;
        /**
         * Postleitzahl des Orts
         *
         */
        @XmlElement(name = "POSTL_COD1")
        protected String postlcod1;
        @XmlElement(name = "PO_BOX")
        protected String pobox;
        /**
         * Straße
         *
         */
        @XmlElement(name = "STREET")
        protected String street;
        /**
         * Hausnummer
         *
         */
        @XmlElement(name = "HOUSE_NO")
        protected String houseno;
        /**
         * Länderschlüssel
         *
         */
        @XmlElement(name = "COUNTRY")
        protected String country;

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
         * Gets the value of the district property.
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
         * Gets the value of the pobox property.
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

    }


    /**
     * <p>Java class for anonymous complex type</p>.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.</p>
     *
     * <pre>{@code
     * <complexType>
     *   <complexContent>
     *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       <sequence>
     *         <element name="PARTN_CAT" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               <enumeration value="1"/>
     *               <enumeration value="2"/>
     *               <length value="1"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="NAME" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               <maxLength value="40"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="VORNAME" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               <maxLength value="40"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="GEBDAT" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
     *       </sequence>
     *     </restriction>
     *   </complexContent>
     * </complexType>
     * }</pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "partncat",
        "name",
        "vorname",
        "gebdat"
    })
    public static class FILTERPARMSBASE {

        /**
         * Geschäftspartnertyp
         *
         */
        @XmlElement(name = "PARTN_CAT")
        protected String partncat;
        /**
         * Nachname/Firmenname
         *
         */
        @XmlElement(name = "NAME")
        protected String name;
        /**
         * Vorname
         *
         */
        @XmlElement(name = "VORNAME")
        protected String vorname;
        /**
         * Geburtsdatum
         *
         */
        @XmlElement(name = "GEBDAT")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar gebdat;

        /**
         * Geschäftspartnertyp
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
         * Nachname/Firmenname
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getNAME() {
            return name;
        }

        /**
         * Sets the value of the name property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         * @see #getNAME()
         */
        public void setNAME(String value) {
            this.name = value;
        }

        /**
         * Vorname
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getVORNAME() {
            return vorname;
        }

        /**
         * Sets the value of the vorname property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         * @see #getVORNAME()
         */
        public void setVORNAME(String value) {
            this.vorname = value;
        }

        /**
         * Geburtsdatum
         *
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *
         */
        public XMLGregorianCalendar getGEBDAT() {
            return gebdat;
        }

        /**
         * Sets the value of the gebdat property.
         *
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *
         * @see #getGEBDAT()
         */
        public void setGEBDAT(XMLGregorianCalendar value) {
            this.gebdat = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type</p>.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.</p>
     *
     * <pre>{@code
     * <complexType>
     *   <complexContent>
     *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       <sequence>
     *         <element name="EMAIL" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               <maxLength value="241"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="TELEPHONE" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               <maxLength value="30"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="MOBILE" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               <maxLength value="30"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="FAX" minOccurs="0">
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
    @XmlType(name = "", propOrder = {
        "email",
        "telephone",
        "mobile",
        "fax"
    })
    public static class FILTERPARMSCOMM {

        /**
         * Email
         *
         */
        @XmlElement(name = "EMAIL")
        protected String email;
        /**
         * Telefon
         *
         */
        @XmlElement(name = "TELEPHONE")
        protected String telephone;
        /**
         * Mobile Nummer
         *
         */
        @XmlElement(name = "MOBILE")
        protected String mobile;
        /**
         * Fax
         *
         */
        @XmlElement(name = "FAX")
        protected String fax;

        /**
         * Email
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
         * Telefon
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
         * Mobile Nummer
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
         * Fax
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


    /**
     * <p>Java class for anonymous complex type</p>.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.</p>
     *
     * <pre>{@code
     * <complexType>
     *   <complexContent>
     *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       <sequence>
     *         <element name="BPARTNER" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               <maxLength value="10"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="EXT_ID" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               <maxLength value="20"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="AHV_NR" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               <maxLength value="13"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="UID_NR" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               <maxLength value="12"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="ZPV_NR" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               <maxLength value="10"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="DELIVERY_ID" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               <totalDigits value="19"/>
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
    @XmlType(name = "", propOrder = {
        "bpartner",
        "extid",
        "ahvnr",
        "uidnr",
        "zpvnr",
        "deliveryid"
    })
    public static class FILTERPARMSID {

        /**
         * SAP ID eines Business Partners
         *
         */
        @XmlElement(name = "BPARTNER")
        protected String bpartner;
        /**
         * Geschäftspartnernummer im externen System
         *
         */
        @XmlElement(name = "EXT_ID")
        protected String extid;
        /**
         * AHV-Nummer
         *
         */
        @XmlElement(name = "AHV_NR")
        protected String ahvnr;
        /**
         * Unternehmens-Identifikationsnummer
         *
         */
        @XmlElement(name = "UID_NR")
        protected String uidnr;
        /**
         * ZPV-Nummer
         *
         */
        @XmlElement(name = "ZPV_NR")
        protected String zpvnr;
        /**
         * Suche mit Delivery ID
         *
         */
        @XmlElement(name = "DELIVERY_ID")
        protected BigDecimal deliveryid;

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

        /**
         * Suche mit Delivery ID
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

    }

}
