package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("MassendruckJob")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class MassendruckJobDto  implements Serializable {
  private @Valid UUID id;
  private @Valid Integer massendruckJobNumber;
  private @Valid String userErstellt;
  private @Valid java.time.LocalDateTime timestampErstellt;
  private @Valid ch.dvbern.stip.api.massendruck.type.MassendruckJobStatus massendruckJobStatus;
  private @Valid ch.dvbern.stip.api.massendruck.type.MassendruckJobTyp massendruckJobTyp;

  /**
   **/
  public MassendruckJobDto id(UUID id) {
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
  public MassendruckJobDto massendruckJobNumber(Integer massendruckJobNumber) {
    this.massendruckJobNumber = massendruckJobNumber;
    return this;
  }

  
  @JsonProperty("massendruckJobNumber")
  @NotNull
  public Integer getMassendruckJobNumber() {
    return massendruckJobNumber;
  }

  @JsonProperty("massendruckJobNumber")
  public void setMassendruckJobNumber(Integer massendruckJobNumber) {
    this.massendruckJobNumber = massendruckJobNumber;
  }

  /**
   **/
  public MassendruckJobDto userErstellt(String userErstellt) {
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
  public MassendruckJobDto timestampErstellt(java.time.LocalDateTime timestampErstellt) {
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
  public MassendruckJobDto massendruckJobStatus(ch.dvbern.stip.api.massendruck.type.MassendruckJobStatus massendruckJobStatus) {
    this.massendruckJobStatus = massendruckJobStatus;
    return this;
  }

  
  @JsonProperty("massendruckJobStatus")
  @NotNull
  public ch.dvbern.stip.api.massendruck.type.MassendruckJobStatus getMassendruckJobStatus() {
    return massendruckJobStatus;
  }

  @JsonProperty("massendruckJobStatus")
  public void setMassendruckJobStatus(ch.dvbern.stip.api.massendruck.type.MassendruckJobStatus massendruckJobStatus) {
    this.massendruckJobStatus = massendruckJobStatus;
  }

  /**
   **/
  public MassendruckJobDto massendruckJobTyp(ch.dvbern.stip.api.massendruck.type.MassendruckJobTyp massendruckJobTyp) {
    this.massendruckJobTyp = massendruckJobTyp;
    return this;
  }

  
  @JsonProperty("massendruckJobTyp")
  @NotNull
  public ch.dvbern.stip.api.massendruck.type.MassendruckJobTyp getMassendruckJobTyp() {
    return massendruckJobTyp;
  }

  @JsonProperty("massendruckJobTyp")
  public void setMassendruckJobTyp(ch.dvbern.stip.api.massendruck.type.MassendruckJobTyp massendruckJobTyp) {
    this.massendruckJobTyp = massendruckJobTyp;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MassendruckJobDto massendruckJob = (MassendruckJobDto) o;
    return Objects.equals(this.id, massendruckJob.id) &&
        Objects.equals(this.massendruckJobNumber, massendruckJob.massendruckJobNumber) &&
        Objects.equals(this.userErstellt, massendruckJob.userErstellt) &&
        Objects.equals(this.timestampErstellt, massendruckJob.timestampErstellt) &&
        Objects.equals(this.massendruckJobStatus, massendruckJob.massendruckJobStatus) &&
        Objects.equals(this.massendruckJobTyp, massendruckJob.massendruckJobTyp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, massendruckJobNumber, userErstellt, timestampErstellt, massendruckJobStatus, massendruckJobTyp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MassendruckJobDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    massendruckJobNumber: ").append(toIndentedString(massendruckJobNumber)).append("\n");
    sb.append("    userErstellt: ").append(toIndentedString(userErstellt)).append("\n");
    sb.append("    timestampErstellt: ").append(toIndentedString(timestampErstellt)).append("\n");
    sb.append("    massendruckJobStatus: ").append(toIndentedString(massendruckJobStatus)).append("\n");
    sb.append("    massendruckJobTyp: ").append(toIndentedString(massendruckJobTyp)).append("\n");
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

