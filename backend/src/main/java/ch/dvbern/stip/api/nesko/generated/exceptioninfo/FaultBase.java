
package ch.dvbern.stip.api.nesko.generated.exceptioninfo;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FaultBase complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="FaultBase">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="ErrorCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="UserMessage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="TechnicalMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="ErrorTicketId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="InnerFault" type="{http://sv.fin.be.ch/schemas/base/20070131/ExceptionInfo}FaultBase" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FaultBase", propOrder = {
    "errorCode",
    "userMessage",
    "technicalMessage",
    "errorTicketId",
    "innerFault"
})
@XmlSeeAlso({
    InfrastructureFault.class,
    InvalidArgumentsFault.class,
    BusinessFault.class,
    PermissionDeniedFault.class
})
public class FaultBase {

    @XmlElement(name = "ErrorCode")
    protected String errorCode;
    @XmlElement(name = "UserMessage", required = true)
    protected String userMessage;
    @XmlElement(name = "TechnicalMessage")
    protected String technicalMessage;
    @XmlElement(name = "ErrorTicketId")
    protected String errorTicketId;
    @XmlElement(name = "InnerFault")
    protected FaultBase innerFault;

    /**
     * Gets the value of the errorCode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Sets the value of the errorCode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setErrorCode(String value) {
        this.errorCode = value;
    }

    /**
     * Gets the value of the userMessage property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getUserMessage() {
        return userMessage;
    }

    /**
     * Sets the value of the userMessage property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setUserMessage(String value) {
        this.userMessage = value;
    }

    /**
     * Gets the value of the technicalMessage property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTechnicalMessage() {
        return technicalMessage;
    }

    /**
     * Sets the value of the technicalMessage property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTechnicalMessage(String value) {
        this.technicalMessage = value;
    }

    /**
     * Gets the value of the errorTicketId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getErrorTicketId() {
        return errorTicketId;
    }

    /**
     * Sets the value of the errorTicketId property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setErrorTicketId(String value) {
        this.errorTicketId = value;
    }

    /**
     * Gets the value of the innerFault property.
     *
     * @return
     *     possible object is
     *     {@link FaultBase }
     *
     */
    public FaultBase getInnerFault() {
        return innerFault;
    }

    /**
     * Sets the value of the innerFault property.
     *
     * @param value
     *     allowed object is
     *     {@link FaultBase }
     *
     */
    public void setInnerFault(FaultBase value) {
        this.innerFault = value;
    }

}
