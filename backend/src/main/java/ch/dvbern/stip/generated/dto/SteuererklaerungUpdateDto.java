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



@JsonTypeName("SteuererklaerungUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class SteuererklaerungUpdateDto  implements Serializable {
  private @Valid ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp steuerdatenTyp;
  private @Valid Boolean steuererklaerungInBern;
  private @Valid UUID id;

  /**
   **/
  public SteuererklaerungUpdateDto steuerdatenTyp(ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp steuerdatenTyp) {
    this.steuerdatenTyp = steuerdatenTyp;
    return this;
  }

  
  @JsonProperty("steuerdatenTyp")
  @NotNull
  public ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp getSteuerdatenTyp() {
    return steuerdatenTyp;
  }

  @JsonProperty("steuerdatenTyp")
  public void setSteuerdatenTyp(ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp steuerdatenTyp) {
    this.steuerdatenTyp = steuerdatenTyp;
  }

  /**
   **/
  public SteuererklaerungUpdateDto steuererklaerungInBern(Boolean steuererklaerungInBern) {
    this.steuererklaerungInBern = steuererklaerungInBern;
    return this;
  }

  
  @JsonProperty("steuererklaerungInBern")
  @NotNull
  public Boolean getSteuererklaerungInBern() {
    return steuererklaerungInBern;
  }

  @JsonProperty("steuererklaerungInBern")
  public void setSteuererklaerungInBern(Boolean steuererklaerungInBern) {
    this.steuererklaerungInBern = steuererklaerungInBern;
  }

  /**
   **/
  public SteuererklaerungUpdateDto id(UUID id) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SteuererklaerungUpdateDto steuererklaerungUpdate = (SteuererklaerungUpdateDto) o;
    return Objects.equals(this.steuerdatenTyp, steuererklaerungUpdate.steuerdatenTyp) &&
        Objects.equals(this.steuererklaerungInBern, steuererklaerungUpdate.steuererklaerungInBern) &&
        Objects.equals(this.id, steuererklaerungUpdate.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(steuerdatenTyp, steuererklaerungInBern, id);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SteuererklaerungUpdateDto {\n");
    
    sb.append("    steuerdatenTyp: ").append(toIndentedString(steuerdatenTyp)).append("\n");
    sb.append("    steuererklaerungInBern: ").append(toIndentedString(steuererklaerungInBern)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

