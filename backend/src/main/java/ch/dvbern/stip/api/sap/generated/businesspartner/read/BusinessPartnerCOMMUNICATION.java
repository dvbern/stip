
package ch.dvbern.stip.api.sap.generated.businesspartner.read;

import java.math.BigInteger;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für BusinessPartner_COMMUNICATION complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="BusinessPartner_COMMUNICATION"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CONSNUMBER"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *               &lt;totalDigits value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="EMAIL"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="241"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="TELEPHONE"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="30"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="MOBILE"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="30"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="FAX"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="30"/&gt;
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
@XmlType(name = "BusinessPartner_COMMUNICATION", namespace = "urn:be.ch:KTBE_MDG:BUSINESS_PARTNER", propOrder = {
    "consnumber",
    "email",
    "telephone",
    "mobile",
    "fax"
})
public class BusinessPartnerCOMMUNICATION {

    @XmlElement(name = "CONSNUMBER", required = true)
    protected BigInteger consnumber;
    @XmlElement(name = "EMAIL", required = true)
    protected String email;
    @XmlElement(name = "TELEPHONE", required = true)
    protected String telephone;
    @XmlElement(name = "MOBILE", required = true)
    protected String mobile;
    @XmlElement(name = "FAX", required = true)
    protected String fax;

    /**
     * Ruft den Wert der consnumber-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link BigInteger }
     *
     */
    public BigInteger getCONSNUMBER() {
        return consnumber;
    }

    /**
     * Legt den Wert der consnumber-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *
     */
    public void setCONSNUMBER(BigInteger value) {
        this.consnumber = value;
    }

    /**
     * Ruft den Wert der email-Eigenschaft ab.
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
     * Legt den Wert der email-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEMAIL(String value) {
        this.email = value;
    }

    /**
     * Ruft den Wert der telephone-Eigenschaft ab.
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
     * Legt den Wert der telephone-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTELEPHONE(String value) {
        this.telephone = value;
    }

    /**
     * Ruft den Wert der mobile-Eigenschaft ab.
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
     * Legt den Wert der mobile-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMOBILE(String value) {
        this.mobile = value;
    }

    /**
     * Ruft den Wert der fax-Eigenschaft ab.
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
     * Legt den Wert der fax-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFAX(String value) {
        this.fax = value;
    }

}
