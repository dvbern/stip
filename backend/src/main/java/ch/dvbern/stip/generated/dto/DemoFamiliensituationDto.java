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
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DemoFamiliensituationDto  implements Serializable {
  private @Valid Boolean elternVerheiratetZusammen;
  private @Valid Boolean gerichtlicheAlimentenregelung;
  private @Valid ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung werZahltAlimente;
  private @Valid Boolean mutterWiederverheiratetAlimente;
  private @Valid Boolean vaterWiederverheiratetAlimente;
  private @Valid Boolean elternteilUnbekanntVerstorben;
  private @Valid Boolean mutterVerstorben;
  private @Valid Boolean mutterUnbekannt;
  private @Valid ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund mutterUnbekanntGrund;
  private @Valid Boolean mutterKeineOptionen;
  private @Valid Boolean mutterWiederverheiratetUnbekannt;
  private @Valid Boolean vaterVerstorben;
  private @Valid Boolean vaterUnbekannt;
  private @Valid ch.dvbern.stip.api.familiensituation.type.ElternUnbekanntheitsGrund vaterUnbekanntGrund;
  private @Valid Boolean vaterKeineOptionen;
  private @Valid Boolean vaterWiederverheiratetUnbekannt;
  private @Valid Boolean mutterWiederverheiratetUngewiss;
  private @Valid Boolean vaterWiederverheiratetUngewiss;

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
  public DemoFamiliensituationDto mutterWiederverheiratetAlimente(Boolean mutterWiederverheiratetAlimente) {
    this.mutterWiederverheiratetAlimente = mutterWiederverheiratetAlimente;
    return this;
  }

  
  @JsonProperty("mutterWiederverheiratetAlimente")
  public Boolean getMutterWiederverheiratetAlimente() {
    return mutterWiederverheiratetAlimente;
  }

  @JsonProperty("mutterWiederverheiratetAlimente")
  public void setMutterWiederverheiratetAlimente(Boolean mutterWiederverheiratetAlimente) {
    this.mutterWiederverheiratetAlimente = mutterWiederverheiratetAlimente;
  }

  /**
   **/
  public DemoFamiliensituationDto vaterWiederverheiratetAlimente(Boolean vaterWiederverheiratetAlimente) {
    this.vaterWiederverheiratetAlimente = vaterWiederverheiratetAlimente;
    return this;
  }

  
  @JsonProperty("vaterWiederverheiratetAlimente")
  public Boolean getVaterWiederverheiratetAlimente() {
    return vaterWiederverheiratetAlimente;
  }

  @JsonProperty("vaterWiederverheiratetAlimente")
  public void setVaterWiederverheiratetAlimente(Boolean vaterWiederverheiratetAlimente) {
    this.vaterWiederverheiratetAlimente = vaterWiederverheiratetAlimente;
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
  public DemoFamiliensituationDto mutterWiederverheiratetUnbekannt(Boolean mutterWiederverheiratetUnbekannt) {
    this.mutterWiederverheiratetUnbekannt = mutterWiederverheiratetUnbekannt;
    return this;
  }

  
  @JsonProperty("mutterWiederverheiratetUnbekannt")
  public Boolean getMutterWiederverheiratetUnbekannt() {
    return mutterWiederverheiratetUnbekannt;
  }

  @JsonProperty("mutterWiederverheiratetUnbekannt")
  public void setMutterWiederverheiratetUnbekannt(Boolean mutterWiederverheiratetUnbekannt) {
    this.mutterWiederverheiratetUnbekannt = mutterWiederverheiratetUnbekannt;
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

  /**
   **/
  public DemoFamiliensituationDto vaterWiederverheiratetUnbekannt(Boolean vaterWiederverheiratetUnbekannt) {
    this.vaterWiederverheiratetUnbekannt = vaterWiederverheiratetUnbekannt;
    return this;
  }

  
  @JsonProperty("vaterWiederverheiratetUnbekannt")
  public Boolean getVaterWiederverheiratetUnbekannt() {
    return vaterWiederverheiratetUnbekannt;
  }

  @JsonProperty("vaterWiederverheiratetUnbekannt")
  public void setVaterWiederverheiratetUnbekannt(Boolean vaterWiederverheiratetUnbekannt) {
    this.vaterWiederverheiratetUnbekannt = vaterWiederverheiratetUnbekannt;
  }

  /**
   **/
  public DemoFamiliensituationDto mutterWiederverheiratetUngewiss(Boolean mutterWiederverheiratetUngewiss) {
    this.mutterWiederverheiratetUngewiss = mutterWiederverheiratetUngewiss;
    return this;
  }

  
  @JsonProperty("mutterWiederverheiratetUngewiss")
  public Boolean getMutterWiederverheiratetUngewiss() {
    return mutterWiederverheiratetUngewiss;
  }

  @JsonProperty("mutterWiederverheiratetUngewiss")
  public void setMutterWiederverheiratetUngewiss(Boolean mutterWiederverheiratetUngewiss) {
    this.mutterWiederverheiratetUngewiss = mutterWiederverheiratetUngewiss;
  }

  /**
   **/
  public DemoFamiliensituationDto vaterWiederverheiratetUngewiss(Boolean vaterWiederverheiratetUngewiss) {
    this.vaterWiederverheiratetUngewiss = vaterWiederverheiratetUngewiss;
    return this;
  }

  
  @JsonProperty("vaterWiederverheiratetUngewiss")
  public Boolean getVaterWiederverheiratetUngewiss() {
    return vaterWiederverheiratetUngewiss;
  }

  @JsonProperty("vaterWiederverheiratetUngewiss")
  public void setVaterWiederverheiratetUngewiss(Boolean vaterWiederverheiratetUngewiss) {
    this.vaterWiederverheiratetUngewiss = vaterWiederverheiratetUngewiss;
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
        Objects.equals(this.mutterWiederverheiratetAlimente, demoFamiliensituation.mutterWiederverheiratetAlimente) &&
        Objects.equals(this.vaterWiederverheiratetAlimente, demoFamiliensituation.vaterWiederverheiratetAlimente) &&
        Objects.equals(this.elternteilUnbekanntVerstorben, demoFamiliensituation.elternteilUnbekanntVerstorben) &&
        Objects.equals(this.mutterVerstorben, demoFamiliensituation.mutterVerstorben) &&
        Objects.equals(this.mutterUnbekannt, demoFamiliensituation.mutterUnbekannt) &&
        Objects.equals(this.mutterUnbekanntGrund, demoFamiliensituation.mutterUnbekanntGrund) &&
        Objects.equals(this.mutterKeineOptionen, demoFamiliensituation.mutterKeineOptionen) &&
        Objects.equals(this.mutterWiederverheiratetUnbekannt, demoFamiliensituation.mutterWiederverheiratetUnbekannt) &&
        Objects.equals(this.vaterVerstorben, demoFamiliensituation.vaterVerstorben) &&
        Objects.equals(this.vaterUnbekannt, demoFamiliensituation.vaterUnbekannt) &&
        Objects.equals(this.vaterUnbekanntGrund, demoFamiliensituation.vaterUnbekanntGrund) &&
        Objects.equals(this.vaterKeineOptionen, demoFamiliensituation.vaterKeineOptionen) &&
        Objects.equals(this.vaterWiederverheiratetUnbekannt, demoFamiliensituation.vaterWiederverheiratetUnbekannt) &&
        Objects.equals(this.mutterWiederverheiratetUngewiss, demoFamiliensituation.mutterWiederverheiratetUngewiss) &&
        Objects.equals(this.vaterWiederverheiratetUngewiss, demoFamiliensituation.vaterWiederverheiratetUngewiss);
  }

  @Override
  public int hashCode() {
    return Objects.hash(elternVerheiratetZusammen, gerichtlicheAlimentenregelung, werZahltAlimente, mutterWiederverheiratetAlimente, vaterWiederverheiratetAlimente, elternteilUnbekanntVerstorben, mutterVerstorben, mutterUnbekannt, mutterUnbekanntGrund, mutterKeineOptionen, mutterWiederverheiratetUnbekannt, vaterVerstorben, vaterUnbekannt, vaterUnbekanntGrund, vaterKeineOptionen, vaterWiederverheiratetUnbekannt, mutterWiederverheiratetUngewiss, vaterWiederverheiratetUngewiss);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoFamiliensituationDto {\n");
    
    sb.append("    elternVerheiratetZusammen: ").append(toIndentedString(elternVerheiratetZusammen)).append("\n");
    sb.append("    gerichtlicheAlimentenregelung: ").append(toIndentedString(gerichtlicheAlimentenregelung)).append("\n");
    sb.append("    werZahltAlimente: ").append(toIndentedString(werZahltAlimente)).append("\n");
    sb.append("    mutterWiederverheiratetAlimente: ").append(toIndentedString(mutterWiederverheiratetAlimente)).append("\n");
    sb.append("    vaterWiederverheiratetAlimente: ").append(toIndentedString(vaterWiederverheiratetAlimente)).append("\n");
    sb.append("    elternteilUnbekanntVerstorben: ").append(toIndentedString(elternteilUnbekanntVerstorben)).append("\n");
    sb.append("    mutterVerstorben: ").append(toIndentedString(mutterVerstorben)).append("\n");
    sb.append("    mutterUnbekannt: ").append(toIndentedString(mutterUnbekannt)).append("\n");
    sb.append("    mutterUnbekanntGrund: ").append(toIndentedString(mutterUnbekanntGrund)).append("\n");
    sb.append("    mutterKeineOptionen: ").append(toIndentedString(mutterKeineOptionen)).append("\n");
    sb.append("    mutterWiederverheiratetUnbekannt: ").append(toIndentedString(mutterWiederverheiratetUnbekannt)).append("\n");
    sb.append("    vaterVerstorben: ").append(toIndentedString(vaterVerstorben)).append("\n");
    sb.append("    vaterUnbekannt: ").append(toIndentedString(vaterUnbekannt)).append("\n");
    sb.append("    vaterUnbekanntGrund: ").append(toIndentedString(vaterUnbekanntGrund)).append("\n");
    sb.append("    vaterKeineOptionen: ").append(toIndentedString(vaterKeineOptionen)).append("\n");
    sb.append("    vaterWiederverheiratetUnbekannt: ").append(toIndentedString(vaterWiederverheiratetUnbekannt)).append("\n");
    sb.append("    mutterWiederverheiratetUngewiss: ").append(toIndentedString(mutterWiederverheiratetUngewiss)).append("\n");
    sb.append("    vaterWiederverheiratetUngewiss: ").append(toIndentedString(vaterWiederverheiratetUngewiss)).append("\n");
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

