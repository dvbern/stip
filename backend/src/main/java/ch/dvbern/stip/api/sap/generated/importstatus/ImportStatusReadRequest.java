
package ch.dvbern.stip.api.sap.generated.importstatus;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.datatype.XMLGregorianCalendar;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Request für SST-073
 *
 * <p>Java-Klasse für ImportStatusRead_Request complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="ImportStatusRead_Request"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SENDER" type="{urn:be.ch:KTBE_ERP_FI:GENERAL}SenderParms"/&gt;
 *         &lt;element name="FILTER_PARMS"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="DELIVERY_ID" minOccurs="0"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *                         &lt;totalDigits value="19"/&gt;
 *                         &lt;fractionDigits value="0"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="SHOW_DETAILS" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *                   &lt;element name="STATUS" minOccurs="0"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                         &lt;enumeration value="0"/&gt;
 *                         &lt;enumeration value="1"/&gt;
 *                         &lt;enumeration value="2"/&gt;
 *                         &lt;enumeration value="3"/&gt;
 *                         &lt;enumeration value="4"/&gt;
 *                         &lt;enumeration value="99"/&gt;
 *                         &lt;totalDigits value="3"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="DATE_FROM" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="DATE_TO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                   &lt;element name="OFFSET" minOccurs="0"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                         &lt;totalDigits value="6"/&gt;
 *                       &lt;/restriction&gt;
 *                     &lt;/simpleType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="LIMIT" minOccurs="0"&gt;
 *                     &lt;simpleType&gt;
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                         &lt;totalDigits value="6"/&gt;
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
@XmlRootElement(name = "ImportStatusRead_Request", namespace = "urn:be.ch:KTBE_ERP_FI:IMPORT_STATUS")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ImportStatusRead_Request", namespace = "urn:be.ch:KTBE_ERP_FI:IMPORT_STATUS", propOrder = {
    "sender",
    "filterparms"
})
public class ImportStatusReadRequest {

    @XmlElement(name = "SENDER", required = true)
    protected SenderParms sender;
    @XmlElement(name = "FILTER_PARMS", required = true)
    protected ImportStatusReadRequest.FILTERPARMS filterparms;

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
     *     {@link ImportStatusReadRequest.FILTERPARMS }
     *
     */
    public ImportStatusReadRequest.FILTERPARMS getFILTERPARMS() {
        return filterparms;
    }

    /**
     * Legt den Wert der filterparms-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link ImportStatusReadRequest.FILTERPARMS }
     *
     */
    public void setFILTERPARMS(ImportStatusReadRequest.FILTERPARMS value) {
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
     *         &lt;element name="DELIVERY_ID" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
     *               &lt;totalDigits value="19"/&gt;
     *               &lt;fractionDigits value="0"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="SHOW_DETAILS" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
     *         &lt;element name="STATUS" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *               &lt;enumeration value="0"/&gt;
     *               &lt;enumeration value="1"/&gt;
     *               &lt;enumeration value="2"/&gt;
     *               &lt;enumeration value="3"/&gt;
     *               &lt;enumeration value="4"/&gt;
     *               &lt;enumeration value="99"/&gt;
     *               &lt;totalDigits value="3"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="DATE_FROM" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="DATE_TO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *         &lt;element name="OFFSET" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *               &lt;totalDigits value="6"/&gt;
     *             &lt;/restriction&gt;
     *           &lt;/simpleType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="LIMIT" minOccurs="0"&gt;
     *           &lt;simpleType&gt;
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *               &lt;totalDigits value="6"/&gt;
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
        "deliveryid",
        "showdetails",
        "status",
        "datefrom",
        "dateto",
        "offset",
        "limit"
    })
    public static class FILTERPARMS {

        @XmlElement(name = "DELIVERY_ID")
        protected BigDecimal deliveryid;
        @XmlElement(name = "SHOW_DETAILS", defaultValue = "false")
        protected Boolean showdetails;
        @XmlElement(name = "STATUS", defaultValue = "99")
        protected BigInteger status;
        @XmlElement(name = "DATE_FROM")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar datefrom;
        @XmlElement(name = "DATE_TO")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dateto;
        @XmlElement(name = "OFFSET", defaultValue = "0")
        protected BigInteger offset;
        @XmlElement(name = "LIMIT", defaultValue = "100")
        protected BigInteger limit;

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
         * Ruft den Wert der showdetails-Eigenschaft ab.
         *
         * @return
         *     possible object is
         *     {@link Boolean }
         *
         */
        public Boolean isSHOWDETAILS() {
            return showdetails;
        }

        /**
         * Legt den Wert der showdetails-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *
         */
        public void setSHOWDETAILS(Boolean value) {
            this.showdetails = value;
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
         * Ruft den Wert der datefrom-Eigenschaft ab.
         *
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *
         */
        public XMLGregorianCalendar getDATEFROM() {
            return datefrom;
        }

        /**
         * Legt den Wert der datefrom-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *
         */
        public void setDATEFROM(XMLGregorianCalendar value) {
            this.datefrom = value;
        }

        /**
         * Ruft den Wert der dateto-Eigenschaft ab.
         *
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *
         */
        public XMLGregorianCalendar getDATETO() {
            return dateto;
        }

        /**
         * Legt den Wert der dateto-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *
         */
        public void setDATETO(XMLGregorianCalendar value) {
            this.dateto = value;
        }

        /**
         * Ruft den Wert der offset-Eigenschaft ab.
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
         * Legt den Wert der offset-Eigenschaft fest.
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
         * Ruft den Wert der limit-Eigenschaft ab.
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
         * Legt den Wert der limit-Eigenschaft fest.
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

}
