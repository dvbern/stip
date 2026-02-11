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



@JsonTypeName("DemoData_stipendienanspruch")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DemoDataStipendienanspruchDto  implements Serializable {
  private @Valid ch.dvbern.stip.api.verfuegung.type.VerfuegungStatus status;
  private @Valid Integer betragStipendienSoll;
  private @Valid Integer betragStipendienIst;
  private @Valid Integer betragDarlehenSoll;
  private @Valid Integer betragDarlehenIst;

  /**
   **/
  public DemoDataStipendienanspruchDto status(ch.dvbern.stip.api.verfuegung.type.VerfuegungStatus status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  @NotNull
  public ch.dvbern.stip.api.verfuegung.type.VerfuegungStatus getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(ch.dvbern.stip.api.verfuegung.type.VerfuegungStatus status) {
    this.status = status;
  }

  /**
   **/
  public DemoDataStipendienanspruchDto betragStipendienSoll(Integer betragStipendienSoll) {
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
  public DemoDataStipendienanspruchDto betragStipendienIst(Integer betragStipendienIst) {
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
  public DemoDataStipendienanspruchDto betragDarlehenSoll(Integer betragDarlehenSoll) {
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
  public DemoDataStipendienanspruchDto betragDarlehenIst(Integer betragDarlehenIst) {
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
    DemoDataStipendienanspruchDto demoDataStipendienanspruch = (DemoDataStipendienanspruchDto) o;
    return Objects.equals(this.status, demoDataStipendienanspruch.status) &&
        Objects.equals(this.betragStipendienSoll, demoDataStipendienanspruch.betragStipendienSoll) &&
        Objects.equals(this.betragStipendienIst, demoDataStipendienanspruch.betragStipendienIst) &&
        Objects.equals(this.betragDarlehenSoll, demoDataStipendienanspruch.betragDarlehenSoll) &&
        Objects.equals(this.betragDarlehenIst, demoDataStipendienanspruch.betragDarlehenIst);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, betragStipendienSoll, betragStipendienIst, betragDarlehenSoll, betragDarlehenIst);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoDataStipendienanspruchDto {\n");
    
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

