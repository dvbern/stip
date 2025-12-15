
package ch.dvbern.stip.api.sap.generated.business_partner;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BusinessPartnerSearch_Response complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="BusinessPartnerSearch_Response">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="BUSINESS_PARTNER" maxOccurs="unbounded" minOccurs="0">
 *           <complexType>
 *             <complexContent>
 *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 <sequence>
 *                   <element name="HEADER" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_HEADER"/>
 *                   <element name="ID_KEYS" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_ID_KEYS"/>
 *                   <element name="PERS_DATA" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_PERS_DATA" minOccurs="0"/>
 *                   <element name="ORG_DATA" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_ORG_DATA" minOccurs="0"/>
 *                   <element name="ADDRESS" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_ADDRESS" maxOccurs="unbounded" minOccurs="0"/>
 *                   <element name="COMMUNICATION" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_COMMUNICATION" maxOccurs="unbounded" minOccurs="0"/>
 *                   <element name="ADDITIONAL_DATA" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}AdditionalData" maxOccurs="unbounded" minOccurs="0"/>
 *                 </sequence>
 *               </restriction>
 *             </complexContent>
 *           </complexType>
 *         </element>
 *         <element name="RETURN_CODE" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}ReturnCode" maxOccurs="unbounded" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BusinessPartnerSearch_Response", propOrder = {
    "businesspartner",
    "returncode"
})
public class BusinessPartnerSearchResponse {

    @XmlElement(name = "BUSINESS_PARTNER")
    protected List<BUSINESSPARTNER> businesspartner;
    @XmlElement(name = "RETURN_CODE")
    protected List<ReturnCode> returncode;

    /**
     * Gets the value of the businesspartner property.
     *
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the businesspartner property.</p>
     *
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getBUSINESSPARTNER().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BUSINESSPARTNER }
     * </p>
     *
     *
     * @return
     *     The value of the businesspartner property.
     */
    public List<BUSINESSPARTNER> getBUSINESSPARTNER() {
        if (businesspartner == null) {
            businesspartner = new ArrayList<>();
        }
        return this.businesspartner;
    }

    /**
     * Gets the value of the returncode property.
     *
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the returncode property.</p>
     *
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getRETURNCODE().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReturnCode }
     * </p>
     *
     *
     * @return
     *     The value of the returncode property.
     */
    public List<ReturnCode> getRETURNCODE() {
        if (returncode == null) {
            returncode = new ArrayList<>();
        }
        return this.returncode;
    }

    public void setRETURNCODE(List<ReturnCode> value) {
        this.returncode = value;
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
     *         <element name="HEADER" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_HEADER"/>
     *         <element name="ID_KEYS" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_ID_KEYS"/>
     *         <element name="PERS_DATA" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_PERS_DATA" minOccurs="0"/>
     *         <element name="ORG_DATA" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_ORG_DATA" minOccurs="0"/>
     *         <element name="ADDRESS" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_ADDRESS" maxOccurs="unbounded" minOccurs="0"/>
     *         <element name="COMMUNICATION" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_COMMUNICATION" maxOccurs="unbounded" minOccurs="0"/>
     *         <element name="ADDITIONAL_DATA" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}AdditionalData" maxOccurs="unbounded" minOccurs="0"/>
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
        "header",
        "idkeys",
        "persdata",
        "orgdata",
        "address",
        "communication",
        "additionaldata"
    })
    public static class BUSINESSPARTNER {

        @XmlElement(name = "HEADER", required = true)
        protected BusinessPartnerHEADER header;
        @XmlElement(name = "ID_KEYS", required = true)
        protected BusinessPartnerIDKEYS idkeys;
        @XmlElement(name = "PERS_DATA")
        protected BusinessPartnerPERSDATA persdata;
        @XmlElement(name = "ORG_DATA")
        protected BusinessPartnerORGDATA orgdata;
        @XmlElement(name = "ADDRESS")
        protected List<BusinessPartnerADDRESS> address;
        @XmlElement(name = "COMMUNICATION")
        protected List<BusinessPartnerCOMMUNICATION> communication;
        @XmlElement(name = "ADDITIONAL_DATA")
        protected List<AdditionalData> additionaldata;

        /**
         * Gets the value of the header property.
         *
         * @return
         *     possible object is
         *     {@link BusinessPartnerHEADER }
         *
         */
        public BusinessPartnerHEADER getHEADER() {
            return header;
        }

        /**
         * Sets the value of the header property.
         *
         * @param value
         *     allowed object is
         *     {@link BusinessPartnerHEADER }
         *
         */
        public void setHEADER(BusinessPartnerHEADER value) {
            this.header = value;
        }

        /**
         * Gets the value of the idkeys property.
         *
         * @return
         *     possible object is
         *     {@link BusinessPartnerIDKEYS }
         *
         */
        public BusinessPartnerIDKEYS getIDKEYS() {
            return idkeys;
        }

        /**
         * Sets the value of the idkeys property.
         *
         * @param value
         *     allowed object is
         *     {@link BusinessPartnerIDKEYS }
         *
         */
        public void setIDKEYS(BusinessPartnerIDKEYS value) {
            this.idkeys = value;
        }

        /**
         * Gets the value of the persdata property.
         *
         * @return
         *     possible object is
         *     {@link BusinessPartnerPERSDATA }
         *
         */
        public BusinessPartnerPERSDATA getPERSDATA() {
            return persdata;
        }

        /**
         * Sets the value of the persdata property.
         *
         * @param value
         *     allowed object is
         *     {@link BusinessPartnerPERSDATA }
         *
         */
        public void setPERSDATA(BusinessPartnerPERSDATA value) {
            this.persdata = value;
        }

        /**
         * Gets the value of the orgdata property.
         *
         * @return
         *     possible object is
         *     {@link BusinessPartnerORGDATA }
         *
         */
        public BusinessPartnerORGDATA getORGDATA() {
            return orgdata;
        }

        /**
         * Sets the value of the orgdata property.
         *
         * @param value
         *     allowed object is
         *     {@link BusinessPartnerORGDATA }
         *
         */
        public void setORGDATA(BusinessPartnerORGDATA value) {
            this.orgdata = value;
        }

        /**
         * Gets the value of the address property.
         *
         * <p>This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the address property.</p>
         *
         * <p>
         * For example, to add a new item, do as follows:
         * </p>
         * <pre>
         * getADDRESS().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link BusinessPartnerADDRESS }
         * </p>
         *
         *
         * @return
         *     The value of the address property.
         */
        public List<BusinessPartnerADDRESS> getADDRESS() {
            if (address == null) {
                address = new ArrayList<>();
            }
            return this.address;
        }

        /**
         * Gets the value of the communication property.
         *
         * <p>This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the communication property.</p>
         *
         * <p>
         * For example, to add a new item, do as follows:
         * </p>
         * <pre>
         * getCOMMUNICATION().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link BusinessPartnerCOMMUNICATION }
         * </p>
         *
         *
         * @return
         *     The value of the communication property.
         */
        public List<BusinessPartnerCOMMUNICATION> getCOMMUNICATION() {
            if (communication == null) {
                communication = new ArrayList<>();
            }
            return this.communication;
        }

        /**
         * Gets the value of the additionaldata property.
         *
         * <p>This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the additionaldata property.</p>
         *
         * <p>
         * For example, to add a new item, do as follows:
         * </p>
         * <pre>
         * getADDITIONALDATA().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link AdditionalData }
         * </p>
         *
         *
         * @return
         *     The value of the additionaldata property.
         */
        public List<AdditionalData> getADDITIONALDATA() {
            if (additionaldata == null) {
                additionaldata = new ArrayList<>();
            }
            return this.additionaldata;
        }

    }

}
