
package ch.dvbern.stip.api.sap.generated.vendor_posting;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import ch.dvbern.stip.api.sap.generated.general.AdditionalData;
import ch.dvbern.stip.api.sap.generated.general.SenderParmsDelivery;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VendorPostingCreate_Request complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="VendorPostingCreate_Request">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="SENDER" type="{urn:be.ch:KTBE_ERP_FI:GENERAL}SenderParmsDelivery"/>
 *         <element name="VENDOR_POSTING" maxOccurs="unbounded">
 *           <complexType>
 *             <complexContent>
 *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 <sequence>
 *                   <element name="HEADER">
 *                     <complexType>
 *                       <complexContent>
 *                         <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           <sequence>
 *                             <element name="DOC_TYPE">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="2"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="COMP_CODE">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="4"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="HEADER_TXT">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="25"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="REF_DOC_NO">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="16"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="PAYMENT_REASON" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="16"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *                             <element name="PSTNG_DATE" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *                             <element name="CURRENCY">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="5"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="IKS_RELEVANT" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *                           </sequence>
 *                         </restriction>
 *                       </complexContent>
 *                     </complexType>
 *                   </element>
 *                   <element name="VENDOR">
 *                     <complexType>
 *                       <complexContent>
 *                         <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           <sequence>
 *                             <element name="VENDOR_NO">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <minLength value="1"/>
 *                                   <maxLength value="10"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="AMT_DOCCUR">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   <totalDigits value="23"/>
 *                                   <fractionDigits value="2"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="ZTERM">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="4"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="ITEM_TEXT" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="50"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                           </sequence>
 *                         </restriction>
 *                       </complexContent>
 *                     </complexType>
 *                   </element>
 *                   <element name="PAYMENT_DETAIL">
 *                     <complexType>
 *                       <complexContent>
 *                         <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           <sequence>
 *                             <element name="BANK_ID" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="4"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="IBAN" minOccurs="0">
 *                               <complexType>
 *                                 <complexContent>
 *                                   <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     <sequence>
 *                                       <element name="IBAN">
 *                                         <simpleType>
 *                                           <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                             <maxLength value="34"/>
 *                                           </restriction>
 *                                         </simpleType>
 *                                       </element>
 *                                       <element name="ACCOUNTHOLDER" minOccurs="0">
 *                                         <simpleType>
 *                                           <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                             <maxLength value="60"/>
 *                                           </restriction>
 *                                         </simpleType>
 *                                       </element>
 *                                     </sequence>
 *                                   </restriction>
 *                                 </complexContent>
 *                               </complexType>
 *                             </element>
 *                             <element name="QR_IBAN" minOccurs="0">
 *                               <complexType>
 *                                 <complexContent>
 *                                   <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     <sequence>
 *                                       <element name="QRIBAN">
 *                                         <simpleType>
 *                                           <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                             <maxLength value="34"/>
 *                                           </restriction>
 *                                         </simpleType>
 *                                       </element>
 *                                       <element name="QRIBAN_ADDL_INFO">
 *                                         <simpleType>
 *                                           <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                             <maxLength value="140"/>
 *                                           </restriction>
 *                                         </simpleType>
 *                                       </element>
 *                                       <element name="PO_REF_NO">
 *                                         <simpleType>
 *                                           <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                             <maxLength value="27"/>
 *                                           </restriction>
 *                                         </simpleType>
 *                                       </element>
 *                                     </sequence>
 *                                   </restriction>
 *                                 </complexContent>
 *                               </complexType>
 *                             </element>
 *                           </sequence>
 *                         </restriction>
 *                       </complexContent>
 *                     </complexType>
 *                   </element>
 *                   <element name="GL_ACCOUNT" maxOccurs="9999">
 *                     <complexType>
 *                       <complexContent>
 *                         <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           <sequence>
 *                             <element name="ITEMNO_ACC">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   <totalDigits value="10"/>
 *                                   <fractionDigits value="0"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="GL_ACCOUNT">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <minLength value="1"/>
 *                                   <maxLength value="10"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="ORDERID" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="12"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="TAX_CODE" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="2"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="WBS_ELEM" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="24"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="COSTCENTER" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="10"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="ITEM_TEXT" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="50"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="KBLNR" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="10"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="KBLPOS" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                                   <totalDigits value="3"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="REFSETERLK" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *                           </sequence>
 *                         </restriction>
 *                       </complexContent>
 *                     </complexType>
 *                   </element>
 *                   <element name="POSITION" maxOccurs="9999">
 *                     <complexType>
 *                       <complexContent>
 *                         <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           <sequence>
 *                             <element name="ITEMNO_ACC">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   <totalDigits value="10"/>
 *                                   <fractionDigits value="0"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="AMT_DOCCUR">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   <totalDigits value="19"/>
 *                                   <fractionDigits value="4"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                           </sequence>
 *                         </restriction>
 *                       </complexContent>
 *                     </complexType>
 *                   </element>
 *                   <element name="ATTACHMENT" maxOccurs="unbounded" minOccurs="0">
 *                     <complexType>
 *                       <complexContent>
 *                         <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           <sequence>
 *                             <element name="FILENAME">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="60"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="FILECONTENT" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
 *                           </sequence>
 *                         </restriction>
 *                       </complexContent>
 *                     </complexType>
 *                   </element>
 *                   <element name="ADDITIONAL_DATA" type="{urn:be.ch:KTBE_ERP_FI:GENERAL}AdditionalData" maxOccurs="unbounded" minOccurs="0"/>
 *                 </sequence>
 *               </restriction>
 *             </complexContent>
 *           </complexType>
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
@XmlType(name = "VendorPostingCreate_Request", propOrder = {
    "sender",
    "vendorposting"
})
public class VendorPostingCreateRequest {

    @XmlElement(name = "SENDER", required = true)
    protected SenderParmsDelivery sender;
    @XmlElement(name = "VENDOR_POSTING", required = true)
    protected List<VENDORPOSTING> vendorposting;

    /**
     * Gets the value of the sender property.
     *
     * @return
     *     possible object is
     *     {@link SenderParmsDelivery }
     *
     */
    public SenderParmsDelivery getSENDER() {
        return sender;
    }

    /**
     * Sets the value of the sender property.
     *
     * @param value
     *     allowed object is
     *     {@link SenderParmsDelivery }
     *
     */
    public void setSENDER(SenderParmsDelivery value) {
        this.sender = value;
    }

    /**
     * Gets the value of the vendorposting property.
     *
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the vendorposting property.</p>
     *
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getVENDORPOSTING().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VENDORPOSTING }
     * </p>
     *
     *
     * @return
     *     The value of the vendorposting property.
     */
    public List<VENDORPOSTING> getVENDORPOSTING() {
        if (vendorposting == null) {
            vendorposting = new ArrayList<>();
        }
        return this.vendorposting;
    }


    /**
     * <p>Java class for anonymous complex type</p>.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.</p>
     *
     * <pre>{@code
     * <complexType>
     *   <complexContent>
     *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       <sequence>
     *         <element name="HEADER">
     *           <complexType>
     *             <complexContent>
     *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 <sequence>
     *                   <element name="DOC_TYPE">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="2"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="COMP_CODE">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="4"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="HEADER_TXT">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="25"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="REF_DOC_NO">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="16"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="PAYMENT_REASON" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="16"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date"/>
     *                   <element name="PSTNG_DATE" type="{http://www.w3.org/2001/XMLSchema}date"/>
     *                   <element name="CURRENCY">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="5"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="IKS_RELEVANT" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
     *                 </sequence>
     *               </restriction>
     *             </complexContent>
     *           </complexType>
     *         </element>
     *         <element name="VENDOR">
     *           <complexType>
     *             <complexContent>
     *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 <sequence>
     *                   <element name="VENDOR_NO">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <minLength value="1"/>
     *                         <maxLength value="10"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="AMT_DOCCUR">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *                         <totalDigits value="23"/>
     *                         <fractionDigits value="2"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="ZTERM">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="4"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="ITEM_TEXT" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="50"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                 </sequence>
     *               </restriction>
     *             </complexContent>
     *           </complexType>
     *         </element>
     *         <element name="PAYMENT_DETAIL">
     *           <complexType>
     *             <complexContent>
     *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 <sequence>
     *                   <element name="BANK_ID" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="4"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="IBAN" minOccurs="0">
     *                     <complexType>
     *                       <complexContent>
     *                         <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           <sequence>
     *                             <element name="IBAN">
     *                               <simpleType>
     *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   <maxLength value="34"/>
     *                                 </restriction>
     *                               </simpleType>
     *                             </element>
     *                             <element name="ACCOUNTHOLDER" minOccurs="0">
     *                               <simpleType>
     *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   <maxLength value="60"/>
     *                                 </restriction>
     *                               </simpleType>
     *                             </element>
     *                           </sequence>
     *                         </restriction>
     *                       </complexContent>
     *                     </complexType>
     *                   </element>
     *                   <element name="QR_IBAN" minOccurs="0">
     *                     <complexType>
     *                       <complexContent>
     *                         <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           <sequence>
     *                             <element name="QRIBAN">
     *                               <simpleType>
     *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   <maxLength value="34"/>
     *                                 </restriction>
     *                               </simpleType>
     *                             </element>
     *                             <element name="QRIBAN_ADDL_INFO">
     *                               <simpleType>
     *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   <maxLength value="140"/>
     *                                 </restriction>
     *                               </simpleType>
     *                             </element>
     *                             <element name="PO_REF_NO">
     *                               <simpleType>
     *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   <maxLength value="27"/>
     *                                 </restriction>
     *                               </simpleType>
     *                             </element>
     *                           </sequence>
     *                         </restriction>
     *                       </complexContent>
     *                     </complexType>
     *                   </element>
     *                 </sequence>
     *               </restriction>
     *             </complexContent>
     *           </complexType>
     *         </element>
     *         <element name="GL_ACCOUNT" maxOccurs="9999">
     *           <complexType>
     *             <complexContent>
     *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 <sequence>
     *                   <element name="ITEMNO_ACC">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *                         <totalDigits value="10"/>
     *                         <fractionDigits value="0"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="GL_ACCOUNT">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <minLength value="1"/>
     *                         <maxLength value="10"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="ORDERID" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="12"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="TAX_CODE" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="2"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="WBS_ELEM" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="24"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="COSTCENTER" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="10"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="ITEM_TEXT" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="50"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="KBLNR" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="10"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="KBLPOS" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *                         <totalDigits value="3"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="REFSETERLK" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
     *                 </sequence>
     *               </restriction>
     *             </complexContent>
     *           </complexType>
     *         </element>
     *         <element name="POSITION" maxOccurs="9999">
     *           <complexType>
     *             <complexContent>
     *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 <sequence>
     *                   <element name="ITEMNO_ACC">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *                         <totalDigits value="10"/>
     *                         <fractionDigits value="0"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="AMT_DOCCUR">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *                         <totalDigits value="19"/>
     *                         <fractionDigits value="4"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                 </sequence>
     *               </restriction>
     *             </complexContent>
     *           </complexType>
     *         </element>
     *         <element name="ATTACHMENT" maxOccurs="unbounded" minOccurs="0">
     *           <complexType>
     *             <complexContent>
     *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 <sequence>
     *                   <element name="FILENAME">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="60"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="FILECONTENT" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
     *                 </sequence>
     *               </restriction>
     *             </complexContent>
     *           </complexType>
     *         </element>
     *         <element name="ADDITIONAL_DATA" type="{urn:be.ch:KTBE_ERP_FI:GENERAL}AdditionalData" maxOccurs="unbounded" minOccurs="0"/>
     *       </sequence>
     *     </restriction>
     *   </complexContent>
     * </complexType>
     * }</pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "header",
        "vendor",
        "paymentdetail",
        "glaccount",
        "position",
        "attachment",
        "additionaldata"
    })
    public static class VENDORPOSTING {

        @XmlElement(name = "HEADER", required = true)
        protected HEADER header;
        @XmlElement(name = "VENDOR", required = true)
        protected VENDOR vendor;
        /**
         * Eine von 4 Zahloptionen muss übermittelt werden
         *
         */
        @XmlElement(name = "PAYMENT_DETAIL", required = true)
        protected PAYMENTDETAIL paymentdetail;
        @XmlElement(name = "GL_ACCOUNT", required = true)
        protected List<GLACCOUNT> glaccount;
        @XmlElement(name = "POSITION", required = true)
        protected List<POSITION> position;
        @XmlElement(name = "ATTACHMENT")
        protected List<ATTACHMENT> attachment;
        @XmlElement(name = "ADDITIONAL_DATA")
        protected List<AdditionalData> additionaldata;

        /**
         * Gets the value of the header property.
         *
         * @return
         *     possible object is
         *     {@link HEADER }
         *
         */
        public HEADER getHEADER() {
            return header;
        }

        /**
         * Sets the value of the header property.
         *
         * @param value
         *     allowed object is
         *     {@link HEADER }
         *
         */
        public void setHEADER(HEADER value) {
            this.header = value;
        }

        /**
         * Gets the value of the vendor property.
         *
         * @return
         *     possible object is
         *     {@link VENDOR }
         *
         */
        public VENDOR getVENDOR() {
            return vendor;
        }

        /**
         * Sets the value of the vendor property.
         *
         * @param value
         *     allowed object is
         *     {@link VENDOR }
         *
         */
        public void setVENDOR(VENDOR value) {
            this.vendor = value;
        }

        /**
         * Eine von 4 Zahloptionen muss übermittelt werden
         *
         * @return
         *     possible object is
         *     {@link PAYMENTDETAIL }
         *
         */
        public PAYMENTDETAIL getPAYMENTDETAIL() {
            return paymentdetail;
        }

        /**
         * Sets the value of the paymentdetail property.
         *
         * @param value
         *     allowed object is
         *     {@link PAYMENTDETAIL }
         *
         * @see #getPAYMENTDETAIL()
         */
        public void setPAYMENTDETAIL(PAYMENTDETAIL value) {
            this.paymentdetail = value;
        }

        /**
         * Gets the value of the glaccount property.
         *
         * <p>This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the glaccount property.</p>
         *
         * <p>
         * For example, to add a new item, do as follows:
         * </p>
         * <pre>
         * getGLACCOUNT().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link GLACCOUNT }
         * </p>
         *
         *
         * @return
         *     The value of the glaccount property.
         */
        public List<GLACCOUNT> getGLACCOUNT() {
            if (glaccount == null) {
                glaccount = new ArrayList<>();
            }
            return this.glaccount;
        }

        /**
         * Gets the value of the position property.
         *
         * <p>This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the position property.</p>
         *
         * <p>
         * For example, to add a new item, do as follows:
         * </p>
         * <pre>
         * getPOSITION().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link POSITION }
         * </p>
         *
         *
         * @return
         *     The value of the position property.
         */
        public List<POSITION> getPOSITION() {
            if (position == null) {
                position = new ArrayList<>();
            }
            return this.position;
        }

        /**
         * Gets the value of the attachment property.
         *
         * <p>This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the attachment property.</p>
         *
         * <p>
         * For example, to add a new item, do as follows:
         * </p>
         * <pre>
         * getATTACHMENT().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ATTACHMENT }
         * </p>
         *
         *
         * @return
         *     The value of the attachment property.
         */
        public List<ATTACHMENT> getATTACHMENT() {
            if (attachment == null) {
                attachment = new ArrayList<>();
            }
            return this.attachment;
        }

        /**
         * Gets the value of the additionaldata property.
         *
         * <p>This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the additionaldata property.</p>
         *
         * <p>
         * For example, to add a new item, do as follows:
         * </p>
         * <pre>
         * getADDITIONALDATA().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link AdditionalData }
         * </p>
         *
         *
         * @return
         *     The value of the additionaldata property.
         */
        public List<AdditionalData> getADDITIONALDATA() {
            if (additionaldata == null) {
                additionaldata = new ArrayList<>();
            }
            return this.additionaldata;
        }


        /**
         * <p>Java class for anonymous complex type</p>.
         *
         * <p>The following schema fragment specifies the expected content contained within this class.</p>
         *
         * <pre>{@code
         * <complexType>
         *   <complexContent>
         *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       <sequence>
         *         <element name="FILENAME">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="60"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="FILECONTENT" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/>
         *       </sequence>
         *     </restriction>
         *   </complexContent>
         * </complexType>
         * }</pre>
         *
         *
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "filename",
            "filecontent"
        })
        public static class ATTACHMENT {

            /**
             * Dateiname
             *
             */
            @XmlElement(name = "FILENAME", required = true)
            protected String filename;
            /**
             * Inhalt der Datei in base64Binary-Format
             *
             */
            @XmlElement(name = "FILECONTENT", required = true)
            protected byte[] filecontent;

            /**
             * Dateiname
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getFILENAME() {
                return filename;
            }

            /**
             * Sets the value of the filename property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getFILENAME()
             */
            public void setFILENAME(String value) {
                this.filename = value;
            }

            /**
             * Inhalt der Datei in base64Binary-Format
             *
             * @return
             *     possible object is
             *     byte[]
             */
            public byte[] getFILECONTENT() {
                return filecontent;
            }

            /**
             * Sets the value of the filecontent property.
             *
             * @param value
             *     allowed object is
             *     byte[]
             * @see #getFILECONTENT()
             */
            public void setFILECONTENT(byte[] value) {
                this.filecontent = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type</p>.
         *
         * <p>The following schema fragment specifies the expected content contained within this class.</p>
         *
         * <pre>{@code
         * <complexType>
         *   <complexContent>
         *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       <sequence>
         *         <element name="ITEMNO_ACC">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
         *               <totalDigits value="10"/>
         *               <fractionDigits value="0"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="GL_ACCOUNT">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <minLength value="1"/>
         *               <maxLength value="10"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="ORDERID" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="12"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="TAX_CODE" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="2"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="WBS_ELEM" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="24"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="COSTCENTER" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="10"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="ITEM_TEXT" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="50"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="KBLNR" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="10"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="KBLPOS" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
         *               <totalDigits value="3"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="REFSETERLK" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
         *       </sequence>
         *     </restriction>
         *   </complexContent>
         * </complexType>
         * }</pre>
         *
         *
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "itemnoacc",
            "glaccount",
            "orderid",
            "taxcode",
            "wbselem",
            "costcenter",
            "itemtext",
            "kblnr",
            "kblpos",
            "refseterlk"
        })
        public static class GLACCOUNT {

            /**
             * Positions-ID (muss übereinstimmend sein)
             *
             */
            @XmlElement(name = "ITEMNO_ACC", required = true)
            protected BigDecimal itemnoacc;
            /**
             * Aufwandkonto im SAP: i.d.R 3*, 5*
             *
             */
            @XmlElement(name = "GL_ACCOUNT", required = true)
            protected String glaccount;
            /**
             * Kontierung: Innenauftrag [Service: SST-012]
             *
             */
            @XmlElement(name = "ORDERID")
            protected String orderid;
            /**
             * Mehrwertsteuerkennzeichen
             *
             */
            @XmlElement(name = "TAX_CODE")
            protected String taxcode;
            /**
             * Kontierung: PSP-Element [Service: SST-012]
             *
             */
            @XmlElement(name = "WBS_ELEM")
            protected String wbselem;
            /**
             * Kontierung: Kostenstelle [Service: SST-012]
             *
             */
            @XmlElement(name = "COSTCENTER")
            protected String costcenter;
            /**
             * Positionstext zur Leistung
             *
             */
            @XmlElement(name = "ITEM_TEXT")
            protected String itemtext;
            /**
             * Belegnummer Mittelvormerkung
             *
             */
            @XmlElement(name = "KBLNR")
            protected String kblnr;
            /**
             * Belegposition Mittelvormerkung
             *
             */
            @XmlElement(name = "KBLPOS")
            protected BigInteger kblpos;
            /**
             * Verbrauchte Mittelvormerkung wird auf erledigt gesetzt
             *
             */
            @XmlElement(name = "REFSETERLK", defaultValue = "0")
            protected Boolean refseterlk;

            /**
             * Positions-ID (muss übereinstimmend sein)
             *
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *
             */
            public BigDecimal getITEMNOACC() {
                return itemnoacc;
            }

            /**
             * Sets the value of the itemnoacc property.
             *
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *
             * @see #getITEMNOACC()
             */
            public void setITEMNOACC(BigDecimal value) {
                this.itemnoacc = value;
            }

            /**
             * Aufwandkonto im SAP: i.d.R 3*, 5*
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getGLACCOUNT() {
                return glaccount;
            }

            /**
             * Sets the value of the glaccount property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getGLACCOUNT()
             */
            public void setGLACCOUNT(String value) {
                this.glaccount = value;
            }

            /**
             * Kontierung: Innenauftrag [Service: SST-012]
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getORDERID() {
                return orderid;
            }

            /**
             * Sets the value of the orderid property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getORDERID()
             */
            public void setORDERID(String value) {
                this.orderid = value;
            }

            /**
             * Mehrwertsteuerkennzeichen
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getTAXCODE() {
                return taxcode;
            }

            /**
             * Sets the value of the taxcode property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getTAXCODE()
             */
            public void setTAXCODE(String value) {
                this.taxcode = value;
            }

            /**
             * Kontierung: PSP-Element [Service: SST-012]
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getWBSELEM() {
                return wbselem;
            }

            /**
             * Sets the value of the wbselem property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getWBSELEM()
             */
            public void setWBSELEM(String value) {
                this.wbselem = value;
            }

            /**
             * Kontierung: Kostenstelle [Service: SST-012]
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getCOSTCENTER() {
                return costcenter;
            }

            /**
             * Sets the value of the costcenter property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getCOSTCENTER()
             */
            public void setCOSTCENTER(String value) {
                this.costcenter = value;
            }

            /**
             * Positionstext zur Leistung
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getITEMTEXT() {
                return itemtext;
            }

            /**
             * Sets the value of the itemtext property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getITEMTEXT()
             */
            public void setITEMTEXT(String value) {
                this.itemtext = value;
            }

            /**
             * Belegnummer Mittelvormerkung
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getKBLNR() {
                return kblnr;
            }

            /**
             * Sets the value of the kblnr property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getKBLNR()
             */
            public void setKBLNR(String value) {
                this.kblnr = value;
            }

            /**
             * Belegposition Mittelvormerkung
             *
             * @return
             *     possible object is
             *     {@link BigInteger }
             *
             */
            public BigInteger getKBLPOS() {
                return kblpos;
            }

            /**
             * Sets the value of the kblpos property.
             *
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *
             * @see #getKBLPOS()
             */
            public void setKBLPOS(BigInteger value) {
                this.kblpos = value;
            }

            /**
             * Verbrauchte Mittelvormerkung wird auf erledigt gesetzt
             *
             * @return
             *     possible object is
             *     {@link Boolean }
             *
             */
            public Boolean isREFSETERLK() {
                return refseterlk;
            }

            /**
             * Sets the value of the refseterlk property.
             *
             * @param value
             *     allowed object is
             *     {@link Boolean }
             *
             * @see #isREFSETERLK()
             */
            public void setREFSETERLK(Boolean value) {
                this.refseterlk = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type</p>.
         *
         * <p>The following schema fragment specifies the expected content contained within this class.</p>
         *
         * <pre>{@code
         * <complexType>
         *   <complexContent>
         *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       <sequence>
         *         <element name="DOC_TYPE">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="2"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="COMP_CODE">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="4"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="HEADER_TXT">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="25"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="REF_DOC_NO">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="16"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="PAYMENT_REASON" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="16"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date"/>
         *         <element name="PSTNG_DATE" type="{http://www.w3.org/2001/XMLSchema}date"/>
         *         <element name="CURRENCY">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="5"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="IKS_RELEVANT" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
         *       </sequence>
         *     </restriction>
         *   </complexContent>
         * </complexType>
         * }</pre>
         *
         *
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "doctype",
            "compcode",
            "headertxt",
            "refdocno",
            "paymentreason",
            "docdate",
            "pstngdate",
            "currency",
            "iksrelevant"
        })
        public static class HEADER {

            /**
             * Belegart. YK = Kreditorenrechnung (über Schnittstelle)
             *
             */
            @XmlElement(name = "DOC_TYPE", required = true, defaultValue = "YK")
            protected String doctype;
            /**
             * Buchungskreis
             *
             */
            @XmlElement(name = "COMP_CODE", required = true)
            protected String compcode;
            /**
             * Kopftext, steht für wen die Leistung sein soll
             *
             */
            @XmlElement(name = "HEADER_TXT", required = true)
            protected String headertxt;
            /**
             * Referenz-Belegnummer
             *
             */
            @XmlElement(name = "REF_DOC_NO", required = true)
            protected String refdocno;
            @XmlElement(name = "PAYMENT_REASON")
            protected String paymentreason;
            /**
             * Belegdatum im Beleg
             *
             */
            @XmlElement(name = "DOC_DATE", required = true)
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar docdate;
            /**
             * Buchungsdatum im Beleg
             *
             */
            @XmlElement(name = "PSTNG_DATE", required = true)
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar pstngdate;
            /**
             * Währung: CHF/EUR/USD etc.
             *
             */
            @XmlElement(name = "CURRENCY", required = true, defaultValue = "CHF")
            protected String currency;
            /**
             * Soll der Beleg durch Genehmigungs-WorkFlow laufen?
             *
             */
            @XmlElement(name = "IKS_RELEVANT", defaultValue = "true")
            protected Boolean iksrelevant;

            /**
             * Belegart. YK = Kreditorenrechnung (über Schnittstelle)
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getDOCTYPE() {
                return doctype;
            }

            /**
             * Sets the value of the doctype property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getDOCTYPE()
             */
            public void setDOCTYPE(String value) {
                this.doctype = value;
            }

            /**
             * Buchungskreis
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getCOMPCODE() {
                return compcode;
            }

            /**
             * Sets the value of the compcode property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getCOMPCODE()
             */
            public void setCOMPCODE(String value) {
                this.compcode = value;
            }

            /**
             * Kopftext, steht für wen die Leistung sein soll
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getHEADERTXT() {
                return headertxt;
            }

            /**
             * Sets the value of the headertxt property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getHEADERTXT()
             */
            public void setHEADERTXT(String value) {
                this.headertxt = value;
            }

            /**
             * Referenz-Belegnummer
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getREFDOCNO() {
                return refdocno;
            }

            /**
             * Sets the value of the refdocno property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getREFDOCNO()
             */
            public void setREFDOCNO(String value) {
                this.refdocno = value;
            }

            /**
             * Gets the value of the paymentreason property.
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getPAYMENTREASON() {
                return paymentreason;
            }

            /**
             * Sets the value of the paymentreason property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setPAYMENTREASON(String value) {
                this.paymentreason = value;
            }

            /**
             * Belegdatum im Beleg
             *
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *
             */
            public XMLGregorianCalendar getDOCDATE() {
                return docdate;
            }

            /**
             * Sets the value of the docdate property.
             *
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *
             * @see #getDOCDATE()
             */
            public void setDOCDATE(XMLGregorianCalendar value) {
                this.docdate = value;
            }

            /**
             * Buchungsdatum im Beleg
             *
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *
             */
            public XMLGregorianCalendar getPSTNGDATE() {
                return pstngdate;
            }

            /**
             * Sets the value of the pstngdate property.
             *
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *
             * @see #getPSTNGDATE()
             */
            public void setPSTNGDATE(XMLGregorianCalendar value) {
                this.pstngdate = value;
            }

            /**
             * Währung: CHF/EUR/USD etc.
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getCURRENCY() {
                return currency;
            }

            /**
             * Sets the value of the currency property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getCURRENCY()
             */
            public void setCURRENCY(String value) {
                this.currency = value;
            }

            /**
             * Soll der Beleg durch Genehmigungs-WorkFlow laufen?
             *
             * @return
             *     possible object is
             *     {@link Boolean }
             *
             */
            public Boolean isIKSRELEVANT() {
                return iksrelevant;
            }

            /**
             * Sets the value of the iksrelevant property.
             *
             * @param value
             *     allowed object is
             *     {@link Boolean }
             *
             * @see #isIKSRELEVANT()
             */
            public void setIKSRELEVANT(Boolean value) {
                this.iksrelevant = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type</p>.
         *
         * <p>The following schema fragment specifies the expected content contained within this class.</p>
         *
         * <pre>{@code
         * <complexType>
         *   <complexContent>
         *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       <sequence>
         *         <element name="BANK_ID" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="4"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="IBAN" minOccurs="0">
         *           <complexType>
         *             <complexContent>
         *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 <sequence>
         *                   <element name="IBAN">
         *                     <simpleType>
         *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         <maxLength value="34"/>
         *                       </restriction>
         *                     </simpleType>
         *                   </element>
         *                   <element name="ACCOUNTHOLDER" minOccurs="0">
         *                     <simpleType>
         *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         <maxLength value="60"/>
         *                       </restriction>
         *                     </simpleType>
         *                   </element>
         *                 </sequence>
         *               </restriction>
         *             </complexContent>
         *           </complexType>
         *         </element>
         *         <element name="QR_IBAN" minOccurs="0">
         *           <complexType>
         *             <complexContent>
         *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 <sequence>
         *                   <element name="QRIBAN">
         *                     <simpleType>
         *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         <maxLength value="34"/>
         *                       </restriction>
         *                     </simpleType>
         *                   </element>
         *                   <element name="QRIBAN_ADDL_INFO">
         *                     <simpleType>
         *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         <maxLength value="140"/>
         *                       </restriction>
         *                     </simpleType>
         *                   </element>
         *                   <element name="PO_REF_NO">
         *                     <simpleType>
         *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         <maxLength value="27"/>
         *                       </restriction>
         *                     </simpleType>
         *                   </element>
         *                 </sequence>
         *               </restriction>
         *             </complexContent>
         *           </complexType>
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
        @XmlType(name = "", propOrder = {
            "bankid",
            "iban",
            "qriban"
        })
        public static class PAYMENTDETAIL {

            /**
             * Zahlungverbindungs-ID, an das die Auszahlung erfolgt [Service: SST-075]
             *
             */
            @XmlElement(name = "BANK_ID")
            protected String bankid;
            @XmlElement(name = "IBAN")
            protected IBAN iban;
            @XmlElement(name = "QR_IBAN")
            protected QRIBAN qriban;

            /**
             * Zahlungverbindungs-ID, an das die Auszahlung erfolgt [Service: SST-075]
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getBANKID() {
                return bankid;
            }

            /**
             * Sets the value of the bankid property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getBANKID()
             */
            public void setBANKID(String value) {
                this.bankid = value;
            }

            /**
             * Gets the value of the iban property.
             *
             * @return
             *     possible object is
             *     {@link IBAN }
             *
             */
            public IBAN getIBAN() {
                return iban;
            }

            /**
             * Sets the value of the iban property.
             *
             * @param value
             *     allowed object is
             *     {@link IBAN }
             *
             */
            public void setIBAN(IBAN value) {
                this.iban = value;
            }

            /**
             * Gets the value of the qriban property.
             *
             * @return
             *     possible object is
             *     {@link QRIBAN }
             *
             */
            public QRIBAN getQRIBAN() {
                return qriban;
            }

            /**
             * Sets the value of the qriban property.
             *
             * @param value
             *     allowed object is
             *     {@link QRIBAN }
             *
             */
            public void setQRIBAN(QRIBAN value) {
                this.qriban = value;
            }


            /**
             * <p>Java class for anonymous complex type</p>.
             *
             * <p>The following schema fragment specifies the expected content contained within this class.</p>
             *
             * <pre>{@code
             * <complexType>
             *   <complexContent>
             *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       <sequence>
             *         <element name="IBAN">
             *           <simpleType>
             *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               <maxLength value="34"/>
             *             </restriction>
             *           </simpleType>
             *         </element>
             *         <element name="ACCOUNTHOLDER" minOccurs="0">
             *           <simpleType>
             *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               <maxLength value="60"/>
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
            @XmlType(name = "", propOrder = {
                "iban",
                "accountholder"
            })
            public static class IBAN {

                /**
                 * IBAN
                 *
                 */
                @XmlElement(name = "IBAN", required = true)
                protected String iban;
                /**
                 * Abweichender Name der Zahlungsverbindung
                 *
                 */
                @XmlElement(name = "ACCOUNTHOLDER")
                protected String accountholder;

                /**
                 * IBAN
                 *
                 * @return
                 *     possible object is
                 *     {@link String }
                 *
                 */
                public String getIBAN() {
                    return iban;
                }

                /**
                 * Sets the value of the iban property.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *
                 * @see #getIBAN()
                 */
                public void setIBAN(String value) {
                    this.iban = value;
                }

                /**
                 * Abweichender Name der Zahlungsverbindung
                 *
                 * @return
                 *     possible object is
                 *     {@link String }
                 *
                 */
                public String getACCOUNTHOLDER() {
                    return accountholder;
                }

                /**
                 * Sets the value of the accountholder property.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *
                 * @see #getACCOUNTHOLDER()
                 */
                public void setACCOUNTHOLDER(String value) {
                    this.accountholder = value;
                }

            }


            /**
             * <p>Java class for anonymous complex type</p>.
             *
             * <p>The following schema fragment specifies the expected content contained within this class.</p>
             *
             * <pre>{@code
             * <complexType>
             *   <complexContent>
             *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
             *       <sequence>
             *         <element name="QRIBAN">
             *           <simpleType>
             *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               <maxLength value="34"/>
             *             </restriction>
             *           </simpleType>
             *         </element>
             *         <element name="QRIBAN_ADDL_INFO">
             *           <simpleType>
             *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               <maxLength value="140"/>
             *             </restriction>
             *           </simpleType>
             *         </element>
             *         <element name="PO_REF_NO">
             *           <simpleType>
             *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               <maxLength value="27"/>
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
            @XmlType(name = "", propOrder = {
                "qriban",
                "qribanaddlinfo",
                "porefno"
            })
            public static class QRIBAN {

                /**
                 * QR-IBAN International Bank Account Number
                 *
                 */
                @XmlElement(name = "QRIBAN", required = true)
                protected String qriban;
                /**
                 * QR-Rechnung: Zusätzliche Informationen
                 *
                 */
                @XmlElement(name = "QRIBAN_ADDL_INFO", required = true)
                protected String qribanaddlinfo;
                /**
                 * ESR/QR-Referenznummer
                 *
                 */
                @XmlElement(name = "PO_REF_NO", required = true)
                protected String porefno;

                /**
                 * QR-IBAN International Bank Account Number
                 *
                 * @return
                 *     possible object is
                 *     {@link String }
                 *
                 */
                public String getQRIBAN() {
                    return qriban;
                }

                /**
                 * Sets the value of the qriban property.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *
                 * @see #getQRIBAN()
                 */
                public void setQRIBAN(String value) {
                    this.qriban = value;
                }

                /**
                 * QR-Rechnung: Zusätzliche Informationen
                 *
                 * @return
                 *     possible object is
                 *     {@link String }
                 *
                 */
                public String getQRIBANADDLINFO() {
                    return qribanaddlinfo;
                }

                /**
                 * Sets the value of the qribanaddlinfo property.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *
                 * @see #getQRIBANADDLINFO()
                 */
                public void setQRIBANADDLINFO(String value) {
                    this.qribanaddlinfo = value;
                }

                /**
                 * ESR/QR-Referenznummer
                 *
                 * @return
                 *     possible object is
                 *     {@link String }
                 *
                 */
                public String getPOREFNO() {
                    return porefno;
                }

                /**
                 * Sets the value of the porefno property.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *
                 * @see #getPOREFNO()
                 */
                public void setPOREFNO(String value) {
                    this.porefno = value;
                }

            }

        }


        /**
         * <p>Java class for anonymous complex type</p>.
         *
         * <p>The following schema fragment specifies the expected content contained within this class.</p>
         *
         * <pre>{@code
         * <complexType>
         *   <complexContent>
         *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       <sequence>
         *         <element name="ITEMNO_ACC">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
         *               <totalDigits value="10"/>
         *               <fractionDigits value="0"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="AMT_DOCCUR">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
         *               <totalDigits value="19"/>
         *               <fractionDigits value="4"/>
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
        @XmlType(name = "", propOrder = {
            "itemnoacc",
            "amtdoccur"
        })
        public static class POSITION {

            /**
             * Positions-ID (muss übereinstimmend sein)
             *
             */
            @XmlElement(name = "ITEMNO_ACC", required = true)
            protected BigDecimal itemnoacc;
            /**
             * Betrag: muss in Summe den Kreditorenbetrag ausgleichen, kann aber auf verschiedene Kostenträger gebucht werden. Dezimalstelle als Punkt übertragen, bis zu 4 Nachkommastellen möglich
             *
             */
            @XmlElement(name = "AMT_DOCCUR", required = true)
            protected BigDecimal amtdoccur;

            /**
             * Positions-ID (muss übereinstimmend sein)
             *
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *
             */
            public BigDecimal getITEMNOACC() {
                return itemnoacc;
            }

            /**
             * Sets the value of the itemnoacc property.
             *
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *
             * @see #getITEMNOACC()
             */
            public void setITEMNOACC(BigDecimal value) {
                this.itemnoacc = value;
            }

            /**
             * Betrag: muss in Summe den Kreditorenbetrag ausgleichen, kann aber auf verschiedene Kostenträger gebucht werden. Dezimalstelle als Punkt übertragen, bis zu 4 Nachkommastellen möglich
             *
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *
             */
            public BigDecimal getAMTDOCCUR() {
                return amtdoccur;
            }

            /**
             * Sets the value of the amtdoccur property.
             *
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *
             * @see #getAMTDOCCUR()
             */
            public void setAMTDOCCUR(BigDecimal value) {
                this.amtdoccur = value;
            }

        }


        /**
         * <p>Java class for anonymous complex type</p>.
         *
         * <p>The following schema fragment specifies the expected content contained within this class.</p>
         *
         * <pre>{@code
         * <complexType>
         *   <complexContent>
         *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       <sequence>
         *         <element name="VENDOR_NO">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <minLength value="1"/>
         *               <maxLength value="10"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="AMT_DOCCUR">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
         *               <totalDigits value="23"/>
         *               <fractionDigits value="2"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="ZTERM">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="4"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="ITEM_TEXT" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="50"/>
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
        @XmlType(name = "", propOrder = {
            "vendorno",
            "amtdoccur",
            "zterm",
            "itemtext"
        })
        public static class VENDOR {

            /**
             * SAP Kreditorennummer [Service: SST-074]
             *
             */
            @XmlElement(name = "VENDOR_NO", required = true)
            protected String vendorno;
            /**
             * Betrag, der an den Kreditor ausgezahlt wird; Dezimalzeichen ist Punkt (kein Komma); bis zu 2 Nachkommastellen sind erlaubt. Nur positive Werte
             *
             */
            @XmlElement(name = "AMT_DOCCUR", required = true)
            protected BigDecimal amtdoccur;
            /**
             * Zahlungsbedingungen, müssen vorher im SAP sein, sonst wird ein Standardwert genommen [Service: SST-091]
             *
             */
            @XmlElement(name = "ZTERM", required = true)
            protected String zterm;
            /**
             * Positionstext zur Leistung
             *
             */
            @XmlElement(name = "ITEM_TEXT")
            protected String itemtext;

            /**
             * SAP Kreditorennummer [Service: SST-074]
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getVENDORNO() {
                return vendorno;
            }

            /**
             * Sets the value of the vendorno property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getVENDORNO()
             */
            public void setVENDORNO(String value) {
                this.vendorno = value;
            }

            /**
             * Betrag, der an den Kreditor ausgezahlt wird; Dezimalzeichen ist Punkt (kein Komma); bis zu 2 Nachkommastellen sind erlaubt. Nur positive Werte
             *
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *
             */
            public BigDecimal getAMTDOCCUR() {
                return amtdoccur;
            }

            /**
             * Sets the value of the amtdoccur property.
             *
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *
             * @see #getAMTDOCCUR()
             */
            public void setAMTDOCCUR(BigDecimal value) {
                this.amtdoccur = value;
            }

            /**
             * Zahlungsbedingungen, müssen vorher im SAP sein, sonst wird ein Standardwert genommen [Service: SST-091]
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getZTERM() {
                return zterm;
            }

            /**
             * Sets the value of the zterm property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getZTERM()
             */
            public void setZTERM(String value) {
                this.zterm = value;
            }

            /**
             * Positionstext zur Leistung
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getITEMTEXT() {
                return itemtext;
            }

            /**
             * Sets the value of the itemtext property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getITEMTEXT()
             */
            public void setITEMTEXT(String value) {
                this.itemtext = value;
            }

        }

    }

}
