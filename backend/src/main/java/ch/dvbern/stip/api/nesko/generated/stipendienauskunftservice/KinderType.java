
package ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for KinderType complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="KinderType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="AnzahlKinder" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         <element name="Kind" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}KindType" maxOccurs="unbounded" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "KinderType", propOrder = {
    "anzahlKinder",
    "kind"
})
public class KinderType {

    /**
     * Anzahl Kinder DT0672-Zelle 330/01
     *
     */
    @XmlElement(name = "AnzahlKinder")
    protected int anzahlKinder;
    @XmlElement(name = "Kind")
    protected List<KindType> kind;

    /**
     * Anzahl Kinder DT0672-Zelle 330/01
     *
     */
    public int getAnzahlKinder() {
        return anzahlKinder;
    }

    /**
     * Sets the value of the anzahlKinder property.
     *
     */
    public void setAnzahlKinder(int value) {
        this.anzahlKinder = value;
    }

    /**
     * Gets the value of the kind property.
     *
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the kind property.</p>
     *
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getKind().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link KindType }
     * </p>
     *
     *
     * @return
     *     The value of the kind property.
     */
    public List<KindType> getKind() {
        if (kind == null) {
            kind = new ArrayList<>();
        }
        return this.kind;
    }

}
