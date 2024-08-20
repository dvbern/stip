package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonTypeName("CreateAuszahlung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class CreateAuszahlungDto  implements Serializable {
  private @Valid Integer deliveryId;
  private @Valid Integer vendorNo;
  private @Valid AuszahlungDto auszahlung;
  private @Valid String positionsText;
  private @Valid String zahlungszweck;

  /**
   **/
  public CreateAuszahlungDto deliveryId(Integer deliveryId) {
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
  public CreateAuszahlungDto vendorNo(Integer vendorNo) {
    this.vendorNo = vendorNo;
    return this;
  }


  @JsonProperty("vendorNo")
  @NotNull
  public Integer getVendorNo() {
    return vendorNo;
  }

  @JsonProperty("vendorNo")
  public void setVendorNo(Integer vendorNo) {
    this.vendorNo = vendorNo;
  }

  /**
   **/
  public CreateAuszahlungDto auszahlung(AuszahlungDto auszahlung) {
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

  /**
   **/
  public CreateAuszahlungDto positionsText(String positionsText) {
    this.positionsText = positionsText;
    return this;
  }


  @JsonProperty("positionsText")
  public String getPositionsText() {
    return positionsText;
  }

  @JsonProperty("positionsText")
  public void setPositionsText(String positionsText) {
    this.positionsText = positionsText;
  }

  /**
   **/
  public CreateAuszahlungDto zahlungszweck(String zahlungszweck) {
    this.zahlungszweck = zahlungszweck;
    return this;
  }


  @JsonProperty("zahlungszweck")
  public String getZahlungszweck() {
    return zahlungszweck;
  }

  @JsonProperty("zahlungszweck")
  public void setZahlungszweck(String zahlungszweck) {
    this.zahlungszweck = zahlungszweck;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateAuszahlungDto createAuszahlung = (CreateAuszahlungDto) o;
    return Objects.equals(this.deliveryId, createAuszahlung.deliveryId) &&
        Objects.equals(this.vendorNo, createAuszahlung.vendorNo) &&
        Objects.equals(this.auszahlung, createAuszahlung.auszahlung) &&
        Objects.equals(this.positionsText, createAuszahlung.positionsText) &&
        Objects.equals(this.zahlungszweck, createAuszahlung.zahlungszweck);
  }

  @Override
  public int hashCode() {
    return Objects.hash(deliveryId, vendorNo, auszahlung, positionsText, zahlungszweck);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateAuszahlungDto {\n");

    sb.append("    deliveryId: ").append(toIndentedString(deliveryId)).append("\n");
    sb.append("    vendorNo: ").append(toIndentedString(vendorNo)).append("\n");
    sb.append("    auszahlung: ").append(toIndentedString(auszahlung)).append("\n");
    sb.append("    positionsText: ").append(toIndentedString(positionsText)).append("\n");
    sb.append("    zahlungszweck: ").append(toIndentedString(zahlungszweck)).append("\n");
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

