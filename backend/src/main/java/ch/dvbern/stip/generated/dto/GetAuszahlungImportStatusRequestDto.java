package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonTypeName("GetAuszahlungImportStatusRequest")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GetAuszahlungImportStatusRequestDto  implements Serializable {
  private @Valid String deliveryId;
  private @Valid String sysId;

  /**
   **/
  public GetAuszahlungImportStatusRequestDto deliveryId(String deliveryId) {
    this.deliveryId = deliveryId;
    return this;
  }


  @JsonProperty("deliveryId")
  @NotNull
  public String getDeliveryId() {
    return deliveryId;
  }

  @JsonProperty("deliveryId")
  public void setDeliveryId(String deliveryId) {
    this.deliveryId = deliveryId;
  }

  /**
   **/
  public GetAuszahlungImportStatusRequestDto sysId(String sysId) {
    this.sysId = sysId;
    return this;
  }


  @JsonProperty("sysId")
  public String getSysId() {
    return sysId;
  }

  @JsonProperty("sysId")
  public void setSysId(String sysId) {
    this.sysId = sysId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAuszahlungImportStatusRequestDto getAuszahlungImportStatusRequest = (GetAuszahlungImportStatusRequestDto) o;
    return Objects.equals(this.deliveryId, getAuszahlungImportStatusRequest.deliveryId) &&
        Objects.equals(this.sysId, getAuszahlungImportStatusRequest.sysId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deliveryId, sysId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAuszahlungImportStatusRequestDto {\n");

    sb.append("    deliveryId: ").append(toIndentedString(deliveryId)).append("\n");
    sb.append("    sysId: ").append(toIndentedString(sysId)).append("\n");
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

