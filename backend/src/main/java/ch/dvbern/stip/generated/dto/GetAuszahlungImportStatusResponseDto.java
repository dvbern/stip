package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonTypeName("GetAuszahlungImportStatusResponse")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GetAuszahlungImportStatusResponseDto  implements Serializable {
  private @Valid String deliveryId;
  private @Valid String message;

  /**
   **/
  public GetAuszahlungImportStatusResponseDto deliveryId(String deliveryId) {
    this.deliveryId = deliveryId;
    return this;
  }


  @JsonProperty("deliveryId")
  public String getDeliveryId() {
    return deliveryId;
  }

  @JsonProperty("deliveryId")
  public void setDeliveryId(String deliveryId) {
    this.deliveryId = deliveryId;
  }

  /**
   **/
  public GetAuszahlungImportStatusResponseDto message(String message) {
    this.message = message;
    return this;
  }


  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  @JsonProperty("message")
  public void setMessage(String message) {
    this.message = message;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAuszahlungImportStatusResponseDto getAuszahlungImportStatusResponse = (GetAuszahlungImportStatusResponseDto) o;
    return Objects.equals(this.deliveryId, getAuszahlungImportStatusResponse.deliveryId) &&
        Objects.equals(this.message, getAuszahlungImportStatusResponse.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deliveryId, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAuszahlungImportStatusResponseDto {\n");

    sb.append("    deliveryId: ").append(toIndentedString(deliveryId)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
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

