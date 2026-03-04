package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("VerfuegtGesuch")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class VerfuegtGesuchDto  implements Serializable {
  private @Valid LocalDate timestamp;
  private @Valid List<GesuchTrancheSlimDto> tranchen = new ArrayList<>();
  private @Valid UUID berechnungId;

  /**
   **/
  public VerfuegtGesuchDto timestamp(LocalDate timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  
  @JsonProperty("timestamp")
  @NotNull
  public LocalDate getTimestamp() {
    return timestamp;
  }

  @JsonProperty("timestamp")
  public void setTimestamp(LocalDate timestamp) {
    this.timestamp = timestamp;
  }

  /**
   **/
  public VerfuegtGesuchDto tranchen(List<GesuchTrancheSlimDto> tranchen) {
    this.tranchen = tranchen;
    return this;
  }

  
  @JsonProperty("tranchen")
  @NotNull
  public List<GesuchTrancheSlimDto> getTranchen() {
    return tranchen;
  }

  @JsonProperty("tranchen")
  public void setTranchen(List<GesuchTrancheSlimDto> tranchen) {
    this.tranchen = tranchen;
  }

  public VerfuegtGesuchDto addTranchenItem(GesuchTrancheSlimDto tranchenItem) {
    if (this.tranchen == null) {
      this.tranchen = new ArrayList<>();
    }

    this.tranchen.add(tranchenItem);
    return this;
  }

  public VerfuegtGesuchDto removeTranchenItem(GesuchTrancheSlimDto tranchenItem) {
    if (tranchenItem != null && this.tranchen != null) {
      this.tranchen.remove(tranchenItem);
    }

    return this;
  }
  /**
   **/
  public VerfuegtGesuchDto berechnungId(UUID berechnungId) {
    this.berechnungId = berechnungId;
    return this;
  }

  
  @JsonProperty("berechnungId")
  @NotNull
  public UUID getBerechnungId() {
    return berechnungId;
  }

  @JsonProperty("berechnungId")
  public void setBerechnungId(UUID berechnungId) {
    this.berechnungId = berechnungId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VerfuegtGesuchDto verfuegtGesuch = (VerfuegtGesuchDto) o;
    return Objects.equals(this.timestamp, verfuegtGesuch.timestamp) &&
        Objects.equals(this.tranchen, verfuegtGesuch.tranchen) &&
        Objects.equals(this.berechnungId, verfuegtGesuch.berechnungId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, tranchen, berechnungId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VerfuegtGesuchDto {\n");
    
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    tranchen: ").append(toIndentedString(tranchen)).append("\n");
    sb.append("    berechnungId: ").append(toIndentedString(berechnungId)).append("\n");
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

