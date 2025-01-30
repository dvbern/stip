package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DelegierungDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("GesuchWithChanges")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchWithChangesDto  implements Serializable {
  private @Valid UUID fallId;
  private @Valid String fallNummer;
  private @Valid UUID ausbildungId;
  private @Valid GesuchsperiodeDto gesuchsperiode;
  private @Valid ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus;
  private @Valid String gesuchNummer;
  private @Valid UUID id;
  private @Valid LocalDate aenderungsdatum;
  private @Valid GesuchTrancheDto gesuchTrancheToWorkWith;
  private @Valid String bearbeiter;
  private @Valid String einreichedatum;
  private @Valid DelegierungDto delegierung;
  private @Valid List<GesuchTrancheDto> changes;

  /**
   **/
  public GesuchWithChangesDto fallId(UUID fallId) {
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
  public GesuchWithChangesDto fallNummer(String fallNummer) {
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
  public GesuchWithChangesDto ausbildungId(UUID ausbildungId) {
    this.ausbildungId = ausbildungId;
    return this;
  }

  
  @JsonProperty("ausbildungId")
  @NotNull
  public UUID getAusbildungId() {
    return ausbildungId;
  }

  @JsonProperty("ausbildungId")
  public void setAusbildungId(UUID ausbildungId) {
    this.ausbildungId = ausbildungId;
  }

  /**
   **/
  public GesuchWithChangesDto gesuchsperiode(GesuchsperiodeDto gesuchsperiode) {
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
  public GesuchWithChangesDto gesuchStatus(ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus) {
    this.gesuchStatus = gesuchStatus;
    return this;
  }

  
  @JsonProperty("gesuchStatus")
  @NotNull
  public ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus getGesuchStatus() {
    return gesuchStatus;
  }

  @JsonProperty("gesuchStatus")
  public void setGesuchStatus(ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus) {
    this.gesuchStatus = gesuchStatus;
  }

  /**
   **/
  public GesuchWithChangesDto gesuchNummer(String gesuchNummer) {
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
  public GesuchWithChangesDto id(UUID id) {
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
  public GesuchWithChangesDto aenderungsdatum(LocalDate aenderungsdatum) {
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
  public GesuchWithChangesDto gesuchTrancheToWorkWith(GesuchTrancheDto gesuchTrancheToWorkWith) {
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
  public GesuchWithChangesDto bearbeiter(String bearbeiter) {
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

  /**
   **/
  public GesuchWithChangesDto einreichedatum(String einreichedatum) {
    this.einreichedatum = einreichedatum;
    return this;
  }

  
  @JsonProperty("einreichedatum")
  public String getEinreichedatum() {
    return einreichedatum;
  }

  @JsonProperty("einreichedatum")
  public void setEinreichedatum(String einreichedatum) {
    this.einreichedatum = einreichedatum;
  }

  /**
   **/
  public GesuchWithChangesDto delegierung(DelegierungDto delegierung) {
    this.delegierung = delegierung;
    return this;
  }

  
  @JsonProperty("delegierung")
  public DelegierungDto getDelegierung() {
    return delegierung;
  }

  @JsonProperty("delegierung")
  public void setDelegierung(DelegierungDto delegierung) {
    this.delegierung = delegierung;
  }

  /**
   **/
  public GesuchWithChangesDto changes(List<GesuchTrancheDto> changes) {
    this.changes = changes;
    return this;
  }

  
  @JsonProperty("changes")
  public List<GesuchTrancheDto> getChanges() {
    return changes;
  }

  @JsonProperty("changes")
  public void setChanges(List<GesuchTrancheDto> changes) {
    this.changes = changes;
  }

  public GesuchWithChangesDto addChangesItem(GesuchTrancheDto changesItem) {
    if (this.changes == null) {
      this.changes = new ArrayList<>();
    }

    this.changes.add(changesItem);
    return this;
  }

  public GesuchWithChangesDto removeChangesItem(GesuchTrancheDto changesItem) {
    if (changesItem != null && this.changes != null) {
      this.changes.remove(changesItem);
    }

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchWithChangesDto gesuchWithChanges = (GesuchWithChangesDto) o;
    return Objects.equals(this.fallId, gesuchWithChanges.fallId) &&
        Objects.equals(this.fallNummer, gesuchWithChanges.fallNummer) &&
        Objects.equals(this.ausbildungId, gesuchWithChanges.ausbildungId) &&
        Objects.equals(this.gesuchsperiode, gesuchWithChanges.gesuchsperiode) &&
        Objects.equals(this.gesuchStatus, gesuchWithChanges.gesuchStatus) &&
        Objects.equals(this.gesuchNummer, gesuchWithChanges.gesuchNummer) &&
        Objects.equals(this.id, gesuchWithChanges.id) &&
        Objects.equals(this.aenderungsdatum, gesuchWithChanges.aenderungsdatum) &&
        Objects.equals(this.gesuchTrancheToWorkWith, gesuchWithChanges.gesuchTrancheToWorkWith) &&
        Objects.equals(this.bearbeiter, gesuchWithChanges.bearbeiter) &&
        Objects.equals(this.einreichedatum, gesuchWithChanges.einreichedatum) &&
        Objects.equals(this.delegierung, gesuchWithChanges.delegierung) &&
        Objects.equals(this.changes, gesuchWithChanges.changes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fallId, fallNummer, ausbildungId, gesuchsperiode, gesuchStatus, gesuchNummer, id, aenderungsdatum, gesuchTrancheToWorkWith, bearbeiter, einreichedatum, delegierung, changes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchWithChangesDto {\n");
    
    sb.append("    fallId: ").append(toIndentedString(fallId)).append("\n");
    sb.append("    fallNummer: ").append(toIndentedString(fallNummer)).append("\n");
    sb.append("    ausbildungId: ").append(toIndentedString(ausbildungId)).append("\n");
    sb.append("    gesuchsperiode: ").append(toIndentedString(gesuchsperiode)).append("\n");
    sb.append("    gesuchStatus: ").append(toIndentedString(gesuchStatus)).append("\n");
    sb.append("    gesuchNummer: ").append(toIndentedString(gesuchNummer)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    aenderungsdatum: ").append(toIndentedString(aenderungsdatum)).append("\n");
    sb.append("    gesuchTrancheToWorkWith: ").append(toIndentedString(gesuchTrancheToWorkWith)).append("\n");
    sb.append("    bearbeiter: ").append(toIndentedString(bearbeiter)).append("\n");
    sb.append("    einreichedatum: ").append(toIndentedString(einreichedatum)).append("\n");
    sb.append("    delegierung: ").append(toIndentedString(delegierung)).append("\n");
    sb.append("    changes: ").append(toIndentedString(changes)).append("\n");
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

