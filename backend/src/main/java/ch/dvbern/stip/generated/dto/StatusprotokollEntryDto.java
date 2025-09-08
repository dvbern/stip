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
  private @Valid java.time.LocalDateTime timestamp;
  private @Valid String statusTo;
  private @Valid ch.dvbern.stip.api.statusprotokoll.type.StatusprotokollEntryTyp typ;
  private @Valid String benutzer;
  private @Valid String kommentar;
  private @Valid String statusFrom;

  /**
   * Datum und Zeit vom wechsel zu status, ISO 8601 formatiert
   **/
  public StatusprotokollEntryDto timestamp(java.time.LocalDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  
  @JsonProperty("timestamp")
  @NotNull
  public java.time.LocalDateTime getTimestamp() {
    return timestamp;
  }

  @JsonProperty("timestamp")
  public void setTimestamp(java.time.LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  /**
   **/
  public StatusprotokollEntryDto statusTo(String statusTo) {
    this.statusTo = statusTo;
    return this;
  }

  
  @JsonProperty("statusTo")
  @NotNull
  public String getStatusTo() {
    return statusTo;
  }

  @JsonProperty("statusTo")
  public void setStatusTo(String statusTo) {
    this.statusTo = statusTo;
  }

  /**
   **/
  public StatusprotokollEntryDto typ(ch.dvbern.stip.api.statusprotokoll.type.StatusprotokollEntryTyp typ) {
    this.typ = typ;
    return this;
  }

  
  @JsonProperty("typ")
  @NotNull
  public ch.dvbern.stip.api.statusprotokoll.type.StatusprotokollEntryTyp getTyp() {
    return typ;
  }

  @JsonProperty("typ")
  public void setTyp(ch.dvbern.stip.api.statusprotokoll.type.StatusprotokollEntryTyp typ) {
    this.typ = typ;
  }

  /**
   * Der Benutzer welcher den Statuswechsel ausgelöst hat
   **/
  public StatusprotokollEntryDto benutzer(String benutzer) {
    this.benutzer = benutzer;
    return this;
  }

  
  @JsonProperty("benutzer")
  @NotNull
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
  @NotNull
  public String getKommentar() {
    return kommentar;
  }

  @JsonProperty("kommentar")
  public void setKommentar(String kommentar) {
    this.kommentar = kommentar;
  }

  /**
   **/
  public StatusprotokollEntryDto statusFrom(String statusFrom) {
    this.statusFrom = statusFrom;
    return this;
  }

  
  @JsonProperty("statusFrom")
  public String getStatusFrom() {
    return statusFrom;
  }

  @JsonProperty("statusFrom")
  public void setStatusFrom(String statusFrom) {
    this.statusFrom = statusFrom;
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
        Objects.equals(this.statusTo, statusprotokollEntry.statusTo) &&
        Objects.equals(this.typ, statusprotokollEntry.typ) &&
        Objects.equals(this.benutzer, statusprotokollEntry.benutzer) &&
        Objects.equals(this.kommentar, statusprotokollEntry.kommentar) &&
        Objects.equals(this.statusFrom, statusprotokollEntry.statusFrom);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, statusTo, typ, benutzer, kommentar, statusFrom);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatusprotokollEntryDto {\n");
    
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    statusTo: ").append(toIndentedString(statusTo)).append("\n");
    sb.append("    typ: ").append(toIndentedString(typ)).append("\n");
    sb.append("    benutzer: ").append(toIndentedString(benutzer)).append("\n");
    sb.append("    kommentar: ").append(toIndentedString(kommentar)).append("\n");
    sb.append("    statusFrom: ").append(toIndentedString(statusFrom)).append("\n");
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

