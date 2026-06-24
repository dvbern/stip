package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
import ch.dvbern.stip.generated.dto.VerfuegtGesuchDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("InitialGesuchs")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class InitialGesuchsDto  implements Serializable {
  private @Valid GesuchTrancheSlimDto eingereichtGesuch;
  private @Valid VerfuegtGesuchDto verfuegtGesuch;

  /**
   **/
  public InitialGesuchsDto eingereichtGesuch(GesuchTrancheSlimDto eingereichtGesuch) {
    this.eingereichtGesuch = eingereichtGesuch;
    return this;
  }

  
  @JsonProperty("eingereichtGesuch")
  public GesuchTrancheSlimDto getEingereichtGesuch() {
    return eingereichtGesuch;
  }

  @JsonProperty("eingereichtGesuch")
  public void setEingereichtGesuch(GesuchTrancheSlimDto eingereichtGesuch) {
    this.eingereichtGesuch = eingereichtGesuch;
  }

  /**
   **/
  public InitialGesuchsDto verfuegtGesuch(VerfuegtGesuchDto verfuegtGesuch) {
    this.verfuegtGesuch = verfuegtGesuch;
    return this;
  }

  
  @JsonProperty("verfuegtGesuch")
  public VerfuegtGesuchDto getVerfuegtGesuch() {
    return verfuegtGesuch;
  }

  @JsonProperty("verfuegtGesuch")
  public void setVerfuegtGesuch(VerfuegtGesuchDto verfuegtGesuch) {
    this.verfuegtGesuch = verfuegtGesuch;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InitialGesuchsDto initialGesuchs = (InitialGesuchsDto) o;
    return Objects.equals(this.eingereichtGesuch, initialGesuchs.eingereichtGesuch) &&
        Objects.equals(this.verfuegtGesuch, initialGesuchs.verfuegtGesuch);
  }

  @Override
  public int hashCode() {
    return Objects.hash(eingereichtGesuch, verfuegtGesuch);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class InitialGesuchsDto {\n");
    
    sb.append("    eingereichtGesuch: ").append(toIndentedString(eingereichtGesuch)).append("\n");
    sb.append("    verfuegtGesuch: ").append(toIndentedString(verfuegtGesuch)).append("\n");
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

