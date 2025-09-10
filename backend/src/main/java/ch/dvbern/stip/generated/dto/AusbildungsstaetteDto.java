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



@JsonTypeName("Ausbildungsstaette")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AusbildungsstaetteDto  implements Serializable {
  private @Valid UUID id;
  private @Valid String nameDe;
  private @Valid String nameFr;
  private @Valid ch.dvbern.stip.api.ausbildung.type.AusbildungsstaetteNummerTyp nummerTyp;
  private @Valid Boolean aktiv;
  private @Valid String nummer;

  /**
   **/
  public AusbildungsstaetteDto id(UUID id) {
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
  public AusbildungsstaetteDto nummerTyp(ch.dvbern.stip.api.ausbildung.type.AusbildungsstaetteNummerTyp nummerTyp) {
    this.nummerTyp = nummerTyp;
    return this;
  }

  
  @JsonProperty("nummerTyp")
  @NotNull
  public ch.dvbern.stip.api.ausbildung.type.AusbildungsstaetteNummerTyp getNummerTyp() {
    return nummerTyp;
  }

  @JsonProperty("nummerTyp")
  public void setNummerTyp(ch.dvbern.stip.api.ausbildung.type.AusbildungsstaetteNummerTyp nummerTyp) {
    this.nummerTyp = nummerTyp;
  }

  /**
   **/
  public AusbildungsstaetteDto aktiv(Boolean aktiv) {
    this.aktiv = aktiv;
    return this;
  }

  
  @JsonProperty("aktiv")
  @NotNull
  public Boolean getAktiv() {
    return aktiv;
  }

  @JsonProperty("aktiv")
  public void setAktiv(Boolean aktiv) {
    this.aktiv = aktiv;
  }

  /**
   **/
  public AusbildungsstaetteDto nummer(String nummer) {
    this.nummer = nummer;
    return this;
  }

  
  @JsonProperty("nummer")
  public String getNummer() {
    return nummer;
  }

  @JsonProperty("nummer")
  public void setNummer(String nummer) {
    this.nummer = nummer;
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
    return Objects.equals(this.id, ausbildungsstaette.id) &&
        Objects.equals(this.nameDe, ausbildungsstaette.nameDe) &&
        Objects.equals(this.nameFr, ausbildungsstaette.nameFr) &&
        Objects.equals(this.nummerTyp, ausbildungsstaette.nummerTyp) &&
        Objects.equals(this.aktiv, ausbildungsstaette.aktiv) &&
        Objects.equals(this.nummer, ausbildungsstaette.nummer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nameDe, nameFr, nummerTyp, aktiv, nummer);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungsstaetteDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    nameDe: ").append(toIndentedString(nameDe)).append("\n");
    sb.append("    nameFr: ").append(toIndentedString(nameFr)).append("\n");
    sb.append("    nummerTyp: ").append(toIndentedString(nummerTyp)).append("\n");
    sb.append("    aktiv: ").append(toIndentedString(aktiv)).append("\n");
    sb.append("    nummer: ").append(toIndentedString(nummer)).append("\n");
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

