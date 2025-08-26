package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("SapDelivery")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class SapDeliveryDto  implements Serializable {
  private @Valid String sapId;
  private @Valid java.time.LocalDateTime timestampErstellt;
  private @Valid ch.dvbern.stip.api.buchhaltung.type.SapStatus sapStatus;

  /**
   **/
  public SapDeliveryDto sapId(String sapId) {
    this.sapId = sapId;
    return this;
  }

  
  @JsonProperty("sapId")
  @NotNull
  public String getSapId() {
    return sapId;
  }

  @JsonProperty("sapId")
  public void setSapId(String sapId) {
    this.sapId = sapId;
  }

  /**
   **/
  public SapDeliveryDto timestampErstellt(java.time.LocalDateTime timestampErstellt) {
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
  public SapDeliveryDto sapStatus(ch.dvbern.stip.api.buchhaltung.type.SapStatus sapStatus) {
    this.sapStatus = sapStatus;
    return this;
  }

  
  @JsonProperty("sapStatus")
  @NotNull
  public ch.dvbern.stip.api.buchhaltung.type.SapStatus getSapStatus() {
    return sapStatus;
  }

  @JsonProperty("sapStatus")
  public void setSapStatus(ch.dvbern.stip.api.buchhaltung.type.SapStatus sapStatus) {
    this.sapStatus = sapStatus;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SapDeliveryDto sapDelivery = (SapDeliveryDto) o;
    return Objects.equals(this.sapId, sapDelivery.sapId) &&
        Objects.equals(this.timestampErstellt, sapDelivery.timestampErstellt) &&
        Objects.equals(this.sapStatus, sapDelivery.sapStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sapId, timestampErstellt, sapStatus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SapDeliveryDto {\n");
    
    sb.append("    sapId: ").append(toIndentedString(sapId)).append("\n");
    sb.append("    timestampErstellt: ").append(toIndentedString(timestampErstellt)).append("\n");
    sb.append("    sapStatus: ").append(toIndentedString(sapStatus)).append("\n");
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

