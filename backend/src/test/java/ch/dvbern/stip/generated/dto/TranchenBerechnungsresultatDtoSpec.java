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
import ch.dvbern.stip.generated.dto.BerechnungsStammdatenDtoSpec;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDtoSpec;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatDtoSpec;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Resultat der Berechnung (eine Tranche)
 */
@JsonPropertyOrder({
  TranchenBerechnungsresultatDtoSpec.JSON_PROPERTY_NAME_GESUCHSTELLER,
  TranchenBerechnungsresultatDtoSpec.JSON_PROPERTY_BERECHNUNG,
  TranchenBerechnungsresultatDtoSpec.JSON_PROPERTY_GUELTIG_AB,
  TranchenBerechnungsresultatDtoSpec.JSON_PROPERTY_GUELTIG_BIS,
  TranchenBerechnungsresultatDtoSpec.JSON_PROPERTY_GESUCH_TRANCHE_ID,
  TranchenBerechnungsresultatDtoSpec.JSON_PROPERTY_BERECHNUNGSANTEIL_KINDER,
  TranchenBerechnungsresultatDtoSpec.JSON_PROPERTY_BERECHNUNGS_STAMMDATEN,
  TranchenBerechnungsresultatDtoSpec.JSON_PROPERTY_PERSOENLICHES_BUDGETRESULTAT,
  TranchenBerechnungsresultatDtoSpec.JSON_PROPERTY_FAMILIEN_BUDGETRESULTATE
})
@JsonTypeName("TranchenBerechnungsresultat")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class TranchenBerechnungsresultatDtoSpec {
  public static final String JSON_PROPERTY_NAME_GESUCHSTELLER = "nameGesuchsteller";
  private String nameGesuchsteller;

  public static final String JSON_PROPERTY_BERECHNUNG = "berechnung";
  private Integer berechnung;

  public static final String JSON_PROPERTY_GUELTIG_AB = "gueltigAb";
  private LocalDate gueltigAb;

  public static final String JSON_PROPERTY_GUELTIG_BIS = "gueltigBis";
  private LocalDate gueltigBis;

  public static final String JSON_PROPERTY_GESUCH_TRANCHE_ID = "gesuchTrancheId";
  private UUID gesuchTrancheId;

  public static final String JSON_PROPERTY_BERECHNUNGSANTEIL_KINDER = "berechnungsanteilKinder";
  private BigDecimal berechnungsanteilKinder;

  public static final String JSON_PROPERTY_BERECHNUNGS_STAMMDATEN = "berechnungsStammdaten";
  private BerechnungsStammdatenDtoSpec berechnungsStammdaten;

  public static final String JSON_PROPERTY_PERSOENLICHES_BUDGETRESULTAT = "persoenlichesBudgetresultat";
  private PersoenlichesBudgetresultatDtoSpec persoenlichesBudgetresultat;

  public static final String JSON_PROPERTY_FAMILIEN_BUDGETRESULTATE = "familienBudgetresultate";
  private List<FamilienBudgetresultatDtoSpec> familienBudgetresultate;

  public TranchenBerechnungsresultatDtoSpec() {
  }

  public TranchenBerechnungsresultatDtoSpec nameGesuchsteller(String nameGesuchsteller) {
    
    this.nameGesuchsteller = nameGesuchsteller;
    return this;
  }

   /**
   * Get nameGesuchsteller
   * @return nameGesuchsteller
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_NAME_GESUCHSTELLER)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getNameGesuchsteller() {
    return nameGesuchsteller;
  }


  @JsonProperty(JSON_PROPERTY_NAME_GESUCHSTELLER)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setNameGesuchsteller(String nameGesuchsteller) {
    this.nameGesuchsteller = nameGesuchsteller;
  }


  public TranchenBerechnungsresultatDtoSpec berechnung(Integer berechnung) {
    
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


  public TranchenBerechnungsresultatDtoSpec gueltigAb(LocalDate gueltigAb) {
    
    this.gueltigAb = gueltigAb;
    return this;
  }

   /**
   * Get gueltigAb
   * @return gueltigAb
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_GUELTIG_AB)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public LocalDate getGueltigAb() {
    return gueltigAb;
  }


  @JsonProperty(JSON_PROPERTY_GUELTIG_AB)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setGueltigAb(LocalDate gueltigAb) {
    this.gueltigAb = gueltigAb;
  }


  public TranchenBerechnungsresultatDtoSpec gueltigBis(LocalDate gueltigBis) {
    
    this.gueltigBis = gueltigBis;
    return this;
  }

   /**
   * Get gueltigBis
   * @return gueltigBis
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_GUELTIG_BIS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public LocalDate getGueltigBis() {
    return gueltigBis;
  }


  @JsonProperty(JSON_PROPERTY_GUELTIG_BIS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setGueltigBis(LocalDate gueltigBis) {
    this.gueltigBis = gueltigBis;
  }


  public TranchenBerechnungsresultatDtoSpec gesuchTrancheId(UUID gesuchTrancheId) {
    
    this.gesuchTrancheId = gesuchTrancheId;
    return this;
  }

   /**
   * Get gesuchTrancheId
   * @return gesuchTrancheId
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_GESUCH_TRANCHE_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public UUID getGesuchTrancheId() {
    return gesuchTrancheId;
  }


  @JsonProperty(JSON_PROPERTY_GESUCH_TRANCHE_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setGesuchTrancheId(UUID gesuchTrancheId) {
    this.gesuchTrancheId = gesuchTrancheId;
  }


  public TranchenBerechnungsresultatDtoSpec berechnungsanteilKinder(BigDecimal berechnungsanteilKinder) {
    
    this.berechnungsanteilKinder = berechnungsanteilKinder;
    return this;
  }

   /**
   * Anteil dieser Berechnung am Berechnungstotal. Für Tranchen welche nur eine Berechnung haben ist dieser wert &#x3D;&#x3D; 1.
   * @return berechnungsanteilKinder
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_BERECHNUNGSANTEIL_KINDER)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public BigDecimal getBerechnungsanteilKinder() {
    return berechnungsanteilKinder;
  }


  @JsonProperty(JSON_PROPERTY_BERECHNUNGSANTEIL_KINDER)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setBerechnungsanteilKinder(BigDecimal berechnungsanteilKinder) {
    this.berechnungsanteilKinder = berechnungsanteilKinder;
  }


  public TranchenBerechnungsresultatDtoSpec berechnungsStammdaten(BerechnungsStammdatenDtoSpec berechnungsStammdaten) {
    
    this.berechnungsStammdaten = berechnungsStammdaten;
    return this;
  }

   /**
   * Get berechnungsStammdaten
   * @return berechnungsStammdaten
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_BERECHNUNGS_STAMMDATEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public BerechnungsStammdatenDtoSpec getBerechnungsStammdaten() {
    return berechnungsStammdaten;
  }


  @JsonProperty(JSON_PROPERTY_BERECHNUNGS_STAMMDATEN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setBerechnungsStammdaten(BerechnungsStammdatenDtoSpec berechnungsStammdaten) {
    this.berechnungsStammdaten = berechnungsStammdaten;
  }


  public TranchenBerechnungsresultatDtoSpec persoenlichesBudgetresultat(PersoenlichesBudgetresultatDtoSpec persoenlichesBudgetresultat) {
    
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


  public TranchenBerechnungsresultatDtoSpec familienBudgetresultate(List<FamilienBudgetresultatDtoSpec> familienBudgetresultate) {
    
    this.familienBudgetresultate = familienBudgetresultate;
    return this;
  }

  public TranchenBerechnungsresultatDtoSpec addFamilienBudgetresultateItem(FamilienBudgetresultatDtoSpec familienBudgetresultateItem) {
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
    TranchenBerechnungsresultatDtoSpec tranchenBerechnungsresultat = (TranchenBerechnungsresultatDtoSpec) o;
    return Objects.equals(this.nameGesuchsteller, tranchenBerechnungsresultat.nameGesuchsteller) &&
        Objects.equals(this.berechnung, tranchenBerechnungsresultat.berechnung) &&
        Objects.equals(this.gueltigAb, tranchenBerechnungsresultat.gueltigAb) &&
        Objects.equals(this.gueltigBis, tranchenBerechnungsresultat.gueltigBis) &&
        Objects.equals(this.gesuchTrancheId, tranchenBerechnungsresultat.gesuchTrancheId) &&
        Objects.equals(this.berechnungsanteilKinder, tranchenBerechnungsresultat.berechnungsanteilKinder) &&
        Objects.equals(this.berechnungsStammdaten, tranchenBerechnungsresultat.berechnungsStammdaten) &&
        Objects.equals(this.persoenlichesBudgetresultat, tranchenBerechnungsresultat.persoenlichesBudgetresultat) &&
        Objects.equals(this.familienBudgetresultate, tranchenBerechnungsresultat.familienBudgetresultate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nameGesuchsteller, berechnung, gueltigAb, gueltigBis, gesuchTrancheId, berechnungsanteilKinder, berechnungsStammdaten, persoenlichesBudgetresultat, familienBudgetresultate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TranchenBerechnungsresultatDtoSpec {\n");
    sb.append("    nameGesuchsteller: ").append(toIndentedString(nameGesuchsteller)).append("\n");
    sb.append("    berechnung: ").append(toIndentedString(berechnung)).append("\n");
    sb.append("    gueltigAb: ").append(toIndentedString(gueltigAb)).append("\n");
    sb.append("    gueltigBis: ").append(toIndentedString(gueltigBis)).append("\n");
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

