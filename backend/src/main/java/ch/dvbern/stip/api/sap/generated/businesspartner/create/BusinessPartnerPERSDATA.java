
package ch.dvbern.stip.api.sap.generated.businesspartner.create;

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
 * <p>Java-Klasse für BusinessPartner_PERS_DATA complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="BusinessPartner_PERS_DATA"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="FIRSTNAME"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="40"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="LASTNAME"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="40"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="CORRESPONDLANGUAGEISO"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="BIRTHNAME"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="40"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="MIDDLENAME"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="40"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="SECONDNAME"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="40"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="TITLE_ACA1"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="4"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="TITLE_ACA2"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="4"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="TITLE_SPPL"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="4"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="PREFIX1"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="4"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="PREFIX2"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="4"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="NICKNAME"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="40"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="INITIALS"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="10"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="NAMEFORMAT"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="NAMCOUNTRY"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="NAMCOUNTRYISO"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="SEX"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="BIRTHPLACE"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="40"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="BIRTHDATE" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;simpleContent&gt;
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;date"&gt;
 *                 &lt;attribute ref="{http://sap.com/xi/SAPGlobal/GDT}DeletionIndicator"/&gt;
 *               &lt;/extension&gt;
 *             &lt;/simpleContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="DEATHDATE" minOccurs="0"&gt;
 *           &lt;complexType&gt;
 *             &lt;simpleContent&gt;
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;date"&gt;
 *                 &lt;attribute ref="{http://sap.com/xi/SAPGlobal/GDT}DeletionIndicator"/&gt;
 *               &lt;/extension&gt;
 *             &lt;/simpleContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="MARITALSTATUS"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="CORRESPONDLANGUAGE"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="1"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="FULLNAME"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="80"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="EMPLOYER"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="35"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="OCCUPATION"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="4"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="NATIONALITY"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="NATIONALITYISO"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="2"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="COUNTRYORIGIN"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="3"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="BIRTHDT_STATUS"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="1"/&gt;
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
@XmlType(name = "BusinessPartner_PERS_DATA", namespace = "urn:be.ch:KTBE_MDG:BUSINESS_PARTNER", propOrder = {
    "firstname",
    "lastname",
    "correspondlanguageiso",
    "birthname",
    "middlename",
    "secondname",
    "titleaca1",
    "titleaca2",
    "titlesppl",
    "prefix1",
    "prefix2",
    "nickname",
    "initials",
    "nameformat",
    "namcountry",
    "namcountryiso",
    "sex",
    "birthplace",
    "birthdate",
    "deathdate",
    "maritalstatus",
    "correspondlanguage",
    "fullname",
    "employer",
    "occupation",
    "nationality",
    "nationalityiso",
    "countryorigin",
    "birthdtstatus"
})
public class BusinessPartnerPERSDATA {

    @XmlElement(name = "FIRSTNAME", required = true)
    protected String firstname;
    @XmlElement(name = "LASTNAME", required = true)
    protected String lastname;
    @XmlElement(name = "CORRESPONDLANGUAGEISO", required = true)
    protected String correspondlanguageiso;
    @XmlElement(name = "BIRTHNAME", required = true)
    protected String birthname;
    @XmlElement(name = "MIDDLENAME", required = true)
    protected String middlename;
    @XmlElement(name = "SECONDNAME", required = true)
    protected String secondname;
    @XmlElement(name = "TITLE_ACA1", required = true)
    protected String titleaca1;
    @XmlElement(name = "TITLE_ACA2", required = true)
    protected String titleaca2;
    @XmlElement(name = "TITLE_SPPL", required = true)
    protected String titlesppl;
    @XmlElement(name = "PREFIX1", required = true)
    protected String prefix1;
    @XmlElement(name = "PREFIX2", required = true)
    protected String prefix2;
    @XmlElement(name = "NICKNAME", required = true)
    protected String nickname;
    @XmlElement(name = "INITIALS", required = true)
    protected String initials;
    @XmlElement(name = "NAMEFORMAT", required = true)
    protected String nameformat;
    @XmlElement(name = "NAMCOUNTRY", required = true)
    protected String namcountry;
    @XmlElement(name = "NAMCOUNTRYISO", required = true)
    protected String namcountryiso;
    @XmlElement(name = "SEX", required = true)
    protected String sex;
    @XmlElement(name = "BIRTHPLACE", required = true)
    protected String birthplace;
    @XmlElementRef(name = "BIRTHDATE", type = JAXBElement.class, required = false)
    protected JAXBElement<BusinessPartnerPERSDATA.BIRTHDATE> birthdate;
    @XmlElementRef(name = "DEATHDATE", type = JAXBElement.class, required = false)
    protected JAXBElement<BusinessPartnerPERSDATA.DEATHDATE> deathdate;
    @XmlElement(name = "MARITALSTATUS", required = true)
    protected String maritalstatus;
    @XmlElement(name = "CORRESPONDLANGUAGE", required = true)
    protected String correspondlanguage;
    @XmlElement(name = "FULLNAME", required = true)
    protected String fullname;
    @XmlElement(name = "EMPLOYER", required = true)
    protected String employer;
    @XmlElement(name = "OCCUPATION", required = true)
    protected String occupation;
    @XmlElement(name = "NATIONALITY", required = true)
    protected String nationality;
    @XmlElement(name = "NATIONALITYISO", required = true)
    protected String nationalityiso;
    @XmlElement(name = "COUNTRYORIGIN", required = true)
    protected String countryorigin;
    @XmlElement(name = "BIRTHDT_STATUS", required = true)
    protected String birthdtstatus;

    /**
     * Ruft den Wert der firstname-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFIRSTNAME() {
        return firstname;
    }

    /**
     * Legt den Wert der firstname-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFIRSTNAME(String value) {
        this.firstname = value;
    }

    /**
     * Ruft den Wert der lastname-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLASTNAME() {
        return lastname;
    }

    /**
     * Legt den Wert der lastname-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLASTNAME(String value) {
        this.lastname = value;
    }

    /**
     * Ruft den Wert der correspondlanguageiso-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCORRESPONDLANGUAGEISO() {
        return correspondlanguageiso;
    }

    /**
     * Legt den Wert der correspondlanguageiso-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCORRESPONDLANGUAGEISO(String value) {
        this.correspondlanguageiso = value;
    }

    /**
     * Ruft den Wert der birthname-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBIRTHNAME() {
        return birthname;
    }

    /**
     * Legt den Wert der birthname-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBIRTHNAME(String value) {
        this.birthname = value;
    }

    /**
     * Ruft den Wert der middlename-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMIDDLENAME() {
        return middlename;
    }

    /**
     * Legt den Wert der middlename-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMIDDLENAME(String value) {
        this.middlename = value;
    }

    /**
     * Ruft den Wert der secondname-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSECONDNAME() {
        return secondname;
    }

    /**
     * Legt den Wert der secondname-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSECONDNAME(String value) {
        this.secondname = value;
    }

    /**
     * Ruft den Wert der titleaca1-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTITLEACA1() {
        return titleaca1;
    }

    /**
     * Legt den Wert der titleaca1-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTITLEACA1(String value) {
        this.titleaca1 = value;
    }

    /**
     * Ruft den Wert der titleaca2-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTITLEACA2() {
        return titleaca2;
    }

    /**
     * Legt den Wert der titleaca2-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTITLEACA2(String value) {
        this.titleaca2 = value;
    }

    /**
     * Ruft den Wert der titlesppl-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTITLESPPL() {
        return titlesppl;
    }

    /**
     * Legt den Wert der titlesppl-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTITLESPPL(String value) {
        this.titlesppl = value;
    }

    /**
     * Ruft den Wert der prefix1-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPREFIX1() {
        return prefix1;
    }

    /**
     * Legt den Wert der prefix1-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPREFIX1(String value) {
        this.prefix1 = value;
    }

    /**
     * Ruft den Wert der prefix2-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPREFIX2() {
        return prefix2;
    }

    /**
     * Legt den Wert der prefix2-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPREFIX2(String value) {
        this.prefix2 = value;
    }

    /**
     * Ruft den Wert der nickname-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNICKNAME() {
        return nickname;
    }

    /**
     * Legt den Wert der nickname-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNICKNAME(String value) {
        this.nickname = value;
    }

    /**
     * Ruft den Wert der initials-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getINITIALS() {
        return initials;
    }

    /**
     * Legt den Wert der initials-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setINITIALS(String value) {
        this.initials = value;
    }

    /**
     * Ruft den Wert der nameformat-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNAMEFORMAT() {
        return nameformat;
    }

    /**
     * Legt den Wert der nameformat-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNAMEFORMAT(String value) {
        this.nameformat = value;
    }

    /**
     * Ruft den Wert der namcountry-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNAMCOUNTRY() {
        return namcountry;
    }

    /**
     * Legt den Wert der namcountry-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNAMCOUNTRY(String value) {
        this.namcountry = value;
    }

    /**
     * Ruft den Wert der namcountryiso-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNAMCOUNTRYISO() {
        return namcountryiso;
    }

    /**
     * Legt den Wert der namcountryiso-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNAMCOUNTRYISO(String value) {
        this.namcountryiso = value;
    }

    /**
     * Ruft den Wert der sex-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSEX() {
        return sex;
    }

    /**
     * Legt den Wert der sex-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSEX(String value) {
        this.sex = value;
    }

    /**
     * Ruft den Wert der birthplace-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBIRTHPLACE() {
        return birthplace;
    }

    /**
     * Legt den Wert der birthplace-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBIRTHPLACE(String value) {
        this.birthplace = value;
    }

    /**
     * Ruft den Wert der birthdate-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link BusinessPartnerPERSDATA.BIRTHDATE }{@code >}
     *
     */
    public JAXBElement<BusinessPartnerPERSDATA.BIRTHDATE> getBIRTHDATE() {
        return birthdate;
    }

    /**
     * Legt den Wert der birthdate-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link BusinessPartnerPERSDATA.BIRTHDATE }{@code >}
     *
     */
    public void setBIRTHDATE(JAXBElement<BusinessPartnerPERSDATA.BIRTHDATE> value) {
        this.birthdate = value;
    }

    /**
     * Ruft den Wert der deathdate-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link BusinessPartnerPERSDATA.DEATHDATE }{@code >}
     *
     */
    public JAXBElement<BusinessPartnerPERSDATA.DEATHDATE> getDEATHDATE() {
        return deathdate;
    }

    /**
     * Legt den Wert der deathdate-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link BusinessPartnerPERSDATA.DEATHDATE }{@code >}
     *
     */
    public void setDEATHDATE(JAXBElement<BusinessPartnerPERSDATA.DEATHDATE> value) {
        this.deathdate = value;
    }

    /**
     * Ruft den Wert der maritalstatus-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getMARITALSTATUS() {
        return maritalstatus;
    }

    /**
     * Legt den Wert der maritalstatus-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setMARITALSTATUS(String value) {
        this.maritalstatus = value;
    }

    /**
     * Ruft den Wert der correspondlanguage-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCORRESPONDLANGUAGE() {
        return correspondlanguage;
    }

    /**
     * Legt den Wert der correspondlanguage-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCORRESPONDLANGUAGE(String value) {
        this.correspondlanguage = value;
    }

    /**
     * Ruft den Wert der fullname-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getFULLNAME() {
        return fullname;
    }

    /**
     * Legt den Wert der fullname-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setFULLNAME(String value) {
        this.fullname = value;
    }

    /**
     * Ruft den Wert der employer-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEMPLOYER() {
        return employer;
    }

    /**
     * Legt den Wert der employer-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEMPLOYER(String value) {
        this.employer = value;
    }

    /**
     * Ruft den Wert der occupation-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOCCUPATION() {
        return occupation;
    }

    /**
     * Legt den Wert der occupation-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOCCUPATION(String value) {
        this.occupation = value;
    }

    /**
     * Ruft den Wert der nationality-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNATIONALITY() {
        return nationality;
    }

    /**
     * Legt den Wert der nationality-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNATIONALITY(String value) {
        this.nationality = value;
    }

    /**
     * Ruft den Wert der nationalityiso-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNATIONALITYISO() {
        return nationalityiso;
    }

    /**
     * Legt den Wert der nationalityiso-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNATIONALITYISO(String value) {
        this.nationalityiso = value;
    }

    /**
     * Ruft den Wert der countryorigin-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCOUNTRYORIGIN() {
        return countryorigin;
    }

    /**
     * Legt den Wert der countryorigin-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCOUNTRYORIGIN(String value) {
        this.countryorigin = value;
    }

    /**
     * Ruft den Wert der birthdtstatus-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getBIRTHDTSTATUS() {
        return birthdtstatus;
    }

    /**
     * Legt den Wert der birthdtstatus-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setBIRTHDTSTATUS(String value) {
        this.birthdtstatus = value;
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
    public static class BIRTHDATE {

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
    public static class DEATHDATE {

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
