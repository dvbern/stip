
package ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Moegliche Veranlagungsstati
 *
 * <p>Java class for VeranlagungsStatusType</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * <pre>{@code
 * <simpleType name="VeranlagungsStatusType">
 *   <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     <enumeration value="DokumentBereitFuerVersand"/>
 *     <enumeration value="DokumentVersandt"/>
 *     <enumeration value="DokumentEingegangen"/>
 *     <enumeration value="DokumentProvisorischErfasst"/>
 *     <enumeration value="DokumentErfasst"/>
 *     <enumeration value="Vorerfasst"/>
 *     <enumeration value="AutomatischProvisorischVeranlagt"/>
 *     <enumeration value="SuvProvisorischVeranlagt"/>
 *     <enumeration value="AutomatischDefinitivVeranlagt"/>
 *     <enumeration value="SuvDefinitivVeranlagt"/>
 *     <enumeration value="VerfahrenHaengig"/>
 *     <enumeration value="ProvisorischBearbeitet"/>
 *     <enumeration value="DefinitivBearbeitet"/>
 *     <enumeration value="AnGerichtsinstanzWeitergeleitet"/>
 *     <enumeration value="Eroeffnet"/>
 *     <enumeration value="Rechtskraeftig"/>
 *   </restriction>
 * </simpleType>
 * }</pre>
 *
 */
@XmlType(name = "VeranlagungsStatusType")
@XmlEnum
public enum VeranlagungsStatusType {


    /**
     *  00 - Dokument bereit fuer Versand
     *
     */
    @XmlEnumValue("DokumentBereitFuerVersand")
    DOKUMENT_BEREIT_FUER_VERSAND("DokumentBereitFuerVersand"),

    /**
     *  01 - Dokument versandt
     *
     */
    @XmlEnumValue("DokumentVersandt")
    DOKUMENT_VERSANDT("DokumentVersandt"),

    /**
     *  02 - Dokument eingegangen
     *
     */
    @XmlEnumValue("DokumentEingegangen")
    DOKUMENT_EINGEGANGEN("DokumentEingegangen"),

    /**
     *  04 - Dokument provisorisch erfasst
     *
     */
    @XmlEnumValue("DokumentProvisorischErfasst")
    DOKUMENT_PROVISORISCH_ERFASST("DokumentProvisorischErfasst"),

    /**
     *  05 - Dokument erfasst
     *
     */
    @XmlEnumValue("DokumentErfasst")
    DOKUMENT_ERFASST("DokumentErfasst"),

    /**
     *  10 - Vorerfasst
     *
     */
    @XmlEnumValue("Vorerfasst")
    VORERFASST("Vorerfasst"),

    /**
     *  20 - Automatisch provisorisch veranlagt
     *
     */
    @XmlEnumValue("AutomatischProvisorischVeranlagt")
    AUTOMATISCH_PROVISORISCH_VERANLAGT("AutomatischProvisorischVeranlagt"),

    /**
     *  21 - SUV provisorisch veranlagt
     *
     */
    @XmlEnumValue("SuvProvisorischVeranlagt")
    SUV_PROVISORISCH_VERANLAGT("SuvProvisorischVeranlagt"),

    /**
     *  30 - Automatisch definitiv veranlagt
     *
     */
    @XmlEnumValue("AutomatischDefinitivVeranlagt")
    AUTOMATISCH_DEFINITIV_VERANLAGT("AutomatischDefinitivVeranlagt"),

    /**
     *  31 - SUV definitiv veranlagt
     *
     */
    @XmlEnumValue("SuvDefinitivVeranlagt")
    SUV_DEFINITIV_VERANLAGT("SuvDefinitivVeranlagt"),

    /**
     *  50 - Verfahren haengig
     *
     */
    @XmlEnumValue("VerfahrenHaengig")
    VERFAHREN_HAENGIG("VerfahrenHaengig"),

    /**
     *  51 - Provisorisch bearbeitet
     *
     */
    @XmlEnumValue("ProvisorischBearbeitet")
    PROVISORISCH_BEARBEITET("ProvisorischBearbeitet"),

    /**
     *  52 - Definitiv bearbeitet
     *
     */
    @XmlEnumValue("DefinitivBearbeitet")
    DEFINITIV_BEARBEITET("DefinitivBearbeitet"),

    /**
     *  53 - An Gerichtsinstanz weitergeleitet
     *
     */
    @XmlEnumValue("AnGerichtsinstanzWeitergeleitet")
    AN_GERICHTSINSTANZ_WEITERGELEITET("AnGerichtsinstanzWeitergeleitet"),

    /**
     *  90 - Eroeffnet
     *
     */
    @XmlEnumValue("Eroeffnet")
    EROEFFNET("Eroeffnet"),

    /**
     *  91 - Rechtskraeftig
     *
     */
    @XmlEnumValue("Rechtskraeftig")
    RECHTSKRAEFTIG("Rechtskraeftig");
    private final String value;

    VeranlagungsStatusType(String v) {
        value = v;
    }

    /**
     * Gets the value associated to the enum constant.
     *
     * @return
     *     The value linked to the enum.
     */
    public String value() {
        return value;
    }

    /**
     * Gets the enum associated to the value passed as parameter.
     *
     * @param v
     *     The value to get the enum from.
     * @return
     *     The enum which corresponds to the value, if it exists.
     * @throws IllegalArgumentException
     *     If no value matches in the enum declaration.
     */
    public static VeranlagungsStatusType fromValue(String v) {
        for (VeranlagungsStatusType c: VeranlagungsStatusType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
