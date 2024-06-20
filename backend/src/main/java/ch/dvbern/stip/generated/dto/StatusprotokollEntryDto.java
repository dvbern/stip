package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Eintrag von dem Statusprotokoll
 **/

@JsonTypeName("StatusprotokollEntry")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class StatusprotokollEntryDto  implements Serializable {
  private @Valid java.time.LocalDateTime stichdatum;
  private @Valid ch.dvbern.stip.api.gesuch.type.Gesuchstatus status;

  /**
   * Datum vom wechsel zu status
   **/
  public StatusprotokollEntryDto stichdatum(java.time.LocalDateTime stichdatum) {
    this.stichdatum = stichdatum;
    return this;
  }

  
  @JsonProperty("stichdatum")
  public java.time.LocalDateTime getStichdatum() {
    return stichdatum;
  }

  @JsonProperty("stichdatum")
  public void setStichdatum(java.time.LocalDateTime stichdatum) {
    this.stichdatum = stichdatum;
  }

  /**
   **/
  public StatusprotokollEntryDto status(ch.dvbern.stip.api.gesuch.type.Gesuchstatus status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  public ch.dvbern.stip.api.gesuch.type.Gesuchstatus getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(ch.dvbern.stip.api.gesuch.type.Gesuchstatus status) {
    this.status = status;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatusprotokollEntryDto statusprotokollEntry = (StatusprotokollEntryDto) o;
    return Objects.equals(this.stichdatum, statusprotokollEntry.stichdatum) &&
        Objects.equals(this.status, statusprotokollEntry.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(stichdatum, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatusprotokollEntryDto {\n");
    
    sb.append("    stichdatum: ").append(toIndentedString(stichdatum)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

