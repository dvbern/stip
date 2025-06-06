package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DelegierungSlimDto;
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
  private @Valid UUID fallId;
  private @Valid String fallNummer;
  private @Valid UUID ausbildungId;
  private @Valid GesuchsperiodeDto gesuchsperiode;
  private @Valid ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus;
  private @Valid String gesuchNummer;
  private @Valid UUID id;
  private @Valid LocalDate aenderungsdatum;
  private @Valid GesuchTrancheDto gesuchTrancheToWorkWith;
  private @Valid Boolean verfuegt;
  private @Valid String bearbeiter;
  private @Valid LocalDate einreichedatum;
  private @Valid DelegierungSlimDto delegierung;
  private @Valid LocalDate nachfristDokumente;

  /**
   **/
  public GesuchDto fallId(UUID fallId) {
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
  public GesuchDto fallNummer(String fallNummer) {
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
  public GesuchDto ausbildungId(UUID ausbildungId) {
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
  public GesuchDto gesuchStatus(ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus) {
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
  public GesuchDto gesuchNummer(String gesuchNummer) {
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
   **/
  public GesuchDto verfuegt(Boolean verfuegt) {
    this.verfuegt = verfuegt;
    return this;
  }

  
  @JsonProperty("verfuegt")
  @NotNull
  public Boolean getVerfuegt() {
    return verfuegt;
  }

  @JsonProperty("verfuegt")
  public void setVerfuegt(Boolean verfuegt) {
    this.verfuegt = verfuegt;
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

  /**
   **/
  public GesuchDto einreichedatum(LocalDate einreichedatum) {
    this.einreichedatum = einreichedatum;
    return this;
  }

  
  @JsonProperty("einreichedatum")
  public LocalDate getEinreichedatum() {
    return einreichedatum;
  }

  @JsonProperty("einreichedatum")
  public void setEinreichedatum(LocalDate einreichedatum) {
    this.einreichedatum = einreichedatum;
  }

  /**
   **/
  public GesuchDto delegierung(DelegierungSlimDto delegierung) {
    this.delegierung = delegierung;
    return this;
  }

  
  @JsonProperty("delegierung")
  public DelegierungSlimDto getDelegierung() {
    return delegierung;
  }

  @JsonProperty("delegierung")
  public void setDelegierung(DelegierungSlimDto delegierung) {
    this.delegierung = delegierung;
  }

  /**
   **/
  public GesuchDto nachfristDokumente(LocalDate nachfristDokumente) {
    this.nachfristDokumente = nachfristDokumente;
    return this;
  }

  
  @JsonProperty("nachfristDokumente")
  public LocalDate getNachfristDokumente() {
    return nachfristDokumente;
  }

  @JsonProperty("nachfristDokumente")
  public void setNachfristDokumente(LocalDate nachfristDokumente) {
    this.nachfristDokumente = nachfristDokumente;
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
    return Objects.equals(this.fallId, gesuch.fallId) &&
        Objects.equals(this.fallNummer, gesuch.fallNummer) &&
        Objects.equals(this.ausbildungId, gesuch.ausbildungId) &&
        Objects.equals(this.gesuchsperiode, gesuch.gesuchsperiode) &&
        Objects.equals(this.gesuchStatus, gesuch.gesuchStatus) &&
        Objects.equals(this.gesuchNummer, gesuch.gesuchNummer) &&
        Objects.equals(this.id, gesuch.id) &&
        Objects.equals(this.aenderungsdatum, gesuch.aenderungsdatum) &&
        Objects.equals(this.gesuchTrancheToWorkWith, gesuch.gesuchTrancheToWorkWith) &&
        Objects.equals(this.verfuegt, gesuch.verfuegt) &&
        Objects.equals(this.bearbeiter, gesuch.bearbeiter) &&
        Objects.equals(this.einreichedatum, gesuch.einreichedatum) &&
        Objects.equals(this.delegierung, gesuch.delegierung) &&
        Objects.equals(this.nachfristDokumente, gesuch.nachfristDokumente);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fallId, fallNummer, ausbildungId, gesuchsperiode, gesuchStatus, gesuchNummer, id, aenderungsdatum, gesuchTrancheToWorkWith, verfuegt, bearbeiter, einreichedatum, delegierung, nachfristDokumente);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchDto {\n");
    
    sb.append("    fallId: ").append(toIndentedString(fallId)).append("\n");
    sb.append("    fallNummer: ").append(toIndentedString(fallNummer)).append("\n");
    sb.append("    ausbildungId: ").append(toIndentedString(ausbildungId)).append("\n");
    sb.append("    gesuchsperiode: ").append(toIndentedString(gesuchsperiode)).append("\n");
    sb.append("    gesuchStatus: ").append(toIndentedString(gesuchStatus)).append("\n");
    sb.append("    gesuchNummer: ").append(toIndentedString(gesuchNummer)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    aenderungsdatum: ").append(toIndentedString(aenderungsdatum)).append("\n");
    sb.append("    gesuchTrancheToWorkWith: ").append(toIndentedString(gesuchTrancheToWorkWith)).append("\n");
    sb.append("    verfuegt: ").append(toIndentedString(verfuegt)).append("\n");
    sb.append("    bearbeiter: ").append(toIndentedString(bearbeiter)).append("\n");
    sb.append("    einreichedatum: ").append(toIndentedString(einreichedatum)).append("\n");
    sb.append("    delegierung: ").append(toIndentedString(delegierung)).append("\n");
    sb.append("    nachfristDokumente: ").append(toIndentedString(nachfristDokumente)).append("\n");
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

