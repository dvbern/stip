package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("UpdateAusbildungUnterbruchAntragSB")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class UpdateAusbildungUnterbruchAntragSBDto  implements Serializable {
  private @Valid ch.dvbern.stip.api.ausbildung.type.AusbildungUnterbruchAntragStatus status;
  private @Valid String kommentarSB;
  private @Valid LocalDate startDate;
  private @Valid LocalDate endDate;
  private @Valid Integer monateOhneAnspruch;

  /**
   **/
  public UpdateAusbildungUnterbruchAntragSBDto status(ch.dvbern.stip.api.ausbildung.type.AusbildungUnterbruchAntragStatus status) {
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
  public UpdateAusbildungUnterbruchAntragSBDto kommentarSB(String kommentarSB) {
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
  public UpdateAusbildungUnterbruchAntragSBDto startDate(LocalDate startDate) {
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
  public UpdateAusbildungUnterbruchAntragSBDto endDate(LocalDate endDate) {
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
  public UpdateAusbildungUnterbruchAntragSBDto monateOhneAnspruch(Integer monateOhneAnspruch) {
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
    UpdateAusbildungUnterbruchAntragSBDto updateAusbildungUnterbruchAntragSB = (UpdateAusbildungUnterbruchAntragSBDto) o;
    return Objects.equals(this.status, updateAusbildungUnterbruchAntragSB.status) &&
        Objects.equals(this.kommentarSB, updateAusbildungUnterbruchAntragSB.kommentarSB) &&
        Objects.equals(this.startDate, updateAusbildungUnterbruchAntragSB.startDate) &&
        Objects.equals(this.endDate, updateAusbildungUnterbruchAntragSB.endDate) &&
        Objects.equals(this.monateOhneAnspruch, updateAusbildungUnterbruchAntragSB.monateOhneAnspruch);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, kommentarSB, startDate, endDate, monateOhneAnspruch);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateAusbildungUnterbruchAntragSBDto {\n");
    
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    kommentarSB: ").append(toIndentedString(kommentarSB)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
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

