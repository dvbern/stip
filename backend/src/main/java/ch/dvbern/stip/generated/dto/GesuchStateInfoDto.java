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



@JsonTypeName("GesuchStateInfo")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchStateInfoDto  implements Serializable {
  private @Valid ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus;
  private @Valid Boolean beschwerdeHaengig;
  private @Valid Boolean canGetBerechnung;
  private @Valid Boolean canChangeGesuchsperiode;
  private @Valid Boolean canTriggerManuellPruefen;
  private @Valid Boolean canBearbeitungAbschliessen;

  /**
   **/
  public GesuchStateInfoDto gesuchStatus(ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus) {
    this.gesuchStatus = gesuchStatus;
    return this;
  }

  
  @JsonProperty("gesuchStatus")
  @NotNull
  public ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus getGesuchStatus() {
    return gesuchStatus;
  }

  @JsonProperty("gesuchStatus")
  public void setGesuchStatus(ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus) {
    this.gesuchStatus = gesuchStatus;
  }

  /**
   **/
  public GesuchStateInfoDto beschwerdeHaengig(Boolean beschwerdeHaengig) {
    this.beschwerdeHaengig = beschwerdeHaengig;
    return this;
  }

  
  @JsonProperty("beschwerdeHaengig")
  @NotNull
  public Boolean getBeschwerdeHaengig() {
    return beschwerdeHaengig;
  }

  @JsonProperty("beschwerdeHaengig")
  public void setBeschwerdeHaengig(Boolean beschwerdeHaengig) {
    this.beschwerdeHaengig = beschwerdeHaengig;
  }

  /**
   **/
  public GesuchStateInfoDto canGetBerechnung(Boolean canGetBerechnung) {
    this.canGetBerechnung = canGetBerechnung;
    return this;
  }

  
  @JsonProperty("canGetBerechnung")
  @NotNull
  public Boolean getCanGetBerechnung() {
    return canGetBerechnung;
  }

  @JsonProperty("canGetBerechnung")
  public void setCanGetBerechnung(Boolean canGetBerechnung) {
    this.canGetBerechnung = canGetBerechnung;
  }

  /**
   **/
  public GesuchStateInfoDto canChangeGesuchsperiode(Boolean canChangeGesuchsperiode) {
    this.canChangeGesuchsperiode = canChangeGesuchsperiode;
    return this;
  }

  
  @JsonProperty("canChangeGesuchsperiode")
  @NotNull
  public Boolean getCanChangeGesuchsperiode() {
    return canChangeGesuchsperiode;
  }

  @JsonProperty("canChangeGesuchsperiode")
  public void setCanChangeGesuchsperiode(Boolean canChangeGesuchsperiode) {
    this.canChangeGesuchsperiode = canChangeGesuchsperiode;
  }

  /**
   **/
  public GesuchStateInfoDto canTriggerManuellPruefen(Boolean canTriggerManuellPruefen) {
    this.canTriggerManuellPruefen = canTriggerManuellPruefen;
    return this;
  }

  
  @JsonProperty("canTriggerManuellPruefen")
  @NotNull
  public Boolean getCanTriggerManuellPruefen() {
    return canTriggerManuellPruefen;
  }

  @JsonProperty("canTriggerManuellPruefen")
  public void setCanTriggerManuellPruefen(Boolean canTriggerManuellPruefen) {
    this.canTriggerManuellPruefen = canTriggerManuellPruefen;
  }

  /**
   **/
  public GesuchStateInfoDto canBearbeitungAbschliessen(Boolean canBearbeitungAbschliessen) {
    this.canBearbeitungAbschliessen = canBearbeitungAbschliessen;
    return this;
  }

  
  @JsonProperty("canBearbeitungAbschliessen")
  @NotNull
  public Boolean getCanBearbeitungAbschliessen() {
    return canBearbeitungAbschliessen;
  }

  @JsonProperty("canBearbeitungAbschliessen")
  public void setCanBearbeitungAbschliessen(Boolean canBearbeitungAbschliessen) {
    this.canBearbeitungAbschliessen = canBearbeitungAbschliessen;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchStateInfoDto gesuchStateInfo = (GesuchStateInfoDto) o;
    return Objects.equals(this.gesuchStatus, gesuchStateInfo.gesuchStatus) &&
        Objects.equals(this.beschwerdeHaengig, gesuchStateInfo.beschwerdeHaengig) &&
        Objects.equals(this.canGetBerechnung, gesuchStateInfo.canGetBerechnung) &&
        Objects.equals(this.canChangeGesuchsperiode, gesuchStateInfo.canChangeGesuchsperiode) &&
        Objects.equals(this.canTriggerManuellPruefen, gesuchStateInfo.canTriggerManuellPruefen) &&
        Objects.equals(this.canBearbeitungAbschliessen, gesuchStateInfo.canBearbeitungAbschliessen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gesuchStatus, beschwerdeHaengig, canGetBerechnung, canChangeGesuchsperiode, canTriggerManuellPruefen, canBearbeitungAbschliessen);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchStateInfoDto {\n");
    
    sb.append("    gesuchStatus: ").append(toIndentedString(gesuchStatus)).append("\n");
    sb.append("    beschwerdeHaengig: ").append(toIndentedString(beschwerdeHaengig)).append("\n");
    sb.append("    canGetBerechnung: ").append(toIndentedString(canGetBerechnung)).append("\n");
    sb.append("    canChangeGesuchsperiode: ").append(toIndentedString(canChangeGesuchsperiode)).append("\n");
    sb.append("    canTriggerManuellPruefen: ").append(toIndentedString(canTriggerManuellPruefen)).append("\n");
    sb.append("    canBearbeitungAbschliessen: ").append(toIndentedString(canBearbeitungAbschliessen)).append("\n");
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

