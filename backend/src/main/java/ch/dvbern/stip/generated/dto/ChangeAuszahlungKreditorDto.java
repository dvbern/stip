package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AuszahlungDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("ChangeAuszahlungKreditor")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class ChangeAuszahlungKreditorDto  implements Serializable {
  private @Valid Integer businessPartnerId;
  private @Valid Integer deliveryId;
  private @Valid Integer extId;
  private @Valid AuszahlungDto auszahlung;

  /**
   **/
  public ChangeAuszahlungKreditorDto businessPartnerId(Integer businessPartnerId) {
    this.businessPartnerId = businessPartnerId;
    return this;
  }

  
  @JsonProperty("businessPartnerId")
  @NotNull
  public Integer getBusinessPartnerId() {
    return businessPartnerId;
  }

  @JsonProperty("businessPartnerId")
  public void setBusinessPartnerId(Integer businessPartnerId) {
    this.businessPartnerId = businessPartnerId;
  }

  /**
   **/
  public ChangeAuszahlungKreditorDto deliveryId(Integer deliveryId) {
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
  public ChangeAuszahlungKreditorDto extId(Integer extId) {
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
  public ChangeAuszahlungKreditorDto auszahlung(AuszahlungDto auszahlung) {
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
    ChangeAuszahlungKreditorDto changeAuszahlungKreditor = (ChangeAuszahlungKreditorDto) o;
    return Objects.equals(this.businessPartnerId, changeAuszahlungKreditor.businessPartnerId) &&
        Objects.equals(this.deliveryId, changeAuszahlungKreditor.deliveryId) &&
        Objects.equals(this.extId, changeAuszahlungKreditor.extId) &&
        Objects.equals(this.auszahlung, changeAuszahlungKreditor.auszahlung);
  }

  @Override
  public int hashCode() {
    return Objects.hash(businessPartnerId, deliveryId, extId, auszahlung);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ChangeAuszahlungKreditorDto {\n");
    
    sb.append("    businessPartnerId: ").append(toIndentedString(businessPartnerId)).append("\n");
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

