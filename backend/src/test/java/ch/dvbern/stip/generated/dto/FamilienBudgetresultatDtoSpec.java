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
import ch.dvbern.stip.generated.dto.SteuerdatenTypDtoSpec;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Familien Budget daten fuer und von der Berechnung
 */
@JsonPropertyOrder({
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_FAMILIEN_BUDGET_TYP,
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_SELBSTSTAENDIG_ERWERBEND,
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_ANZAHL_PERSONEN_IM_HAUSHALT,
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_ANZAHL_GESCHWISTER_IN_AUSBILDUNG,
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_TOTAL_EINKUENFTE,
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_ERGAENZUNGSLEISTUNGEN,
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_STEUERBARES_VERMOEGEN,
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_VERMOEGEN,
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_EINZAHLUNG_SAEULE23A,
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_EIGENMIETWERT,
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_ALIMENTE,
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_EINKOMMENSFREIBETRAG,
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_EINNAHMEN_FAMILIENBUDGET,
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_GRUNDBEDARF,
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_EFFEKTIVE_WOHNKOSTEN,
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_MEDIZINISCHE_GRUNDVERSORGUNG,
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_INTEGRATIONSZULAGE,
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_STEUERN_KANTON_GEMEINDE,
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_STEUERN_BUND,
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_STEUERN_STAAT,
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_FAHRKOSTEN_PERSON1,
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_FAHRKOSTEN_PERSON2,
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_ESSENSKOSTEN_PERSON1,
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_ESSENSKOSTEN_PERSON2,
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_AUSGABEN_FAMILIENBUDGET,
  FamilienBudgetresultatDtoSpec.JSON_PROPERTY_FAMILIENBUDGET_BERECHNET
})
@JsonTypeName("FamilienBudgetresultat")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class FamilienBudgetresultatDtoSpec {
  public static final String JSON_PROPERTY_FAMILIEN_BUDGET_TYP = "familienBudgetTyp";
  private SteuerdatenTypDtoSpec familienBudgetTyp;

  public static final String JSON_PROPERTY_SELBSTSTAENDIG_ERWERBEND = "selbststaendigErwerbend";
  private Boolean selbststaendigErwerbend;

  public static final String JSON_PROPERTY_ANZAHL_PERSONEN_IM_HAUSHALT = "anzahlPersonenImHaushalt";
  private Integer anzahlPersonenImHaushalt;

  public static final String JSON_PROPERTY_ANZAHL_GESCHWISTER_IN_AUSBILDUNG = "anzahlGeschwisterInAusbildung";
  private Integer anzahlGeschwisterInAusbildung;

  public static final String JSON_PROPERTY_TOTAL_EINKUENFTE = "totalEinkuenfte";
  private Integer totalEinkuenfte;

  public static final String JSON_PROPERTY_ERGAENZUNGSLEISTUNGEN = "ergaenzungsleistungen";
  private Integer ergaenzungsleistungen;

  public static final String JSON_PROPERTY_STEUERBARES_VERMOEGEN = "steuerbaresVermoegen";
  private Integer steuerbaresVermoegen;

  public static final String JSON_PROPERTY_VERMOEGEN = "vermoegen";
  private Integer vermoegen;

  public static final String JSON_PROPERTY_EINZAHLUNG_SAEULE23A = "einzahlungSaeule23a";
  private Integer einzahlungSaeule23a;

  public static final String JSON_PROPERTY_EIGENMIETWERT = "eigenmietwert";
  private Integer eigenmietwert;

  public static final String JSON_PROPERTY_ALIMENTE = "alimente";
  private Integer alimente;

  public static final String JSON_PROPERTY_EINKOMMENSFREIBETRAG = "einkommensfreibetrag";
  private Integer einkommensfreibetrag;

  public static final String JSON_PROPERTY_EINNAHMEN_FAMILIENBUDGET = "einnahmenFamilienbudget";
  private Integer einnahmenFamilienbudget;

  public static final String JSON_PROPERTY_GRUNDBEDARF = "grundbedarf";
  private Integer grundbedarf;

  public static final String JSON_PROPERTY_EFFEKTIVE_WOHNKOSTEN = "effektiveWohnkosten";
  private Integer effektiveWohnkosten;

  public static final String JSON_PROPERTY_MEDIZINISCHE_GRUNDVERSORGUNG = "medizinischeGrundversorgung";
  private Integer medizinischeGrundversorgung;

  public static final String JSON_PROPERTY_INTEGRATIONSZULAGE = "integrationszulage";
  private Integer integrationszulage;

  public static final String JSON_PROPERTY_STEUERN_KANTON_GEMEINDE = "steuernKantonGemeinde";
  private Integer steuernKantonGemeinde;

  public static final String JSON_PROPERTY_STEUERN_BUND = "steuernBund";
  private Integer steuernBund;

  public static final String JSON_PROPERTY_STEUERN_STAAT = "steuernStaat";
  private Integer steuernStaat;

  public static final String JSON_PROPERTY_FAHRKOSTEN_PERSON1 = "fahrkostenPerson1";
  private Integer fahrkostenPerson1;

  public static final String JSON_PROPERTY_FAHRKOSTEN_PERSON2 = "fahrkostenPerson2";
  private Integer fahrkostenPerson2;

  public static final String JSON_PROPERTY_ESSENSKOSTEN_PERSON1 = "essenskostenPerson1";
  private Integer essenskostenPerson1;

  public static final String JSON_PROPERTY_ESSENSKOSTEN_PERSON2 = "essenskostenPerson2";
  private Integer essenskostenPerson2;

  public static final String JSON_PROPERTY_AUSGABEN_FAMILIENBUDGET = "ausgabenFamilienbudget";
  private Integer ausgabenFamilienbudget;

  public static final String JSON_PROPERTY_FAMILIENBUDGET_BERECHNET = "familienbudgetBerechnet";
  private Integer familienbudgetBerechnet;

  public FamilienBudgetresultatDtoSpec() {
  }

  public FamilienBudgetresultatDtoSpec familienBudgetTyp(SteuerdatenTypDtoSpec familienBudgetTyp) {
    
    this.familienBudgetTyp = familienBudgetTyp;
    return this;
  }

   /**
   * Get familienBudgetTyp
   * @return familienBudgetTyp
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_FAMILIEN_BUDGET_TYP)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public SteuerdatenTypDtoSpec getFamilienBudgetTyp() {
    return familienBudgetTyp;
  }


  @JsonProperty(JSON_PROPERTY_FAMILIEN_BUDGET_TYP)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setFamilienBudgetTyp(SteuerdatenTypDtoSpec familienBudgetTyp) {
    this.familienBudgetTyp = familienBudgetTyp;
  }


  public FamilienBudgetresultatDtoSpec selbststaendigErwerbend(Boolean selbststaendigErwerbend) {
    
    this.selbststaendigErwerbend = selbststaendigErwerbend;
    return this;
  }

   /**
   * Get selbststaendigErwerbend
   * @return selbststaendigErwerbend
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_SELBSTSTAENDIG_ERWERBEND)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Boolean getSelbststaendigErwerbend() {
    return selbststaendigErwerbend;
  }


  @JsonProperty(JSON_PROPERTY_SELBSTSTAENDIG_ERWERBEND)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setSelbststaendigErwerbend(Boolean selbststaendigErwerbend) {
    this.selbststaendigErwerbend = selbststaendigErwerbend;
  }


  public FamilienBudgetresultatDtoSpec anzahlPersonenImHaushalt(Integer anzahlPersonenImHaushalt) {
    
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


  public FamilienBudgetresultatDtoSpec anzahlGeschwisterInAusbildung(Integer anzahlGeschwisterInAusbildung) {
    
    this.anzahlGeschwisterInAusbildung = anzahlGeschwisterInAusbildung;
    return this;
  }

   /**
   * Get anzahlGeschwisterInAusbildung
   * @return anzahlGeschwisterInAusbildung
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_ANZAHL_GESCHWISTER_IN_AUSBILDUNG)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getAnzahlGeschwisterInAusbildung() {
    return anzahlGeschwisterInAusbildung;
  }


  @JsonProperty(JSON_PROPERTY_ANZAHL_GESCHWISTER_IN_AUSBILDUNG)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setAnzahlGeschwisterInAusbildung(Integer anzahlGeschwisterInAusbildung) {
    this.anzahlGeschwisterInAusbildung = anzahlGeschwisterInAusbildung;
  }


  public FamilienBudgetresultatDtoSpec totalEinkuenfte(Integer totalEinkuenfte) {
    
    this.totalEinkuenfte = totalEinkuenfte;
    return this;
  }

   /**
   * Get totalEinkuenfte
   * @return totalEinkuenfte
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_TOTAL_EINKUENFTE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getTotalEinkuenfte() {
    return totalEinkuenfte;
  }


  @JsonProperty(JSON_PROPERTY_TOTAL_EINKUENFTE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setTotalEinkuenfte(Integer totalEinkuenfte) {
    this.totalEinkuenfte = totalEinkuenfte;
  }


  public FamilienBudgetresultatDtoSpec ergaenzungsleistungen(Integer ergaenzungsleistungen) {
    
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


  public FamilienBudgetresultatDtoSpec steuerbaresVermoegen(Integer steuerbaresVermoegen) {
    
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


  public FamilienBudgetresultatDtoSpec vermoegen(Integer vermoegen) {
    
    this.vermoegen = vermoegen;
    return this;
  }

   /**
   * Get vermoegen
   * @return vermoegen
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_VERMOEGEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getVermoegen() {
    return vermoegen;
  }


  @JsonProperty(JSON_PROPERTY_VERMOEGEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setVermoegen(Integer vermoegen) {
    this.vermoegen = vermoegen;
  }


  public FamilienBudgetresultatDtoSpec einzahlungSaeule23a(Integer einzahlungSaeule23a) {
    
    this.einzahlungSaeule23a = einzahlungSaeule23a;
    return this;
  }

   /**
   * Get einzahlungSaeule23a
   * @return einzahlungSaeule23a
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_EINZAHLUNG_SAEULE23A)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getEinzahlungSaeule23a() {
    return einzahlungSaeule23a;
  }


  @JsonProperty(JSON_PROPERTY_EINZAHLUNG_SAEULE23A)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setEinzahlungSaeule23a(Integer einzahlungSaeule23a) {
    this.einzahlungSaeule23a = einzahlungSaeule23a;
  }


  public FamilienBudgetresultatDtoSpec eigenmietwert(Integer eigenmietwert) {
    
    this.eigenmietwert = eigenmietwert;
    return this;
  }

   /**
   * Get eigenmietwert
   * @return eigenmietwert
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_EIGENMIETWERT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getEigenmietwert() {
    return eigenmietwert;
  }


  @JsonProperty(JSON_PROPERTY_EIGENMIETWERT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setEigenmietwert(Integer eigenmietwert) {
    this.eigenmietwert = eigenmietwert;
  }


  public FamilienBudgetresultatDtoSpec alimente(Integer alimente) {
    
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


  public FamilienBudgetresultatDtoSpec einkommensfreibetrag(Integer einkommensfreibetrag) {
    
    this.einkommensfreibetrag = einkommensfreibetrag;
    return this;
  }

   /**
   * Get einkommensfreibetrag
   * @return einkommensfreibetrag
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_EINKOMMENSFREIBETRAG)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getEinkommensfreibetrag() {
    return einkommensfreibetrag;
  }


  @JsonProperty(JSON_PROPERTY_EINKOMMENSFREIBETRAG)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setEinkommensfreibetrag(Integer einkommensfreibetrag) {
    this.einkommensfreibetrag = einkommensfreibetrag;
  }


  public FamilienBudgetresultatDtoSpec einnahmenFamilienbudget(Integer einnahmenFamilienbudget) {
    
    this.einnahmenFamilienbudget = einnahmenFamilienbudget;
    return this;
  }

   /**
   * Get einnahmenFamilienbudget
   * @return einnahmenFamilienbudget
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_EINNAHMEN_FAMILIENBUDGET)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getEinnahmenFamilienbudget() {
    return einnahmenFamilienbudget;
  }


  @JsonProperty(JSON_PROPERTY_EINNAHMEN_FAMILIENBUDGET)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setEinnahmenFamilienbudget(Integer einnahmenFamilienbudget) {
    this.einnahmenFamilienbudget = einnahmenFamilienbudget;
  }


  public FamilienBudgetresultatDtoSpec grundbedarf(Integer grundbedarf) {
    
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


  public FamilienBudgetresultatDtoSpec effektiveWohnkosten(Integer effektiveWohnkosten) {
    
    this.effektiveWohnkosten = effektiveWohnkosten;
    return this;
  }

   /**
   * Get effektiveWohnkosten
   * @return effektiveWohnkosten
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_EFFEKTIVE_WOHNKOSTEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getEffektiveWohnkosten() {
    return effektiveWohnkosten;
  }


  @JsonProperty(JSON_PROPERTY_EFFEKTIVE_WOHNKOSTEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setEffektiveWohnkosten(Integer effektiveWohnkosten) {
    this.effektiveWohnkosten = effektiveWohnkosten;
  }


  public FamilienBudgetresultatDtoSpec medizinischeGrundversorgung(Integer medizinischeGrundversorgung) {
    
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


  public FamilienBudgetresultatDtoSpec integrationszulage(Integer integrationszulage) {
    
    this.integrationszulage = integrationszulage;
    return this;
  }

   /**
   * Get integrationszulage
   * @return integrationszulage
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_INTEGRATIONSZULAGE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getIntegrationszulage() {
    return integrationszulage;
  }


  @JsonProperty(JSON_PROPERTY_INTEGRATIONSZULAGE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setIntegrationszulage(Integer integrationszulage) {
    this.integrationszulage = integrationszulage;
  }


  public FamilienBudgetresultatDtoSpec steuernKantonGemeinde(Integer steuernKantonGemeinde) {
    
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


  public FamilienBudgetresultatDtoSpec steuernBund(Integer steuernBund) {
    
    this.steuernBund = steuernBund;
    return this;
  }

   /**
   * Get steuernBund
   * @return steuernBund
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_STEUERN_BUND)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getSteuernBund() {
    return steuernBund;
  }


  @JsonProperty(JSON_PROPERTY_STEUERN_BUND)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setSteuernBund(Integer steuernBund) {
    this.steuernBund = steuernBund;
  }


  public FamilienBudgetresultatDtoSpec steuernStaat(Integer steuernStaat) {
    
    this.steuernStaat = steuernStaat;
    return this;
  }

   /**
   * Get steuernStaat
   * @return steuernStaat
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_STEUERN_STAAT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getSteuernStaat() {
    return steuernStaat;
  }


  @JsonProperty(JSON_PROPERTY_STEUERN_STAAT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setSteuernStaat(Integer steuernStaat) {
    this.steuernStaat = steuernStaat;
  }


  public FamilienBudgetresultatDtoSpec fahrkostenPerson1(Integer fahrkostenPerson1) {
    
    this.fahrkostenPerson1 = fahrkostenPerson1;
    return this;
  }

   /**
   * Get fahrkostenPerson1
   * @return fahrkostenPerson1
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_FAHRKOSTEN_PERSON1)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getFahrkostenPerson1() {
    return fahrkostenPerson1;
  }


  @JsonProperty(JSON_PROPERTY_FAHRKOSTEN_PERSON1)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setFahrkostenPerson1(Integer fahrkostenPerson1) {
    this.fahrkostenPerson1 = fahrkostenPerson1;
  }


  public FamilienBudgetresultatDtoSpec fahrkostenPerson2(Integer fahrkostenPerson2) {
    
    this.fahrkostenPerson2 = fahrkostenPerson2;
    return this;
  }

   /**
   * Get fahrkostenPerson2
   * @return fahrkostenPerson2
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_FAHRKOSTEN_PERSON2)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getFahrkostenPerson2() {
    return fahrkostenPerson2;
  }


  @JsonProperty(JSON_PROPERTY_FAHRKOSTEN_PERSON2)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setFahrkostenPerson2(Integer fahrkostenPerson2) {
    this.fahrkostenPerson2 = fahrkostenPerson2;
  }


  public FamilienBudgetresultatDtoSpec essenskostenPerson1(Integer essenskostenPerson1) {
    
    this.essenskostenPerson1 = essenskostenPerson1;
    return this;
  }

   /**
   * Get essenskostenPerson1
   * @return essenskostenPerson1
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_ESSENSKOSTEN_PERSON1)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getEssenskostenPerson1() {
    return essenskostenPerson1;
  }


  @JsonProperty(JSON_PROPERTY_ESSENSKOSTEN_PERSON1)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setEssenskostenPerson1(Integer essenskostenPerson1) {
    this.essenskostenPerson1 = essenskostenPerson1;
  }


  public FamilienBudgetresultatDtoSpec essenskostenPerson2(Integer essenskostenPerson2) {
    
    this.essenskostenPerson2 = essenskostenPerson2;
    return this;
  }

   /**
   * Get essenskostenPerson2
   * @return essenskostenPerson2
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_ESSENSKOSTEN_PERSON2)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getEssenskostenPerson2() {
    return essenskostenPerson2;
  }


  @JsonProperty(JSON_PROPERTY_ESSENSKOSTEN_PERSON2)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setEssenskostenPerson2(Integer essenskostenPerson2) {
    this.essenskostenPerson2 = essenskostenPerson2;
  }


  public FamilienBudgetresultatDtoSpec ausgabenFamilienbudget(Integer ausgabenFamilienbudget) {
    
    this.ausgabenFamilienbudget = ausgabenFamilienbudget;
    return this;
  }

   /**
   * Get ausgabenFamilienbudget
   * @return ausgabenFamilienbudget
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_AUSGABEN_FAMILIENBUDGET)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getAusgabenFamilienbudget() {
    return ausgabenFamilienbudget;
  }


  @JsonProperty(JSON_PROPERTY_AUSGABEN_FAMILIENBUDGET)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setAusgabenFamilienbudget(Integer ausgabenFamilienbudget) {
    this.ausgabenFamilienbudget = ausgabenFamilienbudget;
  }


  public FamilienBudgetresultatDtoSpec familienbudgetBerechnet(Integer familienbudgetBerechnet) {
    
    this.familienbudgetBerechnet = familienbudgetBerechnet;
    return this;
  }

   /**
   * Get familienbudgetBerechnet
   * @return familienbudgetBerechnet
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_FAMILIENBUDGET_BERECHNET)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getFamilienbudgetBerechnet() {
    return familienbudgetBerechnet;
  }


  @JsonProperty(JSON_PROPERTY_FAMILIENBUDGET_BERECHNET)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setFamilienbudgetBerechnet(Integer familienbudgetBerechnet) {
    this.familienbudgetBerechnet = familienbudgetBerechnet;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FamilienBudgetresultatDtoSpec familienBudgetresultat = (FamilienBudgetresultatDtoSpec) o;
    return Objects.equals(this.familienBudgetTyp, familienBudgetresultat.familienBudgetTyp) &&
        Objects.equals(this.selbststaendigErwerbend, familienBudgetresultat.selbststaendigErwerbend) &&
        Objects.equals(this.anzahlPersonenImHaushalt, familienBudgetresultat.anzahlPersonenImHaushalt) &&
        Objects.equals(this.anzahlGeschwisterInAusbildung, familienBudgetresultat.anzahlGeschwisterInAusbildung) &&
        Objects.equals(this.totalEinkuenfte, familienBudgetresultat.totalEinkuenfte) &&
        Objects.equals(this.ergaenzungsleistungen, familienBudgetresultat.ergaenzungsleistungen) &&
        Objects.equals(this.steuerbaresVermoegen, familienBudgetresultat.steuerbaresVermoegen) &&
        Objects.equals(this.vermoegen, familienBudgetresultat.vermoegen) &&
        Objects.equals(this.einzahlungSaeule23a, familienBudgetresultat.einzahlungSaeule23a) &&
        Objects.equals(this.eigenmietwert, familienBudgetresultat.eigenmietwert) &&
        Objects.equals(this.alimente, familienBudgetresultat.alimente) &&
        Objects.equals(this.einkommensfreibetrag, familienBudgetresultat.einkommensfreibetrag) &&
        Objects.equals(this.einnahmenFamilienbudget, familienBudgetresultat.einnahmenFamilienbudget) &&
        Objects.equals(this.grundbedarf, familienBudgetresultat.grundbedarf) &&
        Objects.equals(this.effektiveWohnkosten, familienBudgetresultat.effektiveWohnkosten) &&
        Objects.equals(this.medizinischeGrundversorgung, familienBudgetresultat.medizinischeGrundversorgung) &&
        Objects.equals(this.integrationszulage, familienBudgetresultat.integrationszulage) &&
        Objects.equals(this.steuernKantonGemeinde, familienBudgetresultat.steuernKantonGemeinde) &&
        Objects.equals(this.steuernBund, familienBudgetresultat.steuernBund) &&
        Objects.equals(this.steuernStaat, familienBudgetresultat.steuernStaat) &&
        Objects.equals(this.fahrkostenPerson1, familienBudgetresultat.fahrkostenPerson1) &&
        Objects.equals(this.fahrkostenPerson2, familienBudgetresultat.fahrkostenPerson2) &&
        Objects.equals(this.essenskostenPerson1, familienBudgetresultat.essenskostenPerson1) &&
        Objects.equals(this.essenskostenPerson2, familienBudgetresultat.essenskostenPerson2) &&
        Objects.equals(this.ausgabenFamilienbudget, familienBudgetresultat.ausgabenFamilienbudget) &&
        Objects.equals(this.familienbudgetBerechnet, familienBudgetresultat.familienbudgetBerechnet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(familienBudgetTyp, selbststaendigErwerbend, anzahlPersonenImHaushalt, anzahlGeschwisterInAusbildung, totalEinkuenfte, ergaenzungsleistungen, steuerbaresVermoegen, vermoegen, einzahlungSaeule23a, eigenmietwert, alimente, einkommensfreibetrag, einnahmenFamilienbudget, grundbedarf, effektiveWohnkosten, medizinischeGrundversorgung, integrationszulage, steuernKantonGemeinde, steuernBund, steuernStaat, fahrkostenPerson1, fahrkostenPerson2, essenskostenPerson1, essenskostenPerson2, ausgabenFamilienbudget, familienbudgetBerechnet);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FamilienBudgetresultatDtoSpec {\n");
    sb.append("    familienBudgetTyp: ").append(toIndentedString(familienBudgetTyp)).append("\n");
    sb.append("    selbststaendigErwerbend: ").append(toIndentedString(selbststaendigErwerbend)).append("\n");
    sb.append("    anzahlPersonenImHaushalt: ").append(toIndentedString(anzahlPersonenImHaushalt)).append("\n");
    sb.append("    anzahlGeschwisterInAusbildung: ").append(toIndentedString(anzahlGeschwisterInAusbildung)).append("\n");
    sb.append("    totalEinkuenfte: ").append(toIndentedString(totalEinkuenfte)).append("\n");
    sb.append("    ergaenzungsleistungen: ").append(toIndentedString(ergaenzungsleistungen)).append("\n");
    sb.append("    steuerbaresVermoegen: ").append(toIndentedString(steuerbaresVermoegen)).append("\n");
    sb.append("    vermoegen: ").append(toIndentedString(vermoegen)).append("\n");
    sb.append("    einzahlungSaeule23a: ").append(toIndentedString(einzahlungSaeule23a)).append("\n");
    sb.append("    eigenmietwert: ").append(toIndentedString(eigenmietwert)).append("\n");
    sb.append("    alimente: ").append(toIndentedString(alimente)).append("\n");
    sb.append("    einkommensfreibetrag: ").append(toIndentedString(einkommensfreibetrag)).append("\n");
    sb.append("    einnahmenFamilienbudget: ").append(toIndentedString(einnahmenFamilienbudget)).append("\n");
    sb.append("    grundbedarf: ").append(toIndentedString(grundbedarf)).append("\n");
    sb.append("    effektiveWohnkosten: ").append(toIndentedString(effektiveWohnkosten)).append("\n");
    sb.append("    medizinischeGrundversorgung: ").append(toIndentedString(medizinischeGrundversorgung)).append("\n");
    sb.append("    integrationszulage: ").append(toIndentedString(integrationszulage)).append("\n");
    sb.append("    steuernKantonGemeinde: ").append(toIndentedString(steuernKantonGemeinde)).append("\n");
    sb.append("    steuernBund: ").append(toIndentedString(steuernBund)).append("\n");
    sb.append("    steuernStaat: ").append(toIndentedString(steuernStaat)).append("\n");
    sb.append("    fahrkostenPerson1: ").append(toIndentedString(fahrkostenPerson1)).append("\n");
    sb.append("    fahrkostenPerson2: ").append(toIndentedString(fahrkostenPerson2)).append("\n");
    sb.append("    essenskostenPerson1: ").append(toIndentedString(essenskostenPerson1)).append("\n");
    sb.append("    essenskostenPerson2: ").append(toIndentedString(essenskostenPerson2)).append("\n");
    sb.append("    ausgabenFamilienbudget: ").append(toIndentedString(ausgabenFamilienbudget)).append("\n");
    sb.append("    familienbudgetBerechnet: ").append(toIndentedString(familienbudgetBerechnet)).append("\n");
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
