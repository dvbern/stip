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



@JsonTypeName("TenantFeature")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class TenantFeatureDto  implements Serializable {
  private @Valid Boolean nesko;

  /**
   **/
  public TenantFeatureDto nesko(Boolean nesko) {
    this.nesko = nesko;
    return this;
  }

  
  @JsonProperty("nesko")
  @NotNull
  public Boolean getNesko() {
    return nesko;
  }

  @JsonProperty("nesko")
  public void setNesko(Boolean nesko) {
    this.nesko = nesko;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TenantFeatureDto tenantFeature = (TenantFeatureDto) o;
    return Objects.equals(this.nesko, tenantFeature.nesko);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nesko);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TenantFeatureDto {\n");
    
    sb.append("    nesko: ").append(toIndentedString(nesko)).append("\n");
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

