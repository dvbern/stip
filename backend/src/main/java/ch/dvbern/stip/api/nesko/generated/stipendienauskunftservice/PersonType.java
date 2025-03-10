
package ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice;

import javax.xml.datatype.XMLGregorianCalendar;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Angaben zur Person
 *
 * <p>Java class for PersonType complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="PersonType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="ZPVNr" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="Sozialversicherungsnummer" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         <element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="Vorname" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="Strasse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="Plz" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="Ort" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="Geschlecht" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}GeschlechtType" minOccurs="0"/>
 *         <element name="Geburtsdatum" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         <element name="Zivilstand" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}ZivilstandType" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PersonType", propOrder = {
    "zpvNr",
    "sozialversicherungsnummer",
    "name",
    "vorname",
    "strasse",
    "plz",
    "ort",
    "geschlecht",
    "geburtsdatum",
    "zivilstand"
})
public class PersonType {

    /**
     * Zpvnummer der angefragten Person: DT0672-Zelle 11/01
     *                         oder
     *                         Zpvnummer der/des Partnerin/Partners: DT0672-Zelle 302/01
     *
     */
    @XmlElement(name = "ZPVNr")
    protected int zpvNr;
    /**
     * Sozialversicherungsnummer (Wert ohne Punkte) DT0672-Zelle 12/01
     *
     */
    @XmlElement(name = "Sozialversicherungsnummer")
    protected Long sozialversicherungsnummer;
    /**
     * Name der angefragten Person: DT0672-Zelle 101/01
     *                         oder
     *                         Name der/des Partnerin/Partners: DT0672-Zelle 303/01
     *
     */
    @XmlElement(name = "Name", required = true)
    protected String name;
    /**
     * Vorname der angefragten Person: DT0672-Zelle 102/01
     *                         oder
     *                         Vorname der/des Partnerin/Partners: DT0672-Zelle 304/01
     *
     */
    @XmlElement(name = "Vorname", required = true)
    protected String vorname;
    /**
     * Strasse der angefragten Person: DT0672-Zelle 103/01
     *                         oder
     *                         Strasse der/des Partnerin/Partners: DT0672-Zelle 305/01
     *
     */
    @XmlElement(name = "Strasse")
    protected String strasse;
    /**
     * Postleitzahl der angefragten Person: DT0672-Zelle 104/01
     *                         oder
     *                         Postleitzahl der/des Partnerin/Partners: DT0672-Zelle 306/01
     *
     */
    @XmlElement(name = "Plz")
    protected String plz;
    /**
     * Ort der angefragten Person: DT0672-Zelle 105/01
     *                         oder
     *                         Ort der/des Partnerin/Partners: DT0672-Zelle 307/01
     *
     */
    @XmlElement(name = "Ort")
    protected String ort;
    /**
     * Geschlecht DT0672-Zelle 107/01
     *
     */
    @XmlElement(name = "Geschlecht")
    @XmlSchemaType(name = "string")
    protected GeschlechtType geschlecht;
    /**
     * Geburtsdatum der angefragten Person: DT0672-Zelle 108/01
     *                         oder
     *                         Ort der/des Partnerin/Partners: DT0672-Zelle 308/01
     *
     */
    @XmlElement(name = "Geburtsdatum")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar geburtsdatum;
    /**
     * Zivilstand DT0672-Zelle 110/01
     *
     */
    @XmlElement(name = "Zivilstand")
    @XmlSchemaType(name = "string")
    protected ZivilstandType zivilstand;

    /**
     * Zpvnummer der angefragten Person: DT0672-Zelle 11/01
     *                         oder
     *                         Zpvnummer der/des Partnerin/Partners: DT0672-Zelle 302/01
     *
     */
    public int getZPVNr() {
        return zpvNr;
    }

    /**
     * Sets the value of the zpvNr property.
     *
     */
    public void setZPVNr(int value) {
        this.zpvNr = value;
    }

    /**
     * Sozialversicherungsnummer (Wert ohne Punkte) DT0672-Zelle 12/01
     *
     * @return
     *     possible object is
     *     {@link Long }
     *
     */
    public Long getSozialversicherungsnummer() {
        return sozialversicherungsnummer;
    }

    /**
     * Sets the value of the sozialversicherungsnummer property.
     *
     * @param value
     *     allowed object is
     *     {@link Long }
     *
     * @see #getSozialversicherungsnummer()
     */
    public void setSozialversicherungsnummer(Long value) {
        this.sozialversicherungsnummer = value;
    }

    /**
     * Name der angefragten Person: DT0672-Zelle 101/01
     *                         oder
     *                         Name der/des Partnerin/Partners: DT0672-Zelle 303/01
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getName()
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Vorname der angefragten Person: DT0672-Zelle 102/01
     *                         oder
     *                         Vorname der/des Partnerin/Partners: DT0672-Zelle 304/01
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getVorname() {
        return vorname;
    }

    /**
     * Sets the value of the vorname property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getVorname()
     */
    public void setVorname(String value) {
        this.vorname = value;
    }

    /**
     * Strasse der angefragten Person: DT0672-Zelle 103/01
     *                         oder
     *                         Strasse der/des Partnerin/Partners: DT0672-Zelle 305/01
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getStrasse() {
        return strasse;
    }

    /**
     * Sets the value of the strasse property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getStrasse()
     */
    public void setStrasse(String value) {
        this.strasse = value;
    }

    /**
     * Postleitzahl der angefragten Person: DT0672-Zelle 104/01
     *                         oder
     *                         Postleitzahl der/des Partnerin/Partners: DT0672-Zelle 306/01
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPlz() {
        return plz;
    }

    /**
     * Sets the value of the plz property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getPlz()
     */
    public void setPlz(String value) {
        this.plz = value;
    }

    /**
     * Ort der angefragten Person: DT0672-Zelle 105/01
     *                         oder
     *                         Ort der/des Partnerin/Partners: DT0672-Zelle 307/01
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOrt() {
        return ort;
    }

    /**
     * Sets the value of the ort property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     * @see #getOrt()
     */
    public void setOrt(String value) {
        this.ort = value;
    }

    /**
     * Geschlecht DT0672-Zelle 107/01
     *
     * @return
     *     possible object is
     *     {@link GeschlechtType }
     *
     */
    public GeschlechtType getGeschlecht() {
        return geschlecht;
    }

    /**
     * Sets the value of the geschlecht property.
     *
     * @param value
     *     allowed object is
     *     {@link GeschlechtType }
     *
     * @see #getGeschlecht()
     */
    public void setGeschlecht(GeschlechtType value) {
        this.geschlecht = value;
    }

    /**
     * Geburtsdatum der angefragten Person: DT0672-Zelle 108/01
     *                         oder
     *                         Ort der/des Partnerin/Partners: DT0672-Zelle 308/01
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getGeburtsdatum() {
        return geburtsdatum;
    }

    /**
     * Sets the value of the geburtsdatum property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     * @see #getGeburtsdatum()
     */
    public void setGeburtsdatum(XMLGregorianCalendar value) {
        this.geburtsdatum = value;
    }

    /**
     * Zivilstand DT0672-Zelle 110/01
     *
     * @return
     *     possible object is
     *     {@link ZivilstandType }
     *
     */
    public ZivilstandType getZivilstand() {
        return zivilstand;
    }

    /**
     * Sets the value of the zivilstand property.
     *
     * @param value
     *     allowed object is
     *     {@link ZivilstandType }
     *
     * @see #getZivilstand()
     */
    public void setZivilstand(ZivilstandType value) {
        this.zivilstand = value;
    }

}
