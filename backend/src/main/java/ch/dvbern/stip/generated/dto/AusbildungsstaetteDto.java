package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;



@JsonTypeName("Ausbildungsstaette")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AusbildungsstaetteDto  implements Serializable {
  private @Valid String nameDe;
  private @Valid String nameFr;
  private @Valid List<AusbildungsgangDto> ausbildungsgaenge;
  private @Valid UUID id;

  /**
   **/
  public AusbildungsstaetteDto nameDe(String nameDe) {
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
  public AusbildungsstaetteDto nameFr(String nameFr) {
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

  /**
   **/
  public AusbildungsstaetteDto ausbildungsgaenge(List<AusbildungsgangDto> ausbildungsgaenge) {
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

  public AusbildungsstaetteDto addAusbildungsgaengeItem(AusbildungsgangDto ausbildungsgaengeItem) {
    if (this.ausbildungsgaenge == null) {
      this.ausbildungsgaenge = new ArrayList<>();
    }

    this.ausbildungsgaenge.add(ausbildungsgaengeItem);
    return this;
  }

  public AusbildungsstaetteDto removeAusbildungsgaengeItem(AusbildungsgangDto ausbildungsgaengeItem) {
    if (ausbildungsgaengeItem != null && this.ausbildungsgaenge != null) {
      this.ausbildungsgaenge.remove(ausbildungsgaengeItem);
    }

    return this;
  }
  /**
   **/
  public AusbildungsstaetteDto id(UUID id) {
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
    AusbildungsstaetteDto ausbildungsstaette = (AusbildungsstaetteDto) o;
    return Objects.equals(this.nameDe, ausbildungsstaette.nameDe) &&
        Objects.equals(this.nameFr, ausbildungsstaette.nameFr) &&
        Objects.equals(this.ausbildungsgaenge, ausbildungsstaette.ausbildungsgaenge) &&
        Objects.equals(this.id, ausbildungsstaette.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nameDe, nameFr, ausbildungsgaenge, id);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungsstaetteDto {\n");

    sb.append("    nameDe: ").append(toIndentedString(nameDe)).append("\n");
    sb.append("    nameFr: ").append(toIndentedString(nameFr)).append("\n");
    sb.append("    ausbildungsgaenge: ").append(toIndentedString(ausbildungsgaenge)).append("\n");
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

