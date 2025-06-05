
package ch.dvbern.stip.api.sap.generated.business_partner;

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
 * <p>Java class for BusinessPartner_PERS_DATA complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="BusinessPartner_PERS_DATA">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="FIRSTNAME">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="40"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="LASTNAME">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="40"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="CORRESPONDLANGUAGEISO">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="2"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="BIRTHNAME">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="40"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="MIDDLENAME">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="40"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="SECONDNAME">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="40"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="TITLE_ACA1">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="4"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="TITLE_ACA2">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="4"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="TITLE_SPPL">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="4"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="PREFIX1">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="4"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="PREFIX2">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="4"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="NICKNAME">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="40"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="INITIALS">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="10"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="NAMEFORMAT">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="2"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="NAMCOUNTRY">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="3"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="NAMCOUNTRYISO">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="2"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="SEX">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="1"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="BIRTHPLACE">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="40"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="BIRTHDATE" minOccurs="0">
 *           <complexType>
 *             <simpleContent>
 *               <extension base="<http://www.w3.org/2001/XMLSchema>date">
 *                 <attribute ref="{http://sap.com/xi/SAPGlobal/GDT}DeletionIndicator"/>
 *               </extension>
 *             </simpleContent>
 *           </complexType>
 *         </element>
 *         <element name="DEATHDATE" minOccurs="0">
 *           <complexType>
 *             <simpleContent>
 *               <extension base="<http://www.w3.org/2001/XMLSchema>date">
 *                 <attribute ref="{http://sap.com/xi/SAPGlobal/GDT}DeletionIndicator"/>
 *               </extension>
 *             </simpleContent>
 *           </complexType>
 *         </element>
 *         <element name="MARITALSTATUS">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="1"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="CORRESPONDLANGUAGE">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="1"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="FULLNAME">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="80"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="EMPLOYER">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="35"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="OCCUPATION">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="4"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="NATIONALITY">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="3"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="NATIONALITYISO">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="2"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="COUNTRYORIGIN">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="3"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="BIRTHDT_STATUS">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="1"/>
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
@XmlType(name = "BusinessPartner_PERS_DATA", propOrder = {
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

    /**
     * Vorname des Geschäftspartners (Person)
     *
     */
    @XmlElement(name = "FIRSTNAME", required = true)
    protected String firstname;
    /**
     * Nachname des Geschäftspartners (Person)
     *
     */
    @XmlElement(name = "LASTNAME", required = true)
    protected String lastname;
    /**
     *  2-stelliger SAP Language Code
     *
     */
    @XmlElement(name = "CORRESPONDLANGUAGEISO", required = true)
    protected String correspondlanguageiso;
    /**
     * Geburtsname des Geschäftspartners
     *
     */
    @XmlElement(name = "BIRTHNAME", required = true)
    protected String birthname;
    /**
     * Mittlerer Name oder zweiter Vorname der Person
     *
     */
    @XmlElement(name = "MIDDLENAME", required = true)
    protected String middlename;
    /**
     * Zweiter Nachname der Person
     *
     */
    @XmlElement(name = "SECONDNAME", required = true)
    protected String secondname;
    /**
     * Akademischer Titel: Schlüssel
     *
     */
    @XmlElement(name = "TITLE_ACA1", required = true)
    protected String titleaca1;
    /**
     * Zweiter akademischer Titel (Schlüssel)
     *
     */
    @XmlElement(name = "TITLE_ACA2", required = true)
    protected String titleaca2;
    /**
     * Namenszusatz, z.B. Adelstitel (Schlüssel)
     *
     */
    @XmlElement(name = "TITLE_SPPL", required = true)
    protected String titlesppl;
    /**
     * Vorsatzwort zum Namen (Schlüssel)
     *
     */
    @XmlElement(name = "PREFIX1", required = true)
    protected String prefix1;
    /**
     *  2. Vorsatzwort zum Namen (Schlüssel)
     *
     */
    @XmlElement(name = "PREFIX2", required = true)
    protected String prefix2;
    /**
     * Rufname des Geschäftspartners (Person)
     *
     */
    @XmlElement(name = "NICKNAME", required = true)
    protected String nickname;
    /**
     * Middle Initial bzw. Initialen der Person
     *
     */
    @XmlElement(name = "INITIALS", required = true)
    protected String initials;
    /**
     * Format für die Namensaufbereitung
     *
     */
    @XmlElement(name = "NAMEFORMAT", required = true)
    protected String nameformat;
    /**
     * Land für Namensaufbereitungsregel
     *
     */
    @XmlElement(name = "NAMCOUNTRY", required = true)
    protected String namcountry;
    /**
     * ISO-Code des Landes
     *
     */
    @XmlElement(name = "NAMCOUNTRYISO", required = true)
    protected String namcountryiso;
    /**
     * Geschlecht des Geschäftspartners (Person)
     *
     */
    @XmlElement(name = "SEX", required = true)
    protected String sex;
    /**
     * Geburtsort des Geschäftspartners
     *
     */
    @XmlElement(name = "BIRTHPLACE", required = true)
    protected String birthplace;
    /**
     * Geburtsdatum des Geschäftspartners
     *
     */
    @XmlElementRef(name = "BIRTHDATE", type = JAXBElement.class, required = false)
    protected JAXBElement<BIRTHDATE> birthdate;
    /**
     * Sterbedatum des Geschäftspartners
     *
     */
    @XmlElementRef(name = "DEATHDATE", type = JAXBElement.class, required = false)
    protected JAXBElement<DEATHDATE> deathdate;
    /**
     * Familienstand des Geschäftspartners
     *
     */
    @XmlElement(name = "MARITALSTATUS", required = true)
    protected String maritalstatus;
    /**
     * Geschäftspartner: Korrespondenzsprache
     *
     */
    @XmlElement(name = "CORRESPONDLANGUAGE", required = true)
    protected String correspondlanguage;
    /**
     * Vollständiger Name
     *
     */
    @XmlElement(name = "FULLNAME", required = true)
    protected String fullname;
    /**
     * Name des Arbeitgebers einer natürlichen Person
     *
     */
    @XmlElement(name = "EMPLOYER", required = true)
    protected String employer;
    /**
     * Beruf des Geschäftspartners
     *
     */
    @XmlElement(name = "OCCUPATION", required = true)
    protected String occupation;
    /**
     * Nationalität des Geschäftspartners
     *
     */
    @XmlElement(name = "NATIONALITY", required = true)
    protected String nationality;
    /**
     * ISO-Code des Landes
     *
     */
    @XmlElement(name = "NATIONALITYISO", required = true)
    protected String nationalityiso;
    /**
     * Herkunftsland bei Gebietsfremden
     *
     */
    @XmlElement(name = "COUNTRYORIGIN", required = true)
    protected String countryorigin;
    /**
     * Date of Birth: Status
     *
     */
    @XmlElement(name = "BIRTHDT_STATUS", required = true)
    protected String birthdtstatus;

    /**
     * Vorname des Geschäftspartners (Person)
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
     * Sets the value of the firstname property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getFIRSTNAME()
     */
    public void setFIRSTNAME(String value) {
        this.firstname = value;
    }

    /**
     * Nachname des Geschäftspartners (Person)
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
     * Sets the value of the lastname property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getLASTNAME()
     */
    public void setLASTNAME(String value) {
        this.lastname = value;
    }

    /**
     *  2-stelliger SAP Language Code
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
     * Sets the value of the correspondlanguageiso property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getCORRESPONDLANGUAGEISO()
     */
    public void setCORRESPONDLANGUAGEISO(String value) {
        this.correspondlanguageiso = value;
    }

    /**
     * Geburtsname des Geschäftspartners
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
     * Sets the value of the birthname property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getBIRTHNAME()
     */
    public void setBIRTHNAME(String value) {
        this.birthname = value;
    }

    /**
     * Mittlerer Name oder zweiter Vorname der Person
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
     * Sets the value of the middlename property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getMIDDLENAME()
     */
    public void setMIDDLENAME(String value) {
        this.middlename = value;
    }

    /**
     * Zweiter Nachname der Person
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
     * Sets the value of the secondname property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getSECONDNAME()
     */
    public void setSECONDNAME(String value) {
        this.secondname = value;
    }

    /**
     * Akademischer Titel: Schlüssel
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
     * Sets the value of the titleaca1 property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getTITLEACA1()
     */
    public void setTITLEACA1(String value) {
        this.titleaca1 = value;
    }

    /**
     * Zweiter akademischer Titel (Schlüssel)
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
     * Sets the value of the titleaca2 property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getTITLEACA2()
     */
    public void setTITLEACA2(String value) {
        this.titleaca2 = value;
    }

    /**
     * Namenszusatz, z.B. Adelstitel (Schlüssel)
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
     * Sets the value of the titlesppl property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getTITLESPPL()
     */
    public void setTITLESPPL(String value) {
        this.titlesppl = value;
    }

    /**
     * Vorsatzwort zum Namen (Schlüssel)
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
     * Sets the value of the prefix1 property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getPREFIX1()
     */
    public void setPREFIX1(String value) {
        this.prefix1 = value;
    }

    /**
     *  2. Vorsatzwort zum Namen (Schlüssel)
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
     * Sets the value of the prefix2 property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getPREFIX2()
     */
    public void setPREFIX2(String value) {
        this.prefix2 = value;
    }

    /**
     * Rufname des Geschäftspartners (Person)
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
     * Sets the value of the nickname property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getNICKNAME()
     */
    public void setNICKNAME(String value) {
        this.nickname = value;
    }

    /**
     * Middle Initial bzw. Initialen der Person
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
     * Sets the value of the initials property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getINITIALS()
     */
    public void setINITIALS(String value) {
        this.initials = value;
    }

    /**
     * Format für die Namensaufbereitung
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
     * Sets the value of the nameformat property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getNAMEFORMAT()
     */
    public void setNAMEFORMAT(String value) {
        this.nameformat = value;
    }

    /**
     * Land für Namensaufbereitungsregel
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
     * Sets the value of the namcountry property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getNAMCOUNTRY()
     */
    public void setNAMCOUNTRY(String value) {
        this.namcountry = value;
    }

    /**
     * ISO-Code des Landes
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
     * Sets the value of the namcountryiso property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getNAMCOUNTRYISO()
     */
    public void setNAMCOUNTRYISO(String value) {
        this.namcountryiso = value;
    }

    /**
     * Geschlecht des Geschäftspartners (Person)
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
     * Sets the value of the sex property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getSEX()
     */
    public void setSEX(String value) {
        this.sex = value;
    }

    /**
     * Geburtsort des Geschäftspartners
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
     * Sets the value of the birthplace property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getBIRTHPLACE()
     */
    public void setBIRTHPLACE(String value) {
        this.birthplace = value;
    }

    /**
     * Geburtsdatum des Geschäftspartners
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link BIRTHDATE }{@code >}
     *
     */
    public JAXBElement<BIRTHDATE> getBIRTHDATE() {
        return birthdate;
    }

    /**
     * Sets the value of the birthdate property.
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link BIRTHDATE }{@code >}
     *
     * @see #getBIRTHDATE()
     */
    public void setBIRTHDATE(JAXBElement<BIRTHDATE> value) {
        this.birthdate = value;
    }

    /**
     * Sterbedatum des Geschäftspartners
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link DEATHDATE }{@code >}
     *
     */
    public JAXBElement<DEATHDATE> getDEATHDATE() {
        return deathdate;
    }

    /**
     * Sets the value of the deathdate property.
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link DEATHDATE }{@code >}
     *
     * @see #getDEATHDATE()
     */
    public void setDEATHDATE(JAXBElement<DEATHDATE> value) {
        this.deathdate = value;
    }

    /**
     * Familienstand des Geschäftspartners
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
     * Sets the value of the maritalstatus property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getMARITALSTATUS()
     */
    public void setMARITALSTATUS(String value) {
        this.maritalstatus = value;
    }

    /**
     * Geschäftspartner: Korrespondenzsprache
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
     * Sets the value of the correspondlanguage property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getCORRESPONDLANGUAGE()
     */
    public void setCORRESPONDLANGUAGE(String value) {
        this.correspondlanguage = value;
    }

    /**
     * Vollständiger Name
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
     * Sets the value of the fullname property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getFULLNAME()
     */
    public void setFULLNAME(String value) {
        this.fullname = value;
    }

    /**
     * Name des Arbeitgebers einer natürlichen Person
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
     * Sets the value of the employer property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getEMPLOYER()
     */
    public void setEMPLOYER(String value) {
        this.employer = value;
    }

    /**
     * Beruf des Geschäftspartners
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
     * Sets the value of the occupation property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getOCCUPATION()
     */
    public void setOCCUPATION(String value) {
        this.occupation = value;
    }

    /**
     * Nationalität des Geschäftspartners
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
     * Sets the value of the nationality property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getNATIONALITY()
     */
    public void setNATIONALITY(String value) {
        this.nationality = value;
    }

    /**
     * ISO-Code des Landes
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
     * Sets the value of the nationalityiso property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getNATIONALITYISO()
     */
    public void setNATIONALITYISO(String value) {
        this.nationalityiso = value;
    }

    /**
     * Herkunftsland bei Gebietsfremden
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
     * Sets the value of the countryorigin property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getCOUNTRYORIGIN()
     */
    public void setCOUNTRYORIGIN(String value) {
        this.countryorigin = value;
    }

    /**
     * Date of Birth: Status
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
     * Sets the value of the birthdtstatus property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getBIRTHDTSTATUS()
     */
    public void setBIRTHDTSTATUS(String value) {
        this.birthdtstatus = value;
    }


    /**
     * <p>Java class for anonymous complex type</p>.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.</p>
     *
     * <pre>{@code
     * <complexType>
     *   <simpleContent>
     *     <extension base="<http://www.w3.org/2001/XMLSchema>date">
     *       <attribute ref="{http://sap.com/xi/SAPGlobal/GDT}DeletionIndicator"/>
     *     </extension>
     *   </simpleContent>
     * </complexType>
     * }</pre>
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
         * Gets the value of the value property.
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
         * Sets the value of the value property.
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
         * Gets the value of the deletionIndicator property.
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
         * Sets the value of the deletionIndicator property.
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
     * <p>Java class for anonymous complex type</p>.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.</p>
     *
     * <pre>{@code
     * <complexType>
     *   <simpleContent>
     *     <extension base="<http://www.w3.org/2001/XMLSchema>date">
     *       <attribute ref="{http://sap.com/xi/SAPGlobal/GDT}DeletionIndicator"/>
     *     </extension>
     *   </simpleContent>
     * </complexType>
     * }</pre>
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
         * Gets the value of the value property.
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
         * Sets the value of the value property.
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
         * Gets the value of the deletionIndicator property.
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
         * Sets the value of the deletionIndicator property.
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
