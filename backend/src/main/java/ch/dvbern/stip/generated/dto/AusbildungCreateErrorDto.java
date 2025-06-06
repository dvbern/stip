package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import ch.dvbern.stip.api.common.type.GesuchsperiodeSelectErrorType;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@JsonTypeName("AusbildungCreateError")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AusbildungCreateErrorDto  implements Serializable {
  private @Valid GesuchsperiodeSelectErrorType type;
  private @Valid LocalDate context;

  /**
   **/
  public AusbildungCreateErrorDto type(GesuchsperiodeSelectErrorType type) {
    this.type = type;
    return this;
  }


  @JsonProperty("type")
  @NotNull
  public GesuchsperiodeSelectErrorType getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(GesuchsperiodeSelectErrorType type) {
    this.type = type;
  }

  /**
   **/
  public AusbildungCreateErrorDto context(LocalDate context) {
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
    AusbildungCreateErrorDto ausbildungCreateError = (AusbildungCreateErrorDto) o;
    return Objects.equals(this.type, ausbildungCreateError.type) &&
        Objects.equals(this.context, ausbildungCreateError.context);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, context);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungCreateErrorDto {\n");

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

