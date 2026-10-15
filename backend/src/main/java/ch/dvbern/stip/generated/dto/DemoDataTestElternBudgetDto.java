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



@JsonTypeName("DemoDataTestElternBudget")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class DemoDataTestElternBudgetDto  implements Serializable {
  private Integer totalEinnahmen;
  private Integer totalKosten;
  private Integer einnahmeUeberschuss;
  private Integer fehlbetrag;

  protected DemoDataTestElternBudgetDto(DemoDataTestElternBudgetDtoBuilder<?, ?> b) {
    this.totalEinnahmen = b.totalEinnahmen;
    this.totalKosten = b.totalKosten;
    this.einnahmeUeberschuss = b.einnahmeUeberschuss;
    this.fehlbetrag = b.fehlbetrag;
  }

  public DemoDataTestElternBudgetDto() {
  }

  /**
   **/
  public DemoDataTestElternBudgetDto totalEinnahmen(Integer totalEinnahmen) {
    this.totalEinnahmen = totalEinnahmen;
    return this;
  }

  
  @JsonProperty("totalEinnahmen")
  public Integer getTotalEinnahmen() {
    return totalEinnahmen;
  }

  @JsonProperty("totalEinnahmen")
  public void setTotalEinnahmen(Integer totalEinnahmen) {
    this.totalEinnahmen = totalEinnahmen;
  }

  /**
   **/
  public DemoDataTestElternBudgetDto totalKosten(Integer totalKosten) {
    this.totalKosten = totalKosten;
    return this;
  }

  
  @JsonProperty("totalKosten")
  public Integer getTotalKosten() {
    return totalKosten;
  }

  @JsonProperty("totalKosten")
  public void setTotalKosten(Integer totalKosten) {
    this.totalKosten = totalKosten;
  }

  /**
   **/
  public DemoDataTestElternBudgetDto einnahmeUeberschuss(Integer einnahmeUeberschuss) {
    this.einnahmeUeberschuss = einnahmeUeberschuss;
    return this;
  }

  
  @JsonProperty("einnahmeUeberschuss")
  public Integer getEinnahmeUeberschuss() {
    return einnahmeUeberschuss;
  }

  @JsonProperty("einnahmeUeberschuss")
  public void setEinnahmeUeberschuss(Integer einnahmeUeberschuss) {
    this.einnahmeUeberschuss = einnahmeUeberschuss;
  }

  /**
   **/
  public DemoDataTestElternBudgetDto fehlbetrag(Integer fehlbetrag) {
    this.fehlbetrag = fehlbetrag;
    return this;
  }

  
  @JsonProperty("fehlbetrag")
  public Integer getFehlbetrag() {
    return fehlbetrag;
  }

  @JsonProperty("fehlbetrag")
  public void setFehlbetrag(Integer fehlbetrag) {
    this.fehlbetrag = fehlbetrag;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoDataTestElternBudgetDto demoDataTestElternBudget = (DemoDataTestElternBudgetDto) o;
    return Objects.equals(this.totalEinnahmen, demoDataTestElternBudget.totalEinnahmen) &&
        Objects.equals(this.totalKosten, demoDataTestElternBudget.totalKosten) &&
        Objects.equals(this.einnahmeUeberschuss, demoDataTestElternBudget.einnahmeUeberschuss) &&
        Objects.equals(this.fehlbetrag, demoDataTestElternBudget.fehlbetrag);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalEinnahmen, totalKosten, einnahmeUeberschuss, fehlbetrag);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoDataTestElternBudgetDto {\n");
    
    sb.append("    totalEinnahmen: ").append(toIndentedString(totalEinnahmen)).append("\n");
    sb.append("    totalKosten: ").append(toIndentedString(totalKosten)).append("\n");
    sb.append("    einnahmeUeberschuss: ").append(toIndentedString(einnahmeUeberschuss)).append("\n");
    sb.append("    fehlbetrag: ").append(toIndentedString(fehlbetrag)).append("\n");
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


  public static DemoDataTestElternBudgetDtoBuilder<?, ?> builder() {
    return new DemoDataTestElternBudgetDtoBuilderImpl();
  }

  private static final class DemoDataTestElternBudgetDtoBuilderImpl extends DemoDataTestElternBudgetDtoBuilder<DemoDataTestElternBudgetDto, DemoDataTestElternBudgetDtoBuilderImpl> {

    @Override
    protected DemoDataTestElternBudgetDtoBuilderImpl self() {
      return this;
    }

    @Override
    public DemoDataTestElternBudgetDto build() {
      return new DemoDataTestElternBudgetDto(this);
    }
  }

  public static abstract class DemoDataTestElternBudgetDtoBuilder<C extends DemoDataTestElternBudgetDto, B extends DemoDataTestElternBudgetDtoBuilder<C, B>>  {
    private Integer totalEinnahmen;
    private Integer totalKosten;
    private Integer einnahmeUeberschuss;
    private Integer fehlbetrag;
    protected abstract B self();

    public abstract C build();

    public B totalEinnahmen(Integer totalEinnahmen) {
      this.totalEinnahmen = totalEinnahmen;
      return self();
    }
    public B totalKosten(Integer totalKosten) {
      this.totalKosten = totalKosten;
      return self();
    }
    public B einnahmeUeberschuss(Integer einnahmeUeberschuss) {
      this.einnahmeUeberschuss = einnahmeUeberschuss;
      return self();
    }
    public B fehlbetrag(Integer fehlbetrag) {
      this.fehlbetrag = fehlbetrag;
      return self();
    }
  }
}
