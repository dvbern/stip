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
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class FamilienBudgetresultatKostenDto  implements Serializable {
  private Integer total;
  private Integer grundbedarf;
  private Integer wohnkosten;
  private Integer medizinischeGrundversorgung;
  private Integer integrationszulage;
  private Integer integrationszulageAnzahl;
  private Integer integrationszulageTotal;
  private Integer kantonsGemeindesteuern;
  private Integer bundessteuern;
  private @Valid List<@Valid PersonValueItemDto> fahrkosten = new ArrayList<>();
  private Integer fahrkostenTotal;
  private @Valid List<@Valid PersonValueItemDto> verpflegung = new ArrayList<>();
  private Integer verpflegungTotal;

  protected FamilienBudgetresultatKostenDto(FamilienBudgetresultatKostenDtoBuilder<?, ?> b) {
    this.total = b.total;
    this.grundbedarf = b.grundbedarf;
    this.wohnkosten = b.wohnkosten;
    this.medizinischeGrundversorgung = b.medizinischeGrundversorgung;
    this.integrationszulage = b.integrationszulage;
    this.integrationszulageAnzahl = b.integrationszulageAnzahl;
    this.integrationszulageTotal = b.integrationszulageTotal;
    this.kantonsGemeindesteuern = b.kantonsGemeindesteuern;
    this.bundessteuern = b.bundessteuern;
    this.fahrkosten = b.fahrkosten;
    this.fahrkostenTotal = b.fahrkostenTotal;
    this.verpflegung = b.verpflegung;
    this.verpflegungTotal = b.verpflegungTotal;
  }

  public FamilienBudgetresultatKostenDto() {
  }

  /**
   **/
  public FamilienBudgetresultatKostenDto total(Integer total) {
    this.total = total;
    return this;
  }

  
  @JsonProperty(required = true, value = "total")
  @NotNull public Integer getTotal() {
    return total;
  }

  @JsonProperty(required = true, value = "total")
  public void setTotal(Integer total) {
    this.total = total;
  }

  /**
   **/
  public FamilienBudgetresultatKostenDto grundbedarf(Integer grundbedarf) {
    this.grundbedarf = grundbedarf;
    return this;
  }

  
  @JsonProperty(required = true, value = "grundbedarf")
  @NotNull public Integer getGrundbedarf() {
    return grundbedarf;
  }

  @JsonProperty(required = true, value = "grundbedarf")
  public void setGrundbedarf(Integer grundbedarf) {
    this.grundbedarf = grundbedarf;
  }

  /**
   **/
  public FamilienBudgetresultatKostenDto wohnkosten(Integer wohnkosten) {
    this.wohnkosten = wohnkosten;
    return this;
  }

  
  @JsonProperty(required = true, value = "wohnkosten")
  @NotNull public Integer getWohnkosten() {
    return wohnkosten;
  }

  @JsonProperty(required = true, value = "wohnkosten")
  public void setWohnkosten(Integer wohnkosten) {
    this.wohnkosten = wohnkosten;
  }

  /**
   **/
  public FamilienBudgetresultatKostenDto medizinischeGrundversorgung(Integer medizinischeGrundversorgung) {
    this.medizinischeGrundversorgung = medizinischeGrundversorgung;
    return this;
  }

  
  @JsonProperty(required = true, value = "medizinischeGrundversorgung")
  @NotNull public Integer getMedizinischeGrundversorgung() {
    return medizinischeGrundversorgung;
  }

  @JsonProperty(required = true, value = "medizinischeGrundversorgung")
  public void setMedizinischeGrundversorgung(Integer medizinischeGrundversorgung) {
    this.medizinischeGrundversorgung = medizinischeGrundversorgung;
  }

  /**
   **/
  public FamilienBudgetresultatKostenDto integrationszulage(Integer integrationszulage) {
    this.integrationszulage = integrationszulage;
    return this;
  }

  
  @JsonProperty(required = true, value = "integrationszulage")
  @NotNull public Integer getIntegrationszulage() {
    return integrationszulage;
  }

  @JsonProperty(required = true, value = "integrationszulage")
  public void setIntegrationszulage(Integer integrationszulage) {
    this.integrationszulage = integrationszulage;
  }

  /**
   **/
  public FamilienBudgetresultatKostenDto integrationszulageAnzahl(Integer integrationszulageAnzahl) {
    this.integrationszulageAnzahl = integrationszulageAnzahl;
    return this;
  }

  
  @JsonProperty(required = true, value = "integrationszulageAnzahl")
  @NotNull public Integer getIntegrationszulageAnzahl() {
    return integrationszulageAnzahl;
  }

  @JsonProperty(required = true, value = "integrationszulageAnzahl")
  public void setIntegrationszulageAnzahl(Integer integrationszulageAnzahl) {
    this.integrationszulageAnzahl = integrationszulageAnzahl;
  }

  /**
   **/
  public FamilienBudgetresultatKostenDto integrationszulageTotal(Integer integrationszulageTotal) {
    this.integrationszulageTotal = integrationszulageTotal;
    return this;
  }

  
  @JsonProperty(required = true, value = "integrationszulageTotal")
  @NotNull public Integer getIntegrationszulageTotal() {
    return integrationszulageTotal;
  }

  @JsonProperty(required = true, value = "integrationszulageTotal")
  public void setIntegrationszulageTotal(Integer integrationszulageTotal) {
    this.integrationszulageTotal = integrationszulageTotal;
  }

  /**
   **/
  public FamilienBudgetresultatKostenDto kantonsGemeindesteuern(Integer kantonsGemeindesteuern) {
    this.kantonsGemeindesteuern = kantonsGemeindesteuern;
    return this;
  }

  
  @JsonProperty(required = true, value = "kantonsGemeindesteuern")
  @NotNull public Integer getKantonsGemeindesteuern() {
    return kantonsGemeindesteuern;
  }

  @JsonProperty(required = true, value = "kantonsGemeindesteuern")
  public void setKantonsGemeindesteuern(Integer kantonsGemeindesteuern) {
    this.kantonsGemeindesteuern = kantonsGemeindesteuern;
  }

  /**
   **/
  public FamilienBudgetresultatKostenDto bundessteuern(Integer bundessteuern) {
    this.bundessteuern = bundessteuern;
    return this;
  }

  
  @JsonProperty(required = true, value = "bundessteuern")
  @NotNull public Integer getBundessteuern() {
    return bundessteuern;
  }

  @JsonProperty(required = true, value = "bundessteuern")
  public void setBundessteuern(Integer bundessteuern) {
    this.bundessteuern = bundessteuern;
  }

  /**
   **/
  public FamilienBudgetresultatKostenDto fahrkosten(List<@Valid PersonValueItemDto> fahrkosten) {
    this.fahrkosten = fahrkosten;
    return this;
  }

  
  @JsonProperty(required = true, value = "fahrkosten")
  @NotNull @Valid public List<@Valid PersonValueItemDto> getFahrkosten() {
    return fahrkosten;
  }

  @JsonProperty(required = true, value = "fahrkosten")
  public void setFahrkosten(List<@Valid PersonValueItemDto> fahrkosten) {
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

  
  @JsonProperty(required = true, value = "fahrkostenTotal")
  @NotNull public Integer getFahrkostenTotal() {
    return fahrkostenTotal;
  }

  @JsonProperty(required = true, value = "fahrkostenTotal")
  public void setFahrkostenTotal(Integer fahrkostenTotal) {
    this.fahrkostenTotal = fahrkostenTotal;
  }

  /**
   **/
  public FamilienBudgetresultatKostenDto verpflegung(List<@Valid PersonValueItemDto> verpflegung) {
    this.verpflegung = verpflegung;
    return this;
  }

  
  @JsonProperty(required = true, value = "verpflegung")
  @NotNull @Valid public List<@Valid PersonValueItemDto> getVerpflegung() {
    return verpflegung;
  }

  @JsonProperty(required = true, value = "verpflegung")
  public void setVerpflegung(List<@Valid PersonValueItemDto> verpflegung) {
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

  
  @JsonProperty(required = true, value = "verpflegungTotal")
  @NotNull public Integer getVerpflegungTotal() {
    return verpflegungTotal;
  }

  @JsonProperty(required = true, value = "verpflegungTotal")
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
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }


  public static FamilienBudgetresultatKostenDtoBuilder<?, ?> builder() {
    return new FamilienBudgetresultatKostenDtoBuilderImpl();
  }

  private static final class FamilienBudgetresultatKostenDtoBuilderImpl extends FamilienBudgetresultatKostenDtoBuilder<FamilienBudgetresultatKostenDto, FamilienBudgetresultatKostenDtoBuilderImpl> {

    @Override
    protected FamilienBudgetresultatKostenDtoBuilderImpl self() {
      return this;
    }

    @Override
    public FamilienBudgetresultatKostenDto build() {
      return new FamilienBudgetresultatKostenDto(this);
    }
  }

  public static abstract class FamilienBudgetresultatKostenDtoBuilder<C extends FamilienBudgetresultatKostenDto, B extends FamilienBudgetresultatKostenDtoBuilder<C, B>>  {
    private Integer total;
    private Integer grundbedarf;
    private Integer wohnkosten;
    private Integer medizinischeGrundversorgung;
    private Integer integrationszulage;
    private Integer integrationszulageAnzahl;
    private Integer integrationszulageTotal;
    private Integer kantonsGemeindesteuern;
    private Integer bundessteuern;
    private List<PersonValueItemDto> fahrkosten = new ArrayList<>();
    private Integer fahrkostenTotal;
    private List<PersonValueItemDto> verpflegung = new ArrayList<>();
    private Integer verpflegungTotal;
    protected abstract B self();

    public abstract C build();

    public B total(Integer total) {
      this.total = total;
      return self();
    }
    public B grundbedarf(Integer grundbedarf) {
      this.grundbedarf = grundbedarf;
      return self();
    }
    public B wohnkosten(Integer wohnkosten) {
      this.wohnkosten = wohnkosten;
      return self();
    }
    public B medizinischeGrundversorgung(Integer medizinischeGrundversorgung) {
      this.medizinischeGrundversorgung = medizinischeGrundversorgung;
      return self();
    }
    public B integrationszulage(Integer integrationszulage) {
      this.integrationszulage = integrationszulage;
      return self();
    }
    public B integrationszulageAnzahl(Integer integrationszulageAnzahl) {
      this.integrationszulageAnzahl = integrationszulageAnzahl;
      return self();
    }
    public B integrationszulageTotal(Integer integrationszulageTotal) {
      this.integrationszulageTotal = integrationszulageTotal;
      return self();
    }
    public B kantonsGemeindesteuern(Integer kantonsGemeindesteuern) {
      this.kantonsGemeindesteuern = kantonsGemeindesteuern;
      return self();
    }
    public B bundessteuern(Integer bundessteuern) {
      this.bundessteuern = bundessteuern;
      return self();
    }
    public B fahrkosten(List<PersonValueItemDto> fahrkosten) {
      this.fahrkosten = fahrkosten;
      return self();
    }
    public B fahrkostenTotal(Integer fahrkostenTotal) {
      this.fahrkostenTotal = fahrkostenTotal;
      return self();
    }
    public B verpflegung(List<PersonValueItemDto> verpflegung) {
      this.verpflegung = verpflegung;
      return self();
    }
    public B verpflegungTotal(Integer verpflegungTotal) {
      this.verpflegungTotal = verpflegungTotal;
      return self();
    }
  }
}
