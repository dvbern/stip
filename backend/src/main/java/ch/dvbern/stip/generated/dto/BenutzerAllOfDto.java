package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenDto;
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



@JsonTypeName("Benutzer_allOf")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class BenutzerAllOfDto  implements Serializable {
  private @Valid UUID id;
  private @Valid SachbearbeiterZuordnungStammdatenDto sachbearbeiterZuordnungStammdaten;

  /**
   **/
  public BenutzerAllOfDto id(UUID id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty("id")
  @NotNull
  public UUID getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   **/
  public BenutzerAllOfDto sachbearbeiterZuordnungStammdaten(SachbearbeiterZuordnungStammdatenDto sachbearbeiterZuordnungStammdaten) {
    this.sachbearbeiterZuordnungStammdaten = sachbearbeiterZuordnungStammdaten;
    return this;
  }

  
  @JsonProperty("sachbearbeiterZuordnungStammdaten")
  public SachbearbeiterZuordnungStammdatenDto getSachbearbeiterZuordnungStammdaten() {
    return sachbearbeiterZuordnungStammdaten;
  }

  @JsonProperty("sachbearbeiterZuordnungStammdaten")
  public void setSachbearbeiterZuordnungStammdaten(SachbearbeiterZuordnungStammdatenDto sachbearbeiterZuordnungStammdaten) {
    this.sachbearbeiterZuordnungStammdaten = sachbearbeiterZuordnungStammdaten;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BenutzerAllOfDto benutzerAllOf = (BenutzerAllOfDto) o;
    return Objects.equals(this.id, benutzerAllOf.id) &&
        Objects.equals(this.sachbearbeiterZuordnungStammdaten, benutzerAllOf.sachbearbeiterZuordnungStammdaten);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, sachbearbeiterZuordnungStammdaten);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BenutzerAllOfDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    sachbearbeiterZuordnungStammdaten: ").append(toIndentedString(sachbearbeiterZuordnungStammdaten)).append("\n");
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

