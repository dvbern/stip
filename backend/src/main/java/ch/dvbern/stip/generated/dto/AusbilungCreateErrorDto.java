package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AusbilungCreateErrorTypeDto;
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



@JsonTypeName("AusbilungCreateError")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AusbilungCreateErrorDto  implements Serializable {
  private @Valid AusbilungCreateErrorTypeDto type;
  private @Valid LocalDate context;

  /**
   **/
  public AusbilungCreateErrorDto type(AusbilungCreateErrorTypeDto type) {
    this.type = type;
    return this;
  }

  
  @JsonProperty("type")
  @NotNull
  public AusbilungCreateErrorTypeDto getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(AusbilungCreateErrorTypeDto type) {
    this.type = type;
  }

  /**
   **/
  public AusbilungCreateErrorDto context(LocalDate context) {
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
    AusbilungCreateErrorDto ausbilungCreateError = (AusbilungCreateErrorDto) o;
    return Objects.equals(this.type, ausbilungCreateError.type) &&
        Objects.equals(this.context, ausbilungCreateError.context);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, context);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbilungCreateErrorDto {\n");
    
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

