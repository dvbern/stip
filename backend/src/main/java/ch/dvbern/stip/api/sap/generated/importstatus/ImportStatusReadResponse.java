
package ch.dvbern.stip.api.sap.generated.importstatus;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Response für SST-073
 *
 * <p>Java-Klasse für ImportStatusRead_Response complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="ImportStatusRead_Response"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DELIVERY" maxOccurs="unbounded" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="DELIVERY_ID"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *                         &lt;totalDigits value="19"/&gt;
 *                         &lt;fractionDigits value="0"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="TYPE"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="40"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="STATUS"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                         &lt;totalDigits value="3"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="KTEXT"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                         &lt;maxLength value="40"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="LAST_ACTION" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *                   &lt;element name="COUNTER_SUCCESS"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                         &lt;totalDigits value="6"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="COUNTER_ERROR"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                         &lt;totalDigits value="6"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="COUNTER_INITIAL"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                         &lt;totalDigits value="6"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="COUNTER_TOTAL"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                         &lt;totalDigits value="6"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="POSITION" maxOccurs="unbounded" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="POSID"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *                                   &lt;totalDigits value="10"/&gt;
 *                                   &lt;fractionDigits value="0"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="STATUS"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="10"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="SAP_KEY"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="40"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="EXT_KEY"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="40"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="LOGS" maxOccurs="unbounded" minOccurs="0"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="DATETIME" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *                                       &lt;element name="TYPE"&gt;
 *                                         &lt;simpleType&gt;
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                             &lt;maxLength value="1"/&gt;
 *                                           &lt;/restriction&gt;
 *                                         &lt;/simpleType&gt;
 *                                       &lt;/element&gt;
 *                                       &lt;element name="MSG_NR"&gt;
 *                                         &lt;simpleType&gt;
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                                             &lt;totalDigits value="3"/&gt;
 *                                           &lt;/restriction&gt;
 *                                         &lt;/simpleType&gt;
 *                                       &lt;/element&gt;
 *                                       &lt;element name="MESSAGE"&gt;
 *                                         &lt;simpleType&gt;
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                             &lt;maxLength value="220"/&gt;
 *                                           &lt;/restriction&gt;
 *                                         &lt;/simpleType&gt;
 *                                       &lt;/element&gt;
 *                                       &lt;element name="ID" minOccurs="0"&gt;
 *                                         &lt;simpleType&gt;
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                             &lt;maxLength value="20"/&gt;
 *                                           &lt;/restriction&gt;
 *                                         &lt;/simpleType&gt;
 *                                       &lt;/element&gt;
 *                                     &lt;/sequence&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="RETURN_CODE" type="{urn:be.ch:KTBE_ERP_FI:GENERAL}ReturnCodeID" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlRootElement(name = "ImportStatusRead_Response", namespace = "urn:be.ch:KTBE_ERP_FI:IMPORT_STATUS")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ImportStatusRead_Response", namespace = "urn:be.ch:KTBE_ERP_FI:IMPORT_STATUS", propOrder = {
    "delivery",
    "returncode"
})
public class ImportStatusReadResponse {

    @XmlElement(name = "DELIVERY")
    protected List<ImportStatusReadResponse.DELIVERY> delivery;
    @XmlElement(name = "RETURN_CODE")
    protected List<ReturnCodeID> returncode;

    /**
     * Gets the value of the delivery property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the delivery property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDELIVERY().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ImportStatusReadResponse.DELIVERY }
     *
     *
     */
    public List<ImportStatusReadResponse.DELIVERY> getDELIVERY() {
        if (delivery == null) {
            delivery = new ArrayList<ImportStatusReadResponse.DELIVERY>();
        }
        return this.delivery;
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
     * {@link ReturnCodeID }
     *
     *
     */
    public List<ReturnCodeID> getRETURNCODE() {
        if (returncode == null) {
            returncode = new ArrayList<ReturnCodeID>();
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
     *         &lt;element name="DELIVERY_ID"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
     *               &lt;totalDigits value="19"/&gt;
     *               &lt;fractionDigits value="0"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="TYPE"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="40"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="STATUS"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *               &lt;totalDigits value="3"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="KTEXT"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *               &lt;maxLength value="40"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="LAST_ACTION" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
     *         &lt;element name="COUNTER_SUCCESS"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *               &lt;totalDigits value="6"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="COUNTER_ERROR"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *               &lt;totalDigits value="6"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="COUNTER_INITIAL"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *               &lt;totalDigits value="6"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="COUNTER_TOTAL"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *               &lt;totalDigits value="6"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="POSITION" maxOccurs="unbounded" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="POSID"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
     *                         &lt;totalDigits value="10"/&gt;
     *                         &lt;fractionDigits value="0"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="STATUS"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="10"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="SAP_KEY"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="40"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="EXT_KEY"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="40"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="LOGS" maxOccurs="unbounded" minOccurs="0"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="DATETIME" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
     *                             &lt;element name="TYPE"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                   &lt;maxLength value="1"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="MSG_NR"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *                                   &lt;totalDigits value="3"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="MESSAGE"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                   &lt;maxLength value="220"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="ID" minOccurs="0"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                   &lt;maxLength value="20"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                           &lt;/sequence&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
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
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "deliveryid",
        "type",
        "status",
        "ktext",
        "lastaction",
        "countersuccess",
        "countererror",
        "counterinitial",
        "countertotal",
        "position"
    })
    public static class DELIVERY {

        @XmlElement(name = "DELIVERY_ID", required = true)
        protected BigDecimal deliveryid;
        @XmlElement(name = "TYPE", required = true)
        protected String type;
        @XmlElement(name = "STATUS", required = true)
        protected BigInteger status;
        @XmlElement(name = "KTEXT", required = true)
        protected String ktext;
        @XmlElement(name = "LAST_ACTION", required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar lastaction;
        @XmlElement(name = "COUNTER_SUCCESS", required = true)
        protected BigInteger countersuccess;
        @XmlElement(name = "COUNTER_ERROR", required = true)
        protected BigInteger countererror;
        @XmlElement(name = "COUNTER_INITIAL", required = true)
        protected BigInteger counterinitial;
        @XmlElement(name = "COUNTER_TOTAL", required = true)
        protected BigInteger countertotal;
        @XmlElement(name = "POSITION")
        protected List<ImportStatusReadResponse.DELIVERY.POSITION> position;

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

        /**
         * Ruft den Wert der type-Eigenschaft ab.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getTYPE() {
            return type;
        }

        /**
         * Legt den Wert der type-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setTYPE(String value) {
            this.type = value;
        }

        /**
         * Ruft den Wert der status-Eigenschaft ab.
         *
         * @return
         *     possible object is
         *     {@link BigInteger }
         *
         */
        public BigInteger getSTATUS() {
            return status;
        }

        /**
         * Legt den Wert der status-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *
         */
        public void setSTATUS(BigInteger value) {
            this.status = value;
        }

        /**
         * Ruft den Wert der ktext-Eigenschaft ab.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getKTEXT() {
            return ktext;
        }

        /**
         * Legt den Wert der ktext-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         */
        public void setKTEXT(String value) {
            this.ktext = value;
        }

        /**
         * Ruft den Wert der lastaction-Eigenschaft ab.
         *
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *
         */
        public XMLGregorianCalendar getLASTACTION() {
            return lastaction;
        }

        /**
         * Legt den Wert der lastaction-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *
         */
        public void setLASTACTION(XMLGregorianCalendar value) {
            this.lastaction = value;
        }

        /**
         * Ruft den Wert der countersuccess-Eigenschaft ab.
         *
         * @return
         *     possible object is
         *     {@link BigInteger }
         *
         */
        public BigInteger getCOUNTERSUCCESS() {
            return countersuccess;
        }

        /**
         * Legt den Wert der countersuccess-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *
         */
        public void setCOUNTERSUCCESS(BigInteger value) {
            this.countersuccess = value;
        }

        /**
         * Ruft den Wert der countererror-Eigenschaft ab.
         *
         * @return
         *     possible object is
         *     {@link BigInteger }
         *
         */
        public BigInteger getCOUNTERERROR() {
            return countererror;
        }

        /**
         * Legt den Wert der countererror-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *
         */
        public void setCOUNTERERROR(BigInteger value) {
            this.countererror = value;
        }

        /**
         * Ruft den Wert der counterinitial-Eigenschaft ab.
         *
         * @return
         *     possible object is
         *     {@link BigInteger }
         *
         */
        public BigInteger getCOUNTERINITIAL() {
            return counterinitial;
        }

        /**
         * Legt den Wert der counterinitial-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *
         */
        public void setCOUNTERINITIAL(BigInteger value) {
            this.counterinitial = value;
        }

        /**
         * Ruft den Wert der countertotal-Eigenschaft ab.
         *
         * @return
         *     possible object is
         *     {@link BigInteger }
         *
         */
        public BigInteger getCOUNTERTOTAL() {
            return countertotal;
        }

        /**
         * Legt den Wert der countertotal-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *
         */
        public void setCOUNTERTOTAL(BigInteger value) {
            this.countertotal = value;
        }

        /**
         * Gets the value of the position property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the Jakarta XML Binding object.
         * This is why there is not a <CODE>set</CODE> method for the position property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPOSITION().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ImportStatusReadResponse.DELIVERY.POSITION }
         *
         *
         */
        public List<ImportStatusReadResponse.DELIVERY.POSITION> getPOSITION() {
            if (position == null) {
                position = new ArrayList<ImportStatusReadResponse.DELIVERY.POSITION>();
            }
            return this.position;
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
         *         &lt;element name="POSID"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
         *               &lt;totalDigits value="10"/&gt;
         *               &lt;fractionDigits value="0"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="STATUS"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="10"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="SAP_KEY"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="40"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="EXT_KEY"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="40"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="LOGS" maxOccurs="unbounded" minOccurs="0"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="DATETIME" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
         *                   &lt;element name="TYPE"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                         &lt;maxLength value="1"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="MSG_NR"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
         *                         &lt;totalDigits value="3"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="MESSAGE"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                         &lt;maxLength value="220"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="ID" minOccurs="0"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                         &lt;maxLength value="20"/&gt;
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
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "posid",
            "status",
            "sapkey",
            "extkey",
            "logs"
        })
        public static class POSITION {

            @XmlElement(name = "POSID", required = true)
            protected BigDecimal posid;
            @XmlElement(name = "STATUS", required = true)
            protected String status;
            @XmlElement(name = "SAP_KEY", required = true)
            protected String sapkey;
            @XmlElement(name = "EXT_KEY", required = true)
            protected String extkey;
            @XmlElement(name = "LOGS")
            protected List<ImportStatusReadResponse.DELIVERY.POSITION.LOGS> logs;

            /**
             * Ruft den Wert der posid-Eigenschaft ab.
             *
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *
             */
            public BigDecimal getPOSID() {
                return posid;
            }

            /**
             * Legt den Wert der posid-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *
             */
            public void setPOSID(BigDecimal value) {
                this.posid = value;
            }

            /**
             * Ruft den Wert der status-Eigenschaft ab.
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getSTATUS() {
                return status;
            }

            /**
             * Legt den Wert der status-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setSTATUS(String value) {
                this.status = value;
            }

            /**
             * Ruft den Wert der sapkey-Eigenschaft ab.
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getSAPKEY() {
                return sapkey;
            }

            /**
             * Legt den Wert der sapkey-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setSAPKEY(String value) {
                this.sapkey = value;
            }

            /**
             * Ruft den Wert der extkey-Eigenschaft ab.
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getEXTKEY() {
                return extkey;
            }

            /**
             * Legt den Wert der extkey-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setEXTKEY(String value) {
                this.extkey = value;
            }

            /**
             * Gets the value of the logs property.
             *
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the Jakarta XML Binding object.
             * This is why there is not a <CODE>set</CODE> method for the logs property.
             *
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getLOGS().add(newItem);
             * </pre>
             *
             *
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link ImportStatusReadResponse.DELIVERY.POSITION.LOGS }
             *
             *
             */
            public List<ImportStatusReadResponse.DELIVERY.POSITION.LOGS> getLOGS() {
                if (logs == null) {
                    logs = new ArrayList<ImportStatusReadResponse.DELIVERY.POSITION.LOGS>();
                }
                return this.logs;
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
             *         &lt;element name="DATETIME" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
             *         &lt;element name="TYPE"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *               &lt;maxLength value="1"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="MSG_NR"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
             *               &lt;totalDigits value="3"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="MESSAGE"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *               &lt;maxLength value="220"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="ID" minOccurs="0"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *               &lt;maxLength value="20"/&gt;
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
                "datetime",
                "type",
                "msgnr",
                "message",
                "id"
            })
            public static class LOGS {

                @XmlElement(name = "DATETIME", required = true)
                @XmlSchemaType(name = "dateTime")
                protected XMLGregorianCalendar datetime;
                @XmlElement(name = "TYPE", required = true)
                protected String type;
                @XmlElement(name = "MSG_NR", required = true)
                protected BigInteger msgnr;
                @XmlElement(name = "MESSAGE", required = true)
                protected String message;
                @XmlElement(name = "ID")
                protected String id;

                /**
                 * Ruft den Wert der datetime-Eigenschaft ab.
                 *
                 * @return
                 *     possible object is
                 *     {@link XMLGregorianCalendar }
                 *
                 */
                public XMLGregorianCalendar getDATETIME() {
                    return datetime;
                }

                /**
                 * Legt den Wert der datetime-Eigenschaft fest.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link XMLGregorianCalendar }
                 *
                 */
                public void setDATETIME(XMLGregorianCalendar value) {
                    this.datetime = value;
                }

                /**
                 * Ruft den Wert der type-Eigenschaft ab.
                 *
                 * @return
                 *     possible object is
                 *     {@link String }
                 *
                 */
                public String getTYPE() {
                    return type;
                }

                /**
                 * Legt den Wert der type-Eigenschaft fest.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *
                 */
                public void setTYPE(String value) {
                    this.type = value;
                }

                /**
                 * Ruft den Wert der msgnr-Eigenschaft ab.
                 *
                 * @return
                 *     possible object is
                 *     {@link BigInteger }
                 *
                 */
                public BigInteger getMSGNR() {
                    return msgnr;
                }

                /**
                 * Legt den Wert der msgnr-Eigenschaft fest.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link BigInteger }
                 *
                 */
                public void setMSGNR(BigInteger value) {
                    this.msgnr = value;
                }

                /**
                 * Ruft den Wert der message-Eigenschaft ab.
                 *
                 * @return
                 *     possible object is
                 *     {@link String }
                 *
                 */
                public String getMESSAGE() {
                    return message;
                }

                /**
                 * Legt den Wert der message-Eigenschaft fest.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *
                 */
                public void setMESSAGE(String value) {
                    this.message = value;
                }

                /**
                 * Ruft den Wert der id-Eigenschaft ab.
                 *
                 * @return
                 *     possible object is
                 *     {@link String }
                 *
                 */
                public String getID() {
                    return id;
                }

                /**
                 * Legt den Wert der id-Eigenschaft fest.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *
                 */
                public void setID(String value) {
                    this.id = value;
                }

            }

        }

    }

}
