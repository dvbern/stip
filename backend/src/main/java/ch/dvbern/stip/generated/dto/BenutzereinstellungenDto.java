package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("Benutzereinstellungen")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class BenutzereinstellungenDto  implements Serializable {
  private @Valid UUID id;
  private @Valid Boolean digitaleKommunikation;

  /**
   **/
  public BenutzereinstellungenDto id(UUID id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   **/
  public BenutzereinstellungenDto digitaleKommunikation(Boolean digitaleKommunikation) {
    this.digitaleKommunikation = digitaleKommunikation;
    return this;
  }

  
  @JsonProperty("digitaleKommunikation")
  public Boolean getDigitaleKommunikation() {
    return digitaleKommunikation;
  }

  @JsonProperty("digitaleKommunikation")
  public void setDigitaleKommunikation(Boolean digitaleKommunikation) {
    this.digitaleKommunikation = digitaleKommunikation;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BenutzereinstellungenDto benutzereinstellungen = (BenutzereinstellungenDto) o;
    return Objects.equals(this.id, benutzereinstellungen.id) &&
        Objects.equals(this.digitaleKommunikation, benutzereinstellungen.digitaleKommunikation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, digitaleKommunikation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BenutzereinstellungenDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    digitaleKommunikation: ").append(toIndentedString(digitaleKommunikation)).append("\n");
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

