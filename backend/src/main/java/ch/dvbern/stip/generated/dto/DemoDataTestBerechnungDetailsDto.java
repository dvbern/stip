package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DemoBudgettypDto;
import ch.dvbern.stip.generated.dto.DemoDataTestBudgetDto;
import ch.dvbern.stip.generated.dto.DemoDataTestElternBudgetDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("DemoDataTestBerechnungDetails")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class DemoDataTestBerechnungDetailsDto  implements Serializable {
  private DemoBudgettypDto budgetArt;
  private DemoDataTestElternBudgetDto elternBudget1;
  private DemoDataTestElternBudgetDto elternBudget2;
  private DemoDataTestBudgetDto persoenlichesBudget;
  private Integer budgetTranche;

  protected DemoDataTestBerechnungDetailsDto(DemoDataTestBerechnungDetailsDtoBuilder<?, ?> b) {
    this.budgetArt = b.budgetArt;
    this.elternBudget1 = b.elternBudget1;
    this.elternBudget2 = b.elternBudget2;
    this.persoenlichesBudget = b.persoenlichesBudget;
    this.budgetTranche = b.budgetTranche;
  }

  public DemoDataTestBerechnungDetailsDto() {
  }

  /**
   **/
  public DemoDataTestBerechnungDetailsDto budgetArt(DemoBudgettypDto budgetArt) {
    this.budgetArt = budgetArt;
    return this;
  }

  
  @JsonProperty("budgetArt")
  public DemoBudgettypDto getBudgetArt() {
    return budgetArt;
  }

  @JsonProperty("budgetArt")
  public void setBudgetArt(DemoBudgettypDto budgetArt) {
    this.budgetArt = budgetArt;
  }

  /**
   **/
  public DemoDataTestBerechnungDetailsDto elternBudget1(DemoDataTestElternBudgetDto elternBudget1) {
    this.elternBudget1 = elternBudget1;
    return this;
  }

  
  @JsonProperty("elternBudget1")
  @Valid public DemoDataTestElternBudgetDto getElternBudget1() {
    return elternBudget1;
  }

  @JsonProperty("elternBudget1")
  public void setElternBudget1(DemoDataTestElternBudgetDto elternBudget1) {
    this.elternBudget1 = elternBudget1;
  }

  /**
   **/
  public DemoDataTestBerechnungDetailsDto elternBudget2(DemoDataTestElternBudgetDto elternBudget2) {
    this.elternBudget2 = elternBudget2;
    return this;
  }

  
  @JsonProperty("elternBudget2")
  @Valid public DemoDataTestElternBudgetDto getElternBudget2() {
    return elternBudget2;
  }

  @JsonProperty("elternBudget2")
  public void setElternBudget2(DemoDataTestElternBudgetDto elternBudget2) {
    this.elternBudget2 = elternBudget2;
  }

  /**
   **/
  public DemoDataTestBerechnungDetailsDto persoenlichesBudget(DemoDataTestBudgetDto persoenlichesBudget) {
    this.persoenlichesBudget = persoenlichesBudget;
    return this;
  }

  
  @JsonProperty("persoenlichesBudget")
  @Valid public DemoDataTestBudgetDto getPersoenlichesBudget() {
    return persoenlichesBudget;
  }

  @JsonProperty("persoenlichesBudget")
  public void setPersoenlichesBudget(DemoDataTestBudgetDto persoenlichesBudget) {
    this.persoenlichesBudget = persoenlichesBudget;
  }

  /**
   **/
  public DemoDataTestBerechnungDetailsDto budgetTranche(Integer budgetTranche) {
    this.budgetTranche = budgetTranche;
    return this;
  }

  
  @JsonProperty("budgetTranche")
  public Integer getBudgetTranche() {
    return budgetTranche;
  }

  @JsonProperty("budgetTranche")
  public void setBudgetTranche(Integer budgetTranche) {
    this.budgetTranche = budgetTranche;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoDataTestBerechnungDetailsDto demoDataTestBerechnungDetails = (DemoDataTestBerechnungDetailsDto) o;
    return Objects.equals(this.budgetArt, demoDataTestBerechnungDetails.budgetArt) &&
        Objects.equals(this.elternBudget1, demoDataTestBerechnungDetails.elternBudget1) &&
        Objects.equals(this.elternBudget2, demoDataTestBerechnungDetails.elternBudget2) &&
        Objects.equals(this.persoenlichesBudget, demoDataTestBerechnungDetails.persoenlichesBudget) &&
        Objects.equals(this.budgetTranche, demoDataTestBerechnungDetails.budgetTranche);
  }

  @Override
  public int hashCode() {
    return Objects.hash(budgetArt, elternBudget1, elternBudget2, persoenlichesBudget, budgetTranche);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoDataTestBerechnungDetailsDto {\n");
    
    sb.append("    budgetArt: ").append(toIndentedString(budgetArt)).append("\n");
    sb.append("    elternBudget1: ").append(toIndentedString(elternBudget1)).append("\n");
    sb.append("    elternBudget2: ").append(toIndentedString(elternBudget2)).append("\n");
    sb.append("    persoenlichesBudget: ").append(toIndentedString(persoenlichesBudget)).append("\n");
    sb.append("    budgetTranche: ").append(toIndentedString(budgetTranche)).append("\n");
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


  public static DemoDataTestBerechnungDetailsDtoBuilder<?, ?> builder() {
    return new DemoDataTestBerechnungDetailsDtoBuilderImpl();
  }

  private static final class DemoDataTestBerechnungDetailsDtoBuilderImpl extends DemoDataTestBerechnungDetailsDtoBuilder<DemoDataTestBerechnungDetailsDto, DemoDataTestBerechnungDetailsDtoBuilderImpl> {

    @Override
    protected DemoDataTestBerechnungDetailsDtoBuilderImpl self() {
      return this;
    }

    @Override
    public DemoDataTestBerechnungDetailsDto build() {
      return new DemoDataTestBerechnungDetailsDto(this);
    }
  }

  public static abstract class DemoDataTestBerechnungDetailsDtoBuilder<C extends DemoDataTestBerechnungDetailsDto, B extends DemoDataTestBerechnungDetailsDtoBuilder<C, B>>  {
    private DemoBudgettypDto budgetArt;
    private DemoDataTestElternBudgetDto elternBudget1;
    private DemoDataTestElternBudgetDto elternBudget2;
    private DemoDataTestBudgetDto persoenlichesBudget;
    private Integer budgetTranche;
    protected abstract B self();

    public abstract C build();

    public B budgetArt(DemoBudgettypDto budgetArt) {
      this.budgetArt = budgetArt;
      return self();
    }
    public B elternBudget1(DemoDataTestElternBudgetDto elternBudget1) {
      this.elternBudget1 = elternBudget1;
      return self();
    }
    public B elternBudget2(DemoDataTestElternBudgetDto elternBudget2) {
      this.elternBudget2 = elternBudget2;
      return self();
    }
    public B persoenlichesBudget(DemoDataTestBudgetDto persoenlichesBudget) {
      this.persoenlichesBudget = persoenlichesBudget;
      return self();
    }
    public B budgetTranche(Integer budgetTranche) {
      this.budgetTranche = budgetTranche;
      return self();
    }
  }
}
