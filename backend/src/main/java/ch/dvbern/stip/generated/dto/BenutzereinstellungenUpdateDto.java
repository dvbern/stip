package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;



@JsonTypeName("BenutzereinstellungenUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class BenutzereinstellungenUpdateDto  implements Serializable {
  private @Valid UUID id;
  private @Valid Boolean digitaleKommunikation;

  /**
   **/
  public BenutzereinstellungenUpdateDto id(UUID id) {
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
  public BenutzereinstellungenUpdateDto digitaleKommunikation(Boolean digitaleKommunikation) {
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
    BenutzereinstellungenUpdateDto benutzereinstellungenUpdate = (BenutzereinstellungenUpdateDto) o;
    return Objects.equals(this.id, benutzereinstellungenUpdate.id) &&
        Objects.equals(this.digitaleKommunikation, benutzereinstellungenUpdate.digitaleKommunikation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, digitaleKommunikation);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BenutzereinstellungenUpdateDto {\n");

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

