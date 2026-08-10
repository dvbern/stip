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



@JsonTypeName("DemoFamiliensituation")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class DemoFamiliensituationDto  implements Serializable {
  private @Valid Boolean elternVerheiratetZusammen;
  private @Valid Boolean gerichtlicheAlimentenregelung;
  private @Valid ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung werZahltAlimente;
  private @Valid Boolean elternteilUnbekanntVerstorben;
  private @Valid Boolean mutterVerstorben;
  private @Valid Boolean mutterUnbekannt;
  private @Valid ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund mutterUnbekanntGrund;
  private @Valid Boolean mutterKeineOptionen;
  private @Valid Boolean vaterVerstorben;
  private @Valid Boolean vaterUnbekannt;
  private @Valid ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund vaterUnbekanntGrund;
  private @Valid Boolean vaterKeineOptionen;

  protected DemoFamiliensituationDto(DemoFamiliensituationDtoBuilder<?, ?> b) {
    this.elternVerheiratetZusammen = b.elternVerheiratetZusammen;
    this.gerichtlicheAlimentenregelung = b.gerichtlicheAlimentenregelung;
    this.werZahltAlimente = b.werZahltAlimente;
    this.elternteilUnbekanntVerstorben = b.elternteilUnbekanntVerstorben;
    this.mutterVerstorben = b.mutterVerstorben;
    this.mutterUnbekannt = b.mutterUnbekannt;
    this.mutterUnbekanntGrund = b.mutterUnbekanntGrund;
    this.mutterKeineOptionen = b.mutterKeineOptionen;
    this.vaterVerstorben = b.vaterVerstorben;
    this.vaterUnbekannt = b.vaterUnbekannt;
    this.vaterUnbekanntGrund = b.vaterUnbekanntGrund;
    this.vaterKeineOptionen = b.vaterKeineOptionen;
  }

  public DemoFamiliensituationDto() {
  }

  /**
   **/
  public DemoFamiliensituationDto elternVerheiratetZusammen(Boolean elternVerheiratetZusammen) {
    this.elternVerheiratetZusammen = elternVerheiratetZusammen;
    return this;
  }

  
  @JsonProperty("elternVerheiratetZusammen")
  @NotNull
  public Boolean getElternVerheiratetZusammen() {
    return elternVerheiratetZusammen;
  }

  @JsonProperty("elternVerheiratetZusammen")
  public void setElternVerheiratetZusammen(Boolean elternVerheiratetZusammen) {
    this.elternVerheiratetZusammen = elternVerheiratetZusammen;
  }

  /**
   **/
  public DemoFamiliensituationDto gerichtlicheAlimentenregelung(Boolean gerichtlicheAlimentenregelung) {
    this.gerichtlicheAlimentenregelung = gerichtlicheAlimentenregelung;
    return this;
  }

  
  @JsonProperty("gerichtlicheAlimentenregelung")
  public Boolean getGerichtlicheAlimentenregelung() {
    return gerichtlicheAlimentenregelung;
  }

  @JsonProperty("gerichtlicheAlimentenregelung")
  public void setGerichtlicheAlimentenregelung(Boolean gerichtlicheAlimentenregelung) {
    this.gerichtlicheAlimentenregelung = gerichtlicheAlimentenregelung;
  }

  /**
   **/
  public DemoFamiliensituationDto werZahltAlimente(ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung werZahltAlimente) {
    this.werZahltAlimente = werZahltAlimente;
    return this;
  }

  
  @JsonProperty("werZahltAlimente")
  public ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung getWerZahltAlimente() {
    return werZahltAlimente;
  }

  @JsonProperty("werZahltAlimente")
  public void setWerZahltAlimente(ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung werZahltAlimente) {
    this.werZahltAlimente = werZahltAlimente;
  }

  /**
   **/
  public DemoFamiliensituationDto elternteilUnbekanntVerstorben(Boolean elternteilUnbekanntVerstorben) {
    this.elternteilUnbekanntVerstorben = elternteilUnbekanntVerstorben;
    return this;
  }

  
  @JsonProperty("elternteilUnbekanntVerstorben")
  public Boolean getElternteilUnbekanntVerstorben() {
    return elternteilUnbekanntVerstorben;
  }

  @JsonProperty("elternteilUnbekanntVerstorben")
  public void setElternteilUnbekanntVerstorben(Boolean elternteilUnbekanntVerstorben) {
    this.elternteilUnbekanntVerstorben = elternteilUnbekanntVerstorben;
  }

  /**
   **/
  public DemoFamiliensituationDto mutterVerstorben(Boolean mutterVerstorben) {
    this.mutterVerstorben = mutterVerstorben;
    return this;
  }

  
  @JsonProperty("mutterVerstorben")
  public Boolean getMutterVerstorben() {
    return mutterVerstorben;
  }

  @JsonProperty("mutterVerstorben")
  public void setMutterVerstorben(Boolean mutterVerstorben) {
    this.mutterVerstorben = mutterVerstorben;
  }

  /**
   **/
  public DemoFamiliensituationDto mutterUnbekannt(Boolean mutterUnbekannt) {
    this.mutterUnbekannt = mutterUnbekannt;
    return this;
  }

  
  @JsonProperty("mutterUnbekannt")
  public Boolean getMutterUnbekannt() {
    return mutterUnbekannt;
  }

  @JsonProperty("mutterUnbekannt")
  public void setMutterUnbekannt(Boolean mutterUnbekannt) {
    this.mutterUnbekannt = mutterUnbekannt;
  }

  /**
   **/
  public DemoFamiliensituationDto mutterUnbekanntGrund(ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund mutterUnbekanntGrund) {
    this.mutterUnbekanntGrund = mutterUnbekanntGrund;
    return this;
  }

  
  @JsonProperty("mutterUnbekanntGrund")
  public ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund getMutterUnbekanntGrund() {
    return mutterUnbekanntGrund;
  }

  @JsonProperty("mutterUnbekanntGrund")
  public void setMutterUnbekanntGrund(ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund mutterUnbekanntGrund) {
    this.mutterUnbekanntGrund = mutterUnbekanntGrund;
  }

  /**
   **/
  public DemoFamiliensituationDto mutterKeineOptionen(Boolean mutterKeineOptionen) {
    this.mutterKeineOptionen = mutterKeineOptionen;
    return this;
  }

  
  @JsonProperty("mutterKeineOptionen")
  public Boolean getMutterKeineOptionen() {
    return mutterKeineOptionen;
  }

  @JsonProperty("mutterKeineOptionen")
  public void setMutterKeineOptionen(Boolean mutterKeineOptionen) {
    this.mutterKeineOptionen = mutterKeineOptionen;
  }

  /**
   **/
  public DemoFamiliensituationDto vaterVerstorben(Boolean vaterVerstorben) {
    this.vaterVerstorben = vaterVerstorben;
    return this;
  }

  
  @JsonProperty("vaterVerstorben")
  public Boolean getVaterVerstorben() {
    return vaterVerstorben;
  }

  @JsonProperty("vaterVerstorben")
  public void setVaterVerstorben(Boolean vaterVerstorben) {
    this.vaterVerstorben = vaterVerstorben;
  }

  /**
   **/
  public DemoFamiliensituationDto vaterUnbekannt(Boolean vaterUnbekannt) {
    this.vaterUnbekannt = vaterUnbekannt;
    return this;
  }

  
  @JsonProperty("vaterUnbekannt")
  public Boolean getVaterUnbekannt() {
    return vaterUnbekannt;
  }

  @JsonProperty("vaterUnbekannt")
  public void setVaterUnbekannt(Boolean vaterUnbekannt) {
    this.vaterUnbekannt = vaterUnbekannt;
  }

  /**
   **/
  public DemoFamiliensituationDto vaterUnbekanntGrund(ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund vaterUnbekanntGrund) {
    this.vaterUnbekanntGrund = vaterUnbekanntGrund;
    return this;
  }

  
  @JsonProperty("vaterUnbekanntGrund")
  public ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund getVaterUnbekanntGrund() {
    return vaterUnbekanntGrund;
  }

  @JsonProperty("vaterUnbekanntGrund")
  public void setVaterUnbekanntGrund(ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund vaterUnbekanntGrund) {
    this.vaterUnbekanntGrund = vaterUnbekanntGrund;
  }

  /**
   **/
  public DemoFamiliensituationDto vaterKeineOptionen(Boolean vaterKeineOptionen) {
    this.vaterKeineOptionen = vaterKeineOptionen;
    return this;
  }

  
  @JsonProperty("vaterKeineOptionen")
  public Boolean getVaterKeineOptionen() {
    return vaterKeineOptionen;
  }

  @JsonProperty("vaterKeineOptionen")
  public void setVaterKeineOptionen(Boolean vaterKeineOptionen) {
    this.vaterKeineOptionen = vaterKeineOptionen;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoFamiliensituationDto demoFamiliensituation = (DemoFamiliensituationDto) o;
    return Objects.equals(this.elternVerheiratetZusammen, demoFamiliensituation.elternVerheiratetZusammen) &&
        Objects.equals(this.gerichtlicheAlimentenregelung, demoFamiliensituation.gerichtlicheAlimentenregelung) &&
        Objects.equals(this.werZahltAlimente, demoFamiliensituation.werZahltAlimente) &&
        Objects.equals(this.elternteilUnbekanntVerstorben, demoFamiliensituation.elternteilUnbekanntVerstorben) &&
        Objects.equals(this.mutterVerstorben, demoFamiliensituation.mutterVerstorben) &&
        Objects.equals(this.mutterUnbekannt, demoFamiliensituation.mutterUnbekannt) &&
        Objects.equals(this.mutterUnbekanntGrund, demoFamiliensituation.mutterUnbekanntGrund) &&
        Objects.equals(this.mutterKeineOptionen, demoFamiliensituation.mutterKeineOptionen) &&
        Objects.equals(this.vaterVerstorben, demoFamiliensituation.vaterVerstorben) &&
        Objects.equals(this.vaterUnbekannt, demoFamiliensituation.vaterUnbekannt) &&
        Objects.equals(this.vaterUnbekanntGrund, demoFamiliensituation.vaterUnbekanntGrund) &&
        Objects.equals(this.vaterKeineOptionen, demoFamiliensituation.vaterKeineOptionen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(elternVerheiratetZusammen, gerichtlicheAlimentenregelung, werZahltAlimente, elternteilUnbekanntVerstorben, mutterVerstorben, mutterUnbekannt, mutterUnbekanntGrund, mutterKeineOptionen, vaterVerstorben, vaterUnbekannt, vaterUnbekanntGrund, vaterKeineOptionen);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoFamiliensituationDto {\n");
    
    sb.append("    elternVerheiratetZusammen: ").append(toIndentedString(elternVerheiratetZusammen)).append("\n");
    sb.append("    gerichtlicheAlimentenregelung: ").append(toIndentedString(gerichtlicheAlimentenregelung)).append("\n");
    sb.append("    werZahltAlimente: ").append(toIndentedString(werZahltAlimente)).append("\n");
    sb.append("    elternteilUnbekanntVerstorben: ").append(toIndentedString(elternteilUnbekanntVerstorben)).append("\n");
    sb.append("    mutterVerstorben: ").append(toIndentedString(mutterVerstorben)).append("\n");
    sb.append("    mutterUnbekannt: ").append(toIndentedString(mutterUnbekannt)).append("\n");
    sb.append("    mutterUnbekanntGrund: ").append(toIndentedString(mutterUnbekanntGrund)).append("\n");
    sb.append("    mutterKeineOptionen: ").append(toIndentedString(mutterKeineOptionen)).append("\n");
    sb.append("    vaterVerstorben: ").append(toIndentedString(vaterVerstorben)).append("\n");
    sb.append("    vaterUnbekannt: ").append(toIndentedString(vaterUnbekannt)).append("\n");
    sb.append("    vaterUnbekanntGrund: ").append(toIndentedString(vaterUnbekanntGrund)).append("\n");
    sb.append("    vaterKeineOptionen: ").append(toIndentedString(vaterKeineOptionen)).append("\n");
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


  public static DemoFamiliensituationDtoBuilder<?, ?> builder() {
    return new DemoFamiliensituationDtoBuilderImpl();
  }

  private static final class DemoFamiliensituationDtoBuilderImpl extends DemoFamiliensituationDtoBuilder<DemoFamiliensituationDto, DemoFamiliensituationDtoBuilderImpl> {

    @Override
    protected DemoFamiliensituationDtoBuilderImpl self() {
      return this;
    }

    @Override
    public DemoFamiliensituationDto build() {
      return new DemoFamiliensituationDto(this);
    }
  }

  public static abstract class DemoFamiliensituationDtoBuilder<C extends DemoFamiliensituationDto, B extends DemoFamiliensituationDtoBuilder<C, B>>  {
    private Boolean elternVerheiratetZusammen;
    private Boolean gerichtlicheAlimentenregelung;
    private ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung werZahltAlimente;
    private Boolean elternteilUnbekanntVerstorben;
    private Boolean mutterVerstorben;
    private Boolean mutterUnbekannt;
    private ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund mutterUnbekanntGrund;
    private Boolean mutterKeineOptionen;
    private Boolean vaterVerstorben;
    private Boolean vaterUnbekannt;
    private ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund vaterUnbekanntGrund;
    private Boolean vaterKeineOptionen;
    protected abstract B self();

    public abstract C build();

    public B elternVerheiratetZusammen(Boolean elternVerheiratetZusammen) {
      this.elternVerheiratetZusammen = elternVerheiratetZusammen;
      return self();
    }
    public B gerichtlicheAlimentenregelung(Boolean gerichtlicheAlimentenregelung) {
      this.gerichtlicheAlimentenregelung = gerichtlicheAlimentenregelung;
      return self();
    }
    public B werZahltAlimente(ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung werZahltAlimente) {
      this.werZahltAlimente = werZahltAlimente;
      return self();
    }
    public B elternteilUnbekanntVerstorben(Boolean elternteilUnbekanntVerstorben) {
      this.elternteilUnbekanntVerstorben = elternteilUnbekanntVerstorben;
      return self();
    }
    public B mutterVerstorben(Boolean mutterVerstorben) {
      this.mutterVerstorben = mutterVerstorben;
      return self();
    }
    public B mutterUnbekannt(Boolean mutterUnbekannt) {
      this.mutterUnbekannt = mutterUnbekannt;
      return self();
    }
    public B mutterUnbekanntGrund(ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund mutterUnbekanntGrund) {
      this.mutterUnbekanntGrund = mutterUnbekanntGrund;
      return self();
    }
    public B mutterKeineOptionen(Boolean mutterKeineOptionen) {
      this.mutterKeineOptionen = mutterKeineOptionen;
      return self();
    }
    public B vaterVerstorben(Boolean vaterVerstorben) {
      this.vaterVerstorben = vaterVerstorben;
      return self();
    }
    public B vaterUnbekannt(Boolean vaterUnbekannt) {
      this.vaterUnbekannt = vaterUnbekannt;
      return self();
    }
    public B vaterUnbekanntGrund(ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund vaterUnbekanntGrund) {
      this.vaterUnbekanntGrund = vaterUnbekanntGrund;
      return self();
    }
    public B vaterKeineOptionen(Boolean vaterKeineOptionen) {
      this.vaterKeineOptionen = vaterKeineOptionen;
      return self();
    }
  }
}

