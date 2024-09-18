
package ch.dvbern.stip.api.sap.generated.businesspartner.read;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für BusinessPartner_ID_KEYS complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="BusinessPartner_ID_KEYS"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="EXT_ID"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="20"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="AHV_NR"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="13"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="UID_NR"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="12"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="ZPV_NR"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="10"/&gt;
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
@XmlType(name = "BusinessPartner_ID_KEYS", namespace = "urn:be.ch:KTBE_MDG:BUSINESS_PARTNER", propOrder = {
    "extid",
    "ahvnr",
    "uidnr",
    "zpvnr"
})
public class BusinessPartnerIDKEYS {

    @XmlElement(name = "EXT_ID", required = true)
    protected String extid;
    @XmlElement(name = "AHV_NR", required = true)
    protected String ahvnr;
    @XmlElement(name = "UID_NR", required = true)
    protected String uidnr;
    @XmlElement(name = "ZPV_NR", required = true)
    protected String zpvnr;

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

}
