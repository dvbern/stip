package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonTypeName("CreateAuszahlungKreditor")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class CreateAuszahlungKreditorDto  implements Serializable {
  private @Valid Integer deliveryId;
  private @Valid Integer extId;
  private @Valid AuszahlungDto auszahlung;

  /**
   **/
  public CreateAuszahlungKreditorDto deliveryId(Integer deliveryId) {
    this.deliveryId = deliveryId;
    return this;
  }


  @JsonProperty("deliveryId")
  @NotNull
  public Integer getDeliveryId() {
    return deliveryId;
  }

  @JsonProperty("deliveryId")
  public void setDeliveryId(Integer deliveryId) {
    this.deliveryId = deliveryId;
  }

  /**
   **/
  public CreateAuszahlungKreditorDto extId(Integer extId) {
    this.extId = extId;
    return this;
  }


  @JsonProperty("extId")
  @NotNull
  public Integer getExtId() {
    return extId;
  }

  @JsonProperty("extId")
  public void setExtId(Integer extId) {
    this.extId = extId;
  }

  /**
   **/
  public CreateAuszahlungKreditorDto auszahlung(AuszahlungDto auszahlung) {
    this.auszahlung = auszahlung;
    return this;
  }


  @JsonProperty("auszahlung")
  @NotNull
  public AuszahlungDto getAuszahlung() {
    return auszahlung;
  }

  @JsonProperty("auszahlung")
  public void setAuszahlung(AuszahlungDto auszahlung) {
    this.auszahlung = auszahlung;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateAuszahlungKreditorDto createAuszahlungKreditor = (CreateAuszahlungKreditorDto) o;
    return Objects.equals(this.deliveryId, createAuszahlungKreditor.deliveryId) &&
        Objects.equals(this.extId, createAuszahlungKreditor.extId) &&
        Objects.equals(this.auszahlung, createAuszahlungKreditor.auszahlung);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deliveryId, extId, auszahlung);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateAuszahlungKreditorDto {\n");

    sb.append("    deliveryId: ").append(toIndentedString(deliveryId)).append("\n");
    sb.append("    extId: ").append(toIndentedString(extId)).append("\n");
    sb.append("    auszahlung: ").append(toIndentedString(auszahlung)).append("\n");
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

