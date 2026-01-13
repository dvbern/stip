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



@JsonTypeName("DemoDataError")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DemoDataErrorDto  implements Serializable {
  private @Valid String internalMessage;
  private @Valid String errorClass;

  /**
   **/
  public DemoDataErrorDto internalMessage(String internalMessage) {
    this.internalMessage = internalMessage;
    return this;
  }

  
  @JsonProperty("internalMessage")
  @NotNull
  public String getInternalMessage() {
    return internalMessage;
  }

  @JsonProperty("internalMessage")
  public void setInternalMessage(String internalMessage) {
    this.internalMessage = internalMessage;
  }

  /**
   **/
  public DemoDataErrorDto errorClass(String errorClass) {
    this.errorClass = errorClass;
    return this;
  }

  
  @JsonProperty("errorClass")
  @NotNull
  public String getErrorClass() {
    return errorClass;
  }

  @JsonProperty("errorClass")
  public void setErrorClass(String errorClass) {
    this.errorClass = errorClass;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoDataErrorDto demoDataError = (DemoDataErrorDto) o;
    return Objects.equals(this.internalMessage, demoDataError.internalMessage) &&
        Objects.equals(this.errorClass, demoDataError.errorClass);
  }

  @Override
  public int hashCode() {
    return Objects.hash(internalMessage, errorClass);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoDataErrorDto {\n");
    
    sb.append("    internalMessage: ").append(toIndentedString(internalMessage)).append("\n");
    sb.append("    errorClass: ").append(toIndentedString(errorClass)).append("\n");
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

