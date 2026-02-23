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



@JsonTypeName("UserConsent")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class UserConsentDto  implements Serializable {
  private @Valid Boolean consentGiven;
  private @Valid java.time.LocalDateTime timestampErstellt;

  /**
   **/
  public UserConsentDto consentGiven(Boolean consentGiven) {
    this.consentGiven = consentGiven;
    return this;
  }


  @JsonProperty("consentGiven")
  @NotNull
  public Boolean getConsentGiven() {
    return consentGiven;
  }

  @JsonProperty("consentGiven")
  public void setConsentGiven(Boolean consentGiven) {
    this.consentGiven = consentGiven;
  }

  /**
   **/
  public UserConsentDto timestampErstellt(java.time.LocalDateTime timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
    return this;
  }


  @JsonProperty("timestampErstellt")
  @NotNull
  public java.time.LocalDateTime getTimestampErstellt() {
    return timestampErstellt;
  }

  @JsonProperty("timestampErstellt")
  public void setTimestampErstellt(java.time.LocalDateTime timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserConsentDto userConsent = (UserConsentDto) o;
    return Objects.equals(this.consentGiven, userConsent.consentGiven) &&
        Objects.equals(this.timestampErstellt, userConsent.timestampErstellt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(consentGiven, timestampErstellt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserConsentDto {\n");

    sb.append("    consentGiven: ").append(toIndentedString(consentGiven)).append("\n");
    sb.append("    timestampErstellt: ").append(toIndentedString(timestampErstellt)).append("\n");
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

