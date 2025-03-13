
package ch.dvbern.stip.api.nesko.generated.exceptioninfo;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for InvalidArgumentsFault complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="InvalidArgumentsFault">
 *   <complexContent>
 *     <extension base="{http://sv.fin.be.ch/schemas/base/20070131/ExceptionInfo}FaultBase">
 *       <sequence>
 *         <element name="ArgumentName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       </sequence>
 *     </extension>
 *   </complexContent>
 * </complexType>
 * }</pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InvalidArgumentsFault", propOrder = {
    "argumentName"
})
public class InvalidArgumentsFault
    extends FaultBase
{

    @XmlElement(name = "ArgumentName", required = true)
    protected String argumentName;

    /**
     * Gets the value of the argumentName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getArgumentName() {
        return argumentName;
    }

    /**
     * Sets the value of the argumentName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setArgumentName(String value) {
        this.argumentName = value;
    }

}
