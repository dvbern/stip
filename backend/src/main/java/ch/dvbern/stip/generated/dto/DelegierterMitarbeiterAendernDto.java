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



@JsonTypeName("DelegierterMitarbeiterAendern")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DelegierterMitarbeiterAendernDto  implements Serializable {
  private @Valid UUID mitarbeiterId;

  /**
   **/
  public DelegierterMitarbeiterAendernDto mitarbeiterId(UUID mitarbeiterId) {
    this.mitarbeiterId = mitarbeiterId;
    return this;
  }

  
  @JsonProperty("mitarbeiterId")
  @NotNull
  public UUID getMitarbeiterId() {
    return mitarbeiterId;
  }

  @JsonProperty("mitarbeiterId")
  public void setMitarbeiterId(UUID mitarbeiterId) {
    this.mitarbeiterId = mitarbeiterId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DelegierterMitarbeiterAendernDto delegierterMitarbeiterAendern = (DelegierterMitarbeiterAendernDto) o;
    return Objects.equals(this.mitarbeiterId, delegierterMitarbeiterAendern.mitarbeiterId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mitarbeiterId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DelegierterMitarbeiterAendernDto {\n");
    
    sb.append("    mitarbeiterId: ").append(toIndentedString(mitarbeiterId)).append("\n");
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

