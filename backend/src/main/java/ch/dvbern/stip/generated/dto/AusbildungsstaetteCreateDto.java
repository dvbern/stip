package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;



@JsonTypeName("AusbildungsstaetteCreate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AusbildungsstaetteCreateDto  implements Serializable {
  private @Valid String nameDe;
  private @Valid String nameFr;

  /**
   **/
  public AusbildungsstaetteCreateDto nameDe(String nameDe) {
    this.nameDe = nameDe;
    return this;
  }


  @JsonProperty("nameDe")
  @NotNull
  public String getNameDe() {
    return nameDe;
  }

  @JsonProperty("nameDe")
  public void setNameDe(String nameDe) {
    this.nameDe = nameDe;
  }

  /**
   **/
  public AusbildungsstaetteCreateDto nameFr(String nameFr) {
    this.nameFr = nameFr;
    return this;
  }


  @JsonProperty("nameFr")
  @NotNull
  public String getNameFr() {
    return nameFr;
  }

  @JsonProperty("nameFr")
  public void setNameFr(String nameFr) {
    this.nameFr = nameFr;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AusbildungsstaetteCreateDto ausbildungsstaetteCreate = (AusbildungsstaetteCreateDto) o;
    return Objects.equals(this.nameDe, ausbildungsstaetteCreate.nameDe) &&
        Objects.equals(this.nameFr, ausbildungsstaetteCreate.nameFr);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nameDe, nameFr);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungsstaetteCreateDto {\n");

    sb.append("    nameDe: ").append(toIndentedString(nameDe)).append("\n");
    sb.append("    nameFr: ").append(toIndentedString(nameFr)).append("\n");
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

