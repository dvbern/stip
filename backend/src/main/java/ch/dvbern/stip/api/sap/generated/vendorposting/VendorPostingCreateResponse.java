
package ch.dvbern.stip.api.sap.generated.vendorposting;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für VendorPostingCreate_Response complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="VendorPostingCreate_Response"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="RETURN_CODE" type="{urn:be.ch:KTBE_ERP_FI:GENERAL}ReturnCode" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlRootElement(name = "VendorPostingCreate_Response", namespace = "urn:be.ch:KTBE_ERP_FI:VENDOR_POSTING")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VendorPostingCreate_Response", namespace = "urn:be.ch:KTBE_ERP_FI:VENDOR_POSTING", propOrder = {
    "returncode"
})
public class VendorPostingCreateResponse {

    @XmlElement(name = "RETURN_CODE", required = true)
    protected List<ReturnCode> returncode;

    /**
     * Gets the value of the returncode property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the returncode property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRETURNCODE().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReturnCode }
     *
     *
     */
    public List<ReturnCode> getRETURNCODE() {
        if (returncode == null) {
            returncode = new ArrayList<ReturnCode>();
        }
        return this.returncode;
    }

}
