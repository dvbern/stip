
package ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice;

import java.math.BigDecimal;

import javax.xml.datatype.XMLGregorianCalendar;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SteuerdatenType complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="SteuerdatenType">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="StatusVeranlagung" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}VeranlagungsStatusType"/>
 *         <element name="DatumStatusVeranlagung" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         <element name="AnzahlKinderabzuege" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}AnzahlKinderabzuegeType" minOccurs="0"/>
 *         <element name="SteuerbaresEinkommenKanton" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}EffSatzType" minOccurs="0"/>
 *         <element name="SteuerbaresEinkommenBund" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}EffSatzType" minOccurs="0"/>
 *         <element name="SteuerbaresVermoegenKanton" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}EffSatzType" minOccurs="0"/>
 *         <element name="SteuerbetragKanton" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}BetragType" minOccurs="0"/>
 *         <element name="SteuerbetragBund" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}BetragType" minOccurs="0"/>
 *         <element name="TotalEinkuenfte" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}EffSatzType" minOccurs="0"/>
 *         <element name="Fahrkosten" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}MannFrauEffSatzType" minOccurs="0"/>
 *         <element name="KostenAuswaertigeVerpflegung" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}MannFrauEffSatzType" minOccurs="0"/>
 *         <element name="Haupterwerb" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}MannFrauEffSatzType" minOccurs="0"/>
 *         <element name="Nebenerwerb" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}MannFrauEffSatzType" minOccurs="0"/>
 *         <element name="MannErwerbstaetigkeitSUS" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         <element name="FrauErwerbstaetigkeitSUS" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         <element name="BeitraegeSaeule3A" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}MannFrauEffSatzType" minOccurs="0"/>
 *         <element name="AufwaendeSelbstErwerbAngefragtePerson" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}AufwaendeSelbstErwerbType" minOccurs="0"/>
 *         <element name="AufwaendeSelbstErwerbEhepartnerIn" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}AufwaendeSelbstErwerbType" minOccurs="0"/>
 *         <element name="MietwertKanton" type="{http://sv.fin.be.ch/schemas/NESKOVANP/20190716/StipendienAuskunftService}BetragType" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SteuerdatenType", propOrder = {
    "statusVeranlagung",
    "datumStatusVeranlagung",
    "anzahlKinderabzuege",
    "steuerbaresEinkommenKanton",
    "steuerbaresEinkommenBund",
    "steuerbaresVermoegenKanton",
    "steuerbetragKanton",
    "steuerbetragBund",
    "totalEinkuenfte",
    "fahrkosten",
    "kostenAuswaertigeVerpflegung",
    "haupterwerb",
    "nebenerwerb",
    "mannErwerbstaetigkeitSUS",
    "frauErwerbstaetigkeitSUS",
    "beitraegeSaeule3A",
    "aufwaendeSelbstErwerbAngefragtePerson",
    "aufwaendeSelbstErwerbEhepartnerIn",
    "mietwertKanton"
})
public class SteuerdatenType {

    /**
     * Status Veranlagung DT0672-Zelle 02/01
     *
     */
    @XmlElement(name = "StatusVeranlagung", required = true)
    @XmlSchemaType(name = "string")
    protected VeranlagungsStatusType statusVeranlagung;
    /**
     * Datum Status Veranlagung DT0672-Zelle 03/01
     *
     */
    @XmlElement(name = "DatumStatusVeranlagung", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datumStatusVeranlagung;
    /**
     * Anzahl Kinderabzuege DT0672-Zelle 106/01
     *
     */
    @XmlElement(name = "AnzahlKinderabzuege")
    protected BigDecimal anzahlKinderabzuege;
    /**
     * Steuerbares Einkommen Kanton
     *                         DT0672-Zelle 201/01 (Effektiv) und DT0672-Zelle 202/01 (Satzbestimmend)
     *
     */
    @XmlElement(name = "SteuerbaresEinkommenKanton")
    protected EffSatzType steuerbaresEinkommenKanton;
    /**
     * Steuerbares Einkommen Bund
     *                         DT0672-Zelle 203/01 (Effektiv) und DT0672-Zelle 204/01 (Satzbestimmend)
     *
     */
    @XmlElement(name = "SteuerbaresEinkommenBund")
    protected EffSatzType steuerbaresEinkommenBund;
    /**
     * Steuerbares Vermoegen
     *                         DT0672-Zelle 205/01 (Effektiv) und DT0672-Zelle 206/01 (Satzbestimmend)
     *
     */
    @XmlElement(name = "SteuerbaresVermoegenKanton")
    protected EffSatzType steuerbaresVermoegenKanton;
    /**
     * Steuerbetrag Kanton DT0672-Zelle 207/01
     *
     */
    @XmlElement(name = "SteuerbetragKanton")
    protected BigDecimal steuerbetragKanton;
    /**
     * Steuerbetrag Bund DT0672-Zelle 208/01
     *
     */
    @XmlElement(name = "SteuerbetragBund")
    protected BigDecimal steuerbetragBund;
    /**
     * Einkuenfte DT0672-Zelle 209/01 (Effektiv) und DT0672-Zelle 210/01 (Satzbestimmend)
     *
     */
    @XmlElement(name = "TotalEinkuenfte")
    protected EffSatzType totalEinkuenfte;
    /**
     * Fahrkosten
     *                         Mann: DT0672-Zelle 211/01 (Effektiv) und DT0672-Zelle 212/01 (Satzbestimmend)
     *                         Frau: DT0672-Zelle 213/01 (Effektiv) und DT0672-Zelle 214/01 (Satzbestimmend)
     *
     */
    @XmlElement(name = "Fahrkosten")
    protected MannFrauEffSatzType fahrkosten;
    /**
     * Kosten auswaertige Verpflegung
     *                         Mann: DT0672-Zelle 215/01 (Effektiv) und DT0672-Zelle 216/01 (Satzbestimmend)
     *                         Frau: DT0672-Zelle 217/01 (Effektiv) und DT0672-Zelle 218/01 (Satzbestimmend)
     *
     */
    @XmlElement(name = "KostenAuswaertigeVerpflegung")
    protected MannFrauEffSatzType kostenAuswaertigeVerpflegung;
    /**
     * Haupterwerb
     *                         Mann: DT0672-Zelle 219/01 (Effektiv) und DT0672-Zelle 220/01 (Satzbestimmend)
     *                         Frau: DT0672-Zelle 221/01 (Effektiv) und DT0672-Zelle 222/01 (Satzbestimmend)
     *
     */
    @XmlElement(name = "Haupterwerb")
    protected MannFrauEffSatzType haupterwerb;
    /**
     * Nebenerwerb
     *                         Mann: DT0672-Zelle 223/01 (Effektiv) und DT0672-Zelle 224/01 (Satzbestimmend)
     *                         Frau: DT0672-Zelle 225/01 (Effektiv) und DT0672-Zelle 226/01 (Satzbestimmend)
     *
     */
    @XmlElement(name = "Nebenerwerb")
    protected MannFrauEffSatzType nebenerwerb;
    /**
     * Mann Selbstaendige/Unselbstaendige Erwerbstaetigkeit (DT0672-Zelle 227/02)
     *
     *                         true=Unselbstaendige Erwerbstaetigkeit Mann:
     *                         DT0001 bis DT0008 UND Keine Beteiligung an Kollektiv-/Kommanditgesellschaften
     *                         UND Keine Beteiligung an Bau-/einfachen Gesellschaften UND Keine DT0009, DT0010
     *
     *                         false=Selbstaendige Erwerbstaetigkeit Mann:
     *                         DT0001 bis DT0008 UND (Beteiligung an Kollektiv-/Kommanditgesellschaften ODER Beteiligung
     *                         an Bau-/einfachen Gesellschaften ODER DT0009, DT0010)
     *
     */
    @XmlElement(name = "MannErwerbstaetigkeitSUS")
    protected Boolean mannErwerbstaetigkeitSUS;
    /**
     * Frau Selbstaendige/Unselbstaendige Erwerbstaetigkeit (DT0672-Zelle 227/03)
     *
     *                         true=Unselbstaendige Erwerbstaetigkeit Frau:
     *                         DT0001 bis DT0008 UND Keine Beteiligung an Kollektiv-/Kommanditgesellschaften
     *                         UND Keine Beteiligung an Bau-/einfachen Gesellschaften UND Keine DT0009, DT0010
     *
     *                         false=Selbstaendige Erwerbstaetigkeit Frau:
     *                         DT0001 bis DT0008 UND (Beteiligung an Kollektiv-/Kommanditgesellschaften ODER Beteiligung
     *                         an Bau-/einfachen Gesellschaften ODER DT0009, DT0010)
     *
     */
    @XmlElement(name = "FrauErwerbstaetigkeitSUS")
    protected Boolean frauErwerbstaetigkeitSUS;
    /**
     * Beitraege Saeule 3a
     *                         Mann: DT0672-Zelle 228/01 (Effektiv) und DT0672-Zelle 229/01 (Satzbestimmend)
     *                         Frau: DT0672-Zelle 230/01 (Effektiv) und DT0672-Zelle 231/01 (Satzbestimmend)
     *
     */
    @XmlElement(name = "BeitraegeSaeule3A")
    protected MannFrauEffSatzType beitraegeSaeule3A;
    /**
     * Summe persoenliche Beitraege/ER belastete Anteile an 2.Saeule, Angefragte Person
     *                         Persoenliche Beitraege: DT0672-Zelle 232/01 (Effektiv) und DT0672-Zelle 233/01 (Satzbestimmend)
     *                         ER belastete Anteile: DT0672-Zelle 234/01 (Effektiv) und DT0672-Zelle 235/01 (Satzbestimmend)
     *
     */
    @XmlElement(name = "AufwaendeSelbstErwerbAngefragtePerson")
    protected AufwaendeSelbstErwerbType aufwaendeSelbstErwerbAngefragtePerson;
    /**
     * Summe persoenliche Beitraege/ER belastete Anteile an 2.Saeule, EhepartnerIn
     *                         Persoenliche Beitraege: DT0672-Zelle 236/01 (Effektiv) und DT0672-Zelle 237/01 (Satzbestimmend)
     *                         ER belastete Anteile: DT0672-Zelle 238/01 (Effektiv) und DT0672-Zelle 239/01 (Satzbestimmend)
     *
     */
    @XmlElement(name = "AufwaendeSelbstErwerbEhepartnerIn")
    protected AufwaendeSelbstErwerbType aufwaendeSelbstErwerbEhepartnerIn;
    /**
     * Summe der Mietwerte Kanton fuer die angefragte Person DT0672-Zelle 240/01
     *
     */
    @XmlElement(name = "MietwertKanton")
    protected BigDecimal mietwertKanton;

    /**
     * Status Veranlagung DT0672-Zelle 02/01
     *
     * @return
     *     possible object is
     *     {@link VeranlagungsStatusType }
     *
     */
    public VeranlagungsStatusType getStatusVeranlagung() {
        return statusVeranlagung;
    }

    /**
     * Sets the value of the statusVeranlagung property.
     *
     * @param value
     *     allowed object is
     *     {@link VeranlagungsStatusType }
     *
     * @see #getStatusVeranlagung()
     */
    public void setStatusVeranlagung(VeranlagungsStatusType value) {
        this.statusVeranlagung = value;
    }

    /**
     * Datum Status Veranlagung DT0672-Zelle 03/01
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getDatumStatusVeranlagung() {
        return datumStatusVeranlagung;
    }

    /**
     * Sets the value of the datumStatusVeranlagung property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     * @see #getDatumStatusVeranlagung()
     */
    public void setDatumStatusVeranlagung(XMLGregorianCalendar value) {
        this.datumStatusVeranlagung = value;
    }

    /**
     * Anzahl Kinderabzuege DT0672-Zelle 106/01
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getAnzahlKinderabzuege() {
        return anzahlKinderabzuege;
    }

    /**
     * Sets the value of the anzahlKinderabzuege property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     * @see #getAnzahlKinderabzuege()
     */
    public void setAnzahlKinderabzuege(BigDecimal value) {
        this.anzahlKinderabzuege = value;
    }

    /**
     * Steuerbares Einkommen Kanton
     *                         DT0672-Zelle 201/01 (Effektiv) und DT0672-Zelle 202/01 (Satzbestimmend)
     *
     * @return
     *     possible object is
     *     {@link EffSatzType }
     *
     */
    public EffSatzType getSteuerbaresEinkommenKanton() {
        return steuerbaresEinkommenKanton;
    }

    /**
     * Sets the value of the steuerbaresEinkommenKanton property.
     *
     * @param value
     *     allowed object is
     *     {@link EffSatzType }
     *
     * @see #getSteuerbaresEinkommenKanton()
     */
    public void setSteuerbaresEinkommenKanton(EffSatzType value) {
        this.steuerbaresEinkommenKanton = value;
    }

    /**
     * Steuerbares Einkommen Bund
     *                         DT0672-Zelle 203/01 (Effektiv) und DT0672-Zelle 204/01 (Satzbestimmend)
     *
     * @return
     *     possible object is
     *     {@link EffSatzType }
     *
     */
    public EffSatzType getSteuerbaresEinkommenBund() {
        return steuerbaresEinkommenBund;
    }

    /**
     * Sets the value of the steuerbaresEinkommenBund property.
     *
     * @param value
     *     allowed object is
     *     {@link EffSatzType }
     *
     * @see #getSteuerbaresEinkommenBund()
     */
    public void setSteuerbaresEinkommenBund(EffSatzType value) {
        this.steuerbaresEinkommenBund = value;
    }

    /**
     * Steuerbares Vermoegen
     *                         DT0672-Zelle 205/01 (Effektiv) und DT0672-Zelle 206/01 (Satzbestimmend)
     *
     * @return
     *     possible object is
     *     {@link EffSatzType }
     *
     */
    public EffSatzType getSteuerbaresVermoegenKanton() {
        return steuerbaresVermoegenKanton;
    }

    /**
     * Sets the value of the steuerbaresVermoegenKanton property.
     *
     * @param value
     *     allowed object is
     *     {@link EffSatzType }
     *
     * @see #getSteuerbaresVermoegenKanton()
     */
    public void setSteuerbaresVermoegenKanton(EffSatzType value) {
        this.steuerbaresVermoegenKanton = value;
    }

    /**
     * Steuerbetrag Kanton DT0672-Zelle 207/01
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getSteuerbetragKanton() {
        return steuerbetragKanton;
    }

    /**
     * Sets the value of the steuerbetragKanton property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     * @see #getSteuerbetragKanton()
     */
    public void setSteuerbetragKanton(BigDecimal value) {
        this.steuerbetragKanton = value;
    }

    /**
     * Steuerbetrag Bund DT0672-Zelle 208/01
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getSteuerbetragBund() {
        return steuerbetragBund;
    }

    /**
     * Sets the value of the steuerbetragBund property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     * @see #getSteuerbetragBund()
     */
    public void setSteuerbetragBund(BigDecimal value) {
        this.steuerbetragBund = value;
    }

    /**
     * Einkuenfte DT0672-Zelle 209/01 (Effektiv) und DT0672-Zelle 210/01 (Satzbestimmend)
     *
     * @return
     *     possible object is
     *     {@link EffSatzType }
     *
     */
    public EffSatzType getTotalEinkuenfte() {
        return totalEinkuenfte;
    }

    /**
     * Sets the value of the totalEinkuenfte property.
     *
     * @param value
     *     allowed object is
     *     {@link EffSatzType }
     *
     * @see #getTotalEinkuenfte()
     */
    public void setTotalEinkuenfte(EffSatzType value) {
        this.totalEinkuenfte = value;
    }

    /**
     * Fahrkosten
     *                         Mann: DT0672-Zelle 211/01 (Effektiv) und DT0672-Zelle 212/01 (Satzbestimmend)
     *                         Frau: DT0672-Zelle 213/01 (Effektiv) und DT0672-Zelle 214/01 (Satzbestimmend)
     *
     * @return
     *     possible object is
     *     {@link MannFrauEffSatzType }
     *
     */
    public MannFrauEffSatzType getFahrkosten() {
        return fahrkosten;
    }

    /**
     * Sets the value of the fahrkosten property.
     *
     * @param value
     *     allowed object is
     *     {@link MannFrauEffSatzType }
     *
     * @see #getFahrkosten()
     */
    public void setFahrkosten(MannFrauEffSatzType value) {
        this.fahrkosten = value;
    }

    /**
     * Kosten auswaertige Verpflegung
     *                         Mann: DT0672-Zelle 215/01 (Effektiv) und DT0672-Zelle 216/01 (Satzbestimmend)
     *                         Frau: DT0672-Zelle 217/01 (Effektiv) und DT0672-Zelle 218/01 (Satzbestimmend)
     *
     * @return
     *     possible object is
     *     {@link MannFrauEffSatzType }
     *
     */
    public MannFrauEffSatzType getKostenAuswaertigeVerpflegung() {
        return kostenAuswaertigeVerpflegung;
    }

    /**
     * Sets the value of the kostenAuswaertigeVerpflegung property.
     *
     * @param value
     *     allowed object is
     *     {@link MannFrauEffSatzType }
     *
     * @see #getKostenAuswaertigeVerpflegung()
     */
    public void setKostenAuswaertigeVerpflegung(MannFrauEffSatzType value) {
        this.kostenAuswaertigeVerpflegung = value;
    }

    /**
     * Haupterwerb
     *                         Mann: DT0672-Zelle 219/01 (Effektiv) und DT0672-Zelle 220/01 (Satzbestimmend)
     *                         Frau: DT0672-Zelle 221/01 (Effektiv) und DT0672-Zelle 222/01 (Satzbestimmend)
     *
     * @return
     *     possible object is
     *     {@link MannFrauEffSatzType }
     *
     */
    public MannFrauEffSatzType getHaupterwerb() {
        return haupterwerb;
    }

    /**
     * Sets the value of the haupterwerb property.
     *
     * @param value
     *     allowed object is
     *     {@link MannFrauEffSatzType }
     *
     * @see #getHaupterwerb()
     */
    public void setHaupterwerb(MannFrauEffSatzType value) {
        this.haupterwerb = value;
    }

    /**
     * Nebenerwerb
     *                         Mann: DT0672-Zelle 223/01 (Effektiv) und DT0672-Zelle 224/01 (Satzbestimmend)
     *                         Frau: DT0672-Zelle 225/01 (Effektiv) und DT0672-Zelle 226/01 (Satzbestimmend)
     *
     * @return
     *     possible object is
     *     {@link MannFrauEffSatzType }
     *
     */
    public MannFrauEffSatzType getNebenerwerb() {
        return nebenerwerb;
    }

    /**
     * Sets the value of the nebenerwerb property.
     *
     * @param value
     *     allowed object is
     *     {@link MannFrauEffSatzType }
     *
     * @see #getNebenerwerb()
     */
    public void setNebenerwerb(MannFrauEffSatzType value) {
        this.nebenerwerb = value;
    }

    /**
     * Mann Selbstaendige/Unselbstaendige Erwerbstaetigkeit (DT0672-Zelle 227/02)
     *
     *                         true=Unselbstaendige Erwerbstaetigkeit Mann:
     *                         DT0001 bis DT0008 UND Keine Beteiligung an Kollektiv-/Kommanditgesellschaften
     *                         UND Keine Beteiligung an Bau-/einfachen Gesellschaften UND Keine DT0009, DT0010
     *
     *                         false=Selbstaendige Erwerbstaetigkeit Mann:
     *                         DT0001 bis DT0008 UND (Beteiligung an Kollektiv-/Kommanditgesellschaften ODER Beteiligung
     *                         an Bau-/einfachen Gesellschaften ODER DT0009, DT0010)
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isMannErwerbstaetigkeitSUS() {
        return mannErwerbstaetigkeitSUS;
    }

    /**
     * Sets the value of the mannErwerbstaetigkeitSUS property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     * @see #isMannErwerbstaetigkeitSUS()
     */
    public void setMannErwerbstaetigkeitSUS(Boolean value) {
        this.mannErwerbstaetigkeitSUS = value;
    }

    /**
     * Frau Selbstaendige/Unselbstaendige Erwerbstaetigkeit (DT0672-Zelle 227/03)
     *
     *                         true=Unselbstaendige Erwerbstaetigkeit Frau:
     *                         DT0001 bis DT0008 UND Keine Beteiligung an Kollektiv-/Kommanditgesellschaften
     *                         UND Keine Beteiligung an Bau-/einfachen Gesellschaften UND Keine DT0009, DT0010
     *
     *                         false=Selbstaendige Erwerbstaetigkeit Frau:
     *                         DT0001 bis DT0008 UND (Beteiligung an Kollektiv-/Kommanditgesellschaften ODER Beteiligung
     *                         an Bau-/einfachen Gesellschaften ODER DT0009, DT0010)
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isFrauErwerbstaetigkeitSUS() {
        return frauErwerbstaetigkeitSUS;
    }

    /**
     * Sets the value of the frauErwerbstaetigkeitSUS property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     * @see #isFrauErwerbstaetigkeitSUS()
     */
    public void setFrauErwerbstaetigkeitSUS(Boolean value) {
        this.frauErwerbstaetigkeitSUS = value;
    }

    /**
     * Beitraege Saeule 3a
     *                         Mann: DT0672-Zelle 228/01 (Effektiv) und DT0672-Zelle 229/01 (Satzbestimmend)
     *                         Frau: DT0672-Zelle 230/01 (Effektiv) und DT0672-Zelle 231/01 (Satzbestimmend)
     *
     * @return
     *     possible object is
     *     {@link MannFrauEffSatzType }
     *
     */
    public MannFrauEffSatzType getBeitraegeSaeule3A() {
        return beitraegeSaeule3A;
    }

    /**
     * Sets the value of the beitraegeSaeule3A property.
     *
     * @param value
     *     allowed object is
     *     {@link MannFrauEffSatzType }
     *
     * @see #getBeitraegeSaeule3A()
     */
    public void setBeitraegeSaeule3A(MannFrauEffSatzType value) {
        this.beitraegeSaeule3A = value;
    }

    /**
     * Summe persoenliche Beitraege/ER belastete Anteile an 2.Saeule, Angefragte Person
     *                         Persoenliche Beitraege: DT0672-Zelle 232/01 (Effektiv) und DT0672-Zelle 233/01 (Satzbestimmend)
     *                         ER belastete Anteile: DT0672-Zelle 234/01 (Effektiv) und DT0672-Zelle 235/01 (Satzbestimmend)
     *
     * @return
     *     possible object is
     *     {@link AufwaendeSelbstErwerbType }
     *
     */
    public AufwaendeSelbstErwerbType getAufwaendeSelbstErwerbAngefragtePerson() {
        return aufwaendeSelbstErwerbAngefragtePerson;
    }

    /**
     * Sets the value of the aufwaendeSelbstErwerbAngefragtePerson property.
     *
     * @param value
     *     allowed object is
     *     {@link AufwaendeSelbstErwerbType }
     *
     * @see #getAufwaendeSelbstErwerbAngefragtePerson()
     */
    public void setAufwaendeSelbstErwerbAngefragtePerson(AufwaendeSelbstErwerbType value) {
        this.aufwaendeSelbstErwerbAngefragtePerson = value;
    }

    /**
     * Summe persoenliche Beitraege/ER belastete Anteile an 2.Saeule, EhepartnerIn
     *                         Persoenliche Beitraege: DT0672-Zelle 236/01 (Effektiv) und DT0672-Zelle 237/01 (Satzbestimmend)
     *                         ER belastete Anteile: DT0672-Zelle 238/01 (Effektiv) und DT0672-Zelle 239/01 (Satzbestimmend)
     *
     * @return
     *     possible object is
     *     {@link AufwaendeSelbstErwerbType }
     *
     */
    public AufwaendeSelbstErwerbType getAufwaendeSelbstErwerbEhepartnerIn() {
        return aufwaendeSelbstErwerbEhepartnerIn;
    }

    /**
     * Sets the value of the aufwaendeSelbstErwerbEhepartnerIn property.
     *
     * @param value
     *     allowed object is
     *     {@link AufwaendeSelbstErwerbType }
     *
     * @see #getAufwaendeSelbstErwerbEhepartnerIn()
     */
    public void setAufwaendeSelbstErwerbEhepartnerIn(AufwaendeSelbstErwerbType value) {
        this.aufwaendeSelbstErwerbEhepartnerIn = value;
    }

    /**
     * Summe der Mietwerte Kanton fuer die angefragte Person DT0672-Zelle 240/01
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getMietwertKanton() {
        return mietwertKanton;
    }

    /**
     * Sets the value of the mietwertKanton property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     * @see #getMietwertKanton()
     */
    public void setMietwertKanton(BigDecimal value) {
        this.mietwertKanton = value;
    }

}
