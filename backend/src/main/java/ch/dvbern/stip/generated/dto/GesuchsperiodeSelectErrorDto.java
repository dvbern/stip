package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("GesuchsperiodeSelectError")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchsperiodeSelectErrorDto  implements Serializable {
  private @Valid ch.dvbern.stip.api.common.type.GesuchsperiodeSelectErrorType type;
  private @Valid LocalDate context;

  /**
   **/
  public GesuchsperiodeSelectErrorDto type(ch.dvbern.stip.api.common.type.GesuchsperiodeSelectErrorType type) {
    this.type = type;
    return this;
  }

  
  @JsonProperty("type")
  @NotNull
  public ch.dvbern.stip.api.common.type.GesuchsperiodeSelectErrorType getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(ch.dvbern.stip.api.common.type.GesuchsperiodeSelectErrorType type) {
    this.type = type;
  }

  /**
   **/
  public GesuchsperiodeSelectErrorDto context(LocalDate context) {
    this.context = context;
    return this;
  }

  
  @JsonProperty("context")
  public LocalDate getContext() {
    return context;
  }

  @JsonProperty("context")
  public void setContext(LocalDate context) {
    this.context = context;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchsperiodeSelectErrorDto gesuchsperiodeSelectError = (GesuchsperiodeSelectErrorDto) o;
    return Objects.equals(this.type, gesuchsperiodeSelectError.type) &&
        Objects.equals(this.context, gesuchsperiodeSelectError.context);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, context);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchsperiodeSelectErrorDto {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    context: ").append(toIndentedString(context)).append("\n");
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

