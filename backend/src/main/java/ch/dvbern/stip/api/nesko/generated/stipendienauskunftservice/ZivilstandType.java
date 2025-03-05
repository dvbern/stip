
package ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Moegliche Zivilstaende (gemäss zivilst.cod)
 *
 * <p>Java class for ZivilstandType</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * <pre>{@code
 * <simpleType name="ZivilstandType">
 *   <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     <enumeration value="Keiner"/>
 *     <enumeration value="Unbekannt"/>
 *     <enumeration value="Ledig"/>
 *     <enumeration value="Verheiratet"/>
 *     <enumeration value="Verwitwet"/>
 *     <enumeration value="Geschieden"/>
 *     <enumeration value="Getrennt"/>
 *     <enumeration value="InEingetragenerPartnerschaft"/>
 *     <enumeration value="VerwitwetEingetragenePartnerschaft"/>
 *     <enumeration value="AufgeloestePartnerschaft"/>
 *     <enumeration value="GetrennteEingetragenePartnerschaft"/>
 *   </restriction>
 * </simpleType>
 * }</pre>
 *
 */
@XmlType(name = "ZivilstandType")
@XmlEnum
public enum ZivilstandType {


    /**
     * Keiner (JP+PG)
     *
     */
    @XmlEnumValue("Keiner")
    KEINER("Keiner"),

    /**
     *  0 - Unbekannt
     *
     */
    @XmlEnumValue("Unbekannt")
    UNBEKANNT("Unbekannt"),

    /**
     *  1 - Ledig
     *
     */
    @XmlEnumValue("Ledig")
    LEDIG("Ledig"),

    /**
     *  2 - Verheiratet
     *
     */
    @XmlEnumValue("Verheiratet")
    VERHEIRATET("Verheiratet"),

    /**
     *  3 - Verwitwet
     *
     */
    @XmlEnumValue("Verwitwet")
    VERWITWET("Verwitwet"),

    /**
     *  4 - Geschieden
     *
     */
    @XmlEnumValue("Geschieden")
    GESCHIEDEN("Geschieden"),

    /**
     *  5 - Getrennt
     *
     */
    @XmlEnumValue("Getrennt")
    GETRENNT("Getrennt"),

    /**
     *  6 - In eingetragener Partnerschaft
     *
     */
    @XmlEnumValue("InEingetragenerPartnerschaft")
    IN_EINGETRAGENER_PARTNERSCHAFT("InEingetragenerPartnerschaft"),

    /**
     *  7 - Verwitwet eingetragene Partnerschaft
     *
     */
    @XmlEnumValue("VerwitwetEingetragenePartnerschaft")
    VERWITWET_EINGETRAGENE_PARTNERSCHAFT("VerwitwetEingetragenePartnerschaft"),

    /**
     *  8 - Aufgeloeste Partnerschaft
     *
     */
    @XmlEnumValue("AufgeloestePartnerschaft")
    AUFGELOESTE_PARTNERSCHAFT("AufgeloestePartnerschaft"),

    /**
     *  9 - Getrennte eingetragene Partnerschaft
     *
     */
    @XmlEnumValue("GetrennteEingetragenePartnerschaft")
    GETRENNTE_EINGETRAGENE_PARTNERSCHAFT("GetrennteEingetragenePartnerschaft");
    private final String value;

    ZivilstandType(String v) {
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
    public static ZivilstandType fromValue(String v) {
        for (ZivilstandType c: ZivilstandType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
