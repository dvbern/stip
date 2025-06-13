package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.GesuchsperiodeSelectErrorDto;
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



@JsonTypeName("GesuchCreateResponse")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchCreateResponseDto  implements Serializable {
  private @Valid UUID gesuch;
  private @Valid GesuchsperiodeSelectErrorDto error;

  /**
   **/
  public GesuchCreateResponseDto gesuch(UUID gesuch) {
    this.gesuch = gesuch;
    return this;
  }

  
  @JsonProperty("gesuch")
  public UUID getGesuch() {
    return gesuch;
  }

  @JsonProperty("gesuch")
  public void setGesuch(UUID gesuch) {
    this.gesuch = gesuch;
  }

  /**
   **/
  public GesuchCreateResponseDto error(GesuchsperiodeSelectErrorDto error) {
    this.error = error;
    return this;
  }

  
  @JsonProperty("error")
  public GesuchsperiodeSelectErrorDto getError() {
    return error;
  }

  @JsonProperty("error")
  public void setError(GesuchsperiodeSelectErrorDto error) {
    this.error = error;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchCreateResponseDto gesuchCreateResponse = (GesuchCreateResponseDto) o;
    return Objects.equals(this.gesuch, gesuchCreateResponse.gesuch) &&
        Objects.equals(this.error, gesuchCreateResponse.error);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gesuch, error);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchCreateResponseDto {\n");
    
    sb.append("    gesuch: ").append(toIndentedString(gesuch)).append("\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
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

