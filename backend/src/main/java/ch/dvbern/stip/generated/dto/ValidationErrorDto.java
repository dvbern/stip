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



@JsonTypeName("ValidationError")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class ValidationErrorDto  implements Serializable {
  private @Valid String messageTemplate;
  private @Valid String message;
  private @Valid String propertyPath;

  /**
   **/
  public ValidationErrorDto messageTemplate(String messageTemplate) {
    this.messageTemplate = messageTemplate;
    return this;
  }

  
  @JsonProperty("messageTemplate")
  @NotNull
  public String getMessageTemplate() {
    return messageTemplate;
  }

  @JsonProperty("messageTemplate")
  public void setMessageTemplate(String messageTemplate) {
    this.messageTemplate = messageTemplate;
  }

  /**
   **/
  public ValidationErrorDto message(String message) {
    this.message = message;
    return this;
  }

  
  @JsonProperty("message")
  @NotNull
  public String getMessage() {
    return message;
  }

  @JsonProperty("message")
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   **/
  public ValidationErrorDto propertyPath(String propertyPath) {
    this.propertyPath = propertyPath;
    return this;
  }

  
  @JsonProperty("propertyPath")
  public String getPropertyPath() {
    return propertyPath;
  }

  @JsonProperty("propertyPath")
  public void setPropertyPath(String propertyPath) {
    this.propertyPath = propertyPath;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValidationErrorDto validationError = (ValidationErrorDto) o;
    return Objects.equals(this.messageTemplate, validationError.messageTemplate) &&
        Objects.equals(this.message, validationError.message) &&
        Objects.equals(this.propertyPath, validationError.propertyPath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageTemplate, message, propertyPath);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValidationErrorDto {\n");
    
    sb.append("    messageTemplate: ").append(toIndentedString(messageTemplate)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    propertyPath: ").append(toIndentedString(propertyPath)).append("\n");
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

