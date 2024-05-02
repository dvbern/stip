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



@JsonTypeName("GesuchCreate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchCreateDto  implements Serializable {
  private @Valid UUID fallId;
  private @Valid UUID gesuchsperiodeId;

  /**
   **/
  public GesuchCreateDto fallId(UUID fallId) {
    this.fallId = fallId;
    return this;
  }

  
  @JsonProperty("fallId")
  @NotNull
  public UUID getFallId() {
    return fallId;
  }

  @JsonProperty("fallId")
  public void setFallId(UUID fallId) {
    this.fallId = fallId;
  }

  /**
   **/
  public GesuchCreateDto gesuchsperiodeId(UUID gesuchsperiodeId) {
    this.gesuchsperiodeId = gesuchsperiodeId;
    return this;
  }

  
  @JsonProperty("gesuchsperiodeId")
  @NotNull
  public UUID getGesuchsperiodeId() {
    return gesuchsperiodeId;
  }

  @JsonProperty("gesuchsperiodeId")
  public void setGesuchsperiodeId(UUID gesuchsperiodeId) {
    this.gesuchsperiodeId = gesuchsperiodeId;
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
    return Objects.equals(this.fallId, gesuchCreate.fallId) &&
        Objects.equals(this.gesuchsperiodeId, gesuchCreate.gesuchsperiodeId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fallId, gesuchsperiodeId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchCreateDto {\n");
    
    sb.append("    fallId: ").append(toIndentedString(fallId)).append("\n");
    sb.append("    gesuchsperiodeId: ").append(toIndentedString(gesuchsperiodeId)).append("\n");
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

