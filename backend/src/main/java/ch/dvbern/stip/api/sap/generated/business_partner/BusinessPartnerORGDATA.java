
package ch.dvbern.stip.api.sap.generated.business_partner;

import javax.xml.datatype.XMLGregorianCalendar;

import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementRef;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for BusinessPartner_ORG_DATA complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="BusinessPartner_ORG_DATA">
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
 *         <element name="NAME2">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="40"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="NAME3">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="40"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="NAME4">
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
 *         <element name="LEGALFORM">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="2"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="INDUSTRYSECTOR">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="10"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="FOUNDATIONDATE" minOccurs="0">
 *           <complexType>
 *             <simpleContent>
 *               <extension base="<http://www.w3.org/2001/XMLSchema>date">
 *                 <attribute ref="{http://sap.com/xi/SAPGlobal/GDT}DeletionIndicator"/>
 *               </extension>
 *             </simpleContent>
 *           </complexType>
 *         </element>
 *         <element name="LIQUIDATIONDATE" minOccurs="0">
 *           <complexType>
 *             <simpleContent>
 *               <extension base="<http://www.w3.org/2001/XMLSchema>date">
 *                 <attribute ref="{http://sap.com/xi/SAPGlobal/GDT}DeletionIndicator"/>
 *               </extension>
 *             </simpleContent>
 *           </complexType>
 *         </element>
 *         <element name="LOC_NO_1">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="7"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="LOC_NO_2">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="5"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="CHK_DIGIT">
 *           <simpleType>
 *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               <maxLength value="1"/>
 *             </restriction>
 *           </simpleType>
 *         </element>
 *         <element name="LEGALORG">
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
@XmlType(name = "BusinessPartner_ORG_DATA", propOrder = {
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
public class BusinessPartnerORGDATA {

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
    @XmlElement(name = "NAME2", required = true)
    protected String name2;
    /**
     * Name 3 der Organisation
     *
     */
    @XmlElement(name = "NAME3", required = true)
    protected String name3;
    /**
     * Name 4 der Organisation
     *
     */
    @XmlElement(name = "NAME4", required = true)
    protected String name4;
    /**
     * Sprache der Organisation
     *
     */
    @XmlElement(name = "LANGU_ISO", required = true)
    protected String languiso;
    /**
     * GP: Rechtsform der Organisation
     *
     */
    @XmlElement(name = "LEGALFORM", required = true)
    protected String legalform;
    /**
     * Branche
     *
     */
    @XmlElement(name = "INDUSTRYSECTOR", required = true)
    protected String industrysector;
    /**
     * Gründungsdatum der Organisation
     *
     */
    @XmlElementRef(name = "FOUNDATIONDATE", type = JAXBElement.class, required = false)
    protected JAXBElement<FOUNDATIONDATE> foundationdate;
    /**
     * Liquidationsdatum der Organisation
     *
     */
    @XmlElementRef(name = "LIQUIDATIONDATE", type = JAXBElement.class, required = false)
    protected JAXBElement<LIQUIDATIONDATE> liquidationdate;
    /**
     * Internationale Lokationsnummer (Teil 1)
     *
     */
    @XmlElement(name = "LOC_NO_1", required = true)
    protected String locno1;
    /**
     * Internationale Lokationsnummer (Teil 2)
     *
     */
    @XmlElement(name = "LOC_NO_2", required = true)
    protected String locno2;
    /**
     * Prüfziffer der internationalen Lokationsnummer
     *
     */
    @XmlElement(name = "CHK_DIGIT", required = true)
    protected String chkdigit;
    /**
     * Rechtsträger der Organisation
     *
     */
    @XmlElement(name = "LEGALORG", required = true)
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
     *     {@link JAXBElement }{@code <}{@link FOUNDATIONDATE }{@code >}
     *
     */
    public JAXBElement<FOUNDATIONDATE> getFOUNDATIONDATE() {
        return foundationdate;
    }

    /**
     * Sets the value of the foundationdate property.
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link FOUNDATIONDATE }{@code >}
     *
     * @see #getFOUNDATIONDATE()
     */
    public void setFOUNDATIONDATE(JAXBElement<FOUNDATIONDATE> value) {
        this.foundationdate = value;
    }

    /**
     * Liquidationsdatum der Organisation
     *
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link LIQUIDATIONDATE }{@code >}
     *
     */
    public JAXBElement<LIQUIDATIONDATE> getLIQUIDATIONDATE() {
        return liquidationdate;
    }

    /**
     * Sets the value of the liquidationdate property.
     *
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link LIQUIDATIONDATE }{@code >}
     *
     * @see #getLIQUIDATIONDATE()
     */
    public void setLIQUIDATIONDATE(JAXBElement<LIQUIDATIONDATE> value) {
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


    /**
     * <p>Java class for anonymous complex type</p>.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.</p>
     *
     * <pre>{@code
     * <complexType>
     *   <simpleContent>
     *     <extension base="<http://www.w3.org/2001/XMLSchema>date">
     *       <attribute ref="{http://sap.com/xi/SAPGlobal/GDT}DeletionIndicator"/>
     *     </extension>
     *   </simpleContent>
     * </complexType>
     * }</pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class FOUNDATIONDATE {

        @XmlValue
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar value;
        @XmlAttribute(name = "DeletionIndicator", namespace = "http://sap.com/xi/SAPGlobal/GDT")
        protected Boolean deletionIndicator;

        /**
         * Gets the value of the value property.
         *
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *
         */
        public XMLGregorianCalendar getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         *
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *
         */
        public void setValue(XMLGregorianCalendar value) {
            this.value = value;
        }

        /**
         * Gets the value of the deletionIndicator property.
         *
         * @return
         *     possible object is
         *     {@link Boolean }
         *
         */
        public Boolean isDeletionIndicator() {
            return deletionIndicator;
        }

        /**
         * Sets the value of the deletionIndicator property.
         *
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *
         */
        public void setDeletionIndicator(Boolean value) {
            this.deletionIndicator = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type</p>.
     *
     * <p>The following schema fragment specifies the expected content contained within this class.</p>
     *
     * <pre>{@code
     * <complexType>
     *   <simpleContent>
     *     <extension base="<http://www.w3.org/2001/XMLSchema>date">
     *       <attribute ref="{http://sap.com/xi/SAPGlobal/GDT}DeletionIndicator"/>
     *     </extension>
     *   </simpleContent>
     * </complexType>
     * }</pre>
     *
     *
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class LIQUIDATIONDATE {

        @XmlValue
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar value;
        @XmlAttribute(name = "DeletionIndicator", namespace = "http://sap.com/xi/SAPGlobal/GDT")
        protected Boolean deletionIndicator;

        /**
         * Gets the value of the value property.
         *
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *
         */
        public XMLGregorianCalendar getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         *
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *
         */
        public void setValue(XMLGregorianCalendar value) {
            this.value = value;
        }

        /**
         * Gets the value of the deletionIndicator property.
         *
         * @return
         *     possible object is
         *     {@link Boolean }
         *
         */
        public Boolean isDeletionIndicator() {
            return deletionIndicator;
        }

        /**
         * Sets the value of the deletionIndicator property.
         *
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *
         */
        public void setDeletionIndicator(Boolean value) {
            this.deletionIndicator = value;
        }

    }

}
