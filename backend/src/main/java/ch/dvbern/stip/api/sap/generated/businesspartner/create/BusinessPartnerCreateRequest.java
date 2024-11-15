
package ch.dvbern.stip.api.sap.generated.businesspartner.create;

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
 * <p>Java-Klasse für BusinessPartnerCreate_Request complex type.
 *
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 *
 * <pre>
 * &lt;complexType name="BusinessPartnerCreate_Request"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="SENDER" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}SenderParmsDelivery"/&gt;
 *         &lt;element name="BUSINESS_PARTNER"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="HEADER"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="PARTN_CAT"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;enumeration value="1"/&gt;
 *                                   &lt;enumeration value="2"/&gt;
 *                                   &lt;maxLength value="10"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="PARTN_TYP" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="4"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="PARTN_GRP" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;enumeration value="ZBPE"/&gt;
 *                                   &lt;maxLength value="4"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="PARTN_TAXKD" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="1"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="ID_KEYS"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="EXT_ID"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="20"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="AHV_NR" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="13"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="UID_NR" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="12"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="ZPV_NR" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="10"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="PERS_DATA" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="FIRSTNAME"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="40"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="LASTNAME"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="40"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="CORRESPONDLANGUAGEISO"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="2"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="BIRTHNAME" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="40"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="MIDDLENAME" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="40"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="SECONDNAME" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="40"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="TITLE_ACA1" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;enumeration value="0001"/&gt;
 *                                   &lt;enumeration value="0002"/&gt;
 *                                   &lt;enumeration value="0003"/&gt;
 *                                   &lt;enumeration value="0004"/&gt;
 *                                   &lt;enumeration value="0005"/&gt;
 *                                   &lt;enumeration value="0006"/&gt;
 *                                   &lt;maxLength value="4"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="TITLE_ACA2" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="4"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="TITLE_SPPL" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="4"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="PREFIX1" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="4"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="PREFIX2" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="4"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="NICKNAME" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="40"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="INITIALS" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="10"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="NAMEFORMAT" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="2"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="NAMCOUNTRY" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="3"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="NAMCOUNTRYISO" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="2"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="SEX" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;enumeration value="1"/&gt;
 *                                   &lt;enumeration value="2"/&gt;
 *                                   &lt;maxLength value="1"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="BIRTHPLACE" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="40"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="BIRTHDATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                             &lt;element name="DEATHDATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                             &lt;element name="MARITALSTATUS" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="1"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="CORRESPONDLANGUAGE" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="1"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="FULLNAME" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="80"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="EMPLOYER" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="35"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="OCCUPATION" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="4"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="NATIONALITY" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="3"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="NATIONALITYISO" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="2"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="COUNTRYORIGIN" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="3"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="BIRTHDT_STATUS" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="1"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="ORG_DATA" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="NAME1"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="40"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="NAME2" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="40"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="NAME3" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="40"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="NAME4" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="40"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="LANGU_ISO"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="2"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="LEGALFORM" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="2"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="INDUSTRYSECTOR" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="10"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="FOUNDATIONDATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                             &lt;element name="LIQUIDATIONDATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                             &lt;element name="LOC_NO_1" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="7"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="LOC_NO_2" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="5"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="CHK_DIGIT" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="1"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="LEGALORG" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="2"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="ADDRESS" maxOccurs="unbounded"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="ADR_KIND"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;minLength value="1"/&gt;
 *                                   &lt;maxLength value="10"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="C_O_NAME" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="40"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="CITY"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;minLength value="1"/&gt;
 *                                   &lt;maxLength value="40"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="DISTRICT" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="40"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="POSTL_COD1"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;minLength value="1"/&gt;
 *                                   &lt;maxLength value="10"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="PO_BOX" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="10"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="STREET"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;minLength value="1"/&gt;
 *                                   &lt;maxLength value="40"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="HOUSE_NO" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="10"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="COUNTRY"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="3"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="POSTL_COD2" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="10"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="COMMUNICATION" maxOccurs="unbounded" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;complexContent&gt;
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                           &lt;sequence&gt;
 *                             &lt;element name="EMAIL" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="241"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="TELEPHONE" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="30"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="MOBILE" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="30"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                             &lt;element name="FAX" minOccurs="0"&gt;
 *                               &lt;simpleType&gt;
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *                                   &lt;maxLength value="30"/&gt;
 *                                 &lt;/restriction&gt;
 *                               &lt;/simpleType&gt;
 *                             &lt;/element&gt;
 *                           &lt;/sequence&gt;
 *                         &lt;/restriction&gt;
 *                       &lt;/complexContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                   &lt;element name="PAYMENT_DETAIL" maxOccurs="unbounded" minOccurs="0"&gt;
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
 *                             &lt;element name="BANKDETAILVALIDFROM" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
 *                             &lt;element name="BANKDETAILVALIDTO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
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
 *                   &lt;element name="ADDITIONAL_DATA" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}AdditionalData" maxOccurs="unbounded" minOccurs="0"/&gt;
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
@XmlRootElement(name = "BusinessPartnerCreate_Request", namespace = "urn:be.ch:KTBE_MDG:BUSINESS_PARTNER")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BusinessPartnerCreate_Request", namespace = "urn:be.ch:KTBE_MDG:BUSINESS_PARTNER", propOrder = {
    "sender",
    "businesspartner"
})
public class BusinessPartnerCreateRequest {

    @XmlElement(name = "SENDER", required = true)
    protected SenderParmsDelivery sender;
    @XmlElement(name = "BUSINESS_PARTNER", required = true)
    protected BusinessPartnerCreateRequest.BUSINESSPARTNER businesspartner;

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
     * Ruft den Wert der businesspartner-Eigenschaft ab.
     *
     * @return
     *     possible object is
     *     {@link BusinessPartnerCreateRequest.BUSINESSPARTNER }
     *
     */
    public BusinessPartnerCreateRequest.BUSINESSPARTNER getBUSINESSPARTNER() {
        return businesspartner;
    }

    /**
     * Legt den Wert der businesspartner-Eigenschaft fest.
     *
     * @param value
     *     allowed object is
     *     {@link BusinessPartnerCreateRequest.BUSINESSPARTNER }
     *
     */
    public void setBUSINESSPARTNER(BusinessPartnerCreateRequest.BUSINESSPARTNER value) {
        this.businesspartner = value;
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
     *                   &lt;element name="PARTN_CAT"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;enumeration value="1"/&gt;
     *                         &lt;enumeration value="2"/&gt;
     *                         &lt;maxLength value="10"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="PARTN_TYP" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="4"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="PARTN_GRP" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;enumeration value="ZBPE"/&gt;
     *                         &lt;maxLength value="4"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="PARTN_TAXKD" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="1"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="ID_KEYS"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="EXT_ID"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="20"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="AHV_NR" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="13"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="UID_NR" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="12"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="ZPV_NR" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="10"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="PERS_DATA" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="FIRSTNAME"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="40"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="LASTNAME"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="40"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="CORRESPONDLANGUAGEISO"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="2"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="BIRTHNAME" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="40"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="MIDDLENAME" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="40"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="SECONDNAME" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="40"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="TITLE_ACA1" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;enumeration value="0001"/&gt;
     *                         &lt;enumeration value="0002"/&gt;
     *                         &lt;enumeration value="0003"/&gt;
     *                         &lt;enumeration value="0004"/&gt;
     *                         &lt;enumeration value="0005"/&gt;
     *                         &lt;enumeration value="0006"/&gt;
     *                         &lt;maxLength value="4"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="TITLE_ACA2" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="4"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="TITLE_SPPL" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="4"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="PREFIX1" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="4"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="PREFIX2" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="4"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="NICKNAME" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="40"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="INITIALS" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="10"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="NAMEFORMAT" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="2"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="NAMCOUNTRY" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="3"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="NAMCOUNTRYISO" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="2"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="SEX" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;enumeration value="1"/&gt;
     *                         &lt;enumeration value="2"/&gt;
     *                         &lt;maxLength value="1"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="BIRTHPLACE" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="40"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="BIRTHDATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *                   &lt;element name="DEATHDATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *                   &lt;element name="MARITALSTATUS" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="1"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="CORRESPONDLANGUAGE" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="1"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="FULLNAME" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="80"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="EMPLOYER" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="35"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="OCCUPATION" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="4"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="NATIONALITY" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="3"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="NATIONALITYISO" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="2"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="COUNTRYORIGIN" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="3"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="BIRTHDT_STATUS" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="1"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="ORG_DATA" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="NAME1"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="40"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="NAME2" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="40"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="NAME3" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="40"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="NAME4" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="40"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="LANGU_ISO"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="2"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="LEGALFORM" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="2"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="INDUSTRYSECTOR" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="10"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="FOUNDATIONDATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *                   &lt;element name="LIQUIDATIONDATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *                   &lt;element name="LOC_NO_1" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="7"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="LOC_NO_2" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="5"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="CHK_DIGIT" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="1"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="LEGALORG" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="2"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="ADDRESS" maxOccurs="unbounded"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="ADR_KIND"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;minLength value="1"/&gt;
     *                         &lt;maxLength value="10"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="C_O_NAME" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="40"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="CITY"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;minLength value="1"/&gt;
     *                         &lt;maxLength value="40"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="DISTRICT" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="40"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="POSTL_COD1"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;minLength value="1"/&gt;
     *                         &lt;maxLength value="10"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="PO_BOX" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="10"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="STREET"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;minLength value="1"/&gt;
     *                         &lt;maxLength value="40"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="HOUSE_NO" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="10"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="COUNTRY"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="3"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="POSTL_COD2" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="10"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="COMMUNICATION" maxOccurs="unbounded" minOccurs="0"&gt;
     *           &lt;complexType&gt;
     *             &lt;complexContent&gt;
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
     *                 &lt;sequence&gt;
     *                   &lt;element name="EMAIL" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="241"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="TELEPHONE" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="30"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="MOBILE" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="30"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                   &lt;element name="FAX" minOccurs="0"&gt;
     *                     &lt;simpleType&gt;
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
     *                         &lt;maxLength value="30"/&gt;
     *                       &lt;/restriction&gt;
     *                     &lt;/simpleType&gt;
     *                   &lt;/element&gt;
     *                 &lt;/sequence&gt;
     *               &lt;/restriction&gt;
     *             &lt;/complexContent&gt;
     *           &lt;/complexType&gt;
     *         &lt;/element&gt;
     *         &lt;element name="PAYMENT_DETAIL" maxOccurs="unbounded" minOccurs="0"&gt;
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
     *                   &lt;element name="BANKDETAILVALIDFROM" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
     *                   &lt;element name="BANKDETAILVALIDTO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
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
     *         &lt;element name="ADDITIONAL_DATA" type="{urn:be.ch:KTBE_MDG:BUSINESS_PARTNER}AdditionalData" maxOccurs="unbounded" minOccurs="0"/&gt;
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
        protected BusinessPartnerCreateRequest.BUSINESSPARTNER.HEADER header;
        @XmlElement(name = "ID_KEYS", required = true)
        protected BusinessPartnerCreateRequest.BUSINESSPARTNER.IDKEYS idkeys;
        @XmlElement(name = "PERS_DATA")
        protected BusinessPartnerCreateRequest.BUSINESSPARTNER.PERSDATA persdata;
        @XmlElement(name = "ORG_DATA")
        protected BusinessPartnerCreateRequest.BUSINESSPARTNER.ORGDATA orgdata;
        @XmlElement(name = "ADDRESS", required = true)
        protected List<BusinessPartnerCreateRequest.BUSINESSPARTNER.ADDRESS> address;
        @XmlElement(name = "COMMUNICATION")
        protected List<BusinessPartnerCreateRequest.BUSINESSPARTNER.COMMUNICATION> communication;
        @XmlElement(name = "PAYMENT_DETAIL")
        protected List<BusinessPartnerCreateRequest.BUSINESSPARTNER.PAYMENTDETAIL> paymentdetail;
        @XmlElement(name = "ADDITIONAL_DATA")
        protected List<AdditionalData> additionaldata;

        /**
         * Ruft den Wert der header-Eigenschaft ab.
         *
         * @return
         *     possible object is
         *     {@link BusinessPartnerCreateRequest.BUSINESSPARTNER.HEADER }
         *
         */
        public BusinessPartnerCreateRequest.BUSINESSPARTNER.HEADER getHEADER() {
            return header;
        }

        /**
         * Legt den Wert der header-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link BusinessPartnerCreateRequest.BUSINESSPARTNER.HEADER }
         *
         */
        public void setHEADER(BusinessPartnerCreateRequest.BUSINESSPARTNER.HEADER value) {
            this.header = value;
        }

        /**
         * Ruft den Wert der idkeys-Eigenschaft ab.
         *
         * @return
         *     possible object is
         *     {@link BusinessPartnerCreateRequest.BUSINESSPARTNER.IDKEYS }
         *
         */
        public BusinessPartnerCreateRequest.BUSINESSPARTNER.IDKEYS getIDKEYS() {
            return idkeys;
        }

        /**
         * Legt den Wert der idkeys-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link BusinessPartnerCreateRequest.BUSINESSPARTNER.IDKEYS }
         *
         */
        public void setIDKEYS(BusinessPartnerCreateRequest.BUSINESSPARTNER.IDKEYS value) {
            this.idkeys = value;
        }

        /**
         * Ruft den Wert der persdata-Eigenschaft ab.
         *
         * @return
         *     possible object is
         *     {@link BusinessPartnerCreateRequest.BUSINESSPARTNER.PERSDATA }
         *
         */
        public BusinessPartnerCreateRequest.BUSINESSPARTNER.PERSDATA getPERSDATA() {
            return persdata;
        }

        /**
         * Legt den Wert der persdata-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link BusinessPartnerCreateRequest.BUSINESSPARTNER.PERSDATA }
         *
         */
        public void setPERSDATA(BusinessPartnerCreateRequest.BUSINESSPARTNER.PERSDATA value) {
            this.persdata = value;
        }

        /**
         * Ruft den Wert der orgdata-Eigenschaft ab.
         *
         * @return
         *     possible object is
         *     {@link BusinessPartnerCreateRequest.BUSINESSPARTNER.ORGDATA }
         *
         */
        public BusinessPartnerCreateRequest.BUSINESSPARTNER.ORGDATA getORGDATA() {
            return orgdata;
        }

        /**
         * Legt den Wert der orgdata-Eigenschaft fest.
         *
         * @param value
         *     allowed object is
         *     {@link BusinessPartnerCreateRequest.BUSINESSPARTNER.ORGDATA }
         *
         */
        public void setORGDATA(BusinessPartnerCreateRequest.BUSINESSPARTNER.ORGDATA value) {
            this.orgdata = value;
        }

        /**
         * Gets the value of the address property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the Jakarta XML Binding object.
         * This is why there is not a <CODE>set</CODE> method for the address property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getADDRESS().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link BusinessPartnerCreateRequest.BUSINESSPARTNER.ADDRESS }
         *
         *
         */
        public List<BusinessPartnerCreateRequest.BUSINESSPARTNER.ADDRESS> getADDRESS() {
            if (address == null) {
                address = new ArrayList<BusinessPartnerCreateRequest.BUSINESSPARTNER.ADDRESS>();
            }
            return this.address;
        }

        /**
         * Gets the value of the communication property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the Jakarta XML Binding object.
         * This is why there is not a <CODE>set</CODE> method for the communication property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCOMMUNICATION().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link BusinessPartnerCreateRequest.BUSINESSPARTNER.COMMUNICATION }
         *
         *
         */
        public List<BusinessPartnerCreateRequest.BUSINESSPARTNER.COMMUNICATION> getCOMMUNICATION() {
            if (communication == null) {
                communication = new ArrayList<BusinessPartnerCreateRequest.BUSINESSPARTNER.COMMUNICATION>();
            }
            return this.communication;
        }

        /**
         * Gets the value of the paymentdetail property.
         *
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the Jakarta XML Binding object.
         * This is why there is not a <CODE>set</CODE> method for the paymentdetail property.
         *
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getPAYMENTDETAIL().add(newItem);
         * </pre>
         *
         *
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link BusinessPartnerCreateRequest.BUSINESSPARTNER.PAYMENTDETAIL }
         *
         *
         */
        public List<BusinessPartnerCreateRequest.BUSINESSPARTNER.PAYMENTDETAIL> getPAYMENTDETAIL() {
            if (paymentdetail == null) {
                paymentdetail = new ArrayList<BusinessPartnerCreateRequest.BUSINESSPARTNER.PAYMENTDETAIL>();
            }
            return this.paymentdetail;
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
         *         &lt;element name="ADR_KIND"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;minLength value="1"/&gt;
         *               &lt;maxLength value="10"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="C_O_NAME" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="40"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="CITY"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;minLength value="1"/&gt;
         *               &lt;maxLength value="40"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="DISTRICT" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="40"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="POSTL_COD1"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;minLength value="1"/&gt;
         *               &lt;maxLength value="10"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="PO_BOX" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="10"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="STREET"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;minLength value="1"/&gt;
         *               &lt;maxLength value="40"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="HOUSE_NO" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="10"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="COUNTRY"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="3"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="POSTL_COD2" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="10"/&gt;
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

            @XmlElement(name = "ADR_KIND", required = true, defaultValue = "XXDEFAULT")
            protected String adrkind;
            @XmlElement(name = "C_O_NAME")
            protected String coname;
            @XmlElement(name = "CITY", required = true)
            protected String city;
            @XmlElement(name = "DISTRICT")
            protected String district;
            @XmlElement(name = "POSTL_COD1", required = true)
            protected String postlcod1;
            @XmlElement(name = "PO_BOX")
            protected String pobox;
            @XmlElement(name = "STREET", required = true)
            protected String street;
            @XmlElement(name = "HOUSE_NO")
            protected String houseno;
            @XmlElement(name = "COUNTRY", required = true)
            protected String country;
            @XmlElement(name = "POSTL_COD2")
            protected String postlcod2;

            /**
             * Ruft den Wert der adrkind-Eigenschaft ab.
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
             * Legt den Wert der adrkind-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setADRKIND(String value) {
                this.adrkind = value;
            }

            /**
             * Ruft den Wert der coname-Eigenschaft ab.
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
             * Legt den Wert der coname-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setCONAME(String value) {
                this.coname = value;
            }

            /**
             * Ruft den Wert der city-Eigenschaft ab.
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
             * Legt den Wert der city-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setCITY(String value) {
                this.city = value;
            }

            /**
             * Ruft den Wert der district-Eigenschaft ab.
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
             * Legt den Wert der district-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setDISTRICT(String value) {
                this.district = value;
            }

            /**
             * Ruft den Wert der postlcod1-Eigenschaft ab.
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
             * Legt den Wert der postlcod1-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setPOSTLCOD1(String value) {
                this.postlcod1 = value;
            }

            /**
             * Ruft den Wert der pobox-Eigenschaft ab.
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
             * Legt den Wert der pobox-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setPOBOX(String value) {
                this.pobox = value;
            }

            /**
             * Ruft den Wert der street-Eigenschaft ab.
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
             * Legt den Wert der street-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setSTREET(String value) {
                this.street = value;
            }

            /**
             * Ruft den Wert der houseno-Eigenschaft ab.
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
             * Legt den Wert der houseno-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setHOUSENO(String value) {
                this.houseno = value;
            }

            /**
             * Ruft den Wert der country-Eigenschaft ab.
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
             * Legt den Wert der country-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setCOUNTRY(String value) {
                this.country = value;
            }

            /**
             * Ruft den Wert der postlcod2-Eigenschaft ab.
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
             * Legt den Wert der postlcod2-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setPOSTLCOD2(String value) {
                this.postlcod2 = value;
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
         *         &lt;element name="EMAIL" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="241"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="TELEPHONE" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="30"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="MOBILE" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="30"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="FAX" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="30"/&gt;
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
            "email",
            "telephone",
            "mobile",
            "fax"
        })
        public static class COMMUNICATION {

            @XmlElement(name = "EMAIL")
            protected String email;
            @XmlElement(name = "TELEPHONE")
            protected String telephone;
            @XmlElement(name = "MOBILE")
            protected String mobile;
            @XmlElement(name = "FAX")
            protected String fax;

            /**
             * Ruft den Wert der email-Eigenschaft ab.
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
             * Legt den Wert der email-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setEMAIL(String value) {
                this.email = value;
            }

            /**
             * Ruft den Wert der telephone-Eigenschaft ab.
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
             * Legt den Wert der telephone-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setTELEPHONE(String value) {
                this.telephone = value;
            }

            /**
             * Ruft den Wert der mobile-Eigenschaft ab.
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
             * Legt den Wert der mobile-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setMOBILE(String value) {
                this.mobile = value;
            }

            /**
             * Ruft den Wert der fax-Eigenschaft ab.
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
             * Legt den Wert der fax-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setFAX(String value) {
                this.fax = value;
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
         *         &lt;element name="PARTN_CAT"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;enumeration value="1"/&gt;
         *               &lt;enumeration value="2"/&gt;
         *               &lt;maxLength value="10"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="PARTN_TYP" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="4"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="PARTN_GRP" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;enumeration value="ZBPE"/&gt;
         *               &lt;maxLength value="4"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="PARTN_TAXKD" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="1"/&gt;
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
            "partncat",
            "partntyp",
            "partngrp",
            "partntaxkd"
        })
        public static class HEADER {

            @XmlElement(name = "PARTN_CAT", required = true, defaultValue = "1")
            protected String partncat;
            @XmlElement(name = "PARTN_TYP")
            protected String partntyp;
            @XmlElement(name = "PARTN_GRP", defaultValue = "ZBPE")
            protected String partngrp;
            @XmlElement(name = "PARTN_TAXKD", defaultValue = "1")
            protected String partntaxkd;

            /**
             * Ruft den Wert der partncat-Eigenschaft ab.
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
             * Legt den Wert der partncat-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setPARTNCAT(String value) {
                this.partncat = value;
            }

            /**
             * Ruft den Wert der partntyp-Eigenschaft ab.
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
             * Legt den Wert der partntyp-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setPARTNTYP(String value) {
                this.partntyp = value;
            }

            /**
             * Ruft den Wert der partngrp-Eigenschaft ab.
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
             * Legt den Wert der partngrp-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setPARTNGRP(String value) {
                this.partngrp = value;
            }

            /**
             * Ruft den Wert der partntaxkd-Eigenschaft ab.
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
             * Legt den Wert der partntaxkd-Eigenschaft fest.
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
         * <p>Java-Klasse für anonymous complex type.
         *
         * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
         *
         * <pre>
         * &lt;complexType&gt;
         *   &lt;complexContent&gt;
         *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
         *       &lt;sequence&gt;
         *         &lt;element name="EXT_ID"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="20"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="AHV_NR" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="13"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="UID_NR" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="12"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="ZPV_NR" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="10"/&gt;
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
            "extid",
            "ahvnr",
            "uidnr",
            "zpvnr"
        })
        public static class IDKEYS {

            @XmlElement(name = "EXT_ID", required = true)
            protected String extid;
            @XmlElement(name = "AHV_NR")
            protected String ahvnr;
            @XmlElement(name = "UID_NR")
            protected String uidnr;
            @XmlElement(name = "ZPV_NR")
            protected String zpvnr;

            /**
             * Ruft den Wert der extid-Eigenschaft ab.
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
             * Legt den Wert der extid-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setEXTID(String value) {
                this.extid = value;
            }

            /**
             * Ruft den Wert der ahvnr-Eigenschaft ab.
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
             * Legt den Wert der ahvnr-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setAHVNR(String value) {
                this.ahvnr = value;
            }

            /**
             * Ruft den Wert der uidnr-Eigenschaft ab.
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
             * Legt den Wert der uidnr-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setUIDNR(String value) {
                this.uidnr = value;
            }

            /**
             * Ruft den Wert der zpvnr-Eigenschaft ab.
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
             * Legt den Wert der zpvnr-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setZPVNR(String value) {
                this.zpvnr = value;
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
         *         &lt;element name="NAME1"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="40"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="NAME2" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="40"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="NAME3" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="40"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="NAME4" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="40"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="LANGU_ISO"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="2"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="LEGALFORM" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="2"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="INDUSTRYSECTOR" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="10"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="FOUNDATIONDATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
         *         &lt;element name="LIQUIDATIONDATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
         *         &lt;element name="LOC_NO_1" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="7"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="LOC_NO_2" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="5"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="CHK_DIGIT" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="1"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="LEGALORG" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="2"/&gt;
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

            @XmlElement(name = "NAME1", required = true)
            protected String name1;
            @XmlElement(name = "NAME2")
            protected String name2;
            @XmlElement(name = "NAME3")
            protected String name3;
            @XmlElement(name = "NAME4")
            protected String name4;
            @XmlElement(name = "LANGU_ISO", required = true, defaultValue = "DE")
            protected String languiso;
            @XmlElement(name = "LEGALFORM")
            protected String legalform;
            @XmlElement(name = "INDUSTRYSECTOR")
            protected String industrysector;
            @XmlElement(name = "FOUNDATIONDATE")
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar foundationdate;
            @XmlElement(name = "LIQUIDATIONDATE")
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar liquidationdate;
            @XmlElement(name = "LOC_NO_1")
            protected String locno1;
            @XmlElement(name = "LOC_NO_2")
            protected String locno2;
            @XmlElement(name = "CHK_DIGIT")
            protected String chkdigit;
            @XmlElement(name = "LEGALORG")
            protected String legalorg;

            /**
             * Ruft den Wert der name1-Eigenschaft ab.
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
             * Legt den Wert der name1-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setNAME1(String value) {
                this.name1 = value;
            }

            /**
             * Ruft den Wert der name2-Eigenschaft ab.
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
             * Legt den Wert der name2-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setNAME2(String value) {
                this.name2 = value;
            }

            /**
             * Ruft den Wert der name3-Eigenschaft ab.
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
             * Legt den Wert der name3-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setNAME3(String value) {
                this.name3 = value;
            }

            /**
             * Ruft den Wert der name4-Eigenschaft ab.
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
             * Legt den Wert der name4-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setNAME4(String value) {
                this.name4 = value;
            }

            /**
             * Ruft den Wert der languiso-Eigenschaft ab.
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
             * Legt den Wert der languiso-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setLANGUISO(String value) {
                this.languiso = value;
            }

            /**
             * Ruft den Wert der legalform-Eigenschaft ab.
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
             * Legt den Wert der legalform-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setLEGALFORM(String value) {
                this.legalform = value;
            }

            /**
             * Ruft den Wert der industrysector-Eigenschaft ab.
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
             * Legt den Wert der industrysector-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setINDUSTRYSECTOR(String value) {
                this.industrysector = value;
            }

            /**
             * Ruft den Wert der foundationdate-Eigenschaft ab.
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
             * Legt den Wert der foundationdate-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *
             */
            public void setFOUNDATIONDATE(XMLGregorianCalendar value) {
                this.foundationdate = value;
            }

            /**
             * Ruft den Wert der liquidationdate-Eigenschaft ab.
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
             * Legt den Wert der liquidationdate-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *
             */
            public void setLIQUIDATIONDATE(XMLGregorianCalendar value) {
                this.liquidationdate = value;
            }

            /**
             * Ruft den Wert der locno1-Eigenschaft ab.
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
             * Legt den Wert der locno1-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setLOCNO1(String value) {
                this.locno1 = value;
            }

            /**
             * Ruft den Wert der locno2-Eigenschaft ab.
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
             * Legt den Wert der locno2-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setLOCNO2(String value) {
                this.locno2 = value;
            }

            /**
             * Ruft den Wert der chkdigit-Eigenschaft ab.
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
             * Legt den Wert der chkdigit-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setCHKDIGIT(String value) {
                this.chkdigit = value;
            }

            /**
             * Ruft den Wert der legalorg-Eigenschaft ab.
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
             * Legt den Wert der legalorg-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setLEGALORG(String value) {
                this.legalorg = value;
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
         *         &lt;element name="IBAN"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="34"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="BANKDETAILVALIDFROM" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
         *         &lt;element name="BANKDETAILVALIDTO" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
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
            "bankdetailvalidfrom",
            "bankdetailvalidto",
            "accountholder"
        })
        public static class PAYMENTDETAIL {

            @XmlElement(name = "IBAN", required = true)
            protected String iban;
            @XmlElement(name = "BANKDETAILVALIDFROM")
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar bankdetailvalidfrom;
            @XmlElement(name = "BANKDETAILVALIDTO")
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar bankdetailvalidto;
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
             * Ruft den Wert der bankdetailvalidfrom-Eigenschaft ab.
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
             * Legt den Wert der bankdetailvalidfrom-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *
             */
            public void setBANKDETAILVALIDFROM(XMLGregorianCalendar value) {
                this.bankdetailvalidfrom = value;
            }

            /**
             * Ruft den Wert der bankdetailvalidto-Eigenschaft ab.
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
             * Legt den Wert der bankdetailvalidto-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *
             */
            public void setBANKDETAILVALIDTO(XMLGregorianCalendar value) {
                this.bankdetailvalidto = value;
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
         *         &lt;element name="FIRSTNAME"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="40"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="LASTNAME"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="40"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="CORRESPONDLANGUAGEISO"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="2"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="BIRTHNAME" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="40"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="MIDDLENAME" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="40"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="SECONDNAME" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="40"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="TITLE_ACA1" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;enumeration value="0001"/&gt;
         *               &lt;enumeration value="0002"/&gt;
         *               &lt;enumeration value="0003"/&gt;
         *               &lt;enumeration value="0004"/&gt;
         *               &lt;enumeration value="0005"/&gt;
         *               &lt;enumeration value="0006"/&gt;
         *               &lt;maxLength value="4"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="TITLE_ACA2" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="4"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="TITLE_SPPL" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="4"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="PREFIX1" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="4"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="PREFIX2" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="4"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="NICKNAME" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="40"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="INITIALS" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="10"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="NAMEFORMAT" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="2"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="NAMCOUNTRY" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="3"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="NAMCOUNTRYISO" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="2"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="SEX" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;enumeration value="1"/&gt;
         *               &lt;enumeration value="2"/&gt;
         *               &lt;maxLength value="1"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="BIRTHPLACE" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="40"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="BIRTHDATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
         *         &lt;element name="DEATHDATE" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/&gt;
         *         &lt;element name="MARITALSTATUS" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="1"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="CORRESPONDLANGUAGE" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="1"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="FULLNAME" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="80"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="EMPLOYER" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="35"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="OCCUPATION" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="4"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="NATIONALITY" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="3"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="NATIONALITYISO" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="2"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="COUNTRYORIGIN" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="3"/&gt;
         *             &lt;/restriction&gt;
         *           &lt;/simpleType&gt;
         *         &lt;/element&gt;
         *         &lt;element name="BIRTHDT_STATUS" minOccurs="0"&gt;
         *           &lt;simpleType&gt;
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
         *               &lt;maxLength value="1"/&gt;
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

            @XmlElement(name = "FIRSTNAME", required = true)
            protected String firstname;
            @XmlElement(name = "LASTNAME", required = true)
            protected String lastname;
            @XmlElement(name = "CORRESPONDLANGUAGEISO", required = true, defaultValue = "DE")
            protected String correspondlanguageiso;
            @XmlElement(name = "BIRTHNAME")
            protected String birthname;
            @XmlElement(name = "MIDDLENAME")
            protected String middlename;
            @XmlElement(name = "SECONDNAME")
            protected String secondname;
            @XmlElement(name = "TITLE_ACA1")
            protected String titleaca1;
            @XmlElement(name = "TITLE_ACA2")
            protected String titleaca2;
            @XmlElement(name = "TITLE_SPPL")
            protected String titlesppl;
            @XmlElement(name = "PREFIX1")
            protected String prefix1;
            @XmlElement(name = "PREFIX2")
            protected String prefix2;
            @XmlElement(name = "NICKNAME")
            protected String nickname;
            @XmlElement(name = "INITIALS")
            protected String initials;
            @XmlElement(name = "NAMEFORMAT")
            protected String nameformat;
            @XmlElement(name = "NAMCOUNTRY")
            protected String namcountry;
            @XmlElement(name = "NAMCOUNTRYISO")
            protected String namcountryiso;
            @XmlElement(name = "SEX")
            protected String sex;
            @XmlElement(name = "BIRTHPLACE")
            protected String birthplace;
            @XmlElement(name = "BIRTHDATE")
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar birthdate;
            @XmlElement(name = "DEATHDATE")
            @XmlSchemaType(name = "date")
            protected XMLGregorianCalendar deathdate;
            @XmlElement(name = "MARITALSTATUS")
            protected String maritalstatus;
            @XmlElement(name = "CORRESPONDLANGUAGE")
            protected String correspondlanguage;
            @XmlElement(name = "FULLNAME")
            protected String fullname;
            @XmlElement(name = "EMPLOYER")
            protected String employer;
            @XmlElement(name = "OCCUPATION")
            protected String occupation;
            @XmlElement(name = "NATIONALITY")
            protected String nationality;
            @XmlElement(name = "NATIONALITYISO")
            protected String nationalityiso;
            @XmlElement(name = "COUNTRYORIGIN")
            protected String countryorigin;
            @XmlElement(name = "BIRTHDT_STATUS")
            protected String birthdtstatus;

            /**
             * Ruft den Wert der firstname-Eigenschaft ab.
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
             * Legt den Wert der firstname-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setFIRSTNAME(String value) {
                this.firstname = value;
            }

            /**
             * Ruft den Wert der lastname-Eigenschaft ab.
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
             * Legt den Wert der lastname-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setLASTNAME(String value) {
                this.lastname = value;
            }

            /**
             * Ruft den Wert der correspondlanguageiso-Eigenschaft ab.
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
             * Legt den Wert der correspondlanguageiso-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setCORRESPONDLANGUAGEISO(String value) {
                this.correspondlanguageiso = value;
            }

            /**
             * Ruft den Wert der birthname-Eigenschaft ab.
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
             * Legt den Wert der birthname-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setBIRTHNAME(String value) {
                this.birthname = value;
            }

            /**
             * Ruft den Wert der middlename-Eigenschaft ab.
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
             * Legt den Wert der middlename-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setMIDDLENAME(String value) {
                this.middlename = value;
            }

            /**
             * Ruft den Wert der secondname-Eigenschaft ab.
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
             * Legt den Wert der secondname-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setSECONDNAME(String value) {
                this.secondname = value;
            }

            /**
             * Ruft den Wert der titleaca1-Eigenschaft ab.
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
             * Legt den Wert der titleaca1-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setTITLEACA1(String value) {
                this.titleaca1 = value;
            }

            /**
             * Ruft den Wert der titleaca2-Eigenschaft ab.
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
             * Legt den Wert der titleaca2-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setTITLEACA2(String value) {
                this.titleaca2 = value;
            }

            /**
             * Ruft den Wert der titlesppl-Eigenschaft ab.
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
             * Legt den Wert der titlesppl-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setTITLESPPL(String value) {
                this.titlesppl = value;
            }

            /**
             * Ruft den Wert der prefix1-Eigenschaft ab.
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
             * Legt den Wert der prefix1-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setPREFIX1(String value) {
                this.prefix1 = value;
            }

            /**
             * Ruft den Wert der prefix2-Eigenschaft ab.
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
             * Legt den Wert der prefix2-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setPREFIX2(String value) {
                this.prefix2 = value;
            }

            /**
             * Ruft den Wert der nickname-Eigenschaft ab.
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
             * Legt den Wert der nickname-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setNICKNAME(String value) {
                this.nickname = value;
            }

            /**
             * Ruft den Wert der initials-Eigenschaft ab.
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
             * Legt den Wert der initials-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setINITIALS(String value) {
                this.initials = value;
            }

            /**
             * Ruft den Wert der nameformat-Eigenschaft ab.
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
             * Legt den Wert der nameformat-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setNAMEFORMAT(String value) {
                this.nameformat = value;
            }

            /**
             * Ruft den Wert der namcountry-Eigenschaft ab.
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
             * Legt den Wert der namcountry-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setNAMCOUNTRY(String value) {
                this.namcountry = value;
            }

            /**
             * Ruft den Wert der namcountryiso-Eigenschaft ab.
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
             * Legt den Wert der namcountryiso-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setNAMCOUNTRYISO(String value) {
                this.namcountryiso = value;
            }

            /**
             * Ruft den Wert der sex-Eigenschaft ab.
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
             * Legt den Wert der sex-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setSEX(String value) {
                this.sex = value;
            }

            /**
             * Ruft den Wert der birthplace-Eigenschaft ab.
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
             * Legt den Wert der birthplace-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setBIRTHPLACE(String value) {
                this.birthplace = value;
            }

            /**
             * Ruft den Wert der birthdate-Eigenschaft ab.
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
             * Legt den Wert der birthdate-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *
             */
            public void setBIRTHDATE(XMLGregorianCalendar value) {
                this.birthdate = value;
            }

            /**
             * Ruft den Wert der deathdate-Eigenschaft ab.
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
             * Legt den Wert der deathdate-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link XMLGregorianCalendar }
             *
             */
            public void setDEATHDATE(XMLGregorianCalendar value) {
                this.deathdate = value;
            }

            /**
             * Ruft den Wert der maritalstatus-Eigenschaft ab.
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
             * Legt den Wert der maritalstatus-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setMARITALSTATUS(String value) {
                this.maritalstatus = value;
            }

            /**
             * Ruft den Wert der correspondlanguage-Eigenschaft ab.
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
             * Legt den Wert der correspondlanguage-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setCORRESPONDLANGUAGE(String value) {
                this.correspondlanguage = value;
            }

            /**
             * Ruft den Wert der fullname-Eigenschaft ab.
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
             * Legt den Wert der fullname-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setFULLNAME(String value) {
                this.fullname = value;
            }

            /**
             * Ruft den Wert der employer-Eigenschaft ab.
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
             * Legt den Wert der employer-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setEMPLOYER(String value) {
                this.employer = value;
            }

            /**
             * Ruft den Wert der occupation-Eigenschaft ab.
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
             * Legt den Wert der occupation-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setOCCUPATION(String value) {
                this.occupation = value;
            }

            /**
             * Ruft den Wert der nationality-Eigenschaft ab.
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
             * Legt den Wert der nationality-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setNATIONALITY(String value) {
                this.nationality = value;
            }

            /**
             * Ruft den Wert der nationalityiso-Eigenschaft ab.
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
             * Legt den Wert der nationalityiso-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setNATIONALITYISO(String value) {
                this.nationalityiso = value;
            }

            /**
             * Ruft den Wert der countryorigin-Eigenschaft ab.
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
             * Legt den Wert der countryorigin-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setCOUNTRYORIGIN(String value) {
                this.countryorigin = value;
            }

            /**
             * Ruft den Wert der birthdtstatus-Eigenschaft ab.
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
             * Legt den Wert der birthdtstatus-Eigenschaft fest.
             *
             * @param value
             *     allowed object is
             *     {@link String }
             *
             */
            public void setBIRTHDTSTATUS(String value) {
                this.birthdtstatus = value;
            }

        }

    }

}
