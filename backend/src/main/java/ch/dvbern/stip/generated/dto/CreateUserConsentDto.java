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



@JsonTypeName("CreateUserConsent")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class CreateUserConsentDto  implements Serializable {
  private @Valid Boolean consentGiven;

  /**
   **/
  public CreateUserConsentDto consentGiven(Boolean consentGiven) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateUserConsentDto createUserConsent = (CreateUserConsentDto) o;
    return Objects.equals(this.consentGiven, createUserConsent.consentGiven);
  }

  @Override
  public int hashCode() {
    return Objects.hash(consentGiven);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateUserConsentDto {\n");
    
    sb.append("    consentGiven: ").append(toIndentedString(consentGiven)).append("\n");
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

