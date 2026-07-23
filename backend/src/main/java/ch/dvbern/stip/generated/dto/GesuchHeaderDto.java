package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.GesuchAenderungsDto;
import ch.dvbern.stip.generated.dto.GesuchInfoDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
import ch.dvbern.stip.generated.dto.InitialGesuchsDto;
import ch.dvbern.stip.generated.dto.VerfuegtGesuchDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
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



@JsonTypeName("GesuchHeader")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchHeaderDto  implements Serializable {
  private @Valid List<VerfuegtGesuchDto> versions = new ArrayList<>();
  private @Valid GesuchAenderungsDto aenderungs;
  private @Valid List<GesuchTrancheSlimDto> currentTranches = new ArrayList<>();
  private @Valid GesuchInfoDto gesuchInfo;
  private @Valid InitialGesuchsDto initial;
  private @Valid UUID latestVerfuegungId;
  private @Valid java.time.LocalDateTime latestVerfuegtAt;

  /**
   **/
  public GesuchHeaderDto versions(List<VerfuegtGesuchDto> versions) {
    this.versions = versions;
    return this;
  }

  
  @JsonProperty("versions")
  @NotNull
  public List<VerfuegtGesuchDto> getVersions() {
    return versions;
  }

  @JsonProperty("versions")
  public void setVersions(List<VerfuegtGesuchDto> versions) {
    this.versions = versions;
  }

  public GesuchHeaderDto addVersionsItem(VerfuegtGesuchDto versionsItem) {
    if (this.versions == null) {
      this.versions = new ArrayList<>();
    }

    this.versions.add(versionsItem);
    return this;
  }

  public GesuchHeaderDto removeVersionsItem(VerfuegtGesuchDto versionsItem) {
    if (versionsItem != null && this.versions != null) {
      this.versions.remove(versionsItem);
    }

    return this;
  }
  /**
   **/
  public GesuchHeaderDto aenderungs(GesuchAenderungsDto aenderungs) {
    this.aenderungs = aenderungs;
    return this;
  }

  
  @JsonProperty("aenderungs")
  @NotNull
  public GesuchAenderungsDto getAenderungs() {
    return aenderungs;
  }

  @JsonProperty("aenderungs")
  public void setAenderungs(GesuchAenderungsDto aenderungs) {
    this.aenderungs = aenderungs;
  }

  /**
   **/
  public GesuchHeaderDto currentTranches(List<GesuchTrancheSlimDto> currentTranches) {
    this.currentTranches = currentTranches;
    return this;
  }

  
  @JsonProperty("currentTranches")
  @NotNull
  public List<GesuchTrancheSlimDto> getCurrentTranches() {
    return currentTranches;
  }

  @JsonProperty("currentTranches")
  public void setCurrentTranches(List<GesuchTrancheSlimDto> currentTranches) {
    this.currentTranches = currentTranches;
  }

  public GesuchHeaderDto addCurrentTranchesItem(GesuchTrancheSlimDto currentTranchesItem) {
    if (this.currentTranches == null) {
      this.currentTranches = new ArrayList<>();
    }

    this.currentTranches.add(currentTranchesItem);
    return this;
  }

  public GesuchHeaderDto removeCurrentTranchesItem(GesuchTrancheSlimDto currentTranchesItem) {
    if (currentTranchesItem != null && this.currentTranches != null) {
      this.currentTranches.remove(currentTranchesItem);
    }

    return this;
  }
  /**
   **/
  public GesuchHeaderDto gesuchInfo(GesuchInfoDto gesuchInfo) {
    this.gesuchInfo = gesuchInfo;
    return this;
  }

  
  @JsonProperty("gesuchInfo")
  @NotNull
  public GesuchInfoDto getGesuchInfo() {
    return gesuchInfo;
  }

  @JsonProperty("gesuchInfo")
  public void setGesuchInfo(GesuchInfoDto gesuchInfo) {
    this.gesuchInfo = gesuchInfo;
  }

  /**
   **/
  public GesuchHeaderDto initial(InitialGesuchsDto initial) {
    this.initial = initial;
    return this;
  }

  
  @JsonProperty("initial")
  public InitialGesuchsDto getInitial() {
    return initial;
  }

  @JsonProperty("initial")
  public void setInitial(InitialGesuchsDto initial) {
    this.initial = initial;
  }

  /**
   **/
  public GesuchHeaderDto latestVerfuegungId(UUID latestVerfuegungId) {
    this.latestVerfuegungId = latestVerfuegungId;
    return this;
  }

  
  @JsonProperty("latestVerfuegungId")
  public UUID getLatestVerfuegungId() {
    return latestVerfuegungId;
  }

  @JsonProperty("latestVerfuegungId")
  public void setLatestVerfuegungId(UUID latestVerfuegungId) {
    this.latestVerfuegungId = latestVerfuegungId;
  }

  /**
   **/
  public GesuchHeaderDto latestVerfuegtAt(java.time.LocalDateTime latestVerfuegtAt) {
    this.latestVerfuegtAt = latestVerfuegtAt;
    return this;
  }

  
  @JsonProperty("latestVerfuegtAt")
  public java.time.LocalDateTime getLatestVerfuegtAt() {
    return latestVerfuegtAt;
  }

  @JsonProperty("latestVerfuegtAt")
  public void setLatestVerfuegtAt(java.time.LocalDateTime latestVerfuegtAt) {
    this.latestVerfuegtAt = latestVerfuegtAt;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchHeaderDto gesuchHeader = (GesuchHeaderDto) o;
    return Objects.equals(this.versions, gesuchHeader.versions) &&
        Objects.equals(this.aenderungs, gesuchHeader.aenderungs) &&
        Objects.equals(this.currentTranches, gesuchHeader.currentTranches) &&
        Objects.equals(this.gesuchInfo, gesuchHeader.gesuchInfo) &&
        Objects.equals(this.initial, gesuchHeader.initial) &&
        Objects.equals(this.latestVerfuegungId, gesuchHeader.latestVerfuegungId) &&
        Objects.equals(this.latestVerfuegtAt, gesuchHeader.latestVerfuegtAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(versions, aenderungs, currentTranches, gesuchInfo, initial, latestVerfuegungId, latestVerfuegtAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchHeaderDto {\n");
    
    sb.append("    versions: ").append(toIndentedString(versions)).append("\n");
    sb.append("    aenderungs: ").append(toIndentedString(aenderungs)).append("\n");
    sb.append("    currentTranches: ").append(toIndentedString(currentTranches)).append("\n");
    sb.append("    gesuchInfo: ").append(toIndentedString(gesuchInfo)).append("\n");
    sb.append("    initial: ").append(toIndentedString(initial)).append("\n");
    sb.append("    latestVerfuegungId: ").append(toIndentedString(latestVerfuegungId)).append("\n");
    sb.append("    latestVerfuegtAt: ").append(toIndentedString(latestVerfuegtAt)).append("\n");
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

