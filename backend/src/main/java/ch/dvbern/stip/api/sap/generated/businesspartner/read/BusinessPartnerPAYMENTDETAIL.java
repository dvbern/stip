
package ch.dvbern.stip.api.sap.generated.businesspartner.read;

import javax.xml.datatype.XMLGregorianCalendar;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für BusinessPartner_PAYMENT_DETAIL complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="BusinessPartner_PAYMENT_DETAIL"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="BANK_ID"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="4"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="IBAN"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="34"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="BANKDETAILVALIDFROM" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="BANKDETAILVALIDTO" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *         &lt;element name="ACCOUNTHOLDER"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="60"/&gt;
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
@XmlType(name = "BusinessPartner_PAYMENT_DETAIL", namespace = "urn:be.ch:KTBE_MDG:BUSINESS_PARTNER", propOrder = {
    "bankid",
    "iban",
    "bankdetailvalidfrom",
    "bankdetailvalidto",
    "accountholder"
})
public class BusinessPartnerPAYMENTDETAIL {

    @XmlElement(name = "BANK_ID", required = true)
    protected String bankid;
    @XmlElement(name = "IBAN", required = true)
    protected String iban;
    @XmlElement(name = "BANKDETAILVALIDFROM", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar bankdetailvalidfrom;
    @XmlElement(name = "BANKDETAILVALIDTO", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar bankdetailvalidto;
    @XmlElement(name = "ACCOUNTHOLDER", required = true)
    protected String accountholder;

    /**
     * Ruft den Wert der bankid-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBANKID() {
        return bankid;
    }

    /**
     * Legt den Wert der bankid-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBANKID(String value) {
        this.bankid = value;
    }

    /**
     * Ruft den Wert der iban-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getIBAN() {
        return iban;
    }

    /**
     * Legt den Wert der iban-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setIBAN(String value) {
        this.iban = value;
    }

    /**
     * Ruft den Wert der bankdetailvalidfrom-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getBANKDETAILVALIDFROM() {
        return bankdetailvalidfrom;
    }

    /**
     * Legt den Wert der bankdetailvalidfrom-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setBANKDETAILVALIDFROM(XMLGregorianCalendar value) {
        this.bankdetailvalidfrom = value;
    }

    /**
     * Ruft den Wert der bankdetailvalidto-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getBANKDETAILVALIDTO() {
        return bankdetailvalidto;
    }

    /**
     * Legt den Wert der bankdetailvalidto-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setBANKDETAILVALIDTO(XMLGregorianCalendar value) {
        this.bankdetailvalidto = value;
    }

    /**
     * Ruft den Wert der accountholder-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getACCOUNTHOLDER() {
        return accountholder;
    }

    /**
     * Legt den Wert der accountholder-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setACCOUNTHOLDER(String value) {
        this.accountholder = value;
    }

}
