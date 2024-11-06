package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;



@JsonTypeName("GesuchCreate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchCreateDto  implements Serializable {
  private @Valid UUID ausbildungId;

  /**
   **/
  public GesuchCreateDto ausbildungId(UUID ausbildungId) {
    this.ausbildungId = ausbildungId;
    return this;
  }


  @JsonProperty("ausbildungId")
  @NotNull
  public UUID getAusbildungId() {
    return ausbildungId;
  }

  @JsonProperty("ausbildungId")
  public void setAusbildungId(UUID ausbildungId) {
    this.ausbildungId = ausbildungId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchCreateDto gesuchCreate = (GesuchCreateDto) o;
    return Objects.equals(this.ausbildungId, gesuchCreate.ausbildungId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ausbildungId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchCreateDto {\n");

    sb.append("    ausbildungId: ").append(toIndentedString(ausbildungId)).append("\n");
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

