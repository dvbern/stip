package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.PersoenlicheAngabenDto;
import ch.dvbern.stip.generated.dto.SozialdienstBenutzerDto;
import ch.dvbern.stip.generated.dto.SozialdienstSlimDto;
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



@JsonTypeName("DelegierungEntry")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DelegierungEntryDto  implements Serializable {
  private @Valid UUID id;
  private @Valid SozialdienstSlimDto sozialdienst;
  private @Valid PersoenlicheAngabenDto persoenlicheAngaben;
  private @Valid ch.dvbern.stip.api.delegieren.type.DelegierungStatus status;
  private @Valid String tenant;
  private @Valid SozialdienstBenutzerDto delegierterMitarbeiter;
  private @Valid LocalDate startDate;
  private @Valid LocalDate endDate;
  private @Valid UUID fallId;
  private @Valid String fallNummer;

  /**
   **/
  public DelegierungEntryDto id(UUID id) {
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
  public DelegierungEntryDto sozialdienst(SozialdienstSlimDto sozialdienst) {
    this.sozialdienst = sozialdienst;
    return this;
  }

  
  @JsonProperty("sozialdienst")
  @NotNull
  public SozialdienstSlimDto getSozialdienst() {
    return sozialdienst;
  }

  @JsonProperty("sozialdienst")
  public void setSozialdienst(SozialdienstSlimDto sozialdienst) {
    this.sozialdienst = sozialdienst;
  }

  /**
   **/
  public DelegierungEntryDto persoenlicheAngaben(PersoenlicheAngabenDto persoenlicheAngaben) {
    this.persoenlicheAngaben = persoenlicheAngaben;
    return this;
  }

  
  @JsonProperty("persoenlicheAngaben")
  @NotNull
  public PersoenlicheAngabenDto getPersoenlicheAngaben() {
    return persoenlicheAngaben;
  }

  @JsonProperty("persoenlicheAngaben")
  public void setPersoenlicheAngaben(PersoenlicheAngabenDto persoenlicheAngaben) {
    this.persoenlicheAngaben = persoenlicheAngaben;
  }

  /**
   **/
  public DelegierungEntryDto status(ch.dvbern.stip.api.delegieren.type.DelegierungStatus status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  @NotNull
  public ch.dvbern.stip.api.delegieren.type.DelegierungStatus getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(ch.dvbern.stip.api.delegieren.type.DelegierungStatus status) {
    this.status = status;
  }

  /**
   **/
  public DelegierungEntryDto tenant(String tenant) {
    this.tenant = tenant;
    return this;
  }

  
  @JsonProperty("tenant")
  @NotNull
  public String getTenant() {
    return tenant;
  }

  @JsonProperty("tenant")
  public void setTenant(String tenant) {
    this.tenant = tenant;
  }

  /**
   **/
  public DelegierungEntryDto delegierterMitarbeiter(SozialdienstBenutzerDto delegierterMitarbeiter) {
    this.delegierterMitarbeiter = delegierterMitarbeiter;
    return this;
  }

  
  @JsonProperty("delegierterMitarbeiter")
  public SozialdienstBenutzerDto getDelegierterMitarbeiter() {
    return delegierterMitarbeiter;
  }

  @JsonProperty("delegierterMitarbeiter")
  public void setDelegierterMitarbeiter(SozialdienstBenutzerDto delegierterMitarbeiter) {
    this.delegierterMitarbeiter = delegierterMitarbeiter;
  }

  /**
   **/
  public DelegierungEntryDto startDate(LocalDate startDate) {
    this.startDate = startDate;
    return this;
  }

  
  @JsonProperty("startDate")
  public LocalDate getStartDate() {
    return startDate;
  }

  @JsonProperty("startDate")
  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  /**
   **/
  public DelegierungEntryDto endDate(LocalDate endDate) {
    this.endDate = endDate;
    return this;
  }

  
  @JsonProperty("endDate")
  public LocalDate getEndDate() {
    return endDate;
  }

  @JsonProperty("endDate")
  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  /**
   **/
  public DelegierungEntryDto fallId(UUID fallId) {
    this.fallId = fallId;
    return this;
  }

  
  @JsonProperty("fallId")
  public UUID getFallId() {
    return fallId;
  }

  @JsonProperty("fallId")
  public void setFallId(UUID fallId) {
    this.fallId = fallId;
  }

  /**
   **/
  public DelegierungEntryDto fallNummer(String fallNummer) {
    this.fallNummer = fallNummer;
    return this;
  }

  
  @JsonProperty("fallNummer")
  public String getFallNummer() {
    return fallNummer;
  }

  @JsonProperty("fallNummer")
  public void setFallNummer(String fallNummer) {
    this.fallNummer = fallNummer;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DelegierungEntryDto delegierungEntry = (DelegierungEntryDto) o;
    return Objects.equals(this.id, delegierungEntry.id) &&
        Objects.equals(this.sozialdienst, delegierungEntry.sozialdienst) &&
        Objects.equals(this.persoenlicheAngaben, delegierungEntry.persoenlicheAngaben) &&
        Objects.equals(this.status, delegierungEntry.status) &&
        Objects.equals(this.tenant, delegierungEntry.tenant) &&
        Objects.equals(this.delegierterMitarbeiter, delegierungEntry.delegierterMitarbeiter) &&
        Objects.equals(this.startDate, delegierungEntry.startDate) &&
        Objects.equals(this.endDate, delegierungEntry.endDate) &&
        Objects.equals(this.fallId, delegierungEntry.fallId) &&
        Objects.equals(this.fallNummer, delegierungEntry.fallNummer);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, sozialdienst, persoenlicheAngaben, status, tenant, delegierterMitarbeiter, startDate, endDate, fallId, fallNummer);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DelegierungEntryDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    sozialdienst: ").append(toIndentedString(sozialdienst)).append("\n");
    sb.append("    persoenlicheAngaben: ").append(toIndentedString(persoenlicheAngaben)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    tenant: ").append(toIndentedString(tenant)).append("\n");
    sb.append("    delegierterMitarbeiter: ").append(toIndentedString(delegierterMitarbeiter)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    fallId: ").append(toIndentedString(fallId)).append("\n");
    sb.append("    fallNummer: ").append(toIndentedString(fallNummer)).append("\n");
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

