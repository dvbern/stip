package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Familien Budget daten fuer und von der Berechnung
 **/

@JsonTypeName("FamilienBudgetresultat")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class FamilienBudgetresultatDto  implements Serializable {
  private @Valid ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp familienBudgetTyp;
  private @Valid Boolean selbststaendigErwerbend;
  private @Valid Integer anzahlPersonenImHaushalt;
  private @Valid Integer anzahlGeschwisterInAusbildung;
  private @Valid Integer totalEinkuenfte;
  private @Valid Integer ergaenzungsleistungen;
  private @Valid Integer steuerbaresVermoegen;
  private @Valid Integer anrechenbaresVermoegen;
  private @Valid Integer saeule2;
  private @Valid Integer saeule3a;
  private @Valid Integer eigenmietwert;
  private @Valid Integer alimente;
  private @Valid Integer einnahmenFamilienbudget;
  private @Valid Integer grundbedarf;
  private @Valid Integer effektiveWohnkosten;
  private @Valid Integer medizinischeGrundversorgung;
  private @Valid Integer integrationszulage;
  private @Valid Integer steuernBund;
  private @Valid Integer steuernKantonGemeinde;
  private @Valid Integer fahrkostenPerson1;
  private @Valid Integer fahrkostenPerson2;
  private @Valid Integer essenskostenPerson1;
  private @Valid Integer essenskostenPerson2;
  private @Valid Integer ausgabenFamilienbudget;
  private @Valid Integer familienbudgetBerechnet;

  /**
   **/
  public FamilienBudgetresultatDto familienBudgetTyp(ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp familienBudgetTyp) {
    this.familienBudgetTyp = familienBudgetTyp;
    return this;
  }

  
  @JsonProperty("familienBudgetTyp")
  @NotNull
  public ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp getFamilienBudgetTyp() {
    return familienBudgetTyp;
  }

  @JsonProperty("familienBudgetTyp")
  public void setFamilienBudgetTyp(ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp familienBudgetTyp) {
    this.familienBudgetTyp = familienBudgetTyp;
  }

  /**
   **/
  public FamilienBudgetresultatDto selbststaendigErwerbend(Boolean selbststaendigErwerbend) {
    this.selbststaendigErwerbend = selbststaendigErwerbend;
    return this;
  }

  
  @JsonProperty("selbststaendigErwerbend")
  @NotNull
  public Boolean getSelbststaendigErwerbend() {
    return selbststaendigErwerbend;
  }

  @JsonProperty("selbststaendigErwerbend")
  public void setSelbststaendigErwerbend(Boolean selbststaendigErwerbend) {
    this.selbststaendigErwerbend = selbststaendigErwerbend;
  }

  /**
   **/
  public FamilienBudgetresultatDto anzahlPersonenImHaushalt(Integer anzahlPersonenImHaushalt) {
    this.anzahlPersonenImHaushalt = anzahlPersonenImHaushalt;
    return this;
  }

  
  @JsonProperty("anzahlPersonenImHaushalt")
  @NotNull
  public Integer getAnzahlPersonenImHaushalt() {
    return anzahlPersonenImHaushalt;
  }

  @JsonProperty("anzahlPersonenImHaushalt")
  public void setAnzahlPersonenImHaushalt(Integer anzahlPersonenImHaushalt) {
    this.anzahlPersonenImHaushalt = anzahlPersonenImHaushalt;
  }

  /**
   **/
  public FamilienBudgetresultatDto anzahlGeschwisterInAusbildung(Integer anzahlGeschwisterInAusbildung) {
    this.anzahlGeschwisterInAusbildung = anzahlGeschwisterInAusbildung;
    return this;
  }

  
  @JsonProperty("anzahlGeschwisterInAusbildung")
  @NotNull
  public Integer getAnzahlGeschwisterInAusbildung() {
    return anzahlGeschwisterInAusbildung;
  }

  @JsonProperty("anzahlGeschwisterInAusbildung")
  public void setAnzahlGeschwisterInAusbildung(Integer anzahlGeschwisterInAusbildung) {
    this.anzahlGeschwisterInAusbildung = anzahlGeschwisterInAusbildung;
  }

  /**
   **/
  public FamilienBudgetresultatDto totalEinkuenfte(Integer totalEinkuenfte) {
    this.totalEinkuenfte = totalEinkuenfte;
    return this;
  }

  
  @JsonProperty("totalEinkuenfte")
  @NotNull
  public Integer getTotalEinkuenfte() {
    return totalEinkuenfte;
  }

  @JsonProperty("totalEinkuenfte")
  public void setTotalEinkuenfte(Integer totalEinkuenfte) {
    this.totalEinkuenfte = totalEinkuenfte;
  }

  /**
   **/
  public FamilienBudgetresultatDto ergaenzungsleistungen(Integer ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
    return this;
  }

  
  @JsonProperty("ergaenzungsleistungen")
  @NotNull
  public Integer getErgaenzungsleistungen() {
    return ergaenzungsleistungen;
  }

  @JsonProperty("ergaenzungsleistungen")
  public void setErgaenzungsleistungen(Integer ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
  }

  /**
   **/
  public FamilienBudgetresultatDto steuerbaresVermoegen(Integer steuerbaresVermoegen) {
    this.steuerbaresVermoegen = steuerbaresVermoegen;
    return this;
  }

  
  @JsonProperty("steuerbaresVermoegen")
  @NotNull
  public Integer getSteuerbaresVermoegen() {
    return steuerbaresVermoegen;
  }

  @JsonProperty("steuerbaresVermoegen")
  public void setSteuerbaresVermoegen(Integer steuerbaresVermoegen) {
    this.steuerbaresVermoegen = steuerbaresVermoegen;
  }

  /**
   **/
  public FamilienBudgetresultatDto anrechenbaresVermoegen(Integer anrechenbaresVermoegen) {
    this.anrechenbaresVermoegen = anrechenbaresVermoegen;
    return this;
  }

  
  @JsonProperty("anrechenbaresVermoegen")
  @NotNull
  public Integer getAnrechenbaresVermoegen() {
    return anrechenbaresVermoegen;
  }

  @JsonProperty("anrechenbaresVermoegen")
  public void setAnrechenbaresVermoegen(Integer anrechenbaresVermoegen) {
    this.anrechenbaresVermoegen = anrechenbaresVermoegen;
  }

  /**
   **/
  public FamilienBudgetresultatDto saeule2(Integer saeule2) {
    this.saeule2 = saeule2;
    return this;
  }

  
  @JsonProperty("saeule2")
  @NotNull
  public Integer getSaeule2() {
    return saeule2;
  }

  @JsonProperty("saeule2")
  public void setSaeule2(Integer saeule2) {
    this.saeule2 = saeule2;
  }

  /**
   **/
  public FamilienBudgetresultatDto saeule3a(Integer saeule3a) {
    this.saeule3a = saeule3a;
    return this;
  }

  
  @JsonProperty("saeule3a")
  @NotNull
  public Integer getSaeule3a() {
    return saeule3a;
  }

  @JsonProperty("saeule3a")
  public void setSaeule3a(Integer saeule3a) {
    this.saeule3a = saeule3a;
  }

  /**
   **/
  public FamilienBudgetresultatDto eigenmietwert(Integer eigenmietwert) {
    this.eigenmietwert = eigenmietwert;
    return this;
  }

  
  @JsonProperty("eigenmietwert")
  @NotNull
  public Integer getEigenmietwert() {
    return eigenmietwert;
  }

  @JsonProperty("eigenmietwert")
  public void setEigenmietwert(Integer eigenmietwert) {
    this.eigenmietwert = eigenmietwert;
  }

  /**
   **/
  public FamilienBudgetresultatDto alimente(Integer alimente) {
    this.alimente = alimente;
    return this;
  }

  
  @JsonProperty("alimente")
  @NotNull
  public Integer getAlimente() {
    return alimente;
  }

  @JsonProperty("alimente")
  public void setAlimente(Integer alimente) {
    this.alimente = alimente;
  }

  /**
   **/
  public FamilienBudgetresultatDto einnahmenFamilienbudget(Integer einnahmenFamilienbudget) {
    this.einnahmenFamilienbudget = einnahmenFamilienbudget;
    return this;
  }

  
  @JsonProperty("einnahmenFamilienbudget")
  @NotNull
  public Integer getEinnahmenFamilienbudget() {
    return einnahmenFamilienbudget;
  }

  @JsonProperty("einnahmenFamilienbudget")
  public void setEinnahmenFamilienbudget(Integer einnahmenFamilienbudget) {
    this.einnahmenFamilienbudget = einnahmenFamilienbudget;
  }

  /**
   **/
  public FamilienBudgetresultatDto grundbedarf(Integer grundbedarf) {
    this.grundbedarf = grundbedarf;
    return this;
  }

  
  @JsonProperty("grundbedarf")
  @NotNull
  public Integer getGrundbedarf() {
    return grundbedarf;
  }

  @JsonProperty("grundbedarf")
  public void setGrundbedarf(Integer grundbedarf) {
    this.grundbedarf = grundbedarf;
  }

  /**
   **/
  public FamilienBudgetresultatDto effektiveWohnkosten(Integer effektiveWohnkosten) {
    this.effektiveWohnkosten = effektiveWohnkosten;
    return this;
  }

  
  @JsonProperty("effektiveWohnkosten")
  @NotNull
  public Integer getEffektiveWohnkosten() {
    return effektiveWohnkosten;
  }

  @JsonProperty("effektiveWohnkosten")
  public void setEffektiveWohnkosten(Integer effektiveWohnkosten) {
    this.effektiveWohnkosten = effektiveWohnkosten;
  }

  /**
   **/
  public FamilienBudgetresultatDto medizinischeGrundversorgung(Integer medizinischeGrundversorgung) {
    this.medizinischeGrundversorgung = medizinischeGrundversorgung;
    return this;
  }

  
  @JsonProperty("medizinischeGrundversorgung")
  @NotNull
  public Integer getMedizinischeGrundversorgung() {
    return medizinischeGrundversorgung;
  }

  @JsonProperty("medizinischeGrundversorgung")
  public void setMedizinischeGrundversorgung(Integer medizinischeGrundversorgung) {
    this.medizinischeGrundversorgung = medizinischeGrundversorgung;
  }

  /**
   **/
  public FamilienBudgetresultatDto integrationszulage(Integer integrationszulage) {
    this.integrationszulage = integrationszulage;
    return this;
  }

  
  @JsonProperty("integrationszulage")
  @NotNull
  public Integer getIntegrationszulage() {
    return integrationszulage;
  }

  @JsonProperty("integrationszulage")
  public void setIntegrationszulage(Integer integrationszulage) {
    this.integrationszulage = integrationszulage;
  }

  /**
   **/
  public FamilienBudgetresultatDto steuernBund(Integer steuernBund) {
    this.steuernBund = steuernBund;
    return this;
  }

  
  @JsonProperty("steuernBund")
  @NotNull
  public Integer getSteuernBund() {
    return steuernBund;
  }

  @JsonProperty("steuernBund")
  public void setSteuernBund(Integer steuernBund) {
    this.steuernBund = steuernBund;
  }

  /**
   **/
  public FamilienBudgetresultatDto steuernKantonGemeinde(Integer steuernKantonGemeinde) {
    this.steuernKantonGemeinde = steuernKantonGemeinde;
    return this;
  }

  
  @JsonProperty("steuernKantonGemeinde")
  @NotNull
  public Integer getSteuernKantonGemeinde() {
    return steuernKantonGemeinde;
  }

  @JsonProperty("steuernKantonGemeinde")
  public void setSteuernKantonGemeinde(Integer steuernKantonGemeinde) {
    this.steuernKantonGemeinde = steuernKantonGemeinde;
  }

  /**
   **/
  public FamilienBudgetresultatDto fahrkostenPerson1(Integer fahrkostenPerson1) {
    this.fahrkostenPerson1 = fahrkostenPerson1;
    return this;
  }

  
  @JsonProperty("fahrkostenPerson1")
  @NotNull
  public Integer getFahrkostenPerson1() {
    return fahrkostenPerson1;
  }

  @JsonProperty("fahrkostenPerson1")
  public void setFahrkostenPerson1(Integer fahrkostenPerson1) {
    this.fahrkostenPerson1 = fahrkostenPerson1;
  }

  /**
   **/
  public FamilienBudgetresultatDto fahrkostenPerson2(Integer fahrkostenPerson2) {
    this.fahrkostenPerson2 = fahrkostenPerson2;
    return this;
  }

  
  @JsonProperty("fahrkostenPerson2")
  @NotNull
  public Integer getFahrkostenPerson2() {
    return fahrkostenPerson2;
  }

  @JsonProperty("fahrkostenPerson2")
  public void setFahrkostenPerson2(Integer fahrkostenPerson2) {
    this.fahrkostenPerson2 = fahrkostenPerson2;
  }

  /**
   **/
  public FamilienBudgetresultatDto essenskostenPerson1(Integer essenskostenPerson1) {
    this.essenskostenPerson1 = essenskostenPerson1;
    return this;
  }

  
  @JsonProperty("essenskostenPerson1")
  @NotNull
  public Integer getEssenskostenPerson1() {
    return essenskostenPerson1;
  }

  @JsonProperty("essenskostenPerson1")
  public void setEssenskostenPerson1(Integer essenskostenPerson1) {
    this.essenskostenPerson1 = essenskostenPerson1;
  }

  /**
   **/
  public FamilienBudgetresultatDto essenskostenPerson2(Integer essenskostenPerson2) {
    this.essenskostenPerson2 = essenskostenPerson2;
    return this;
  }

  
  @JsonProperty("essenskostenPerson2")
  @NotNull
  public Integer getEssenskostenPerson2() {
    return essenskostenPerson2;
  }

  @JsonProperty("essenskostenPerson2")
  public void setEssenskostenPerson2(Integer essenskostenPerson2) {
    this.essenskostenPerson2 = essenskostenPerson2;
  }

  /**
   **/
  public FamilienBudgetresultatDto ausgabenFamilienbudget(Integer ausgabenFamilienbudget) {
    this.ausgabenFamilienbudget = ausgabenFamilienbudget;
    return this;
  }

  
  @JsonProperty("ausgabenFamilienbudget")
  @NotNull
  public Integer getAusgabenFamilienbudget() {
    return ausgabenFamilienbudget;
  }

  @JsonProperty("ausgabenFamilienbudget")
  public void setAusgabenFamilienbudget(Integer ausgabenFamilienbudget) {
    this.ausgabenFamilienbudget = ausgabenFamilienbudget;
  }

  /**
   **/
  public FamilienBudgetresultatDto familienbudgetBerechnet(Integer familienbudgetBerechnet) {
    this.familienbudgetBerechnet = familienbudgetBerechnet;
    return this;
  }

  
  @JsonProperty("familienbudgetBerechnet")
  @NotNull
  public Integer getFamilienbudgetBerechnet() {
    return familienbudgetBerechnet;
  }

  @JsonProperty("familienbudgetBerechnet")
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
    FamilienBudgetresultatDto familienBudgetresultat = (FamilienBudgetresultatDto) o;
    return Objects.equals(this.familienBudgetTyp, familienBudgetresultat.familienBudgetTyp) &&
        Objects.equals(this.selbststaendigErwerbend, familienBudgetresultat.selbststaendigErwerbend) &&
        Objects.equals(this.anzahlPersonenImHaushalt, familienBudgetresultat.anzahlPersonenImHaushalt) &&
        Objects.equals(this.anzahlGeschwisterInAusbildung, familienBudgetresultat.anzahlGeschwisterInAusbildung) &&
        Objects.equals(this.totalEinkuenfte, familienBudgetresultat.totalEinkuenfte) &&
        Objects.equals(this.ergaenzungsleistungen, familienBudgetresultat.ergaenzungsleistungen) &&
        Objects.equals(this.steuerbaresVermoegen, familienBudgetresultat.steuerbaresVermoegen) &&
        Objects.equals(this.anrechenbaresVermoegen, familienBudgetresultat.anrechenbaresVermoegen) &&
        Objects.equals(this.saeule2, familienBudgetresultat.saeule2) &&
        Objects.equals(this.saeule3a, familienBudgetresultat.saeule3a) &&
        Objects.equals(this.eigenmietwert, familienBudgetresultat.eigenmietwert) &&
        Objects.equals(this.alimente, familienBudgetresultat.alimente) &&
        Objects.equals(this.einnahmenFamilienbudget, familienBudgetresultat.einnahmenFamilienbudget) &&
        Objects.equals(this.grundbedarf, familienBudgetresultat.grundbedarf) &&
        Objects.equals(this.effektiveWohnkosten, familienBudgetresultat.effektiveWohnkosten) &&
        Objects.equals(this.medizinischeGrundversorgung, familienBudgetresultat.medizinischeGrundversorgung) &&
        Objects.equals(this.integrationszulage, familienBudgetresultat.integrationszulage) &&
        Objects.equals(this.steuernBund, familienBudgetresultat.steuernBund) &&
        Objects.equals(this.steuernKantonGemeinde, familienBudgetresultat.steuernKantonGemeinde) &&
        Objects.equals(this.fahrkostenPerson1, familienBudgetresultat.fahrkostenPerson1) &&
        Objects.equals(this.fahrkostenPerson2, familienBudgetresultat.fahrkostenPerson2) &&
        Objects.equals(this.essenskostenPerson1, familienBudgetresultat.essenskostenPerson1) &&
        Objects.equals(this.essenskostenPerson2, familienBudgetresultat.essenskostenPerson2) &&
        Objects.equals(this.ausgabenFamilienbudget, familienBudgetresultat.ausgabenFamilienbudget) &&
        Objects.equals(this.familienbudgetBerechnet, familienBudgetresultat.familienbudgetBerechnet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(familienBudgetTyp, selbststaendigErwerbend, anzahlPersonenImHaushalt, anzahlGeschwisterInAusbildung, totalEinkuenfte, ergaenzungsleistungen, steuerbaresVermoegen, anrechenbaresVermoegen, saeule2, saeule3a, eigenmietwert, alimente, einnahmenFamilienbudget, grundbedarf, effektiveWohnkosten, medizinischeGrundversorgung, integrationszulage, steuernBund, steuernKantonGemeinde, fahrkostenPerson1, fahrkostenPerson2, essenskostenPerson1, essenskostenPerson2, ausgabenFamilienbudget, familienbudgetBerechnet);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FamilienBudgetresultatDto {\n");
    
    sb.append("    familienBudgetTyp: ").append(toIndentedString(familienBudgetTyp)).append("\n");
    sb.append("    selbststaendigErwerbend: ").append(toIndentedString(selbststaendigErwerbend)).append("\n");
    sb.append("    anzahlPersonenImHaushalt: ").append(toIndentedString(anzahlPersonenImHaushalt)).append("\n");
    sb.append("    anzahlGeschwisterInAusbildung: ").append(toIndentedString(anzahlGeschwisterInAusbildung)).append("\n");
    sb.append("    totalEinkuenfte: ").append(toIndentedString(totalEinkuenfte)).append("\n");
    sb.append("    ergaenzungsleistungen: ").append(toIndentedString(ergaenzungsleistungen)).append("\n");
    sb.append("    steuerbaresVermoegen: ").append(toIndentedString(steuerbaresVermoegen)).append("\n");
    sb.append("    anrechenbaresVermoegen: ").append(toIndentedString(anrechenbaresVermoegen)).append("\n");
    sb.append("    saeule2: ").append(toIndentedString(saeule2)).append("\n");
    sb.append("    saeule3a: ").append(toIndentedString(saeule3a)).append("\n");
    sb.append("    eigenmietwert: ").append(toIndentedString(eigenmietwert)).append("\n");
    sb.append("    alimente: ").append(toIndentedString(alimente)).append("\n");
    sb.append("    einnahmenFamilienbudget: ").append(toIndentedString(einnahmenFamilienbudget)).append("\n");
    sb.append("    grundbedarf: ").append(toIndentedString(grundbedarf)).append("\n");
    sb.append("    effektiveWohnkosten: ").append(toIndentedString(effektiveWohnkosten)).append("\n");
    sb.append("    medizinischeGrundversorgung: ").append(toIndentedString(medizinischeGrundversorgung)).append("\n");
    sb.append("    integrationszulage: ").append(toIndentedString(integrationszulage)).append("\n");
    sb.append("    steuernBund: ").append(toIndentedString(steuernBund)).append("\n");
    sb.append("    steuernKantonGemeinde: ").append(toIndentedString(steuernKantonGemeinde)).append("\n");
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

