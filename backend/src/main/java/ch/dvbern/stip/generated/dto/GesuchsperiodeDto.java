package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.GesuchsperiodenstatusDto;
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



@JsonTypeName("Gesuchsperiode")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchsperiodeDto  implements Serializable {
  private @Valid UUID id;
  private @Valid LocalDate gueltigAb;
  private @Valid LocalDate gueltigBis;
  private @Valid String bezeichnungDe;
  private @Valid String bezeichnungFr;
  private @Valid GesuchsperiodenstatusDto status;
  private @Valid LocalDate einreichfrist;
  private @Valid LocalDate aufschaltdatum;

  /**
   **/
  public GesuchsperiodeDto id(UUID id) {
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
  public GesuchsperiodeDto gueltigAb(LocalDate gueltigAb) {
    this.gueltigAb = gueltigAb;
    return this;
  }

  
  @JsonProperty("gueltigAb")
  @NotNull
  public LocalDate getGueltigAb() {
    return gueltigAb;
  }

  @JsonProperty("gueltigAb")
  public void setGueltigAb(LocalDate gueltigAb) {
    this.gueltigAb = gueltigAb;
  }

  /**
   **/
  public GesuchsperiodeDto gueltigBis(LocalDate gueltigBis) {
    this.gueltigBis = gueltigBis;
    return this;
  }

  
  @JsonProperty("gueltigBis")
  @NotNull
  public LocalDate getGueltigBis() {
    return gueltigBis;
  }

  @JsonProperty("gueltigBis")
  public void setGueltigBis(LocalDate gueltigBis) {
    this.gueltigBis = gueltigBis;
  }

  /**
   **/
  public GesuchsperiodeDto bezeichnungDe(String bezeichnungDe) {
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
  public GesuchsperiodeDto bezeichnungFr(String bezeichnungFr) {
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
  public GesuchsperiodeDto status(GesuchsperiodenstatusDto status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  @NotNull
  public GesuchsperiodenstatusDto getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(GesuchsperiodenstatusDto status) {
    this.status = status;
  }

  /**
   **/
  public GesuchsperiodeDto einreichfrist(LocalDate einreichfrist) {
    this.einreichfrist = einreichfrist;
    return this;
  }

  
  @JsonProperty("einreichfrist")
  public LocalDate getEinreichfrist() {
    return einreichfrist;
  }

  @JsonProperty("einreichfrist")
  public void setEinreichfrist(LocalDate einreichfrist) {
    this.einreichfrist = einreichfrist;
  }

  /**
   **/
  public GesuchsperiodeDto aufschaltdatum(LocalDate aufschaltdatum) {
    this.aufschaltdatum = aufschaltdatum;
    return this;
  }

  
  @JsonProperty("aufschaltdatum")
  public LocalDate getAufschaltdatum() {
    return aufschaltdatum;
  }

  @JsonProperty("aufschaltdatum")
  public void setAufschaltdatum(LocalDate aufschaltdatum) {
    this.aufschaltdatum = aufschaltdatum;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchsperiodeDto gesuchsperiode = (GesuchsperiodeDto) o;
    return Objects.equals(this.id, gesuchsperiode.id) &&
        Objects.equals(this.gueltigAb, gesuchsperiode.gueltigAb) &&
        Objects.equals(this.gueltigBis, gesuchsperiode.gueltigBis) &&
        Objects.equals(this.bezeichnungDe, gesuchsperiode.bezeichnungDe) &&
        Objects.equals(this.bezeichnungFr, gesuchsperiode.bezeichnungFr) &&
        Objects.equals(this.status, gesuchsperiode.status) &&
        Objects.equals(this.einreichfrist, gesuchsperiode.einreichfrist) &&
        Objects.equals(this.aufschaltdatum, gesuchsperiode.aufschaltdatum);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, gueltigAb, gueltigBis, bezeichnungDe, bezeichnungFr, status, einreichfrist, aufschaltdatum);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchsperiodeDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    gueltigAb: ").append(toIndentedString(gueltigAb)).append("\n");
    sb.append("    gueltigBis: ").append(toIndentedString(gueltigBis)).append("\n");
    sb.append("    bezeichnungDe: ").append(toIndentedString(bezeichnungDe)).append("\n");
    sb.append("    bezeichnungFr: ").append(toIndentedString(bezeichnungFr)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    einreichfrist: ").append(toIndentedString(einreichfrist)).append("\n");
    sb.append("    aufschaltdatum: ").append(toIndentedString(aufschaltdatum)).append("\n");
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

