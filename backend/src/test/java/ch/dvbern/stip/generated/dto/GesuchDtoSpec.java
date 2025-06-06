/*
 * STIP API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package ch.dvbern.stip.generated.dto;

import java.util.Objects;
import java.util.Arrays;
import ch.dvbern.stip.generated.dto.DelegierungSlimDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchTrancheDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchsperiodeDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchstatusDtoSpec;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.LocalDate;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * GesuchDtoSpec
 */
@JsonPropertyOrder({
  GesuchDtoSpec.JSON_PROPERTY_FALL_ID,
  GesuchDtoSpec.JSON_PROPERTY_FALL_NUMMER,
  GesuchDtoSpec.JSON_PROPERTY_AUSBILDUNG_ID,
  GesuchDtoSpec.JSON_PROPERTY_GESUCHSPERIODE,
  GesuchDtoSpec.JSON_PROPERTY_GESUCH_STATUS,
  GesuchDtoSpec.JSON_PROPERTY_GESUCH_NUMMER,
  GesuchDtoSpec.JSON_PROPERTY_ID,
  GesuchDtoSpec.JSON_PROPERTY_AENDERUNGSDATUM,
  GesuchDtoSpec.JSON_PROPERTY_BEARBEITER,
  GesuchDtoSpec.JSON_PROPERTY_GESUCH_TRANCHE_TO_WORK_WITH,
  GesuchDtoSpec.JSON_PROPERTY_EINREICHEDATUM,
  GesuchDtoSpec.JSON_PROPERTY_DELEGIERUNG,
  GesuchDtoSpec.JSON_PROPERTY_NACHFRIST_DOKUMENTE,
  GesuchDtoSpec.JSON_PROPERTY_VERFUEGT
})
@JsonTypeName("Gesuch")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class GesuchDtoSpec {
  public static final String JSON_PROPERTY_FALL_ID = "fallId";
  private UUID fallId;

  public static final String JSON_PROPERTY_FALL_NUMMER = "fallNummer";
  private String fallNummer;

  public static final String JSON_PROPERTY_AUSBILDUNG_ID = "ausbildungId";
  private UUID ausbildungId;

  public static final String JSON_PROPERTY_GESUCHSPERIODE = "gesuchsperiode";
  private GesuchsperiodeDtoSpec gesuchsperiode;

  public static final String JSON_PROPERTY_GESUCH_STATUS = "gesuchStatus";
  private GesuchstatusDtoSpec gesuchStatus;

  public static final String JSON_PROPERTY_GESUCH_NUMMER = "gesuchNummer";
  private String gesuchNummer;

  public static final String JSON_PROPERTY_ID = "id";
  private UUID id;

  public static final String JSON_PROPERTY_AENDERUNGSDATUM = "aenderungsdatum";
  private LocalDate aenderungsdatum;

  public static final String JSON_PROPERTY_BEARBEITER = "bearbeiter";
  private String bearbeiter;

  public static final String JSON_PROPERTY_GESUCH_TRANCHE_TO_WORK_WITH = "gesuchTrancheToWorkWith";
  private GesuchTrancheDtoSpec gesuchTrancheToWorkWith;

  public static final String JSON_PROPERTY_EINREICHEDATUM = "einreichedatum";
  private LocalDate einreichedatum;

  public static final String JSON_PROPERTY_DELEGIERUNG = "delegierung";
  private DelegierungSlimDtoSpec delegierung;

  public static final String JSON_PROPERTY_NACHFRIST_DOKUMENTE = "nachfristDokumente";
  private LocalDate nachfristDokumente;

  public static final String JSON_PROPERTY_VERFUEGT = "verfuegt";
  private Boolean verfuegt;

  public GesuchDtoSpec() {
  }

  public GesuchDtoSpec fallId(UUID fallId) {
    
    this.fallId = fallId;
    return this;
  }

   /**
   * Get fallId
   * @return fallId
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_FALL_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public UUID getFallId() {
    return fallId;
  }


  @JsonProperty(JSON_PROPERTY_FALL_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setFallId(UUID fallId) {
    this.fallId = fallId;
  }


  public GesuchDtoSpec fallNummer(String fallNummer) {
    
    this.fallNummer = fallNummer;
    return this;
  }

   /**
   * Get fallNummer
   * @return fallNummer
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_FALL_NUMMER)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getFallNummer() {
    return fallNummer;
  }


  @JsonProperty(JSON_PROPERTY_FALL_NUMMER)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setFallNummer(String fallNummer) {
    this.fallNummer = fallNummer;
  }


  public GesuchDtoSpec ausbildungId(UUID ausbildungId) {
    
    this.ausbildungId = ausbildungId;
    return this;
  }

   /**
   * Get ausbildungId
   * @return ausbildungId
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_AUSBILDUNG_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public UUID getAusbildungId() {
    return ausbildungId;
  }


  @JsonProperty(JSON_PROPERTY_AUSBILDUNG_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setAusbildungId(UUID ausbildungId) {
    this.ausbildungId = ausbildungId;
  }


  public GesuchDtoSpec gesuchsperiode(GesuchsperiodeDtoSpec gesuchsperiode) {
    
    this.gesuchsperiode = gesuchsperiode;
    return this;
  }

   /**
   * Get gesuchsperiode
   * @return gesuchsperiode
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_GESUCHSPERIODE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public GesuchsperiodeDtoSpec getGesuchsperiode() {
    return gesuchsperiode;
  }


  @JsonProperty(JSON_PROPERTY_GESUCHSPERIODE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setGesuchsperiode(GesuchsperiodeDtoSpec gesuchsperiode) {
    this.gesuchsperiode = gesuchsperiode;
  }


  public GesuchDtoSpec gesuchStatus(GesuchstatusDtoSpec gesuchStatus) {
    
    this.gesuchStatus = gesuchStatus;
    return this;
  }

   /**
   * Get gesuchStatus
   * @return gesuchStatus
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_GESUCH_STATUS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public GesuchstatusDtoSpec getGesuchStatus() {
    return gesuchStatus;
  }


  @JsonProperty(JSON_PROPERTY_GESUCH_STATUS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setGesuchStatus(GesuchstatusDtoSpec gesuchStatus) {
    this.gesuchStatus = gesuchStatus;
  }


  public GesuchDtoSpec gesuchNummer(String gesuchNummer) {
    
    this.gesuchNummer = gesuchNummer;
    return this;
  }

   /**
   * Get gesuchNummer
   * @return gesuchNummer
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_GESUCH_NUMMER)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getGesuchNummer() {
    return gesuchNummer;
  }


  @JsonProperty(JSON_PROPERTY_GESUCH_NUMMER)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setGesuchNummer(String gesuchNummer) {
    this.gesuchNummer = gesuchNummer;
  }


  public GesuchDtoSpec id(UUID id) {
    
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public UUID getId() {
    return id;
  }


  @JsonProperty(JSON_PROPERTY_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setId(UUID id) {
    this.id = id;
  }


  public GesuchDtoSpec aenderungsdatum(LocalDate aenderungsdatum) {
    
    this.aenderungsdatum = aenderungsdatum;
    return this;
  }

   /**
   * Get aenderungsdatum
   * @return aenderungsdatum
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_AENDERUNGSDATUM)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public LocalDate getAenderungsdatum() {
    return aenderungsdatum;
  }


  @JsonProperty(JSON_PROPERTY_AENDERUNGSDATUM)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setAenderungsdatum(LocalDate aenderungsdatum) {
    this.aenderungsdatum = aenderungsdatum;
  }


  public GesuchDtoSpec bearbeiter(String bearbeiter) {
    
    this.bearbeiter = bearbeiter;
    return this;
  }

   /**
   * Zuständiger Sachbearbeiter des Gesuchs
   * @return bearbeiter
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_BEARBEITER)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getBearbeiter() {
    return bearbeiter;
  }


  @JsonProperty(JSON_PROPERTY_BEARBEITER)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setBearbeiter(String bearbeiter) {
    this.bearbeiter = bearbeiter;
  }


  public GesuchDtoSpec gesuchTrancheToWorkWith(GesuchTrancheDtoSpec gesuchTrancheToWorkWith) {
    
    this.gesuchTrancheToWorkWith = gesuchTrancheToWorkWith;
    return this;
  }

   /**
   * Get gesuchTrancheToWorkWith
   * @return gesuchTrancheToWorkWith
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_GESUCH_TRANCHE_TO_WORK_WITH)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public GesuchTrancheDtoSpec getGesuchTrancheToWorkWith() {
    return gesuchTrancheToWorkWith;
  }


  @JsonProperty(JSON_PROPERTY_GESUCH_TRANCHE_TO_WORK_WITH)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setGesuchTrancheToWorkWith(GesuchTrancheDtoSpec gesuchTrancheToWorkWith) {
    this.gesuchTrancheToWorkWith = gesuchTrancheToWorkWith;
  }


  public GesuchDtoSpec einreichedatum(LocalDate einreichedatum) {
    
    this.einreichedatum = einreichedatum;
    return this;
  }

   /**
   * Get einreichedatum
   * @return einreichedatum
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_EINREICHEDATUM)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public LocalDate getEinreichedatum() {
    return einreichedatum;
  }


  @JsonProperty(JSON_PROPERTY_EINREICHEDATUM)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEinreichedatum(LocalDate einreichedatum) {
    this.einreichedatum = einreichedatum;
  }


  public GesuchDtoSpec delegierung(DelegierungSlimDtoSpec delegierung) {
    
    this.delegierung = delegierung;
    return this;
  }

   /**
   * Get delegierung
   * @return delegierung
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_DELEGIERUNG)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public DelegierungSlimDtoSpec getDelegierung() {
    return delegierung;
  }


  @JsonProperty(JSON_PROPERTY_DELEGIERUNG)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setDelegierung(DelegierungSlimDtoSpec delegierung) {
    this.delegierung = delegierung;
  }


  public GesuchDtoSpec nachfristDokumente(LocalDate nachfristDokumente) {
    
    this.nachfristDokumente = nachfristDokumente;
    return this;
  }

   /**
   * Get nachfristDokumente
   * @return nachfristDokumente
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_NACHFRIST_DOKUMENTE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public LocalDate getNachfristDokumente() {
    return nachfristDokumente;
  }


  @JsonProperty(JSON_PROPERTY_NACHFRIST_DOKUMENTE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setNachfristDokumente(LocalDate nachfristDokumente) {
    this.nachfristDokumente = nachfristDokumente;
  }


  public GesuchDtoSpec verfuegt(Boolean verfuegt) {
    
    this.verfuegt = verfuegt;
    return this;
  }

   /**
   * Get verfuegt
   * @return verfuegt
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_VERFUEGT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Boolean getVerfuegt() {
    return verfuegt;
  }


  @JsonProperty(JSON_PROPERTY_VERFUEGT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setVerfuegt(Boolean verfuegt) {
    this.verfuegt = verfuegt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchDtoSpec gesuch = (GesuchDtoSpec) o;
    return Objects.equals(this.fallId, gesuch.fallId) &&
        Objects.equals(this.fallNummer, gesuch.fallNummer) &&
        Objects.equals(this.ausbildungId, gesuch.ausbildungId) &&
        Objects.equals(this.gesuchsperiode, gesuch.gesuchsperiode) &&
        Objects.equals(this.gesuchStatus, gesuch.gesuchStatus) &&
        Objects.equals(this.gesuchNummer, gesuch.gesuchNummer) &&
        Objects.equals(this.id, gesuch.id) &&
        Objects.equals(this.aenderungsdatum, gesuch.aenderungsdatum) &&
        Objects.equals(this.bearbeiter, gesuch.bearbeiter) &&
        Objects.equals(this.gesuchTrancheToWorkWith, gesuch.gesuchTrancheToWorkWith) &&
        Objects.equals(this.einreichedatum, gesuch.einreichedatum) &&
        Objects.equals(this.delegierung, gesuch.delegierung) &&
        Objects.equals(this.nachfristDokumente, gesuch.nachfristDokumente) &&
        Objects.equals(this.verfuegt, gesuch.verfuegt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fallId, fallNummer, ausbildungId, gesuchsperiode, gesuchStatus, gesuchNummer, id, aenderungsdatum, bearbeiter, gesuchTrancheToWorkWith, einreichedatum, delegierung, nachfristDokumente, verfuegt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchDtoSpec {\n");
    sb.append("    fallId: ").append(toIndentedString(fallId)).append("\n");
    sb.append("    fallNummer: ").append(toIndentedString(fallNummer)).append("\n");
    sb.append("    ausbildungId: ").append(toIndentedString(ausbildungId)).append("\n");
    sb.append("    gesuchsperiode: ").append(toIndentedString(gesuchsperiode)).append("\n");
    sb.append("    gesuchStatus: ").append(toIndentedString(gesuchStatus)).append("\n");
    sb.append("    gesuchNummer: ").append(toIndentedString(gesuchNummer)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    aenderungsdatum: ").append(toIndentedString(aenderungsdatum)).append("\n");
    sb.append("    bearbeiter: ").append(toIndentedString(bearbeiter)).append("\n");
    sb.append("    gesuchTrancheToWorkWith: ").append(toIndentedString(gesuchTrancheToWorkWith)).append("\n");
    sb.append("    einreichedatum: ").append(toIndentedString(einreichedatum)).append("\n");
    sb.append("    delegierung: ").append(toIndentedString(delegierung)).append("\n");
    sb.append("    nachfristDokumente: ").append(toIndentedString(nachfristDokumente)).append("\n");
    sb.append("    verfuegt: ").append(toIndentedString(verfuegt)).append("\n");
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

