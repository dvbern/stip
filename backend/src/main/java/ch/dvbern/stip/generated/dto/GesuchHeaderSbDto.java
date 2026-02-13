package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.GesuchStateInfoDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
import ch.dvbern.stip.generated.dto.HistorizedTranchenDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("GesuchHeaderSb")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchHeaderSbDto  implements Serializable {
  private @Valid HistorizedTranchenDto historized;
  private @Valid List<GesuchTrancheSlimDto> currentTranchen = new ArrayList<>();
  private @Valid GesuchStateInfoDto stateInfo;
  private @Valid LocalDate periodeStart;
  private @Valid LocalDate periodeEnd;

  /**
   **/
  public GesuchHeaderSbDto historized(HistorizedTranchenDto historized) {
    this.historized = historized;
    return this;
  }

  
  @JsonProperty("historized")
  @NotNull
  public HistorizedTranchenDto getHistorized() {
    return historized;
  }

  @JsonProperty("historized")
  public void setHistorized(HistorizedTranchenDto historized) {
    this.historized = historized;
  }

  /**
   **/
  public GesuchHeaderSbDto currentTranchen(List<GesuchTrancheSlimDto> currentTranchen) {
    this.currentTranchen = currentTranchen;
    return this;
  }

  
  @JsonProperty("currentTranchen")
  @NotNull
  public List<GesuchTrancheSlimDto> getCurrentTranchen() {
    return currentTranchen;
  }

  @JsonProperty("currentTranchen")
  public void setCurrentTranchen(List<GesuchTrancheSlimDto> currentTranchen) {
    this.currentTranchen = currentTranchen;
  }

  public GesuchHeaderSbDto addCurrentTranchenItem(GesuchTrancheSlimDto currentTranchenItem) {
    if (this.currentTranchen == null) {
      this.currentTranchen = new ArrayList<>();
    }

    this.currentTranchen.add(currentTranchenItem);
    return this;
  }

  public GesuchHeaderSbDto removeCurrentTranchenItem(GesuchTrancheSlimDto currentTranchenItem) {
    if (currentTranchenItem != null && this.currentTranchen != null) {
      this.currentTranchen.remove(currentTranchenItem);
    }

    return this;
  }
  /**
   **/
  public GesuchHeaderSbDto stateInfo(GesuchStateInfoDto stateInfo) {
    this.stateInfo = stateInfo;
    return this;
  }

  
  @JsonProperty("stateInfo")
  @NotNull
  public GesuchStateInfoDto getStateInfo() {
    return stateInfo;
  }

  @JsonProperty("stateInfo")
  public void setStateInfo(GesuchStateInfoDto stateInfo) {
    this.stateInfo = stateInfo;
  }

  /**
   **/
  public GesuchHeaderSbDto periodeStart(LocalDate periodeStart) {
    this.periodeStart = periodeStart;
    return this;
  }

  
  @JsonProperty("periodeStart")
  @NotNull
  public LocalDate getPeriodeStart() {
    return periodeStart;
  }

  @JsonProperty("periodeStart")
  public void setPeriodeStart(LocalDate periodeStart) {
    this.periodeStart = periodeStart;
  }

  /**
   **/
  public GesuchHeaderSbDto periodeEnd(LocalDate periodeEnd) {
    this.periodeEnd = periodeEnd;
    return this;
  }

  
  @JsonProperty("periodeEnd")
  @NotNull
  public LocalDate getPeriodeEnd() {
    return periodeEnd;
  }

  @JsonProperty("periodeEnd")
  public void setPeriodeEnd(LocalDate periodeEnd) {
    this.periodeEnd = periodeEnd;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchHeaderSbDto gesuchHeaderSb = (GesuchHeaderSbDto) o;
    return Objects.equals(this.historized, gesuchHeaderSb.historized) &&
        Objects.equals(this.currentTranchen, gesuchHeaderSb.currentTranchen) &&
        Objects.equals(this.stateInfo, gesuchHeaderSb.stateInfo) &&
        Objects.equals(this.periodeStart, gesuchHeaderSb.periodeStart) &&
        Objects.equals(this.periodeEnd, gesuchHeaderSb.periodeEnd);
  }

  @Override
  public int hashCode() {
    return Objects.hash(historized, currentTranchen, stateInfo, periodeStart, periodeEnd);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchHeaderSbDto {\n");
    
    sb.append("    historized: ").append(toIndentedString(historized)).append("\n");
    sb.append("    currentTranchen: ").append(toIndentedString(currentTranchen)).append("\n");
    sb.append("    stateInfo: ").append(toIndentedString(stateInfo)).append("\n");
    sb.append("    periodeStart: ").append(toIndentedString(periodeStart)).append("\n");
    sb.append("    periodeEnd: ").append(toIndentedString(periodeEnd)).append("\n");
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

