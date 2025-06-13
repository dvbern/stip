
package ch.dvbern.stip.api.sap.generated.business_partner;

import java.math.BigDecimal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BusinessPartnerRead_Request complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="BusinessPartnerRead_Request">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="SENDER" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}SenderParms"/>
 *         <element name="FILTER_PARMS">
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
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BusinessPartnerRead_Request", propOrder = {
    "sender",
    "filterparms"
})
public class BusinessPartnerReadRequest {

    @XmlElement(name = "SENDER", required = true)
    protected SenderParms sender;
    @XmlElement(name = "FILTER_PARMS", required = true)
    protected FILTERPARMS filterparms;

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
    public static class FILTERPARMS {

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
         * Unternehmens-Identifikationsnummer (nur bei PARNT_CAT = 2 (Organisation))
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
         * Delivery-ID
         *
         */
        @XmlElement(name = "DELIVERY_ID", defaultValue = "0")
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
         * Unternehmens-Identifikationsnummer (nur bei PARNT_CAT = 2 (Organisation))
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
         * Delivery-ID
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
