package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.GesuchTrancheTypDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("GesuchTrancheSlim")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchTrancheSlimDto  implements Serializable {
  private @Valid UUID id;
  private @Valid LocalDate gueltigAb;
  private @Valid LocalDate gueltigBis;
  private @Valid ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus status;
  private @Valid GesuchTrancheTypDto typ;

  /**
   **/
  public GesuchTrancheSlimDto id(UUID id) {
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

  /**
   **/
  public GesuchTrancheSlimDto gueltigAb(LocalDate gueltigAb) {
    this.gueltigAb = gueltigAb;
    return this;
  }

  
  @JsonProperty("gueltigAb")
  public LocalDate getGueltigAb() {
    return gueltigAb;
  }

  @JsonProperty("gueltigAb")
  public void setGueltigAb(LocalDate gueltigAb) {
    this.gueltigAb = gueltigAb;
  }

  /**
   **/
  public GesuchTrancheSlimDto gueltigBis(LocalDate gueltigBis) {
    this.gueltigBis = gueltigBis;
    return this;
  }

  
  @JsonProperty("gueltigBis")
  public LocalDate getGueltigBis() {
    return gueltigBis;
  }

  @JsonProperty("gueltigBis")
  public void setGueltigBis(LocalDate gueltigBis) {
    this.gueltigBis = gueltigBis;
  }

  /**
   **/
  public GesuchTrancheSlimDto status(ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  public ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(ch.dvbern.stip.api.gesuch.type.GesuchTrancheStatus status) {
    this.status = status;
  }

  /**
   **/
  public GesuchTrancheSlimDto typ(GesuchTrancheTypDto typ) {
    this.typ = typ;
    return this;
  }

  
  @JsonProperty("typ")
  public GesuchTrancheTypDto getTyp() {
    return typ;
  }

  @JsonProperty("typ")
  public void setTyp(GesuchTrancheTypDto typ) {
    this.typ = typ;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchTrancheSlimDto gesuchTrancheSlim = (GesuchTrancheSlimDto) o;
    return Objects.equals(this.id, gesuchTrancheSlim.id) &&
        Objects.equals(this.gueltigAb, gesuchTrancheSlim.gueltigAb) &&
        Objects.equals(this.gueltigBis, gesuchTrancheSlim.gueltigBis) &&
        Objects.equals(this.status, gesuchTrancheSlim.status) &&
        Objects.equals(this.typ, gesuchTrancheSlim.typ);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, gueltigAb, gueltigBis, status, typ);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchTrancheSlimDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    gueltigAb: ").append(toIndentedString(gueltigAb)).append("\n");
    sb.append("    gueltigBis: ").append(toIndentedString(gueltigBis)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    typ: ").append(toIndentedString(typ)).append("\n");
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

