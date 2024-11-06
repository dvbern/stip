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



@JsonTypeName("SachbearbeiterZuordnungStammdatenList")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class SachbearbeiterZuordnungStammdatenListDto  implements Serializable {
  private @Valid UUID sachbearbeiter;
  private @Valid SachbearbeiterZuordnungStammdatenDto zuordnung;

  /**
   **/
  public SachbearbeiterZuordnungStammdatenListDto sachbearbeiter(UUID sachbearbeiter) {
    this.sachbearbeiter = sachbearbeiter;
    return this;
  }

  
  @JsonProperty("sachbearbeiter")
  public UUID getSachbearbeiter() {
    return sachbearbeiter;
  }

  @JsonProperty("sachbearbeiter")
  public void setSachbearbeiter(UUID sachbearbeiter) {
    this.sachbearbeiter = sachbearbeiter;
  }

  /**
   **/
  public SachbearbeiterZuordnungStammdatenListDto zuordnung(SachbearbeiterZuordnungStammdatenDto zuordnung) {
    this.zuordnung = zuordnung;
    return this;
  }

  
  @JsonProperty("zuordnung")
  public SachbearbeiterZuordnungStammdatenDto getZuordnung() {
    return zuordnung;
  }

  @JsonProperty("zuordnung")
  public void setZuordnung(SachbearbeiterZuordnungStammdatenDto zuordnung) {
    this.zuordnung = zuordnung;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SachbearbeiterZuordnungStammdatenListDto sachbearbeiterZuordnungStammdatenList = (SachbearbeiterZuordnungStammdatenListDto) o;
    return Objects.equals(this.sachbearbeiter, sachbearbeiterZuordnungStammdatenList.sachbearbeiter) &&
        Objects.equals(this.zuordnung, sachbearbeiterZuordnungStammdatenList.zuordnung);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sachbearbeiter, zuordnung);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SachbearbeiterZuordnungStammdatenListDto {\n");
    
    sb.append("    sachbearbeiter: ").append(toIndentedString(sachbearbeiter)).append("\n");
    sb.append("    zuordnung: ").append(toIndentedString(zuordnung)).append("\n");
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

