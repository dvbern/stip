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
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDtoSpec;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatDtoSpec;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Resultat der Berechnung (eine Tranche)
 */
@JsonPropertyOrder({
  BerechnungsresultatDtoSpec.JSON_PROPERTY_BERECHNUNG,
  BerechnungsresultatDtoSpec.JSON_PROPERTY_PERSOENLICHES_BUDGETRESULTAT,
  BerechnungsresultatDtoSpec.JSON_PROPERTY_FAMILIEN_BUDGETRESULTATE
})
@JsonTypeName("Berechnungsresultat")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class BerechnungsresultatDtoSpec {
  public static final String JSON_PROPERTY_BERECHNUNG = "berechnung";
  private Integer berechnung;

  public static final String JSON_PROPERTY_PERSOENLICHES_BUDGETRESULTAT = "persoenlichesBudgetresultat";
  private PersoenlichesBudgetresultatDtoSpec persoenlichesBudgetresultat;

  public static final String JSON_PROPERTY_FAMILIEN_BUDGETRESULTATE = "familienBudgetresultate";
  private List<FamilienBudgetresultatDtoSpec> familienBudgetresultate;

  public BerechnungsresultatDtoSpec() {
  }

  public BerechnungsresultatDtoSpec berechnung(Integer berechnung) {
    
    this.berechnung = berechnung;
    return this;
  }

   /**
   * Berechneter Stpendiumsanspruch für diese Tranche
   * @return berechnung
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_BERECHNUNG)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Integer getBerechnung() {
    return berechnung;
  }


  @JsonProperty(JSON_PROPERTY_BERECHNUNG)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setBerechnung(Integer berechnung) {
    this.berechnung = berechnung;
  }


  public BerechnungsresultatDtoSpec persoenlichesBudgetresultat(PersoenlichesBudgetresultatDtoSpec persoenlichesBudgetresultat) {
    
    this.persoenlichesBudgetresultat = persoenlichesBudgetresultat;
    return this;
  }

   /**
   * Get persoenlichesBudgetresultat
   * @return persoenlichesBudgetresultat
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_PERSOENLICHES_BUDGETRESULTAT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public PersoenlichesBudgetresultatDtoSpec getPersoenlichesBudgetresultat() {
    return persoenlichesBudgetresultat;
  }


  @JsonProperty(JSON_PROPERTY_PERSOENLICHES_BUDGETRESULTAT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setPersoenlichesBudgetresultat(PersoenlichesBudgetresultatDtoSpec persoenlichesBudgetresultat) {
    this.persoenlichesBudgetresultat = persoenlichesBudgetresultat;
  }


  public BerechnungsresultatDtoSpec familienBudgetresultate(List<FamilienBudgetresultatDtoSpec> familienBudgetresultate) {
    
    this.familienBudgetresultate = familienBudgetresultate;
    return this;
  }

  public BerechnungsresultatDtoSpec addFamilienBudgetresultateItem(FamilienBudgetresultatDtoSpec familienBudgetresultateItem) {
    if (this.familienBudgetresultate == null) {
      this.familienBudgetresultate = new ArrayList<>();
    }
    this.familienBudgetresultate.add(familienBudgetresultateItem);
    return this;
  }

   /**
   * Berechnungsdaten der Familienbudgets
   * @return familienBudgetresultate
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_FAMILIEN_BUDGETRESULTATE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public List<FamilienBudgetresultatDtoSpec> getFamilienBudgetresultate() {
    return familienBudgetresultate;
  }


  @JsonProperty(JSON_PROPERTY_FAMILIEN_BUDGETRESULTATE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setFamilienBudgetresultate(List<FamilienBudgetresultatDtoSpec> familienBudgetresultate) {
    this.familienBudgetresultate = familienBudgetresultate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BerechnungsresultatDtoSpec berechnungsresultat = (BerechnungsresultatDtoSpec) o;
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
    sb.append("class BerechnungsresultatDtoSpec {\n");
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

