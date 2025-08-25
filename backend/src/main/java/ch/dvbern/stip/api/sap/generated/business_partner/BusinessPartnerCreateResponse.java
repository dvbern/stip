
package ch.dvbern.stip.api.sap.generated.business_partner;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BusinessPartnerCreate_Response complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="BusinessPartnerCreate_Response">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="BUSINESS_PARTNER" minOccurs="0">
 *           <complexType>
 *             <complexContent>
 *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 <sequence>
 *                   <element name="HEADER" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_HEADER"/>
 *                   <element name="ID_KEYS" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_ID_KEYS"/>
 *                   <element name="PERS_DATA" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_PERS_DATA" minOccurs="0"/>
 *                   <element name="ORG_DATA" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}BusinessPartner_ORG_DATA" minOccurs="0"/>
 *                 </sequence>
 *               </restriction>
 *             </complexContent>
 *           </complexType>
 *         </element>
 *         <element name="RETURN_CODE" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}ReturnCode" maxOccurs="unbounded"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BusinessPartnerCreate_Response", propOrder = {
    "businesspartner",
    "returncode"
})
public class BusinessPartnerCreateResponse {

    @XmlElement(name = "BUSINESS_PARTNER")
    protected BUSINESSPARTNER businesspartner;
    @XmlElement(name = "RETURN_CODE", required = true)
    protected List<ReturnCode> returncode;

    /**
     * Gets the value of the businesspartner property.
     *
     * @return
     *     possible object is
     *     {@link BUSINESSPARTNER }
     *
     */
    public BUSINESSPARTNER getBUSINESSPARTNER() {
        return businesspartner;
    }

    /**
     * Sets the value of the businesspartner property.
     *
     * @param value
     *     allowed object is
     *     {@link BUSINESSPARTNER }
     *
     */
    public void setBUSINESSPARTNER(BUSINESSPARTNER value) {
        this.businesspartner = value;
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
        "orgdata"
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

    }

}
