
package ch.dvbern.stip.api.sap.generated.business_partner;

import javax.xml.datatype.XMLGregorianCalendar;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BusinessPartner_PAYMENT_DETAIL complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="BusinessPartner_PAYMENT_DETAIL">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="BANK_ID">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="4"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="IBAN">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="34"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="BANKDETAILVALIDFROM" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         <element name="BANKDETAILVALIDTO" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         <element name="ACCOUNTHOLDER">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="60"/>
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
@XmlType(name = "BusinessPartner_PAYMENT_DETAIL", propOrder = {
    "bankid",
    "iban",
    "bankdetailvalidfrom",
    "bankdetailvalidto",
    "accountholder"
})
public class BusinessPartnerPAYMENTDETAIL {

    /**
     * ID der Zahlungsverbindung
     *
     */
    @XmlElement(name = "BANK_ID", required = true)
    protected String bankid;
    /**
     * IBAN (International Bank Account Number)
     *
     */
    @XmlElement(name = "IBAN", required = true)
    protected String iban;
    /**
     * Gültigkeitsdatum (gültig ab)
     *
     */
    @XmlElement(name = "BANKDETAILVALIDFROM", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar bankdetailvalidfrom;
    /**
     * Gültigkeitsdatum (gültig bis)
     *
     */
    @XmlElement(name = "BANKDETAILVALIDTO", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar bankdetailvalidto;
    /**
     * Abweichender Name der Zahlungsverbindung
     *
     */
    @XmlElement(name = "ACCOUNTHOLDER", required = true)
    protected String accountholder;

    /**
     * ID der Zahlungsverbindung
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
     * Sets the value of the bankid property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getBANKID()
     */
    public void setBANKID(String value) {
        this.bankid = value;
    }

    /**
     * IBAN (International Bank Account Number)
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
     * Sets the value of the iban property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getIBAN()
     */
    public void setIBAN(String value) {
        this.iban = value;
    }

    /**
     * Gültigkeitsdatum (gültig ab)
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
     * Sets the value of the bankdetailvalidfrom property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     * @see #getBANKDETAILVALIDFROM()
     */
    public void setBANKDETAILVALIDFROM(XMLGregorianCalendar value) {
        this.bankdetailvalidfrom = value;
    }

    /**
     * Gültigkeitsdatum (gültig bis)
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
     * Sets the value of the bankdetailvalidto property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     * @see #getBANKDETAILVALIDTO()
     */
    public void setBANKDETAILVALIDTO(XMLGregorianCalendar value) {
        this.bankdetailvalidto = value;
    }

    /**
     * Abweichender Name der Zahlungsverbindung
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
     * Sets the value of the accountholder property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getACCOUNTHOLDER()
     */
    public void setACCOUNTHOLDER(String value) {
        this.accountholder = value;
    }

}
