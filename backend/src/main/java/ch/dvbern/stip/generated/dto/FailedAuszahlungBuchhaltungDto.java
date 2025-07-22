package ch.dvbern.stip.generated.dto;

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



@JsonTypeName("FailedAuszahlungBuchhaltung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class FailedAuszahlungBuchhaltungDto  implements Serializable {
  private @Valid UUID fallId;
  private @Valid String fallNummer;
  private @Valid UUID gesuchId;
  private @Valid String gesuchNummer;
  private @Valid String name;
  private @Valid String vorname;
  private @Valid LocalDate lastTryDate;

  /**
   **/
  public FailedAuszahlungBuchhaltungDto fallId(UUID fallId) {
    this.fallId = fallId;
    return this;
  }

  
  @JsonProperty("fallId")
  @NotNull
  public UUID getFallId() {
    return fallId;
  }

  @JsonProperty("fallId")
  public void setFallId(UUID fallId) {
    this.fallId = fallId;
  }

  /**
   **/
  public FailedAuszahlungBuchhaltungDto fallNummer(String fallNummer) {
    this.fallNummer = fallNummer;
    return this;
  }

  
  @JsonProperty("fallNummer")
  @NotNull
  public String getFallNummer() {
    return fallNummer;
  }

  @JsonProperty("fallNummer")
  public void setFallNummer(String fallNummer) {
    this.fallNummer = fallNummer;
  }

  /**
   **/
  public FailedAuszahlungBuchhaltungDto gesuchId(UUID gesuchId) {
    this.gesuchId = gesuchId;
    return this;
  }

  
  @JsonProperty("gesuchId")
  @NotNull
  public UUID getGesuchId() {
    return gesuchId;
  }

  @JsonProperty("gesuchId")
  public void setGesuchId(UUID gesuchId) {
    this.gesuchId = gesuchId;
  }

  /**
   **/
  public FailedAuszahlungBuchhaltungDto gesuchNummer(String gesuchNummer) {
    this.gesuchNummer = gesuchNummer;
    return this;
  }

  
  @JsonProperty("gesuchNummer")
  @NotNull
  public String getGesuchNummer() {
    return gesuchNummer;
  }

  @JsonProperty("gesuchNummer")
  public void setGesuchNummer(String gesuchNummer) {
    this.gesuchNummer = gesuchNummer;
  }

  /**
   **/
  public FailedAuszahlungBuchhaltungDto name(String name) {
    this.name = name;
    return this;
  }

  
  @JsonProperty("name")
  @NotNull
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  public FailedAuszahlungBuchhaltungDto vorname(String vorname) {
    this.vorname = vorname;
    return this;
  }

  
  @JsonProperty("vorname")
  @NotNull
  public String getVorname() {
    return vorname;
  }

  @JsonProperty("vorname")
  public void setVorname(String vorname) {
    this.vorname = vorname;
  }

  /**
   **/
  public FailedAuszahlungBuchhaltungDto lastTryDate(LocalDate lastTryDate) {
    this.lastTryDate = lastTryDate;
    return this;
  }

  
  @JsonProperty("lastTryDate")
  @NotNull
  public LocalDate getLastTryDate() {
    return lastTryDate;
  }

  @JsonProperty("lastTryDate")
  public void setLastTryDate(LocalDate lastTryDate) {
    this.lastTryDate = lastTryDate;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FailedAuszahlungBuchhaltungDto failedAuszahlungBuchhaltung = (FailedAuszahlungBuchhaltungDto) o;
    return Objects.equals(this.fallId, failedAuszahlungBuchhaltung.fallId) &&
        Objects.equals(this.fallNummer, failedAuszahlungBuchhaltung.fallNummer) &&
        Objects.equals(this.gesuchId, failedAuszahlungBuchhaltung.gesuchId) &&
        Objects.equals(this.gesuchNummer, failedAuszahlungBuchhaltung.gesuchNummer) &&
        Objects.equals(this.name, failedAuszahlungBuchhaltung.name) &&
        Objects.equals(this.vorname, failedAuszahlungBuchhaltung.vorname) &&
        Objects.equals(this.lastTryDate, failedAuszahlungBuchhaltung.lastTryDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fallId, fallNummer, gesuchId, gesuchNummer, name, vorname, lastTryDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FailedAuszahlungBuchhaltungDto {\n");
    
    sb.append("    fallId: ").append(toIndentedString(fallId)).append("\n");
    sb.append("    fallNummer: ").append(toIndentedString(fallNummer)).append("\n");
    sb.append("    gesuchId: ").append(toIndentedString(gesuchId)).append("\n");
    sb.append("    gesuchNummer: ").append(toIndentedString(gesuchNummer)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    lastTryDate: ").append(toIndentedString(lastTryDate)).append("\n");
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

