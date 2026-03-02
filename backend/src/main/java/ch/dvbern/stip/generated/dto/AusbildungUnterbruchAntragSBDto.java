package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DokumentDto;
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



@JsonTypeName("AusbildungUnterbruchAntragSB")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AusbildungUnterbruchAntragSBDto  implements Serializable {
  private @Valid ch.dvbern.stip.api.ausbildung.type.AusbildungUnterbruchAntragStatus status;
  private @Valid String kommentarSB;
  private @Valid LocalDate startDate;
  private @Valid LocalDate endDate;
  private @Valid UUID id;
  private @Valid java.time.LocalDateTime timestampErstellt;
  private @Valid String userErstellt;
  private @Valid String kommentarGS;
  private @Valid Boolean canAntragAkzeptieren;
  private @Valid List<DokumentDto> dokuments = new ArrayList<>();
  private @Valid UUID gesuchId;
  private @Valid LocalDate unterbruchLatestEndDate;
  private @Valid LocalDate unterbruchEarliestStartDate;
  private @Valid Integer monateOhneAnspruch;

  /**
   **/
  public AusbildungUnterbruchAntragSBDto status(ch.dvbern.stip.api.ausbildung.type.AusbildungUnterbruchAntragStatus status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  @NotNull
  public ch.dvbern.stip.api.ausbildung.type.AusbildungUnterbruchAntragStatus getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(ch.dvbern.stip.api.ausbildung.type.AusbildungUnterbruchAntragStatus status) {
    this.status = status;
  }

  /**
   **/
  public AusbildungUnterbruchAntragSBDto kommentarSB(String kommentarSB) {
    this.kommentarSB = kommentarSB;
    return this;
  }

  
  @JsonProperty("kommentarSB")
  @NotNull
  public String getKommentarSB() {
    return kommentarSB;
  }

  @JsonProperty("kommentarSB")
  public void setKommentarSB(String kommentarSB) {
    this.kommentarSB = kommentarSB;
  }

  /**
   **/
  public AusbildungUnterbruchAntragSBDto startDate(LocalDate startDate) {
    this.startDate = startDate;
    return this;
  }

  
  @JsonProperty("startDate")
  @NotNull
  public LocalDate getStartDate() {
    return startDate;
  }

  @JsonProperty("startDate")
  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  /**
   **/
  public AusbildungUnterbruchAntragSBDto endDate(LocalDate endDate) {
    this.endDate = endDate;
    return this;
  }

  
  @JsonProperty("endDate")
  @NotNull
  public LocalDate getEndDate() {
    return endDate;
  }

  @JsonProperty("endDate")
  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  /**
   **/
  public AusbildungUnterbruchAntragSBDto id(UUID id) {
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
  public AusbildungUnterbruchAntragSBDto timestampErstellt(java.time.LocalDateTime timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
    return this;
  }

  
  @JsonProperty("timestampErstellt")
  @NotNull
  public java.time.LocalDateTime getTimestampErstellt() {
    return timestampErstellt;
  }

  @JsonProperty("timestampErstellt")
  public void setTimestampErstellt(java.time.LocalDateTime timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
  }

  /**
   **/
  public AusbildungUnterbruchAntragSBDto userErstellt(String userErstellt) {
    this.userErstellt = userErstellt;
    return this;
  }

  
  @JsonProperty("userErstellt")
  @NotNull
  public String getUserErstellt() {
    return userErstellt;
  }

  @JsonProperty("userErstellt")
  public void setUserErstellt(String userErstellt) {
    this.userErstellt = userErstellt;
  }

  /**
   **/
  public AusbildungUnterbruchAntragSBDto kommentarGS(String kommentarGS) {
    this.kommentarGS = kommentarGS;
    return this;
  }

  
  @JsonProperty("kommentarGS")
  @NotNull
  public String getKommentarGS() {
    return kommentarGS;
  }

  @JsonProperty("kommentarGS")
  public void setKommentarGS(String kommentarGS) {
    this.kommentarGS = kommentarGS;
  }

  /**
   **/
  public AusbildungUnterbruchAntragSBDto canAntragAkzeptieren(Boolean canAntragAkzeptieren) {
    this.canAntragAkzeptieren = canAntragAkzeptieren;
    return this;
  }

  
  @JsonProperty("canAntragAkzeptieren")
  @NotNull
  public Boolean getCanAntragAkzeptieren() {
    return canAntragAkzeptieren;
  }

  @JsonProperty("canAntragAkzeptieren")
  public void setCanAntragAkzeptieren(Boolean canAntragAkzeptieren) {
    this.canAntragAkzeptieren = canAntragAkzeptieren;
  }

  /**
   **/
  public AusbildungUnterbruchAntragSBDto dokuments(List<DokumentDto> dokuments) {
    this.dokuments = dokuments;
    return this;
  }

  
  @JsonProperty("dokuments")
  @NotNull
  public List<DokumentDto> getDokuments() {
    return dokuments;
  }

  @JsonProperty("dokuments")
  public void setDokuments(List<DokumentDto> dokuments) {
    this.dokuments = dokuments;
  }

  public AusbildungUnterbruchAntragSBDto addDokumentsItem(DokumentDto dokumentsItem) {
    if (this.dokuments == null) {
      this.dokuments = new ArrayList<>();
    }

    this.dokuments.add(dokumentsItem);
    return this;
  }

  public AusbildungUnterbruchAntragSBDto removeDokumentsItem(DokumentDto dokumentsItem) {
    if (dokumentsItem != null && this.dokuments != null) {
      this.dokuments.remove(dokumentsItem);
    }

    return this;
  }
  /**
   **/
  public AusbildungUnterbruchAntragSBDto gesuchId(UUID gesuchId) {
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
  public AusbildungUnterbruchAntragSBDto unterbruchLatestEndDate(LocalDate unterbruchLatestEndDate) {
    this.unterbruchLatestEndDate = unterbruchLatestEndDate;
    return this;
  }

  
  @JsonProperty("unterbruchLatestEndDate")
  @NotNull
  public LocalDate getUnterbruchLatestEndDate() {
    return unterbruchLatestEndDate;
  }

  @JsonProperty("unterbruchLatestEndDate")
  public void setUnterbruchLatestEndDate(LocalDate unterbruchLatestEndDate) {
    this.unterbruchLatestEndDate = unterbruchLatestEndDate;
  }

  /**
   **/
  public AusbildungUnterbruchAntragSBDto unterbruchEarliestStartDate(LocalDate unterbruchEarliestStartDate) {
    this.unterbruchEarliestStartDate = unterbruchEarliestStartDate;
    return this;
  }

  
  @JsonProperty("unterbruchEarliestStartDate")
  @NotNull
  public LocalDate getUnterbruchEarliestStartDate() {
    return unterbruchEarliestStartDate;
  }

  @JsonProperty("unterbruchEarliestStartDate")
  public void setUnterbruchEarliestStartDate(LocalDate unterbruchEarliestStartDate) {
    this.unterbruchEarliestStartDate = unterbruchEarliestStartDate;
  }

  /**
   **/
  public AusbildungUnterbruchAntragSBDto monateOhneAnspruch(Integer monateOhneAnspruch) {
    this.monateOhneAnspruch = monateOhneAnspruch;
    return this;
  }

  
  @JsonProperty("monateOhneAnspruch")
  public Integer getMonateOhneAnspruch() {
    return monateOhneAnspruch;
  }

  @JsonProperty("monateOhneAnspruch")
  public void setMonateOhneAnspruch(Integer monateOhneAnspruch) {
    this.monateOhneAnspruch = monateOhneAnspruch;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AusbildungUnterbruchAntragSBDto ausbildungUnterbruchAntragSB = (AusbildungUnterbruchAntragSBDto) o;
    return Objects.equals(this.status, ausbildungUnterbruchAntragSB.status) &&
        Objects.equals(this.kommentarSB, ausbildungUnterbruchAntragSB.kommentarSB) &&
        Objects.equals(this.startDate, ausbildungUnterbruchAntragSB.startDate) &&
        Objects.equals(this.endDate, ausbildungUnterbruchAntragSB.endDate) &&
        Objects.equals(this.id, ausbildungUnterbruchAntragSB.id) &&
        Objects.equals(this.timestampErstellt, ausbildungUnterbruchAntragSB.timestampErstellt) &&
        Objects.equals(this.userErstellt, ausbildungUnterbruchAntragSB.userErstellt) &&
        Objects.equals(this.kommentarGS, ausbildungUnterbruchAntragSB.kommentarGS) &&
        Objects.equals(this.canAntragAkzeptieren, ausbildungUnterbruchAntragSB.canAntragAkzeptieren) &&
        Objects.equals(this.dokuments, ausbildungUnterbruchAntragSB.dokuments) &&
        Objects.equals(this.gesuchId, ausbildungUnterbruchAntragSB.gesuchId) &&
        Objects.equals(this.unterbruchLatestEndDate, ausbildungUnterbruchAntragSB.unterbruchLatestEndDate) &&
        Objects.equals(this.unterbruchEarliestStartDate, ausbildungUnterbruchAntragSB.unterbruchEarliestStartDate) &&
        Objects.equals(this.monateOhneAnspruch, ausbildungUnterbruchAntragSB.monateOhneAnspruch);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, kommentarSB, startDate, endDate, id, timestampErstellt, userErstellt, kommentarGS, canAntragAkzeptieren, dokuments, gesuchId, unterbruchLatestEndDate, unterbruchEarliestStartDate, monateOhneAnspruch);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungUnterbruchAntragSBDto {\n");
    
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    kommentarSB: ").append(toIndentedString(kommentarSB)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    timestampErstellt: ").append(toIndentedString(timestampErstellt)).append("\n");
    sb.append("    userErstellt: ").append(toIndentedString(userErstellt)).append("\n");
    sb.append("    kommentarGS: ").append(toIndentedString(kommentarGS)).append("\n");
    sb.append("    canAntragAkzeptieren: ").append(toIndentedString(canAntragAkzeptieren)).append("\n");
    sb.append("    dokuments: ").append(toIndentedString(dokuments)).append("\n");
    sb.append("    gesuchId: ").append(toIndentedString(gesuchId)).append("\n");
    sb.append("    unterbruchLatestEndDate: ").append(toIndentedString(unterbruchLatestEndDate)).append("\n");
    sb.append("    unterbruchEarliestStartDate: ").append(toIndentedString(unterbruchEarliestStartDate)).append("\n");
    sb.append("    monateOhneAnspruch: ").append(toIndentedString(monateOhneAnspruch)).append("\n");
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

