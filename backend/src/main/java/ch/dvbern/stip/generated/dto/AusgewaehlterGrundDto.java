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



@JsonTypeName("AusgewaehlterGrund")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AusgewaehlterGrundDto  implements Serializable {
  private @Valid UUID decisionId;

  /**
   **/
  public AusgewaehlterGrundDto decisionId(UUID decisionId) {
    this.decisionId = decisionId;
    return this;
  }

  
  @JsonProperty("decisionId")
  @NotNull
  public UUID getDecisionId() {
    return decisionId;
  }

  @JsonProperty("decisionId")
  public void setDecisionId(UUID decisionId) {
    this.decisionId = decisionId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AusgewaehlterGrundDto ausgewaehlterGrund = (AusgewaehlterGrundDto) o;
    return Objects.equals(this.decisionId, ausgewaehlterGrund.decisionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(decisionId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusgewaehlterGrundDto {\n");
    
    sb.append("    decisionId: ").append(toIndentedString(decisionId)).append("\n");
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
