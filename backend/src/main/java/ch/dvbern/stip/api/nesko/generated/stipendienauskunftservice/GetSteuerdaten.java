
package ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetSteuerdaten complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="GetSteuerdaten">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="ZPVNr" minOccurs="0">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               <minExclusive value="0"/>
 *               <maxInclusive value="999999999"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="Sozialversicherungsnummer" minOccurs="0">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}long">
 *               <minExclusive value="0"/>
 *               <maxInclusive value="9999999999999"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="Steuerjahr">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               <pattern value="[2-9][0-9][0-9][0-9]"/>
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
@XmlType(name = "GetSteuerdaten", propOrder = {
    "zpvNr",
    "sozialversicherungsnummer",
    "steuerjahr"
})
public class GetSteuerdaten {

    /**
     * ZPV-Nummer muss vorhanden sein, wenn Sozialversicherungsnummer fehlt
     *
     */
    @XmlElement(name = "ZPVNr")
    protected Integer zpvNr;
    /**
     * Sozialversicherungsnummer muss vorhanden sein, wenn ZPV-Nummer fehlt
     *
     */
    @XmlElement(name = "Sozialversicherungsnummer")
    protected Long sozialversicherungsnummer;
    /**
     * Steuerjahr muss immer vorhanden sein
     *
     */
    @XmlElement(name = "Steuerjahr")
    protected int steuerjahr;

    /**
     * ZPV-Nummer muss vorhanden sein, wenn Sozialversicherungsnummer fehlt
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getZPVNr() {
        return zpvNr;
    }

    /**
     * Sets the value of the zpvNr property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     * @see #getZPVNr()
     */
    public void setZPVNr(Integer value) {
        this.zpvNr = value;
    }

    /**
     * Sozialversicherungsnummer muss vorhanden sein, wenn ZPV-Nummer fehlt
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
     * Steuerjahr muss immer vorhanden sein
     *
     */
    public int getSteuerjahr() {
        return steuerjahr;
    }

    /**
     * Sets the value of the steuerjahr property.
     *
     */
    public void setSteuerjahr(int value) {
        this.steuerjahr = value;
    }

}
