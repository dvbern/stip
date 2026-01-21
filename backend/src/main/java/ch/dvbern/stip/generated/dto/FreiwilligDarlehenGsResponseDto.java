package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.FreiwilligDarlehenDto;
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



@JsonTypeName("FreiwilligDarlehenGsResponse")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class FreiwilligDarlehenGsResponseDto  implements Serializable {
  private @Valid List<FreiwilligDarlehenDto> darlehenList = new ArrayList<>();
  private @Valid Boolean canCreateDarlehen;

  /**
   **/
  public FreiwilligDarlehenGsResponseDto darlehenList(List<FreiwilligDarlehenDto> darlehenList) {
    this.darlehenList = darlehenList;
    return this;
  }

  
  @JsonProperty("darlehenList")
  @NotNull
  public List<FreiwilligDarlehenDto> getDarlehenList() {
    return darlehenList;
  }

  @JsonProperty("darlehenList")
  public void setDarlehenList(List<FreiwilligDarlehenDto> darlehenList) {
    this.darlehenList = darlehenList;
  }

  public FreiwilligDarlehenGsResponseDto addDarlehenListItem(FreiwilligDarlehenDto darlehenListItem) {
    if (this.darlehenList == null) {
      this.darlehenList = new ArrayList<>();
    }

    this.darlehenList.add(darlehenListItem);
    return this;
  }

  public FreiwilligDarlehenGsResponseDto removeDarlehenListItem(FreiwilligDarlehenDto darlehenListItem) {
    if (darlehenListItem != null && this.darlehenList != null) {
      this.darlehenList.remove(darlehenListItem);
    }

    return this;
  }
  /**
   **/
  public FreiwilligDarlehenGsResponseDto canCreateDarlehen(Boolean canCreateDarlehen) {
    this.canCreateDarlehen = canCreateDarlehen;
    return this;
  }

  
  @JsonProperty("canCreateDarlehen")
  @NotNull
  public Boolean getCanCreateDarlehen() {
    return canCreateDarlehen;
  }

  @JsonProperty("canCreateDarlehen")
  public void setCanCreateDarlehen(Boolean canCreateDarlehen) {
    this.canCreateDarlehen = canCreateDarlehen;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FreiwilligDarlehenGsResponseDto freiwilligDarlehenGsResponse = (FreiwilligDarlehenGsResponseDto) o;
    return Objects.equals(this.darlehenList, freiwilligDarlehenGsResponse.darlehenList) &&
        Objects.equals(this.canCreateDarlehen, freiwilligDarlehenGsResponse.canCreateDarlehen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(darlehenList, canCreateDarlehen);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FreiwilligDarlehenGsResponseDto {\n");
    
    sb.append("    darlehenList: ").append(toIndentedString(darlehenList)).append("\n");
    sb.append("    canCreateDarlehen: ").append(toIndentedString(canCreateDarlehen)).append("\n");
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

