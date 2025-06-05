
package ch.dvbern.stip.api.sap.generated.business_partner;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BusinessPartnerCreate_Request complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="BusinessPartnerCreate_Request">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="SENDER" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}SenderParmsDelivery"/>
 *         <element name="BUSINESS_PARTNER">
 *           <complexType>
 *             <complexContent>
 *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 <sequence>
 *                   <element name="HEADER">
 *                     <complexType>
 *                       <complexContent>
 *                         <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           <sequence>
 *                             <element name="PARTN_CAT">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <enumeration value="1"/>
 *                                   <enumeration value="2"/>
 *                                   <maxLength value="10"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="PARTN_TYP" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="4"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="PARTN_GRP" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <enumeration value="ZBPE"/>
 *                                   <maxLength value="4"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="PARTN_TAXKD" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="1"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                           </sequence>
 *                         </restriction>
 *                       </complexContent>
 *                     </complexType>
 *                   </element>
 *                   <element name="ID_KEYS">
 *                     <complexType>
 *                       <complexContent>
 *                         <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           <sequence>
 *                             <element name="EXT_ID">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="20"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="AHV_NR" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="13"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="UID_NR" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="12"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="ZPV_NR" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="10"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                           </sequence>
 *                         </restriction>
 *                       </complexContent>
 *                     </complexType>
 *                   </element>
 *                   <element name="PERS_DATA" minOccurs="0">
 *                     <complexType>
 *                       <complexContent>
 *                         <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           <sequence>
 *                             <element name="FIRSTNAME">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="40"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="LASTNAME">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="40"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="CORRESPONDLANGUAGEISO">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="2"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="BIRTHNAME" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="40"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="MIDDLENAME" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="40"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="SECONDNAME" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="40"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="TITLE_ACA1" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <enumeration value="0001"/>
 *                                   <enumeration value="0002"/>
 *                                   <enumeration value="0003"/>
 *                                   <enumeration value="0004"/>
 *                                   <enumeration value="0005"/>
 *                                   <enumeration value="0006"/>
 *                                   <maxLength value="4"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="TITLE_ACA2" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="4"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="TITLE_SPPL" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="4"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="PREFIX1" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="4"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="PREFIX2" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="4"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="NICKNAME" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="40"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="INITIALS" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="10"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="NAMEFORMAT" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="2"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="NAMCOUNTRY" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="3"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="NAMCOUNTRYISO" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="2"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="SEX" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <enumeration value="1"/>
 *                                   <enumeration value="2"/>
 *                                   <maxLength value="1"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="BIRTHPLACE" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="40"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="BIRTHDATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *                             <element name="DEATHDATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *                             <element name="MARITALSTATUS" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="1"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="CORRESPONDLANGUAGE" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="1"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="FULLNAME" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="80"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="EMPLOYER" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="35"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="OCCUPATION" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="4"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="NATIONALITY" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="3"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="NATIONALITYISO" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="2"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="COUNTRYORIGIN" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="3"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="BIRTHDT_STATUS" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="1"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                           </sequence>
 *                         </restriction>
 *                       </complexContent>
 *                     </complexType>
 *                   </element>
 *                   <element name="ORG_DATA" minOccurs="0">
 *                     <complexType>
 *                       <complexContent>
 *                         <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           <sequence>
 *                             <element name="NAME1">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="40"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="NAME2" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="40"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="NAME3" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="40"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="NAME4" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="40"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="LANGU_ISO">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="2"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="LEGALFORM" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="2"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="INDUSTRYSECTOR" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="10"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="FOUNDATIONDATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *                             <element name="LIQUIDATIONDATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *                             <element name="LOC_NO_1" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="7"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="LOC_NO_2" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="5"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="CHK_DIGIT" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="1"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="LEGALORG" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="2"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                           </sequence>
 *                         </restriction>
 *                       </complexContent>
 *                     </complexType>
 *                   </element>
 *                   <element name="ADDRESS" maxOccurs="unbounded">
 *                     <complexType>
 *                       <complexContent>
 *                         <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           <sequence>
 *                             <element name="ADR_KIND">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <minLength value="1"/>
 *                                   <maxLength value="10"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="C_O_NAME" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="40"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="CITY">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <minLength value="1"/>
 *                                   <maxLength value="40"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="DISTRICT" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="40"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="POSTL_COD1">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <minLength value="1"/>
 *                                   <maxLength value="10"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="PO_BOX" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="10"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="STREET">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <minLength value="1"/>
 *                                   <maxLength value="40"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="HOUSE_NO" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="10"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="COUNTRY">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="3"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="POSTL_COD2" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="10"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                           </sequence>
 *                         </restriction>
 *                       </complexContent>
 *                     </complexType>
 *                   </element>
 *                   <element name="COMMUNICATION" maxOccurs="unbounded" minOccurs="0">
 *                     <complexType>
 *                       <complexContent>
 *                         <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           <sequence>
 *                             <element name="EMAIL" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="241"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="TELEPHONE" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="30"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="MOBILE" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="30"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="FAX" minOccurs="0">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="30"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                           </sequence>
 *                         </restriction>
 *                       </complexContent>
 *                     </complexType>
 *                   </element>
 *                   <element name="PAYMENT_DETAIL" maxOccurs="unbounded" minOccurs="0">
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
 *                             <element name="BANKDETAILVALIDFROM" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *                             <element name="BANKDETAILVALIDTO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
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
 *                   <element name="ADDITIONAL_DATA" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}AdditionalData" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "BusinessPartnerCreate_Request", propOrder = {
    "sender",
    "businesspartner"
})
public class BusinessPartnerCreateRequest {

    @XmlElement(name = "SENDER", required = true)
    protected SenderParmsDelivery sender;
    @XmlElement(name = "BUSINESS_PARTNER", required = true)
    protected BUSINESSPARTNER businesspartner;

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
     * Gets the value of the businesspartner property.
     *
     * @return
     *     possible object is
     *     {@link BUSINESSPARTNER }
     *
     */
    public BUSINESSPARTNER getBUSINESSPARTNER() {
        return businesspartner;
    }

    /**
     * Sets the value of the businesspartner property.
     *
     * @param value
     *     allowed object is
     *     {@link BUSINESSPARTNER }
     *
     */
    public void setBUSINESSPARTNER(BUSINESSPARTNER value) {
        this.businesspartner = value;
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
     *                   <element name="PARTN_CAT">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <enumeration value="1"/>
     *                         <enumeration value="2"/>
     *                         <maxLength value="10"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="PARTN_TYP" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="4"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="PARTN_GRP" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <enumeration value="ZBPE"/>
     *                         <maxLength value="4"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="PARTN_TAXKD" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="1"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                 </sequence>
     *               </restriction>
     *             </complexContent>
     *           </complexType>
     *         </element>
     *         <element name="ID_KEYS">
     *           <complexType>
     *             <complexContent>
     *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 <sequence>
     *                   <element name="EXT_ID">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="20"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="AHV_NR" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="13"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="UID_NR" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="12"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="ZPV_NR" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="10"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                 </sequence>
     *               </restriction>
     *             </complexContent>
     *           </complexType>
     *         </element>
     *         <element name="PERS_DATA" minOccurs="0">
     *           <complexType>
     *             <complexContent>
     *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 <sequence>
     *                   <element name="FIRSTNAME">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="40"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="LASTNAME">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="40"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="CORRESPONDLANGUAGEISO">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="2"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="BIRTHNAME" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="40"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="MIDDLENAME" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="40"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="SECONDNAME" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="40"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="TITLE_ACA1" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <enumeration value="0001"/>
     *                         <enumeration value="0002"/>
     *                         <enumeration value="0003"/>
     *                         <enumeration value="0004"/>
     *                         <enumeration value="0005"/>
     *                         <enumeration value="0006"/>
     *                         <maxLength value="4"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="TITLE_ACA2" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="4"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="TITLE_SPPL" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="4"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="PREFIX1" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="4"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="PREFIX2" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="4"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="NICKNAME" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="40"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="INITIALS" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="10"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="NAMEFORMAT" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="2"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="NAMCOUNTRY" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="3"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="NAMCOUNTRYISO" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="2"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="SEX" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <enumeration value="1"/>
     *                         <enumeration value="2"/>
     *                         <maxLength value="1"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="BIRTHPLACE" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="40"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="BIRTHDATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
     *                   <element name="DEATHDATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
     *                   <element name="MARITALSTATUS" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="1"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="CORRESPONDLANGUAGE" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="1"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="FULLNAME" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="80"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="EMPLOYER" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="35"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="OCCUPATION" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="4"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="NATIONALITY" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="3"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="NATIONALITYISO" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="2"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="COUNTRYORIGIN" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="3"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="BIRTHDT_STATUS" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="1"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                 </sequence>
     *               </restriction>
     *             </complexContent>
     *           </complexType>
     *         </element>
     *         <element name="ORG_DATA" minOccurs="0">
     *           <complexType>
     *             <complexContent>
     *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 <sequence>
     *                   <element name="NAME1">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="40"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="NAME2" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="40"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="NAME3" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="40"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="NAME4" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="40"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="LANGU_ISO">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="2"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="LEGALFORM" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="2"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="INDUSTRYSECTOR" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="10"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="FOUNDATIONDATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
     *                   <element name="LIQUIDATIONDATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
     *                   <element name="LOC_NO_1" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="7"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="LOC_NO_2" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="5"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="CHK_DIGIT" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="1"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="LEGALORG" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="2"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                 </sequence>
     *               </restriction>
     *             </complexContent>
     *           </complexType>
     *         </element>
     *         <element name="ADDRESS" maxOccurs="unbounded">
     *           <complexType>
     *             <complexContent>
     *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 <sequence>
     *                   <element name="ADR_KIND">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <minLength value="1"/>
     *                         <maxLength value="10"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="C_O_NAME" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="40"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="CITY">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <minLength value="1"/>
     *                         <maxLength value="40"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="DISTRICT" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="40"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="POSTL_COD1">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <minLength value="1"/>
     *                         <maxLength value="10"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="PO_BOX" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="10"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="STREET">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <minLength value="1"/>
     *                         <maxLength value="40"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="HOUSE_NO" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="10"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="COUNTRY">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="3"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="POSTL_COD2" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="10"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                 </sequence>
     *               </restriction>
     *             </complexContent>
     *           </complexType>
     *         </element>
     *         <element name="COMMUNICATION" maxOccurs="unbounded" minOccurs="0">
     *           <complexType>
     *             <complexContent>
     *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 <sequence>
     *                   <element name="EMAIL" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="241"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="TELEPHONE" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="30"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="MOBILE" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="30"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="FAX" minOccurs="0">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="30"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                 </sequence>
     *               </restriction>
     *             </complexContent>
     *           </complexType>
     *         </element>
     *         <element name="PAYMENT_DETAIL" maxOccurs="unbounded" minOccurs="0">
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
     *                   <element name="BANKDETAILVALIDFROM" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
     *                   <element name="BANKDETAILVALIDTO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
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
     *         <element name="ADDITIONAL_DATA" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}AdditionalData" maxOccurs="unbounded" minOccurs="0"/>
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
        "idkeys",
        "persdata",
        "orgdata",
        "address",
        "communication",
        "paymentdetail",
        "additionaldata"
    })
    public static class BUSINESSPARTNER {

        @XmlElement(name = "HEADER", required = true)
        protected HEADER header;
        @XmlElement(name = "ID_KEYS", required = true)
        protected IDKEYS idkeys;
        @XmlElement(name = "PERS_DATA")
        protected PERSDATA persdata;
        @XmlElement(name = "ORG_DATA")
        protected ORGDATA orgdata;
        @XmlElement(name = "ADDRESS", required = true)
        protected List<ADDRESS> address;
        @XmlElement(name = "COMMUNICATION")
        protected List<COMMUNICATION> communication;
        @XmlElement(name = "PAYMENT_DETAIL")
        protected List<PAYMENTDETAIL> paymentdetail;
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
         * Gets the value of the idkeys property.
         *
         * @return
         *     possible object is
         *     {@link IDKEYS }
         *
         */
        public IDKEYS getIDKEYS() {
            return idkeys;
        }

        /**
         * Sets the value of the idkeys property.
         *
         * @param value
         *     allowed object is
         *     {@link IDKEYS }
         *
         */
        public void setIDKEYS(IDKEYS value) {
            this.idkeys = value;
        }

        /**
         * Gets the value of the persdata property.
         *
         * @return
         *     possible object is
         *     {@link PERSDATA }
         *
         */
        public PERSDATA getPERSDATA() {
            return persdata;
        }

        /**
         * Sets the value of the persdata property.
         *
         * @param value
         *     allowed object is
         *     {@link PERSDATA }
         *
         */
        public void setPERSDATA(PERSDATA value) {
            this.persdata = value;
        }

        /**
         * Gets the value of the orgdata property.
         *
         * @return
         *     possible object is
         *     {@link ORGDATA }
         *
         */
        public ORGDATA getORGDATA() {
            return orgdata;
        }

        /**
         * Sets the value of the orgdata property.
         *
         * @param value
         *     allowed object is
         *     {@link ORGDATA }
         *
         */
        public void setORGDATA(ORGDATA value) {
            this.orgdata = value;
        }

        /**
         * Gets the value of the address property.
         *
         * <p>This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the address property.</p>
         *
         * <p>
         * For example, to add a new item, do as follows:
         * </p>
         * <pre>
         * getADDRESS().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ADDRESS }
         * </p>
         *
         *
         * @return
         *     The value of the address property.
         */
        public List<ADDRESS> getADDRESS() {
            if (address == null) {
                address = new ArrayList<>();
            }
            return this.address;
        }

        /**
         * Gets the value of the communication property.
         *
         * <p>This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the communication property.</p>
         *
         * <p>
         * For example, to add a new item, do as follows:
         * </p>
         * <pre>
         * getCOMMUNICATION().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link COMMUNICATION }
         * </p>
         *
         *
         * @return
         *     The value of the communication property.
         */
        public List<COMMUNICATION> getCOMMUNICATION() {
            if (communication == null) {
                communication = new ArrayList<>();
            }
            return this.communication;
        }

        /**
         * Gets the value of the paymentdetail property.
         *
         * <p>This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the paymentdetail property.</p>
         *
         * <p>
         * For example, to add a new item, do as follows:
         * </p>
         * <pre>
         * getPAYMENTDETAIL().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link PAYMENTDETAIL }
         * </p>
         *
         *
         * @return
         *     The value of the paymentdetail property.
         */
        public List<PAYMENTDETAIL> getPAYMENTDETAIL() {
            if (paymentdetail == null) {
                paymentdetail = new ArrayList<>();
            }
            return this.paymentdetail;
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
         *         <element name="ADR_KIND">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <minLength value="1"/>
         *               <maxLength value="10"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="C_O_NAME" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="40"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="CITY">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <minLength value="1"/>
         *               <maxLength value="40"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="DISTRICT" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="40"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="POSTL_COD1">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <minLength value="1"/>
         *               <maxLength value="10"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="PO_BOX" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="10"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="STREET">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <minLength value="1"/>
         *               <maxLength value="40"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="HOUSE_NO" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="10"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="COUNTRY">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="3"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="POSTL_COD2" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="10"/>
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
            "adrkind",
            "coname",
            "city",
            "district",
            "postlcod1",
            "pobox",
            "street",
            "houseno",
            "country",
            "postlcod2"
        })
        public static class ADDRESS {

            /**
             * Adressart
             *
             */
            @XmlElement(name = "ADR_KIND", required = true, defaultValue = "XXDEFAULT")
            protected String adrkind;
            /**
             * c/o-Name
             *
             */
            @XmlElement(name = "C_O_NAME")
            protected String coname;
            /**
             * Ort
             *
             */
            @XmlElement(name = "CITY", required = true)
            protected String city;
            /**
             * Ortsteil
             *
             */
            @XmlElement(name = "DISTRICT")
            protected String district;
            /**
             * Postleitzahl des Orts
             *
             */
            @XmlElement(name = "POSTL_COD1", required = true)
            protected String postlcod1;
            /**
             * Postfach
             *
             */
            @XmlElement(name = "PO_BOX")
            protected String pobox;
            /**
             * Straße
             *
             */
            @XmlElement(name = "STREET", required = true)
            protected String street;
            /**
             * Hausnummer
             *
             */
            @XmlElement(name = "HOUSE_NO")
            protected String houseno;
            /**
             * Länderschlüssel
             *
             */
            @XmlElement(name = "COUNTRY", required = true)
            protected String country;
            /**
             * Postleitzahl zum Postfach
             *
             */
            @XmlElement(name = "POSTL_COD2")
            protected String postlcod2;

            /**
             * Adressart
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getADRKIND() {
                return adrkind;
            }

            /**
             * Sets the value of the adrkind property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getADRKIND()
             */
            public void setADRKIND(String value) {
                this.adrkind = value;
            }

            /**
             * c/o-Name
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getCONAME() {
                return coname;
            }

            /**
             * Sets the value of the coname property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getCONAME()
             */
            public void setCONAME(String value) {
                this.coname = value;
            }

            /**
             * Ort
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getCITY() {
                return city;
            }

            /**
             * Sets the value of the city property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getCITY()
             */
            public void setCITY(String value) {
                this.city = value;
            }

            /**
             * Ortsteil
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getDISTRICT() {
                return district;
            }

            /**
             * Sets the value of the district property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getDISTRICT()
             */
            public void setDISTRICT(String value) {
                this.district = value;
            }

            /**
             * Postleitzahl des Orts
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getPOSTLCOD1() {
                return postlcod1;
            }

            /**
             * Sets the value of the postlcod1 property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getPOSTLCOD1()
             */
            public void setPOSTLCOD1(String value) {
                this.postlcod1 = value;
            }

            /**
             * Postfach
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getPOBOX() {
                return pobox;
            }

            /**
             * Sets the value of the pobox property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getPOBOX()
             */
            public void setPOBOX(String value) {
                this.pobox = value;
            }

            /**
             * Straße
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getSTREET() {
                return street;
            }

            /**
             * Sets the value of the street property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getSTREET()
             */
            public void setSTREET(String value) {
                this.street = value;
            }

            /**
             * Hausnummer
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getHOUSENO() {
                return houseno;
            }

            /**
             * Sets the value of the houseno property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getHOUSENO()
             */
            public void setHOUSENO(String value) {
                this.houseno = value;
            }

            /**
             * Länderschlüssel
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getCOUNTRY() {
                return country;
            }

            /**
             * Sets the value of the country property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getCOUNTRY()
             */
            public void setCOUNTRY(String value) {
                this.country = value;
            }

            /**
             * Postleitzahl zum Postfach
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getPOSTLCOD2() {
                return postlcod2;
            }

            /**
             * Sets the value of the postlcod2 property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getPOSTLCOD2()
             */
            public void setPOSTLCOD2(String value) {
                this.postlcod2 = value;
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
         *         <element name="EMAIL" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="241"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="TELEPHONE" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="30"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="MOBILE" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="30"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="FAX" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="30"/>
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
            "email",
            "telephone",
            "mobile",
            "fax"
        })
        public static class COMMUNICATION {

            /**
             * Email-Adresse
             *
             */
            @XmlElement(name = "EMAIL")
            protected String email;
            /**
             * Erste Telefon-Nr.: Vorwahl + Anschluß
             *
             */
            @XmlElement(name = "TELEPHONE")
            protected String telephone;
            /**
             * Erste Mobiltelefon-Nr.: Vorwahl + Anschluß
             *
             */
            @XmlElement(name = "MOBILE")
            protected String mobile;
            /**
             * Erste Fax-Nr.: Vorwahl + Anschluss
             *
             */
            @XmlElement(name = "FAX")
            protected String fax;

            /**
             * Email-Adresse
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getEMAIL() {
                return email;
            }

            /**
             * Sets the value of the email property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getEMAIL()
             */
            public void setEMAIL(String value) {
                this.email = value;
            }

            /**
             * Erste Telefon-Nr.: Vorwahl + Anschluß
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getTELEPHONE() {
                return telephone;
            }

            /**
             * Sets the value of the telephone property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getTELEPHONE()
             */
            public void setTELEPHONE(String value) {
                this.telephone = value;
            }

            /**
             * Erste Mobiltelefon-Nr.: Vorwahl + Anschluß
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getMOBILE() {
                return mobile;
            }

            /**
             * Sets the value of the mobile property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getMOBILE()
             */
            public void setMOBILE(String value) {
                this.mobile = value;
            }

            /**
             * Erste Fax-Nr.: Vorwahl + Anschluss
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getFAX() {
                return fax;
            }

            /**
             * Sets the value of the fax property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getFAX()
             */
            public void setFAX(String value) {
                this.fax = value;
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
         *         <element name="PARTN_CAT">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <enumeration value="1"/>
         *               <enumeration value="2"/>
         *               <maxLength value="10"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="PARTN_TYP" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="4"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="PARTN_GRP" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <enumeration value="ZBPE"/>
         *               <maxLength value="4"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="PARTN_TAXKD" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="1"/>
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
            "partncat",
            "partntyp",
            "partngrp",
            "partntaxkd"
        })
        public static class HEADER {

            /**
             * Geschäftspartnertyp
             *
             */
            @XmlElement(name = "PARTN_CAT", required = true, defaultValue = "1")
            protected String partncat;
            /**
             * Geschäftspartnerart
             *
             */
            @XmlElement(name = "PARTN_TYP")
            protected String partntyp;
            /**
             * Geschäftspartnergruppierung
             *
             */
            @XmlElement(name = "PARTN_GRP", defaultValue = "ZBPE")
            protected String partngrp;
            @XmlElement(name = "PARTN_TAXKD", defaultValue = "1")
            protected String partntaxkd;

            /**
             * Geschäftspartnertyp
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getPARTNCAT() {
                return partncat;
            }

            /**
             * Sets the value of the partncat property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getPARTNCAT()
             */
            public void setPARTNCAT(String value) {
                this.partncat = value;
            }

            /**
             * Geschäftspartnerart
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getPARTNTYP() {
                return partntyp;
            }

            /**
             * Sets the value of the partntyp property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getPARTNTYP()
             */
            public void setPARTNTYP(String value) {
                this.partntyp = value;
            }

            /**
             * Geschäftspartnergruppierung
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getPARTNGRP() {
                return partngrp;
            }

            /**
             * Sets the value of the partngrp property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getPARTNGRP()
             */
            public void setPARTNGRP(String value) {
                this.partngrp = value;
            }

            /**
             * Gets the value of the partntaxkd property.
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getPARTNTAXKD() {
                return partntaxkd;
            }

            /**
             * Sets the value of the partntaxkd property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setPARTNTAXKD(String value) {
                this.partntaxkd = value;
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
         *         <element name="EXT_ID">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="20"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="AHV_NR" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="13"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="UID_NR" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="12"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="ZPV_NR" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="10"/>
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
            "extid",
            "ahvnr",
            "uidnr",
            "zpvnr"
        })
        public static class IDKEYS {

            /**
             * Geschäftspartnernummer im externen System
             *
             */
            @XmlElement(name = "EXT_ID", required = true)
            protected String extid;
            /**
             * AHV-Nummer
             *
             */
            @XmlElement(name = "AHV_NR")
            protected String ahvnr;
            /**
             * Unternehmens-Identifikationsnummer
             *
             */
            @XmlElement(name = "UID_NR")
            protected String uidnr;
            /**
             * ZPV-Nummer
             *
             */
            @XmlElement(name = "ZPV_NR")
            protected String zpvnr;

            /**
             * Geschäftspartnernummer im externen System
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getEXTID() {
                return extid;
            }

            /**
             * Sets the value of the extid property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getEXTID()
             */
            public void setEXTID(String value) {
                this.extid = value;
            }

            /**
             * AHV-Nummer
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getAHVNR() {
                return ahvnr;
            }

            /**
             * Sets the value of the ahvnr property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getAHVNR()
             */
            public void setAHVNR(String value) {
                this.ahvnr = value;
            }

            /**
             * Unternehmens-Identifikationsnummer
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getUIDNR() {
                return uidnr;
            }

            /**
             * Sets the value of the uidnr property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getUIDNR()
             */
            public void setUIDNR(String value) {
                this.uidnr = value;
            }

            /**
             * ZPV-Nummer
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getZPVNR() {
                return zpvnr;
            }

            /**
             * Sets the value of the zpvnr property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getZPVNR()
             */
            public void setZPVNR(String value) {
                this.zpvnr = value;
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
         *         <element name="NAME1">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="40"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="NAME2" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="40"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="NAME3" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="40"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="NAME4" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="40"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="LANGU_ISO">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="2"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="LEGALFORM" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="2"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="INDUSTRYSECTOR" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="10"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="FOUNDATIONDATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
         *         <element name="LIQUIDATIONDATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
         *         <element name="LOC_NO_1" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="7"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="LOC_NO_2" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="5"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="CHK_DIGIT" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="1"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="LEGALORG" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="2"/>
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
            "name1",
            "name2",
            "name3",
            "name4",
            "languiso",
            "legalform",
            "industrysector",
            "foundationdate",
            "liquidationdate",
            "locno1",
            "locno2",
            "chkdigit",
            "legalorg"
        })
        public static class ORGDATA {

            /**
             * Name 1 der Organisation
             *
             */
            @XmlElement(name = "NAME1", required = true)
            protected String name1;
            /**
             * Name 2 der Organisation
             *
             */
            @XmlElement(name = "NAME2")
            protected String name2;
            /**
             * Name 3 der Organisation
             *
             */
            @XmlElement(name = "NAME3")
            protected String name3;
            /**
             * Name 4 der Organisation
             *
             */
            @XmlElement(name = "NAME4")
            protected String name4;
            /**
             * Sprache der Organisation
             *
             */
            @XmlElement(name = "LANGU_ISO", required = true, defaultValue = "DE")
            protected String languiso;
            /**
             * GP: Rechtsform der Organisation
             *
             */
            @XmlElement(name = "LEGALFORM")
            protected String legalform;
            /**
             * Branche
             *
             */
            @XmlElement(name = "INDUSTRYSECTOR")
            protected String industrysector;
            /**
             * Gründungsdatum der Organisation
             *
             */
            @XmlElement(name = "FOUNDATIONDATE")
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar foundationdate;
            /**
             * Liquidationsdatum der Organisation
             *
             */
            @XmlElement(name = "LIQUIDATIONDATE")
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar liquidationdate;
            /**
             * Internationale Lokationsnummer (Teil 1)
             *
             */
            @XmlElement(name = "LOC_NO_1")
            protected String locno1;
            /**
             * Internationale Lokationsnummer (Teil 2)
             *
             */
            @XmlElement(name = "LOC_NO_2")
            protected String locno2;
            /**
             * Prüfziffer der internationalen Lokationsnummer
             *
             */
            @XmlElement(name = "CHK_DIGIT")
            protected String chkdigit;
            /**
             * Rechtsträger der Organisation
             *
             */
            @XmlElement(name = "LEGALORG")
            protected String legalorg;

            /**
             * Name 1 der Organisation
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getNAME1() {
                return name1;
            }

            /**
             * Sets the value of the name1 property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getNAME1()
             */
            public void setNAME1(String value) {
                this.name1 = value;
            }

            /**
             * Name 2 der Organisation
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getNAME2() {
                return name2;
            }

            /**
             * Sets the value of the name2 property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getNAME2()
             */
            public void setNAME2(String value) {
                this.name2 = value;
            }

            /**
             * Name 3 der Organisation
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getNAME3() {
                return name3;
            }

            /**
             * Sets the value of the name3 property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getNAME3()
             */
            public void setNAME3(String value) {
                this.name3 = value;
            }

            /**
             * Name 4 der Organisation
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getNAME4() {
                return name4;
            }

            /**
             * Sets the value of the name4 property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getNAME4()
             */
            public void setNAME4(String value) {
                this.name4 = value;
            }

            /**
             * Sprache der Organisation
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getLANGUISO() {
                return languiso;
            }

            /**
             * Sets the value of the languiso property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getLANGUISO()
             */
            public void setLANGUISO(String value) {
                this.languiso = value;
            }

            /**
             * GP: Rechtsform der Organisation
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getLEGALFORM() {
                return legalform;
            }

            /**
             * Sets the value of the legalform property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getLEGALFORM()
             */
            public void setLEGALFORM(String value) {
                this.legalform = value;
            }

            /**
             * Branche
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getINDUSTRYSECTOR() {
                return industrysector;
            }

            /**
             * Sets the value of the industrysector property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getINDUSTRYSECTOR()
             */
            public void setINDUSTRYSECTOR(String value) {
                this.industrysector = value;
            }

            /**
             * Gründungsdatum der Organisation
             *
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *
             */
            public XMLGregorianCalendar getFOUNDATIONDATE() {
                return foundationdate;
            }

            /**
             * Sets the value of the foundationdate property.
             *
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *
             * @see #getFOUNDATIONDATE()
             */
            public void setFOUNDATIONDATE(XMLGregorianCalendar value) {
                this.foundationdate = value;
            }

            /**
             * Liquidationsdatum der Organisation
             *
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *
             */
            public XMLGregorianCalendar getLIQUIDATIONDATE() {
                return liquidationdate;
            }

            /**
             * Sets the value of the liquidationdate property.
             *
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *
             * @see #getLIQUIDATIONDATE()
             */
            public void setLIQUIDATIONDATE(XMLGregorianCalendar value) {
                this.liquidationdate = value;
            }

            /**
             * Internationale Lokationsnummer (Teil 1)
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getLOCNO1() {
                return locno1;
            }

            /**
             * Sets the value of the locno1 property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getLOCNO1()
             */
            public void setLOCNO1(String value) {
                this.locno1 = value;
            }

            /**
             * Internationale Lokationsnummer (Teil 2)
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getLOCNO2() {
                return locno2;
            }

            /**
             * Sets the value of the locno2 property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getLOCNO2()
             */
            public void setLOCNO2(String value) {
                this.locno2 = value;
            }

            /**
             * Prüfziffer der internationalen Lokationsnummer
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getCHKDIGIT() {
                return chkdigit;
            }

            /**
             * Sets the value of the chkdigit property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getCHKDIGIT()
             */
            public void setCHKDIGIT(String value) {
                this.chkdigit = value;
            }

            /**
             * Rechtsträger der Organisation
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getLEGALORG() {
                return legalorg;
            }

            /**
             * Sets the value of the legalorg property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getLEGALORG()
             */
            public void setLEGALORG(String value) {
                this.legalorg = value;
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
         *         <element name="IBAN">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="34"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="BANKDETAILVALIDFROM" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
         *         <element name="BANKDETAILVALIDTO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
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
            "bankdetailvalidfrom",
            "bankdetailvalidto",
            "accountholder"
        })
        public static class PAYMENTDETAIL {

            /**
             * IBAN (International Bank Account Number)
             *
             */
            @XmlElement(name = "IBAN", required = true)
            protected String iban;
            /**
             * Gültigkeitsdatum (gültig ab)
             *
             */
            @XmlElement(name = "BANKDETAILVALIDFROM")
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar bankdetailvalidfrom;
            /**
             * Gültigkeitsdatum (gültig bis)
             *
             */
            @XmlElement(name = "BANKDETAILVALIDTO")
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar bankdetailvalidto;
            /**
             * Abweichender Name der Zahlungsverbindung
             *
             */
            @XmlElement(name = "ACCOUNTHOLDER")
            protected String accountholder;

            /**
             * IBAN (International Bank Account Number)
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
             * Gültigkeitsdatum (gültig ab)
             *
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *
             */
            public XMLGregorianCalendar getBANKDETAILVALIDFROM() {
                return bankdetailvalidfrom;
            }

            /**
             * Sets the value of the bankdetailvalidfrom property.
             *
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *
             * @see #getBANKDETAILVALIDFROM()
             */
            public void setBANKDETAILVALIDFROM(XMLGregorianCalendar value) {
                this.bankdetailvalidfrom = value;
            }

            /**
             * Gültigkeitsdatum (gültig bis)
             *
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *
             */
            public XMLGregorianCalendar getBANKDETAILVALIDTO() {
                return bankdetailvalidto;
            }

            /**
             * Sets the value of the bankdetailvalidto property.
             *
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *
             * @see #getBANKDETAILVALIDTO()
             */
            public void setBANKDETAILVALIDTO(XMLGregorianCalendar value) {
                this.bankdetailvalidto = value;
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
         *         <element name="FIRSTNAME">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="40"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="LASTNAME">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="40"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="CORRESPONDLANGUAGEISO">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="2"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="BIRTHNAME" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="40"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="MIDDLENAME" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="40"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="SECONDNAME" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="40"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="TITLE_ACA1" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <enumeration value="0001"/>
         *               <enumeration value="0002"/>
         *               <enumeration value="0003"/>
         *               <enumeration value="0004"/>
         *               <enumeration value="0005"/>
         *               <enumeration value="0006"/>
         *               <maxLength value="4"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="TITLE_ACA2" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="4"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="TITLE_SPPL" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="4"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="PREFIX1" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="4"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="PREFIX2" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="4"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="NICKNAME" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="40"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="INITIALS" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="10"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="NAMEFORMAT" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="2"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="NAMCOUNTRY" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="3"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="NAMCOUNTRYISO" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="2"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="SEX" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <enumeration value="1"/>
         *               <enumeration value="2"/>
         *               <maxLength value="1"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="BIRTHPLACE" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="40"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="BIRTHDATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
         *         <element name="DEATHDATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
         *         <element name="MARITALSTATUS" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="1"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="CORRESPONDLANGUAGE" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="1"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="FULLNAME" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="80"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="EMPLOYER" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="35"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="OCCUPATION" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="4"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="NATIONALITY" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="3"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="NATIONALITYISO" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="2"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="COUNTRYORIGIN" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="3"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="BIRTHDT_STATUS" minOccurs="0">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="1"/>
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
            "firstname",
            "lastname",
            "correspondlanguageiso",
            "birthname",
            "middlename",
            "secondname",
            "titleaca1",
            "titleaca2",
            "titlesppl",
            "prefix1",
            "prefix2",
            "nickname",
            "initials",
            "nameformat",
            "namcountry",
            "namcountryiso",
            "sex",
            "birthplace",
            "birthdate",
            "deathdate",
            "maritalstatus",
            "correspondlanguage",
            "fullname",
            "employer",
            "occupation",
            "nationality",
            "nationalityiso",
            "countryorigin",
            "birthdtstatus"
        })
        public static class PERSDATA {

            /**
             * Vorname des Geschäftspartners (Person)
             *
             */
            @XmlElement(name = "FIRSTNAME", required = true)
            protected String firstname;
            /**
             * Nachname des Geschäftspartners (Person)
             *
             */
            @XmlElement(name = "LASTNAME", required = true)
            protected String lastname;
            /**
             *  2-stelliger SAP Language Code
             *
             */
            @XmlElement(name = "CORRESPONDLANGUAGEISO", required = true, defaultValue = "DE")
            protected String correspondlanguageiso;
            /**
             * Geburtsname des Geschäftspartners
             *
             */
            @XmlElement(name = "BIRTHNAME")
            protected String birthname;
            /**
             * Mittlerer Name oder zweiter Vorname der Person
             *
             */
            @XmlElement(name = "MIDDLENAME")
            protected String middlename;
            /**
             * Zweiter Nachname der Person
             *
             */
            @XmlElement(name = "SECONDNAME")
            protected String secondname;
            /**
             * Akademischer Titel: Schlüssel
             *
             */
            @XmlElement(name = "TITLE_ACA1")
            protected String titleaca1;
            /**
             * Zweiter akademischer Titel (Schlüssel)
             *
             */
            @XmlElement(name = "TITLE_ACA2")
            protected String titleaca2;
            /**
             * Namenszusatz, z.B. Adelstitel (Schlüssel)
             *
             */
            @XmlElement(name = "TITLE_SPPL")
            protected String titlesppl;
            /**
             * Vorsatzwort zum Namen (Schlüssel)
             *
             */
            @XmlElement(name = "PREFIX1")
            protected String prefix1;
            /**
             *  2. Vorsatzwort zum Namen (Schlüssel)
             *
             */
            @XmlElement(name = "PREFIX2")
            protected String prefix2;
            /**
             * Rufname des Geschäftspartners (Person)
             *
             */
            @XmlElement(name = "NICKNAME")
            protected String nickname;
            /**
             * Middle Initial bzw. Initialen der Person
             *
             */
            @XmlElement(name = "INITIALS")
            protected String initials;
            /**
             * Format für die Namensaufbereitung
             *
             */
            @XmlElement(name = "NAMEFORMAT")
            protected String nameformat;
            /**
             * Land für Namensaufbereitungsregel
             *
             */
            @XmlElement(name = "NAMCOUNTRY")
            protected String namcountry;
            /**
             * ISO-Code des Landes
             *
             */
            @XmlElement(name = "NAMCOUNTRYISO")
            protected String namcountryiso;
            /**
             * Geschlecht des Geschäftspartners (Person)
             *
             */
            @XmlElement(name = "SEX")
            protected String sex;
            /**
             * Geburtsort des Geschäftspartners
             *
             */
            @XmlElement(name = "BIRTHPLACE")
            protected String birthplace;
            /**
             * Geburtsdatum des Geschäftspartners
             *
             */
            @XmlElement(name = "BIRTHDATE")
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar birthdate;
            /**
             * Sterbedatum des Geschäftspartners
             *
             */
            @XmlElement(name = "DEATHDATE")
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar deathdate;
            /**
             * Familienstand des Geschäftspartners
             *
             */
            @XmlElement(name = "MARITALSTATUS")
            protected String maritalstatus;
            /**
             * Geschäftspartner: Korrespondenzsprache
             *
             */
            @XmlElement(name = "CORRESPONDLANGUAGE")
            protected String correspondlanguage;
            /**
             * Vollständiger Name
             *
             */
            @XmlElement(name = "FULLNAME")
            protected String fullname;
            /**
             * Name des Arbeitgebers einer natürlichen Person
             *
             */
            @XmlElement(name = "EMPLOYER")
            protected String employer;
            /**
             * Beruf des Geschäftspartners
             *
             */
            @XmlElement(name = "OCCUPATION")
            protected String occupation;
            /**
             * Nationalität des Geschäftspartners
             *
             */
            @XmlElement(name = "NATIONALITY")
            protected String nationality;
            /**
             * ISO-Code des Landes
             *
             */
            @XmlElement(name = "NATIONALITYISO")
            protected String nationalityiso;
            /**
             * Herkunftsland bei Gebietsfremden
             *
             */
            @XmlElement(name = "COUNTRYORIGIN")
            protected String countryorigin;
            /**
             * Date of Birth: Status
             *
             */
            @XmlElement(name = "BIRTHDT_STATUS")
            protected String birthdtstatus;

            /**
             * Vorname des Geschäftspartners (Person)
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getFIRSTNAME() {
                return firstname;
            }

            /**
             * Sets the value of the firstname property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getFIRSTNAME()
             */
            public void setFIRSTNAME(String value) {
                this.firstname = value;
            }

            /**
             * Nachname des Geschäftspartners (Person)
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getLASTNAME() {
                return lastname;
            }

            /**
             * Sets the value of the lastname property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getLASTNAME()
             */
            public void setLASTNAME(String value) {
                this.lastname = value;
            }

            /**
             *  2-stelliger SAP Language Code
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getCORRESPONDLANGUAGEISO() {
                return correspondlanguageiso;
            }

            /**
             * Sets the value of the correspondlanguageiso property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getCORRESPONDLANGUAGEISO()
             */
            public void setCORRESPONDLANGUAGEISO(String value) {
                this.correspondlanguageiso = value;
            }

            /**
             * Geburtsname des Geschäftspartners
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getBIRTHNAME() {
                return birthname;
            }

            /**
             * Sets the value of the birthname property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getBIRTHNAME()
             */
            public void setBIRTHNAME(String value) {
                this.birthname = value;
            }

            /**
             * Mittlerer Name oder zweiter Vorname der Person
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getMIDDLENAME() {
                return middlename;
            }

            /**
             * Sets the value of the middlename property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getMIDDLENAME()
             */
            public void setMIDDLENAME(String value) {
                this.middlename = value;
            }

            /**
             * Zweiter Nachname der Person
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getSECONDNAME() {
                return secondname;
            }

            /**
             * Sets the value of the secondname property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getSECONDNAME()
             */
            public void setSECONDNAME(String value) {
                this.secondname = value;
            }

            /**
             * Akademischer Titel: Schlüssel
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getTITLEACA1() {
                return titleaca1;
            }

            /**
             * Sets the value of the titleaca1 property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getTITLEACA1()
             */
            public void setTITLEACA1(String value) {
                this.titleaca1 = value;
            }

            /**
             * Zweiter akademischer Titel (Schlüssel)
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getTITLEACA2() {
                return titleaca2;
            }

            /**
             * Sets the value of the titleaca2 property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getTITLEACA2()
             */
            public void setTITLEACA2(String value) {
                this.titleaca2 = value;
            }

            /**
             * Namenszusatz, z.B. Adelstitel (Schlüssel)
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getTITLESPPL() {
                return titlesppl;
            }

            /**
             * Sets the value of the titlesppl property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getTITLESPPL()
             */
            public void setTITLESPPL(String value) {
                this.titlesppl = value;
            }

            /**
             * Vorsatzwort zum Namen (Schlüssel)
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getPREFIX1() {
                return prefix1;
            }

            /**
             * Sets the value of the prefix1 property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getPREFIX1()
             */
            public void setPREFIX1(String value) {
                this.prefix1 = value;
            }

            /**
             *  2. Vorsatzwort zum Namen (Schlüssel)
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getPREFIX2() {
                return prefix2;
            }

            /**
             * Sets the value of the prefix2 property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getPREFIX2()
             */
            public void setPREFIX2(String value) {
                this.prefix2 = value;
            }

            /**
             * Rufname des Geschäftspartners (Person)
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getNICKNAME() {
                return nickname;
            }

            /**
             * Sets the value of the nickname property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getNICKNAME()
             */
            public void setNICKNAME(String value) {
                this.nickname = value;
            }

            /**
             * Middle Initial bzw. Initialen der Person
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getINITIALS() {
                return initials;
            }

            /**
             * Sets the value of the initials property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getINITIALS()
             */
            public void setINITIALS(String value) {
                this.initials = value;
            }

            /**
             * Format für die Namensaufbereitung
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getNAMEFORMAT() {
                return nameformat;
            }

            /**
             * Sets the value of the nameformat property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getNAMEFORMAT()
             */
            public void setNAMEFORMAT(String value) {
                this.nameformat = value;
            }

            /**
             * Land für Namensaufbereitungsregel
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getNAMCOUNTRY() {
                return namcountry;
            }

            /**
             * Sets the value of the namcountry property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getNAMCOUNTRY()
             */
            public void setNAMCOUNTRY(String value) {
                this.namcountry = value;
            }

            /**
             * ISO-Code des Landes
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getNAMCOUNTRYISO() {
                return namcountryiso;
            }

            /**
             * Sets the value of the namcountryiso property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getNAMCOUNTRYISO()
             */
            public void setNAMCOUNTRYISO(String value) {
                this.namcountryiso = value;
            }

            /**
             * Geschlecht des Geschäftspartners (Person)
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getSEX() {
                return sex;
            }

            /**
             * Sets the value of the sex property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getSEX()
             */
            public void setSEX(String value) {
                this.sex = value;
            }

            /**
             * Geburtsort des Geschäftspartners
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getBIRTHPLACE() {
                return birthplace;
            }

            /**
             * Sets the value of the birthplace property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getBIRTHPLACE()
             */
            public void setBIRTHPLACE(String value) {
                this.birthplace = value;
            }

            /**
             * Geburtsdatum des Geschäftspartners
             *
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *
             */
            public XMLGregorianCalendar getBIRTHDATE() {
                return birthdate;
            }

            /**
             * Sets the value of the birthdate property.
             *
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *
             * @see #getBIRTHDATE()
             */
            public void setBIRTHDATE(XMLGregorianCalendar value) {
                this.birthdate = value;
            }

            /**
             * Sterbedatum des Geschäftspartners
             *
             * @return
             *     possible object is
             *     {@link XMLGregorianCalendar }
             *
             */
            public XMLGregorianCalendar getDEATHDATE() {
                return deathdate;
            }

            /**
             * Sets the value of the deathdate property.
             *
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *
             * @see #getDEATHDATE()
             */
            public void setDEATHDATE(XMLGregorianCalendar value) {
                this.deathdate = value;
            }

            /**
             * Familienstand des Geschäftspartners
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getMARITALSTATUS() {
                return maritalstatus;
            }

            /**
             * Sets the value of the maritalstatus property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getMARITALSTATUS()
             */
            public void setMARITALSTATUS(String value) {
                this.maritalstatus = value;
            }

            /**
             * Geschäftspartner: Korrespondenzsprache
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getCORRESPONDLANGUAGE() {
                return correspondlanguage;
            }

            /**
             * Sets the value of the correspondlanguage property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getCORRESPONDLANGUAGE()
             */
            public void setCORRESPONDLANGUAGE(String value) {
                this.correspondlanguage = value;
            }

            /**
             * Vollständiger Name
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getFULLNAME() {
                return fullname;
            }

            /**
             * Sets the value of the fullname property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getFULLNAME()
             */
            public void setFULLNAME(String value) {
                this.fullname = value;
            }

            /**
             * Name des Arbeitgebers einer natürlichen Person
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getEMPLOYER() {
                return employer;
            }

            /**
             * Sets the value of the employer property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getEMPLOYER()
             */
            public void setEMPLOYER(String value) {
                this.employer = value;
            }

            /**
             * Beruf des Geschäftspartners
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getOCCUPATION() {
                return occupation;
            }

            /**
             * Sets the value of the occupation property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getOCCUPATION()
             */
            public void setOCCUPATION(String value) {
                this.occupation = value;
            }

            /**
             * Nationalität des Geschäftspartners
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getNATIONALITY() {
                return nationality;
            }

            /**
             * Sets the value of the nationality property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getNATIONALITY()
             */
            public void setNATIONALITY(String value) {
                this.nationality = value;
            }

            /**
             * ISO-Code des Landes
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getNATIONALITYISO() {
                return nationalityiso;
            }

            /**
             * Sets the value of the nationalityiso property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getNATIONALITYISO()
             */
            public void setNATIONALITYISO(String value) {
                this.nationalityiso = value;
            }

            /**
             * Herkunftsland bei Gebietsfremden
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getCOUNTRYORIGIN() {
                return countryorigin;
            }

            /**
             * Sets the value of the countryorigin property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getCOUNTRYORIGIN()
             */
            public void setCOUNTRYORIGIN(String value) {
                this.countryorigin = value;
            }

            /**
             * Date of Birth: Status
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getBIRTHDTSTATUS() {
                return birthdtstatus;
            }

            /**
             * Sets the value of the birthdtstatus property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getBIRTHDTSTATUS()
             */
            public void setBIRTHDTSTATUS(String value) {
                this.birthdtstatus = value;
            }

        }

    }

}
