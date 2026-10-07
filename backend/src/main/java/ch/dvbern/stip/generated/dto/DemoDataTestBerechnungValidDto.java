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



@JsonTypeName("DemoDataTestBerechnungValid")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class DemoDataTestBerechnungValidDto  implements Serializable {
  private Boolean status;
  private Boolean ungekuerztStipendien;
  private Boolean ungekuerztDarlehen;
  private Boolean stipendien;
  private Boolean darlehen;
  private Boolean budgetArt;
  private Boolean elternBudget1;
  private Boolean elternBudget2;
  private Boolean persoenlichesBudget;
  private Boolean fehlbetrag;
  private Boolean proKopfteilung;
  private Boolean anzahlMonateEinreichefrist;
  private Boolean totalNachKuerzungNachEinreichefrist;

  protected DemoDataTestBerechnungValidDto(DemoDataTestBerechnungValidDtoBuilder<?, ?> b) {
    this.status = b.status;
    this.ungekuerztStipendien = b.ungekuerztStipendien;
    this.ungekuerztDarlehen = b.ungekuerztDarlehen;
    this.stipendien = b.stipendien;
    this.darlehen = b.darlehen;
    this.budgetArt = b.budgetArt;
    this.elternBudget1 = b.elternBudget1;
    this.elternBudget2 = b.elternBudget2;
    this.persoenlichesBudget = b.persoenlichesBudget;
    this.fehlbetrag = b.fehlbetrag;
    this.proKopfteilung = b.proKopfteilung;
    this.anzahlMonateEinreichefrist = b.anzahlMonateEinreichefrist;
    this.totalNachKuerzungNachEinreichefrist = b.totalNachKuerzungNachEinreichefrist;
  }

  public DemoDataTestBerechnungValidDto() {
  }

  /**
   **/
  public DemoDataTestBerechnungValidDto status(Boolean status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  public Boolean getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(Boolean status) {
    this.status = status;
  }

  /**
   **/
  public DemoDataTestBerechnungValidDto ungekuerztStipendien(Boolean ungekuerztStipendien) {
    this.ungekuerztStipendien = ungekuerztStipendien;
    return this;
  }

  
  @JsonProperty("ungekuerztStipendien")
  public Boolean getUngekuerztStipendien() {
    return ungekuerztStipendien;
  }

  @JsonProperty("ungekuerztStipendien")
  public void setUngekuerztStipendien(Boolean ungekuerztStipendien) {
    this.ungekuerztStipendien = ungekuerztStipendien;
  }

  /**
   **/
  public DemoDataTestBerechnungValidDto ungekuerztDarlehen(Boolean ungekuerztDarlehen) {
    this.ungekuerztDarlehen = ungekuerztDarlehen;
    return this;
  }

  
  @JsonProperty("ungekuerztDarlehen")
  public Boolean getUngekuerztDarlehen() {
    return ungekuerztDarlehen;
  }

  @JsonProperty("ungekuerztDarlehen")
  public void setUngekuerztDarlehen(Boolean ungekuerztDarlehen) {
    this.ungekuerztDarlehen = ungekuerztDarlehen;
  }

  /**
   **/
  public DemoDataTestBerechnungValidDto stipendien(Boolean stipendien) {
    this.stipendien = stipendien;
    return this;
  }

  
  @JsonProperty("stipendien")
  public Boolean getStipendien() {
    return stipendien;
  }

  @JsonProperty("stipendien")
  public void setStipendien(Boolean stipendien) {
    this.stipendien = stipendien;
  }

  /**
   **/
  public DemoDataTestBerechnungValidDto darlehen(Boolean darlehen) {
    this.darlehen = darlehen;
    return this;
  }

  
  @JsonProperty("darlehen")
  public Boolean getDarlehen() {
    return darlehen;
  }

  @JsonProperty("darlehen")
  public void setDarlehen(Boolean darlehen) {
    this.darlehen = darlehen;
  }

  /**
   **/
  public DemoDataTestBerechnungValidDto budgetArt(Boolean budgetArt) {
    this.budgetArt = budgetArt;
    return this;
  }

  
  @JsonProperty("budgetArt")
  public Boolean getBudgetArt() {
    return budgetArt;
  }

  @JsonProperty("budgetArt")
  public void setBudgetArt(Boolean budgetArt) {
    this.budgetArt = budgetArt;
  }

  /**
   **/
  public DemoDataTestBerechnungValidDto elternBudget1(Boolean elternBudget1) {
    this.elternBudget1 = elternBudget1;
    return this;
  }

  
  @JsonProperty("elternBudget1")
  public Boolean getElternBudget1() {
    return elternBudget1;
  }

  @JsonProperty("elternBudget1")
  public void setElternBudget1(Boolean elternBudget1) {
    this.elternBudget1 = elternBudget1;
  }

  /**
   **/
  public DemoDataTestBerechnungValidDto elternBudget2(Boolean elternBudget2) {
    this.elternBudget2 = elternBudget2;
    return this;
  }

  
  @JsonProperty("elternBudget2")
  public Boolean getElternBudget2() {
    return elternBudget2;
  }

  @JsonProperty("elternBudget2")
  public void setElternBudget2(Boolean elternBudget2) {
    this.elternBudget2 = elternBudget2;
  }

  /**
   **/
  public DemoDataTestBerechnungValidDto persoenlichesBudget(Boolean persoenlichesBudget) {
    this.persoenlichesBudget = persoenlichesBudget;
    return this;
  }

  
  @JsonProperty("persoenlichesBudget")
  public Boolean getPersoenlichesBudget() {
    return persoenlichesBudget;
  }

  @JsonProperty("persoenlichesBudget")
  public void setPersoenlichesBudget(Boolean persoenlichesBudget) {
    this.persoenlichesBudget = persoenlichesBudget;
  }

  /**
   **/
  public DemoDataTestBerechnungValidDto fehlbetrag(Boolean fehlbetrag) {
    this.fehlbetrag = fehlbetrag;
    return this;
  }

  
  @JsonProperty("fehlbetrag")
  public Boolean getFehlbetrag() {
    return fehlbetrag;
  }

  @JsonProperty("fehlbetrag")
  public void setFehlbetrag(Boolean fehlbetrag) {
    this.fehlbetrag = fehlbetrag;
  }

  /**
   **/
  public DemoDataTestBerechnungValidDto proKopfteilung(Boolean proKopfteilung) {
    this.proKopfteilung = proKopfteilung;
    return this;
  }

  
  @JsonProperty("proKopfteilung")
  public Boolean getProKopfteilung() {
    return proKopfteilung;
  }

  @JsonProperty("proKopfteilung")
  public void setProKopfteilung(Boolean proKopfteilung) {
    this.proKopfteilung = proKopfteilung;
  }

  /**
   **/
  public DemoDataTestBerechnungValidDto anzahlMonateEinreichefrist(Boolean anzahlMonateEinreichefrist) {
    this.anzahlMonateEinreichefrist = anzahlMonateEinreichefrist;
    return this;
  }

  
  @JsonProperty("anzahlMonateEinreichefrist")
  public Boolean getAnzahlMonateEinreichefrist() {
    return anzahlMonateEinreichefrist;
  }

  @JsonProperty("anzahlMonateEinreichefrist")
  public void setAnzahlMonateEinreichefrist(Boolean anzahlMonateEinreichefrist) {
    this.anzahlMonateEinreichefrist = anzahlMonateEinreichefrist;
  }

  /**
   **/
  public DemoDataTestBerechnungValidDto totalNachKuerzungNachEinreichefrist(Boolean totalNachKuerzungNachEinreichefrist) {
    this.totalNachKuerzungNachEinreichefrist = totalNachKuerzungNachEinreichefrist;
    return this;
  }

  
  @JsonProperty("totalNachKuerzungNachEinreichefrist")
  public Boolean getTotalNachKuerzungNachEinreichefrist() {
    return totalNachKuerzungNachEinreichefrist;
  }

  @JsonProperty("totalNachKuerzungNachEinreichefrist")
  public void setTotalNachKuerzungNachEinreichefrist(Boolean totalNachKuerzungNachEinreichefrist) {
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
    DemoDataTestBerechnungValidDto demoDataTestBerechnungValid = (DemoDataTestBerechnungValidDto) o;
    return Objects.equals(this.status, demoDataTestBerechnungValid.status) &&
        Objects.equals(this.ungekuerztStipendien, demoDataTestBerechnungValid.ungekuerztStipendien) &&
        Objects.equals(this.ungekuerztDarlehen, demoDataTestBerechnungValid.ungekuerztDarlehen) &&
        Objects.equals(this.stipendien, demoDataTestBerechnungValid.stipendien) &&
        Objects.equals(this.darlehen, demoDataTestBerechnungValid.darlehen) &&
        Objects.equals(this.budgetArt, demoDataTestBerechnungValid.budgetArt) &&
        Objects.equals(this.elternBudget1, demoDataTestBerechnungValid.elternBudget1) &&
        Objects.equals(this.elternBudget2, demoDataTestBerechnungValid.elternBudget2) &&
        Objects.equals(this.persoenlichesBudget, demoDataTestBerechnungValid.persoenlichesBudget) &&
        Objects.equals(this.fehlbetrag, demoDataTestBerechnungValid.fehlbetrag) &&
        Objects.equals(this.proKopfteilung, demoDataTestBerechnungValid.proKopfteilung) &&
        Objects.equals(this.anzahlMonateEinreichefrist, demoDataTestBerechnungValid.anzahlMonateEinreichefrist) &&
        Objects.equals(this.totalNachKuerzungNachEinreichefrist, demoDataTestBerechnungValid.totalNachKuerzungNachEinreichefrist);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, ungekuerztStipendien, ungekuerztDarlehen, stipendien, darlehen, budgetArt, elternBudget1, elternBudget2, persoenlichesBudget, fehlbetrag, proKopfteilung, anzahlMonateEinreichefrist, totalNachKuerzungNachEinreichefrist);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoDataTestBerechnungValidDto {\n");
    
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    ungekuerztStipendien: ").append(toIndentedString(ungekuerztStipendien)).append("\n");
    sb.append("    ungekuerztDarlehen: ").append(toIndentedString(ungekuerztDarlehen)).append("\n");
    sb.append("    stipendien: ").append(toIndentedString(stipendien)).append("\n");
    sb.append("    darlehen: ").append(toIndentedString(darlehen)).append("\n");
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


  public static DemoDataTestBerechnungValidDtoBuilder<?, ?> builder() {
    return new DemoDataTestBerechnungValidDtoBuilderImpl();
  }

  private static final class DemoDataTestBerechnungValidDtoBuilderImpl extends DemoDataTestBerechnungValidDtoBuilder<DemoDataTestBerechnungValidDto, DemoDataTestBerechnungValidDtoBuilderImpl> {

    @Override
    protected DemoDataTestBerechnungValidDtoBuilderImpl self() {
      return this;
    }

    @Override
    public DemoDataTestBerechnungValidDto build() {
      return new DemoDataTestBerechnungValidDto(this);
    }
  }

  public static abstract class DemoDataTestBerechnungValidDtoBuilder<C extends DemoDataTestBerechnungValidDto, B extends DemoDataTestBerechnungValidDtoBuilder<C, B>>  {
    private Boolean status;
    private Boolean ungekuerztStipendien;
    private Boolean ungekuerztDarlehen;
    private Boolean stipendien;
    private Boolean darlehen;
    private Boolean budgetArt;
    private Boolean elternBudget1;
    private Boolean elternBudget2;
    private Boolean persoenlichesBudget;
    private Boolean fehlbetrag;
    private Boolean proKopfteilung;
    private Boolean anzahlMonateEinreichefrist;
    private Boolean totalNachKuerzungNachEinreichefrist;
    protected abstract B self();

    public abstract C build();

    public B status(Boolean status) {
      this.status = status;
      return self();
    }
    public B ungekuerztStipendien(Boolean ungekuerztStipendien) {
      this.ungekuerztStipendien = ungekuerztStipendien;
      return self();
    }
    public B ungekuerztDarlehen(Boolean ungekuerztDarlehen) {
      this.ungekuerztDarlehen = ungekuerztDarlehen;
      return self();
    }
    public B stipendien(Boolean stipendien) {
      this.stipendien = stipendien;
      return self();
    }
    public B darlehen(Boolean darlehen) {
      this.darlehen = darlehen;
      return self();
    }
    public B budgetArt(Boolean budgetArt) {
      this.budgetArt = budgetArt;
      return self();
    }
    public B elternBudget1(Boolean elternBudget1) {
      this.elternBudget1 = elternBudget1;
      return self();
    }
    public B elternBudget2(Boolean elternBudget2) {
      this.elternBudget2 = elternBudget2;
      return self();
    }
    public B persoenlichesBudget(Boolean persoenlichesBudget) {
      this.persoenlichesBudget = persoenlichesBudget;
      return self();
    }
    public B fehlbetrag(Boolean fehlbetrag) {
      this.fehlbetrag = fehlbetrag;
      return self();
    }
    public B proKopfteilung(Boolean proKopfteilung) {
      this.proKopfteilung = proKopfteilung;
      return self();
    }
    public B anzahlMonateEinreichefrist(Boolean anzahlMonateEinreichefrist) {
      this.anzahlMonateEinreichefrist = anzahlMonateEinreichefrist;
      return self();
    }
    public B totalNachKuerzungNachEinreichefrist(Boolean totalNachKuerzungNachEinreichefrist) {
      this.totalNachKuerzungNachEinreichefrist = totalNachKuerzungNachEinreichefrist;
      return self();
    }
  }
}
