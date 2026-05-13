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



@JsonTypeName("SteuerdatenPortError")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class SteuerdatenPortErrorDto  implements Serializable {
  private @Valid String type;
  private @Valid String error;
  private @Valid String userMessage;

  /**
   **/
  public SteuerdatenPortErrorDto type(String type) {
    this.type = type;
    return this;
  }

  
  @JsonProperty("type")
  @NotNull
  public String getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(String type) {
    this.type = type;
  }

  /**
   **/
  public SteuerdatenPortErrorDto error(String error) {
    this.error = error;
    return this;
  }

  
  @JsonProperty("error")
  @NotNull
  public String getError() {
    return error;
  }

  @JsonProperty("error")
  public void setError(String error) {
    this.error = error;
  }

  /**
   **/
  public SteuerdatenPortErrorDto userMessage(String userMessage) {
    this.userMessage = userMessage;
    return this;
  }

  
  @JsonProperty("userMessage")
  @NotNull
  public String getUserMessage() {
    return userMessage;
  }

  @JsonProperty("userMessage")
  public void setUserMessage(String userMessage) {
    this.userMessage = userMessage;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SteuerdatenPortErrorDto steuerdatenPortError = (SteuerdatenPortErrorDto) o;
    return Objects.equals(this.type, steuerdatenPortError.type) &&
        Objects.equals(this.error, steuerdatenPortError.error) &&
        Objects.equals(this.userMessage, steuerdatenPortError.userMessage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, error, userMessage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SteuerdatenPortErrorDto {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    error: ").append(toIndentedString(error)).append("\n");
    sb.append("    userMessage: ").append(toIndentedString(userMessage)).append("\n");
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

