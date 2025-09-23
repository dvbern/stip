package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import ch.dvbern.stip.api.massendruck.type.MassendruckJobStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@JsonTypeName("Druckauftrag")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DruckauftragDto  implements Serializable {
  private @Valid UUID id;
  private @Valid String batchName;
  private @Valid String bearbeiter;
  private @Valid String timestampErstellt;
  private @Valid MassendruckJobStatus massendruckJobStatus;
  private @Valid ch.dvbern.stip.api.massendruck.type.DruckauftragTyp druckauftragTyp;

  /**
   **/
  public DruckauftragDto id(UUID id) {
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
  public DruckauftragDto batchName(String batchName) {
    this.batchName = batchName;
    return this;
  }


  @JsonProperty("batchName")
  @NotNull
  public String getBatchName() {
    return batchName;
  }

  @JsonProperty("batchName")
  public void setBatchName(String batchName) {
    this.batchName = batchName;
  }

  /**
   **/
  public DruckauftragDto bearbeiter(String bearbeiter) {
    this.bearbeiter = bearbeiter;
    return this;
  }


  @JsonProperty("bearbeiter")
  @NotNull
  public String getBearbeiter() {
    return bearbeiter;
  }

  @JsonProperty("bearbeiter")
  public void setBearbeiter(String bearbeiter) {
    this.bearbeiter = bearbeiter;
  }

  /**
   **/
  public DruckauftragDto timestampErstellt(String timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
    return this;
  }


  @JsonProperty("timestampErstellt")
  @NotNull
  public String getTimestampErstellt() {
    return timestampErstellt;
  }

  @JsonProperty("timestampErstellt")
  public void setTimestampErstellt(String timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
  }

  /**
   **/
  public DruckauftragDto druckauftragStatus(MassendruckJobStatus massendruckJobStatus) {
    this.massendruckJobStatus = massendruckJobStatus;
    return this;
  }


  @JsonProperty("druckauftragStatus")
  @NotNull
  public MassendruckJobStatus getMassendruckJobStatus() {
    return massendruckJobStatus;
  }

  @JsonProperty("druckauftragStatus")
  public void setMassendruckJobStatus(MassendruckJobStatus massendruckJobStatus) {
    this.massendruckJobStatus = massendruckJobStatus;
  }

  /**
   **/
  public DruckauftragDto druckauftragTyp(ch.dvbern.stip.api.massendruck.type.DruckauftragTyp druckauftragTyp) {
    this.druckauftragTyp = druckauftragTyp;
    return this;
  }


  @JsonProperty("druckauftragTyp")
  @NotNull
  public ch.dvbern.stip.api.massendruck.type.DruckauftragTyp getDruckauftragTyp() {
    return druckauftragTyp;
  }

  @JsonProperty("druckauftragTyp")
  public void setDruckauftragTyp(ch.dvbern.stip.api.massendruck.type.DruckauftragTyp druckauftragTyp) {
    this.druckauftragTyp = druckauftragTyp;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DruckauftragDto druckauftrag = (DruckauftragDto) o;
    return Objects.equals(this.id, druckauftrag.id) &&
        Objects.equals(this.batchName, druckauftrag.batchName) &&
        Objects.equals(this.bearbeiter, druckauftrag.bearbeiter) &&
        Objects.equals(this.timestampErstellt, druckauftrag.timestampErstellt) &&
        Objects.equals(this.massendruckJobStatus, druckauftrag.massendruckJobStatus) &&
        Objects.equals(this.druckauftragTyp, druckauftrag.druckauftragTyp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, batchName, bearbeiter, timestampErstellt, massendruckJobStatus, druckauftragTyp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DruckauftragDto {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    batchName: ").append(toIndentedString(batchName)).append("\n");
    sb.append("    bearbeiter: ").append(toIndentedString(bearbeiter)).append("\n");
    sb.append("    timestampErstellt: ").append(toIndentedString(timestampErstellt)).append("\n");
    sb.append("    druckauftragStatus: ").append(toIndentedString(massendruckJobStatus)).append("\n");
    sb.append("    druckauftragTyp: ").append(toIndentedString(druckauftragTyp)).append("\n");
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

