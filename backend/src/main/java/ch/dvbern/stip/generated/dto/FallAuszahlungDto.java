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



@JsonTypeName("FallAuszahlung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class FallAuszahlungDto  implements Serializable {
  private @Valid Boolean isDelegated;
  private @Valid AuszahlungUpdateDto auszahlung;

  /**
   **/
  public FallAuszahlungDto isDelegated(Boolean isDelegated) {
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
  public FallAuszahlungDto auszahlung(AuszahlungUpdateDto auszahlung) {
    this.auszahlung = auszahlung;
    return this;
  }

  
  @JsonProperty("auszahlung")
  public AuszahlungUpdateDto getAuszahlung() {
    return auszahlung;
  }

  @JsonProperty("auszahlung")
  public void setAuszahlung(AuszahlungUpdateDto auszahlung) {
    this.auszahlung = auszahlung;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FallAuszahlungDto fallAuszahlung = (FallAuszahlungDto) o;
    return Objects.equals(this.isDelegated, fallAuszahlung.isDelegated) &&
        Objects.equals(this.auszahlung, fallAuszahlung.auszahlung);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isDelegated, auszahlung);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FallAuszahlungDto {\n");
    
    sb.append("    isDelegated: ").append(toIndentedString(isDelegated)).append("\n");
    sb.append("    auszahlung: ").append(toIndentedString(auszahlung)).append("\n");
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

