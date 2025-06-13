
package ch.dvbern.stip.api.sap.generated.import_status;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.datatype.XMLGregorianCalendar;

import ch.dvbern.stip.api.sap.generated.general.SenderParms;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Request für SST-073
 *
 * <p>Java class for ImportStatusRead_Request complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="ImportStatusRead_Request">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="SENDER" type="{urn:be.ch:KTBE_ERP_FI:GENERAL}SenderParms"/>
 *         <element name="FILTER_PARMS">
 *           <complexType>
 *             <complexContent>
 *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 <sequence>
 *                   <element name="DELIVERY_ID" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         <totalDigits value="19"/>
 *                         <fractionDigits value="0"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="SHOW_DETAILS" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *                   <element name="STATUS" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                         <enumeration value="0"/>
 *                         <enumeration value="1"/>
 *                         <enumeration value="2"/>
 *                         <enumeration value="3"/>
 *                         <enumeration value="4"/>
 *                         <enumeration value="99"/>
 *                         <totalDigits value="3"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="DATE_FROM" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *                   <element name="DATE_TO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *                   <element name="OFFSET" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                         <totalDigits value="6"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="LIMIT" minOccurs="0">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                         <totalDigits value="6"/>
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
@XmlType(name = "ImportStatusRead_Request", propOrder = {
    "sender",
    "filterparms"
})
public class ImportStatusReadRequest {

    @XmlElement(name = "SENDER", required = true)
    protected SenderParms sender;
    @XmlElement(name = "FILTER_PARMS", required = true)
    protected FILTERPARMS filterparms;

    /**
     * Gets the value of the sender property.
     *
     * @return
     *     possible object is
     *     {@link SenderParms }
     *
     */
    public SenderParms getSENDER() {
        return sender;
    }

    /**
     * Sets the value of the sender property.
     *
     * @param value
     *     allowed object is
     *     {@link SenderParms }
     *
     */
    public void setSENDER(SenderParms value) {
        this.sender = value;
    }

    /**
     * Gets the value of the filterparms property.
     *
     * @return
     *     possible object is
     *     {@link FILTERPARMS }
     *
     */
    public FILTERPARMS getFILTERPARMS() {
        return filterparms;
    }

    /**
     * Sets the value of the filterparms property.
     *
     * @param value
     *     allowed object is
     *     {@link FILTERPARMS }
     *
     */
    public void setFILTERPARMS(FILTERPARMS value) {
        this.filterparms = value;
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
     *         <element name="DELIVERY_ID" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               <totalDigits value="19"/>
     *               <fractionDigits value="0"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="SHOW_DETAILS" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
     *         <element name="STATUS" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *               <enumeration value="0"/>
     *               <enumeration value="1"/>
     *               <enumeration value="2"/>
     *               <enumeration value="3"/>
     *               <enumeration value="4"/>
     *               <enumeration value="99"/>
     *               <totalDigits value="3"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="DATE_FROM" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
     *         <element name="DATE_TO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
     *         <element name="OFFSET" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *               <totalDigits value="6"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="LIMIT" minOccurs="0">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *               <totalDigits value="6"/>
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
        "deliveryid",
        "showdetails",
        "status",
        "datefrom",
        "dateto",
        "offset",
        "limit"
    })
    public static class FILTERPARMS {

        /**
         * Für das Liefersystem eindeutige Id zum Identifizieren der Lieferung
         *
         */
        @XmlElement(name = "DELIVERY_ID")
        protected BigDecimal deliveryid;
        /**
         * Sollen Details zu den einzelnen Positionen/Belegen gelesen werden -> "true/1"
         *
         */
        @XmlElement(name = "SHOW_DETAILS", defaultValue = "false")
        protected Boolean showdetails;
        /**
         * Import-Status der Lieferung (0 = Initial, 1 = Erfolg, 2 = Teilerfolg, 3 = Fehler, 4 = Sistiert, 99 = Alle)
         *
         */
        @XmlElement(name = "STATUS", defaultValue = "99")
        protected BigInteger status;
        /**
         * Datum der Übermittlung von (einschl.)
         *
         */
        @XmlElement(name = "DATE_FROM")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar datefrom;
        /**
         * Datum der Übermittlung bis (einschl.)
         *
         */
        @XmlElement(name = "DATE_TO")
        @XmlSchemaType(name = "date")
        protected XMLGregorianCalendar dateto;
        /**
         * Selektion ab Datensatz
         *
         */
        @XmlElement(name = "OFFSET", defaultValue = "0")
        protected BigInteger offset;
        /**
         * Maximale Anzahl an selektierten Datensätzen
         *
         */
        @XmlElement(name = "LIMIT", defaultValue = "100")
        protected BigInteger limit;

        /**
         * Für das Liefersystem eindeutige Id zum Identifizieren der Lieferung
         *
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *
         */
        public BigDecimal getDELIVERYID() {
            return deliveryid;
        }

        /**
         * Sets the value of the deliveryid property.
         *
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *
         * @see #getDELIVERYID()
         */
        public void setDELIVERYID(BigDecimal value) {
            this.deliveryid = value;
        }

        /**
         * Sollen Details zu den einzelnen Positionen/Belegen gelesen werden -> "true/1"
         *
         * @return
         *     possible object is
         *     {@link Boolean }
         *
         */
        public Boolean isSHOWDETAILS() {
            return showdetails;
        }

        /**
         * Sets the value of the showdetails property.
         *
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *
         * @see #isSHOWDETAILS()
         */
        public void setSHOWDETAILS(Boolean value) {
            this.showdetails = value;
        }

        /**
         * Import-Status der Lieferung (0 = Initial, 1 = Erfolg, 2 = Teilerfolg, 3 = Fehler, 4 = Sistiert, 99 = Alle)
         *
         * @return
         *     possible object is
         *     {@link BigInteger }
         *
         */
        public BigInteger getSTATUS() {
            return status;
        }

        /**
         * Sets the value of the status property.
         *
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *
         * @see #getSTATUS()
         */
        public void setSTATUS(BigInteger value) {
            this.status = value;
        }

        /**
         * Datum der Übermittlung von (einschl.)
         *
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *
         */
        public XMLGregorianCalendar getDATEFROM() {
            return datefrom;
        }

        /**
         * Sets the value of the datefrom property.
         *
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *
         * @see #getDATEFROM()
         */
        public void setDATEFROM(XMLGregorianCalendar value) {
            this.datefrom = value;
        }

        /**
         * Datum der Übermittlung bis (einschl.)
         *
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *
         */
        public XMLGregorianCalendar getDATETO() {
            return dateto;
        }

        /**
         * Sets the value of the dateto property.
         *
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *
         * @see #getDATETO()
         */
        public void setDATETO(XMLGregorianCalendar value) {
            this.dateto = value;
        }

        /**
         * Selektion ab Datensatz
         *
         * @return
         *     possible object is
         *     {@link BigInteger }
         *
         */
        public BigInteger getOFFSET() {
            return offset;
        }

        /**
         * Sets the value of the offset property.
         *
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *
         * @see #getOFFSET()
         */
        public void setOFFSET(BigInteger value) {
            this.offset = value;
        }

        /**
         * Maximale Anzahl an selektierten Datensätzen
         *
         * @return
         *     possible object is
         *     {@link BigInteger }
         *
         */
        public BigInteger getLIMIT() {
            return limit;
        }

        /**
         * Sets the value of the limit property.
         *
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *
         * @see #getLIMIT()
         */
        public void setLIMIT(BigInteger value) {
            this.limit = value;
        }

    }

}
