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



@JsonTypeName("Ausbildungsgang")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AusbildungsgangDto  implements Serializable {
  private @Valid UUID id;
  private @Valid ch.dvbern.stip.api.common.type.Bildungsart ausbildungsrichtung;
  private @Valid String ausbildungsort;
  private @Valid String bezeichnungDe;
  private @Valid String bezeichnungFr;
  private @Valid UUID ausbildungsstaetteId;

  /**
   **/
  public AusbildungsgangDto id(UUID id) {
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
  public AusbildungsgangDto ausbildungsrichtung(ch.dvbern.stip.api.common.type.Bildungsart ausbildungsrichtung) {
    this.ausbildungsrichtung = ausbildungsrichtung;
    return this;
  }

  
  @JsonProperty("ausbildungsrichtung")
  @NotNull
  public ch.dvbern.stip.api.common.type.Bildungsart getAusbildungsrichtung() {
    return ausbildungsrichtung;
  }

  @JsonProperty("ausbildungsrichtung")
  public void setAusbildungsrichtung(ch.dvbern.stip.api.common.type.Bildungsart ausbildungsrichtung) {
    this.ausbildungsrichtung = ausbildungsrichtung;
  }

  /**
   * 
   **/
  public AusbildungsgangDto ausbildungsort(String ausbildungsort) {
    this.ausbildungsort = ausbildungsort;
    return this;
  }

  
  @JsonProperty("ausbildungsort")
  @NotNull
  public String getAusbildungsort() {
    return ausbildungsort;
  }

  @JsonProperty("ausbildungsort")
  public void setAusbildungsort(String ausbildungsort) {
    this.ausbildungsort = ausbildungsort;
  }

  /**
   * 
   **/
  public AusbildungsgangDto bezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
    return this;
  }

  
  @JsonProperty("bezeichnungDe")
  public String getBezeichnungDe() {
    return bezeichnungDe;
  }

  @JsonProperty("bezeichnungDe")
  public void setBezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
  }

  /**
   * 
   **/
  public AusbildungsgangDto bezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
    return this;
  }

  
  @JsonProperty("bezeichnungFr")
  public String getBezeichnungFr() {
    return bezeichnungFr;
  }

  @JsonProperty("bezeichnungFr")
  public void setBezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
  }

  /**
   **/
  public AusbildungsgangDto ausbildungsstaetteId(UUID ausbildungsstaetteId) {
    this.ausbildungsstaetteId = ausbildungsstaetteId;
    return this;
  }

  
  @JsonProperty("ausbildungsstaetteId")
  public UUID getAusbildungsstaetteId() {
    return ausbildungsstaetteId;
  }

  @JsonProperty("ausbildungsstaetteId")
  public void setAusbildungsstaetteId(UUID ausbildungsstaetteId) {
    this.ausbildungsstaetteId = ausbildungsstaetteId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AusbildungsgangDto ausbildungsgang = (AusbildungsgangDto) o;
    return Objects.equals(this.id, ausbildungsgang.id) &&
        Objects.equals(this.ausbildungsrichtung, ausbildungsgang.ausbildungsrichtung) &&
        Objects.equals(this.ausbildungsort, ausbildungsgang.ausbildungsort) &&
        Objects.equals(this.bezeichnungDe, ausbildungsgang.bezeichnungDe) &&
        Objects.equals(this.bezeichnungFr, ausbildungsgang.bezeichnungFr) &&
        Objects.equals(this.ausbildungsstaetteId, ausbildungsgang.ausbildungsstaetteId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, ausbildungsrichtung, ausbildungsort, bezeichnungDe, bezeichnungFr, ausbildungsstaetteId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungsgangDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    ausbildungsrichtung: ").append(toIndentedString(ausbildungsrichtung)).append("\n");
    sb.append("    ausbildungsort: ").append(toIndentedString(ausbildungsort)).append("\n");
    sb.append("    bezeichnungDe: ").append(toIndentedString(bezeichnungDe)).append("\n");
    sb.append("    bezeichnungFr: ").append(toIndentedString(bezeichnungFr)).append("\n");
    sb.append("    ausbildungsstaetteId: ").append(toIndentedString(ausbildungsstaetteId)).append("\n");
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

