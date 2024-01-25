package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("Eltern_allOf")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class ElternAllOfDto  implements Serializable {
  private @Valid UUID copyOfId;

  /**
   **/
  public ElternAllOfDto copyOfId(UUID copyOfId) {
    this.copyOfId = copyOfId;
    return this;
  }

  
  @JsonProperty("copyOfId")
  public UUID getCopyOfId() {
    return copyOfId;
  }

  @JsonProperty("copyOfId")
  public void setCopyOfId(UUID copyOfId) {
    this.copyOfId = copyOfId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ElternAllOfDto elternAllOf = (ElternAllOfDto) o;
    return Objects.equals(this.copyOfId, elternAllOf.copyOfId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(copyOfId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ElternAllOfDto {\n");
    
    sb.append("    copyOfId: ").append(toIndentedString(copyOfId)).append("\n");
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

