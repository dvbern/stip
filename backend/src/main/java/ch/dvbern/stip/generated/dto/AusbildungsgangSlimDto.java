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



@JsonTypeName("AusbildungsgangSlim")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AusbildungsgangSlimDto  implements Serializable {
  private @Valid UUID id;
  private @Valid String bezeichnungDe;
  private @Valid String bezeichnungFr;
  private @Valid ch.dvbern.stip.api.ausbildung.type.Bildungskategorie bildungskategorie;
  private @Valid ch.dvbern.stip.api.ausbildung.type.AbschlussZusatzfrage zusatzfrage;
  private @Valid Boolean askForBerufsmaturitaet;

  /**
   **/
  public AusbildungsgangSlimDto id(UUID id) {
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
  public AusbildungsgangSlimDto bezeichnungDe(String bezeichnungDe) {
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
  public AusbildungsgangSlimDto bezeichnungFr(String bezeichnungFr) {
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
  public AusbildungsgangSlimDto bildungskategorie(ch.dvbern.stip.api.ausbildung.type.Bildungskategorie bildungskategorie) {
    this.bildungskategorie = bildungskategorie;
    return this;
  }

  
  @JsonProperty("bildungskategorie")
  @NotNull
  public ch.dvbern.stip.api.ausbildung.type.Bildungskategorie getBildungskategorie() {
    return bildungskategorie;
  }

  @JsonProperty("bildungskategorie")
  public void setBildungskategorie(ch.dvbern.stip.api.ausbildung.type.Bildungskategorie bildungskategorie) {
    this.bildungskategorie = bildungskategorie;
  }

  /**
   **/
  public AusbildungsgangSlimDto zusatzfrage(ch.dvbern.stip.api.ausbildung.type.AbschlussZusatzfrage zusatzfrage) {
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

  /**
   **/
  public AusbildungsgangSlimDto askForBerufsmaturitaet(Boolean askForBerufsmaturitaet) {
    this.askForBerufsmaturitaet = askForBerufsmaturitaet;
    return this;
  }

  
  @JsonProperty("askForBerufsmaturitaet")
  public Boolean getAskForBerufsmaturitaet() {
    return askForBerufsmaturitaet;
  }

  @JsonProperty("askForBerufsmaturitaet")
  public void setAskForBerufsmaturitaet(Boolean askForBerufsmaturitaet) {
    this.askForBerufsmaturitaet = askForBerufsmaturitaet;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AusbildungsgangSlimDto ausbildungsgangSlim = (AusbildungsgangSlimDto) o;
    return Objects.equals(this.id, ausbildungsgangSlim.id) &&
        Objects.equals(this.bezeichnungDe, ausbildungsgangSlim.bezeichnungDe) &&
        Objects.equals(this.bezeichnungFr, ausbildungsgangSlim.bezeichnungFr) &&
        Objects.equals(this.bildungskategorie, ausbildungsgangSlim.bildungskategorie) &&
        Objects.equals(this.zusatzfrage, ausbildungsgangSlim.zusatzfrage) &&
        Objects.equals(this.askForBerufsmaturitaet, ausbildungsgangSlim.askForBerufsmaturitaet);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, bezeichnungDe, bezeichnungFr, bildungskategorie, zusatzfrage, askForBerufsmaturitaet);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungsgangSlimDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    bezeichnungDe: ").append(toIndentedString(bezeichnungDe)).append("\n");
    sb.append("    bezeichnungFr: ").append(toIndentedString(bezeichnungFr)).append("\n");
    sb.append("    bildungskategorie: ").append(toIndentedString(bildungskategorie)).append("\n");
    sb.append("    zusatzfrage: ").append(toIndentedString(zusatzfrage)).append("\n");
    sb.append("    askForBerufsmaturitaet: ").append(toIndentedString(askForBerufsmaturitaet)).append("\n");
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

