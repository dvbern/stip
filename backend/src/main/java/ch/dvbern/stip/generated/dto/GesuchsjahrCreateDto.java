package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;



@JsonTypeName("GesuchsjahrCreate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchsjahrCreateDto  implements Serializable {
  private @Valid String bezeichnungDe;
  private @Valid String bezeichnungFr;
  private @Valid Integer technischesJahr;

  /**
   **/
  public GesuchsjahrCreateDto bezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
    return this;
  }


  @JsonProperty("bezeichnungDe")
  @NotNull
  public String getBezeichnungDe() {
    return bezeichnungDe;
  }

  @JsonProperty("bezeichnungDe")
  public void setBezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
  }

  /**
   **/
  public GesuchsjahrCreateDto bezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
    return this;
  }


  @JsonProperty("bezeichnungFr")
  @NotNull
  public String getBezeichnungFr() {
    return bezeichnungFr;
  }

  @JsonProperty("bezeichnungFr")
  public void setBezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
  }

  /**
   **/
  public GesuchsjahrCreateDto technischesJahr(Integer technischesJahr) {
    this.technischesJahr = technischesJahr;
    return this;
  }


  @JsonProperty("technischesJahr")
  @NotNull
  public Integer getTechnischesJahr() {
    return technischesJahr;
  }

  @JsonProperty("technischesJahr")
  public void setTechnischesJahr(Integer technischesJahr) {
    this.technischesJahr = technischesJahr;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchsjahrCreateDto gesuchsjahrCreate = (GesuchsjahrCreateDto) o;
    return Objects.equals(this.bezeichnungDe, gesuchsjahrCreate.bezeichnungDe) &&
        Objects.equals(this.bezeichnungFr, gesuchsjahrCreate.bezeichnungFr) &&
        Objects.equals(this.technischesJahr, gesuchsjahrCreate.technischesJahr);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bezeichnungDe, bezeichnungFr, technischesJahr);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchsjahrCreateDto {\n");

    sb.append("    bezeichnungDe: ").append(toIndentedString(bezeichnungDe)).append("\n");
    sb.append("    bezeichnungFr: ").append(toIndentedString(bezeichnungFr)).append("\n");
    sb.append("    technischesJahr: ").append(toIndentedString(technischesJahr)).append("\n");
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

