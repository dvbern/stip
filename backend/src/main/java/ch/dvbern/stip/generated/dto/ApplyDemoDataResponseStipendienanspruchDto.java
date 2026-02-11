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



@JsonTypeName("ApplyDemoDataResponse_stipendienanspruch")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class ApplyDemoDataResponseStipendienanspruchDto  implements Serializable {
  private @Valid Boolean success;
  private @Valid ch.dvbern.stip.api.verfuegung.type.VerfuegungStatus statusSoll;
  private @Valid ch.dvbern.stip.api.verfuegung.type.VerfuegungStatus statusIst;
  private @Valid Integer betragStipendienSoll;
  private @Valid Integer betragStipendienIst;
  private @Valid Integer betragDarlehenSoll;
  private @Valid Integer betragDarlehenIst;

  /**
   **/
  public ApplyDemoDataResponseStipendienanspruchDto success(Boolean success) {
    this.success = success;
    return this;
  }

  
  @JsonProperty("success")
  @NotNull
  public Boolean getSuccess() {
    return success;
  }

  @JsonProperty("success")
  public void setSuccess(Boolean success) {
    this.success = success;
  }

  /**
   **/
  public ApplyDemoDataResponseStipendienanspruchDto statusSoll(ch.dvbern.stip.api.verfuegung.type.VerfuegungStatus statusSoll) {
    this.statusSoll = statusSoll;
    return this;
  }

  
  @JsonProperty("statusSoll")
  public ch.dvbern.stip.api.verfuegung.type.VerfuegungStatus getStatusSoll() {
    return statusSoll;
  }

  @JsonProperty("statusSoll")
  public void setStatusSoll(ch.dvbern.stip.api.verfuegung.type.VerfuegungStatus statusSoll) {
    this.statusSoll = statusSoll;
  }

  /**
   **/
  public ApplyDemoDataResponseStipendienanspruchDto statusIst(ch.dvbern.stip.api.verfuegung.type.VerfuegungStatus statusIst) {
    this.statusIst = statusIst;
    return this;
  }

  
  @JsonProperty("statusIst")
  public ch.dvbern.stip.api.verfuegung.type.VerfuegungStatus getStatusIst() {
    return statusIst;
  }

  @JsonProperty("statusIst")
  public void setStatusIst(ch.dvbern.stip.api.verfuegung.type.VerfuegungStatus statusIst) {
    this.statusIst = statusIst;
  }

  /**
   **/
  public ApplyDemoDataResponseStipendienanspruchDto betragStipendienSoll(Integer betragStipendienSoll) {
    this.betragStipendienSoll = betragStipendienSoll;
    return this;
  }

  
  @JsonProperty("betragStipendienSoll")
  public Integer getBetragStipendienSoll() {
    return betragStipendienSoll;
  }

  @JsonProperty("betragStipendienSoll")
  public void setBetragStipendienSoll(Integer betragStipendienSoll) {
    this.betragStipendienSoll = betragStipendienSoll;
  }

  /**
   **/
  public ApplyDemoDataResponseStipendienanspruchDto betragStipendienIst(Integer betragStipendienIst) {
    this.betragStipendienIst = betragStipendienIst;
    return this;
  }

  
  @JsonProperty("betragStipendienIst")
  public Integer getBetragStipendienIst() {
    return betragStipendienIst;
  }

  @JsonProperty("betragStipendienIst")
  public void setBetragStipendienIst(Integer betragStipendienIst) {
    this.betragStipendienIst = betragStipendienIst;
  }

  /**
   **/
  public ApplyDemoDataResponseStipendienanspruchDto betragDarlehenSoll(Integer betragDarlehenSoll) {
    this.betragDarlehenSoll = betragDarlehenSoll;
    return this;
  }

  
  @JsonProperty("betragDarlehenSoll")
  public Integer getBetragDarlehenSoll() {
    return betragDarlehenSoll;
  }

  @JsonProperty("betragDarlehenSoll")
  public void setBetragDarlehenSoll(Integer betragDarlehenSoll) {
    this.betragDarlehenSoll = betragDarlehenSoll;
  }

  /**
   **/
  public ApplyDemoDataResponseStipendienanspruchDto betragDarlehenIst(Integer betragDarlehenIst) {
    this.betragDarlehenIst = betragDarlehenIst;
    return this;
  }

  
  @JsonProperty("betragDarlehenIst")
  public Integer getBetragDarlehenIst() {
    return betragDarlehenIst;
  }

  @JsonProperty("betragDarlehenIst")
  public void setBetragDarlehenIst(Integer betragDarlehenIst) {
    this.betragDarlehenIst = betragDarlehenIst;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplyDemoDataResponseStipendienanspruchDto applyDemoDataResponseStipendienanspruch = (ApplyDemoDataResponseStipendienanspruchDto) o;
    return Objects.equals(this.success, applyDemoDataResponseStipendienanspruch.success) &&
        Objects.equals(this.statusSoll, applyDemoDataResponseStipendienanspruch.statusSoll) &&
        Objects.equals(this.statusIst, applyDemoDataResponseStipendienanspruch.statusIst) &&
        Objects.equals(this.betragStipendienSoll, applyDemoDataResponseStipendienanspruch.betragStipendienSoll) &&
        Objects.equals(this.betragStipendienIst, applyDemoDataResponseStipendienanspruch.betragStipendienIst) &&
        Objects.equals(this.betragDarlehenSoll, applyDemoDataResponseStipendienanspruch.betragDarlehenSoll) &&
        Objects.equals(this.betragDarlehenIst, applyDemoDataResponseStipendienanspruch.betragDarlehenIst);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success, statusSoll, statusIst, betragStipendienSoll, betragStipendienIst, betragDarlehenSoll, betragDarlehenIst);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplyDemoDataResponseStipendienanspruchDto {\n");
    
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
    sb.append("    statusSoll: ").append(toIndentedString(statusSoll)).append("\n");
    sb.append("    statusIst: ").append(toIndentedString(statusIst)).append("\n");
    sb.append("    betragStipendienSoll: ").append(toIndentedString(betragStipendienSoll)).append("\n");
    sb.append("    betragStipendienIst: ").append(toIndentedString(betragStipendienIst)).append("\n");
    sb.append("    betragDarlehenSoll: ").append(toIndentedString(betragDarlehenSoll)).append("\n");
    sb.append("    betragDarlehenIst: ").append(toIndentedString(betragDarlehenIst)).append("\n");
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

