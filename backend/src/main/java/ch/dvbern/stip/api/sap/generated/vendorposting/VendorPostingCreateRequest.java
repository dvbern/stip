
package ch.dvbern.stip.api.sap.generated.vendorposting;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für VendorPostingCreate_Request complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="VendorPostingCreate_Request"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SENDER" type="{urn:be.ch:KTBE_ERP_FI:GENERAL}SenderParmsDelivery"/&gt;
 *         &lt;element name="VENDOR_POSTING" maxOccurs="unbounded"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="HEADER"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="DOC_TYPE"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="2"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="COMP_CODE"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="4"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="HEADER_TXT"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="25"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="REF_DOC_NO"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="16"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="PAYMENT_REASON" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="16"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *                             &lt;element name="PSTNG_DATE" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *                             &lt;element name="CURRENCY"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="5"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="IKS_RELEVANT" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="VENDOR"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="VENDOR_NO"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;minLength value="1"/&gt;
 *                                   &lt;maxLength value="10"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="AMT_DOCCUR"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *                                   &lt;totalDigits value="23"/&gt;
 *                                   &lt;fractionDigits value="2"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="ZTERM"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="4"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="ITEM_TEXT" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="50"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="PAYMENT_DETAIL"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="BANK_ID" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="4"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="IBAN" minOccurs="0"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="IBAN"&gt;
 *                                         &lt;simpleType&gt;
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                             &lt;maxLength value="34"/&gt;
 *                                           &lt;/restriction&gt;
 *                                         &lt;/simpleType&gt;
 *                                       &lt;/element&gt;
 *                                       &lt;element name="ACCOUNTHOLDER" minOccurs="0"&gt;
 *                                         &lt;simpleType&gt;
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                             &lt;maxLength value="60"/&gt;
 *                                           &lt;/restriction&gt;
 *                                         &lt;/simpleType&gt;
 *                                       &lt;/element&gt;
 *                                     &lt;/sequence&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="QR_IBAN" minOccurs="0"&gt;
 *                               &lt;complexType&gt;
 *                                 &lt;complexContent&gt;
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                                     &lt;sequence&gt;
 *                                       &lt;element name="QRIBAN"&gt;
 *                                         &lt;simpleType&gt;
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                             &lt;maxLength value="34"/&gt;
 *                                           &lt;/restriction&gt;
 *                                         &lt;/simpleType&gt;
 *                                       &lt;/element&gt;
 *                                       &lt;element name="QRIBAN_ADDL_INFO"&gt;
 *                                         &lt;simpleType&gt;
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                             &lt;maxLength value="140"/&gt;
 *                                           &lt;/restriction&gt;
 *                                         &lt;/simpleType&gt;
 *                                       &lt;/element&gt;
 *                                       &lt;element name="PO_REF_NO"&gt;
 *                                         &lt;simpleType&gt;
 *                                           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                             &lt;maxLength value="27"/&gt;
 *                                           &lt;/restriction&gt;
 *                                         &lt;/simpleType&gt;
 *                                       &lt;/element&gt;
 *                                     &lt;/sequence&gt;
 *                                   &lt;/restriction&gt;
 *                                 &lt;/complexContent&gt;
 *                               &lt;/complexType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="GL_ACCOUNT" maxOccurs="9999"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="ITEMNO_ACC"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *                                   &lt;totalDigits value="10"/&gt;
 *                                   &lt;fractionDigits value="0"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="GL_ACCOUNT"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;minLength value="1"/&gt;
 *                                   &lt;maxLength value="10"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="ORDERID" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="12"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="TAX_CODE" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="2"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="WBS_ELEM" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="24"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="COSTCENTER" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="10"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="ITEM_TEXT" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="50"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="KBLNR" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="10"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="KBLPOS" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
 *                                   &lt;totalDigits value="3"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="REFSETERLK" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="POSITION" maxOccurs="9999"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="ITEMNO_ACC"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *                                   &lt;totalDigits value="10"/&gt;
 *                                   &lt;fractionDigits value="0"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="AMT_DOCCUR"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
 *                                   &lt;totalDigits value="19"/&gt;
 *                                   &lt;fractionDigits value="4"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="ATTACHMENT" maxOccurs="unbounded" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="FILENAME"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="60"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="FILECONTENT" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="ADDITIONAL_DATA" type="{urn:be.ch:KTBE_ERP_FI:GENERAL}AdditionalData" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 *
 */
@XmlRootElement(name = "VendorPostingCreate_Request", namespace = "urn:be.ch:KTBE_ERP_FI:VENDOR_POSTING")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VendorPostingCreate_Request", namespace = "urn:be.ch:KTBE_ERP_FI:VENDOR_POSTING", propOrder = {
    "sender",
    "vendorposting"
})
public class VendorPostingCreateRequest {

    @XmlElement(name = "SENDER", required = true)
    protected SenderParmsDelivery sender;
    @XmlElement(name = "VENDOR_POSTING", required = true)
    protected List<VendorPostingCreateRequest.VENDORPOSTING> vendorposting;

    /**
     * Ruft den Wert der sender-Eigenschaft ab.
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
     * Legt den Wert der sender-Eigenschaft fest.
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
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the vendorposting property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVENDORPOSTING().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VendorPostingCreateRequest.VENDORPOSTING }
     *
     *
     */
    public List<VendorPostingCreateRequest.VENDORPOSTING> getVENDORPOSTING() {
        if (vendorposting == null) {
            vendorposting = new ArrayList<VendorPostingCreateRequest.VENDORPOSTING>();
        }
        return this.vendorposting;
    }


    /**
     * <p>Java-Klasse für anonymous complex type.
     *
     * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
     *
     * <pre>
     * &lt;complexType&gt;
     *   &lt;complexContent&gt;
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *       &lt;sequence&gt;
     *         &lt;element name="HEADER"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="DOC_TYPE"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="2"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="COMP_CODE"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="4"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="HEADER_TXT"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="25"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="REF_DOC_NO"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="16"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="PAYMENT_REASON" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="16"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
     *                   &lt;element name="PSTNG_DATE" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
     *                   &lt;element name="CURRENCY"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="5"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="IKS_RELEVANT" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="VENDOR"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="VENDOR_NO"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;minLength value="1"/&gt;
     *                         &lt;maxLength value="10"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="AMT_DOCCUR"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
     *                         &lt;totalDigits value="23"/&gt;
     *                         &lt;fractionDigits value="2"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="ZTERM"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="4"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="ITEM_TEXT" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="50"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="PAYMENT_DETAIL"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="BANK_ID" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="4"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="IBAN" minOccurs="0"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="IBAN"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                   &lt;maxLength value="34"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="ACCOUNTHOLDER" minOccurs="0"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                   &lt;maxLength value="60"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                           &lt;/sequence&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="QR_IBAN" minOccurs="0"&gt;
     *                     &lt;complexType&gt;
     *                       &lt;complexContent&gt;
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                           &lt;sequence&gt;
     *                             &lt;element name="QRIBAN"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                   &lt;maxLength value="34"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="QRIBAN_ADDL_INFO"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                   &lt;maxLength value="140"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                             &lt;element name="PO_REF_NO"&gt;
     *                               &lt;simpleType&gt;
     *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                                   &lt;maxLength value="27"/&gt;
     *                                 &lt;/restriction&gt;
     *                               &lt;/simpleType&gt;
     *                             &lt;/element&gt;
     *                           &lt;/sequence&gt;
     *                         &lt;/restriction&gt;
     *                       &lt;/complexContent&gt;
     *                     &lt;/complexType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="GL_ACCOUNT" maxOccurs="9999"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="ITEMNO_ACC"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
     *                         &lt;totalDigits value="10"/&gt;
     *                         &lt;fractionDigits value="0"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="GL_ACCOUNT"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;minLength value="1"/&gt;
     *                         &lt;maxLength value="10"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="ORDERID" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="12"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="TAX_CODE" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="2"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="WBS_ELEM" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="24"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="COSTCENTER" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="10"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="ITEM_TEXT" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="50"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="KBLNR" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="10"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="KBLPOS" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
     *                         &lt;totalDigits value="3"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="REFSETERLK" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="POSITION" maxOccurs="9999"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="ITEMNO_ACC"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
     *                         &lt;totalDigits value="10"/&gt;
     *                         &lt;fractionDigits value="0"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="AMT_DOCCUR"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
     *                         &lt;totalDigits value="19"/&gt;
     *                         &lt;fractionDigits value="4"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="ATTACHMENT" maxOccurs="unbounded" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="FILENAME"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="60"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="FILECONTENT" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="ADDITIONAL_DATA" type="{urn:be.ch:KTBE_ERP_FI:GENERAL}AdditionalData" maxOccurs="unbounded" minOccurs="0"/&gt;
     *       &lt;/sequence&gt;
     *     &lt;/restriction&gt;
     *   &lt;/complexContent&gt;
     * &lt;/complexType&gt;
     * </pre>
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
        protected VendorPostingCreateRequest.VENDORPOSTING.HEADER header;
        @XmlElement(name = "VENDOR", required = true)
        protected VendorPostingCreateRequest.VENDORPOSTING.VENDOR vendor;
        @XmlElement(name = "PAYMENT_DETAIL", required = true)
        protected VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL paymentdetail;
        @XmlElement(name = "GL_ACCOUNT", required = true)
        protected List<VendorPostingCreateRequest.VENDORPOSTING.GLACCOUNT> glaccount;
        @XmlElement(name = "POSITION", required = true)
        protected List<VendorPostingCreateRequest.VENDORPOSTING.POSITION> position;
        @XmlElement(name = "ATTACHMENT")
        protected List<VendorPostingCreateRequest.VENDORPOSTING.ATTACHMENT> attachment;
        @XmlElement(name = "ADDITIONAL_DATA")
        protected List<AdditionalData> additionaldata;

        /**
         * Ruft den Wert der header-Eigenschaft ab.
         *
         * @return
         *     possible object is
         *     {@link VendorPostingCreateRequest.VENDORPOSTING.HEADER }
         *
         */
        public VendorPostingCreateRequest.VENDORPOSTING.HEADER getHEADER() {
            return header;
        }

        /**
         * Legt den Wert der header-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link VendorPostingCreateRequest.VENDORPOSTING.HEADER }
         *
         */
        public void setHEADER(VendorPostingCreateRequest.VENDORPOSTING.HEADER value) {
            this.header = value;
        }

        /**
         * Ruft den Wert der vendor-Eigenschaft ab.
         *
         * @return
         *     possible object is
         *     {@link VendorPostingCreateRequest.VENDORPOSTING.VENDOR }
         *
         */
        public VendorPostingCreateRequest.VENDORPOSTING.VENDOR getVENDOR() {
            return vendor;
        }

        /**
         * Legt den Wert der vendor-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link VendorPostingCreateRequest.VENDORPOSTING.VENDOR }
         *
         */
        public void setVENDOR(VendorPostingCreateRequest.VENDORPOSTING.VENDOR value) {
            this.vendor = value;
        }

        /**
         * Ruft den Wert der paymentdetail-Eigenschaft ab.
         *
         * @return
         *     possible object is
         *     {@link VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL }
         *
         */
        public VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL getPAYMENTDETAIL() {
            return paymentdetail;
        }

        /**
         * Legt den Wert der paymentdetail-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL }
         *
         */
        public void setPAYMENTDETAIL(VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL value) {
            this.paymentdetail = value;
        }

        /**
         * Gets the value of the glaccount property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the Jakarta XML Binding object.
         * This is why there is not a <CODE>set</CODE> method for the glaccount property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getGLACCOUNT().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link VendorPostingCreateRequest.VENDORPOSTING.GLACCOUNT }
         *
         *
         */
        public List<VendorPostingCreateRequest.VENDORPOSTING.GLACCOUNT> getGLACCOUNT() {
            if (glaccount == null) {
                glaccount = new ArrayList<VendorPostingCreateRequest.VENDORPOSTING.GLACCOUNT>();
            }
            return this.glaccount;
        }

        /**
         * Gets the value of the position property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the Jakarta XML Binding object.
         * This is why there is not a <CODE>set</CODE> method for the position property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPOSITION().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link VendorPostingCreateRequest.VENDORPOSTING.POSITION }
         *
         *
         */
        public List<VendorPostingCreateRequest.VENDORPOSTING.POSITION> getPOSITION() {
            if (position == null) {
                position = new ArrayList<VendorPostingCreateRequest.VENDORPOSTING.POSITION>();
            }
            return this.position;
        }

        /**
         * Gets the value of the attachment property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the Jakarta XML Binding object.
         * This is why there is not a <CODE>set</CODE> method for the attachment property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getATTACHMENT().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link VendorPostingCreateRequest.VENDORPOSTING.ATTACHMENT }
         *
         *
         */
        public List<VendorPostingCreateRequest.VENDORPOSTING.ATTACHMENT> getATTACHMENT() {
            if (attachment == null) {
                attachment = new ArrayList<VendorPostingCreateRequest.VENDORPOSTING.ATTACHMENT>();
            }
            return this.attachment;
        }

        /**
         * Gets the value of the additionaldata property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the Jakarta XML Binding object.
         * This is why there is not a <CODE>set</CODE> method for the additionaldata property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getADDITIONALDATA().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link AdditionalData }
         *
         *
         */
        public List<AdditionalData> getADDITIONALDATA() {
            if (additionaldata == null) {
                additionaldata = new ArrayList<AdditionalData>();
            }
            return this.additionaldata;
        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         *
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         *
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="FILENAME"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="60"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="FILECONTENT" type="{http://www.w3.org/2001/XMLSchema}base64Binary"/&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         *
         *
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "filename",
            "filecontent"
        })
        public static class ATTACHMENT {

            @XmlElement(name = "FILENAME", required = true)
            protected String filename;
            @XmlElement(name = "FILECONTENT", required = true)
            protected byte[] filecontent;

            /**
             * Ruft den Wert der filename-Eigenschaft ab.
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
             * Legt den Wert der filename-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setFILENAME(String value) {
                this.filename = value;
            }

            /**
             * Ruft den Wert der filecontent-Eigenschaft ab.
             *
             * @return
             *     possible object is
             *     byte[]
             */
            public byte[] getFILECONTENT() {
                return filecontent;
            }

            /**
             * Legt den Wert der filecontent-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     byte[]
             */
            public void setFILECONTENT(byte[] value) {
                this.filecontent = value;
            }

        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         *
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         *
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="ITEMNO_ACC"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
         *               &lt;totalDigits value="10"/&gt;
         *               &lt;fractionDigits value="0"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="GL_ACCOUNT"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;minLength value="1"/&gt;
         *               &lt;maxLength value="10"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="ORDERID" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="12"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="TAX_CODE" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="2"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="WBS_ELEM" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="24"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="COSTCENTER" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="10"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="ITEM_TEXT" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="50"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="KBLNR" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="10"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="KBLPOS" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer"&gt;
         *               &lt;totalDigits value="3"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="REFSETERLK" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
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

            @XmlElement(name = "ITEMNO_ACC", required = true)
            protected BigDecimal itemnoacc;
            @XmlElement(name = "GL_ACCOUNT", required = true)
            protected String glaccount;
            @XmlElement(name = "ORDERID")
            protected String orderid;
            @XmlElement(name = "TAX_CODE")
            protected String taxcode;
            @XmlElement(name = "WBS_ELEM")
            protected String wbselem;
            @XmlElement(name = "COSTCENTER")
            protected String costcenter;
            @XmlElement(name = "ITEM_TEXT")
            protected String itemtext;
            @XmlElement(name = "KBLNR")
            protected String kblnr;
            @XmlElement(name = "KBLPOS")
            protected BigInteger kblpos;
            @XmlElement(name = "REFSETERLK", defaultValue = "0")
            protected Boolean refseterlk;

            /**
             * Ruft den Wert der itemnoacc-Eigenschaft ab.
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
             * Legt den Wert der itemnoacc-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *
             */
            public void setITEMNOACC(BigDecimal value) {
                this.itemnoacc = value;
            }

            /**
             * Ruft den Wert der glaccount-Eigenschaft ab.
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
             * Legt den Wert der glaccount-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setGLACCOUNT(String value) {
                this.glaccount = value;
            }

            /**
             * Ruft den Wert der orderid-Eigenschaft ab.
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
             * Legt den Wert der orderid-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setORDERID(String value) {
                this.orderid = value;
            }

            /**
             * Ruft den Wert der taxcode-Eigenschaft ab.
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
             * Legt den Wert der taxcode-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setTAXCODE(String value) {
                this.taxcode = value;
            }

            /**
             * Ruft den Wert der wbselem-Eigenschaft ab.
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
             * Legt den Wert der wbselem-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setWBSELEM(String value) {
                this.wbselem = value;
            }

            /**
             * Ruft den Wert der costcenter-Eigenschaft ab.
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
             * Legt den Wert der costcenter-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setCOSTCENTER(String value) {
                this.costcenter = value;
            }

            /**
             * Ruft den Wert der itemtext-Eigenschaft ab.
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
             * Legt den Wert der itemtext-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setITEMTEXT(String value) {
                this.itemtext = value;
            }

            /**
             * Ruft den Wert der kblnr-Eigenschaft ab.
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
             * Legt den Wert der kblnr-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setKBLNR(String value) {
                this.kblnr = value;
            }

            /**
             * Ruft den Wert der kblpos-Eigenschaft ab.
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
             * Legt den Wert der kblpos-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link BigInteger }
             *
             */
            public void setKBLPOS(BigInteger value) {
                this.kblpos = value;
            }

            /**
             * Ruft den Wert der refseterlk-Eigenschaft ab.
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
             * Legt den Wert der refseterlk-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link Boolean }
             *
             */
            public void setREFSETERLK(Boolean value) {
                this.refseterlk = value;
            }

        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         *
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         *
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="DOC_TYPE"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="2"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="COMP_CODE"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="4"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="HEADER_TXT"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="25"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="REF_DOC_NO"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="16"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="PAYMENT_REASON" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="16"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="DOC_DATE" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
         *         &lt;element name="PSTNG_DATE" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
         *         &lt;element name="CURRENCY"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="5"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="IKS_RELEVANT" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
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

            @XmlElement(name = "DOC_TYPE", required = true, defaultValue = "YK")
            protected String doctype;
            @XmlElement(name = "COMP_CODE", required = true)
            protected String compcode;
            @XmlElement(name = "HEADER_TXT", required = true)
            protected String headertxt;
            @XmlElement(name = "REF_DOC_NO", required = true)
            protected String refdocno;
            @XmlElement(name = "PAYMENT_REASON")
            protected String paymentreason;
            @XmlElement(name = "DOC_DATE", required = true)
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar docdate;
            @XmlElement(name = "PSTNG_DATE", required = true)
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar pstngdate;
            @XmlElement(name = "CURRENCY", required = true, defaultValue = "CHF")
            protected String currency;
            @XmlElement(name = "IKS_RELEVANT", defaultValue = "true")
            protected Boolean iksrelevant;

            /**
             * Ruft den Wert der doctype-Eigenschaft ab.
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
             * Legt den Wert der doctype-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setDOCTYPE(String value) {
                this.doctype = value;
            }

            /**
             * Ruft den Wert der compcode-Eigenschaft ab.
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
             * Legt den Wert der compcode-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setCOMPCODE(String value) {
                this.compcode = value;
            }

            /**
             * Ruft den Wert der headertxt-Eigenschaft ab.
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
             * Legt den Wert der headertxt-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setHEADERTXT(String value) {
                this.headertxt = value;
            }

            /**
             * Ruft den Wert der refdocno-Eigenschaft ab.
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
             * Legt den Wert der refdocno-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setREFDOCNO(String value) {
                this.refdocno = value;
            }

            /**
             * Ruft den Wert der paymentreason-Eigenschaft ab.
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
             * Legt den Wert der paymentreason-Eigenschaft fest.
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
             * Ruft den Wert der docdate-Eigenschaft ab.
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
             * Legt den Wert der docdate-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *
             */
            public void setDOCDATE(XMLGregorianCalendar value) {
                this.docdate = value;
            }

            /**
             * Ruft den Wert der pstngdate-Eigenschaft ab.
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
             * Legt den Wert der pstngdate-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *
             */
            public void setPSTNGDATE(XMLGregorianCalendar value) {
                this.pstngdate = value;
            }

            /**
             * Ruft den Wert der currency-Eigenschaft ab.
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
             * Legt den Wert der currency-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setCURRENCY(String value) {
                this.currency = value;
            }

            /**
             * Ruft den Wert der iksrelevant-Eigenschaft ab.
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
             * Legt den Wert der iksrelevant-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link Boolean }
             *
             */
            public void setIKSRELEVANT(Boolean value) {
                this.iksrelevant = value;
            }

        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         *
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         *
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="BANK_ID" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="4"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="IBAN" minOccurs="0"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="IBAN"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                         &lt;maxLength value="34"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="ACCOUNTHOLDER" minOccurs="0"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                         &lt;maxLength value="60"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                 &lt;/sequence&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="QR_IBAN" minOccurs="0"&gt;
         *           &lt;complexType&gt;
         *             &lt;complexContent&gt;
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *                 &lt;sequence&gt;
         *                   &lt;element name="QRIBAN"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                         &lt;maxLength value="34"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="QRIBAN_ADDL_INFO"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                         &lt;maxLength value="140"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                   &lt;element name="PO_REF_NO"&gt;
         *                     &lt;simpleType&gt;
         *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *                         &lt;maxLength value="27"/&gt;
         *                       &lt;/restriction&gt;
         *                     &lt;/simpleType&gt;
         *                   &lt;/element&gt;
         *                 &lt;/sequence&gt;
         *               &lt;/restriction&gt;
         *             &lt;/complexContent&gt;
         *           &lt;/complexType&gt;
         *         &lt;/element&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
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

            @XmlElement(name = "BANK_ID")
            protected String bankid;
            @XmlElement(name = "IBAN")
            protected VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL.IBAN iban;
            @XmlElement(name = "QR_IBAN")
            protected VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL.QRIBAN qriban;

            /**
             * Ruft den Wert der bankid-Eigenschaft ab.
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
             * Legt den Wert der bankid-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setBANKID(String value) {
                this.bankid = value;
            }

            /**
             * Ruft den Wert der iban-Eigenschaft ab.
             *
             * @return
             *     possible object is
             *     {@link VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL.IBAN }
             *
             */
            public VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL.IBAN getIBAN() {
                return iban;
            }

            /**
             * Legt den Wert der iban-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL.IBAN }
             *
             */
            public void setIBAN(VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL.IBAN value) {
                this.iban = value;
            }

            /**
             * Ruft den Wert der qriban-Eigenschaft ab.
             *
             * @return
             *     possible object is
             *     {@link VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL.QRIBAN }
             *
             */
            public VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL.QRIBAN getQRIBAN() {
                return qriban;
            }

            /**
             * Legt den Wert der qriban-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL.QRIBAN }
             *
             */
            public void setQRIBAN(VendorPostingCreateRequest.VENDORPOSTING.PAYMENTDETAIL.QRIBAN value) {
                this.qriban = value;
            }


            /**
             * <p>Java-Klasse für anonymous complex type.
             *
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
             *
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;sequence&gt;
             *         &lt;element name="IBAN"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *               &lt;maxLength value="34"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="ACCOUNTHOLDER" minOccurs="0"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *               &lt;maxLength value="60"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *       &lt;/sequence&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
             *
             *
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                "iban",
                "accountholder"
            })
            public static class IBAN {

                @XmlElement(name = "IBAN", required = true)
                protected String iban;
                @XmlElement(name = "ACCOUNTHOLDER")
                protected String accountholder;

                /**
                 * Ruft den Wert der iban-Eigenschaft ab.
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
                 * Legt den Wert der iban-Eigenschaft fest.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *
                 */
                public void setIBAN(String value) {
                    this.iban = value;
                }

                /**
                 * Ruft den Wert der accountholder-Eigenschaft ab.
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
                 * Legt den Wert der accountholder-Eigenschaft fest.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *
                 */
                public void setACCOUNTHOLDER(String value) {
                    this.accountholder = value;
                }

            }


            /**
             * <p>Java-Klasse für anonymous complex type.
             *
             * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
             *
             * <pre>
             * &lt;complexType&gt;
             *   &lt;complexContent&gt;
             *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
             *       &lt;sequence&gt;
             *         &lt;element name="QRIBAN"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *               &lt;maxLength value="34"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="QRIBAN_ADDL_INFO"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *               &lt;maxLength value="140"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *         &lt;element name="PO_REF_NO"&gt;
             *           &lt;simpleType&gt;
             *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
             *               &lt;maxLength value="27"/&gt;
             *             &lt;/restriction&gt;
             *           &lt;/simpleType&gt;
             *         &lt;/element&gt;
             *       &lt;/sequence&gt;
             *     &lt;/restriction&gt;
             *   &lt;/complexContent&gt;
             * &lt;/complexType&gt;
             * </pre>
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

                @XmlElement(name = "QRIBAN", required = true)
                protected String qriban;
                @XmlElement(name = "QRIBAN_ADDL_INFO", required = true)
                protected String qribanaddlinfo;
                @XmlElement(name = "PO_REF_NO", required = true)
                protected String porefno;

                /**
                 * Ruft den Wert der qriban-Eigenschaft ab.
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
                 * Legt den Wert der qriban-Eigenschaft fest.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *
                 */
                public void setQRIBAN(String value) {
                    this.qriban = value;
                }

                /**
                 * Ruft den Wert der qribanaddlinfo-Eigenschaft ab.
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
                 * Legt den Wert der qribanaddlinfo-Eigenschaft fest.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *
                 */
                public void setQRIBANADDLINFO(String value) {
                    this.qribanaddlinfo = value;
                }

                /**
                 * Ruft den Wert der porefno-Eigenschaft ab.
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
                 * Legt den Wert der porefno-Eigenschaft fest.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *
                 */
                public void setPOREFNO(String value) {
                    this.porefno = value;
                }

            }

        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         *
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         *
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="ITEMNO_ACC"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
         *               &lt;totalDigits value="10"/&gt;
         *               &lt;fractionDigits value="0"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="AMT_DOCCUR"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
         *               &lt;totalDigits value="19"/&gt;
         *               &lt;fractionDigits value="4"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
         *
         *
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "itemnoacc",
            "amtdoccur"
        })
        public static class POSITION {

            @XmlElement(name = "ITEMNO_ACC", required = true)
            protected BigDecimal itemnoacc;
            @XmlElement(name = "AMT_DOCCUR", required = true)
            protected BigDecimal amtdoccur;

            /**
             * Ruft den Wert der itemnoacc-Eigenschaft ab.
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
             * Legt den Wert der itemnoacc-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *
             */
            public void setITEMNOACC(BigDecimal value) {
                this.itemnoacc = value;
            }

            /**
             * Ruft den Wert der amtdoccur-Eigenschaft ab.
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
             * Legt den Wert der amtdoccur-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *
             */
            public void setAMTDOCCUR(BigDecimal value) {
                this.amtdoccur = value;
            }

        }


        /**
         * <p>Java-Klasse für anonymous complex type.
         *
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         *
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="VENDOR_NO"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;minLength value="1"/&gt;
         *               &lt;maxLength value="10"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="AMT_DOCCUR"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}decimal"&gt;
         *               &lt;totalDigits value="23"/&gt;
         *               &lt;fractionDigits value="2"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="ZTERM"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="4"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="ITEM_TEXT" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="50"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *       &lt;/sequence&gt;
         *     &lt;/restriction&gt;
         *   &lt;/complexContent&gt;
         * &lt;/complexType&gt;
         * </pre>
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

            @XmlElement(name = "VENDOR_NO", required = true)
            protected String vendorno;
            @XmlElement(name = "AMT_DOCCUR", required = true)
            protected BigDecimal amtdoccur;
            @XmlElement(name = "ZTERM", required = true)
            protected String zterm;
            @XmlElement(name = "ITEM_TEXT")
            protected String itemtext;

            /**
             * Ruft den Wert der vendorno-Eigenschaft ab.
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
             * Legt den Wert der vendorno-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setVENDORNO(String value) {
                this.vendorno = value;
            }

            /**
             * Ruft den Wert der amtdoccur-Eigenschaft ab.
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
             * Legt den Wert der amtdoccur-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *
             */
            public void setAMTDOCCUR(BigDecimal value) {
                this.amtdoccur = value;
            }

            /**
             * Ruft den Wert der zterm-Eigenschaft ab.
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
             * Legt den Wert der zterm-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setZTERM(String value) {
                this.zterm = value;
            }

            /**
             * Ruft den Wert der itemtext-Eigenschaft ab.
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
             * Legt den Wert der itemtext-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setITEMTEXT(String value) {
                this.itemtext = value;
            }

        }

    }

}
