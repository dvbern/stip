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



@JsonTypeName("EinreichedatumStatus")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class EinreichedatumStatusDto  implements Serializable {
  private @Valid Boolean canAendern;

  /**
   **/
  public EinreichedatumStatusDto canAendern(Boolean canAendern) {
    this.canAendern = canAendern;
    return this;
  }

  
  @JsonProperty("canAendern")
  @NotNull
  public Boolean getCanAendern() {
    return canAendern;
  }

  @JsonProperty("canAendern")
  public void setCanAendern(Boolean canAendern) {
    this.canAendern = canAendern;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EinreichedatumStatusDto einreichedatumStatus = (EinreichedatumStatusDto) o;
    return Objects.equals(this.canAendern, einreichedatumStatus.canAendern);
  }

  @Override
  public int hashCode() {
    return Objects.hash(canAendern);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EinreichedatumStatusDto {\n");
    
    sb.append("    canAendern: ").append(toIndentedString(canAendern)).append("\n");
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

