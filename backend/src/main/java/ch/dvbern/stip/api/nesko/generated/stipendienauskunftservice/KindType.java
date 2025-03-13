
package ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice;

import javax.xml.datatype.XMLGregorianCalendar;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Angaben zu einem Kind
 *
 * <p>Java class for KindType complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="KindType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="ZPVNr" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="Vorname" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="Geburtsdatum" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "KindType", propOrder = {
    "zpvNr",
    "vorname",
    "geburtsdatum"
})
public class KindType {

    /**
     * Zpvnummer Kind DT0672-Zelle 331/01
     *
     */
    @XmlElement(name = "ZPVNr")
    protected int zpvNr;
    /**
     * Vorname Kind DT0672-Zelle 331/02
     *
     */
    @XmlElement(name = "Vorname", required = true)
    protected String vorname;
    /**
     * Geburtsdatum Kind DT0672-Zelle 331/03
     *
     */
    @XmlElement(name = "Geburtsdatum", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar geburtsdatum;

    /**
     * Zpvnummer Kind DT0672-Zelle 331/01
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
     * Vorname Kind DT0672-Zelle 331/02
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
     * Geburtsdatum Kind DT0672-Zelle 331/03
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

}
