package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.FallDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeDto;
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



@JsonTypeName("Gesuch")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchDto  implements Serializable {
  private @Valid FallDto fall;
  private @Valid GesuchsperiodeDto gesuchsperiode;
  private @Valid ch.dvbern.stip.api.gesuch.type.Gesuchstatus gesuchStatus;
  private @Valid Integer gesuchNummer;
  private @Valid UUID id;
  private @Valid LocalDate aenderungsdatum;
  private @Valid GesuchTrancheDto gesuchTrancheToWorkWith;
  private @Valid String bearbeiter;

  /**
   **/
  public GesuchDto fall(FallDto fall) {
    this.fall = fall;
    return this;
  }

  
  @JsonProperty("fall")
  @NotNull
  public FallDto getFall() {
    return fall;
  }

  @JsonProperty("fall")
  public void setFall(FallDto fall) {
    this.fall = fall;
  }

  /**
   **/
  public GesuchDto gesuchsperiode(GesuchsperiodeDto gesuchsperiode) {
    this.gesuchsperiode = gesuchsperiode;
    return this;
  }

  
  @JsonProperty("gesuchsperiode")
  @NotNull
  public GesuchsperiodeDto getGesuchsperiode() {
    return gesuchsperiode;
  }

  @JsonProperty("gesuchsperiode")
  public void setGesuchsperiode(GesuchsperiodeDto gesuchsperiode) {
    this.gesuchsperiode = gesuchsperiode;
  }

  /**
   **/
  public GesuchDto gesuchStatus(ch.dvbern.stip.api.gesuch.type.Gesuchstatus gesuchStatus) {
    this.gesuchStatus = gesuchStatus;
    return this;
  }

  
  @JsonProperty("gesuchStatus")
  @NotNull
  public ch.dvbern.stip.api.gesuch.type.Gesuchstatus getGesuchStatus() {
    return gesuchStatus;
  }

  @JsonProperty("gesuchStatus")
  public void setGesuchStatus(ch.dvbern.stip.api.gesuch.type.Gesuchstatus gesuchStatus) {
    this.gesuchStatus = gesuchStatus;
  }

  /**
   **/
  public GesuchDto gesuchNummer(Integer gesuchNummer) {
    this.gesuchNummer = gesuchNummer;
    return this;
  }

  
  @JsonProperty("gesuchNummer")
  @NotNull
  public Integer getGesuchNummer() {
    return gesuchNummer;
  }

  @JsonProperty("gesuchNummer")
  public void setGesuchNummer(Integer gesuchNummer) {
    this.gesuchNummer = gesuchNummer;
  }

  /**
   **/
  public GesuchDto id(UUID id) {
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
  public GesuchDto aenderungsdatum(LocalDate aenderungsdatum) {
    this.aenderungsdatum = aenderungsdatum;
    return this;
  }

  
  @JsonProperty("aenderungsdatum")
  @NotNull
  public LocalDate getAenderungsdatum() {
    return aenderungsdatum;
  }

  @JsonProperty("aenderungsdatum")
  public void setAenderungsdatum(LocalDate aenderungsdatum) {
    this.aenderungsdatum = aenderungsdatum;
  }

  /**
   **/
  public GesuchDto gesuchTrancheToWorkWith(GesuchTrancheDto gesuchTrancheToWorkWith) {
    this.gesuchTrancheToWorkWith = gesuchTrancheToWorkWith;
    return this;
  }

  
  @JsonProperty("gesuchTrancheToWorkWith")
  @NotNull
  public GesuchTrancheDto getGesuchTrancheToWorkWith() {
    return gesuchTrancheToWorkWith;
  }

  @JsonProperty("gesuchTrancheToWorkWith")
  public void setGesuchTrancheToWorkWith(GesuchTrancheDto gesuchTrancheToWorkWith) {
    this.gesuchTrancheToWorkWith = gesuchTrancheToWorkWith;
  }

  /**
   * Zuständiger Sachbearbeiter des Gesuchs
   **/
  public GesuchDto bearbeiter(String bearbeiter) {
    this.bearbeiter = bearbeiter;
    return this;
  }

  
  @JsonProperty("bearbeiter")
  public String getBearbeiter() {
    return bearbeiter;
  }

  @JsonProperty("bearbeiter")
  public void setBearbeiter(String bearbeiter) {
    this.bearbeiter = bearbeiter;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchDto gesuch = (GesuchDto) o;
    return Objects.equals(this.fall, gesuch.fall) &&
        Objects.equals(this.gesuchsperiode, gesuch.gesuchsperiode) &&
        Objects.equals(this.gesuchStatus, gesuch.gesuchStatus) &&
        Objects.equals(this.gesuchNummer, gesuch.gesuchNummer) &&
        Objects.equals(this.id, gesuch.id) &&
        Objects.equals(this.aenderungsdatum, gesuch.aenderungsdatum) &&
        Objects.equals(this.gesuchTrancheToWorkWith, gesuch.gesuchTrancheToWorkWith) &&
        Objects.equals(this.bearbeiter, gesuch.bearbeiter);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fall, gesuchsperiode, gesuchStatus, gesuchNummer, id, aenderungsdatum, gesuchTrancheToWorkWith, bearbeiter);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchDto {\n");
    
    sb.append("    fall: ").append(toIndentedString(fall)).append("\n");
    sb.append("    gesuchsperiode: ").append(toIndentedString(gesuchsperiode)).append("\n");
    sb.append("    gesuchStatus: ").append(toIndentedString(gesuchStatus)).append("\n");
    sb.append("    gesuchNummer: ").append(toIndentedString(gesuchNummer)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    aenderungsdatum: ").append(toIndentedString(aenderungsdatum)).append("\n");
    sb.append("    gesuchTrancheToWorkWith: ").append(toIndentedString(gesuchTrancheToWorkWith)).append("\n");
    sb.append("    bearbeiter: ").append(toIndentedString(bearbeiter)).append("\n");
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

