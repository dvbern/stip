package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDto;
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



@JsonTypeName("GesuchTrancheUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchTrancheUpdateDto  implements Serializable {
  private @Valid GesuchFormularUpdateDto gesuchFormular;
  private @Valid UUID id;

  /**
   **/
  public GesuchTrancheUpdateDto gesuchFormular(GesuchFormularUpdateDto gesuchFormular) {
    this.gesuchFormular = gesuchFormular;
    return this;
  }

  
  @JsonProperty("gesuchFormular")
  @NotNull
  public GesuchFormularUpdateDto getGesuchFormular() {
    return gesuchFormular;
  }

  @JsonProperty("gesuchFormular")
  public void setGesuchFormular(GesuchFormularUpdateDto gesuchFormular) {
    this.gesuchFormular = gesuchFormular;
  }

  /**
   **/
  public GesuchTrancheUpdateDto id(UUID id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty("id")
  @NotNull
  public UUID getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(UUID id) {
    this.id = id;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchTrancheUpdateDto gesuchTrancheUpdate = (GesuchTrancheUpdateDto) o;
    return Objects.equals(this.gesuchFormular, gesuchTrancheUpdate.gesuchFormular) &&
        Objects.equals(this.id, gesuchTrancheUpdate.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gesuchFormular, id);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchTrancheUpdateDto {\n");
    
    sb.append("    gesuchFormular: ").append(toIndentedString(gesuchFormular)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

