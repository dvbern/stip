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



@JsonTypeName("FamiliensituationUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class FamiliensituationUpdateDto  implements Serializable {
  private @Valid Boolean elternVerheiratetZusammen;
  private @Valid Boolean elternteilUnbekanntVerstorben;
  private @Valid Boolean gerichtlicheAlimentenregelung;
  private @Valid ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund mutterUnbekanntVerstorben;
  private @Valid ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund mutterUnbekanntGrund;
  private @Valid ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund vaterUnbekanntVerstorben;
  private @Valid ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund vaterUnbekanntGrund;
  private @Valid ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung werZahltAlimente;

  protected FamiliensituationUpdateDto(FamiliensituationUpdateDtoBuilder<?, ?> b) {
    this.elternVerheiratetZusammen = b.elternVerheiratetZusammen;
    this.elternteilUnbekanntVerstorben = b.elternteilUnbekanntVerstorben;
    this.gerichtlicheAlimentenregelung = b.gerichtlicheAlimentenregelung;
    this.mutterUnbekanntVerstorben = b.mutterUnbekanntVerstorben;
    this.mutterUnbekanntGrund = b.mutterUnbekanntGrund;
    this.vaterUnbekanntVerstorben = b.vaterUnbekanntVerstorben;
    this.vaterUnbekanntGrund = b.vaterUnbekanntGrund;
    this.werZahltAlimente = b.werZahltAlimente;
  }

  public FamiliensituationUpdateDto() {
  }

  /**
   **/
  public FamiliensituationUpdateDto elternVerheiratetZusammen(Boolean elternVerheiratetZusammen) {
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
  public FamiliensituationUpdateDto elternteilUnbekanntVerstorben(Boolean elternteilUnbekanntVerstorben) {
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
  public FamiliensituationUpdateDto gerichtlicheAlimentenregelung(Boolean gerichtlicheAlimentenregelung) {
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
  public FamiliensituationUpdateDto mutterUnbekanntVerstorben(ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund mutterUnbekanntVerstorben) {
    this.mutterUnbekanntVerstorben = mutterUnbekanntVerstorben;
    return this;
  }

  
  @JsonProperty("mutterUnbekanntVerstorben")
  public ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund getMutterUnbekanntVerstorben() {
    return mutterUnbekanntVerstorben;
  }

  @JsonProperty("mutterUnbekanntVerstorben")
  public void setMutterUnbekanntVerstorben(ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund mutterUnbekanntVerstorben) {
    this.mutterUnbekanntVerstorben = mutterUnbekanntVerstorben;
  }

  /**
   **/
  public FamiliensituationUpdateDto mutterUnbekanntGrund(ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund mutterUnbekanntGrund) {
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
  public FamiliensituationUpdateDto vaterUnbekanntVerstorben(ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund vaterUnbekanntVerstorben) {
    this.vaterUnbekanntVerstorben = vaterUnbekanntVerstorben;
    return this;
  }

  
  @JsonProperty("vaterUnbekanntVerstorben")
  public ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund getVaterUnbekanntVerstorben() {
    return vaterUnbekanntVerstorben;
  }

  @JsonProperty("vaterUnbekanntVerstorben")
  public void setVaterUnbekanntVerstorben(ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund vaterUnbekanntVerstorben) {
    this.vaterUnbekanntVerstorben = vaterUnbekanntVerstorben;
  }

  /**
   **/
  public FamiliensituationUpdateDto vaterUnbekanntGrund(ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund vaterUnbekanntGrund) {
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
  public FamiliensituationUpdateDto werZahltAlimente(ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung werZahltAlimente) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FamiliensituationUpdateDto familiensituationUpdate = (FamiliensituationUpdateDto) o;
    return Objects.equals(this.elternVerheiratetZusammen, familiensituationUpdate.elternVerheiratetZusammen) &&
        Objects.equals(this.elternteilUnbekanntVerstorben, familiensituationUpdate.elternteilUnbekanntVerstorben) &&
        Objects.equals(this.gerichtlicheAlimentenregelung, familiensituationUpdate.gerichtlicheAlimentenregelung) &&
        Objects.equals(this.mutterUnbekanntVerstorben, familiensituationUpdate.mutterUnbekanntVerstorben) &&
        Objects.equals(this.mutterUnbekanntGrund, familiensituationUpdate.mutterUnbekanntGrund) &&
        Objects.equals(this.vaterUnbekanntVerstorben, familiensituationUpdate.vaterUnbekanntVerstorben) &&
        Objects.equals(this.vaterUnbekanntGrund, familiensituationUpdate.vaterUnbekanntGrund) &&
        Objects.equals(this.werZahltAlimente, familiensituationUpdate.werZahltAlimente);
  }

  @Override
  public int hashCode() {
    return Objects.hash(elternVerheiratetZusammen, elternteilUnbekanntVerstorben, gerichtlicheAlimentenregelung, mutterUnbekanntVerstorben, mutterUnbekanntGrund, vaterUnbekanntVerstorben, vaterUnbekanntGrund, werZahltAlimente);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FamiliensituationUpdateDto {\n");
    
    sb.append("    elternVerheiratetZusammen: ").append(toIndentedString(elternVerheiratetZusammen)).append("\n");
    sb.append("    elternteilUnbekanntVerstorben: ").append(toIndentedString(elternteilUnbekanntVerstorben)).append("\n");
    sb.append("    gerichtlicheAlimentenregelung: ").append(toIndentedString(gerichtlicheAlimentenregelung)).append("\n");
    sb.append("    mutterUnbekanntVerstorben: ").append(toIndentedString(mutterUnbekanntVerstorben)).append("\n");
    sb.append("    mutterUnbekanntGrund: ").append(toIndentedString(mutterUnbekanntGrund)).append("\n");
    sb.append("    vaterUnbekanntVerstorben: ").append(toIndentedString(vaterUnbekanntVerstorben)).append("\n");
    sb.append("    vaterUnbekanntGrund: ").append(toIndentedString(vaterUnbekanntGrund)).append("\n");
    sb.append("    werZahltAlimente: ").append(toIndentedString(werZahltAlimente)).append("\n");
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


  public static FamiliensituationUpdateDtoBuilder<?, ?> builder() {
    return new FamiliensituationUpdateDtoBuilderImpl();
  }

  private static final class FamiliensituationUpdateDtoBuilderImpl extends FamiliensituationUpdateDtoBuilder<FamiliensituationUpdateDto, FamiliensituationUpdateDtoBuilderImpl> {

    @Override
    protected FamiliensituationUpdateDtoBuilderImpl self() {
      return this;
    }

    @Override
    public FamiliensituationUpdateDto build() {
      return new FamiliensituationUpdateDto(this);
    }
  }

  public static abstract class FamiliensituationUpdateDtoBuilder<C extends FamiliensituationUpdateDto, B extends FamiliensituationUpdateDtoBuilder<C, B>>  {
    private Boolean elternVerheiratetZusammen;
    private Boolean elternteilUnbekanntVerstorben;
    private Boolean gerichtlicheAlimentenregelung;
    private ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund mutterUnbekanntVerstorben;
    private ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund mutterUnbekanntGrund;
    private ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund vaterUnbekanntVerstorben;
    private ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund vaterUnbekanntGrund;
    private ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung werZahltAlimente;
    protected abstract B self();

    public abstract C build();

    public B elternVerheiratetZusammen(Boolean elternVerheiratetZusammen) {
      this.elternVerheiratetZusammen = elternVerheiratetZusammen;
      return self();
    }
    public B elternteilUnbekanntVerstorben(Boolean elternteilUnbekanntVerstorben) {
      this.elternteilUnbekanntVerstorben = elternteilUnbekanntVerstorben;
      return self();
    }
    public B gerichtlicheAlimentenregelung(Boolean gerichtlicheAlimentenregelung) {
      this.gerichtlicheAlimentenregelung = gerichtlicheAlimentenregelung;
      return self();
    }
    public B mutterUnbekanntVerstorben(ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund mutterUnbekanntVerstorben) {
      this.mutterUnbekanntVerstorben = mutterUnbekanntVerstorben;
      return self();
    }
    public B mutterUnbekanntGrund(ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund mutterUnbekanntGrund) {
      this.mutterUnbekanntGrund = mutterUnbekanntGrund;
      return self();
    }
    public B vaterUnbekanntVerstorben(ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund vaterUnbekanntVerstorben) {
      this.vaterUnbekanntVerstorben = vaterUnbekanntVerstorben;
      return self();
    }
    public B vaterUnbekanntGrund(ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund vaterUnbekanntGrund) {
      this.vaterUnbekanntGrund = vaterUnbekanntGrund;
      return self();
    }
    public B werZahltAlimente(ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung werZahltAlimente) {
      this.werZahltAlimente = werZahltAlimente;
      return self();
    }
  }
}

