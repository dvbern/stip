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



@JsonTypeName("updateNutzungsbedingungen_request")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class UpdateNutzungsbedingungenRequestDto  implements Serializable {
  private @Valid Boolean akzeptiert;

  /**
   **/
  public UpdateNutzungsbedingungenRequestDto akzeptiert(Boolean akzeptiert) {
    this.akzeptiert = akzeptiert;
    return this;
  }

  
  @JsonProperty("akzeptiert")
  public Boolean getAkzeptiert() {
    return akzeptiert;
  }

  @JsonProperty("akzeptiert")
  public void setAkzeptiert(Boolean akzeptiert) {
    this.akzeptiert = akzeptiert;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateNutzungsbedingungenRequestDto updateNutzungsbedingungenRequest = (UpdateNutzungsbedingungenRequestDto) o;
    return Objects.equals(this.akzeptiert, updateNutzungsbedingungenRequest.akzeptiert);
  }

  @Override
  public int hashCode() {
    return Objects.hash(akzeptiert);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateNutzungsbedingungenRequestDto {\n");
    
    sb.append("    akzeptiert: ").append(toIndentedString(akzeptiert)).append("\n");
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

