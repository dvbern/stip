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



@JsonTypeName("DemoDataTestBudget")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class DemoDataTestBudgetDto  implements Serializable {
  private Integer totalEinnahmen;
  private Integer totalKosten;

  protected DemoDataTestBudgetDto(DemoDataTestBudgetDtoBuilder<?, ?> b) {
    this.totalEinnahmen = b.totalEinnahmen;
    this.totalKosten = b.totalKosten;
  }

  public DemoDataTestBudgetDto() {
  }

  /**
   **/
  public DemoDataTestBudgetDto totalEinnahmen(Integer totalEinnahmen) {
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
  public DemoDataTestBudgetDto totalKosten(Integer totalKosten) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoDataTestBudgetDto demoDataTestBudget = (DemoDataTestBudgetDto) o;
    return Objects.equals(this.totalEinnahmen, demoDataTestBudget.totalEinnahmen) &&
        Objects.equals(this.totalKosten, demoDataTestBudget.totalKosten);
  }

  @Override
  public int hashCode() {
    return Objects.hash(totalEinnahmen, totalKosten);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoDataTestBudgetDto {\n");
    
    sb.append("    totalEinnahmen: ").append(toIndentedString(totalEinnahmen)).append("\n");
    sb.append("    totalKosten: ").append(toIndentedString(totalKosten)).append("\n");
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


  public static DemoDataTestBudgetDtoBuilder<?, ?> builder() {
    return new DemoDataTestBudgetDtoBuilderImpl();
  }

  private static final class DemoDataTestBudgetDtoBuilderImpl extends DemoDataTestBudgetDtoBuilder<DemoDataTestBudgetDto, DemoDataTestBudgetDtoBuilderImpl> {

    @Override
    protected DemoDataTestBudgetDtoBuilderImpl self() {
      return this;
    }

    @Override
    public DemoDataTestBudgetDto build() {
      return new DemoDataTestBudgetDto(this);
    }
  }

  public static abstract class DemoDataTestBudgetDtoBuilder<C extends DemoDataTestBudgetDto, B extends DemoDataTestBudgetDtoBuilder<C, B>>  {
    private Integer totalEinnahmen;
    private Integer totalKosten;
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
  }
}
