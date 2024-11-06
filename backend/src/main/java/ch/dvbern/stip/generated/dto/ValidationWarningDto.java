package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;



@JsonTypeName("ValidationWarning")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class ValidationWarningDto  implements Serializable {
  private @Valid String messageTemplate;
  private @Valid String message;
  private @Valid String propertyPath;

  /**
   **/
  public ValidationWarningDto messageTemplate(String messageTemplate) {
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
  public ValidationWarningDto message(String message) {
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
  public ValidationWarningDto propertyPath(String propertyPath) {
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
    ValidationWarningDto validationWarning = (ValidationWarningDto) o;
    return Objects.equals(this.messageTemplate, validationWarning.messageTemplate) &&
        Objects.equals(this.message, validationWarning.message) &&
        Objects.equals(this.propertyPath, validationWarning.propertyPath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(messageTemplate, message, propertyPath);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ValidationWarningDto {\n");

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

