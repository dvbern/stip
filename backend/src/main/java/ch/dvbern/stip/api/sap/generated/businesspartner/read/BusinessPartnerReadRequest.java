
package ch.dvbern.stip.api.sap.generated.businesspartner.read;

import java.math.BigDecimal;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für BusinessPartnerRead_Request complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="BusinessPartnerRead_Request"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SENDER" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}SenderParms"/&gt;
 *         &lt;element name="FILTER_PARMS"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="BPARTNER" minOccurs="0"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="10"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="EXT_ID" minOccurs="0"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="20"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="AHV_NR" minOccurs="0"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="13"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="UID_NR" minOccurs="0"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="12"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="ZPV_NR" minOccurs="0"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="10"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="DELIVERY_ID" minOccurs="0"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *                         &lt;totalDigits value="19"/&gt;
 *                         &lt;fractionDigits value="0"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */

@XmlRootElement(name = "BusinessPartnerRead_Request", namespace = "urn:be.ch:KTBE_MDG:BUSINESS_PARTNER")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BusinessPartnerRead_Request", namespace = "urn:be.ch:KTBE_MDG:BUSINESS_PARTNER", propOrder = {
    "sender",
    "filterparms"
})
public class BusinessPartnerReadRequest {

    @XmlElement(name = "SENDER", required = true)
    protected SenderParms sender;
    @XmlElement(name = "FILTER_PARMS", required = true)
    protected BusinessPartnerReadRequest.FILTERPARMS filterparms;

    /**
     * Ruft den Wert der sender-Eigenschaft ab.
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
     * Legt den Wert der sender-Eigenschaft fest.
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
     * Ruft den Wert der filterparms-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link BusinessPartnerReadRequest.FILTERPARMS }
     *
     */
    public BusinessPartnerReadRequest.FILTERPARMS getFILTERPARMS() {
        return filterparms;
    }

    /**
     * Legt den Wert der filterparms-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link BusinessPartnerReadRequest.FILTERPARMS }
     *
     */
    public void setFILTERPARMS(BusinessPartnerReadRequest.FILTERPARMS value) {
        this.filterparms = value;
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
     *         &lt;element name="BPARTNER" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="10"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="EXT_ID" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="20"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="AHV_NR" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="13"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="UID_NR" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="12"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="ZPV_NR" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="10"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="DELIVERY_ID" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
     *               &lt;totalDigits value="19"/&gt;
     *               &lt;fractionDigits value="0"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
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
        "bpartner",
        "extid",
        "ahvnr",
        "uidnr",
        "zpvnr",
        "deliveryid"
    })
    public static class FILTERPARMS {

        @XmlElement(name = "BPARTNER")
        protected String bpartner;
        @XmlElement(name = "EXT_ID")
        protected String extid;
        @XmlElement(name = "AHV_NR")
        protected String ahvnr;
        @XmlElement(name = "UID_NR")
        protected String uidnr;
        @XmlElement(name = "ZPV_NR")
        protected String zpvnr;
        @XmlElement(name = "DELIVERY_ID", defaultValue = "0")
        protected BigDecimal deliveryid;

        /**
         * Ruft den Wert der bpartner-Eigenschaft ab.
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
         * Legt den Wert der bpartner-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setBPARTNER(String value) {
            this.bpartner = value;
        }

        /**
         * Ruft den Wert der extid-Eigenschaft ab.
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
         * Legt den Wert der extid-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setEXTID(String value) {
            this.extid = value;
        }

        /**
         * Ruft den Wert der ahvnr-Eigenschaft ab.
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
         * Legt den Wert der ahvnr-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setAHVNR(String value) {
            this.ahvnr = value;
        }

        /**
         * Ruft den Wert der uidnr-Eigenschaft ab.
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
         * Legt den Wert der uidnr-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setUIDNR(String value) {
            this.uidnr = value;
        }

        /**
         * Ruft den Wert der zpvnr-Eigenschaft ab.
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
         * Legt den Wert der zpvnr-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setZPVNR(String value) {
            this.zpvnr = value;
        }

        /**
         * Ruft den Wert der deliveryid-Eigenschaft ab.
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
         * Legt den Wert der deliveryid-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *
         */
        public void setDELIVERYID(BigDecimal value) {
            this.deliveryid = value;
        }

    }

}
