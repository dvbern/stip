
package ch.dvbern.stip.api.nesko.generated.stipendienauskunftservice;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Moegliche Geschlechter (gemäss mfn.cod)
 *
 * <p>Java class for GeschlechtType</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 * <pre>{@code
 * <simpleType name="GeschlechtType">
 *   <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     <enumeration value="Frau"/>
 *     <enumeration value="Mann"/>
 *     <enumeration value="Neutral"/>
 *   </restriction>
 * </simpleType>
 * }</pre>
 *
 */
@XmlType(name = "GeschlechtType")
@XmlEnum
public enum GeschlechtType {


    /**
     * W - Frau
     *
     */
    @XmlEnumValue("Frau")
    FRAU("Frau"),

    /**
     * M - Mann
     *
     */
    @XmlEnumValue("Mann")
    MANN("Mann"),

    /**
     * Neutral
     *
     */
    @XmlEnumValue("Neutral")
    NEUTRAL("Neutral");
    private final String value;

    GeschlechtType(String v) {
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
    public static GeschlechtType fromValue(String v) {
        for (GeschlechtType c: GeschlechtType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
