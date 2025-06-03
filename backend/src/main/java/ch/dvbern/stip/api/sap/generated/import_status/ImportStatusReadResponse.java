
package ch.dvbern.stip.api.sap.generated.import_status;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import ch.dvbern.stip.api.sap.generated.general.ReturnCodeID;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;


/**
 * Response für SST-073
 *
 * <p>Java class for ImportStatusRead_Response complex type</p>.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.</p>
 *
 * <pre>{@code
 * <complexType name="ImportStatusRead_Response">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="DELIVERY" maxOccurs="unbounded" minOccurs="0">
 *           <complexType>
 *             <complexContent>
 *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 <sequence>
 *                   <element name="DELIVERY_ID">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                         <totalDigits value="19"/>
 *                         <fractionDigits value="0"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="TYPE">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         <maxLength value="40"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="STATUS">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                         <totalDigits value="3"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="KTEXT">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         <maxLength value="40"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="LAST_ACTION" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *                   <element name="COUNTER_SUCCESS">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                         <totalDigits value="6"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="COUNTER_ERROR">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                         <totalDigits value="6"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="COUNTER_INITIAL">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                         <totalDigits value="6"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="COUNTER_TOTAL">
 *                     <simpleType>
 *                       <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                         <totalDigits value="6"/>
 *                       </restriction>
 *                     </simpleType>
 *                   </element>
 *                   <element name="POSITION" maxOccurs="unbounded" minOccurs="0">
 *                     <complexType>
 *                       <complexContent>
 *                         <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           <sequence>
 *                             <element name="POSID">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
 *                                   <totalDigits value="10"/>
 *                                   <fractionDigits value="0"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="STATUS">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="10"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="SAP_KEY">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="40"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="EXT_KEY">
 *                               <simpleType>
 *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   <maxLength value="40"/>
 *                                 </restriction>
 *                               </simpleType>
 *                             </element>
 *                             <element name="LOGS" maxOccurs="unbounded" minOccurs="0">
 *                               <complexType>
 *                                 <complexContent>
 *                                   <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     <sequence>
 *                                       <element name="DATETIME" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *                                       <element name="TYPE">
 *                                         <simpleType>
 *                                           <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                             <maxLength value="1"/>
 *                                           </restriction>
 *                                         </simpleType>
 *                                       </element>
 *                                       <element name="MSG_NR">
 *                                         <simpleType>
 *                                           <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *                                             <totalDigits value="3"/>
 *                                           </restriction>
 *                                         </simpleType>
 *                                       </element>
 *                                       <element name="MESSAGE">
 *                                         <simpleType>
 *                                           <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                             <maxLength value="220"/>
 *                                           </restriction>
 *                                         </simpleType>
 *                                       </element>
 *                                       <element name="ID" minOccurs="0">
 *                                         <simpleType>
 *                                           <restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                             <maxLength value="20"/>
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
 *                 </sequence>
 *               </restriction>
 *             </complexContent>
 *           </complexType>
 *         </element>
 *         <element name="RETURN_CODE" type="{urn:be.ch:KTBE_ERP_FI:GENERAL}ReturnCodeID" maxOccurs="unbounded" minOccurs="0"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ImportStatusRead_Response", propOrder = {
    "delivery",
    "returncode"
})
public class ImportStatusReadResponse {

    @XmlElement(name = "DELIVERY")
    protected List<DELIVERY> delivery;
    @XmlElement(name = "RETURN_CODE")
    protected List<ReturnCodeID> returncode;

    /**
     * Gets the value of the delivery property.
     *
     * <p>This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the delivery property.</p>
     *
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     * <pre>
     * getDELIVERY().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DELIVERY }
     * </p>
     *
     *
     * @return
     *     The value of the delivery property.
     */
    public List<DELIVERY> getDELIVERY() {
        if (delivery == null) {
            delivery = new ArrayList<>();
        }
        return this.delivery;
    }

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
     * {@link ReturnCodeID }
     * </p>
     *
     *
     * @return
     *     The value of the returncode property.
     */
    public List<ReturnCodeID> getRETURNCODE() {
        if (returncode == null) {
            returncode = new ArrayList<>();
        }
        return this.returncode;
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
     *         <element name="DELIVERY_ID">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *               <totalDigits value="19"/>
     *               <fractionDigits value="0"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="TYPE">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               <maxLength value="40"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="STATUS">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *               <totalDigits value="3"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="KTEXT">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               <maxLength value="40"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="LAST_ACTION" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     *         <element name="COUNTER_SUCCESS">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *               <totalDigits value="6"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="COUNTER_ERROR">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *               <totalDigits value="6"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="COUNTER_INITIAL">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *               <totalDigits value="6"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="COUNTER_TOTAL">
     *           <simpleType>
     *             <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *               <totalDigits value="6"/>
     *             </restriction>
     *           </simpleType>
     *         </element>
     *         <element name="POSITION" maxOccurs="unbounded" minOccurs="0">
     *           <complexType>
     *             <complexContent>
     *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 <sequence>
     *                   <element name="POSID">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
     *                         <totalDigits value="10"/>
     *                         <fractionDigits value="0"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="STATUS">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="10"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="SAP_KEY">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="40"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="EXT_KEY">
     *                     <simpleType>
     *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         <maxLength value="40"/>
     *                       </restriction>
     *                     </simpleType>
     *                   </element>
     *                   <element name="LOGS" maxOccurs="unbounded" minOccurs="0">
     *                     <complexType>
     *                       <complexContent>
     *                         <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           <sequence>
     *                             <element name="DATETIME" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
     *                             <element name="TYPE">
     *                               <simpleType>
     *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   <maxLength value="1"/>
     *                                 </restriction>
     *                               </simpleType>
     *                             </element>
     *                             <element name="MSG_NR">
     *                               <simpleType>
     *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
     *                                   <totalDigits value="3"/>
     *                                 </restriction>
     *                               </simpleType>
     *                             </element>
     *                             <element name="MESSAGE">
     *                               <simpleType>
     *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   <maxLength value="220"/>
     *                                 </restriction>
     *                               </simpleType>
     *                             </element>
     *                             <element name="ID" minOccurs="0">
     *                               <simpleType>
     *                                 <restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                                   <maxLength value="20"/>
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
        "type",
        "status",
        "ktext",
        "lastaction",
        "countersuccess",
        "countererror",
        "counterinitial",
        "countertotal",
        "position"
    })
    public static class DELIVERY {

        /**
         * Für das Liefersystem eindeutige Id zum Identifizieren der Lieferung
         *
         */
        @XmlElement(name = "DELIVERY_ID", required = true)
        protected BigDecimal deliveryid;
        /**
         * Typ der Delivery (SST-NR + Beschreibung)
         *
         */
        @XmlElement(name = "TYPE", required = true)
        protected String type;
        /**
         * Import-Status der Lieferung (0 = Initial, 1 = Erfolg, 2 = Teilerfolg, 3 = Fehler, 4 = Sistiert)
         *
         */
        @XmlElement(name = "STATUS", required = true)
        protected BigInteger status;
        /**
         * Initial/Erfolg/Teilerfolg/Fehler/Sistierung.
         *
         */
        @XmlElement(name = "KTEXT", required = true)
        protected String ktext;
        /**
         * Zeitpunkt der letzten Status-Aktualisierung
         *
         */
        @XmlElement(name = "LAST_ACTION", required = true)
        @XmlSchemaType(name = "dateTime")
        protected XMLGregorianCalendar lastaction;
        /**
         * Anzahl erfolgreich verarbeiteter Positionen
         *
         */
        @XmlElement(name = "COUNTER_SUCCESS", required = true)
        protected BigInteger countersuccess;
        /**
         * Anzahl fehlerhaft verarbeiteter Positionen
         *
         */
        @XmlElement(name = "COUNTER_ERROR", required = true)
        protected BigInteger countererror;
        /**
         * Anzahl noch nicht verarbeiteter Positionen
         *
         */
        @XmlElement(name = "COUNTER_INITIAL", required = true)
        protected BigInteger counterinitial;
        /**
         * Anzahl aller Positionen
         *
         */
        @XmlElement(name = "COUNTER_TOTAL", required = true)
        protected BigInteger countertotal;
        @XmlElement(name = "POSITION")
        protected List<POSITION> position;

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
         * Typ der Delivery (SST-NR + Beschreibung)
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getTYPE() {
            return type;
        }

        /**
         * Sets the value of the type property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         * @see #getTYPE()
         */
        public void setTYPE(String value) {
            this.type = value;
        }

        /**
         * Import-Status der Lieferung (0 = Initial, 1 = Erfolg, 2 = Teilerfolg, 3 = Fehler, 4 = Sistiert)
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
         * Initial/Erfolg/Teilerfolg/Fehler/Sistierung.
         *
         * @return
         *     possible object is
         *     {@link String }
         *
         */
        public String getKTEXT() {
            return ktext;
        }

        /**
         * Sets the value of the ktext property.
         *
         * @param value
         *     allowed object is
         *     {@link String }
         *
         * @see #getKTEXT()
         */
        public void setKTEXT(String value) {
            this.ktext = value;
        }

        /**
         * Zeitpunkt der letzten Status-Aktualisierung
         *
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *
         */
        public XMLGregorianCalendar getLASTACTION() {
            return lastaction;
        }

        /**
         * Sets the value of the lastaction property.
         *
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *
         * @see #getLASTACTION()
         */
        public void setLASTACTION(XMLGregorianCalendar value) {
            this.lastaction = value;
        }

        /**
         * Anzahl erfolgreich verarbeiteter Positionen
         *
         * @return
         *     possible object is
         *     {@link BigInteger }
         *
         */
        public BigInteger getCOUNTERSUCCESS() {
            return countersuccess;
        }

        /**
         * Sets the value of the countersuccess property.
         *
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *
         * @see #getCOUNTERSUCCESS()
         */
        public void setCOUNTERSUCCESS(BigInteger value) {
            this.countersuccess = value;
        }

        /**
         * Anzahl fehlerhaft verarbeiteter Positionen
         *
         * @return
         *     possible object is
         *     {@link BigInteger }
         *
         */
        public BigInteger getCOUNTERERROR() {
            return countererror;
        }

        /**
         * Sets the value of the countererror property.
         *
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *
         * @see #getCOUNTERERROR()
         */
        public void setCOUNTERERROR(BigInteger value) {
            this.countererror = value;
        }

        /**
         * Anzahl noch nicht verarbeiteter Positionen
         *
         * @return
         *     possible object is
         *     {@link BigInteger }
         *
         */
        public BigInteger getCOUNTERINITIAL() {
            return counterinitial;
        }

        /**
         * Sets the value of the counterinitial property.
         *
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *
         * @see #getCOUNTERINITIAL()
         */
        public void setCOUNTERINITIAL(BigInteger value) {
            this.counterinitial = value;
        }

        /**
         * Anzahl aller Positionen
         *
         * @return
         *     possible object is
         *     {@link BigInteger }
         *
         */
        public BigInteger getCOUNTERTOTAL() {
            return countertotal;
        }

        /**
         * Sets the value of the countertotal property.
         *
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *
         * @see #getCOUNTERTOTAL()
         */
        public void setCOUNTERTOTAL(BigInteger value) {
            this.countertotal = value;
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
         * <p>Java class for anonymous complex type</p>.
         *
         * <p>The following schema fragment specifies the expected content contained within this class.</p>
         *
         * <pre>{@code
         * <complexType>
         *   <complexContent>
         *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *       <sequence>
         *         <element name="POSID">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}decimal">
         *               <totalDigits value="10"/>
         *               <fractionDigits value="0"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="STATUS">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="10"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="SAP_KEY">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="40"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="EXT_KEY">
         *           <simpleType>
         *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               <maxLength value="40"/>
         *             </restriction>
         *           </simpleType>
         *         </element>
         *         <element name="LOGS" maxOccurs="unbounded" minOccurs="0">
         *           <complexType>
         *             <complexContent>
         *               <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 <sequence>
         *                   <element name="DATETIME" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
         *                   <element name="TYPE">
         *                     <simpleType>
         *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         <maxLength value="1"/>
         *                       </restriction>
         *                     </simpleType>
         *                   </element>
         *                   <element name="MSG_NR">
         *                     <simpleType>
         *                       <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
         *                         <totalDigits value="3"/>
         *                       </restriction>
         *                     </simpleType>
         *                   </element>
         *                   <element name="MESSAGE">
         *                     <simpleType>
         *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         <maxLength value="220"/>
         *                       </restriction>
         *                     </simpleType>
         *                   </element>
         *                   <element name="ID" minOccurs="0">
         *                     <simpleType>
         *                       <restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *                         <maxLength value="20"/>
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
            "posid",
            "status",
            "sapkey",
            "extkey",
            "logs"
        })
        public static class POSITION {

            /**
             * Positions-ID des Belegs, wird von SAP intern aufsteigend vergeben
             *
             */
            @XmlElement(name = "POSID", required = true)
            protected BigDecimal posid;
            /**
             * Status des Belegs: INITIAL, POSTING, RETRY, SUCCESS, ERROR, SUSPENDED
             *
             */
            @XmlElement(name = "STATUS", required = true)
            protected String status;
            /**
             * SAP-Schlüssel zum Objekt (falls schon eingespielt)
             *
             */
            @XmlElement(name = "SAP_KEY", required = true)
            protected String sapkey;
            /**
             * Externer Schlüssel der Fachapplikation
             *
             */
            @XmlElement(name = "EXT_KEY", required = true)
            protected String extkey;
            @XmlElement(name = "LOGS")
            protected List<LOGS> logs;

            /**
             * Positions-ID des Belegs, wird von SAP intern aufsteigend vergeben
             *
             * @return
             *     possible object is
             *     {@link BigDecimal }
             *
             */
            public BigDecimal getPOSID() {
                return posid;
            }

            /**
             * Sets the value of the posid property.
             *
             * @param value
             *     allowed object is
             *     {@link BigDecimal }
             *
             * @see #getPOSID()
             */
            public void setPOSID(BigDecimal value) {
                this.posid = value;
            }

            /**
             * Status des Belegs: INITIAL, POSTING, RETRY, SUCCESS, ERROR, SUSPENDED
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getSTATUS() {
                return status;
            }

            /**
             * Sets the value of the status property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getSTATUS()
             */
            public void setSTATUS(String value) {
                this.status = value;
            }

            /**
             * SAP-Schlüssel zum Objekt (falls schon eingespielt)
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getSAPKEY() {
                return sapkey;
            }

            /**
             * Sets the value of the sapkey property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getSAPKEY()
             */
            public void setSAPKEY(String value) {
                this.sapkey = value;
            }

            /**
             * Externer Schlüssel der Fachapplikation
             *
             * @return
             *     possible object is
             *     {@link String }
             *
             */
            public String getEXTKEY() {
                return extkey;
            }

            /**
             * Sets the value of the extkey property.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             * @see #getEXTKEY()
             */
            public void setEXTKEY(String value) {
                this.extkey = value;
            }

            /**
             * Gets the value of the logs property.
             *
             * <p>This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the logs property.</p>
             *
             * <p>
             * For example, to add a new item, do as follows:
             * </p>
             * <pre>
             * getLOGS().add(newItem);
             * </pre>
             *
             *
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link LOGS }
             * </p>
             *
             *
             * @return
             *     The value of the logs property.
             */
            public List<LOGS> getLOGS() {
                if (logs == null) {
                    logs = new ArrayList<>();
                }
                return this.logs;
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
             *         <element name="DATETIME" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
             *         <element name="TYPE">
             *           <simpleType>
             *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               <maxLength value="1"/>
             *             </restriction>
             *           </simpleType>
             *         </element>
             *         <element name="MSG_NR">
             *           <simpleType>
             *             <restriction base="{http://www.w3.org/2001/XMLSchema}integer">
             *               <totalDigits value="3"/>
             *             </restriction>
             *           </simpleType>
             *         </element>
             *         <element name="MESSAGE">
             *           <simpleType>
             *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               <maxLength value="220"/>
             *             </restriction>
             *           </simpleType>
             *         </element>
             *         <element name="ID" minOccurs="0">
             *           <simpleType>
             *             <restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *               <maxLength value="20"/>
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
                "datetime",
                "type",
                "msgnr",
                "message",
                "id"
            })
            public static class LOGS {

                /**
                 * Zeitpunkt des Logs
                 *
                 */
                @XmlElement(name = "DATETIME", required = true)
                @XmlSchemaType(name = "dateTime")
                protected XMLGregorianCalendar datetime;
                /**
                 * S = Success, W = Warning, E = Error, I = Info
                 *
                 */
                @XmlElement(name = "TYPE", required = true)
                protected String type;
                /**
                 * ID/Nummer der Nachricht
                 *
                 */
                @XmlElement(name = "MSG_NR", required = true)
                protected BigInteger msgnr;
                /**
                 * MESSAGE Element xsd:string 1  false maxLength="220"  Text der Nachricht
                 *
                 */
                @XmlElement(name = "MESSAGE", required = true)
                protected String message;
                @XmlElement(name = "ID")
                protected String id;

                /**
                 * Zeitpunkt des Logs
                 *
                 * @return
                 *     possible object is
                 *     {@link XMLGregorianCalendar }
                 *
                 */
                public XMLGregorianCalendar getDATETIME() {
                    return datetime;
                }

                /**
                 * Sets the value of the datetime property.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link XMLGregorianCalendar }
                 *
                 * @see #getDATETIME()
                 */
                public void setDATETIME(XMLGregorianCalendar value) {
                    this.datetime = value;
                }

                /**
                 * S = Success, W = Warning, E = Error, I = Info
                 *
                 * @return
                 *     possible object is
                 *     {@link String }
                 *
                 */
                public String getTYPE() {
                    return type;
                }

                /**
                 * Sets the value of the type property.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *
                 * @see #getTYPE()
                 */
                public void setTYPE(String value) {
                    this.type = value;
                }

                /**
                 * ID/Nummer der Nachricht
                 *
                 * @return
                 *     possible object is
                 *     {@link BigInteger }
                 *
                 */
                public BigInteger getMSGNR() {
                    return msgnr;
                }

                /**
                 * Sets the value of the msgnr property.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link BigInteger }
                 *
                 * @see #getMSGNR()
                 */
                public void setMSGNR(BigInteger value) {
                    this.msgnr = value;
                }

                /**
                 * MESSAGE Element xsd:string 1  false maxLength="220"  Text der Nachricht
                 *
                 * @return
                 *     possible object is
                 *     {@link String }
                 *
                 */
                public String getMESSAGE() {
                    return message;
                }

                /**
                 * Sets the value of the message property.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *
                 * @see #getMESSAGE()
                 */
                public void setMESSAGE(String value) {
                    this.message = value;
                }

                /**
                 * Gets the value of the id property.
                 *
                 * @return
                 *     possible object is
                 *     {@link String }
                 *
                 */
                public String getID() {
                    return id;
                }

                /**
                 * Sets the value of the id property.
                 *
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *
                 */
                public void setID(String value) {
                    this.id = value;
                }

            }

        }

    }

}
