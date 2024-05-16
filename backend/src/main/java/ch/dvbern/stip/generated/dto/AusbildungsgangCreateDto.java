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

/**
 * 
 **/

@JsonTypeName("AusbildungsgangCreate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AusbildungsgangCreateDto  implements Serializable {
  private @Valid String bezeichnungDe;
  private @Valid String bezeichnungFr;
  private @Valid ch.dvbern.stip.api.common.type.Bildungsart ausbildungsrichtung;
  private @Valid UUID ausbildungsstaetteId;

  /**
   * 
   **/
  public AusbildungsgangCreateDto bezeichnungDe(String bezeichnungDe) {
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
   * 
   **/
  public AusbildungsgangCreateDto bezeichnungFr(String bezeichnungFr) {
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
  public AusbildungsgangCreateDto ausbildungsrichtung(ch.dvbern.stip.api.common.type.Bildungsart ausbildungsrichtung) {
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
   **/
  public AusbildungsgangCreateDto ausbildungsstaetteId(UUID ausbildungsstaetteId) {
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
    AusbildungsgangCreateDto ausbildungsgangCreate = (AusbildungsgangCreateDto) o;
    return Objects.equals(this.bezeichnungDe, ausbildungsgangCreate.bezeichnungDe) &&
        Objects.equals(this.bezeichnungFr, ausbildungsgangCreate.bezeichnungFr) &&
        Objects.equals(this.ausbildungsrichtung, ausbildungsgangCreate.ausbildungsrichtung) &&
        Objects.equals(this.ausbildungsstaetteId, ausbildungsgangCreate.ausbildungsstaetteId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bezeichnungDe, bezeichnungFr, ausbildungsrichtung, ausbildungsstaetteId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungsgangCreateDto {\n");
    
    sb.append("    bezeichnungDe: ").append(toIndentedString(bezeichnungDe)).append("\n");
    sb.append("    bezeichnungFr: ").append(toIndentedString(bezeichnungFr)).append("\n");
    sb.append("    ausbildungsrichtung: ").append(toIndentedString(ausbildungsrichtung)).append("\n");
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

