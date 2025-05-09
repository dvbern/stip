/*
 * STIP API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package ch.dvbern.stip.generated.dto;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Persoenliche Budget daten fuer und von der Berechnung
 */
@JsonPropertyOrder({
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_ANZAHL_PERSONEN_IM_HAUSHALT,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_TOTAL_VOR_TEILUNG,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_EIGENER_HAUSHALT,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_ANTEIL_FAMILIENBUDGET,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_EINKOMMEN,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_ALIMENTE,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_LEISTUNGEN_E_O,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_RENTE,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_KINDER_AUSBILDUNGSZULAGEN,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_ERGAENZUNGSLEISTUNGEN,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_GEMEINDE_INSTITUTIONEN,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_STEUERBARES_VERMOEGEN,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_ANRECHENBARES_VERMOEGEN,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_EINKOMMEN_PARTNER,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_EINNAHMEN_PERSOENLICHES_BUDGET,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_ANTEIL_LEBENSHALTUNGSKOSTEN,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_GRUNDBEDARF,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_WOHNKOSTEN,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_MEDIZINISCHE_GRUNDVERSORGUNG,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_STEUERN_KANTON_GEMEINDE,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_FAHRKOSTEN,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_FAHRKOSTEN_PARTNER,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_VERPFLEGUNG,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_VERPFLEGUNG_PARTNER,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_FREMDBETREUUNG,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_AUSBILDUNGSKOSTEN,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_AUSGABEN_PERSOENLICHES_BUDGET,
  PersoenlichesBudgetresultatDtoSpec.JSON_PROPERTY_PERSOENLICHESBUDGET_BERECHNET
})
@JsonTypeName("PersoenlichesBudgetresultat")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class PersoenlichesBudgetresultatDtoSpec {
  public static final String JSON_PROPERTY_ANZAHL_PERSONEN_IM_HAUSHALT = "anzahlPersonenImHaushalt";
  private Integer anzahlPersonenImHaushalt;

  public static final String JSON_PROPERTY_TOTAL_VOR_TEILUNG = "totalVorTeilung";
  private Integer totalVorTeilung;

  public static final String JSON_PROPERTY_EIGENER_HAUSHALT = "eigenerHaushalt";
  private Boolean eigenerHaushalt;

  public static final String JSON_PROPERTY_ANTEIL_FAMILIENBUDGET = "anteilFamilienbudget";
  private Integer anteilFamilienbudget;

  public static final String JSON_PROPERTY_EINKOMMEN = "einkommen";
  private Integer einkommen;

  public static final String JSON_PROPERTY_ALIMENTE = "alimente";
  private Integer alimente;

  public static final String JSON_PROPERTY_LEISTUNGEN_E_O = "leistungenEO";
  private Integer leistungenEO;

  public static final String JSON_PROPERTY_RENTE = "rente";
  private Integer rente;

  public static final String JSON_PROPERTY_KINDER_AUSBILDUNGSZULAGEN = "kinderAusbildungszulagen";
  private Integer kinderAusbildungszulagen;

  public static final String JSON_PROPERTY_ERGAENZUNGSLEISTUNGEN = "ergaenzungsleistungen";
  private Integer ergaenzungsleistungen;

  public static final String JSON_PROPERTY_GEMEINDE_INSTITUTIONEN = "gemeindeInstitutionen";
  private Integer gemeindeInstitutionen;

  public static final String JSON_PROPERTY_STEUERBARES_VERMOEGEN = "steuerbaresVermoegen";
  private Integer steuerbaresVermoegen;

  public static final String JSON_PROPERTY_ANRECHENBARES_VERMOEGEN = "anrechenbaresVermoegen";
  private Integer anrechenbaresVermoegen;

  public static final String JSON_PROPERTY_EINKOMMEN_PARTNER = "einkommenPartner";
  private Integer einkommenPartner;

  public static final String JSON_PROPERTY_EINNAHMEN_PERSOENLICHES_BUDGET = "einnahmenPersoenlichesBudget";
  private Integer einnahmenPersoenlichesBudget;

  public static final String JSON_PROPERTY_ANTEIL_LEBENSHALTUNGSKOSTEN = "anteilLebenshaltungskosten";
  private Integer anteilLebenshaltungskosten;

  public static final String JSON_PROPERTY_GRUNDBEDARF = "grundbedarf";
  private Integer grundbedarf;

  public static final String JSON_PROPERTY_WOHNKOSTEN = "wohnkosten";
  private Integer wohnkosten;

  public static final String JSON_PROPERTY_MEDIZINISCHE_GRUNDVERSORGUNG = "medizinischeGrundversorgung";
  private Integer medizinischeGrundversorgung;

  public static final String JSON_PROPERTY_STEUERN_KANTON_GEMEINDE = "steuernKantonGemeinde";
  private Integer steuernKantonGemeinde;

  public static final String JSON_PROPERTY_FAHRKOSTEN = "fahrkosten";
  private Integer fahrkosten;

  public static final String JSON_PROPERTY_FAHRKOSTEN_PARTNER = "fahrkostenPartner";
  private Integer fahrkostenPartner;

  public static final String JSON_PROPERTY_VERPFLEGUNG = "verpflegung";
  private Integer verpflegung;

  public static final String JSON_PROPERTY_VERPFLEGUNG_PARTNER = "verpflegungPartner";
  private Integer verpflegungPartner;

  public static final String JSON_PROPERTY_FREMDBETREUUNG = "fremdbetreuung";
  private Integer fremdbetreuung;

  public static final String JSON_PROPERTY_AUSBILDUNGSKOSTEN = "ausbildungskosten";
  private Integer ausbildungskosten;

  public static final String JSON_PROPERTY_AUSGABEN_PERSOENLICHES_BUDGET = "ausgabenPersoenlichesBudget";
  private Integer ausgabenPersoenlichesBudget;

  public static final String JSON_PROPERTY_PERSOENLICHESBUDGET_BERECHNET = "persoenlichesbudgetBerechnet";
  private Integer persoenlichesbudgetBerechnet;

  public PersoenlichesBudgetresultatDtoSpec() {
  }

  public PersoenlichesBudgetresultatDtoSpec anzahlPersonenImHaushalt(Integer anzahlPersonenImHaushalt) {
    
    this.anzahlPersonenImHaushalt = anzahlPersonenImHaushalt;
    return this;
  }

   /**
   * Get anzahlPersonenImHaushalt
   * @return anzahlPersonenImHaushalt
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_ANZAHL_PERSONEN_IM_HAUSHALT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getAnzahlPersonenImHaushalt() {
    return anzahlPersonenImHaushalt;
  }


  @JsonProperty(JSON_PROPERTY_ANZAHL_PERSONEN_IM_HAUSHALT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setAnzahlPersonenImHaushalt(Integer anzahlPersonenImHaushalt) {
    this.anzahlPersonenImHaushalt = anzahlPersonenImHaushalt;
  }


  public PersoenlichesBudgetresultatDtoSpec totalVorTeilung(Integer totalVorTeilung) {
    
    this.totalVorTeilung = totalVorTeilung;
    return this;
  }

   /**
   * Get totalVorTeilung
   * @return totalVorTeilung
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TOTAL_VOR_TEILUNG)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getTotalVorTeilung() {
    return totalVorTeilung;
  }


  @JsonProperty(JSON_PROPERTY_TOTAL_VOR_TEILUNG)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTotalVorTeilung(Integer totalVorTeilung) {
    this.totalVorTeilung = totalVorTeilung;
  }


  public PersoenlichesBudgetresultatDtoSpec eigenerHaushalt(Boolean eigenerHaushalt) {
    
    this.eigenerHaushalt = eigenerHaushalt;
    return this;
  }

   /**
   * Get eigenerHaushalt
   * @return eigenerHaushalt
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_EIGENER_HAUSHALT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Boolean getEigenerHaushalt() {
    return eigenerHaushalt;
  }


  @JsonProperty(JSON_PROPERTY_EIGENER_HAUSHALT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setEigenerHaushalt(Boolean eigenerHaushalt) {
    this.eigenerHaushalt = eigenerHaushalt;
  }


  public PersoenlichesBudgetresultatDtoSpec anteilFamilienbudget(Integer anteilFamilienbudget) {
    
    this.anteilFamilienbudget = anteilFamilienbudget;
    return this;
  }

   /**
   * Get anteilFamilienbudget
   * @return anteilFamilienbudget
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_ANTEIL_FAMILIENBUDGET)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getAnteilFamilienbudget() {
    return anteilFamilienbudget;
  }


  @JsonProperty(JSON_PROPERTY_ANTEIL_FAMILIENBUDGET)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setAnteilFamilienbudget(Integer anteilFamilienbudget) {
    this.anteilFamilienbudget = anteilFamilienbudget;
  }


  public PersoenlichesBudgetresultatDtoSpec einkommen(Integer einkommen) {
    
    this.einkommen = einkommen;
    return this;
  }

   /**
   * Get einkommen
   * @return einkommen
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_EINKOMMEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getEinkommen() {
    return einkommen;
  }


  @JsonProperty(JSON_PROPERTY_EINKOMMEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setEinkommen(Integer einkommen) {
    this.einkommen = einkommen;
  }


  public PersoenlichesBudgetresultatDtoSpec alimente(Integer alimente) {
    
    this.alimente = alimente;
    return this;
  }

   /**
   * Get alimente
   * @return alimente
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_ALIMENTE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getAlimente() {
    return alimente;
  }


  @JsonProperty(JSON_PROPERTY_ALIMENTE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setAlimente(Integer alimente) {
    this.alimente = alimente;
  }


  public PersoenlichesBudgetresultatDtoSpec leistungenEO(Integer leistungenEO) {
    
    this.leistungenEO = leistungenEO;
    return this;
  }

   /**
   * Get leistungenEO
   * @return leistungenEO
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_LEISTUNGEN_E_O)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getLeistungenEO() {
    return leistungenEO;
  }


  @JsonProperty(JSON_PROPERTY_LEISTUNGEN_E_O)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setLeistungenEO(Integer leistungenEO) {
    this.leistungenEO = leistungenEO;
  }


  public PersoenlichesBudgetresultatDtoSpec rente(Integer rente) {
    
    this.rente = rente;
    return this;
  }

   /**
   * Get rente
   * @return rente
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_RENTE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getRente() {
    return rente;
  }


  @JsonProperty(JSON_PROPERTY_RENTE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setRente(Integer rente) {
    this.rente = rente;
  }


  public PersoenlichesBudgetresultatDtoSpec kinderAusbildungszulagen(Integer kinderAusbildungszulagen) {
    
    this.kinderAusbildungszulagen = kinderAusbildungszulagen;
    return this;
  }

   /**
   * Get kinderAusbildungszulagen
   * @return kinderAusbildungszulagen
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_KINDER_AUSBILDUNGSZULAGEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getKinderAusbildungszulagen() {
    return kinderAusbildungszulagen;
  }


  @JsonProperty(JSON_PROPERTY_KINDER_AUSBILDUNGSZULAGEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setKinderAusbildungszulagen(Integer kinderAusbildungszulagen) {
    this.kinderAusbildungszulagen = kinderAusbildungszulagen;
  }


  public PersoenlichesBudgetresultatDtoSpec ergaenzungsleistungen(Integer ergaenzungsleistungen) {
    
    this.ergaenzungsleistungen = ergaenzungsleistungen;
    return this;
  }

   /**
   * Get ergaenzungsleistungen
   * @return ergaenzungsleistungen
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_ERGAENZUNGSLEISTUNGEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getErgaenzungsleistungen() {
    return ergaenzungsleistungen;
  }


  @JsonProperty(JSON_PROPERTY_ERGAENZUNGSLEISTUNGEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setErgaenzungsleistungen(Integer ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
  }


  public PersoenlichesBudgetresultatDtoSpec gemeindeInstitutionen(Integer gemeindeInstitutionen) {
    
    this.gemeindeInstitutionen = gemeindeInstitutionen;
    return this;
  }

   /**
   * Get gemeindeInstitutionen
   * @return gemeindeInstitutionen
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_GEMEINDE_INSTITUTIONEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getGemeindeInstitutionen() {
    return gemeindeInstitutionen;
  }


  @JsonProperty(JSON_PROPERTY_GEMEINDE_INSTITUTIONEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setGemeindeInstitutionen(Integer gemeindeInstitutionen) {
    this.gemeindeInstitutionen = gemeindeInstitutionen;
  }


  public PersoenlichesBudgetresultatDtoSpec steuerbaresVermoegen(Integer steuerbaresVermoegen) {
    
    this.steuerbaresVermoegen = steuerbaresVermoegen;
    return this;
  }

   /**
   * Get steuerbaresVermoegen
   * @return steuerbaresVermoegen
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_STEUERBARES_VERMOEGEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getSteuerbaresVermoegen() {
    return steuerbaresVermoegen;
  }


  @JsonProperty(JSON_PROPERTY_STEUERBARES_VERMOEGEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setSteuerbaresVermoegen(Integer steuerbaresVermoegen) {
    this.steuerbaresVermoegen = steuerbaresVermoegen;
  }


  public PersoenlichesBudgetresultatDtoSpec anrechenbaresVermoegen(Integer anrechenbaresVermoegen) {
    
    this.anrechenbaresVermoegen = anrechenbaresVermoegen;
    return this;
  }

   /**
   * Get anrechenbaresVermoegen
   * @return anrechenbaresVermoegen
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_ANRECHENBARES_VERMOEGEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getAnrechenbaresVermoegen() {
    return anrechenbaresVermoegen;
  }


  @JsonProperty(JSON_PROPERTY_ANRECHENBARES_VERMOEGEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setAnrechenbaresVermoegen(Integer anrechenbaresVermoegen) {
    this.anrechenbaresVermoegen = anrechenbaresVermoegen;
  }


  public PersoenlichesBudgetresultatDtoSpec einkommenPartner(Integer einkommenPartner) {
    
    this.einkommenPartner = einkommenPartner;
    return this;
  }

   /**
   * Get einkommenPartner
   * @return einkommenPartner
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_EINKOMMEN_PARTNER)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getEinkommenPartner() {
    return einkommenPartner;
  }


  @JsonProperty(JSON_PROPERTY_EINKOMMEN_PARTNER)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setEinkommenPartner(Integer einkommenPartner) {
    this.einkommenPartner = einkommenPartner;
  }


  public PersoenlichesBudgetresultatDtoSpec einnahmenPersoenlichesBudget(Integer einnahmenPersoenlichesBudget) {
    
    this.einnahmenPersoenlichesBudget = einnahmenPersoenlichesBudget;
    return this;
  }

   /**
   * Get einnahmenPersoenlichesBudget
   * @return einnahmenPersoenlichesBudget
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_EINNAHMEN_PERSOENLICHES_BUDGET)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getEinnahmenPersoenlichesBudget() {
    return einnahmenPersoenlichesBudget;
  }


  @JsonProperty(JSON_PROPERTY_EINNAHMEN_PERSOENLICHES_BUDGET)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setEinnahmenPersoenlichesBudget(Integer einnahmenPersoenlichesBudget) {
    this.einnahmenPersoenlichesBudget = einnahmenPersoenlichesBudget;
  }


  public PersoenlichesBudgetresultatDtoSpec anteilLebenshaltungskosten(Integer anteilLebenshaltungskosten) {
    
    this.anteilLebenshaltungskosten = anteilLebenshaltungskosten;
    return this;
  }

   /**
   * Get anteilLebenshaltungskosten
   * @return anteilLebenshaltungskosten
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_ANTEIL_LEBENSHALTUNGSKOSTEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getAnteilLebenshaltungskosten() {
    return anteilLebenshaltungskosten;
  }


  @JsonProperty(JSON_PROPERTY_ANTEIL_LEBENSHALTUNGSKOSTEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setAnteilLebenshaltungskosten(Integer anteilLebenshaltungskosten) {
    this.anteilLebenshaltungskosten = anteilLebenshaltungskosten;
  }


  public PersoenlichesBudgetresultatDtoSpec grundbedarf(Integer grundbedarf) {
    
    this.grundbedarf = grundbedarf;
    return this;
  }

   /**
   * Get grundbedarf
   * @return grundbedarf
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_GRUNDBEDARF)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getGrundbedarf() {
    return grundbedarf;
  }


  @JsonProperty(JSON_PROPERTY_GRUNDBEDARF)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setGrundbedarf(Integer grundbedarf) {
    this.grundbedarf = grundbedarf;
  }


  public PersoenlichesBudgetresultatDtoSpec wohnkosten(Integer wohnkosten) {
    
    this.wohnkosten = wohnkosten;
    return this;
  }

   /**
   * Get wohnkosten
   * @return wohnkosten
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_WOHNKOSTEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getWohnkosten() {
    return wohnkosten;
  }


  @JsonProperty(JSON_PROPERTY_WOHNKOSTEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setWohnkosten(Integer wohnkosten) {
    this.wohnkosten = wohnkosten;
  }


  public PersoenlichesBudgetresultatDtoSpec medizinischeGrundversorgung(Integer medizinischeGrundversorgung) {
    
    this.medizinischeGrundversorgung = medizinischeGrundversorgung;
    return this;
  }

   /**
   * Get medizinischeGrundversorgung
   * @return medizinischeGrundversorgung
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_MEDIZINISCHE_GRUNDVERSORGUNG)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getMedizinischeGrundversorgung() {
    return medizinischeGrundversorgung;
  }


  @JsonProperty(JSON_PROPERTY_MEDIZINISCHE_GRUNDVERSORGUNG)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setMedizinischeGrundversorgung(Integer medizinischeGrundversorgung) {
    this.medizinischeGrundversorgung = medizinischeGrundversorgung;
  }


  public PersoenlichesBudgetresultatDtoSpec steuernKantonGemeinde(Integer steuernKantonGemeinde) {
    
    this.steuernKantonGemeinde = steuernKantonGemeinde;
    return this;
  }

   /**
   * Get steuernKantonGemeinde
   * @return steuernKantonGemeinde
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_STEUERN_KANTON_GEMEINDE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getSteuernKantonGemeinde() {
    return steuernKantonGemeinde;
  }


  @JsonProperty(JSON_PROPERTY_STEUERN_KANTON_GEMEINDE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setSteuernKantonGemeinde(Integer steuernKantonGemeinde) {
    this.steuernKantonGemeinde = steuernKantonGemeinde;
  }


  public PersoenlichesBudgetresultatDtoSpec fahrkosten(Integer fahrkosten) {
    
    this.fahrkosten = fahrkosten;
    return this;
  }

   /**
   * Get fahrkosten
   * @return fahrkosten
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_FAHRKOSTEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getFahrkosten() {
    return fahrkosten;
  }


  @JsonProperty(JSON_PROPERTY_FAHRKOSTEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setFahrkosten(Integer fahrkosten) {
    this.fahrkosten = fahrkosten;
  }


  public PersoenlichesBudgetresultatDtoSpec fahrkostenPartner(Integer fahrkostenPartner) {
    
    this.fahrkostenPartner = fahrkostenPartner;
    return this;
  }

   /**
   * Get fahrkostenPartner
   * @return fahrkostenPartner
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_FAHRKOSTEN_PARTNER)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getFahrkostenPartner() {
    return fahrkostenPartner;
  }


  @JsonProperty(JSON_PROPERTY_FAHRKOSTEN_PARTNER)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setFahrkostenPartner(Integer fahrkostenPartner) {
    this.fahrkostenPartner = fahrkostenPartner;
  }


  public PersoenlichesBudgetresultatDtoSpec verpflegung(Integer verpflegung) {
    
    this.verpflegung = verpflegung;
    return this;
  }

   /**
   * Get verpflegung
   * @return verpflegung
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_VERPFLEGUNG)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getVerpflegung() {
    return verpflegung;
  }


  @JsonProperty(JSON_PROPERTY_VERPFLEGUNG)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setVerpflegung(Integer verpflegung) {
    this.verpflegung = verpflegung;
  }


  public PersoenlichesBudgetresultatDtoSpec verpflegungPartner(Integer verpflegungPartner) {
    
    this.verpflegungPartner = verpflegungPartner;
    return this;
  }

   /**
   * Get verpflegungPartner
   * @return verpflegungPartner
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_VERPFLEGUNG_PARTNER)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getVerpflegungPartner() {
    return verpflegungPartner;
  }


  @JsonProperty(JSON_PROPERTY_VERPFLEGUNG_PARTNER)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setVerpflegungPartner(Integer verpflegungPartner) {
    this.verpflegungPartner = verpflegungPartner;
  }


  public PersoenlichesBudgetresultatDtoSpec fremdbetreuung(Integer fremdbetreuung) {
    
    this.fremdbetreuung = fremdbetreuung;
    return this;
  }

   /**
   * Get fremdbetreuung
   * @return fremdbetreuung
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_FREMDBETREUUNG)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getFremdbetreuung() {
    return fremdbetreuung;
  }


  @JsonProperty(JSON_PROPERTY_FREMDBETREUUNG)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setFremdbetreuung(Integer fremdbetreuung) {
    this.fremdbetreuung = fremdbetreuung;
  }


  public PersoenlichesBudgetresultatDtoSpec ausbildungskosten(Integer ausbildungskosten) {
    
    this.ausbildungskosten = ausbildungskosten;
    return this;
  }

   /**
   * Get ausbildungskosten
   * @return ausbildungskosten
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_AUSBILDUNGSKOSTEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getAusbildungskosten() {
    return ausbildungskosten;
  }


  @JsonProperty(JSON_PROPERTY_AUSBILDUNGSKOSTEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setAusbildungskosten(Integer ausbildungskosten) {
    this.ausbildungskosten = ausbildungskosten;
  }


  public PersoenlichesBudgetresultatDtoSpec ausgabenPersoenlichesBudget(Integer ausgabenPersoenlichesBudget) {
    
    this.ausgabenPersoenlichesBudget = ausgabenPersoenlichesBudget;
    return this;
  }

   /**
   * Get ausgabenPersoenlichesBudget
   * @return ausgabenPersoenlichesBudget
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_AUSGABEN_PERSOENLICHES_BUDGET)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getAusgabenPersoenlichesBudget() {
    return ausgabenPersoenlichesBudget;
  }


  @JsonProperty(JSON_PROPERTY_AUSGABEN_PERSOENLICHES_BUDGET)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setAusgabenPersoenlichesBudget(Integer ausgabenPersoenlichesBudget) {
    this.ausgabenPersoenlichesBudget = ausgabenPersoenlichesBudget;
  }


  public PersoenlichesBudgetresultatDtoSpec persoenlichesbudgetBerechnet(Integer persoenlichesbudgetBerechnet) {
    
    this.persoenlichesbudgetBerechnet = persoenlichesbudgetBerechnet;
    return this;
  }

   /**
   * Get persoenlichesbudgetBerechnet
   * @return persoenlichesbudgetBerechnet
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_PERSOENLICHESBUDGET_BERECHNET)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getPersoenlichesbudgetBerechnet() {
    return persoenlichesbudgetBerechnet;
  }


  @JsonProperty(JSON_PROPERTY_PERSOENLICHESBUDGET_BERECHNET)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setPersoenlichesbudgetBerechnet(Integer persoenlichesbudgetBerechnet) {
    this.persoenlichesbudgetBerechnet = persoenlichesbudgetBerechnet;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PersoenlichesBudgetresultatDtoSpec persoenlichesBudgetresultat = (PersoenlichesBudgetresultatDtoSpec) o;
    return Objects.equals(this.anzahlPersonenImHaushalt, persoenlichesBudgetresultat.anzahlPersonenImHaushalt) &&
        Objects.equals(this.totalVorTeilung, persoenlichesBudgetresultat.totalVorTeilung) &&
        Objects.equals(this.eigenerHaushalt, persoenlichesBudgetresultat.eigenerHaushalt) &&
        Objects.equals(this.anteilFamilienbudget, persoenlichesBudgetresultat.anteilFamilienbudget) &&
        Objects.equals(this.einkommen, persoenlichesBudgetresultat.einkommen) &&
        Objects.equals(this.alimente, persoenlichesBudgetresultat.alimente) &&
        Objects.equals(this.leistungenEO, persoenlichesBudgetresultat.leistungenEO) &&
        Objects.equals(this.rente, persoenlichesBudgetresultat.rente) &&
        Objects.equals(this.kinderAusbildungszulagen, persoenlichesBudgetresultat.kinderAusbildungszulagen) &&
        Objects.equals(this.ergaenzungsleistungen, persoenlichesBudgetresultat.ergaenzungsleistungen) &&
        Objects.equals(this.gemeindeInstitutionen, persoenlichesBudgetresultat.gemeindeInstitutionen) &&
        Objects.equals(this.steuerbaresVermoegen, persoenlichesBudgetresultat.steuerbaresVermoegen) &&
        Objects.equals(this.anrechenbaresVermoegen, persoenlichesBudgetresultat.anrechenbaresVermoegen) &&
        Objects.equals(this.einkommenPartner, persoenlichesBudgetresultat.einkommenPartner) &&
        Objects.equals(this.einnahmenPersoenlichesBudget, persoenlichesBudgetresultat.einnahmenPersoenlichesBudget) &&
        Objects.equals(this.anteilLebenshaltungskosten, persoenlichesBudgetresultat.anteilLebenshaltungskosten) &&
        Objects.equals(this.grundbedarf, persoenlichesBudgetresultat.grundbedarf) &&
        Objects.equals(this.wohnkosten, persoenlichesBudgetresultat.wohnkosten) &&
        Objects.equals(this.medizinischeGrundversorgung, persoenlichesBudgetresultat.medizinischeGrundversorgung) &&
        Objects.equals(this.steuernKantonGemeinde, persoenlichesBudgetresultat.steuernKantonGemeinde) &&
        Objects.equals(this.fahrkosten, persoenlichesBudgetresultat.fahrkosten) &&
        Objects.equals(this.fahrkostenPartner, persoenlichesBudgetresultat.fahrkostenPartner) &&
        Objects.equals(this.verpflegung, persoenlichesBudgetresultat.verpflegung) &&
        Objects.equals(this.verpflegungPartner, persoenlichesBudgetresultat.verpflegungPartner) &&
        Objects.equals(this.fremdbetreuung, persoenlichesBudgetresultat.fremdbetreuung) &&
        Objects.equals(this.ausbildungskosten, persoenlichesBudgetresultat.ausbildungskosten) &&
        Objects.equals(this.ausgabenPersoenlichesBudget, persoenlichesBudgetresultat.ausgabenPersoenlichesBudget) &&
        Objects.equals(this.persoenlichesbudgetBerechnet, persoenlichesBudgetresultat.persoenlichesbudgetBerechnet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(anzahlPersonenImHaushalt, totalVorTeilung, eigenerHaushalt, anteilFamilienbudget, einkommen, alimente, leistungenEO, rente, kinderAusbildungszulagen, ergaenzungsleistungen, gemeindeInstitutionen, steuerbaresVermoegen, anrechenbaresVermoegen, einkommenPartner, einnahmenPersoenlichesBudget, anteilLebenshaltungskosten, grundbedarf, wohnkosten, medizinischeGrundversorgung, steuernKantonGemeinde, fahrkosten, fahrkostenPartner, verpflegung, verpflegungPartner, fremdbetreuung, ausbildungskosten, ausgabenPersoenlichesBudget, persoenlichesbudgetBerechnet);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PersoenlichesBudgetresultatDtoSpec {\n");
    sb.append("    anzahlPersonenImHaushalt: ").append(toIndentedString(anzahlPersonenImHaushalt)).append("\n");
    sb.append("    totalVorTeilung: ").append(toIndentedString(totalVorTeilung)).append("\n");
    sb.append("    eigenerHaushalt: ").append(toIndentedString(eigenerHaushalt)).append("\n");
    sb.append("    anteilFamilienbudget: ").append(toIndentedString(anteilFamilienbudget)).append("\n");
    sb.append("    einkommen: ").append(toIndentedString(einkommen)).append("\n");
    sb.append("    alimente: ").append(toIndentedString(alimente)).append("\n");
    sb.append("    leistungenEO: ").append(toIndentedString(leistungenEO)).append("\n");
    sb.append("    rente: ").append(toIndentedString(rente)).append("\n");
    sb.append("    kinderAusbildungszulagen: ").append(toIndentedString(kinderAusbildungszulagen)).append("\n");
    sb.append("    ergaenzungsleistungen: ").append(toIndentedString(ergaenzungsleistungen)).append("\n");
    sb.append("    gemeindeInstitutionen: ").append(toIndentedString(gemeindeInstitutionen)).append("\n");
    sb.append("    steuerbaresVermoegen: ").append(toIndentedString(steuerbaresVermoegen)).append("\n");
    sb.append("    anrechenbaresVermoegen: ").append(toIndentedString(anrechenbaresVermoegen)).append("\n");
    sb.append("    einkommenPartner: ").append(toIndentedString(einkommenPartner)).append("\n");
    sb.append("    einnahmenPersoenlichesBudget: ").append(toIndentedString(einnahmenPersoenlichesBudget)).append("\n");
    sb.append("    anteilLebenshaltungskosten: ").append(toIndentedString(anteilLebenshaltungskosten)).append("\n");
    sb.append("    grundbedarf: ").append(toIndentedString(grundbedarf)).append("\n");
    sb.append("    wohnkosten: ").append(toIndentedString(wohnkosten)).append("\n");
    sb.append("    medizinischeGrundversorgung: ").append(toIndentedString(medizinischeGrundversorgung)).append("\n");
    sb.append("    steuernKantonGemeinde: ").append(toIndentedString(steuernKantonGemeinde)).append("\n");
    sb.append("    fahrkosten: ").append(toIndentedString(fahrkosten)).append("\n");
    sb.append("    fahrkostenPartner: ").append(toIndentedString(fahrkostenPartner)).append("\n");
    sb.append("    verpflegung: ").append(toIndentedString(verpflegung)).append("\n");
    sb.append("    verpflegungPartner: ").append(toIndentedString(verpflegungPartner)).append("\n");
    sb.append("    fremdbetreuung: ").append(toIndentedString(fremdbetreuung)).append("\n");
    sb.append("    ausbildungskosten: ").append(toIndentedString(ausbildungskosten)).append("\n");
    sb.append("    ausgabenPersoenlichesBudget: ").append(toIndentedString(ausgabenPersoenlichesBudget)).append("\n");
    sb.append("    persoenlichesbudgetBerechnet: ").append(toIndentedString(persoenlichesbudgetBerechnet)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

