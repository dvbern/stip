package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AusbildungCreateErrorDto;
import ch.dvbern.stip.generated.dto.AusbildungDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("AusbildungCreateResponse")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AusbildungCreateResponseDto  implements Serializable {
  private @Valid AusbildungDto ausbildung;
  private @Valid AusbildungCreateErrorDto error;

  /**
   **/
  public AusbildungCreateResponseDto ausbildung(AusbildungDto ausbildung) {
    this.ausbildung = ausbildung;
    return this;
  }

  
  @JsonProperty("ausbildung")
  public AusbildungDto getAusbildung() {
    return ausbildung;
  }

  @JsonProperty("ausbildung")
  public void setAusbildung(AusbildungDto ausbildung) {
    this.ausbildung = ausbildung;
  }

  /**
   **/
  public AusbildungCreateResponseDto error(AusbildungCreateErrorDto error) {
    this.error = error;
    return this;
  }

  
  @JsonProperty("error")
  public AusbildungCreateErrorDto getError() {
    return error;
  }

  @JsonProperty("error")
  public void setError(AusbildungCreateErrorDto error) {
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
    AusbildungCreateResponseDto ausbildungCreateResponse = (AusbildungCreateResponseDto) o;
    return Objects.equals(this.ausbildung, ausbildungCreateResponse.ausbildung) &&
        Objects.equals(this.error, ausbildungCreateResponse.error);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ausbildung, error);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungCreateResponseDto {\n");
    
    sb.append("    ausbildung: ").append(toIndentedString(ausbildung)).append("\n");
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

