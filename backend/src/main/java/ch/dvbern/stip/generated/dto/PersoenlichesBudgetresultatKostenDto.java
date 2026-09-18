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
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class PersoenlichesBudgetresultatKostenDto  implements Serializable {
  private Integer total;
  private Integer ausbildungskosten;
  private Integer ausbildungskostenTotal;
  private Integer fahrkosten;
  private Integer fahrkostenTotal;
  private Integer verpflegungskosten;
  private Integer grundbedarf;
  private Integer wohnkosten;
  private @Valid List<@Valid PersonValueItemDto> medizinischeGrundversorgung = new ArrayList<>();
  private Integer medizinischeGrundversorgungTotal;
  private @Valid List<@Valid PersonValueItemDto> betreuungskosten = new ArrayList<>();
  private Integer betreuungskostenTotal;
  private Integer steuern;
  private Integer anteilLebenshaltungskosten;
  private Integer fahrkostenPartner;
  private Integer verpflegungPartner;

  protected PersoenlichesBudgetresultatKostenDto(PersoenlichesBudgetresultatKostenDtoBuilder<?, ?> b) {
    this.total = b.total;
    this.ausbildungskosten = b.ausbildungskosten;
    this.ausbildungskostenTotal = b.ausbildungskostenTotal;
    this.fahrkosten = b.fahrkosten;
    this.fahrkostenTotal = b.fahrkostenTotal;
    this.verpflegungskosten = b.verpflegungskosten;
    this.grundbedarf = b.grundbedarf;
    this.wohnkosten = b.wohnkosten;
    this.medizinischeGrundversorgung = b.medizinischeGrundversorgung;
    this.medizinischeGrundversorgungTotal = b.medizinischeGrundversorgungTotal;
    this.betreuungskosten = b.betreuungskosten;
    this.betreuungskostenTotal = b.betreuungskostenTotal;
    this.steuern = b.steuern;
    this.anteilLebenshaltungskosten = b.anteilLebenshaltungskosten;
    this.fahrkostenPartner = b.fahrkostenPartner;
    this.verpflegungPartner = b.verpflegungPartner;
  }

  public PersoenlichesBudgetresultatKostenDto() {
  }

  /**
   **/
  public PersoenlichesBudgetresultatKostenDto total(Integer total) {
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
  public PersoenlichesBudgetresultatKostenDto ausbildungskosten(Integer ausbildungskosten) {
    this.ausbildungskosten = ausbildungskosten;
    return this;
  }

  
  @JsonProperty(required = true, value = "ausbildungskosten")
  @NotNull public Integer getAusbildungskosten() {
    return ausbildungskosten;
  }

  @JsonProperty(required = true, value = "ausbildungskosten")
  public void setAusbildungskosten(Integer ausbildungskosten) {
    this.ausbildungskosten = ausbildungskosten;
  }

  /**
   **/
  public PersoenlichesBudgetresultatKostenDto ausbildungskostenTotal(Integer ausbildungskostenTotal) {
    this.ausbildungskostenTotal = ausbildungskostenTotal;
    return this;
  }

  
  @JsonProperty(required = true, value = "ausbildungskostenTotal")
  @NotNull public Integer getAusbildungskostenTotal() {
    return ausbildungskostenTotal;
  }

  @JsonProperty(required = true, value = "ausbildungskostenTotal")
  public void setAusbildungskostenTotal(Integer ausbildungskostenTotal) {
    this.ausbildungskostenTotal = ausbildungskostenTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatKostenDto fahrkosten(Integer fahrkosten) {
    this.fahrkosten = fahrkosten;
    return this;
  }

  
  @JsonProperty(required = true, value = "fahrkosten")
  @NotNull public Integer getFahrkosten() {
    return fahrkosten;
  }

  @JsonProperty(required = true, value = "fahrkosten")
  public void setFahrkosten(Integer fahrkosten) {
    this.fahrkosten = fahrkosten;
  }

  /**
   **/
  public PersoenlichesBudgetresultatKostenDto fahrkostenTotal(Integer fahrkostenTotal) {
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
  public PersoenlichesBudgetresultatKostenDto verpflegungskosten(Integer verpflegungskosten) {
    this.verpflegungskosten = verpflegungskosten;
    return this;
  }

  
  @JsonProperty(required = true, value = "verpflegungskosten")
  @NotNull public Integer getVerpflegungskosten() {
    return verpflegungskosten;
  }

  @JsonProperty(required = true, value = "verpflegungskosten")
  public void setVerpflegungskosten(Integer verpflegungskosten) {
    this.verpflegungskosten = verpflegungskosten;
  }

  /**
   **/
  public PersoenlichesBudgetresultatKostenDto grundbedarf(Integer grundbedarf) {
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
  public PersoenlichesBudgetresultatKostenDto wohnkosten(Integer wohnkosten) {
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
  public PersoenlichesBudgetresultatKostenDto medizinischeGrundversorgung(List<@Valid PersonValueItemDto> medizinischeGrundversorgung) {
    this.medizinischeGrundversorgung = medizinischeGrundversorgung;
    return this;
  }

  
  @JsonProperty(required = true, value = "medizinischeGrundversorgung")
  @NotNull @Valid public List<@Valid PersonValueItemDto> getMedizinischeGrundversorgung() {
    return medizinischeGrundversorgung;
  }

  @JsonProperty(required = true, value = "medizinischeGrundversorgung")
  public void setMedizinischeGrundversorgung(List<@Valid PersonValueItemDto> medizinischeGrundversorgung) {
    this.medizinischeGrundversorgung = medizinischeGrundversorgung;
  }

  public PersoenlichesBudgetresultatKostenDto addMedizinischeGrundversorgungItem(PersonValueItemDto medizinischeGrundversorgungItem) {
    if (this.medizinischeGrundversorgung == null) {
      this.medizinischeGrundversorgung = new ArrayList<>();
    }

    this.medizinischeGrundversorgung.add(medizinischeGrundversorgungItem);
    return this;
  }

  public PersoenlichesBudgetresultatKostenDto removeMedizinischeGrundversorgungItem(PersonValueItemDto medizinischeGrundversorgungItem) {
    if (medizinischeGrundversorgungItem != null && this.medizinischeGrundversorgung != null) {
      this.medizinischeGrundversorgung.remove(medizinischeGrundversorgungItem);
    }

    return this;
  }
  /**
   **/
  public PersoenlichesBudgetresultatKostenDto medizinischeGrundversorgungTotal(Integer medizinischeGrundversorgungTotal) {
    this.medizinischeGrundversorgungTotal = medizinischeGrundversorgungTotal;
    return this;
  }

  
  @JsonProperty(required = true, value = "medizinischeGrundversorgungTotal")
  @NotNull public Integer getMedizinischeGrundversorgungTotal() {
    return medizinischeGrundversorgungTotal;
  }

  @JsonProperty(required = true, value = "medizinischeGrundversorgungTotal")
  public void setMedizinischeGrundversorgungTotal(Integer medizinischeGrundversorgungTotal) {
    this.medizinischeGrundversorgungTotal = medizinischeGrundversorgungTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatKostenDto betreuungskosten(List<@Valid PersonValueItemDto> betreuungskosten) {
    this.betreuungskosten = betreuungskosten;
    return this;
  }

  
  @JsonProperty(required = true, value = "betreuungskosten")
  @NotNull @Valid public List<@Valid PersonValueItemDto> getBetreuungskosten() {
    return betreuungskosten;
  }

  @JsonProperty(required = true, value = "betreuungskosten")
  public void setBetreuungskosten(List<@Valid PersonValueItemDto> betreuungskosten) {
    this.betreuungskosten = betreuungskosten;
  }

  public PersoenlichesBudgetresultatKostenDto addBetreuungskostenItem(PersonValueItemDto betreuungskostenItem) {
    if (this.betreuungskosten == null) {
      this.betreuungskosten = new ArrayList<>();
    }

    this.betreuungskosten.add(betreuungskostenItem);
    return this;
  }

  public PersoenlichesBudgetresultatKostenDto removeBetreuungskostenItem(PersonValueItemDto betreuungskostenItem) {
    if (betreuungskostenItem != null && this.betreuungskosten != null) {
      this.betreuungskosten.remove(betreuungskostenItem);
    }

    return this;
  }
  /**
   **/
  public PersoenlichesBudgetresultatKostenDto betreuungskostenTotal(Integer betreuungskostenTotal) {
    this.betreuungskostenTotal = betreuungskostenTotal;
    return this;
  }

  
  @JsonProperty(required = true, value = "betreuungskostenTotal")
  @NotNull public Integer getBetreuungskostenTotal() {
    return betreuungskostenTotal;
  }

  @JsonProperty(required = true, value = "betreuungskostenTotal")
  public void setBetreuungskostenTotal(Integer betreuungskostenTotal) {
    this.betreuungskostenTotal = betreuungskostenTotal;
  }

  /**
   **/
  public PersoenlichesBudgetresultatKostenDto steuern(Integer steuern) {
    this.steuern = steuern;
    return this;
  }

  
  @JsonProperty(required = true, value = "steuern")
  @NotNull public Integer getSteuern() {
    return steuern;
  }

  @JsonProperty(required = true, value = "steuern")
  public void setSteuern(Integer steuern) {
    this.steuern = steuern;
  }

  /**
   **/
  public PersoenlichesBudgetresultatKostenDto anteilLebenshaltungskosten(Integer anteilLebenshaltungskosten) {
    this.anteilLebenshaltungskosten = anteilLebenshaltungskosten;
    return this;
  }

  
  @JsonProperty(required = true, value = "anteilLebenshaltungskosten")
  @NotNull public Integer getAnteilLebenshaltungskosten() {
    return anteilLebenshaltungskosten;
  }

  @JsonProperty(required = true, value = "anteilLebenshaltungskosten")
  public void setAnteilLebenshaltungskosten(Integer anteilLebenshaltungskosten) {
    this.anteilLebenshaltungskosten = anteilLebenshaltungskosten;
  }

  /**
   **/
  public PersoenlichesBudgetresultatKostenDto fahrkostenPartner(Integer fahrkostenPartner) {
    this.fahrkostenPartner = fahrkostenPartner;
    return this;
  }

  
  @JsonProperty("fahrkostenPartner")
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
  public Integer getVerpflegungPartner() {
    return verpflegungPartner;
  }

  @JsonProperty("verpflegungPartner")
  public void setVerpflegungPartner(Integer verpflegungPartner) {
    this.verpflegungPartner = verpflegungPartner;
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
        Objects.equals(this.ausbildungskostenTotal, persoenlichesBudgetresultatKosten.ausbildungskostenTotal) &&
        Objects.equals(this.fahrkosten, persoenlichesBudgetresultatKosten.fahrkosten) &&
        Objects.equals(this.fahrkostenTotal, persoenlichesBudgetresultatKosten.fahrkostenTotal) &&
        Objects.equals(this.verpflegungskosten, persoenlichesBudgetresultatKosten.verpflegungskosten) &&
        Objects.equals(this.grundbedarf, persoenlichesBudgetresultatKosten.grundbedarf) &&
        Objects.equals(this.wohnkosten, persoenlichesBudgetresultatKosten.wohnkosten) &&
        Objects.equals(this.medizinischeGrundversorgung, persoenlichesBudgetresultatKosten.medizinischeGrundversorgung) &&
        Objects.equals(this.medizinischeGrundversorgungTotal, persoenlichesBudgetresultatKosten.medizinischeGrundversorgungTotal) &&
        Objects.equals(this.betreuungskosten, persoenlichesBudgetresultatKosten.betreuungskosten) &&
        Objects.equals(this.betreuungskostenTotal, persoenlichesBudgetresultatKosten.betreuungskostenTotal) &&
        Objects.equals(this.steuern, persoenlichesBudgetresultatKosten.steuern) &&
        Objects.equals(this.anteilLebenshaltungskosten, persoenlichesBudgetresultatKosten.anteilLebenshaltungskosten) &&
        Objects.equals(this.fahrkostenPartner, persoenlichesBudgetresultatKosten.fahrkostenPartner) &&
        Objects.equals(this.verpflegungPartner, persoenlichesBudgetresultatKosten.verpflegungPartner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(total, ausbildungskosten, ausbildungskostenTotal, fahrkosten, fahrkostenTotal, verpflegungskosten, grundbedarf, wohnkosten, medizinischeGrundversorgung, medizinischeGrundversorgungTotal, betreuungskosten, betreuungskostenTotal, steuern, anteilLebenshaltungskosten, fahrkostenPartner, verpflegungPartner);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PersoenlichesBudgetresultatKostenDto {\n");
    
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    ausbildungskosten: ").append(toIndentedString(ausbildungskosten)).append("\n");
    sb.append("    ausbildungskostenTotal: ").append(toIndentedString(ausbildungskostenTotal)).append("\n");
    sb.append("    fahrkosten: ").append(toIndentedString(fahrkosten)).append("\n");
    sb.append("    fahrkostenTotal: ").append(toIndentedString(fahrkostenTotal)).append("\n");
    sb.append("    verpflegungskosten: ").append(toIndentedString(verpflegungskosten)).append("\n");
    sb.append("    grundbedarf: ").append(toIndentedString(grundbedarf)).append("\n");
    sb.append("    wohnkosten: ").append(toIndentedString(wohnkosten)).append("\n");
    sb.append("    medizinischeGrundversorgung: ").append(toIndentedString(medizinischeGrundversorgung)).append("\n");
    sb.append("    medizinischeGrundversorgungTotal: ").append(toIndentedString(medizinischeGrundversorgungTotal)).append("\n");
    sb.append("    betreuungskosten: ").append(toIndentedString(betreuungskosten)).append("\n");
    sb.append("    betreuungskostenTotal: ").append(toIndentedString(betreuungskostenTotal)).append("\n");
    sb.append("    steuern: ").append(toIndentedString(steuern)).append("\n");
    sb.append("    anteilLebenshaltungskosten: ").append(toIndentedString(anteilLebenshaltungskosten)).append("\n");
    sb.append("    fahrkostenPartner: ").append(toIndentedString(fahrkostenPartner)).append("\n");
    sb.append("    verpflegungPartner: ").append(toIndentedString(verpflegungPartner)).append("\n");
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


  public static PersoenlichesBudgetresultatKostenDtoBuilder<?, ?> builder() {
    return new PersoenlichesBudgetresultatKostenDtoBuilderImpl();
  }

  private static final class PersoenlichesBudgetresultatKostenDtoBuilderImpl extends PersoenlichesBudgetresultatKostenDtoBuilder<PersoenlichesBudgetresultatKostenDto, PersoenlichesBudgetresultatKostenDtoBuilderImpl> {

    @Override
    protected PersoenlichesBudgetresultatKostenDtoBuilderImpl self() {
      return this;
    }

    @Override
    public PersoenlichesBudgetresultatKostenDto build() {
      return new PersoenlichesBudgetresultatKostenDto(this);
    }
  }

  public static abstract class PersoenlichesBudgetresultatKostenDtoBuilder<C extends PersoenlichesBudgetresultatKostenDto, B extends PersoenlichesBudgetresultatKostenDtoBuilder<C, B>>  {
    private Integer total;
    private Integer ausbildungskosten;
    private Integer ausbildungskostenTotal;
    private Integer fahrkosten;
    private Integer fahrkostenTotal;
    private Integer verpflegungskosten;
    private Integer grundbedarf;
    private Integer wohnkosten;
    private List<PersonValueItemDto> medizinischeGrundversorgung = new ArrayList<>();
    private Integer medizinischeGrundversorgungTotal;
    private List<PersonValueItemDto> betreuungskosten = new ArrayList<>();
    private Integer betreuungskostenTotal;
    private Integer steuern;
    private Integer anteilLebenshaltungskosten;
    private Integer fahrkostenPartner;
    private Integer verpflegungPartner;
    protected abstract B self();

    public abstract C build();

    public B total(Integer total) {
      this.total = total;
      return self();
    }
    public B ausbildungskosten(Integer ausbildungskosten) {
      this.ausbildungskosten = ausbildungskosten;
      return self();
    }
    public B ausbildungskostenTotal(Integer ausbildungskostenTotal) {
      this.ausbildungskostenTotal = ausbildungskostenTotal;
      return self();
    }
    public B fahrkosten(Integer fahrkosten) {
      this.fahrkosten = fahrkosten;
      return self();
    }
    public B fahrkostenTotal(Integer fahrkostenTotal) {
      this.fahrkostenTotal = fahrkostenTotal;
      return self();
    }
    public B verpflegungskosten(Integer verpflegungskosten) {
      this.verpflegungskosten = verpflegungskosten;
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
    public B medizinischeGrundversorgung(List<PersonValueItemDto> medizinischeGrundversorgung) {
      this.medizinischeGrundversorgung = medizinischeGrundversorgung;
      return self();
    }
    public B medizinischeGrundversorgungTotal(Integer medizinischeGrundversorgungTotal) {
      this.medizinischeGrundversorgungTotal = medizinischeGrundversorgungTotal;
      return self();
    }
    public B betreuungskosten(List<PersonValueItemDto> betreuungskosten) {
      this.betreuungskosten = betreuungskosten;
      return self();
    }
    public B betreuungskostenTotal(Integer betreuungskostenTotal) {
      this.betreuungskostenTotal = betreuungskostenTotal;
      return self();
    }
    public B steuern(Integer steuern) {
      this.steuern = steuern;
      return self();
    }
    public B anteilLebenshaltungskosten(Integer anteilLebenshaltungskosten) {
      this.anteilLebenshaltungskosten = anteilLebenshaltungskosten;
      return self();
    }
    public B fahrkostenPartner(Integer fahrkostenPartner) {
      this.fahrkostenPartner = fahrkostenPartner;
      return self();
    }
    public B verpflegungPartner(Integer verpflegungPartner) {
      this.verpflegungPartner = verpflegungPartner;
      return self();
    }
  }
}
