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



@JsonTypeName("AbschlussSlim")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AbschlussSlimDto  implements Serializable {
  private @Valid UUID id;
  private @Valid String bezeichnungDe;
  private @Valid String bezeichnungFr;
  private @Valid ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie ausbildungskategorie;
  private @Valid ch.dvbern.stip.api.ausbildung.type.AbschlussZusatzfrage zusatzfrage;

  /**
   **/
  public AbschlussSlimDto id(UUID id) {
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
  public AbschlussSlimDto bezeichnungDe(String bezeichnungDe) {
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
  public AbschlussSlimDto bezeichnungFr(String bezeichnungFr) {
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
  public AbschlussSlimDto ausbildungskategorie(ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie ausbildungskategorie) {
    this.ausbildungskategorie = ausbildungskategorie;
    return this;
  }

  
  @JsonProperty("ausbildungskategorie")
  @NotNull
  public ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie getAusbildungskategorie() {
    return ausbildungskategorie;
  }

  @JsonProperty("ausbildungskategorie")
  public void setAusbildungskategorie(ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie ausbildungskategorie) {
    this.ausbildungskategorie = ausbildungskategorie;
  }

  /**
   **/
  public AbschlussSlimDto zusatzfrage(ch.dvbern.stip.api.ausbildung.type.AbschlussZusatzfrage zusatzfrage) {
    this.zusatzfrage = zusatzfrage;
    return this;
  }

  
  @JsonProperty("zusatzfrage")
  public ch.dvbern.stip.api.ausbildung.type.AbschlussZusatzfrage getZusatzfrage() {
    return zusatzfrage;
  }

  @JsonProperty("zusatzfrage")
  public void setZusatzfrage(ch.dvbern.stip.api.ausbildung.type.AbschlussZusatzfrage zusatzfrage) {
    this.zusatzfrage = zusatzfrage;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbschlussSlimDto abschlussSlim = (AbschlussSlimDto) o;
    return Objects.equals(this.id, abschlussSlim.id) &&
        Objects.equals(this.bezeichnungDe, abschlussSlim.bezeichnungDe) &&
        Objects.equals(this.bezeichnungFr, abschlussSlim.bezeichnungFr) &&
        Objects.equals(this.ausbildungskategorie, abschlussSlim.ausbildungskategorie) &&
        Objects.equals(this.zusatzfrage, abschlussSlim.zusatzfrage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, bezeichnungDe, bezeichnungFr, ausbildungskategorie, zusatzfrage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbschlussSlimDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    bezeichnungDe: ").append(toIndentedString(bezeichnungDe)).append("\n");
    sb.append("    bezeichnungFr: ").append(toIndentedString(bezeichnungFr)).append("\n");
    sb.append("    ausbildungskategorie: ").append(toIndentedString(ausbildungskategorie)).append("\n");
    sb.append("    zusatzfrage: ").append(toIndentedString(zusatzfrage)).append("\n");
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

