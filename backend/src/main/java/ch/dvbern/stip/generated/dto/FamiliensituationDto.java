package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;



@JsonTypeName("Familiensituation")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class FamiliensituationDto  implements Serializable {
  private @Valid Boolean elternVerheiratetZusammen;
  private @Valid Boolean elternteilUnbekanntVerstorben;
  private @Valid Boolean gerichtlicheAlimentenregelung;
  private @Valid ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund mutterUnbekanntVerstorben;
  private @Valid ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund mutterUnbekanntGrund;
  private @Valid Boolean mutterWiederverheiratet;
  private @Valid ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund vaterUnbekanntVerstorben;
  private @Valid ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund vaterUnbekanntGrund;
  private @Valid Boolean vaterWiederverheiratet;
  private @Valid ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung sorgerecht;
  private @Valid ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung obhut;
  private @Valid BigDecimal obhutMutter;
  private @Valid BigDecimal obhutVater;
  private @Valid ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung werZahltAlimente;

  /**
   **/
  public FamiliensituationDto elternVerheiratetZusammen(Boolean elternVerheiratetZusammen) {
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
  public FamiliensituationDto elternteilUnbekanntVerstorben(Boolean elternteilUnbekanntVerstorben) {
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
  public FamiliensituationDto gerichtlicheAlimentenregelung(Boolean gerichtlicheAlimentenregelung) {
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
  public FamiliensituationDto mutterUnbekanntVerstorben(ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund mutterUnbekanntVerstorben) {
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
  public FamiliensituationDto mutterUnbekanntGrund(ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund mutterUnbekanntGrund) {
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
  public FamiliensituationDto mutterWiederverheiratet(Boolean mutterWiederverheiratet) {
    this.mutterWiederverheiratet = mutterWiederverheiratet;
    return this;
  }


  @JsonProperty("mutterWiederverheiratet")
  public Boolean getMutterWiederverheiratet() {
    return mutterWiederverheiratet;
  }

  @JsonProperty("mutterWiederverheiratet")
  public void setMutterWiederverheiratet(Boolean mutterWiederverheiratet) {
    this.mutterWiederverheiratet = mutterWiederverheiratet;
  }

  /**
   **/
  public FamiliensituationDto vaterUnbekanntVerstorben(ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund vaterUnbekanntVerstorben) {
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
  public FamiliensituationDto vaterUnbekanntGrund(ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund vaterUnbekanntGrund) {
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
  public FamiliensituationDto vaterWiederverheiratet(Boolean vaterWiederverheiratet) {
    this.vaterWiederverheiratet = vaterWiederverheiratet;
    return this;
  }


  @JsonProperty("vaterWiederverheiratet")
  public Boolean getVaterWiederverheiratet() {
    return vaterWiederverheiratet;
  }

  @JsonProperty("vaterWiederverheiratet")
  public void setVaterWiederverheiratet(Boolean vaterWiederverheiratet) {
    this.vaterWiederverheiratet = vaterWiederverheiratet;
  }

  /**
   **/
  public FamiliensituationDto sorgerecht(ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung sorgerecht) {
    this.sorgerecht = sorgerecht;
    return this;
  }


  @JsonProperty("sorgerecht")
  public ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung getSorgerecht() {
    return sorgerecht;
  }

  @JsonProperty("sorgerecht")
  public void setSorgerecht(ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung sorgerecht) {
    this.sorgerecht = sorgerecht;
  }

  /**
   **/
  public FamiliensituationDto obhut(ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung obhut) {
    this.obhut = obhut;
    return this;
  }


  @JsonProperty("obhut")
  public ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung getObhut() {
    return obhut;
  }

  @JsonProperty("obhut")
  public void setObhut(ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung obhut) {
    this.obhut = obhut;
  }

  /**
   * Required nur wenn obhut &#x3D; GEMEINSAM
   **/
  public FamiliensituationDto obhutMutter(BigDecimal obhutMutter) {
    this.obhutMutter = obhutMutter;
    return this;
  }


  @JsonProperty("obhutMutter")
  public BigDecimal getObhutMutter() {
    return obhutMutter;
  }

  @JsonProperty("obhutMutter")
  public void setObhutMutter(BigDecimal obhutMutter) {
    this.obhutMutter = obhutMutter;
  }

  /**
   * Required nur wenn obhut &#x3D; GEMEINSAM
   **/
  public FamiliensituationDto obhutVater(BigDecimal obhutVater) {
    this.obhutVater = obhutVater;
    return this;
  }


  @JsonProperty("obhutVater")
  public BigDecimal getObhutVater() {
    return obhutVater;
  }

  @JsonProperty("obhutVater")
  public void setObhutVater(BigDecimal obhutVater) {
    this.obhutVater = obhutVater;
  }

  /**
   **/
  public FamiliensituationDto werZahltAlimente(ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung werZahltAlimente) {
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
    FamiliensituationDto familiensituation = (FamiliensituationDto) o;
    return Objects.equals(this.elternVerheiratetZusammen, familiensituation.elternVerheiratetZusammen) &&
        Objects.equals(this.elternteilUnbekanntVerstorben, familiensituation.elternteilUnbekanntVerstorben) &&
        Objects.equals(this.gerichtlicheAlimentenregelung, familiensituation.gerichtlicheAlimentenregelung) &&
        Objects.equals(this.mutterUnbekanntVerstorben, familiensituation.mutterUnbekanntVerstorben) &&
        Objects.equals(this.mutterUnbekanntGrund, familiensituation.mutterUnbekanntGrund) &&
        Objects.equals(this.mutterWiederverheiratet, familiensituation.mutterWiederverheiratet) &&
        Objects.equals(this.vaterUnbekanntVerstorben, familiensituation.vaterUnbekanntVerstorben) &&
        Objects.equals(this.vaterUnbekanntGrund, familiensituation.vaterUnbekanntGrund) &&
        Objects.equals(this.vaterWiederverheiratet, familiensituation.vaterWiederverheiratet) &&
        Objects.equals(this.sorgerecht, familiensituation.sorgerecht) &&
        Objects.equals(this.obhut, familiensituation.obhut) &&
        Objects.equals(this.obhutMutter, familiensituation.obhutMutter) &&
        Objects.equals(this.obhutVater, familiensituation.obhutVater) &&
        Objects.equals(this.werZahltAlimente, familiensituation.werZahltAlimente);
  }

  @Override
  public int hashCode() {
    return Objects.hash(elternVerheiratetZusammen, elternteilUnbekanntVerstorben, gerichtlicheAlimentenregelung, mutterUnbekanntVerstorben, mutterUnbekanntGrund, mutterWiederverheiratet, vaterUnbekanntVerstorben, vaterUnbekanntGrund, vaterWiederverheiratet, sorgerecht, obhut, obhutMutter, obhutVater, werZahltAlimente);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FamiliensituationDto {\n");

    sb.append("    elternVerheiratetZusammen: ").append(toIndentedString(elternVerheiratetZusammen)).append("\n");
    sb.append("    elternteilUnbekanntVerstorben: ").append(toIndentedString(elternteilUnbekanntVerstorben)).append("\n");
    sb.append("    gerichtlicheAlimentenregelung: ").append(toIndentedString(gerichtlicheAlimentenregelung)).append("\n");
    sb.append("    mutterUnbekanntVerstorben: ").append(toIndentedString(mutterUnbekanntVerstorben)).append("\n");
    sb.append("    mutterUnbekanntGrund: ").append(toIndentedString(mutterUnbekanntGrund)).append("\n");
    sb.append("    mutterWiederverheiratet: ").append(toIndentedString(mutterWiederverheiratet)).append("\n");
    sb.append("    vaterUnbekanntVerstorben: ").append(toIndentedString(vaterUnbekanntVerstorben)).append("\n");
    sb.append("    vaterUnbekanntGrund: ").append(toIndentedString(vaterUnbekanntGrund)).append("\n");
    sb.append("    vaterWiederverheiratet: ").append(toIndentedString(vaterWiederverheiratet)).append("\n");
    sb.append("    sorgerecht: ").append(toIndentedString(sorgerecht)).append("\n");
    sb.append("    obhut: ").append(toIndentedString(obhut)).append("\n");
    sb.append("    obhutMutter: ").append(toIndentedString(obhutMutter)).append("\n");
    sb.append("    obhutVater: ").append(toIndentedString(obhutVater)).append("\n");
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


}

