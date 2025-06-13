
package ch.dvbern.stip.api.sap.generated.vendor_posting;

import java.util.ArrayList;
import java.util.List;

import ch.dvbern.stip.api.sap.generated.general.ReturnCode;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VendorPostingCreate_Response complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="VendorPostingCreate_Response">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="RETURN_CODE" type="{urn:be.ch:KTBE_ERP_FI:GENERAL}ReturnCode" maxOccurs="unbounded"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VendorPostingCreate_Response", propOrder = {
    "returncode"
})
public class VendorPostingCreateResponse {

    @XmlElement(name = "RETURN_CODE", required = true)
    protected List<ReturnCode> returncode;

    /**
     * Gets the value of the returncode property.
     *
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the returncode property.</p>
     *
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getRETURNCODE().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReturnCode }
     * </p>
     *
     *
     * @return
     *     The value of the returncode property.
     */
    public List<ReturnCode> getRETURNCODE() {
        if (returncode == null) {
            returncode = new ArrayList<>();
        }
        return this.returncode;
    }

}
