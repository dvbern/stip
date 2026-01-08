package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.BerechnungsStammdatenDto;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Resultat der Berechnung (eine Tranche)
 **/

@JsonTypeName("TranchenBerechnungsresultat")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class TranchenBerechnungsresultatDto  implements Serializable {
  private @Valid Integer berechnung;
  private @Valid LocalDate gueltigAb;
  private @Valid LocalDate gueltigBis;
  private @Valid String ausbildungAb;
  private @Valid String ausbildungBis;
  private @Valid UUID gesuchTrancheId;
  private @Valid BigDecimal berechnungsanteilKinder;
  private @Valid BerechnungsStammdatenDto berechnungsStammdaten;
  private @Valid PersoenlichesBudgetresultatDto persoenlichesBudgetresultat;
  private @Valid List<FamilienBudgetresultatDto> familienBudgetresultate = new ArrayList<>();

  /**
   * Berechneter Stpendiumsanspruch für diese Tranche
   **/
  public TranchenBerechnungsresultatDto berechnung(Integer berechnung) {
    this.berechnung = berechnung;
    return this;
  }

  
  @JsonProperty("berechnung")
  @NotNull
  public Integer getBerechnung() {
    return berechnung;
  }

  @JsonProperty("berechnung")
  public void setBerechnung(Integer berechnung) {
    this.berechnung = berechnung;
  }

  /**
   **/
  public TranchenBerechnungsresultatDto gueltigAb(LocalDate gueltigAb) {
    this.gueltigAb = gueltigAb;
    return this;
  }

  
  @JsonProperty("gueltigAb")
  @NotNull
  public LocalDate getGueltigAb() {
    return gueltigAb;
  }

  @JsonProperty("gueltigAb")
  public void setGueltigAb(LocalDate gueltigAb) {
    this.gueltigAb = gueltigAb;
  }

  /**
   **/
  public TranchenBerechnungsresultatDto gueltigBis(LocalDate gueltigBis) {
    this.gueltigBis = gueltigBis;
    return this;
  }

  
  @JsonProperty("gueltigBis")
  @NotNull
  public LocalDate getGueltigBis() {
    return gueltigBis;
  }

  @JsonProperty("gueltigBis")
  public void setGueltigBis(LocalDate gueltigBis) {
    this.gueltigBis = gueltigBis;
  }

  /**
   **/
  public TranchenBerechnungsresultatDto ausbildungAb(String ausbildungAb) {
    this.ausbildungAb = ausbildungAb;
    return this;
  }

  
  @JsonProperty("ausbildungAb")
  @NotNull
  public String getAusbildungAb() {
    return ausbildungAb;
  }

  @JsonProperty("ausbildungAb")
  public void setAusbildungAb(String ausbildungAb) {
    this.ausbildungAb = ausbildungAb;
  }

  /**
   **/
  public TranchenBerechnungsresultatDto ausbildungBis(String ausbildungBis) {
    this.ausbildungBis = ausbildungBis;
    return this;
  }

  
  @JsonProperty("ausbildungBis")
  @NotNull
  public String getAusbildungBis() {
    return ausbildungBis;
  }

  @JsonProperty("ausbildungBis")
  public void setAusbildungBis(String ausbildungBis) {
    this.ausbildungBis = ausbildungBis;
  }

  /**
   **/
  public TranchenBerechnungsresultatDto gesuchTrancheId(UUID gesuchTrancheId) {
    this.gesuchTrancheId = gesuchTrancheId;
    return this;
  }

  
  @JsonProperty("gesuchTrancheId")
  @NotNull
  public UUID getGesuchTrancheId() {
    return gesuchTrancheId;
  }

  @JsonProperty("gesuchTrancheId")
  public void setGesuchTrancheId(UUID gesuchTrancheId) {
    this.gesuchTrancheId = gesuchTrancheId;
  }

  /**
   * Anteil dieser Berechnung am Berechnungstotal. Für Tranchen welche nur eine Berechnung haben ist dieser wert &#x3D;&#x3D; 1.
   **/
  public TranchenBerechnungsresultatDto berechnungsanteilKinder(BigDecimal berechnungsanteilKinder) {
    this.berechnungsanteilKinder = berechnungsanteilKinder;
    return this;
  }

  
  @JsonProperty("berechnungsanteilKinder")
  @NotNull
  public BigDecimal getBerechnungsanteilKinder() {
    return berechnungsanteilKinder;
  }

  @JsonProperty("berechnungsanteilKinder")
  public void setBerechnungsanteilKinder(BigDecimal berechnungsanteilKinder) {
    this.berechnungsanteilKinder = berechnungsanteilKinder;
  }

  /**
   **/
  public TranchenBerechnungsresultatDto berechnungsStammdaten(BerechnungsStammdatenDto berechnungsStammdaten) {
    this.berechnungsStammdaten = berechnungsStammdaten;
    return this;
  }

  
  @JsonProperty("berechnungsStammdaten")
  @NotNull
  public BerechnungsStammdatenDto getBerechnungsStammdaten() {
    return berechnungsStammdaten;
  }

  @JsonProperty("berechnungsStammdaten")
  public void setBerechnungsStammdaten(BerechnungsStammdatenDto berechnungsStammdaten) {
    this.berechnungsStammdaten = berechnungsStammdaten;
  }

  /**
   **/
  public TranchenBerechnungsresultatDto persoenlichesBudgetresultat(PersoenlichesBudgetresultatDto persoenlichesBudgetresultat) {
    this.persoenlichesBudgetresultat = persoenlichesBudgetresultat;
    return this;
  }

  
  @JsonProperty("persoenlichesBudgetresultat")
  @NotNull
  public PersoenlichesBudgetresultatDto getPersoenlichesBudgetresultat() {
    return persoenlichesBudgetresultat;
  }

  @JsonProperty("persoenlichesBudgetresultat")
  public void setPersoenlichesBudgetresultat(PersoenlichesBudgetresultatDto persoenlichesBudgetresultat) {
    this.persoenlichesBudgetresultat = persoenlichesBudgetresultat;
  }

  /**
   * Berechnungsdaten der Familienbudgets
   **/
  public TranchenBerechnungsresultatDto familienBudgetresultate(List<FamilienBudgetresultatDto> familienBudgetresultate) {
    this.familienBudgetresultate = familienBudgetresultate;
    return this;
  }

  
  @JsonProperty("familienBudgetresultate")
  @NotNull
  public List<FamilienBudgetresultatDto> getFamilienBudgetresultate() {
    return familienBudgetresultate;
  }

  @JsonProperty("familienBudgetresultate")
  public void setFamilienBudgetresultate(List<FamilienBudgetresultatDto> familienBudgetresultate) {
    this.familienBudgetresultate = familienBudgetresultate;
  }

  public TranchenBerechnungsresultatDto addFamilienBudgetresultateItem(FamilienBudgetresultatDto familienBudgetresultateItem) {
    if (this.familienBudgetresultate == null) {
      this.familienBudgetresultate = new ArrayList<>();
    }

    this.familienBudgetresultate.add(familienBudgetresultateItem);
    return this;
  }

  public TranchenBerechnungsresultatDto removeFamilienBudgetresultateItem(FamilienBudgetresultatDto familienBudgetresultateItem) {
    if (familienBudgetresultateItem != null && this.familienBudgetresultate != null) {
      this.familienBudgetresultate.remove(familienBudgetresultateItem);
    }

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TranchenBerechnungsresultatDto tranchenBerechnungsresultat = (TranchenBerechnungsresultatDto) o;
    return Objects.equals(this.berechnung, tranchenBerechnungsresultat.berechnung) &&
        Objects.equals(this.gueltigAb, tranchenBerechnungsresultat.gueltigAb) &&
        Objects.equals(this.gueltigBis, tranchenBerechnungsresultat.gueltigBis) &&
        Objects.equals(this.ausbildungAb, tranchenBerechnungsresultat.ausbildungAb) &&
        Objects.equals(this.ausbildungBis, tranchenBerechnungsresultat.ausbildungBis) &&
        Objects.equals(this.gesuchTrancheId, tranchenBerechnungsresultat.gesuchTrancheId) &&
        Objects.equals(this.berechnungsanteilKinder, tranchenBerechnungsresultat.berechnungsanteilKinder) &&
        Objects.equals(this.berechnungsStammdaten, tranchenBerechnungsresultat.berechnungsStammdaten) &&
        Objects.equals(this.persoenlichesBudgetresultat, tranchenBerechnungsresultat.persoenlichesBudgetresultat) &&
        Objects.equals(this.familienBudgetresultate, tranchenBerechnungsresultat.familienBudgetresultate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(berechnung, gueltigAb, gueltigBis, ausbildungAb, ausbildungBis, gesuchTrancheId, berechnungsanteilKinder, berechnungsStammdaten, persoenlichesBudgetresultat, familienBudgetresultate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TranchenBerechnungsresultatDto {\n");
    
    sb.append("    berechnung: ").append(toIndentedString(berechnung)).append("\n");
    sb.append("    gueltigAb: ").append(toIndentedString(gueltigAb)).append("\n");
    sb.append("    gueltigBis: ").append(toIndentedString(gueltigBis)).append("\n");
    sb.append("    ausbildungAb: ").append(toIndentedString(ausbildungAb)).append("\n");
    sb.append("    ausbildungBis: ").append(toIndentedString(ausbildungBis)).append("\n");
    sb.append("    gesuchTrancheId: ").append(toIndentedString(gesuchTrancheId)).append("\n");
    sb.append("    berechnungsanteilKinder: ").append(toIndentedString(berechnungsanteilKinder)).append("\n");
    sb.append("    berechnungsStammdaten: ").append(toIndentedString(berechnungsStammdaten)).append("\n");
    sb.append("    persoenlichesBudgetresultat: ").append(toIndentedString(persoenlichesBudgetresultat)).append("\n");
    sb.append("    familienBudgetresultate: ").append(toIndentedString(familienBudgetresultate)).append("\n");
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

