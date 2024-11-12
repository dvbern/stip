
package ch.dvbern.stip.api.sap.generated.businesspartner.read;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für BusinessPartnerRead_Response complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="BusinessPartnerRead_Response"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="BUSINESS_PARTNER" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="HEADER" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_HEADER"/&gt;
 *                   &lt;element name="ID_KEYS" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_ID_KEYS"/&gt;
 *                   &lt;element name="PERS_DATA" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_PERS_DATA" minOccurs="0"/&gt;
 *                   &lt;element name="ORG_DATA" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_ORG_DATA" minOccurs="0"/&gt;
 *                   &lt;element name="ADDRESS" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_ADDRESS" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                   &lt;element name="COMMUNICATION" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_COMMUNICATION" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                   &lt;element name="PAYMENT_DETAIL" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_PAYMENT_DETAIL" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                   &lt;element name="ADDITIONAL_DATA" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}AdditionalData" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="RETURN_CODE" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}ReturnCode" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BusinessPartnerRead_Response", namespace = "urn:be.ch:KTBE_MDG:BUSINESS_PARTNER", propOrder = {
    "businesspartner",
    "returncode"
})
public class BusinessPartnerReadResponse {

    @XmlElement(name = "BUSINESS_PARTNER")
    protected BusinessPartnerReadResponse.BUSINESSPARTNER businesspartner;
    @XmlElement(name = "RETURN_CODE")
    protected List<ReturnCode> returncode;

    /**
     * Ruft den Wert der businesspartner-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link BusinessPartnerReadResponse.BUSINESSPARTNER }
     *
     */
    public BusinessPartnerReadResponse.BUSINESSPARTNER getBUSINESSPARTNER() {
        return businesspartner;
    }

    /**
     * Legt den Wert der businesspartner-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link BusinessPartnerReadResponse.BUSINESSPARTNER }
     *
     */
    public void setBUSINESSPARTNER(BusinessPartnerReadResponse.BUSINESSPARTNER value) {
        this.businesspartner = value;
    }

    /**
     * Gets the value of the returncode property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the returncode property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRETURNCODE().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReturnCode }
     *
     *
     */
    public List<ReturnCode> getRETURNCODE() {
        if (returncode == null) {
            returncode = new ArrayList<ReturnCode>();
        }
        return this.returncode;
    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     *
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     *
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="HEADER" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_HEADER"/&gt;
     *         &lt;element name="ID_KEYS" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_ID_KEYS"/&gt;
     *         &lt;element name="PERS_DATA" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_PERS_DATA" minOccurs="0"/&gt;
     *         &lt;element name="ORG_DATA" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_ORG_DATA" minOccurs="0"/&gt;
     *         &lt;element name="ADDRESS" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_ADDRESS" maxOccurs="unbounded" minOccurs="0"/&gt;
     *         &lt;element name="COMMUNICATION" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_COMMUNICATION" maxOccurs="unbounded" minOccurs="0"/&gt;
     *         &lt;element name="PAYMENT_DETAIL" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_PAYMENT_DETAIL" maxOccurs="unbounded" minOccurs="0"/&gt;
     *         &lt;element name="ADDITIONAL_DATA" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}AdditionalData" maxOccurs="unbounded" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
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
        "paymentdetail",
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
        @XmlElement(name = "PAYMENT_DETAIL")
        protected List<BusinessPartnerPAYMENTDETAIL> paymentdetail;
        @XmlElement(name = "ADDITIONAL_DATA")
        protected List<AdditionalData> additionaldata;

        /**
         * Ruft den Wert der header-Eigenschaft ab.
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
         * Legt den Wert der header-Eigenschaft fest.
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
         * Ruft den Wert der idkeys-Eigenschaft ab.
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
         * Legt den Wert der idkeys-Eigenschaft fest.
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
         * Ruft den Wert der persdata-Eigenschaft ab.
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
         * Legt den Wert der persdata-Eigenschaft fest.
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
         * Ruft den Wert der orgdata-Eigenschaft ab.
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
         * Legt den Wert der orgdata-Eigenschaft fest.
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
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the Jakarta XML Binding object.
         * This is why there is not a <CODE>set</CODE> method for the address property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getADDRESS().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link BusinessPartnerADDRESS }
         *
         *
         */
        public List<BusinessPartnerADDRESS> getADDRESS() {
            if (address == null) {
                address = new ArrayList<BusinessPartnerADDRESS>();
            }
            return this.address;
        }

        /**
         * Gets the value of the communication property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the Jakarta XML Binding object.
         * This is why there is not a <CODE>set</CODE> method for the communication property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCOMMUNICATION().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link BusinessPartnerCOMMUNICATION }
         *
         *
         */
        public List<BusinessPartnerCOMMUNICATION> getCOMMUNICATION() {
            if (communication == null) {
                communication = new ArrayList<BusinessPartnerCOMMUNICATION>();
            }
            return this.communication;
        }

        /**
         * Gets the value of the paymentdetail property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the Jakarta XML Binding object.
         * This is why there is not a <CODE>set</CODE> method for the paymentdetail property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPAYMENTDETAIL().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link BusinessPartnerPAYMENTDETAIL }
         *
         *
         */
        public List<BusinessPartnerPAYMENTDETAIL> getPAYMENTDETAIL() {
            if (paymentdetail == null) {
                paymentdetail = new ArrayList<BusinessPartnerPAYMENTDETAIL>();
            }
            return this.paymentdetail;
        }

        /**
         * Gets the value of the additionaldata property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the Jakarta XML Binding object.
         * This is why there is not a <CODE>set</CODE> method for the additionaldata property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getADDITIONALDATA().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link AdditionalData }
         *
         *
         */
        public List<AdditionalData> getADDITIONALDATA() {
            if (additionaldata == null) {
                additionaldata = new ArrayList<AdditionalData>();
            }
            return this.additionaldata;
        }

    }

}
