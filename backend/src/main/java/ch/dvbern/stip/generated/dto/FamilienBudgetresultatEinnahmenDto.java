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



@JsonTypeName("FamilienBudgetresultatEinnahmen")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class FamilienBudgetresultatEinnahmenDto  implements Serializable {
  private @Valid Integer total;
  private @Valid Integer totalEinkuenfte;
  private @Valid Integer einnahmenBGSA;
  private @Valid Integer ergaenzungsleistungen;
  private @Valid Integer andereEinnahmen;
  private @Valid Integer eigenmietwert;
  private @Valid Integer unterhaltsbeitraege;
  private @Valid Integer sauele3;
  private @Valid Integer sauele2;
  private @Valid Integer renten;
  private @Valid Integer einkommensfreibetrag;
  private @Valid Integer zwischentotal;
  private @Valid Integer anrechenbaresVermoegen;
  private @Valid Integer steuerbaresVermoegen;

  /**
   **/
  public FamilienBudgetresultatEinnahmenDto total(Integer total) {
    this.total = total;
    return this;
  }

  
  @JsonProperty("total")
  @NotNull
  public Integer getTotal() {
    return total;
  }

  @JsonProperty("total")
  public void setTotal(Integer total) {
    this.total = total;
  }

  /**
   **/
  public FamilienBudgetresultatEinnahmenDto totalEinkuenfte(Integer totalEinkuenfte) {
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
  public FamilienBudgetresultatEinnahmenDto einnahmenBGSA(Integer einnahmenBGSA) {
    this.einnahmenBGSA = einnahmenBGSA;
    return this;
  }

  
  @JsonProperty("einnahmenBGSA")
  @NotNull
  public Integer getEinnahmenBGSA() {
    return einnahmenBGSA;
  }

  @JsonProperty("einnahmenBGSA")
  public void setEinnahmenBGSA(Integer einnahmenBGSA) {
    this.einnahmenBGSA = einnahmenBGSA;
  }

  /**
   **/
  public FamilienBudgetresultatEinnahmenDto ergaenzungsleistungen(Integer ergaenzungsleistungen) {
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
  public FamilienBudgetresultatEinnahmenDto andereEinnahmen(Integer andereEinnahmen) {
    this.andereEinnahmen = andereEinnahmen;
    return this;
  }

  
  @JsonProperty("andereEinnahmen")
  @NotNull
  public Integer getAndereEinnahmen() {
    return andereEinnahmen;
  }

  @JsonProperty("andereEinnahmen")
  public void setAndereEinnahmen(Integer andereEinnahmen) {
    this.andereEinnahmen = andereEinnahmen;
  }

  /**
   **/
  public FamilienBudgetresultatEinnahmenDto eigenmietwert(Integer eigenmietwert) {
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
  public FamilienBudgetresultatEinnahmenDto unterhaltsbeitraege(Integer unterhaltsbeitraege) {
    this.unterhaltsbeitraege = unterhaltsbeitraege;
    return this;
  }

  
  @JsonProperty("unterhaltsbeitraege")
  @NotNull
  public Integer getUnterhaltsbeitraege() {
    return unterhaltsbeitraege;
  }

  @JsonProperty("unterhaltsbeitraege")
  public void setUnterhaltsbeitraege(Integer unterhaltsbeitraege) {
    this.unterhaltsbeitraege = unterhaltsbeitraege;
  }

  /**
   **/
  public FamilienBudgetresultatEinnahmenDto sauele3(Integer sauele3) {
    this.sauele3 = sauele3;
    return this;
  }

  
  @JsonProperty("sauele3")
  @NotNull
  public Integer getSauele3() {
    return sauele3;
  }

  @JsonProperty("sauele3")
  public void setSauele3(Integer sauele3) {
    this.sauele3 = sauele3;
  }

  /**
   **/
  public FamilienBudgetresultatEinnahmenDto sauele2(Integer sauele2) {
    this.sauele2 = sauele2;
    return this;
  }

  
  @JsonProperty("sauele2")
  @NotNull
  public Integer getSauele2() {
    return sauele2;
  }

  @JsonProperty("sauele2")
  public void setSauele2(Integer sauele2) {
    this.sauele2 = sauele2;
  }

  /**
   **/
  public FamilienBudgetresultatEinnahmenDto renten(Integer renten) {
    this.renten = renten;
    return this;
  }

  
  @JsonProperty("renten")
  @NotNull
  public Integer getRenten() {
    return renten;
  }

  @JsonProperty("renten")
  public void setRenten(Integer renten) {
    this.renten = renten;
  }

  /**
   **/
  public FamilienBudgetresultatEinnahmenDto einkommensfreibetrag(Integer einkommensfreibetrag) {
    this.einkommensfreibetrag = einkommensfreibetrag;
    return this;
  }

  
  @JsonProperty("einkommensfreibetrag")
  @NotNull
  public Integer getEinkommensfreibetrag() {
    return einkommensfreibetrag;
  }

  @JsonProperty("einkommensfreibetrag")
  public void setEinkommensfreibetrag(Integer einkommensfreibetrag) {
    this.einkommensfreibetrag = einkommensfreibetrag;
  }

  /**
   **/
  public FamilienBudgetresultatEinnahmenDto zwischentotal(Integer zwischentotal) {
    this.zwischentotal = zwischentotal;
    return this;
  }

  
  @JsonProperty("zwischentotal")
  @NotNull
  public Integer getZwischentotal() {
    return zwischentotal;
  }

  @JsonProperty("zwischentotal")
  public void setZwischentotal(Integer zwischentotal) {
    this.zwischentotal = zwischentotal;
  }

  /**
   **/
  public FamilienBudgetresultatEinnahmenDto anrechenbaresVermoegen(Integer anrechenbaresVermoegen) {
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
  public FamilienBudgetresultatEinnahmenDto steuerbaresVermoegen(Integer steuerbaresVermoegen) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FamilienBudgetresultatEinnahmenDto familienBudgetresultatEinnahmen = (FamilienBudgetresultatEinnahmenDto) o;
    return Objects.equals(this.total, familienBudgetresultatEinnahmen.total) &&
        Objects.equals(this.totalEinkuenfte, familienBudgetresultatEinnahmen.totalEinkuenfte) &&
        Objects.equals(this.einnahmenBGSA, familienBudgetresultatEinnahmen.einnahmenBGSA) &&
        Objects.equals(this.ergaenzungsleistungen, familienBudgetresultatEinnahmen.ergaenzungsleistungen) &&
        Objects.equals(this.andereEinnahmen, familienBudgetresultatEinnahmen.andereEinnahmen) &&
        Objects.equals(this.eigenmietwert, familienBudgetresultatEinnahmen.eigenmietwert) &&
        Objects.equals(this.unterhaltsbeitraege, familienBudgetresultatEinnahmen.unterhaltsbeitraege) &&
        Objects.equals(this.sauele3, familienBudgetresultatEinnahmen.sauele3) &&
        Objects.equals(this.sauele2, familienBudgetresultatEinnahmen.sauele2) &&
        Objects.equals(this.renten, familienBudgetresultatEinnahmen.renten) &&
        Objects.equals(this.einkommensfreibetrag, familienBudgetresultatEinnahmen.einkommensfreibetrag) &&
        Objects.equals(this.zwischentotal, familienBudgetresultatEinnahmen.zwischentotal) &&
        Objects.equals(this.anrechenbaresVermoegen, familienBudgetresultatEinnahmen.anrechenbaresVermoegen) &&
        Objects.equals(this.steuerbaresVermoegen, familienBudgetresultatEinnahmen.steuerbaresVermoegen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(total, totalEinkuenfte, einnahmenBGSA, ergaenzungsleistungen, andereEinnahmen, eigenmietwert, unterhaltsbeitraege, sauele3, sauele2, renten, einkommensfreibetrag, zwischentotal, anrechenbaresVermoegen, steuerbaresVermoegen);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FamilienBudgetresultatEinnahmenDto {\n");
    
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    totalEinkuenfte: ").append(toIndentedString(totalEinkuenfte)).append("\n");
    sb.append("    einnahmenBGSA: ").append(toIndentedString(einnahmenBGSA)).append("\n");
    sb.append("    ergaenzungsleistungen: ").append(toIndentedString(ergaenzungsleistungen)).append("\n");
    sb.append("    andereEinnahmen: ").append(toIndentedString(andereEinnahmen)).append("\n");
    sb.append("    eigenmietwert: ").append(toIndentedString(eigenmietwert)).append("\n");
    sb.append("    unterhaltsbeitraege: ").append(toIndentedString(unterhaltsbeitraege)).append("\n");
    sb.append("    sauele3: ").append(toIndentedString(sauele3)).append("\n");
    sb.append("    sauele2: ").append(toIndentedString(sauele2)).append("\n");
    sb.append("    renten: ").append(toIndentedString(renten)).append("\n");
    sb.append("    einkommensfreibetrag: ").append(toIndentedString(einkommensfreibetrag)).append("\n");
    sb.append("    zwischentotal: ").append(toIndentedString(zwischentotal)).append("\n");
    sb.append("    anrechenbaresVermoegen: ").append(toIndentedString(anrechenbaresVermoegen)).append("\n");
    sb.append("    steuerbaresVermoegen: ").append(toIndentedString(steuerbaresVermoegen)).append("\n");
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

