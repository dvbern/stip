package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;

import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Eintrag von dem Statusprotokoll
 **/

@JsonTypeName("StatusprotokollEntry")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class StatusprotokollEntryDto  implements Serializable {
  private @Valid java.time.LocalDateTime timestamp;
  private @Valid Gesuchstatus status;
  private @Valid String benutzer;
  private @Valid String kommentar;

  /**
   * Datum und Zeit vom wechsel zu status, ISO 8601 formatiert
   **/
  public StatusprotokollEntryDto timestamp(java.time.LocalDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }


  @JsonProperty("timestamp")
  public java.time.LocalDateTime getTimestamp() {
    return timestamp;
  }

  @JsonProperty("timestamp")
  public void setTimestamp(java.time.LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  /**
   **/
  public StatusprotokollEntryDto status(Gesuchstatus status) {
    this.status = status;
    return this;
  }


  @JsonProperty("status")
  public Gesuchstatus getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(Gesuchstatus status) {
    this.status = status;
  }

  /**
   * Der Benutzer welcher den Statuswechsel ausgelöst hat
   **/
  public StatusprotokollEntryDto benutzer(String benutzer) {
    this.benutzer = benutzer;
    return this;
  }


  @JsonProperty("benutzer")
  public String getBenutzer() {
    return benutzer;
  }

  @JsonProperty("benutzer")
  public void setBenutzer(String benutzer) {
    this.benutzer = benutzer;
  }

  /**
   * Kommentar vom SB über die Statusänderung
   **/
  public StatusprotokollEntryDto kommentar(String kommentar) {
    this.kommentar = kommentar;
    return this;
  }


  @JsonProperty("kommentar")
  public String getKommentar() {
    return kommentar;
  }

  @JsonProperty("kommentar")
  public void setKommentar(String kommentar) {
    this.kommentar = kommentar;
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
    return Objects.equals(this.timestamp, statusprotokollEntry.timestamp) &&
        Objects.equals(this.status, statusprotokollEntry.status) &&
        Objects.equals(this.benutzer, statusprotokollEntry.benutzer) &&
        Objects.equals(this.kommentar, statusprotokollEntry.kommentar);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, status, benutzer, kommentar);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatusprotokollEntryDto {\n");

    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    benutzer: ").append(toIndentedString(benutzer)).append("\n");
    sb.append("    kommentar: ").append(toIndentedString(kommentar)).append("\n");
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

