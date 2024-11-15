
package ch.dvbern.stip.api.sap.generated.businesspartner.read;

import javax.xml.datatype.XMLGregorianCalendar;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;


/**
 * <p>Java-Klasse für BusinessPartner_ORG_DATA complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="BusinessPartner_ORG_DATA"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="NAME1"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="40"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="NAME2"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="40"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="NAME3"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="40"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="NAME4"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="40"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="LANGU_ISO"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="LEGALFORM"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="INDUSTRYSECTOR"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="10"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="FOUNDATIONDATE" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;simpleContent&gt;
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;date"&gt;
 *                 &lt;attribute ref="{http://sap.com/xi/SAPGlobal/GDT}DeletionIndicator"/&gt;
 *               &lt;/extension&gt;
 *             &lt;/simpleContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="LIQUIDATIONDATE" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;simpleContent&gt;
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;date"&gt;
 *                 &lt;attribute ref="{http://sap.com/xi/SAPGlobal/GDT}DeletionIndicator"/&gt;
 *               &lt;/extension&gt;
 *             &lt;/simpleContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="LOC_NO_1"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="7"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="LOC_NO_2"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="5"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="CHK_DIGIT"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="LEGALORG"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="2"/&gt;
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
@XmlType(name = "BusinessPartner_ORG_DATA", namespace = "urn:be.ch:KTBE_MDG:BUSINESS_PARTNER", propOrder = {
    "name1",
    "name2",
    "name3",
    "name4",
    "languiso",
    "legalform",
    "industrysector",
    "foundationdate",
    "liquidationdate",
    "locno1",
    "locno2",
    "chkdigit",
    "legalorg"
})
public class BusinessPartnerORGDATA {

    @XmlElement(name = "NAME1", required = true)
    protected String name1;
    @XmlElement(name = "NAME2", required = true)
    protected String name2;
    @XmlElement(name = "NAME3", required = true)
    protected String name3;
    @XmlElement(name = "NAME4", required = true)
    protected String name4;
    @XmlElement(name = "LANGU_ISO", required = true)
    protected String languiso;
    @XmlElement(name = "LEGALFORM", required = true)
    protected String legalform;
    @XmlElement(name = "INDUSTRYSECTOR", required = true)
    protected String industrysector;
    @XmlElementRef(name = "FOUNDATIONDATE", type = JAXBElement.class, required = false)
    protected JAXBElement<BusinessPartnerORGDATA.FOUNDATIONDATE> foundationdate;
    @XmlElementRef(name = "LIQUIDATIONDATE", type = JAXBElement.class, required = false)
    protected JAXBElement<BusinessPartnerORGDATA.LIQUIDATIONDATE> liquidationdate;
    @XmlElement(name = "LOC_NO_1", required = true)
    protected String locno1;
    @XmlElement(name = "LOC_NO_2", required = true)
    protected String locno2;
    @XmlElement(name = "CHK_DIGIT", required = true)
    protected String chkdigit;
    @XmlElement(name = "LEGALORG", required = true)
    protected String legalorg;

    /**
     * Ruft den Wert der name1-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNAME1() {
        return name1;
    }

    /**
     * Legt den Wert der name1-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNAME1(String value) {
        this.name1 = value;
    }

    /**
     * Ruft den Wert der name2-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNAME2() {
        return name2;
    }

    /**
     * Legt den Wert der name2-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNAME2(String value) {
        this.name2 = value;
    }

    /**
     * Ruft den Wert der name3-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNAME3() {
        return name3;
    }

    /**
     * Legt den Wert der name3-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNAME3(String value) {
        this.name3 = value;
    }

    /**
     * Ruft den Wert der name4-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNAME4() {
        return name4;
    }

    /**
     * Legt den Wert der name4-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNAME4(String value) {
        this.name4 = value;
    }

    /**
     * Ruft den Wert der languiso-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLANGUISO() {
        return languiso;
    }

    /**
     * Legt den Wert der languiso-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLANGUISO(String value) {
        this.languiso = value;
    }

    /**
     * Ruft den Wert der legalform-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLEGALFORM() {
        return legalform;
    }

    /**
     * Legt den Wert der legalform-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLEGALFORM(String value) {
        this.legalform = value;
    }

    /**
     * Ruft den Wert der industrysector-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getINDUSTRYSECTOR() {
        return industrysector;
    }

    /**
     * Legt den Wert der industrysector-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setINDUSTRYSECTOR(String value) {
        this.industrysector = value;
    }

    /**
     * Ruft den Wert der foundationdate-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link BusinessPartnerORGDATA.FOUNDATIONDATE }{@code >}
     *
     */
    public JAXBElement<BusinessPartnerORGDATA.FOUNDATIONDATE> getFOUNDATIONDATE() {
        return foundationdate;
    }

    /**
     * Legt den Wert der foundationdate-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link BusinessPartnerORGDATA.FOUNDATIONDATE }{@code >}
     *
     */
    public void setFOUNDATIONDATE(JAXBElement<BusinessPartnerORGDATA.FOUNDATIONDATE> value) {
        this.foundationdate = value;
    }

    /**
     * Ruft den Wert der liquidationdate-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link BusinessPartnerORGDATA.LIQUIDATIONDATE }{@code >}
     *
     */
    public JAXBElement<BusinessPartnerORGDATA.LIQUIDATIONDATE> getLIQUIDATIONDATE() {
        return liquidationdate;
    }

    /**
     * Legt den Wert der liquidationdate-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link BusinessPartnerORGDATA.LIQUIDATIONDATE }{@code >}
     *
     */
    public void setLIQUIDATIONDATE(JAXBElement<BusinessPartnerORGDATA.LIQUIDATIONDATE> value) {
        this.liquidationdate = value;
    }

    /**
     * Ruft den Wert der locno1-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLOCNO1() {
        return locno1;
    }

    /**
     * Legt den Wert der locno1-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLOCNO1(String value) {
        this.locno1 = value;
    }

    /**
     * Ruft den Wert der locno2-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLOCNO2() {
        return locno2;
    }

    /**
     * Legt den Wert der locno2-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLOCNO2(String value) {
        this.locno2 = value;
    }

    /**
     * Ruft den Wert der chkdigit-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCHKDIGIT() {
        return chkdigit;
    }

    /**
     * Legt den Wert der chkdigit-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCHKDIGIT(String value) {
        this.chkdigit = value;
    }

    /**
     * Ruft den Wert der legalorg-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLEGALORG() {
        return legalorg;
    }

    /**
     * Legt den Wert der legalorg-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLEGALORG(String value) {
        this.legalorg = value;
    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     *
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     *
     * <pre>
     * &lt;complexType&gt;
     *   &lt;simpleContent&gt;
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;date"&gt;
     *       &lt;attribute ref="{http://sap.com/xi/SAPGlobal/GDT}DeletionIndicator"/&gt;
     *     &lt;/extension&gt;
     *   &lt;/simpleContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class FOUNDATIONDATE {

        @XmlValue
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar value;
        @XmlAttribute(name = "DeletionIndicator", namespace = "http://sap.com/xi/SAPGlobal/GDT")
        protected Boolean deletionIndicator;

        /**
         * Ruft den Wert der value-Eigenschaft ab.
         *
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *
         */
        public XMLGregorianCalendar getValue() {
            return value;
        }

        /**
         * Legt den Wert der value-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *
         */
        public void setValue(XMLGregorianCalendar value) {
            this.value = value;
        }

        /**
         * Ruft den Wert der deletionIndicator-Eigenschaft ab.
         *
         * @return
         *     possible object is
         *     {@link Boolean }
         *
         */
        public Boolean isDeletionIndicator() {
            return deletionIndicator;
        }

        /**
         * Legt den Wert der deletionIndicator-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *
         */
        public void setDeletionIndicator(Boolean value) {
            this.deletionIndicator = value;
        }

    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     *
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     *
     * <pre>
     * &lt;complexType&gt;
     *   &lt;simpleContent&gt;
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;date"&gt;
     *       &lt;attribute ref="{http://sap.com/xi/SAPGlobal/GDT}DeletionIndicator"/&gt;
     *     &lt;/extension&gt;
     *   &lt;/simpleContent&gt;
     * &lt;/complexType&gt;
     * </pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class LIQUIDATIONDATE {

        @XmlValue
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar value;
        @XmlAttribute(name = "DeletionIndicator", namespace = "http://sap.com/xi/SAPGlobal/GDT")
        protected Boolean deletionIndicator;

        /**
         * Ruft den Wert der value-Eigenschaft ab.
         *
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *
         */
        public XMLGregorianCalendar getValue() {
            return value;
        }

        /**
         * Legt den Wert der value-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *
         */
        public void setValue(XMLGregorianCalendar value) {
            this.value = value;
        }

        /**
         * Ruft den Wert der deletionIndicator-Eigenschaft ab.
         *
         * @return
         *     possible object is
         *     {@link Boolean }
         *
         */
        public Boolean isDeletionIndicator() {
            return deletionIndicator;
        }

        /**
         * Legt den Wert der deletionIndicator-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *
         */
        public void setDeletionIndicator(Boolean value) {
            this.deletionIndicator = value;
        }

    }

}
