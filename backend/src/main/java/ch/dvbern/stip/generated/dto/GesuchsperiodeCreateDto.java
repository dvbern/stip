package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("GesuchsperiodeCreate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchsperiodeCreateDto  implements Serializable {
  private @Valid LocalDate gueltigAb;
  private @Valid LocalDate gueltigBis;
  private @Valid LocalDate einreichfrist;
  private @Valid LocalDate aufschaltdatum;

  /**
   **/
  public GesuchsperiodeCreateDto gueltigAb(LocalDate gueltigAb) {
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
  public GesuchsperiodeCreateDto gueltigBis(LocalDate gueltigBis) {
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
  public GesuchsperiodeCreateDto einreichfrist(LocalDate einreichfrist) {
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
  public GesuchsperiodeCreateDto aufschaltdatum(LocalDate aufschaltdatum) {
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
    GesuchsperiodeCreateDto gesuchsperiodeCreate = (GesuchsperiodeCreateDto) o;
    return Objects.equals(this.gueltigAb, gesuchsperiodeCreate.gueltigAb) &&
        Objects.equals(this.gueltigBis, gesuchsperiodeCreate.gueltigBis) &&
        Objects.equals(this.einreichfrist, gesuchsperiodeCreate.einreichfrist) &&
        Objects.equals(this.aufschaltdatum, gesuchsperiodeCreate.aufschaltdatum);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gueltigAb, gueltigBis, einreichfrist, aufschaltdatum);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchsperiodeCreateDto {\n");
    
    sb.append("    gueltigAb: ").append(toIndentedString(gueltigAb)).append("\n");
    sb.append("    gueltigBis: ").append(toIndentedString(gueltigBis)).append("\n");
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

