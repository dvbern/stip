package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.BerechnungsStammdatenDto;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatDto;
import ch.dvbern.stip.generated.dto.PersonenHaushaltGruppeDto;
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
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class TranchenBerechnungsresultatDto  implements Serializable {
  private @Valid Integer total;
  private @Valid Integer ungekuerztTotal;
  private @Valid LocalDate gueltigAb;
  private @Valid LocalDate gueltigBis;
  private @Valid String ausbildungAb;
  private @Valid String ausbildungBis;
  private @Valid String yearRange;
  private @Valid UUID gesuchTrancheId;
  private @Valid BerechnungsStammdatenDto berechnungsStammdaten;
  private @Valid PersoenlichesBudgetresultatDto persoenlichesBudgetresultat;
  private @Valid List<FamilienBudgetresultatDto> familienBudgetresultate = new ArrayList<>();
  private @Valid List<PersonenHaushaltGruppeDto> personenHaushaltGroups = new ArrayList<>();
  private @Valid BigDecimal berechnungsanteilKinderDerEltern;
  private @Valid Boolean teilzeitKinderBeiPiaAnrechnen;
  private @Valid BigDecimal berechnungsanteilKinderPia;

  protected TranchenBerechnungsresultatDto(TranchenBerechnungsresultatDtoBuilder<?, ?> b) {
    this.total = b.total;
    this.ungekuerztTotal = b.ungekuerztTotal;
    this.gueltigAb = b.gueltigAb;
    this.gueltigBis = b.gueltigBis;
    this.ausbildungAb = b.ausbildungAb;
    this.ausbildungBis = b.ausbildungBis;
    this.yearRange = b.yearRange;
    this.gesuchTrancheId = b.gesuchTrancheId;
    this.berechnungsStammdaten = b.berechnungsStammdaten;
    this.persoenlichesBudgetresultat = b.persoenlichesBudgetresultat;
    this.familienBudgetresultate = b.familienBudgetresultate;
    this.personenHaushaltGroups = b.personenHaushaltGroups;
    this.berechnungsanteilKinderDerEltern = b.berechnungsanteilKinderDerEltern;
    this.teilzeitKinderBeiPiaAnrechnen = b.teilzeitKinderBeiPiaAnrechnen;
    this.berechnungsanteilKinderPia = b.berechnungsanteilKinderPia;
  }

  public TranchenBerechnungsresultatDto() {
  }

  /**
   * Die Summe der berechneten Stpendiums- und Darlehensansprüche für diese Tranche
   **/
  public TranchenBerechnungsresultatDto total(Integer total) {
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
   * Die ungekürzte Summe der berechneten Stpendiums- und Darlehensansprüche für diese Tranche
   **/
  public TranchenBerechnungsresultatDto ungekuerztTotal(Integer ungekuerztTotal) {
    this.ungekuerztTotal = ungekuerztTotal;
    return this;
  }

  
  @JsonProperty("ungekuerztTotal")
  @NotNull
  public Integer getUngekuerztTotal() {
    return ungekuerztTotal;
  }

  @JsonProperty("ungekuerztTotal")
  public void setUngekuerztTotal(Integer ungekuerztTotal) {
    this.ungekuerztTotal = ungekuerztTotal;
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
  public TranchenBerechnungsresultatDto yearRange(String yearRange) {
    this.yearRange = yearRange;
    return this;
  }

  
  @JsonProperty("yearRange")
  @NotNull
  public String getYearRange() {
    return yearRange;
  }

  @JsonProperty("yearRange")
  public void setYearRange(String yearRange) {
    this.yearRange = yearRange;
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
  /**
   * Enthällt eine Liste aller relevanten Personen für die jeweiligen Personen Haushalt Gruppen
   **/
  public TranchenBerechnungsresultatDto personenHaushaltGroups(List<PersonenHaushaltGruppeDto> personenHaushaltGroups) {
    this.personenHaushaltGroups = personenHaushaltGroups;
    return this;
  }

  
  @JsonProperty("personenHaushaltGroups")
  @NotNull
  public List<PersonenHaushaltGruppeDto> getPersonenHaushaltGroups() {
    return personenHaushaltGroups;
  }

  @JsonProperty("personenHaushaltGroups")
  public void setPersonenHaushaltGroups(List<PersonenHaushaltGruppeDto> personenHaushaltGroups) {
    this.personenHaushaltGroups = personenHaushaltGroups;
  }

  public TranchenBerechnungsresultatDto addPersonenHaushaltGroupsItem(PersonenHaushaltGruppeDto personenHaushaltGroupsItem) {
    if (this.personenHaushaltGroups == null) {
      this.personenHaushaltGroups = new ArrayList<>();
    }

    this.personenHaushaltGroups.add(personenHaushaltGroupsItem);
    return this;
  }

  public TranchenBerechnungsresultatDto removePersonenHaushaltGroupsItem(PersonenHaushaltGruppeDto personenHaushaltGroupsItem) {
    if (personenHaushaltGroupsItem != null && this.personenHaushaltGroups != null) {
      this.personenHaushaltGroups.remove(personenHaushaltGroupsItem);
    }

    return this;
  }
  /**
   * Anteil dieser Berechnung am Berechnungstotal. Für Tranchen welche nur eine Berechnung haben ist dieser Wert null.
   **/
  public TranchenBerechnungsresultatDto berechnungsanteilKinderDerEltern(BigDecimal berechnungsanteilKinderDerEltern) {
    this.berechnungsanteilKinderDerEltern = berechnungsanteilKinderDerEltern;
    return this;
  }

  
  @JsonProperty("berechnungsanteilKinderDerEltern")
  public BigDecimal getBerechnungsanteilKinderDerEltern() {
    return berechnungsanteilKinderDerEltern;
  }

  @JsonProperty("berechnungsanteilKinderDerEltern")
  public void setBerechnungsanteilKinderDerEltern(BigDecimal berechnungsanteilKinderDerEltern) {
    this.berechnungsanteilKinderDerEltern = berechnungsanteilKinderDerEltern;
  }

  /**
   * Werden kinder der PiA bei der berechnung dieser Tranche dem Budget der PiA angerechnet
   **/
  public TranchenBerechnungsresultatDto teilzeitKinderBeiPiaAnrechnen(Boolean teilzeitKinderBeiPiaAnrechnen) {
    this.teilzeitKinderBeiPiaAnrechnen = teilzeitKinderBeiPiaAnrechnen;
    return this;
  }

  
  @JsonProperty("teilzeitKinderBeiPiaAnrechnen")
  public Boolean getTeilzeitKinderBeiPiaAnrechnen() {
    return teilzeitKinderBeiPiaAnrechnen;
  }

  @JsonProperty("teilzeitKinderBeiPiaAnrechnen")
  public void setTeilzeitKinderBeiPiaAnrechnen(Boolean teilzeitKinderBeiPiaAnrechnen) {
    this.teilzeitKinderBeiPiaAnrechnen = teilzeitKinderBeiPiaAnrechnen;
  }

  /**
   * Anteil dieser Berechnung am Berechnungstotal. Für Tranchen welche nur eine Berechnung haben ist dieser Wert null.
   **/
  public TranchenBerechnungsresultatDto berechnungsanteilKinderPia(BigDecimal berechnungsanteilKinderPia) {
    this.berechnungsanteilKinderPia = berechnungsanteilKinderPia;
    return this;
  }

  
  @JsonProperty("berechnungsanteilKinderPia")
  public BigDecimal getBerechnungsanteilKinderPia() {
    return berechnungsanteilKinderPia;
  }

  @JsonProperty("berechnungsanteilKinderPia")
  public void setBerechnungsanteilKinderPia(BigDecimal berechnungsanteilKinderPia) {
    this.berechnungsanteilKinderPia = berechnungsanteilKinderPia;
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
    return Objects.equals(this.total, tranchenBerechnungsresultat.total) &&
        Objects.equals(this.ungekuerztTotal, tranchenBerechnungsresultat.ungekuerztTotal) &&
        Objects.equals(this.gueltigAb, tranchenBerechnungsresultat.gueltigAb) &&
        Objects.equals(this.gueltigBis, tranchenBerechnungsresultat.gueltigBis) &&
        Objects.equals(this.ausbildungAb, tranchenBerechnungsresultat.ausbildungAb) &&
        Objects.equals(this.ausbildungBis, tranchenBerechnungsresultat.ausbildungBis) &&
        Objects.equals(this.yearRange, tranchenBerechnungsresultat.yearRange) &&
        Objects.equals(this.gesuchTrancheId, tranchenBerechnungsresultat.gesuchTrancheId) &&
        Objects.equals(this.berechnungsStammdaten, tranchenBerechnungsresultat.berechnungsStammdaten) &&
        Objects.equals(this.persoenlichesBudgetresultat, tranchenBerechnungsresultat.persoenlichesBudgetresultat) &&
        Objects.equals(this.familienBudgetresultate, tranchenBerechnungsresultat.familienBudgetresultate) &&
        Objects.equals(this.personenHaushaltGroups, tranchenBerechnungsresultat.personenHaushaltGroups) &&
        Objects.equals(this.berechnungsanteilKinderDerEltern, tranchenBerechnungsresultat.berechnungsanteilKinderDerEltern) &&
        Objects.equals(this.teilzeitKinderBeiPiaAnrechnen, tranchenBerechnungsresultat.teilzeitKinderBeiPiaAnrechnen) &&
        Objects.equals(this.berechnungsanteilKinderPia, tranchenBerechnungsresultat.berechnungsanteilKinderPia);
  }

  @Override
  public int hashCode() {
    return Objects.hash(total, ungekuerztTotal, gueltigAb, gueltigBis, ausbildungAb, ausbildungBis, yearRange, gesuchTrancheId, berechnungsStammdaten, persoenlichesBudgetresultat, familienBudgetresultate, personenHaushaltGroups, berechnungsanteilKinderDerEltern, teilzeitKinderBeiPiaAnrechnen, berechnungsanteilKinderPia);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TranchenBerechnungsresultatDto {\n");
    
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    ungekuerztTotal: ").append(toIndentedString(ungekuerztTotal)).append("\n");
    sb.append("    gueltigAb: ").append(toIndentedString(gueltigAb)).append("\n");
    sb.append("    gueltigBis: ").append(toIndentedString(gueltigBis)).append("\n");
    sb.append("    ausbildungAb: ").append(toIndentedString(ausbildungAb)).append("\n");
    sb.append("    ausbildungBis: ").append(toIndentedString(ausbildungBis)).append("\n");
    sb.append("    yearRange: ").append(toIndentedString(yearRange)).append("\n");
    sb.append("    gesuchTrancheId: ").append(toIndentedString(gesuchTrancheId)).append("\n");
    sb.append("    berechnungsStammdaten: ").append(toIndentedString(berechnungsStammdaten)).append("\n");
    sb.append("    persoenlichesBudgetresultat: ").append(toIndentedString(persoenlichesBudgetresultat)).append("\n");
    sb.append("    familienBudgetresultate: ").append(toIndentedString(familienBudgetresultate)).append("\n");
    sb.append("    personenHaushaltGroups: ").append(toIndentedString(personenHaushaltGroups)).append("\n");
    sb.append("    berechnungsanteilKinderDerEltern: ").append(toIndentedString(berechnungsanteilKinderDerEltern)).append("\n");
    sb.append("    teilzeitKinderBeiPiaAnrechnen: ").append(toIndentedString(teilzeitKinderBeiPiaAnrechnen)).append("\n");
    sb.append("    berechnungsanteilKinderPia: ").append(toIndentedString(berechnungsanteilKinderPia)).append("\n");
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


  public static TranchenBerechnungsresultatDtoBuilder<?, ?> builder() {
    return new TranchenBerechnungsresultatDtoBuilderImpl();
  }

  private static final class TranchenBerechnungsresultatDtoBuilderImpl extends TranchenBerechnungsresultatDtoBuilder<TranchenBerechnungsresultatDto, TranchenBerechnungsresultatDtoBuilderImpl> {

    @Override
    protected TranchenBerechnungsresultatDtoBuilderImpl self() {
      return this;
    }

    @Override
    public TranchenBerechnungsresultatDto build() {
      return new TranchenBerechnungsresultatDto(this);
    }
  }

  public static abstract class TranchenBerechnungsresultatDtoBuilder<C extends TranchenBerechnungsresultatDto, B extends TranchenBerechnungsresultatDtoBuilder<C, B>>  {
    private Integer total;
    private Integer ungekuerztTotal;
    private LocalDate gueltigAb;
    private LocalDate gueltigBis;
    private String ausbildungAb;
    private String ausbildungBis;
    private String yearRange;
    private UUID gesuchTrancheId;
    private BerechnungsStammdatenDto berechnungsStammdaten;
    private PersoenlichesBudgetresultatDto persoenlichesBudgetresultat;
    private List<FamilienBudgetresultatDto> familienBudgetresultate = new ArrayList<>();
    private List<PersonenHaushaltGruppeDto> personenHaushaltGroups = new ArrayList<>();
    private BigDecimal berechnungsanteilKinderDerEltern;
    private Boolean teilzeitKinderBeiPiaAnrechnen;
    private BigDecimal berechnungsanteilKinderPia;
    protected abstract B self();

    public abstract C build();

    public B total(Integer total) {
      this.total = total;
      return self();
    }
    public B ungekuerztTotal(Integer ungekuerztTotal) {
      this.ungekuerztTotal = ungekuerztTotal;
      return self();
    }
    public B gueltigAb(LocalDate gueltigAb) {
      this.gueltigAb = gueltigAb;
      return self();
    }
    public B gueltigBis(LocalDate gueltigBis) {
      this.gueltigBis = gueltigBis;
      return self();
    }
    public B ausbildungAb(String ausbildungAb) {
      this.ausbildungAb = ausbildungAb;
      return self();
    }
    public B ausbildungBis(String ausbildungBis) {
      this.ausbildungBis = ausbildungBis;
      return self();
    }
    public B yearRange(String yearRange) {
      this.yearRange = yearRange;
      return self();
    }
    public B gesuchTrancheId(UUID gesuchTrancheId) {
      this.gesuchTrancheId = gesuchTrancheId;
      return self();
    }
    public B berechnungsStammdaten(BerechnungsStammdatenDto berechnungsStammdaten) {
      this.berechnungsStammdaten = berechnungsStammdaten;
      return self();
    }
    public B persoenlichesBudgetresultat(PersoenlichesBudgetresultatDto persoenlichesBudgetresultat) {
      this.persoenlichesBudgetresultat = persoenlichesBudgetresultat;
      return self();
    }
    public B familienBudgetresultate(List<FamilienBudgetresultatDto> familienBudgetresultate) {
      this.familienBudgetresultate = familienBudgetresultate;
      return self();
    }
    public B personenHaushaltGroups(List<PersonenHaushaltGruppeDto> personenHaushaltGroups) {
      this.personenHaushaltGroups = personenHaushaltGroups;
      return self();
    }
    public B berechnungsanteilKinderDerEltern(BigDecimal berechnungsanteilKinderDerEltern) {
      this.berechnungsanteilKinderDerEltern = berechnungsanteilKinderDerEltern;
      return self();
    }
    public B teilzeitKinderBeiPiaAnrechnen(Boolean teilzeitKinderBeiPiaAnrechnen) {
      this.teilzeitKinderBeiPiaAnrechnen = teilzeitKinderBeiPiaAnrechnen;
      return self();
    }
    public B berechnungsanteilKinderPia(BigDecimal berechnungsanteilKinderPia) {
      this.berechnungsanteilKinderPia = berechnungsanteilKinderPia;
      return self();
    }
  }
}

