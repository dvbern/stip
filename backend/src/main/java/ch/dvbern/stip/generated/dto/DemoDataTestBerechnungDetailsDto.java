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
  private Integer fehlbetrag;
  private Integer proKopfteilung;
  private Integer anzahlMonateEinreichefrist;
  private Integer totalNachKuerzungNachEinreichefrist;

  protected DemoDataTestBerechnungDetailsDto(DemoDataTestBerechnungDetailsDtoBuilder<?, ?> b) {
    this.budgetArt = b.budgetArt;
    this.elternBudget1 = b.elternBudget1;
    this.elternBudget2 = b.elternBudget2;
    this.persoenlichesBudget = b.persoenlichesBudget;
    this.fehlbetrag = b.fehlbetrag;
    this.proKopfteilung = b.proKopfteilung;
    this.anzahlMonateEinreichefrist = b.anzahlMonateEinreichefrist;
    this.totalNachKuerzungNachEinreichefrist = b.totalNachKuerzungNachEinreichefrist;
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
  public DemoDataTestBerechnungDetailsDto fehlbetrag(Integer fehlbetrag) {
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

  /**
   **/
  public DemoDataTestBerechnungDetailsDto proKopfteilung(Integer proKopfteilung) {
    this.proKopfteilung = proKopfteilung;
    return this;
  }

  
  @JsonProperty("proKopfteilung")
  public Integer getProKopfteilung() {
    return proKopfteilung;
  }

  @JsonProperty("proKopfteilung")
  public void setProKopfteilung(Integer proKopfteilung) {
    this.proKopfteilung = proKopfteilung;
  }

  /**
   **/
  public DemoDataTestBerechnungDetailsDto anzahlMonateEinreichefrist(Integer anzahlMonateEinreichefrist) {
    this.anzahlMonateEinreichefrist = anzahlMonateEinreichefrist;
    return this;
  }

  
  @JsonProperty("anzahlMonateEinreichefrist")
  public Integer getAnzahlMonateEinreichefrist() {
    return anzahlMonateEinreichefrist;
  }

  @JsonProperty("anzahlMonateEinreichefrist")
  public void setAnzahlMonateEinreichefrist(Integer anzahlMonateEinreichefrist) {
    this.anzahlMonateEinreichefrist = anzahlMonateEinreichefrist;
  }

  /**
   **/
  public DemoDataTestBerechnungDetailsDto totalNachKuerzungNachEinreichefrist(Integer totalNachKuerzungNachEinreichefrist) {
    this.totalNachKuerzungNachEinreichefrist = totalNachKuerzungNachEinreichefrist;
    return this;
  }

  
  @JsonProperty("totalNachKuerzungNachEinreichefrist")
  public Integer getTotalNachKuerzungNachEinreichefrist() {
    return totalNachKuerzungNachEinreichefrist;
  }

  @JsonProperty("totalNachKuerzungNachEinreichefrist")
  public void setTotalNachKuerzungNachEinreichefrist(Integer totalNachKuerzungNachEinreichefrist) {
    this.totalNachKuerzungNachEinreichefrist = totalNachKuerzungNachEinreichefrist;
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
        Objects.equals(this.fehlbetrag, demoDataTestBerechnungDetails.fehlbetrag) &&
        Objects.equals(this.proKopfteilung, demoDataTestBerechnungDetails.proKopfteilung) &&
        Objects.equals(this.anzahlMonateEinreichefrist, demoDataTestBerechnungDetails.anzahlMonateEinreichefrist) &&
        Objects.equals(this.totalNachKuerzungNachEinreichefrist, demoDataTestBerechnungDetails.totalNachKuerzungNachEinreichefrist);
  }

  @Override
  public int hashCode() {
    return Objects.hash(budgetArt, elternBudget1, elternBudget2, persoenlichesBudget, fehlbetrag, proKopfteilung, anzahlMonateEinreichefrist, totalNachKuerzungNachEinreichefrist);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoDataTestBerechnungDetailsDto {\n");
    
    sb.append("    budgetArt: ").append(toIndentedString(budgetArt)).append("\n");
    sb.append("    elternBudget1: ").append(toIndentedString(elternBudget1)).append("\n");
    sb.append("    elternBudget2: ").append(toIndentedString(elternBudget2)).append("\n");
    sb.append("    persoenlichesBudget: ").append(toIndentedString(persoenlichesBudget)).append("\n");
    sb.append("    fehlbetrag: ").append(toIndentedString(fehlbetrag)).append("\n");
    sb.append("    proKopfteilung: ").append(toIndentedString(proKopfteilung)).append("\n");
    sb.append("    anzahlMonateEinreichefrist: ").append(toIndentedString(anzahlMonateEinreichefrist)).append("\n");
    sb.append("    totalNachKuerzungNachEinreichefrist: ").append(toIndentedString(totalNachKuerzungNachEinreichefrist)).append("\n");
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
    private Integer fehlbetrag;
    private Integer proKopfteilung;
    private Integer anzahlMonateEinreichefrist;
    private Integer totalNachKuerzungNachEinreichefrist;
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
    public B fehlbetrag(Integer fehlbetrag) {
      this.fehlbetrag = fehlbetrag;
      return self();
    }
    public B proKopfteilung(Integer proKopfteilung) {
      this.proKopfteilung = proKopfteilung;
      return self();
    }
    public B anzahlMonateEinreichefrist(Integer anzahlMonateEinreichefrist) {
      this.anzahlMonateEinreichefrist = anzahlMonateEinreichefrist;
      return self();
    }
    public B totalNachKuerzungNachEinreichefrist(Integer totalNachKuerzungNachEinreichefrist) {
      this.totalNachKuerzungNachEinreichefrist = totalNachKuerzungNachEinreichefrist;
      return self();
    }
  }
}
