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



@JsonTypeName("FamilienBudgetresultatKosten")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class FamilienBudgetresultatKostenDto  implements Serializable {
  private @Valid Integer total;
  private @Valid Integer grundbedarf;
  private @Valid Integer wohnkosten;
  private @Valid Integer medizinischeGrundversorgung;
  private @Valid Integer integrationszulage;
  private @Valid Integer integrationszulageAnzahl;
  private @Valid Integer integrationszulageTotal;
  private @Valid Integer kantonsGemeindesteuern;
  private @Valid Integer bundessteuern;
  private @Valid List<PersonValueItemDto> fahrkosten = new ArrayList<>();
  private @Valid Integer fahrkostenTotal;
  private @Valid List<PersonValueItemDto> verpflegung = new ArrayList<>();
  private @Valid Integer verpflegungTotal;

  /**
   **/
  public FamilienBudgetresultatKostenDto total(Integer total) {
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
  public FamilienBudgetresultatKostenDto grundbedarf(Integer grundbedarf) {
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
  public FamilienBudgetresultatKostenDto wohnkosten(Integer wohnkosten) {
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
  public FamilienBudgetresultatKostenDto medizinischeGrundversorgung(Integer medizinischeGrundversorgung) {
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
  public FamilienBudgetresultatKostenDto integrationszulage(Integer integrationszulage) {
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
  public FamilienBudgetresultatKostenDto integrationszulageAnzahl(Integer integrationszulageAnzahl) {
    this.integrationszulageAnzahl = integrationszulageAnzahl;
    return this;
  }

  
  @JsonProperty("integrationszulageAnzahl")
  @NotNull
  public Integer getIntegrationszulageAnzahl() {
    return integrationszulageAnzahl;
  }

  @JsonProperty("integrationszulageAnzahl")
  public void setIntegrationszulageAnzahl(Integer integrationszulageAnzahl) {
    this.integrationszulageAnzahl = integrationszulageAnzahl;
  }

  /**
   **/
  public FamilienBudgetresultatKostenDto integrationszulageTotal(Integer integrationszulageTotal) {
    this.integrationszulageTotal = integrationszulageTotal;
    return this;
  }

  
  @JsonProperty("integrationszulageTotal")
  @NotNull
  public Integer getIntegrationszulageTotal() {
    return integrationszulageTotal;
  }

  @JsonProperty("integrationszulageTotal")
  public void setIntegrationszulageTotal(Integer integrationszulageTotal) {
    this.integrationszulageTotal = integrationszulageTotal;
  }

  /**
   **/
  public FamilienBudgetresultatKostenDto kantonsGemeindesteuern(Integer kantonsGemeindesteuern) {
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
  public FamilienBudgetresultatKostenDto bundessteuern(Integer bundessteuern) {
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
  public FamilienBudgetresultatKostenDto fahrkosten(List<PersonValueItemDto> fahrkosten) {
    this.fahrkosten = fahrkosten;
    return this;
  }

  
  @JsonProperty("fahrkosten")
  @NotNull
  public List<PersonValueItemDto> getFahrkosten() {
    return fahrkosten;
  }

  @JsonProperty("fahrkosten")
  public void setFahrkosten(List<PersonValueItemDto> fahrkosten) {
    this.fahrkosten = fahrkosten;
  }

  public FamilienBudgetresultatKostenDto addFahrkostenItem(PersonValueItemDto fahrkostenItem) {
    if (this.fahrkosten == null) {
      this.fahrkosten = new ArrayList<>();
    }

    this.fahrkosten.add(fahrkostenItem);
    return this;
  }

  public FamilienBudgetresultatKostenDto removeFahrkostenItem(PersonValueItemDto fahrkostenItem) {
    if (fahrkostenItem != null && this.fahrkosten != null) {
      this.fahrkosten.remove(fahrkostenItem);
    }

    return this;
  }
  /**
   **/
  public FamilienBudgetresultatKostenDto fahrkostenTotal(Integer fahrkostenTotal) {
    this.fahrkostenTotal = fahrkostenTotal;
    return this;
  }

  
  @JsonProperty("fahrkostenTotal")
  @NotNull
  public Integer getFahrkostenTotal() {
    return fahrkostenTotal;
  }

  @JsonProperty("fahrkostenTotal")
  public void setFahrkostenTotal(Integer fahrkostenTotal) {
    this.fahrkostenTotal = fahrkostenTotal;
  }

  /**
   **/
  public FamilienBudgetresultatKostenDto verpflegung(List<PersonValueItemDto> verpflegung) {
    this.verpflegung = verpflegung;
    return this;
  }

  
  @JsonProperty("verpflegung")
  @NotNull
  public List<PersonValueItemDto> getVerpflegung() {
    return verpflegung;
  }

  @JsonProperty("verpflegung")
  public void setVerpflegung(List<PersonValueItemDto> verpflegung) {
    this.verpflegung = verpflegung;
  }

  public FamilienBudgetresultatKostenDto addVerpflegungItem(PersonValueItemDto verpflegungItem) {
    if (this.verpflegung == null) {
      this.verpflegung = new ArrayList<>();
    }

    this.verpflegung.add(verpflegungItem);
    return this;
  }

  public FamilienBudgetresultatKostenDto removeVerpflegungItem(PersonValueItemDto verpflegungItem) {
    if (verpflegungItem != null && this.verpflegung != null) {
      this.verpflegung.remove(verpflegungItem);
    }

    return this;
  }
  /**
   **/
  public FamilienBudgetresultatKostenDto verpflegungTotal(Integer verpflegungTotal) {
    this.verpflegungTotal = verpflegungTotal;
    return this;
  }

  
  @JsonProperty("verpflegungTotal")
  @NotNull
  public Integer getVerpflegungTotal() {
    return verpflegungTotal;
  }

  @JsonProperty("verpflegungTotal")
  public void setVerpflegungTotal(Integer verpflegungTotal) {
    this.verpflegungTotal = verpflegungTotal;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FamilienBudgetresultatKostenDto familienBudgetresultatKosten = (FamilienBudgetresultatKostenDto) o;
    return Objects.equals(this.total, familienBudgetresultatKosten.total) &&
        Objects.equals(this.grundbedarf, familienBudgetresultatKosten.grundbedarf) &&
        Objects.equals(this.wohnkosten, familienBudgetresultatKosten.wohnkosten) &&
        Objects.equals(this.medizinischeGrundversorgung, familienBudgetresultatKosten.medizinischeGrundversorgung) &&
        Objects.equals(this.integrationszulage, familienBudgetresultatKosten.integrationszulage) &&
        Objects.equals(this.integrationszulageAnzahl, familienBudgetresultatKosten.integrationszulageAnzahl) &&
        Objects.equals(this.integrationszulageTotal, familienBudgetresultatKosten.integrationszulageTotal) &&
        Objects.equals(this.kantonsGemeindesteuern, familienBudgetresultatKosten.kantonsGemeindesteuern) &&
        Objects.equals(this.bundessteuern, familienBudgetresultatKosten.bundessteuern) &&
        Objects.equals(this.fahrkosten, familienBudgetresultatKosten.fahrkosten) &&
        Objects.equals(this.fahrkostenTotal, familienBudgetresultatKosten.fahrkostenTotal) &&
        Objects.equals(this.verpflegung, familienBudgetresultatKosten.verpflegung) &&
        Objects.equals(this.verpflegungTotal, familienBudgetresultatKosten.verpflegungTotal);
  }

  @Override
  public int hashCode() {
    return Objects.hash(total, grundbedarf, wohnkosten, medizinischeGrundversorgung, integrationszulage, integrationszulageAnzahl, integrationszulageTotal, kantonsGemeindesteuern, bundessteuern, fahrkosten, fahrkostenTotal, verpflegung, verpflegungTotal);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FamilienBudgetresultatKostenDto {\n");
    
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    grundbedarf: ").append(toIndentedString(grundbedarf)).append("\n");
    sb.append("    wohnkosten: ").append(toIndentedString(wohnkosten)).append("\n");
    sb.append("    medizinischeGrundversorgung: ").append(toIndentedString(medizinischeGrundversorgung)).append("\n");
    sb.append("    integrationszulage: ").append(toIndentedString(integrationszulage)).append("\n");
    sb.append("    integrationszulageAnzahl: ").append(toIndentedString(integrationszulageAnzahl)).append("\n");
    sb.append("    integrationszulageTotal: ").append(toIndentedString(integrationszulageTotal)).append("\n");
    sb.append("    kantonsGemeindesteuern: ").append(toIndentedString(kantonsGemeindesteuern)).append("\n");
    sb.append("    bundessteuern: ").append(toIndentedString(bundessteuern)).append("\n");
    sb.append("    fahrkosten: ").append(toIndentedString(fahrkosten)).append("\n");
    sb.append("    fahrkostenTotal: ").append(toIndentedString(fahrkostenTotal)).append("\n");
    sb.append("    verpflegung: ").append(toIndentedString(verpflegung)).append("\n");
    sb.append("    verpflegungTotal: ").append(toIndentedString(verpflegungTotal)).append("\n");
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

