package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.MassendruckDatenschutzbriefDto;
import ch.dvbern.stip.generated.dto.MassendruckVerfuegungDto;
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



@JsonTypeName("MassendruckJobDetail")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class MassendruckJobDetailDto  implements Serializable {
  private @Valid UUID id;
  private @Valid String userErstellt;
  private @Valid java.time.LocalDateTime timestampErstellt;
  private @Valid ch.dvbern.stip.api.massendruck.type.MassendruckJobStatus massendruckJobStatus;
  private @Valid ch.dvbern.stip.api.massendruck.type.MassendruckJobTyp massendruckJobTyp;
  private @Valid List<MassendruckDatenschutzbriefDto> datenschutzbriefMassendrucks;
  private @Valid List<MassendruckVerfuegungDto> verfuegungMassendrucks;

  /**
   **/
  public MassendruckJobDetailDto id(UUID id) {
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
  public MassendruckJobDetailDto userErstellt(String userErstellt) {
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
  public MassendruckJobDetailDto timestampErstellt(java.time.LocalDateTime timestampErstellt) {
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
  public MassendruckJobDetailDto massendruckJobStatus(ch.dvbern.stip.api.massendruck.type.MassendruckJobStatus massendruckJobStatus) {
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
  public MassendruckJobDetailDto massendruckJobTyp(ch.dvbern.stip.api.massendruck.type.MassendruckJobTyp massendruckJobTyp) {
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

  /**
   **/
  public MassendruckJobDetailDto datenschutzbriefMassendrucks(List<MassendruckDatenschutzbriefDto> datenschutzbriefMassendrucks) {
    this.datenschutzbriefMassendrucks = datenschutzbriefMassendrucks;
    return this;
  }

  
  @JsonProperty("datenschutzbriefMassendrucks")
  public List<MassendruckDatenschutzbriefDto> getDatenschutzbriefMassendrucks() {
    return datenschutzbriefMassendrucks;
  }

  @JsonProperty("datenschutzbriefMassendrucks")
  public void setDatenschutzbriefMassendrucks(List<MassendruckDatenschutzbriefDto> datenschutzbriefMassendrucks) {
    this.datenschutzbriefMassendrucks = datenschutzbriefMassendrucks;
  }

  public MassendruckJobDetailDto addDatenschutzbriefMassendrucksItem(MassendruckDatenschutzbriefDto datenschutzbriefMassendrucksItem) {
    if (this.datenschutzbriefMassendrucks == null) {
      this.datenschutzbriefMassendrucks = new ArrayList<>();
    }

    this.datenschutzbriefMassendrucks.add(datenschutzbriefMassendrucksItem);
    return this;
  }

  public MassendruckJobDetailDto removeDatenschutzbriefMassendrucksItem(MassendruckDatenschutzbriefDto datenschutzbriefMassendrucksItem) {
    if (datenschutzbriefMassendrucksItem != null && this.datenschutzbriefMassendrucks != null) {
      this.datenschutzbriefMassendrucks.remove(datenschutzbriefMassendrucksItem);
    }

    return this;
  }
  /**
   **/
  public MassendruckJobDetailDto verfuegungMassendrucks(List<MassendruckVerfuegungDto> verfuegungMassendrucks) {
    this.verfuegungMassendrucks = verfuegungMassendrucks;
    return this;
  }

  
  @JsonProperty("verfuegungMassendrucks")
  public List<MassendruckVerfuegungDto> getVerfuegungMassendrucks() {
    return verfuegungMassendrucks;
  }

  @JsonProperty("verfuegungMassendrucks")
  public void setVerfuegungMassendrucks(List<MassendruckVerfuegungDto> verfuegungMassendrucks) {
    this.verfuegungMassendrucks = verfuegungMassendrucks;
  }

  public MassendruckJobDetailDto addVerfuegungMassendrucksItem(MassendruckVerfuegungDto verfuegungMassendrucksItem) {
    if (this.verfuegungMassendrucks == null) {
      this.verfuegungMassendrucks = new ArrayList<>();
    }

    this.verfuegungMassendrucks.add(verfuegungMassendrucksItem);
    return this;
  }

  public MassendruckJobDetailDto removeVerfuegungMassendrucksItem(MassendruckVerfuegungDto verfuegungMassendrucksItem) {
    if (verfuegungMassendrucksItem != null && this.verfuegungMassendrucks != null) {
      this.verfuegungMassendrucks.remove(verfuegungMassendrucksItem);
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
    MassendruckJobDetailDto massendruckJobDetail = (MassendruckJobDetailDto) o;
    return Objects.equals(this.id, massendruckJobDetail.id) &&
        Objects.equals(this.userErstellt, massendruckJobDetail.userErstellt) &&
        Objects.equals(this.timestampErstellt, massendruckJobDetail.timestampErstellt) &&
        Objects.equals(this.massendruckJobStatus, massendruckJobDetail.massendruckJobStatus) &&
        Objects.equals(this.massendruckJobTyp, massendruckJobDetail.massendruckJobTyp) &&
        Objects.equals(this.datenschutzbriefMassendrucks, massendruckJobDetail.datenschutzbriefMassendrucks) &&
        Objects.equals(this.verfuegungMassendrucks, massendruckJobDetail.verfuegungMassendrucks);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userErstellt, timestampErstellt, massendruckJobStatus, massendruckJobTyp, datenschutzbriefMassendrucks, verfuegungMassendrucks);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MassendruckJobDetailDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    userErstellt: ").append(toIndentedString(userErstellt)).append("\n");
    sb.append("    timestampErstellt: ").append(toIndentedString(timestampErstellt)).append("\n");
    sb.append("    massendruckJobStatus: ").append(toIndentedString(massendruckJobStatus)).append("\n");
    sb.append("    massendruckJobTyp: ").append(toIndentedString(massendruckJobTyp)).append("\n");
    sb.append("    datenschutzbriefMassendrucks: ").append(toIndentedString(datenschutzbriefMassendrucks)).append("\n");
    sb.append("    verfuegungMassendrucks: ").append(toIndentedString(verfuegungMassendrucks)).append("\n");
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

