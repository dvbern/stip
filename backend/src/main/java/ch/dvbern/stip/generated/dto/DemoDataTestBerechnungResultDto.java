package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("DemoDataTestBerechnungResult")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DemoDataTestBerechnungResultDto  implements Serializable {
  private @Valid UUID demoDataId;
  private @Valid String testFall;
  private @Valid Boolean valid;
  private @Valid Integer soll;
  private @Valid Integer ist;
  private @Valid String message;

  /**
   **/
  public DemoDataTestBerechnungResultDto demoDataId(UUID demoDataId) {
    this.demoDataId = demoDataId;
    return this;
  }

  
  @JsonProperty("demoDataId")
  @NotNull
  public UUID getDemoDataId() {
    return demoDataId;
  }

  @JsonProperty("demoDataId")
  public void setDemoDataId(UUID demoDataId) {
    this.demoDataId = demoDataId;
  }

  /**
   **/
  public DemoDataTestBerechnungResultDto testFall(String testFall) {
    this.testFall = testFall;
    return this;
  }

  
  @JsonProperty("testFall")
  @NotNull
  public String getTestFall() {
    return testFall;
  }

  @JsonProperty("testFall")
  public void setTestFall(String testFall) {
    this.testFall = testFall;
  }

  /**
   **/
  public DemoDataTestBerechnungResultDto valid(Boolean valid) {
    this.valid = valid;
    return this;
  }

  
  @JsonProperty("valid")
  @NotNull
  public Boolean getValid() {
    return valid;
  }

  @JsonProperty("valid")
  public void setValid(Boolean valid) {
    this.valid = valid;
  }

  /**
   **/
  public DemoDataTestBerechnungResultDto soll(Integer soll) {
    this.soll = soll;
    return this;
  }

  
  @JsonProperty("soll")
  public Integer getSoll() {
    return soll;
  }

  @JsonProperty("soll")
  public void setSoll(Integer soll) {
    this.soll = soll;
  }

  /**
   **/
  public DemoDataTestBerechnungResultDto ist(Integer ist) {
    this.ist = ist;
    return this;
  }

  
  @JsonProperty("ist")
  public Integer getIst() {
    return ist;
  }

  @JsonProperty("ist")
  public void setIst(Integer ist) {
    this.ist = ist;
  }

  /**
   **/
  public DemoDataTestBerechnungResultDto message(String message) {
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
    DemoDataTestBerechnungResultDto demoDataTestBerechnungResult = (DemoDataTestBerechnungResultDto) o;
    return Objects.equals(this.demoDataId, demoDataTestBerechnungResult.demoDataId) &&
        Objects.equals(this.testFall, demoDataTestBerechnungResult.testFall) &&
        Objects.equals(this.valid, demoDataTestBerechnungResult.valid) &&
        Objects.equals(this.soll, demoDataTestBerechnungResult.soll) &&
        Objects.equals(this.ist, demoDataTestBerechnungResult.ist) &&
        Objects.equals(this.message, demoDataTestBerechnungResult.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(demoDataId, testFall, valid, soll, ist, message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoDataTestBerechnungResultDto {\n");
    
    sb.append("    demoDataId: ").append(toIndentedString(demoDataId)).append("\n");
    sb.append("    testFall: ").append(toIndentedString(testFall)).append("\n");
    sb.append("    valid: ").append(toIndentedString(valid)).append("\n");
    sb.append("    soll: ").append(toIndentedString(soll)).append("\n");
    sb.append("    ist: ").append(toIndentedString(ist)).append("\n");
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

