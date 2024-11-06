package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;



@JsonTypeName("GesuchUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchUpdateDto  implements Serializable {
  private @Valid GesuchTrancheUpdateDto gesuchTrancheToWorkWith;

  /**
   **/
  public GesuchUpdateDto gesuchTrancheToWorkWith(GesuchTrancheUpdateDto gesuchTrancheToWorkWith) {
    this.gesuchTrancheToWorkWith = gesuchTrancheToWorkWith;
    return this;
  }


  @JsonProperty("gesuchTrancheToWorkWith")
  @NotNull
  public GesuchTrancheUpdateDto getGesuchTrancheToWorkWith() {
    return gesuchTrancheToWorkWith;
  }

  @JsonProperty("gesuchTrancheToWorkWith")
  public void setGesuchTrancheToWorkWith(GesuchTrancheUpdateDto gesuchTrancheToWorkWith) {
    this.gesuchTrancheToWorkWith = gesuchTrancheToWorkWith;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchUpdateDto gesuchUpdate = (GesuchUpdateDto) o;
    return Objects.equals(this.gesuchTrancheToWorkWith, gesuchUpdate.gesuchTrancheToWorkWith);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gesuchTrancheToWorkWith);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchUpdateDto {\n");

    sb.append("    gesuchTrancheToWorkWith: ").append(toIndentedString(gesuchTrancheToWorkWith)).append("\n");
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

