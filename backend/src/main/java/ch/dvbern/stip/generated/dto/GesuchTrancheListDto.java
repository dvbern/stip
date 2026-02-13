package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
import ch.dvbern.stip.generated.dto.HistorizedTranchenDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
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



@JsonTypeName("GesuchTrancheList")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchTrancheListDto  implements Serializable {
  private @Valid HistorizedTranchenDto historized;
  private @Valid List<GesuchTrancheSlimDto> currentTranchen = new ArrayList<>();

  /**
   **/
  public GesuchTrancheListDto historized(HistorizedTranchenDto historized) {
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
  public GesuchTrancheListDto currentTranchen(List<GesuchTrancheSlimDto> currentTranchen) {
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

  public GesuchTrancheListDto addCurrentTranchenItem(GesuchTrancheSlimDto currentTranchenItem) {
    if (this.currentTranchen == null) {
      this.currentTranchen = new ArrayList<>();
    }

    this.currentTranchen.add(currentTranchenItem);
    return this;
  }

  public GesuchTrancheListDto removeCurrentTranchenItem(GesuchTrancheSlimDto currentTranchenItem) {
    if (currentTranchenItem != null && this.currentTranchen != null) {
      this.currentTranchen.remove(currentTranchenItem);
    }

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchTrancheListDto gesuchTrancheList = (GesuchTrancheListDto) o;
    return Objects.equals(this.historized, gesuchTrancheList.historized) &&
        Objects.equals(this.currentTranchen, gesuchTrancheList.currentTranchen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(historized, currentTranchen);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchTrancheListDto {\n");
    
    sb.append("    historized: ").append(toIndentedString(historized)).append("\n");
    sb.append("    currentTranchen: ").append(toIndentedString(currentTranchen)).append("\n");
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

