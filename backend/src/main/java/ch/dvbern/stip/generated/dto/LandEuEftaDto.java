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



@JsonTypeName("LandEuEfta")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class LandEuEftaDto  implements Serializable {
  private @Valid ch.dvbern.stip.api.stammdaten.type.Land land;
  private @Valid Boolean isEuEfta;

  /**
   **/
  public LandEuEftaDto land(ch.dvbern.stip.api.stammdaten.type.Land land) {
    this.land = land;
    return this;
  }

  
  @JsonProperty("land")
  @NotNull
  public ch.dvbern.stip.api.stammdaten.type.Land getLand() {
    return land;
  }

  @JsonProperty("land")
  public void setLand(ch.dvbern.stip.api.stammdaten.type.Land land) {
    this.land = land;
  }

  /**
   **/
  public LandEuEftaDto isEuEfta(Boolean isEuEfta) {
    this.isEuEfta = isEuEfta;
    return this;
  }

  
  @JsonProperty("isEuEfta")
  @NotNull
  public Boolean getIsEuEfta() {
    return isEuEfta;
  }

  @JsonProperty("isEuEfta")
  public void setIsEuEfta(Boolean isEuEfta) {
    this.isEuEfta = isEuEfta;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LandEuEftaDto landEuEfta = (LandEuEftaDto) o;
    return Objects.equals(this.land, landEuEfta.land) &&
        Objects.equals(this.isEuEfta, landEuEfta.isEuEfta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(land, isEuEfta);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LandEuEftaDto {\n");
    
    sb.append("    land: ").append(toIndentedString(land)).append("\n");
    sb.append("    isEuEfta: ").append(toIndentedString(isEuEfta)).append("\n");
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

