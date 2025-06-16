package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AuszahlungUpdateDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("Auszahlung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AuszahlungDto  implements Serializable {
  private @Valid Boolean isDelegated;
  private @Valid AuszahlungUpdateDto value;

  /**
   **/
  public AuszahlungDto isDelegated(Boolean isDelegated) {
    this.isDelegated = isDelegated;
    return this;
  }

  
  @JsonProperty("isDelegated")
  @NotNull
  public Boolean getIsDelegated() {
    return isDelegated;
  }

  @JsonProperty("isDelegated")
  public void setIsDelegated(Boolean isDelegated) {
    this.isDelegated = isDelegated;
  }

  /**
   **/
  public AuszahlungDto value(AuszahlungUpdateDto value) {
    this.value = value;
    return this;
  }

  
  @JsonProperty("value")
  public AuszahlungUpdateDto getValue() {
    return value;
  }

  @JsonProperty("value")
  public void setValue(AuszahlungUpdateDto value) {
    this.value = value;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuszahlungDto auszahlung = (AuszahlungDto) o;
    return Objects.equals(this.isDelegated, auszahlung.isDelegated) &&
        Objects.equals(this.value, auszahlung.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isDelegated, value);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuszahlungDto {\n");
    
    sb.append("    isDelegated: ").append(toIndentedString(isDelegated)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
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

