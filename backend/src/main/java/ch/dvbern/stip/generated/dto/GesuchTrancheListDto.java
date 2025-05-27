package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
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
  private @Valid List<GesuchTrancheSlimDto> tranchen;
  private @Valid List<GesuchTrancheSlimDto> initialTranchen;

  /**
   **/
  public GesuchTrancheListDto tranchen(List<GesuchTrancheSlimDto> tranchen) {
    this.tranchen = tranchen;
    return this;
  }

  
  @JsonProperty("tranchen")
  public List<GesuchTrancheSlimDto> getTranchen() {
    return tranchen;
  }

  @JsonProperty("tranchen")
  public void setTranchen(List<GesuchTrancheSlimDto> tranchen) {
    this.tranchen = tranchen;
  }

  public GesuchTrancheListDto addTranchenItem(GesuchTrancheSlimDto tranchenItem) {
    if (this.tranchen == null) {
      this.tranchen = new ArrayList<>();
    }

    this.tranchen.add(tranchenItem);
    return this;
  }

  public GesuchTrancheListDto removeTranchenItem(GesuchTrancheSlimDto tranchenItem) {
    if (tranchenItem != null && this.tranchen != null) {
      this.tranchen.remove(tranchenItem);
    }

    return this;
  }
  /**
   **/
  public GesuchTrancheListDto initialTranchen(List<GesuchTrancheSlimDto> initialTranchen) {
    this.initialTranchen = initialTranchen;
    return this;
  }

  
  @JsonProperty("initialTranchen")
  public List<GesuchTrancheSlimDto> getInitialTranchen() {
    return initialTranchen;
  }

  @JsonProperty("initialTranchen")
  public void setInitialTranchen(List<GesuchTrancheSlimDto> initialTranchen) {
    this.initialTranchen = initialTranchen;
  }

  public GesuchTrancheListDto addInitialTranchenItem(GesuchTrancheSlimDto initialTranchenItem) {
    if (this.initialTranchen == null) {
      this.initialTranchen = new ArrayList<>();
    }

    this.initialTranchen.add(initialTranchenItem);
    return this;
  }

  public GesuchTrancheListDto removeInitialTranchenItem(GesuchTrancheSlimDto initialTranchenItem) {
    if (initialTranchenItem != null && this.initialTranchen != null) {
      this.initialTranchen.remove(initialTranchenItem);
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
    return Objects.equals(this.tranchen, gesuchTrancheList.tranchen) &&
        Objects.equals(this.initialTranchen, gesuchTrancheList.initialTranchen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tranchen, initialTranchen);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchTrancheListDto {\n");
    
    sb.append("    tranchen: ").append(toIndentedString(tranchen)).append("\n");
    sb.append("    initialTranchen: ").append(toIndentedString(initialTranchen)).append("\n");
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

