package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

@JsonTypeName("Berechnungsresultat")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class BerechnungsresultatDto  implements Serializable {
  private @Valid Integer berechnung;
  private @Valid PersoenlichesBudgetresultatDto persoenlichesBudgetresultat;
  private @Valid List<FamilienBudgetresultatDto> familienBudgetresultate;

  /**
   * Berechneter Stpendiumsanspruch für diese Tranche
   **/
  public BerechnungsresultatDto berechnung(Integer berechnung) {
    this.berechnung = berechnung;
    return this;
  }

  
  @JsonProperty("berechnung")
  public Integer getBerechnung() {
    return berechnung;
  }

  @JsonProperty("berechnung")
  public void setBerechnung(Integer berechnung) {
    this.berechnung = berechnung;
  }

  /**
   **/
  public BerechnungsresultatDto persoenlichesBudgetresultat(PersoenlichesBudgetresultatDto persoenlichesBudgetresultat) {
    this.persoenlichesBudgetresultat = persoenlichesBudgetresultat;
    return this;
  }

  
  @JsonProperty("persoenlichesBudgetresultat")
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
  public BerechnungsresultatDto familienBudgetresultate(List<FamilienBudgetresultatDto> familienBudgetresultate) {
    this.familienBudgetresultate = familienBudgetresultate;
    return this;
  }

  
  @JsonProperty("familienBudgetresultate")
  public List<FamilienBudgetresultatDto> getFamilienBudgetresultate() {
    return familienBudgetresultate;
  }

  @JsonProperty("familienBudgetresultate")
  public void setFamilienBudgetresultate(List<FamilienBudgetresultatDto> familienBudgetresultate) {
    this.familienBudgetresultate = familienBudgetresultate;
  }

  public BerechnungsresultatDto addFamilienBudgetresultateItem(FamilienBudgetresultatDto familienBudgetresultateItem) {
    if (this.familienBudgetresultate == null) {
      this.familienBudgetresultate = new ArrayList<>();
    }

    this.familienBudgetresultate.add(familienBudgetresultateItem);
    return this;
  }

  public BerechnungsresultatDto removeFamilienBudgetresultateItem(FamilienBudgetresultatDto familienBudgetresultateItem) {
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
    BerechnungsresultatDto berechnungsresultat = (BerechnungsresultatDto) o;
    return Objects.equals(this.berechnung, berechnungsresultat.berechnung) &&
        Objects.equals(this.persoenlichesBudgetresultat, berechnungsresultat.persoenlichesBudgetresultat) &&
        Objects.equals(this.familienBudgetresultate, berechnungsresultat.familienBudgetresultate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(berechnung, persoenlichesBudgetresultat, familienBudgetresultate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BerechnungsresultatDto {\n");
    
    sb.append("    berechnung: ").append(toIndentedString(berechnung)).append("\n");
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

