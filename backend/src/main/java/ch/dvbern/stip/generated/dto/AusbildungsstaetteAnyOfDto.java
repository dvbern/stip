package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;



@JsonTypeName("Ausbildungsstaette_anyOf")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AusbildungsstaetteAnyOfDto  implements Serializable {
  private @Valid List<AusbildungsgangDto> ausbildungsgaenge;

  /**
   **/
  public AusbildungsstaetteAnyOfDto ausbildungsgaenge(List<AusbildungsgangDto> ausbildungsgaenge) {
    this.ausbildungsgaenge = ausbildungsgaenge;
    return this;
  }


  @JsonProperty("ausbildungsgaenge")
  public List<AusbildungsgangDto> getAusbildungsgaenge() {
    return ausbildungsgaenge;
  }

  @JsonProperty("ausbildungsgaenge")
  public void setAusbildungsgaenge(List<AusbildungsgangDto> ausbildungsgaenge) {
    this.ausbildungsgaenge = ausbildungsgaenge;
  }

  public AusbildungsstaetteAnyOfDto addAusbildungsgaengeItem(AusbildungsgangDto ausbildungsgaengeItem) {
    if (this.ausbildungsgaenge == null) {
      this.ausbildungsgaenge = new ArrayList<>();
    }

    this.ausbildungsgaenge.add(ausbildungsgaengeItem);
    return this;
  }

  public AusbildungsstaetteAnyOfDto removeAusbildungsgaengeItem(AusbildungsgangDto ausbildungsgaengeItem) {
    if (ausbildungsgaengeItem != null && this.ausbildungsgaenge != null) {
      this.ausbildungsgaenge.remove(ausbildungsgaengeItem);
    }

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AusbildungsstaetteAnyOfDto ausbildungsstaetteAnyOf = (AusbildungsstaetteAnyOfDto) o;
    return Objects.equals(this.ausbildungsgaenge, ausbildungsstaetteAnyOf.ausbildungsgaenge);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ausbildungsgaenge);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungsstaetteAnyOfDto {\n");

    sb.append("    ausbildungsgaenge: ").append(toIndentedString(ausbildungsgaenge)).append("\n");
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

