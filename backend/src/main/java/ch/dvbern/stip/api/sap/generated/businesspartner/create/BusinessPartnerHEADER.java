
package ch.dvbern.stip.api.sap.generated.businesspartner.create;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für BusinessPartner_HEADER complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="BusinessPartner_HEADER"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="BPARTNER"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="10"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="PARTN_CAT"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="10"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="PARTN_TYP"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="4"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="PARTN_GRP" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="4"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="PARTN_TAXKD" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;length value="1"/&gt;
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
@XmlType(name = "BusinessPartner_HEADER", namespace = "urn:be.ch:KTBE_MDG:BUSINESS_PARTNER", propOrder = {
    "bpartner",
    "partncat",
    "partntyp",
    "partngrp",
    "partntaxkd"
})
public class BusinessPartnerHEADER {

    @XmlElement(name = "BPARTNER", required = true)
    protected String bpartner;
    @XmlElement(name = "PARTN_CAT", required = true)
    protected String partncat;
    @XmlElement(name = "PARTN_TYP", required = true)
    protected String partntyp;
    @XmlElement(name = "PARTN_GRP")
    protected String partngrp;
    @XmlElement(name = "PARTN_TAXKD")
    protected String partntaxkd;

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
     * Ruft den Wert der partncat-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPARTNCAT() {
        return partncat;
    }

    /**
     * Legt den Wert der partncat-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPARTNCAT(String value) {
        this.partncat = value;
    }

    /**
     * Ruft den Wert der partntyp-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPARTNTYP() {
        return partntyp;
    }

    /**
     * Legt den Wert der partntyp-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPARTNTYP(String value) {
        this.partntyp = value;
    }

    /**
     * Ruft den Wert der partngrp-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPARTNGRP() {
        return partngrp;
    }

    /**
     * Legt den Wert der partngrp-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPARTNGRP(String value) {
        this.partngrp = value;
    }

    /**
     * Ruft den Wert der partntaxkd-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPARTNTAXKD() {
        return partntaxkd;
    }

    /**
     * Legt den Wert der partntaxkd-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPARTNTAXKD(String value) {
        this.partntaxkd = value;
    }

}
