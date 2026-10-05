package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DemoDataTestBerechnungDetailsDto;
import ch.dvbern.stip.generated.dto.DemoDataTestBerechnungValidDto;
import ch.dvbern.stip.generated.dto.DemoDataTestBerechnungValuesDto;
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



@JsonTypeName("DemoDataTestBerechnungResultat")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class DemoDataTestBerechnungResultatDto  implements Serializable {
  private UUID demoDataId;
  private String testFall;
  private DemoDataTestBerechnungValidDto valid;
  private String message;
  private DemoDataTestBerechnungValuesDto sollValues;
  private DemoDataTestBerechnungValuesDto istValues;
  private DemoDataTestBerechnungDetailsDto sollDetails;
  private DemoDataTestBerechnungDetailsDto istDetails;

  protected DemoDataTestBerechnungResultatDto(DemoDataTestBerechnungResultatDtoBuilder<?, ?> b) {
    this.demoDataId = b.demoDataId;
    this.testFall = b.testFall;
    this.valid = b.valid;
    this.message = b.message;
    this.sollValues = b.sollValues;
    this.istValues = b.istValues;
    this.sollDetails = b.sollDetails;
    this.istDetails = b.istDetails;
  }

  public DemoDataTestBerechnungResultatDto() {
  }

  /**
   **/
  public DemoDataTestBerechnungResultatDto demoDataId(UUID demoDataId) {
    this.demoDataId = demoDataId;
    return this;
  }

  
  @JsonProperty(required = true, value = "demoDataId")
  @NotNull public UUID getDemoDataId() {
    return demoDataId;
  }

  @JsonProperty(required = true, value = "demoDataId")
  public void setDemoDataId(UUID demoDataId) {
    this.demoDataId = demoDataId;
  }

  /**
   **/
  public DemoDataTestBerechnungResultatDto testFall(String testFall) {
    this.testFall = testFall;
    return this;
  }

  
  @JsonProperty(required = true, value = "testFall")
  @NotNull public String getTestFall() {
    return testFall;
  }

  @JsonProperty(required = true, value = "testFall")
  public void setTestFall(String testFall) {
    this.testFall = testFall;
  }

  /**
   **/
  public DemoDataTestBerechnungResultatDto valid(DemoDataTestBerechnungValidDto valid) {
    this.valid = valid;
    return this;
  }

  
  @JsonProperty("valid")
  @Valid public DemoDataTestBerechnungValidDto getValid() {
    return valid;
  }

  @JsonProperty("valid")
  public void setValid(DemoDataTestBerechnungValidDto valid) {
    this.valid = valid;
  }

  /**
   **/
  public DemoDataTestBerechnungResultatDto message(String message) {
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

  /**
   **/
  public DemoDataTestBerechnungResultatDto sollValues(DemoDataTestBerechnungValuesDto sollValues) {
    this.sollValues = sollValues;
    return this;
  }

  
  @JsonProperty("sollValues")
  @Valid public DemoDataTestBerechnungValuesDto getSollValues() {
    return sollValues;
  }

  @JsonProperty("sollValues")
  public void setSollValues(DemoDataTestBerechnungValuesDto sollValues) {
    this.sollValues = sollValues;
  }

  /**
   **/
  public DemoDataTestBerechnungResultatDto istValues(DemoDataTestBerechnungValuesDto istValues) {
    this.istValues = istValues;
    return this;
  }

  
  @JsonProperty("istValues")
  @Valid public DemoDataTestBerechnungValuesDto getIstValues() {
    return istValues;
  }

  @JsonProperty("istValues")
  public void setIstValues(DemoDataTestBerechnungValuesDto istValues) {
    this.istValues = istValues;
  }

  /**
   **/
  public DemoDataTestBerechnungResultatDto sollDetails(DemoDataTestBerechnungDetailsDto sollDetails) {
    this.sollDetails = sollDetails;
    return this;
  }

  
  @JsonProperty("sollDetails")
  @Valid public DemoDataTestBerechnungDetailsDto getSollDetails() {
    return sollDetails;
  }

  @JsonProperty("sollDetails")
  public void setSollDetails(DemoDataTestBerechnungDetailsDto sollDetails) {
    this.sollDetails = sollDetails;
  }

  /**
   **/
  public DemoDataTestBerechnungResultatDto istDetails(DemoDataTestBerechnungDetailsDto istDetails) {
    this.istDetails = istDetails;
    return this;
  }

  
  @JsonProperty("istDetails")
  @Valid public DemoDataTestBerechnungDetailsDto getIstDetails() {
    return istDetails;
  }

  @JsonProperty("istDetails")
  public void setIstDetails(DemoDataTestBerechnungDetailsDto istDetails) {
    this.istDetails = istDetails;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoDataTestBerechnungResultatDto demoDataTestBerechnungResultat = (DemoDataTestBerechnungResultatDto) o;
    return Objects.equals(this.demoDataId, demoDataTestBerechnungResultat.demoDataId) &&
        Objects.equals(this.testFall, demoDataTestBerechnungResultat.testFall) &&
        Objects.equals(this.valid, demoDataTestBerechnungResultat.valid) &&
        Objects.equals(this.message, demoDataTestBerechnungResultat.message) &&
        Objects.equals(this.sollValues, demoDataTestBerechnungResultat.sollValues) &&
        Objects.equals(this.istValues, demoDataTestBerechnungResultat.istValues) &&
        Objects.equals(this.sollDetails, demoDataTestBerechnungResultat.sollDetails) &&
        Objects.equals(this.istDetails, demoDataTestBerechnungResultat.istDetails);
  }

  @Override
  public int hashCode() {
    return Objects.hash(demoDataId, testFall, valid, message, sollValues, istValues, sollDetails, istDetails);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoDataTestBerechnungResultatDto {\n");
    
    sb.append("    demoDataId: ").append(toIndentedString(demoDataId)).append("\n");
    sb.append("    testFall: ").append(toIndentedString(testFall)).append("\n");
    sb.append("    valid: ").append(toIndentedString(valid)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    sollValues: ").append(toIndentedString(sollValues)).append("\n");
    sb.append("    istValues: ").append(toIndentedString(istValues)).append("\n");
    sb.append("    sollDetails: ").append(toIndentedString(sollDetails)).append("\n");
    sb.append("    istDetails: ").append(toIndentedString(istDetails)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }


  public static DemoDataTestBerechnungResultatDtoBuilder<?, ?> builder() {
    return new DemoDataTestBerechnungResultatDtoBuilderImpl();
  }

  private static final class DemoDataTestBerechnungResultatDtoBuilderImpl extends DemoDataTestBerechnungResultatDtoBuilder<DemoDataTestBerechnungResultatDto, DemoDataTestBerechnungResultatDtoBuilderImpl> {

    @Override
    protected DemoDataTestBerechnungResultatDtoBuilderImpl self() {
      return this;
    }

    @Override
    public DemoDataTestBerechnungResultatDto build() {
      return new DemoDataTestBerechnungResultatDto(this);
    }
  }

  public static abstract class DemoDataTestBerechnungResultatDtoBuilder<C extends DemoDataTestBerechnungResultatDto, B extends DemoDataTestBerechnungResultatDtoBuilder<C, B>>  {
    private UUID demoDataId;
    private String testFall;
    private DemoDataTestBerechnungValidDto valid;
    private String message;
    private DemoDataTestBerechnungValuesDto sollValues;
    private DemoDataTestBerechnungValuesDto istValues;
    private DemoDataTestBerechnungDetailsDto sollDetails;
    private DemoDataTestBerechnungDetailsDto istDetails;
    protected abstract B self();

    public abstract C build();

    public B demoDataId(UUID demoDataId) {
      this.demoDataId = demoDataId;
      return self();
    }
    public B testFall(String testFall) {
      this.testFall = testFall;
      return self();
    }
    public B valid(DemoDataTestBerechnungValidDto valid) {
      this.valid = valid;
      return self();
    }
    public B message(String message) {
      this.message = message;
      return self();
    }
    public B sollValues(DemoDataTestBerechnungValuesDto sollValues) {
      this.sollValues = sollValues;
      return self();
    }
    public B istValues(DemoDataTestBerechnungValuesDto istValues) {
      this.istValues = istValues;
      return self();
    }
    public B sollDetails(DemoDataTestBerechnungDetailsDto sollDetails) {
      this.sollDetails = sollDetails;
      return self();
    }
    public B istDetails(DemoDataTestBerechnungDetailsDto istDetails) {
      this.istDetails = istDetails;
      return self();
    }
  }
}
