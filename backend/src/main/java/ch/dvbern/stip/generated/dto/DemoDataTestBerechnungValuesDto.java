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



@JsonTypeName("DemoDataTestBerechnungValues")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DemoDataTestBerechnungValuesDto  implements Serializable {
  private @Valid ch.dvbern.stip.api.verfuegung.type.VerfuegungStatus status;
  private @Valid Integer ungekuerztStipendien;
  private @Valid Integer ungekuerztDarlehen;
  private @Valid Integer stipendien;
  private @Valid Integer darlehen;

  /**
   **/
  public DemoDataTestBerechnungValuesDto status(ch.dvbern.stip.api.verfuegung.type.VerfuegungStatus status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  public ch.dvbern.stip.api.verfuegung.type.VerfuegungStatus getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(ch.dvbern.stip.api.verfuegung.type.VerfuegungStatus status) {
    this.status = status;
  }

  /**
   **/
  public DemoDataTestBerechnungValuesDto ungekuerztStipendien(Integer ungekuerztStipendien) {
    this.ungekuerztStipendien = ungekuerztStipendien;
    return this;
  }

  
  @JsonProperty("ungekuerztStipendien")
  public Integer getUngekuerztStipendien() {
    return ungekuerztStipendien;
  }

  @JsonProperty("ungekuerztStipendien")
  public void setUngekuerztStipendien(Integer ungekuerztStipendien) {
    this.ungekuerztStipendien = ungekuerztStipendien;
  }

  /**
   **/
  public DemoDataTestBerechnungValuesDto ungekuerztDarlehen(Integer ungekuerztDarlehen) {
    this.ungekuerztDarlehen = ungekuerztDarlehen;
    return this;
  }

  
  @JsonProperty("ungekuerztDarlehen")
  public Integer getUngekuerztDarlehen() {
    return ungekuerztDarlehen;
  }

  @JsonProperty("ungekuerztDarlehen")
  public void setUngekuerztDarlehen(Integer ungekuerztDarlehen) {
    this.ungekuerztDarlehen = ungekuerztDarlehen;
  }

  /**
   **/
  public DemoDataTestBerechnungValuesDto stipendien(Integer stipendien) {
    this.stipendien = stipendien;
    return this;
  }

  
  @JsonProperty("stipendien")
  public Integer getStipendien() {
    return stipendien;
  }

  @JsonProperty("stipendien")
  public void setStipendien(Integer stipendien) {
    this.stipendien = stipendien;
  }

  /**
   **/
  public DemoDataTestBerechnungValuesDto darlehen(Integer darlehen) {
    this.darlehen = darlehen;
    return this;
  }

  
  @JsonProperty("darlehen")
  public Integer getDarlehen() {
    return darlehen;
  }

  @JsonProperty("darlehen")
  public void setDarlehen(Integer darlehen) {
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
    DemoDataTestBerechnungValuesDto demoDataTestBerechnungValues = (DemoDataTestBerechnungValuesDto) o;
    return Objects.equals(this.status, demoDataTestBerechnungValues.status) &&
        Objects.equals(this.ungekuerztStipendien, demoDataTestBerechnungValues.ungekuerztStipendien) &&
        Objects.equals(this.ungekuerztDarlehen, demoDataTestBerechnungValues.ungekuerztDarlehen) &&
        Objects.equals(this.stipendien, demoDataTestBerechnungValues.stipendien) &&
        Objects.equals(this.darlehen, demoDataTestBerechnungValues.darlehen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, ungekuerztStipendien, ungekuerztDarlehen, stipendien, darlehen);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoDataTestBerechnungValuesDto {\n");
    
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

