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



@JsonTypeName("DemoDataTestBerechnungValid")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DemoDataTestBerechnungValidDto  implements Serializable {
  private @Valid Boolean status;
  private @Valid Boolean ungekuerztStipendien;
  private @Valid Boolean ungekuerztDarlehen;
  private @Valid Boolean stipendien;
  private @Valid Boolean darlehen;

  /**
   **/
  public DemoDataTestBerechnungValidDto status(Boolean status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  public Boolean getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(Boolean status) {
    this.status = status;
  }

  /**
   **/
  public DemoDataTestBerechnungValidDto ungekuerztStipendien(Boolean ungekuerztStipendien) {
    this.ungekuerztStipendien = ungekuerztStipendien;
    return this;
  }

  
  @JsonProperty("ungekuerztStipendien")
  public Boolean getUngekuerztStipendien() {
    return ungekuerztStipendien;
  }

  @JsonProperty("ungekuerztStipendien")
  public void setUngekuerztStipendien(Boolean ungekuerztStipendien) {
    this.ungekuerztStipendien = ungekuerztStipendien;
  }

  /**
   **/
  public DemoDataTestBerechnungValidDto ungekuerztDarlehen(Boolean ungekuerztDarlehen) {
    this.ungekuerztDarlehen = ungekuerztDarlehen;
    return this;
  }

  
  @JsonProperty("ungekuerztDarlehen")
  public Boolean getUngekuerztDarlehen() {
    return ungekuerztDarlehen;
  }

  @JsonProperty("ungekuerztDarlehen")
  public void setUngekuerztDarlehen(Boolean ungekuerztDarlehen) {
    this.ungekuerztDarlehen = ungekuerztDarlehen;
  }

  /**
   **/
  public DemoDataTestBerechnungValidDto stipendien(Boolean stipendien) {
    this.stipendien = stipendien;
    return this;
  }

  
  @JsonProperty("stipendien")
  public Boolean getStipendien() {
    return stipendien;
  }

  @JsonProperty("stipendien")
  public void setStipendien(Boolean stipendien) {
    this.stipendien = stipendien;
  }

  /**
   **/
  public DemoDataTestBerechnungValidDto darlehen(Boolean darlehen) {
    this.darlehen = darlehen;
    return this;
  }

  
  @JsonProperty("darlehen")
  public Boolean getDarlehen() {
    return darlehen;
  }

  @JsonProperty("darlehen")
  public void setDarlehen(Boolean darlehen) {
    this.darlehen = darlehen;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoDataTestBerechnungValidDto demoDataTestBerechnungValid = (DemoDataTestBerechnungValidDto) o;
    return Objects.equals(this.status, demoDataTestBerechnungValid.status) &&
        Objects.equals(this.ungekuerztStipendien, demoDataTestBerechnungValid.ungekuerztStipendien) &&
        Objects.equals(this.ungekuerztDarlehen, demoDataTestBerechnungValid.ungekuerztDarlehen) &&
        Objects.equals(this.stipendien, demoDataTestBerechnungValid.stipendien) &&
        Objects.equals(this.darlehen, demoDataTestBerechnungValid.darlehen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, ungekuerztStipendien, ungekuerztDarlehen, stipendien, darlehen);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoDataTestBerechnungValidDto {\n");
    
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    ungekuerztStipendien: ").append(toIndentedString(ungekuerztStipendien)).append("\n");
    sb.append("    ungekuerztDarlehen: ").append(toIndentedString(ungekuerztDarlehen)).append("\n");
    sb.append("    stipendien: ").append(toIndentedString(stipendien)).append("\n");
    sb.append("    darlehen: ").append(toIndentedString(darlehen)).append("\n");
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

