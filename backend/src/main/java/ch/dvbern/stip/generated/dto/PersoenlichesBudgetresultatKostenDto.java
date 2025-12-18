package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.PersonValueItemDto;
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
 * Persoenliche Budget daten fuer und von der Berechnung
 **/

@JsonTypeName("PersoenlichesBudgetresultatKosten")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class PersoenlichesBudgetresultatKostenDto  implements Serializable {
  private @Valid Integer total;
  private @Valid Integer ausbildungskosten;
  private @Valid Integer fahrkosten;
  private @Valid Integer verpflegungskosten;
  private @Valid Integer grundbedarf;
  private @Valid Integer wohnkosten;
  private @Valid Integer medizinischeGrundversorgung;
  private @Valid Integer medizinischeGrundversorgungPartner;
  private @Valid List<PersonValueItemDto> medizinischeGrundversorgungKinder = new ArrayList<>();
  private @Valid Integer medizinischeGrundversorgungTotal;
  private @Valid Integer fahrkostenPartner;
  private @Valid Integer verpflegungPartner;
  private @Valid Integer betreuungskostenKinder;
  private @Valid Integer kantonsGemeindesteuern;
  private @Valid Integer bundessteuern;
  private @Valid Integer anteilLebenshaltungskosten;

  /**
   **/
  public PersoenlichesBudgetresultatKostenDto total(Integer total) {
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
  public PersoenlichesBudgetresultatKostenDto ausbildungskosten(Integer ausbildungskosten) {
    this.ausbildungskosten = ausbildungskosten;
    return this;
  }

  
  @JsonProperty("ausbildungskosten")
  @NotNull
  public Integer getAusbildungskosten() {
    return ausbildungskosten;
  }

  @JsonProperty("ausbildungskosten")
  public void setAusbildungskosten(Integer ausbildungskosten) {
    this.ausbildungskosten = ausbildungskosten;
  }

  /**
   **/
  public PersoenlichesBudgetresultatKostenDto fahrkosten(Integer fahrkosten) {
    this.fahrkosten = fahrkosten;
    return this;
  }

  
  @JsonProperty("fahrkosten")
  @NotNull
  public Integer getFahrkosten() {
    return fahrkosten;
  }

  @JsonProperty("fahrkosten")
  public void setFahrkosten(Integer fahrkosten) {
    this.fahrkosten = fahrkosten;
  }

  /**
   **/
  public PersoenlichesBudgetresultatKostenDto verpflegungskosten(Integer verpflegungskosten) {
    this.verpflegungskosten = verpflegungskosten;
    return this;
  }

  
  @JsonProperty("verpflegungskosten")
  @NotNull
  public Integer getVerpflegungskosten() {
    return verpflegungskosten;
  }

  @JsonProperty("verpflegungskosten")
  public void setVerpflegungskosten(Integer verpflegungskosten) {
    this.verpflegungskosten = verpflegungskosten;
  }

  /**
   **/
  public PersoenlichesBudgetresultatKostenDto grundbedarf(Integer grundbedarf) {
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
  public PersoenlichesBudgetresultatKostenDto wohnkosten(Integer wohnkosten) {
    this.wohnkosten = wohnkosten;
    return this;
  }

  
  @JsonProperty("wohnkosten")
  @NotNull
  public Integer getWohnkosten() {
    return wohnkosten;
  }

  @JsonProperty("wohnkosten")
  public void setWohnkosten(Integer wohnkosten) {
    this.wohnkosten = wohnkosten;
  }

  /**
   **/
  public PersoenlichesBudgetresultatKostenDto medizinischeGrundversorgung(Integer medizinischeGrundversorgung) {
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
  public PersoenlichesBudgetresultatKostenDto medizinischeGrundversorgungPartner(Integer medizinischeGrundversorgungPartner) {
    this.medizinischeGrundversorgungPartner = medizinischeGrundversorgungPartner;
    return this;
  }

  
  @JsonProperty("medizinischeGrundversorgungPartner")
  @NotNull
  public Integer getMedizinischeGrundversorgungPartner() {
    return medizinischeGrundversorgungPartner;
  }

  @JsonProperty("medizinischeGrundversorgungPartner")
  public void setMedizinischeGrundversorgungPartner(Integer medizinischeGrundversorgungPartner) {
    this.medizinischeGrundversorgungPartner = medizinischeGrundversorgungPartner;
  }

  /**
   **/
  public PersoenlichesBudgetresultatKostenDto medizinischeGrundversorgungKinder(List<PersonValueItemDto> medizinischeGrundversorgungKinder) {
    this.medizinischeGrundversorgungKinder = medizinischeGrundversorgungKinder;
    return this;
  }

  
  @JsonProperty("medizinischeGrundversorgungKinder")
  @NotNull
  public List<PersonValueItemDto> getMedizinischeGrundversorgungKinder() {
    return medizinischeGrundversorgungKinder;
  }

  @JsonProperty("medizinischeGrundversorgungKinder")
  public void setMedizinischeGrundversorgungKinder(List<PersonValueItemDto> medizinischeGrundversorgungKinder) {
    this.medizinischeGrundversorgungKinder = medizinischeGrundversorgungKinder;
  }

  public PersoenlichesBudgetresultatKostenDto addMedizinischeGrundversorgungKinderItem(PersonValueItemDto medizinischeGrundversorgungKinderItem) {
    if (this.medizinischeGrundversorgungKinder == null) {
      this.medizinischeGrundversorgungKinder = new ArrayList<>();
    }

    this.medizinischeGrundversorgungKinder.add(medizinischeGrundversorgungKinderItem);
    return this;
  }

  public PersoenlichesBudgetresultatKostenDto removeMedizinischeGrundversorgungKinderItem(PersonValueItemDto medizinischeGrundversorgungKinderItem) {
    if (medizinischeGrundversorgungKinderItem != null && this.medizinischeGrundversorgungKinder != null) {
      this.medizinischeGrundversorgungKinder.remove(medizinischeGrundversorgungKinderItem);
    }

    return this;
  }
  /**
   **/
  public PersoenlichesBudgetresultatKostenDto medizinischeGrundversorgungTotal(Integer medizinischeGrundversorgungTotal) {
    this.medizinischeGrundversorgungTotal = medizinischeGrundversorgungTotal;
    return this;
  }

  
  @JsonProperty("medizinischeGrundversorgungTotal")
  @NotNull
  public Integer getMedizinischeGrundversorgungTotal() {
    return medizinischeGrundversorgungTotal;
  }

  @JsonProperty("medizinischeGrundversorgungTotal")
  public void setMedizinischeGrundversorgungTotal(Integer medizinischeGrundversorgungTotal) {
    this.medizinischeGrundversorgungTotal = medizinischeGrundversorgungTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatKostenDto fahrkostenPartner(Integer fahrkostenPartner) {
    this.fahrkostenPartner = fahrkostenPartner;
    return this;
  }

  
  @JsonProperty("fahrkostenPartner")
  @NotNull
  public Integer getFahrkostenPartner() {
    return fahrkostenPartner;
  }

  @JsonProperty("fahrkostenPartner")
  public void setFahrkostenPartner(Integer fahrkostenPartner) {
    this.fahrkostenPartner = fahrkostenPartner;
  }

  /**
   **/
  public PersoenlichesBudgetresultatKostenDto verpflegungPartner(Integer verpflegungPartner) {
    this.verpflegungPartner = verpflegungPartner;
    return this;
  }

  
  @JsonProperty("verpflegungPartner")
  @NotNull
  public Integer getVerpflegungPartner() {
    return verpflegungPartner;
  }

  @JsonProperty("verpflegungPartner")
  public void setVerpflegungPartner(Integer verpflegungPartner) {
    this.verpflegungPartner = verpflegungPartner;
  }

  /**
   **/
  public PersoenlichesBudgetresultatKostenDto betreuungskostenKinder(Integer betreuungskostenKinder) {
    this.betreuungskostenKinder = betreuungskostenKinder;
    return this;
  }

  
  @JsonProperty("betreuungskostenKinder")
  @NotNull
  public Integer getBetreuungskostenKinder() {
    return betreuungskostenKinder;
  }

  @JsonProperty("betreuungskostenKinder")
  public void setBetreuungskostenKinder(Integer betreuungskostenKinder) {
    this.betreuungskostenKinder = betreuungskostenKinder;
  }

  /**
   **/
  public PersoenlichesBudgetresultatKostenDto kantonsGemeindesteuern(Integer kantonsGemeindesteuern) {
    this.kantonsGemeindesteuern = kantonsGemeindesteuern;
    return this;
  }

  
  @JsonProperty("kantonsGemeindesteuern")
  @NotNull
  public Integer getKantonsGemeindesteuern() {
    return kantonsGemeindesteuern;
  }

  @JsonProperty("kantonsGemeindesteuern")
  public void setKantonsGemeindesteuern(Integer kantonsGemeindesteuern) {
    this.kantonsGemeindesteuern = kantonsGemeindesteuern;
  }

  /**
   **/
  public PersoenlichesBudgetresultatKostenDto bundessteuern(Integer bundessteuern) {
    this.bundessteuern = bundessteuern;
    return this;
  }

  
  @JsonProperty("bundessteuern")
  @NotNull
  public Integer getBundessteuern() {
    return bundessteuern;
  }

  @JsonProperty("bundessteuern")
  public void setBundessteuern(Integer bundessteuern) {
    this.bundessteuern = bundessteuern;
  }

  /**
   **/
  public PersoenlichesBudgetresultatKostenDto anteilLebenshaltungskosten(Integer anteilLebenshaltungskosten) {
    this.anteilLebenshaltungskosten = anteilLebenshaltungskosten;
    return this;
  }

  
  @JsonProperty("anteilLebenshaltungskosten")
  @NotNull
  public Integer getAnteilLebenshaltungskosten() {
    return anteilLebenshaltungskosten;
  }

  @JsonProperty("anteilLebenshaltungskosten")
  public void setAnteilLebenshaltungskosten(Integer anteilLebenshaltungskosten) {
    this.anteilLebenshaltungskosten = anteilLebenshaltungskosten;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PersoenlichesBudgetresultatKostenDto persoenlichesBudgetresultatKosten = (PersoenlichesBudgetresultatKostenDto) o;
    return Objects.equals(this.total, persoenlichesBudgetresultatKosten.total) &&
        Objects.equals(this.ausbildungskosten, persoenlichesBudgetresultatKosten.ausbildungskosten) &&
        Objects.equals(this.fahrkosten, persoenlichesBudgetresultatKosten.fahrkosten) &&
        Objects.equals(this.verpflegungskosten, persoenlichesBudgetresultatKosten.verpflegungskosten) &&
        Objects.equals(this.grundbedarf, persoenlichesBudgetresultatKosten.grundbedarf) &&
        Objects.equals(this.wohnkosten, persoenlichesBudgetresultatKosten.wohnkosten) &&
        Objects.equals(this.medizinischeGrundversorgung, persoenlichesBudgetresultatKosten.medizinischeGrundversorgung) &&
        Objects.equals(this.medizinischeGrundversorgungPartner, persoenlichesBudgetresultatKosten.medizinischeGrundversorgungPartner) &&
        Objects.equals(this.medizinischeGrundversorgungKinder, persoenlichesBudgetresultatKosten.medizinischeGrundversorgungKinder) &&
        Objects.equals(this.medizinischeGrundversorgungTotal, persoenlichesBudgetresultatKosten.medizinischeGrundversorgungTotal) &&
        Objects.equals(this.fahrkostenPartner, persoenlichesBudgetresultatKosten.fahrkostenPartner) &&
        Objects.equals(this.verpflegungPartner, persoenlichesBudgetresultatKosten.verpflegungPartner) &&
        Objects.equals(this.betreuungskostenKinder, persoenlichesBudgetresultatKosten.betreuungskostenKinder) &&
        Objects.equals(this.kantonsGemeindesteuern, persoenlichesBudgetresultatKosten.kantonsGemeindesteuern) &&
        Objects.equals(this.bundessteuern, persoenlichesBudgetresultatKosten.bundessteuern) &&
        Objects.equals(this.anteilLebenshaltungskosten, persoenlichesBudgetresultatKosten.anteilLebenshaltungskosten);
  }

  @Override
  public int hashCode() {
    return Objects.hash(total, ausbildungskosten, fahrkosten, verpflegungskosten, grundbedarf, wohnkosten, medizinischeGrundversorgung, medizinischeGrundversorgungPartner, medizinischeGrundversorgungKinder, medizinischeGrundversorgungTotal, fahrkostenPartner, verpflegungPartner, betreuungskostenKinder, kantonsGemeindesteuern, bundessteuern, anteilLebenshaltungskosten);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PersoenlichesBudgetresultatKostenDto {\n");
    
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    ausbildungskosten: ").append(toIndentedString(ausbildungskosten)).append("\n");
    sb.append("    fahrkosten: ").append(toIndentedString(fahrkosten)).append("\n");
    sb.append("    verpflegungskosten: ").append(toIndentedString(verpflegungskosten)).append("\n");
    sb.append("    grundbedarf: ").append(toIndentedString(grundbedarf)).append("\n");
    sb.append("    wohnkosten: ").append(toIndentedString(wohnkosten)).append("\n");
    sb.append("    medizinischeGrundversorgung: ").append(toIndentedString(medizinischeGrundversorgung)).append("\n");
    sb.append("    medizinischeGrundversorgungPartner: ").append(toIndentedString(medizinischeGrundversorgungPartner)).append("\n");
    sb.append("    medizinischeGrundversorgungKinder: ").append(toIndentedString(medizinischeGrundversorgungKinder)).append("\n");
    sb.append("    medizinischeGrundversorgungTotal: ").append(toIndentedString(medizinischeGrundversorgungTotal)).append("\n");
    sb.append("    fahrkostenPartner: ").append(toIndentedString(fahrkostenPartner)).append("\n");
    sb.append("    verpflegungPartner: ").append(toIndentedString(verpflegungPartner)).append("\n");
    sb.append("    betreuungskostenKinder: ").append(toIndentedString(betreuungskostenKinder)).append("\n");
    sb.append("    kantonsGemeindesteuern: ").append(toIndentedString(kantonsGemeindesteuern)).append("\n");
    sb.append("    bundessteuern: ").append(toIndentedString(bundessteuern)).append("\n");
    sb.append("    anteilLebenshaltungskosten: ").append(toIndentedString(anteilLebenshaltungskosten)).append("\n");
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

